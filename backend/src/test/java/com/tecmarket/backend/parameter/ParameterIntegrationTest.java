package com.tecmarket.backend.parameter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecmarket.backend.permission.entity.Role;
import com.tecmarket.backend.permission.repository.RoleRepository;
import com.tecmarket.backend.support.IntegrationTestBase;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.entity.DocumentType;
import com.tecmarket.backend.user.entity.UserRole;
import com.tecmarket.backend.user.repository.AppUserRepository;
import com.tecmarket.backend.user.repository.DocumentTypeRepository;
import com.tecmarket.backend.user.repository.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ParameterIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Test
    void documentTypesPublicCatalogIsAccessible() throws Exception {
        mockMvc.perform(get("/api/v1/parameters/document-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void roleCrudEndpointsWorkWithProperAuthority() throws Exception {
        String code = "QA_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String payload = """
                {
                  "code":"%s",
                  "name":"Rol QA",
                  "description":"Rol creado desde test de integracion",
                  "scope":"GLOBAL"
                }
                """.formatted(code);

        MvcResult createResult = mockMvc.perform(post("/api/v1/parameters/roles")
                        .with(jwt().authorities(new SimpleGrantedAuthority("PARAMETER_MANAGE")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.code").value(code))
                .andReturn();

        JsonNode createdJson = objectMapper.readTree(createResult.getResponse().getContentAsString());
        Integer roleId = createdJson.path("data").path("id").asInt();

        mockMvc.perform(get("/api/v1/parameters/roles/{id}", roleId)
                        .with(jwt().authorities(new SimpleGrantedAuthority("PARAMETER_VIEW"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(roleId))
                .andExpect(jsonPath("$.data.code").value(code));
    }

    @Test
    void userRoleAssignmentOnlyAcceptsGlobalScope() throws Exception {
        AppUser administrator = createActiveUser();
        AppUser nonAdministrator = createActiveUser();
        AppUser user = createActiveUser();
        Role globalRole = roleRepository.findByCode("PLATFORM_ADMIN").orElseThrow();
        Role businessRole = roleRepository.findByCode("BUSINESS_ADMIN").orElseThrow();

        UserRole administratorRole = new UserRole();
        administratorRole.setUser(administrator);
        administratorRole.setRole(globalRole);
        administratorRole.setActive(true);
        userRoleRepository.save(administratorRole);

        mockMvc.perform(put("/api/v1/parameters/users/{userId}/roles", user.getId())
                        .with(jwt().jwt(builder -> builder.subject(nonAdministrator.getId().toString()))
                                .authorities(new SimpleGrantedAuthority("PARAMETER_MANAGE")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"roleIds\": [] }"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false));

        String invalidPayload = """
                { "roleIds": [%d, %d] }
                """.formatted(globalRole.getId(), businessRole.getId());

        mockMvc.perform(put("/api/v1/parameters/users/{userId}/roles", user.getId())
                        .with(jwt().jwt(builder -> builder.subject(administrator.getId().toString()))
                                .authorities(new SimpleGrantedAuthority("PARAMETER_MANAGE")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));

        String validPayload = """
                { "roleIds": [%d] }
                """.formatted(globalRole.getId());

        mockMvc.perform(put("/api/v1/parameters/users/{userId}/roles", user.getId())
                        .with(jwt().jwt(builder -> builder.subject(administrator.getId().toString()))
                                .authorities(new SimpleGrantedAuthority("PARAMETER_MANAGE")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        assertThat(appUserRepository.findById(user.getId()).orElseThrow().getSecurityVersion())
                .isEqualTo(1);

        MvcResult listResult = mockMvc.perform(get("/api/v1/parameters/users/{userId}/roles", user.getId())
                        .with(jwt().jwt(builder -> builder.subject(administrator.getId().toString()))
                                .authorities(new SimpleGrantedAuthority("PARAMETER_VIEW"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn();

        JsonNode listJson = objectMapper.readTree(listResult.getResponse().getContentAsString()).path("data");
        assertThat(listJson).isNotEmpty();

        boolean allGlobal = true;
        boolean hasBusinessRole = false;
        for (JsonNode roleNode : listJson) {
            String scope = roleNode.path("scope").asText();
            String code = roleNode.path("code").asText();
            if (!"GLOBAL".equals(scope)) {
                allGlobal = false;
            }
            if ("BUSINESS_ADMIN".equals(code)) {
                hasBusinessRole = true;
            }
        }

        assertThat(allGlobal).isTrue();
        assertThat(hasBusinessRole).isFalse();
    }

    private AppUser createActiveUser() {
        DocumentType documentType = documentTypeRepository.findByCode("CC").orElseThrow();

        AppUser user = new AppUser();
        user.setDocumentType(documentType);
        user.setDocumentNumber(String.valueOf(System.nanoTime()));
        user.setName("Test");
        user.setSurname("User");
        user.setEmail("user+" + UUID.randomUUID() + "@tecmarket.test");
        user.setPasswordHash("$2a$10$7EqJtq98hPqEX7fNZaFWoOHiXWyf1n4pRr6NfF5iU5zJfK6K7zN6K");
        user.setAddress("Calle Test");
        user.setPhone("+573001110000");
        user.setActive(true);
        user.setEmailVerified(true);

        return appUserRepository.save(user);
    }
}
