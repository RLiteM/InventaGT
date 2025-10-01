package com.inventa.inventa.controller;

import com.inventa.inventa.dto.ajusteinventario.AjusteInventarioRequestDTO;
import com.inventa.inventa.dto.ajusteinventario.AjusteInventarioResponseDTO;
import com.inventa.inventa.entity.AjusteInventario;
import com.inventa.inventa.mapper.AjusteInventarioMapper;
import com.inventa.inventa.service.AjusteInventarioService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ajustes-inventario")
public class AjusteInventarioController {

    private final AjusteInventarioService ajusteInventarioService;
    private final AjusteInventarioMapper ajusteInventarioMapper;

    public AjusteInventarioController(AjusteInventarioService ajusteInventarioService,
            AjusteInventarioMapper ajusteInventarioMapper) {
        this.ajusteInventarioService = ajusteInventarioService;
        this.ajusteInventarioMapper = ajusteInventarioMapper;
    }

    @GetMapping
    public List<AjusteInventarioResponseDTO> listar() {
        return ajusteInventarioService.listar().stream()
                .map(ajusteInventarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AjusteInventarioResponseDTO obtenerPorId(@PathVariable Integer id) {
        AjusteInventario ajuste = ajusteInventarioService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ajuste no encontrado"));
        return ajusteInventarioMapper.toResponse(ajuste);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AjusteInventarioResponseDTO crear(@RequestBody AjusteInventarioRequestDTO dto) {
        AjusteInventario guardado = ajusteInventarioService.guardarDesdeDto(dto);
        return ajusteInventarioMapper.toResponse(guardado);
    }

    @PutMapping("/{id}")
    public AjusteInventarioResponseDTO actualizar(@PathVariable Integer id,
            @RequestBody AjusteInventarioRequestDTO dto) {
        AjusteInventario actualizado = ajusteInventarioService.actualizarDesdeDto(id, dto);
        return ajusteInventarioMapper.toResponse(actualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (ajusteInventarioService.buscarPorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ajuste no encontrado");
        }
        ajusteInventarioService.eliminar(id);
    }
}
