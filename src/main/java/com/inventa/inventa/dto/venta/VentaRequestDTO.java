package com.inventa.inventa.dto.venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VentaRequestDTO {
    private Integer usuarioId;
    private Integer clienteId;
    private LocalDateTime fechaVenta;
    private BigDecimal montoTotal;
    private String metodoPago;
}
