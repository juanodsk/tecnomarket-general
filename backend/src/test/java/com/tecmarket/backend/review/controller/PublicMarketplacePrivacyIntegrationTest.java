package com.tecmarket.backend.review.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecmarket.backend.common.pagination.PageResponse;
import com.tecmarket.backend.review.dto.PublicBusinessReviewResponse;
import com.tecmarket.backend.review.service.BusinessReviewService;
import com.tecmarket.backend.support.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = "app.features.marketplace-enabled=true")
class PublicMarketplacePrivacyIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BusinessReviewService businessReviewService;

    @Test
    void publicReviewsExposeExactlyTheApprovedFields() throws Exception {
        PublicBusinessReviewResponse review = new PublicBusinessReviewResponse(
                5,
                "Excelente servicio",
                "Laura G.",
                LocalDateTime.of(2026, 9, 11, 9, 30));
        when(businessReviewService.findPublicByBusiness(7, 0, 20))
                .thenReturn(new PageResponse<>(List.of(review), 0, 20, 1, 1, false, false));

        String body = mockMvc.perform(get("/api/v1/public/businesses/7/reviews"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode publicReview = objectMapper.readTree(body).path("data").path("items").get(0);
        assertThat(publicReview.properties().stream().map(java.util.Map.Entry::getKey).toList())
                .containsExactlyInAnyOrder("rating", "comment", "customerDisplayName", "createdAt");
        assertThat(body)
                .doesNotContain(
                        "email",
                        "documentNumber",
                        "phone",
                        "customerId",
                        "repairOrderId",
                        "orderNumber");
        verify(businessReviewService).findPublicByBusiness(7, 0, 20);
    }
}
