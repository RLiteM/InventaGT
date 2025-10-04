package com.inventa.inventa.dto.usuario;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String nombreUsuario;
    private String contrasena;
}
