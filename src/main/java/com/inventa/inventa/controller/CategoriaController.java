package com.inventa.inventa.controller;

import com.inventa.inventa.dto.categoria.CategoriaRequestDTO;
import com.inventa.inventa.dto.categoria.CategoriaResponseDTO;
import com.inventa.inventa.entity.Categoria;
import com.inventa.inventa.mapper.CategoriaMapper;
import com.inventa.inventa.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;

    public CategoriaController(CategoriaService categoriaService, CategoriaMapper categoriaMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
    }

    @GetMapping
    public List<CategoriaResponseDTO> listar() {
        return categoriaService.listar().stream().map(categoriaMapper::toResponse).collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponseDTO crear(@RequestBody CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria();
        categoriaMapper.updateEntityFromRequest(categoria, dto);

        Categoria guardada = categoriaService.guardar(categoria);
        return categoriaMapper.toResponse(guardada);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        Categoria categoria = categoriaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
        categoriaService.eliminar(categoria.getCategoriaId());
    }

    // para obtener una categoría por su ID
    @GetMapping("/{id}")
public CategoriaResponseDTO obtenerPorId(@PathVariable Integer id) {
    Categoria categoria = categoriaService.buscarPorId(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
    return categoriaMapper.toResponse(categoria);
}

}
