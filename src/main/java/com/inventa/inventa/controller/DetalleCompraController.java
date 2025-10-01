package com.inventa.inventa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.detallecompra.DetalleCompraRequestDTO;
import com.inventa.inventa.dto.detallecompra.DetalleCompraResponseDTO;
import com.inventa.inventa.entity.DetalleCompra;
import com.inventa.inventa.mapper.DetalleCompraMapper;
import com.inventa.inventa.service.DetalleCompraService;

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

    @GetMapping
    public List<DetalleCompraResponseDTO> listar() {
        return detalleCompraService.listar().stream()
                .map(detalleCompraMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DetalleCompraResponseDTO obtenerPorId(@PathVariable Integer id) {
        DetalleCompra detalle = detalleCompraService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DetalleCompra no encontrado"));
        return detalleCompraMapper.toResponse(detalle);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DetalleCompraResponseDTO crear(@RequestBody DetalleCompraRequestDTO dto) {
        DetalleCompra detalle = new DetalleCompra();
        detalleCompraMapper.updateEntityFromRequest(detalle, dto);
        return detalleCompraMapper.toResponse(detalleCompraService.guardar(detalle));
    }

    @PutMapping("/{id}")
    public DetalleCompraResponseDTO actualizar(@PathVariable Integer id, @RequestBody DetalleCompraRequestDTO dto) {
        DetalleCompra detalle = detalleCompraService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DetalleCompra no encontrado"));
        detalleCompraMapper.updateEntityFromRequest(detalle, dto);
        return detalleCompraMapper.toResponse(detalleCompraService.guardar(detalle));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (detalleCompraService.buscarPorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DetalleCompra no encontrado");
        }
        detalleCompraService.eliminar(id);
    }
}
