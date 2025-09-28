package com.inventa.inventa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.compra.CompraRequestDTO;
import com.inventa.inventa.dto.compra.CompraResponseDTO;
import com.inventa.inventa.entity.Compra;
import com.inventa.inventa.mapper.CompraMapper;
import com.inventa.inventa.service.CompraService;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;
    private final CompraMapper compraMapper;

    public CompraController(CompraService compraService, CompraMapper compraMapper) {
        this.compraService = compraService;
        this.compraMapper = compraMapper;
    }

    @GetMapping
    public List<CompraResponseDTO> listar() {
        return compraService.listar().stream().map(compraMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CompraResponseDTO obtenerPorId(@PathVariable Integer id) {
        Compra compra = compraService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada"));
        return compraMapper.toResponse(compra);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompraResponseDTO crear(@RequestBody CompraRequestDTO dto) {
        Compra compra = new Compra();
        compraMapper.updateEntityFromRequest(compra, dto);
        return compraMapper.toResponse(compraService.guardar(compra));
    }

    @PutMapping("/{id}")
    public CompraResponseDTO actualizar(@PathVariable Integer id, @RequestBody CompraRequestDTO dto) {
        Compra compra = compraService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada"));
        compraMapper.updateEntityFromRequest(compra, dto);
        return compraMapper.toResponse(compraService.guardar(compra));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (compraService.buscarPorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada");
        }
        compraService.eliminar(id);
    }
}
