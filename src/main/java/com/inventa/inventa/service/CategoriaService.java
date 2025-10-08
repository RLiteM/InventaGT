package com.inventa.inventa.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.inventa.inventa.dto.categoria.CategoriaConteoDTO;
import com.inventa.inventa.entity.Categoria;
import com.inventa.inventa.repository.CategoriaRepository;
import com.inventa.inventa.exceptions.BadRequestException;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaConteoDTO> listar() {
        return categoriaRepository.findAllWithProductCount();
    }

    public Optional<Categoria> buscarPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    public Categoria guardar(Categoria categoria) {
        if (categoriaRepository.findByNombre(categoria.getNombre()) != null) {
            throw new BadRequestException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizar(Categoria categoria) {
        Categoria categoriaPorNombre = categoriaRepository.findByNombre(categoria.getNombre());
        if (categoriaPorNombre != null && !categoriaPorNombre.getCategoriaId().equals(categoria.getCategoriaId())) {
            throw new BadRequestException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Integer id) {
        categoriaRepository.deleteById(id);
    }

    public Categoria buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }
}
