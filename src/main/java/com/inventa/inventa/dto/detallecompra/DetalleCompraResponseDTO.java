package com.inventa.inventa.dto.detallecompra;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DetalleCompraResponseDTO {
    private Integer detalleCompraId;
    private Integer compraId;
    private Integer productoId;
    private String productoNombre;
    private BigDecimal cantidad;
    private BigDecimal costoUnitarioCompra;
    private BigDecimal subtotal;
}
