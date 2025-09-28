package com.inventa.inventa.dto.detalleventa;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DetalleVentaRequestDTO {
    private Integer ventaId;
    private Integer loteId;
    private BigDecimal cantidad;
    private BigDecimal precioUnitarioVenta;
    private BigDecimal subtotal;
}
