/*
 * TecMarket Backend
 * Module: Auth
 */

package com.tecmarket.backend.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta del inicio de sesion con JWT.")
public record LoginResponse(
                String accessToken,
                @JsonIgnore
                String refreshToken,
                AuthUserResponse user) {
}
