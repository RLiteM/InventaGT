package com.inventa.inventa.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetearPasswordRequestDTO {

    @NotBlank(message = "El token no puede estar vacío")
    private String token;

    @NotBlank(message = "La nueva contraseña no puede estar vacía")
    private String nuevaContrasena;
}
