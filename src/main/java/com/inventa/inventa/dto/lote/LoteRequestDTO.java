package com.inventa.inventa.dto.lote;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class LoteRequestDTO {
    private Integer productoId;
    private Integer detalleCompraId;
    private LocalDate fechaCaducidad;
    private BigDecimal cantidadInicial;
    private BigDecimal cantidadActual;
}
