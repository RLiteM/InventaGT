package com.inventa.inventa.controller;

import com.inventa.inventa.dto.categoria.CategoriaConteoDTO;
import com.inventa.inventa.dto.categoria.CategoriaRequestDTO;
import com.inventa.inventa.dto.categoria.CategoriaResponseDTO;
import com.inventa.inventa.dto.producto.ProductoResponseDTO;
import com.inventa.inventa.entity.Categoria;
import com.inventa.inventa.mapper.CategoriaMapper;
import com.inventa.inventa.mapper.ProductoMapper;
import com.inventa.inventa.service.CategoriaService;
import com.inventa.inventa.service.ProductoService;
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
    private final ProductoService productoService;
    private final ProductoMapper productoMapper;

    public CategoriaController(CategoriaService categoriaService, CategoriaMapper categoriaMapper, ProductoService productoService, ProductoMapper productoMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
        this.productoService = productoService;
        this.productoMapper = productoMapper;
    }

    @GetMapping
    public List<CategoriaConteoDTO> listar() {
        return categoriaService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponseDTO crear(@RequestBody CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria();
        categoriaMapper.updateEntityFromRequest(categoria, dto);

        Categoria guardada = categoriaService.guardar(categoria);
        return categoriaMapper.toResponse(guardada);
    }

    @PutMapping("/{id}")
    public CategoriaResponseDTO actualizar(@PathVariable Integer id, @RequestBody CategoriaRequestDTO dto) {
        Categoria categoria = categoriaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        categoriaMapper.updateEntityFromRequest(categoria, dto);

        Categoria actualizada = categoriaService.actualizar(categoria);

        return categoriaMapper.toResponse(actualizada);
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

    @GetMapping("/{id}/productos")
    public List<ProductoResponseDTO> listarProductosPorCategoria(@PathVariable Integer id) {
        // First, check if category exists
        categoriaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));

        return productoService.findByCategoriaId(id).stream()
                .map(productoMapper::toResponse)
                .collect(Collectors.toList());
    }

}