package com.inventa.inventa.controller;

import com.inventa.inventa.dto.detalleventa.DetalleVentaRequestDTO;
import com.inventa.inventa.dto.detalleventa.DetalleVentaResponseDTO;
import com.inventa.inventa.entity.DetalleVenta;
import com.inventa.inventa.mapper.DetalleVentaMapper;
import com.inventa.inventa.service.DetalleVentaService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/detalleventa")
public class DetalleVentaController {

    private final DetalleVentaService detalleVentaService;
    private final DetalleVentaMapper detalleVentaMapper;

    public DetalleVentaController(DetalleVentaService detalleVentaService,
                                  DetalleVentaMapper detalleVentaMapper) {
        this.detalleVentaService = detalleVentaService;
        this.detalleVentaMapper = detalleVentaMapper;
    }

    // =========================
    // LISTAR TODOS
    // =========================
    @GetMapping
    public List<DetalleVentaResponseDTO> listar() {
        return detalleVentaService.listar().stream()
                .map(detalleVentaMapper::toResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // OBTENER POR ID
    // =========================
    @GetMapping("/{id}")
    public DetalleVentaResponseDTO obtenerPorId(@PathVariable Integer id) {
        DetalleVenta detalle = detalleVentaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DetalleVenta no encontrado"));
        return detalleVentaMapper.toResponse(detalle);
    }

    // =========================
    // ELIMINAR
    // =========================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        DetalleVenta detalle = detalleVentaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DetalleVenta no encontrado"));
        try {
            detalleVentaService.eliminar(detalle);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede eliminar detalleVenta por restricciones de FK");
        }
    }
}
