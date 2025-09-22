package com.inventa.inventa.controller;

import com.inventa.inventa.dto.categoria.CategoriaRequestDTO;
import com.inventa.inventa.dto.categoria.CategoriaResponseDTO;
import com.inventa.inventa.entity.Categoria;
import com.inventa.inventa.service.CategoriaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<CategoriaResponseDTO> listar() {
        return categoriaService.listar().stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @PostMapping
    public CategoriaResponseDTO crear(@RequestBody CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());

        Categoria guardada = categoriaService.guardar(categoria);
        return toResponseDTO(guardada);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        categoriaService.eliminar(id);
    }

    // 🔹 Mapper interno
    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setCategoriaId(categoria.getCategoriaId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        return dto;
    }
}
