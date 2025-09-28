package com.inventa.inventa.dto.lote;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class LoteResponseDTO {
    private Integer loteId;
    private Integer productoId;
    private String productoNombre;
    private String skuProducto;
    private Integer detalleCompraId;
    private LocalDate fechaCaducidad;
    private BigDecimal cantidadInicial;
    private BigDecimal cantidadActual;
}
