package com.inventa.inventa.dto.reporte;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardAvanzadoDTO {
    private AnalisisFinancieroDTO analisisFinanciero;
    private AnalisisVentasDTO analisisVentas;
    private AnalisisClientesDTO analisisClientes;
    private AnalisisInventarioDTO analisisInventario;
}
