package com.inventa.inventa.dto.dashboard;

import java.math.BigDecimal;

public record ResumenDashboardDTO(
    long totalProductos,
    BigDecimal stockTotal,
    BigDecimal valorInventario,
    BigDecimal totalVentasHistorico,
    BigDecimal gananciaBrutaHistorica,
    long totalClientes
) {}