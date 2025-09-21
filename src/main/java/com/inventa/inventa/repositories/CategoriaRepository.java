package com.inventa.inventa.repositories;

import com.inventa.inventa.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    Categoria findByNombre(String nombre);
}
