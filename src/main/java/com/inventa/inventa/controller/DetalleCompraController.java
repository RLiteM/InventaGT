package com.inventa.inventa.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.detallecompra.DetalleCompraRequestDTO;
import com.inventa.inventa.dto.detallecompra.DetalleCompraResponseDTO;
import com.inventa.inventa.entity.DetalleCompra;
import com.inventa.inventa.mapper.DetalleCompraMapper;
import com.inventa.inventa.service.DetalleCompraService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/detallecompra")
public class DetalleCompraController {

    private final DetalleCompraService detalleCompraService;
    private final DetalleCompraMapper detalleCompraMapper;

    public DetalleCompraController(DetalleCompraService detalleCompraService,
                                   DetalleCompraMapper detalleCompraMapper) {
        this.detalleCompraService = detalleCompraService;
        this.detalleCompraMapper = detalleCompraMapper;
    }

    // =========================
    // LISTAR TODOS LOS DETALLES
    // =========================
    @GetMapping
    public List<DetalleCompraResponseDTO> listar() {
        return detalleCompraService.listar().stream()
                .map(detalleCompraMapper::toResponse)
                .collect(Collectors.toList());
    }

    // =========================
    // OBTENER DETALLE POR ID
    // =========================
    @GetMapping("/{id}")
    public DetalleCompraResponseDTO obtenerPorId(@PathVariable Integer id) {
        DetalleCompra detalle = detalleCompraService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DetalleCompra no encontrado"));
        return detalleCompraMapper.toResponse(detalle);
    }

    // =========================
    // ELIMINAR DETALLE
    // =========================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        DetalleCompra detalle = detalleCompraService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DetalleCompra no encontrado"));
        try {
            detalleCompraService.eliminar(detalle);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede eliminar detalleCompra por restricciones de FK");
        }
    }
}
