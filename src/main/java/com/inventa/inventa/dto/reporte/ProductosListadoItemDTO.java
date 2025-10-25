package com.inventa.inventa.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductosListadoItemDTO {
    private String sku;
    private String nombre;
    private String categoria;
    private String unidadMedida;
    private BigDecimal stockActual;
    private BigDecimal stockMinimo;
    private BigDecimal precioMinorista;
    private BigDecimal precioMayorista;
    private BigDecimal ultimoCosto;
}

