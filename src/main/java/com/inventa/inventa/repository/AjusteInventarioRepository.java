package com.inventa.inventa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventa.inventa.entity.AjusteInventario;

@Repository
public interface AjusteInventarioRepository extends JpaRepository<AjusteInventario, Integer> {
    
}
