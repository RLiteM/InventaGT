package com.inventa.inventa.dto.ajusteinventario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AjusteInventarioResponseDTO {
    private Integer ajusteId;
    private Integer loteId;
    private Integer productoId;
    private String productoNombre;
    private Integer usuarioId;
    private String usuarioNombre;
    private LocalDateTime fechaAjuste;
    private String tipoAjuste;
    private BigDecimal cantidad;
    private String motivo;
}
