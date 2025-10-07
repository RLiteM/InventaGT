package com.inventa.inventa.repository;

import com.inventa.inventa.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Producto findBySku(String sku);

    List<Producto> findByNombreContainingIgnoreCaseOrSkuContainingIgnoreCase(String nombre, String sku);
}
