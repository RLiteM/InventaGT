package com.inventa.inventa.dto.dashboard;

import java.math.BigDecimal;

public record TendenciaVentaDTO(
    String mes,
    BigDecimal totalVentas
) {}
