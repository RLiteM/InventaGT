package com.inventa.inventa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.producto.ProductoRequestDTO;
import com.inventa.inventa.dto.producto.ProductoResponseDTO;
import com.inventa.inventa.entity.Categoria;
import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.service.CategoriaService;
import com.inventa.inventa.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ProductoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<ProductoResponseDTO> listar() {
        return productoService.listar().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductoResponseDTO obtenerPorId(@PathVariable Integer id) {
        Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        return toResponse(producto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponseDTO crear(@RequestBody ProductoRequestDTO dto) {
        Producto producto = new Producto();
        aplicarCambios(producto, dto);
        return toResponse(productoService.guardar(producto));
    }

    @PutMapping("/{id}")
    public ProductoResponseDTO actualizar(@PathVariable Integer id, @RequestBody ProductoRequestDTO dto) {
        Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        aplicarCambios(producto, dto);
        return toResponse(productoService.guardar(producto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (productoService.buscarPorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        productoService.eliminar(id);
    }

    private void aplicarCambios(Producto producto, ProductoRequestDTO dto) {
        producto.setSku(dto.getSku());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setUltimoCosto(dto.getUltimoCosto());
        producto.setPrecioMinorista(dto.getPrecioMinorista());
        producto.setPrecioMayorista(dto.getPrecioMayorista());
        producto.setStockActual(dto.getStockActual());
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setUnidadMedida(dto.getUnidadMedida());

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaService.buscarPorId(dto.getCategoriaId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
            producto.setCategoria(categoria);
        } else {
            producto.setCategoria(null);
        }
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
