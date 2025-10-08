package com.inventa.inventa.dto.reporte;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopProductoDTO {
    private String nombreProducto;
    private String sku;
    private BigDecimal unidadesVendidas;
}
