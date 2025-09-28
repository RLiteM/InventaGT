package com.inventa.inventa.dto.tokenrecuperacion;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TokenRecuperacionResponseDTO {
    private Integer tokenId;
    private Integer usuarioId;
    private String nombreUsuario;
    private String token;
    private LocalDateTime fechaExpiracion;
    private Boolean usado;
}
