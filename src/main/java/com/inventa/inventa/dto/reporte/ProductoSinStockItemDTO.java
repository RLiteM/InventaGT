package com.inventa.inventa.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoSinStockItemDTO {
    private String sku;
    private String nombre;
    private BigDecimal stockActual;
}

