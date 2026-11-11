/*
 * TecMarket Backend
 * Module: Auth
 */

package com.tecmarket.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informacion publica del usuario autenticado.")

public record AuthUserResponse(
                Integer id,
                String documentTypeCode,
                String documentNumber,
                String name,
                String surname,
                String email,
                String phone,
                Integer countryId,
                String countryName,
                Integer departmentId,
                String departmentName,
                Integer cityId,
                String cityName) {
}
