package com.inventa.inventa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.lote.LoteRequestDTO;
import com.inventa.inventa.dto.lote.LoteResponseDTO;
import com.inventa.inventa.entity.Lote;
import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.entity.DetalleCompra;
import com.inventa.inventa.mapper.LoteMapper;
import com.inventa.inventa.service.LoteService;
import com.inventa.inventa.service.ProductoService;
import com.inventa.inventa.service.DetalleCompraService;

@RestController
@RequestMapping("/api/lotes")
public class LoteController {

    private final LoteService loteService;
    private final LoteMapper loteMapper;
    private final ProductoService productoService;
    private final DetalleCompraService detalleCompraService;

    public LoteController(
            LoteService loteService,
            LoteMapper loteMapper,
            ProductoService productoService,
            DetalleCompraService detalleCompraService) {
        this.loteService = loteService;
        this.loteMapper = loteMapper;
        this.productoService = productoService;
        this.detalleCompraService = detalleCompraService;
    }

    // =========================
    // LISTAR TODOS LOS LOTES
    // =========================
    @GetMapping
    public List<LoteResponseDTO> listar() {
        return loteService.listar().stream()
                .map(loteMapper::toResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // OBTENER LOTE POR ID
    // =========================
    @GetMapping("/{id}")
    public LoteResponseDTO obtenerPorId(@PathVariable Integer id) {
        Lote lote = loteService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote no encontrado"));
        return loteMapper.toResponse(lote);
    }

    // =========================
    // CREAR NUEVO LOTE
    // =========================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoteResponseDTO crear(@RequestBody LoteRequestDTO dto) {
        Producto producto = productoService.buscarPorId(dto.getProductoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        DetalleCompra detalleCompra = detalleCompraService.buscarPorId(dto.getDetalleCompraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de compra no encontrado"));

        Lote lote = loteMapper.toEntity(dto, producto, detalleCompra);
        return loteMapper.toResponse(loteService.guardar(lote));
    }

    // =========================
    // ACTUALIZAR LOTE EXISTENTE
    // =========================
    @PutMapping("/{id}")
    public LoteResponseDTO actualizar(@PathVariable Integer id, @RequestBody LoteRequestDTO dto) {
        Lote loteExistente = loteService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote no encontrado"));

        Producto producto = productoService.buscarPorId(dto.getProductoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        DetalleCompra detalleCompra = detalleCompraService.buscarPorId(dto.getDetalleCompraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle de compra no encontrado"));

        loteExistente.setProducto(producto);
        loteExistente.setDetalleCompra(detalleCompra);
        loteExistente.setFechaCaducidad(dto.getFechaCaducidad());
        loteExistente.setCantidadInicial(dto.getCantidadInicial());
        loteExistente.setCantidadActual(dto.getCantidadActual());

        return loteMapper.toResponse(loteService.guardar(loteExistente));
    }

    // =========================
    // ELIMINAR LOTE
    // =========================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        Lote lote = loteService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote no encontrado"));
        loteService.eliminar(lote);
    }
}

