package com.inventa.inventa.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentasDiaListadoItemDTO {
    private String vendedor;
    private String cliente;
    private BigDecimal montoTotal;
    private String metodoPago;
}

