package com.inventa.inventa.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentasDiaMetodoPagoDTO {
    private String metodoPago;
    private long cantidad;
    private BigDecimal total;
}

