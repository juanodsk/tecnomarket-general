/*
 * TecMarket Backend
 * Module: Auth
 */

package com.tecmarket.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciales para iniciar sesion.")
public record LoginRequest(
        @NotBlank @Email @Schema(description = "Correo electronico registrado del usuario.", example = "juan@example.com") String email,

        @NotBlank @Schema(description = "Contrasena del usuario.", example = "Password123") String password) {
}
