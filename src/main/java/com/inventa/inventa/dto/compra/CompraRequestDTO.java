package com.inventa.inventa.dto.compra;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class CompraRequestDTO {
    private Integer proveedorId;
    private Integer usuarioId;
    private LocalDate fechaCompra;
    private String numeroFactura;
    private BigDecimal montoTotal;
}
