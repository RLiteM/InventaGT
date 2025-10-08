package com.inventa.inventa.dto.categoria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaConteoDTO {
    private Integer categoriaId;
    private String nombre;
    private String descripcion;
    private long cantidadProductos;
}
