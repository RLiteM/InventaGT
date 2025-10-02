package com.inventa.inventa.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.lote.LoteRequestDTO;
import com.inventa.inventa.dto.lote.LoteResponseDTO;
import com.inventa.inventa.entity.Lote;
import com.inventa.inventa.mapper.LoteMapper;
import com.inventa.inventa.service.LoteService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lotes")
public class LoteController {

    private final LoteService loteService;
    private final LoteMapper loteMapper;

    public LoteController(LoteService loteService, LoteMapper loteMapper) {
        this.loteService = loteService;
        this.loteMapper = loteMapper;
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
        Lote lote = new Lote();
        loteMapper.updateEntityFromRequest(lote, dto);
        return loteMapper.toResponse(loteService.guardar(lote));
    }

    // =========================
    // ACTUALIZAR LOTE
    // =========================
    @PutMapping("/{id}")
    public LoteResponseDTO actualizar(@PathVariable Integer id, @RequestBody LoteRequestDTO dto) {
        Lote lote = loteService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote no encontrado"));
        loteMapper.updateEntityFromRequest(lote, dto);
        return loteMapper.toResponse(loteService.guardar(lote));
    }

    // =========================
    // ELIMINAR LOTE
    // =========================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarLote(@PathVariable Integer id) {
        Lote lote = loteService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote no encontrado"));
        try {
            loteService.eliminar(lote);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede eliminar lote por restricciones de FK");
        }
    }
}
