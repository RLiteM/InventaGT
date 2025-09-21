package com.inventa.inventa.repositories;

import com.inventa.inventa.entities.TokenRecuperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRecuperacionRepository extends JpaRepository<TokenRecuperacion, Integer> {
    TokenRecuperacion findByToken(String token);
}
