package com.inventa.inventa.dto.dashboard;

import java.math.BigDecimal;

public record ValorCategoriaDTO(
    String categoria,
    BigDecimal valorInventario,
    Long totalProductos
) {}
