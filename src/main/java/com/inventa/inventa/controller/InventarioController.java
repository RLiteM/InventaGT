package com.inventa.inventa.controller;

import com.inventa.inventa.dto.producto.ProductoResponseDTO;
import com.inventa.inventa.mapper.ProductoMapper;
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
    private final ProductoMapper productoMapper;

    public InventarioController(ProductoService productoService, ProductoMapper productoMapper) {
        this.productoService = productoService;
        this.productoMapper = productoMapper;
    }

    @GetMapping("/resumen")
    public List<ProductoResponseDTO> resumenInventario() {
        return productoService.findAllParaResumen().stream().map(productoMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/criticos")
    public List<ProductoResponseDTO> productosConStockCritico() {
        return productoService.findProductosConStockCritico().stream()
                .map(productoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
