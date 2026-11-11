package com.tecmarket.backend.security;

import com.tecmarket.backend.support.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SecurityEndpointsIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void rolesEndpointReturns401WhenMissingToken() throws Exception {
        mockMvc.perform(get("/api/v1/parameters/roles"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void rolesEndpointReturns403WhenTokenHasNoRequiredAuthority() throws Exception {
        mockMvc.perform(get("/api/v1/parameters/roles")
                        .with(jwt()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void rolesEndpointReturns200WhenTokenHasParameterViewAuthority() throws Exception {
        mockMvc.perform(get("/api/v1/parameters/roles")
                        .with(jwt().authorities(new SimpleGrantedAuthority("PARAMETER_VIEW"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void quotationListRequiresQuotationViewAuthority() throws Exception {
        mockMvc.perform(get("/api/v1/businesses/1/repair-orders/1/quotations")
                        .with(jwt()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void quickRepairOrderReturns401WhenMissingToken() throws Exception {
        mockMvc.perform(post("/api/v1/businesses/1/repair-orders/quick")
                        .header("Idempotency-Key", "qa-quick-order-unauthenticated")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void quickRepairOrderRequiresBothDeviceAndOrderPermissions() throws Exception {
        mockMvc.perform(post("/api/v1/businesses/1/repair-orders/quick")
                        .with(jwt().authorities(new SimpleGrantedAuthority("REPAIR_ORDER_CREATE")))
                        .header("Idempotency-Key", "qa-quick-order-missing-device-permission")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customer": {
                                    "documentTypeCode": "CC",
                                    "documentNumber": "100000001",
                                    "name": "QA",
                                    "surname": "Customer",
                                    "email": "qa-customer@example.com",
                                    "address": "QA address",
                                    "phone": "+573000000001",
                                    "countryId": 1,
                                    "departmentId": 1,
                                    "cityId": 1
                                  },
                                  "device": {
                                    "deviceTypeId": 1,
                                    "brandId": 1,
                                    "model": "QA phone",
                                    "imei": "350000000000001",
                                    "color": "Black"
                                  },
                                  "order": {
                                    "branchId": 1,
                                    "businessServiceId": 1,
                                    "assignedBusinessUserId": 1,
                                    "problemDescription": "QA authorization test"
                                  }
                                }
                                """))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void documentTypesListRemainsPublic() throws Exception {
        mockMvc.perform(get("/api/v1/parameters/document-types"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("X-Frame-Options", "DENY"))
                .andExpect(header().string("Referrer-Policy", "strict-origin-when-cross-origin"))
                .andExpect(header().string("Permissions-Policy", "camera=(), geolocation=(), microphone=(), payment=(), usb=()"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void actuatorProbesRemainPublicWithoutDetails() throws Exception {
        mockMvc.perform(get("/actuator/health/liveness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.components").doesNotExist());
    }

    @Test
    void missingPublicResourceReturns404InsteadOf500() throws Exception {
        mockMvc.perform(get("/swagger-ui/missing-resource.html"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
