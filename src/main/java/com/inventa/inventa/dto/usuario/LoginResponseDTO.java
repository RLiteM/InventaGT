package com.inventa.inventa.dto.usuario;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private Integer usuarioId;
    private String nombreCompleto;
    private String rol;
    private String mensaje;
}
