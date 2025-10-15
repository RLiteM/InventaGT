package com.inventa.inventa.controller;

import com.inventa.inventa.dto.producto.ActualizarPreciosDTO;
import com.inventa.inventa.dto.producto.ProductoNombreSkuDTO;
import com.inventa.inventa.dto.producto.ProductoRequestDTO;
import com.inventa.inventa.dto.producto.ProductoResponseDTO;
import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.mapper.ProductoMapper;
import com.inventa.inventa.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


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
    public List<ProductoResponseDTO> listar(@RequestParam(required = false) String search, @RequestParam(required = false) Integer proveedorId) {
        return productoService.listar(search, proveedorId);
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

    @PatchMapping("/{id}/precios")
    public ResponseEntity<ProductoResponseDTO> updatePrices(@PathVariable Integer id, @RequestBody ActualizarPreciosDTO preciosDTO) {
        ProductoResponseDTO updatedProducto = productoService.updatePrices(id, preciosDTO);
        return ResponseEntity.ok(updatedProducto);
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

    @GetMapping("/nombre-sku")
    public List<ProductoNombreSkuDTO> listarNombreSku() {
        return productoService.listarTodos();
    }
}
