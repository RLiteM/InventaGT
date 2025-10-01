package com.inventa.inventa.controller;

import com.inventa.inventa.dto.lote.LoteRequestDTO;
import com.inventa.inventa.dto.lote.LoteResponseDTO;
import com.inventa.inventa.entity.Lote;
import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.entity.DetalleCompra;
import com.inventa.inventa.mapper.LoteMapper;
import com.inventa.inventa.service.LoteService;
import com.inventa.inventa.service.ProductoService;
import com.inventa.inventa.service.DetalleCompraService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lote")
public class LoteController {

    private final LoteService loteService;
    private final ProductoService productoService;
    private final DetalleCompraService detalleCompraService;
    private final LoteMapper loteMapper;

    public LoteController(LoteService loteService,
                          ProductoService productoService,
                          DetalleCompraService detalleCompraService,
                          LoteMapper loteMapper) {
        this.loteService = loteService;
        this.productoService = productoService;
        this.detalleCompraService = detalleCompraService;
        this.loteMapper = loteMapper;
    }

    // =========================
    // LISTAR LOTES
    // =========================
    @GetMapping
    public List<LoteResponseDTO> listar() {
        return loteService.listar().stream()
                .map(loteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public LoteResponseDTO obtenerPorId(@PathVariable Integer id) {
        Lote lote = loteService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote no encontrado"));
        return loteMapper.toResponse(lote);
    }

    // =========================
    // CREAR LOTE
    // =========================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoteResponseDTO crear(@RequestBody LoteRequestDTO dto) {
        Lote lote = new Lote();

        // Cargar Producto
        Producto producto = productoService.buscarPorId(dto.getProductoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        lote.setProducto(producto);

        // Cargar DetalleCompra
        DetalleCompra detalleCompra = detalleCompraService.buscarPorId(dto.getDetalleCompraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DetalleCompra no encontrado"));
        lote.setDetalleCompra(detalleCompra);

        // Datos del lote
        lote.setFechaCaducidad(dto.getFechaCaducidad());
        lote.setCantidadInicial(dto.getCantidadInicial());
        lote.setCantidadActual(dto.getCantidadActual());

        // Guardar
        Lote saved = loteService.guardar(lote);

        return loteMapper.toResponse(saved);
    }

    // =========================
    // ACTUALIZAR LOTE
    // =========================
    @PutMapping("/{id}")
    public LoteResponseDTO actualizar(@PathVariable Integer id, @RequestBody LoteRequestDTO dto) {
        Lote lote = loteService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote no encontrado"));

        // Actualizar Producto
        Producto producto = productoService.buscarPorId(dto.getProductoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        lote.setProducto(producto);

        // Actualizar DetalleCompra
        DetalleCompra detalleCompra = detalleCompraService.buscarPorId(dto.getDetalleCompraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DetalleCompra no encontrado"));
        lote.setDetalleCompra(detalleCompra);

        // Actualizar datos
        lote.setFechaCaducidad(dto.getFechaCaducidad());
        lote.setCantidadInicial(dto.getCantidadInicial());
        lote.setCantidadActual(dto.getCantidadActual());

        Lote updated = loteService.guardar(lote);

        return loteMapper.toResponse(updated);
    }

    // =========================
    // ELIMINAR LOTE
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        return loteService.buscarPorId(id)
                .map(lote -> {
                    try {
                        loteService.eliminarPorId(id);
                        return ResponseEntity.noContent().build(); // 204 si se eliminó
                    } catch (DataIntegrityViolationException e) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body("{\"error\": \"El lote está en uso y no puede eliminarse\"}");
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"error\": \"Lote no encontrado\"}"));
    }
}
