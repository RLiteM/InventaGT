package com.inventa.inventa.repositories;

import com.inventa.inventa.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Producto findBySku(String sku);
}
