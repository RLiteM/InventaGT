package com.inventa.inventa.dto.dashboard;

import java.math.BigDecimal;

public record ResumenVendedorHoyDTO(
    BigDecimal totalVendidoHoy,
    long cantidadVentasHoy,
    long clientesAtendidosHoy
) {}

