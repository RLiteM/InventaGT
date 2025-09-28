package com.inventa.inventa.dto.tokenrecuperacion;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TokenRecuperacionRequestDTO {
    private Integer usuarioId;
    private String token;
    private LocalDateTime fechaExpiracion;
    private Boolean usado;
}
