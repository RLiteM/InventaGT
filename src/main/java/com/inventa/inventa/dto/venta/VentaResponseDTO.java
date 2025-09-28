package com.inventa.inventa.dto.venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class VentaResponseDTO {
    private Integer ventaId;
    private Integer usuarioId;
    private String usuarioNombre;
    private Integer clienteId;
    private String clienteNombre;
    private LocalDateTime fechaVenta;
    private BigDecimal montoTotal;
    private String metodoPago;
}
