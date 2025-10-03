package com.inventa.inventa.dto.usuario;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Integer usuarioId;
    private String nombreCompleto;
    private String cuiDpi;
    private String nombreUsuario;
    private Integer rolId;
    private String nombreRol;
    private LocalDateTime fechaCreacion;
    private String correo;
    private String telefono;
}
