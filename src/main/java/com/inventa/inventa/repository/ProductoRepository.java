package com.inventa.inventa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventa.inventa.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Producto findBySku(String sku);
}
