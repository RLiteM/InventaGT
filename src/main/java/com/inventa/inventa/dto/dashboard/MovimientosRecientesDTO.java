package com.inventa.inventa.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record MovimientosRecientesDTO(
    List<MovimientoInventarioDTO> ultimasEntradas,
    List<MovimientoInventarioDTO> ultimasSalidas
) {
    public record MovimientoInventarioDTO(
        LocalDate fecha,
        String tipo,
        BigDecimal cantidad,
        String productoNombre,
        String usuario
    ) {}
}
