package com.inventa.inventa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.producto.ProductoRequestDTO;
import com.inventa.inventa.dto.producto.ProductoResponseDTO;
import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.mapper.ProductoMapper;
import com.inventa.inventa.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoMapper productoMapper;

    public ProductoController(ProductoService productoService, ProductoMapper productoMapper) {
        this.productoService = productoService;
        this.productoMapper = productoMapper;
    }

    @GetMapping
    public List<ProductoResponseDTO> listar(@RequestParam(required = false) String search) {
        return productoService.listar(search).stream().map(productoMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductoResponseDTO obtenerPorId(@PathVariable Integer id) {
        Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        return productoMapper.toResponse(producto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponseDTO crear(@RequestBody ProductoRequestDTO dto) {
        Producto producto = new Producto();
        productoMapper.updateEntityFromRequest(producto, dto);
        return productoMapper.toResponse(productoService.guardar(producto));
    }

    @PutMapping("/{id}")
    public ProductoResponseDTO actualizar(@PathVariable Integer id, @RequestBody ProductoRequestDTO dto) {
        Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        productoMapper.updateEntityFromRequest(producto, dto);
        return productoMapper.toResponse(productoService.guardar(producto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (productoService.buscarPorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        productoService.eliminar(id);
    }

    @GetMapping("/sku/{sku}")
    public ProductoResponseDTO obtenerPorSku(@PathVariable String sku) {
        Producto producto = productoService.buscarPorSku(sku);
        if (producto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado con ese SKU");
        }
        return productoMapper.toResponse(producto);
    }
}
