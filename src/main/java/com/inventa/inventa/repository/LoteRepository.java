package com.inventa.inventa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventa.inventa.entity.Lote;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Integer> {
}
