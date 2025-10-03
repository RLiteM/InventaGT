package com.inventa.inventa.dto.usuario;

import lombok.Data;

@Data
public class UsuarioRequestDTO {
    private String nombreCompleto;
    private String cuiDpi;
    private String nombreUsuario;
    private String contrasena;   // se hashea al guardar
    private Integer rolId;
    private String correo;
    private String telefono;
}
