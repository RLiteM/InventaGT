package com.inventa.inventa.repositories;

import com.inventa.inventa.entities.AjusteInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AjusteInventarioRepository extends JpaRepository<AjusteInventario, Integer> {
}
