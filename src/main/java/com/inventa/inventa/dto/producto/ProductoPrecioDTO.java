package com.inventa.inventa.dto.producto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoPrecioDTO {
    private Integer productoId;
    private String sku;
    private String nombre;
    private BigDecimal precioMinorista;
    private BigDecimal precioMayorista;
    // Opcional para conveniencia del POS cuando se especifique tipoCliente
    private BigDecimal precioAplicable;
}

