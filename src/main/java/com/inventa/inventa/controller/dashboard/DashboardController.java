package com.inventa.inventa.controller.dashboard;

import com.inventa.inventa.dto.dashboard.*;
import com.inventa.inventa.service.dashboard.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/resumen")
    public ResponseEntity<ResumenDashboardDTO> getResumenDashboard() {
        return ResponseEntity.ok(dashboardService.getResumenDashboard());
    }

    @GetMapping("/alertas")
    public ResponseEntity<AlertasDashboardDTO> getAlertasDashboard() {
        return ResponseEntity.ok(dashboardService.getAlertasDashboard());
    }

    @GetMapping("/movimientos")
    public ResponseEntity<MovimientosRecientesDTO> getMovimientosRecientes() {
        return ResponseEntity.ok(dashboardService.getMovimientosRecientes());
    }

    @GetMapping("/top-productos")
    public ResponseEntity<List<TopProductoDashboardDTO>> getTopProductosMasVendidos() {
        return ResponseEntity.ok(dashboardService.getTopProductosMasVendidos());
    }

    @GetMapping("/top-clientes")
    public ResponseEntity<List<TopClienteDashboardDTO>> getTopClientes() {
        return ResponseEntity.ok(dashboardService.getTopClientes());
    }

    @GetMapping("/tendencias")
    public ResponseEntity<List<TendenciaVentaDTO>> getTendenciaVentas() {
        return ResponseEntity.ok(dashboardService.getTendenciaVentas());
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<ValorCategoriaDTO>> getValorInventarioPorCategoria() {
        return ResponseEntity.ok(dashboardService.getValorInventarioPorCategoria());
    }
}
