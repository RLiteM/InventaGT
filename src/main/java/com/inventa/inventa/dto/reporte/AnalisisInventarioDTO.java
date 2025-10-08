package com.inventa.inventa.dto.reporte;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalisisInventarioDTO {
    private long totalProductos;
    private BigDecimal valorTotalInventario;
    private long conteoProductosBajoStock;
    private long unidadesProximasAVencer;
}
