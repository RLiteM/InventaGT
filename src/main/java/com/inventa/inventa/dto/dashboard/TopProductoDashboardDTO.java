package com.inventa.inventa.dto.dashboard;

import java.math.BigDecimal;

public record TopProductoDashboardDTO(
    Integer productoId,
    String productoNombre,
    String sku,
    Long totalUnidadesVendidas,
    BigDecimal ingresosGenerados
) {}
