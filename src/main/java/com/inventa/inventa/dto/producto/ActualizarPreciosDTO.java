package com.inventa.inventa.dto.producto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ActualizarPreciosDTO {
    private BigDecimal precioMinorista;
    private BigDecimal precioMayorista;
}
