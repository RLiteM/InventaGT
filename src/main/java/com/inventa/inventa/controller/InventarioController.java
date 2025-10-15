package com.inventa.inventa.controller;

import com.inventa.inventa.dto.producto.ProductoResponseDTO;
import com.inventa.inventa.service.ProductoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final ProductoService productoService;

    public InventarioController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/resumen")
    public List<ProductoResponseDTO> resumenInventario() {
        // TODO: This should be a more specific service call, not a generic search
        return productoService.listar(null, null);
    }

    @GetMapping("/criticos")
    public List<ProductoResponseDTO> productosConStockCritico() {
        // TODO: This logic should be moved to the service layer
        return productoService.listar(null, null).stream()
                .filter(producto -> producto.getStockActual() != null && producto.getStockMinimo() != null
                        && producto.getStockActual().compareTo(producto.getStockMinimo()) <= 0)
                .collect(Collectors.toList());
    }
}
