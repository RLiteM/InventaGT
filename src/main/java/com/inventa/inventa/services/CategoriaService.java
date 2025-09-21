package com.inventa.inventa.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.inventa.inventa.entities.Categoria;
import com.inventa.inventa.repositories.CategoriaRepository;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    public Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void eliminar(Integer id) {
        categoriaRepository.deleteById(id);
    }

    public Categoria buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }
}
