package com.inventa.inventa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventa.inventa.entity.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {
}
