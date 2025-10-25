package com.inventa.inventa.controller.dashboard;

import com.inventa.inventa.dto.dashboard.ResumenVendedorHoyDTO;
import com.inventa.inventa.service.dashboard.VendedorDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard/vendedores")
public class VendedorDashboardController {

    private final VendedorDashboardService vendedorDashboardService;

    public VendedorDashboardController(VendedorDashboardService vendedorDashboardService) {
        this.vendedorDashboardService = vendedorDashboardService;
    }

    @GetMapping("/{usuarioId}/resumen-hoy")
    public ResponseEntity<ResumenVendedorHoyDTO> getResumenVendedorHoy(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(vendedorDashboardService.getResumenVendedorHoy(usuarioId));
    }
}

