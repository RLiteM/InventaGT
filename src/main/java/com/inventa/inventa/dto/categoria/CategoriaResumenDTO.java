package com.inventa.inventa.dto.categoria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResumenDTO {
    private Integer categoriaId;
    private String nombre;
    private long cantidadProductos;
}
