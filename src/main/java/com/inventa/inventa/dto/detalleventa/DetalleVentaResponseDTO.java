package com.inventa.inventa.dto.detalleventa;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DetalleVentaResponseDTO {
    private Integer detalleId;
    private Integer ventaId;
    private Integer loteId;
    private Integer productoId;
    private String productoNombre;
    private BigDecimal cantidad;
    private BigDecimal precioUnitarioVenta;
    private BigDecimal subtotal;
}
