package com.inventa.inventa.dto.compra;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class CompraResponseDTO {
    private Integer compraId;
    private Integer proveedorId;
    private String proveedorNombre;
    private Integer usuarioId;
    private String usuarioNombre;
    private LocalDate fechaCompra;
    private String numeroFactura;
    private BigDecimal montoTotal;
}
