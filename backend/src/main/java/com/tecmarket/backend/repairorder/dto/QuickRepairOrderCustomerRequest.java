package com.tecmarket.backend.repairorder.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record QuickRepairOrderCustomerRequest(
        @NotBlank @Size(max = 15)
        @Schema(example = "CC")
        String documentTypeCode,

        @NotBlank @Size(max = 30)
        @Schema(example = "123456789")
        String documentNumber,

        @NotBlank @Size(max = 50)
        @Schema(example = "Laura")
        String name,

        @NotBlank @Size(max = 50)
        @Schema(example = "Gomez")
        String surname,

        @NotBlank @Email @Size(max = 150)
        @Schema(example = "laura@example.com")
        String email,

        @NotBlank @Size(max = 200)
        @Schema(example = "Calle 10 #20-30")
        String address,

        @NotBlank @Size(max = 30)
        @Schema(example = "+573001112233")
        String phone,

        @NotNull @Positive
        @Schema(example = "1")
        Integer countryId,

        @NotNull @Positive
        @Schema(example = "1")
        Integer departmentId,

        @NotNull @Positive
        @Schema(example = "1")
        Integer cityId) {
}
