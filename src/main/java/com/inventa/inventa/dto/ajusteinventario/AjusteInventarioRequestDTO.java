package com.inventa.inventa.dto.ajusteinventario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AjusteInventarioRequestDTO {
    private Integer loteId;
    private Integer usuarioId;
    private LocalDateTime fechaAjuste;
    private String tipoAjuste;
    private BigDecimal cantidad;
    private String motivo;
}
