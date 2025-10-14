package com.inventa.inventa.dto.dashboard;

import java.math.BigDecimal;

public record TopClienteDashboardDTO(
    Integer clienteId,
    String nombreCliente,
    Long totalCompras,
    BigDecimal montoTotalGastado
) {}
