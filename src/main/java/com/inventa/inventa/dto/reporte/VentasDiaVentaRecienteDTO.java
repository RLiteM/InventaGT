package com.inventa.inventa.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentasDiaVentaRecienteDTO {
    private Integer ventaId;
    private LocalDateTime fechaVenta;
    private String vendedor;
    private String cliente;
    private BigDecimal montoTotal;
    private String metodoPago;
}

