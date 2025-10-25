package com.inventa.inventa.controller.reporte;

import com.inventa.inventa.dto.reporte.ProductoSinStockItemDTO;
import com.inventa.inventa.dto.reporte.ProductosResumenStockDTO;
import com.inventa.inventa.service.reporte.ReporteProductosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reportes/productos")
public class ReporteProductosController {

    private final ReporteProductosService reporteProductosService;

    public ReporteProductosController(ReporteProductosService reporteProductosService) {
        this.reporteProductosService = reporteProductosService;
    }

    @GetMapping("/resumen-stock")
    public ResponseEntity<ProductosResumenStockDTO> getResumenStock() {
        return ResponseEntity.ok(reporteProductosService.getResumenStock());
    }

    @GetMapping("/sin-stock")
    public ResponseEntity<List<ProductoSinStockItemDTO>> listarProductosSinStock() {
        return ResponseEntity.ok(reporteProductosService.listarProductosSinStock());
    }
}

