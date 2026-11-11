/*
 * TecMarket Backend
 * Module: Auth
 */

package com.tecmarket.backend.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para registrar un usuario en el sistema.")
public record RegisterRequest(
                @NotBlank @Size(max = 15) @Schema(description = "Codigo del tipo de documento del usuario. Debe existir en el sistema.", example = "CC") String documentTypeCode,

                @NotBlank @Size(max = 30) @Schema(description = "Número del documento del usuario.", example = "123456789") String documentNumber,

                @NotBlank @Size(max = 50) @Schema(description = "Nombre del usuario.", example = "Juan") String name,

                @NotBlank @Size(max = 50) @Schema(description = "Apellido del usuario.", example = "Pérez") String surname,

                @NotBlank @Email @Size(max = 150) @Schema(description = "Correo electronico unico del usuario.", example = "juan@example.com") String email,

                @NotBlank @Size(min = 8, max = 72) @Schema(description = "Contrasena del usuario. Se almacena encriptada.", example = "Password123") String password,

                @NotBlank @Size(max = 200) @Schema(description = "Direccion del usuario.", example = "Calle 10 #20-30") String address,

                @NotBlank @Size(max = 30) @Schema(description = "Telefono del usuario.", example = "+573001112233") String phone,

                @NotNull @Schema(description = "ID del pais de residencia del usuario.", example = "1") Integer countryId,

                @NotNull @Schema(description = "ID del departamento de residencia del usuario.", example = "1") Integer departmentId,

                @NotNull @Schema(description = "ID de la ciudad de residencia del usuario.", example = "1") Integer cityId

) {
}
