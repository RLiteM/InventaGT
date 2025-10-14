package com.inventa.inventa.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AlertasDashboardDTO(
    List<AlertaStockDTO> productosStockCritico,
    List<AlertaCaducidadDTO> lotesProximosACaducar
) {
    public record AlertaStockDTO(
        Integer productoId,
        String productoNombre,
        String sku,
        BigDecimal stockActual,
        BigDecimal stockMinimo
    ) {}

    public record AlertaCaducidadDTO(
        Integer loteId,
        String productoNombre,
        LocalDate fechaCaducidad,
        BigDecimal cantidadActual,
        String sku
    ) {}
}
