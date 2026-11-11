package com.tecmarket.backend.review.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecmarket.backend.review.dto.PublicBusinessReviewResponse;
import com.tecmarket.backend.review.entity.BusinessReview;
import com.tecmarket.backend.user.entity.AppUser;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BusinessReviewMapperPrivacyTest {

    private final BusinessReviewMapper mapper = Mappers.getMapper(BusinessReviewMapper.class);
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void publicReviewContainsOnlyTheApprovedContractAndAnonymizedName() throws Exception {
        AppUser customer = new AppUser();
        customer.setId(991);
        customer.setName("Laura");
        customer.setSurname("Gomez Restrepo");
        customer.setEmail("private@example.test");
        customer.setDocumentNumber("123456789");
        customer.setPhone("3000000000");

        BusinessReview review = new BusinessReview();
        review.setId(88);
        review.setCustomer(customer);
        review.setRating(5);
        review.setComment("Buen servicio");
        review.setCreatedAt(LocalDateTime.of(2026, 9, 1, 10, 30));

        PublicBusinessReviewResponse response = mapper.toPublicResponse(review);
        var json = objectMapper.readTree(objectMapper.writeValueAsBytes(response));

        assertThat(response.customerDisplayName()).isEqualTo("Laura G.");
        assertThat(json.properties().stream().map(java.util.Map.Entry::getKey).toList())
                .containsExactlyInAnyOrder("rating", "comment", "customerDisplayName", "createdAt");
        assertThat(json.toString())
                .doesNotContain("private@example.test", "123456789", "3000000000", "991", "repairOrder");
    }
}
