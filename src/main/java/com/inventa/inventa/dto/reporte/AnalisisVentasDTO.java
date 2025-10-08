package com.inventa.inventa.dto.reporte;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalisisVentasDTO {
    private long volumenTotalVentas;
    private List<VentasPorMesDTO> ventasPorMes;
    private List<TopProductoDTO> top5ProductosMasVendidosPorUnidades;
}
