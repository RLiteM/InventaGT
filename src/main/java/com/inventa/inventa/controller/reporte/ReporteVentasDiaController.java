package com.inventa.inventa.controller.reporte;

import com.inventa.inventa.dto.reporte.VentasDiaReporteDTO;
import com.inventa.inventa.service.reporte.VentasDiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
public class ReporteVentasDiaController {

    private final VentasDiaService ventasDiaService;

    public ReporteVentasDiaController(VentasDiaService ventasDiaService) {
        this.ventasDiaService = ventasDiaService;
    }

    @GetMapping("/ventas-dia")
    public ResponseEntity<VentasDiaReporteDTO> getVentasDelDia() {
        return ResponseEntity.ok(ventasDiaService.getVentasDelDia());
    }
}

