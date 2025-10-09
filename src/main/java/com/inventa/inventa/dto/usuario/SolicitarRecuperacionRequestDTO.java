package com.inventa.inventa.dto.usuario;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class SolicitarRecuperacionRequestDTO {
    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Debe ser una dirección de correo válida")
    private String email;
}
