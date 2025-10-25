package com.inventa.inventa.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentasDiaReporteDTO {
    private long totalVentas;
    private BigDecimal totalMonto;
    private BigDecimal promedioVenta;
    private BigDecimal ventaMaxima;
    private BigDecimal ventaMinima;
    private List<VentasDiaMetodoPagoDTO> resumenMetodosPago;
    private List<VentasDiaVentaRecienteDTO> ventasRecientes;
}

