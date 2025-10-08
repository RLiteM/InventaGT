package com.inventa.inventa.controller;

import com.inventa.inventa.dto.reporte.DashboardAvanzadoDTO;
import com.inventa.inventa.service.ReporteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/dashboard-avanzado")
    public DashboardAvanzadoDTO getDashboardAvanzado() {
        return reporteService.getDashboardAvanzado();
    }
}
