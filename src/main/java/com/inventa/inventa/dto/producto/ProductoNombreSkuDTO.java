package com.inventa.inventa.dto.producto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoNombreSkuDTO {
    private Integer productoId;
    private String nombre;
    private String sku;
}
