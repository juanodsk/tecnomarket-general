package com.tecmarket.backend.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecmarket.backend.auth.entity.EmailVerificationToken;
import com.tecmarket.backend.auth.entity.RefreshToken;
import com.tecmarket.backend.auth.repository.EmailVerificationTokenRepository;
import com.tecmarket.backend.auth.repository.RefreshTokenRepository;
import com.tecmarket.backend.parameter.entity.City;
import com.tecmarket.backend.parameter.entity.Country;
import com.tecmarket.backend.parameter.entity.Department;
import com.tecmarket.backend.parameter.repository.CityRepository;
import com.tecmarket.backend.parameter.repository.CountryRepository;
import com.tecmarket.backend.parameter.repository.DepartmentRepository;
import com.tecmarket.backend.support.IntegrationTestBase;
import com.tecmarket.backend.user.entity.AppUser;
import com.tecmarket.backend.user.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import jakarta.servlet.http.Cookie;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CityRepository cityRepository;

    @Test
    void registerVerifyLoginMePermissionsAndLogoutFlowWorks() throws Exception {
        String email = "integration+" + UUID.randomUUID() + "@tecmarket.test";
        String password = "Password123!";
        Country country = countryRepository.findByCode("CO").orElseThrow();
        Department department = departmentRepository
                .findByCountryIdAndCodeIgnoreCase(country.getId(), "ANT")
                .orElseThrow();
        City city = cityRepository.findByDepartmentIdAndActiveTrueOrderByNameAsc(department.getId())
                .stream()
                .findFirst()
                .orElseThrow();

        String registerPayload = """
                {
                  "documentTypeCode": "CC",
                  "documentNumber": "%s",
                  "name": "Juan",
                  "surname": "Tester",
                  "email": "%s",
                  "password": "%s",
                  "address": "Calle 123",
                  "phone": "+573001112233",
                  "countryId": %d,
                  "departmentId": %d,
                  "cityId": %d
                }
                """.formatted(
                System.currentTimeMillis(),
                email,
                password,
                country.getId(),
                department.getId(),
                city.getId());

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));

        AppUser createdUser = appUserRepository.findByEmail(email).orElseThrow();
        EmailVerificationToken token = emailVerificationTokenRepository.findByUserIdAndActiveTrue(createdUser.getId())
                .stream()
                .findFirst()
                .orElseThrow();
        String rawVerificationToken = "integration-verification-" + UUID.randomUUID() + UUID.randomUUID();
        token.setTokenHash(sha256(rawVerificationToken));
        emailVerificationTokenRepository.saveAndFlush(token);

        mockMvc.perform(post("/api/v1/auth/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(java.util.Map.of("token", rawVerificationToken))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        AppUser verifiedUser = appUserRepository.findById(createdUser.getId()).orElseThrow();
        assertThat(verifiedUser.getEmailVerified()).isTrue();
        assertThat(verifiedUser.getEmailVerifiedAt()).isNotNull();

        mockMvc.perform(post("/api/v1/auth/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(java.util.Map.of("token", rawVerificationToken))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));

        mockMvc.perform(post("/api/v1/auth/verify-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(java.util.Map.of("token", "0".repeat(64)))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));

        String loginPayload = """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(email, password);

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").doesNotExist())
                .andReturn();

        JsonNode loginJson = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        String accessToken = loginJson.path("data").path("accessToken").asText();
        Cookie firstRefreshCookie = loginResult.getResponse().getCookie("tm_refresh");
        assertThat(accessToken).isNotBlank();
        assertThat(firstRefreshCookie).isNotNull();
        String firstRefreshToken = firstRefreshCookie.getValue();
        assertThat(firstRefreshToken).isNotBlank();
        assertThat(refreshTokenRepository.findAll())
                .extracting(RefreshToken::getTokenHash)
                .doesNotContain(firstRefreshToken);

        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value(email));

        mockMvc.perform(get("/api/v1/auth/me/permissions")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").value(createdUser.getId()))
                .andExpect(jsonPath("$.data.roles[0]").value("CUSTOMER"))
                .andExpect(jsonPath("$.data.permissions").isNotEmpty());

        MvcResult refreshResult = mockMvc.perform(post("/api/v1/auth/refresh")
                        .cookie(firstRefreshCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").doesNotExist())
                .andReturn();

        Cookie rotatedRefreshCookie = refreshResult.getResponse().getCookie("tm_refresh");
        assertThat(rotatedRefreshCookie).isNotNull();
        String rotatedRefreshToken = rotatedRefreshCookie.getValue();
        String rotatedAccessToken = objectMapper.readTree(refreshResult.getResponse().getContentAsString())
                .path("data").path("accessToken").asText();
        assertThat(rotatedRefreshToken).isNotEqualTo(firstRefreshToken);

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .cookie(firstRefreshCookie))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .cookie(rotatedRefreshCookie))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + rotatedAccessToken))
                .andExpect(status().isUnauthorized());

        MvcResult secondLoginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode secondLoginJson = objectMapper.readTree(secondLoginResult.getResponse().getContentAsString());
        String prePasswordChangeAccessToken = secondLoginJson.path("data").path("accessToken").asText();
        Cookie prePasswordChangeRefreshCookie = secondLoginResult.getResponse().getCookie("tm_refresh");
        assertThat(prePasswordChangeRefreshCookie).isNotNull();

        String newPassword = "NewPassword456!";
        mockMvc.perform(post("/api/v1/auth/password/change")
                        .header("Authorization", "Bearer " + prePasswordChangeAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(java.util.Map.of(
                                "currentPassword", password,
                                "newPassword", newPassword))))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + prePasswordChangeAccessToken))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .cookie(prePasswordChangeRefreshCookie))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isUnauthorized());

        String newLoginPayload = """
                {
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(email, newPassword);
        MvcResult postPasswordChangeLogin = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newLoginPayload))
                .andExpect(status().isOk())
                .andReturn();
        String preRevokeAllAccessToken = objectMapper
                .readTree(postPasswordChangeLogin.getResponse().getContentAsString())
                .path("data").path("accessToken").asText();
        Cookie preRevokeAllRefreshCookie = postPasswordChangeLogin.getResponse().getCookie("tm_refresh");

        mockMvc.perform(delete("/api/v1/auth/sessions")
                        .header("Authorization", "Bearer " + preRevokeAllAccessToken))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + preRevokeAllAccessToken))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .cookie(preRevokeAllRefreshCookie))
                .andExpect(status().isUnauthorized());

        MvcResult finalLoginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newLoginPayload))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode finalLoginJson = objectMapper.readTree(finalLoginResult.getResponse().getContentAsString());
        String logoutAccessToken = finalLoginJson.path("data").path("accessToken").asText();
        Cookie logoutRefreshCookie = finalLoginResult.getResponse().getCookie("tm_refresh");
        assertThat(logoutRefreshCookie).isNotNull();

        mockMvc.perform(post("/api/v1/auth/logout")
                        .header("Authorization", "Bearer " + logoutAccessToken)
                        .cookie(logoutRefreshCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .cookie(logoutRefreshCookie))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/v1/auth/me")
                        .header("Authorization", "Bearer " + logoutAccessToken))
                .andExpect(status().isUnauthorized());
    }

    private String sha256(String value) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                .digest(value.getBytes(StandardCharsets.UTF_8)));
    }
}
