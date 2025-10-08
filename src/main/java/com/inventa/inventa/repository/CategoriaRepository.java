package com.inventa.inventa.repository;

import com.inventa.inventa.dto.categoria.CategoriaConteoDTO;
import com.inventa.inventa.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    Categoria findByNombre(String nombre);

    @Query("SELECT new com.inventa.inventa.dto.categoria.CategoriaConteoDTO(c.categoriaId, c.nombre, c.descripcion, COUNT(p)) FROM Categoria c LEFT JOIN c.productos p GROUP BY c.categoriaId, c.nombre, c.descripcion")
    List<CategoriaConteoDTO> findAllWithProductCount();
}