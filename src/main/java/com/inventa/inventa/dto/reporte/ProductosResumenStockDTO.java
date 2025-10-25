package com.inventa.inventa.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductosResumenStockDTO {
    private long totalProductos;
    private BigDecimal stockTotal;
    private BigDecimal valorInventario;
    private long productosSinStock;
    private long productosBajoStock;
}

