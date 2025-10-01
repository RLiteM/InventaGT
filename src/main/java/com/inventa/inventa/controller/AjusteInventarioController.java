package com.inventa.inventa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.ajusteinventario.AjusteInventarioRequestDTO;
import com.inventa.inventa.dto.ajusteinventario.AjusteInventarioResponseDTO;
import com.inventa.inventa.entity.AjusteInventario;
import com.inventa.inventa.mapper.AjusteInventarioMapper;
import com.inventa.inventa.service.AjusteInventarioService;

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
        return ajusteInventarioService.listar().stream().map(ajusteInventarioMapper::toResponse)
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
        AjusteInventario ajuste = new AjusteInventario();
        ajusteInventarioMapper.updateEntityFromRequest(ajuste, dto);
        AjusteInventario guardado = ajusteInventarioService.guardar(ajuste);
        return ajusteInventarioMapper.toResponse(guardado);
    }

    @PutMapping("/{id}")
    public AjusteInventarioResponseDTO actualizar(@PathVariable Integer id,
            @RequestBody AjusteInventarioRequestDTO dto) {
        AjusteInventario ajusteActualizado = new AjusteInventario();
        ajusteInventarioMapper.updateEntityFromRequest(ajusteActualizado, dto);
        AjusteInventario actualizado = ajusteInventarioService.actualizar(id, ajusteActualizado);
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
