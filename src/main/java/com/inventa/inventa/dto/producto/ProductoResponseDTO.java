package com.inventa.inventa.dto.producto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductoResponseDTO {
    private Integer productoId;
    private String sku;
    private String nombre;
    private String descripcion;
    private BigDecimal ultimoCosto;
    private BigDecimal precioMinorista;
    private BigDecimal precioMayorista;
    private BigDecimal stockActual;
    private BigDecimal stockMinimo;
    private String unidadMedida;
    private Integer categoriaId;
    private String categoriaNombre;
}
