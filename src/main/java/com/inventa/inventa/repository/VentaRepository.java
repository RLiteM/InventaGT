package com.inventa.inventa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventa.inventa.entity.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
}
