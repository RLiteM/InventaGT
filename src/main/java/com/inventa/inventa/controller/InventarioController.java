package com.inventa.inventa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventa.inventa.dto.producto.ProductoResponseDTO;
import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.service.ProductoService;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    private final ProductoService productoService;

    public InventarioController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/resumen")
    public List<ProductoResponseDTO> resumenInventario() {
        return productoService.listar().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/criticos")
    public List<ProductoResponseDTO> productosConStockCritico() {
        return productoService.listar().stream()
                .filter(producto -> producto.getStockActual() != null && producto.getStockMinimo() != null
                        && producto.getStockActual().compareTo(producto.getStockMinimo()) <= 0)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ProductoResponseDTO toResponse(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setProductoId(producto.getProductoId());
        dto.setSku(producto.getSku());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setUltimoCosto(producto.getUltimoCosto());
        dto.setPrecioMinorista(producto.getPrecioMinorista());
        dto.setPrecioMayorista(producto.getPrecioMayorista());
        dto.setStockActual(producto.getStockActual());
        dto.setStockMinimo(producto.getStockMinimo());
        dto.setUnidadMedida(producto.getUnidadMedida());
        if (producto.getCategoria() != null) {
            dto.setCategoriaId(producto.getCategoria().getCategoriaId());
            dto.setCategoriaNombre(producto.getCategoria().getNombre());
        }
        return dto;
    }
}
