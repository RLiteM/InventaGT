package com.inventa.inventa.controller;

import com.inventa.inventa.dto.producto.ProductoNombreSkuDTO;
import com.inventa.inventa.dto.producto.ProductoRapidoRequestDTO;
import com.inventa.inventa.dto.producto.ProductoRequestDTO;
import com.inventa.inventa.dto.producto.ProductoResponseDTO;
import com.inventa.inventa.dto.producto.ProductoPrecioDTO;
import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.mapper.ProductoMapper;
import com.inventa.inventa.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

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
        return productoService.listar(search, proveedorId).stream().map(productoMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/precios")
    public List<ProductoPrecioDTO> listarPrecios(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer proveedorId,
            @RequestParam(required = false, name = "tipoCliente") String tipoCliente
    ) {
        final boolean mayorista = tipoCliente != null && tipoCliente.equalsIgnoreCase("Mayorista");
        return productoService.listar(search, proveedorId).stream()
                .map(p -> {
                    ProductoPrecioDTO dto = new ProductoPrecioDTO();
                    dto.setProductoId(p.getProductoId());
                    dto.setSku(p.getSku());
                    dto.setNombre(p.getNombre());
                    dto.setPrecioMinorista(p.getPrecioMinorista());
                    dto.setPrecioMayorista(p.getPrecioMayorista());
                    dto.setPrecioAplicable(mayorista ? p.getPrecioMayorista() : p.getPrecioMinorista());
                    return dto;
                })
                .collect(Collectors.toList());
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

    @PostMapping("/rapido")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponseDTO crearRapido(@Valid @RequestBody ProductoRapidoRequestDTO dto) {
        Producto nuevoProducto = productoService.crearProductoRapido(dto);
        return productoMapper.toResponse(nuevoProducto);
    }

    @PutMapping("/{id}")
    public ProductoResponseDTO actualizar(@PathVariable Integer id, @RequestBody ProductoRequestDTO dto) {
        Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        productoMapper.updateEntityFromRequest(producto, dto);
        return productoMapper.toResponse(productoService.guardar(producto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> partialUpdateProducto(@PathVariable Integer id, @RequestBody ProductoRequestDTO requestDTO) {
        ProductoResponseDTO updatedProducto = productoService.partialUpdate(id, requestDTO);
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
        return productoService.listarTodos().stream()
                .map(productoMapper::toNombreSkuDTO)
                .collect(Collectors.toList());
    }
}
