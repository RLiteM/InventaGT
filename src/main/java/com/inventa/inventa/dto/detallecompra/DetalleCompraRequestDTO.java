package com.inventa.inventa.dto.detallecompra;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DetalleCompraRequestDTO {
    private Integer compraId;
    private Integer productoId;
    private BigDecimal cantidad;
    private BigDecimal costoUnitarioCompra;
    private BigDecimal subtotal;
}
