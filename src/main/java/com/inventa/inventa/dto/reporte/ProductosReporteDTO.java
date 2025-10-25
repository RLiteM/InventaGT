package com.inventa.inventa.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductosReporteDTO {
    private ProductosResumenStockDTO resumen;
    private List<ProductosListadoItemDTO> items;
}

