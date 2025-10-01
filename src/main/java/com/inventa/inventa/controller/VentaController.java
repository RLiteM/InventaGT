package com.inventa.inventa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.venta.VentaRequestDTO;
import com.inventa.inventa.dto.venta.VentaResponseDTO;
import com.inventa.inventa.entity.Venta;
import com.inventa.inventa.mapper.VentaMapper;
import com.inventa.inventa.service.VentaService;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final VentaMapper ventaMapper;

    public VentaController(VentaService ventaService, VentaMapper ventaMapper) {
        this.ventaService = ventaService;
        this.ventaMapper = ventaMapper;
    }

    @GetMapping
    public List<VentaResponseDTO> listar() {
        return ventaService.listar().stream().map(ventaMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public VentaResponseDTO obtenerPorId(@PathVariable Integer id) {
        Venta venta = ventaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
        return ventaMapper.toResponse(venta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VentaResponseDTO crear(@RequestBody VentaRequestDTO dto) {
        Venta venta = new Venta();
        ventaMapper.updateEntityFromRequest(venta, dto, true);
        return ventaMapper.toResponse(ventaService.guardar(venta));
    }

    @PutMapping("/{id}")
    public VentaResponseDTO actualizar(@PathVariable Integer id, @RequestBody VentaRequestDTO dto) {
        Venta venta = ventaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
        ventaMapper.updateEntityFromRequest(venta, dto, false);
        return ventaMapper.toResponse(ventaService.guardar(venta));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (ventaService.buscarPorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada");
        }
        ventaService.eliminar(id);
    }
    // hola
}
