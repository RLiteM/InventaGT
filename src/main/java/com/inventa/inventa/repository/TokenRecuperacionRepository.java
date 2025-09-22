package com.inventa.inventa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventa.inventa.entity.TokenRecuperacion;
import com.inventa.inventa.entity.Usuario;

@Repository
public interface TokenRecuperacionRepository extends JpaRepository<TokenRecuperacion, Integer> {
 Optional<TokenRecuperacion> findByToken(String token);

    Optional<TokenRecuperacion> findByUsuario(Usuario usuario);

    void deleteByUsuario(Usuario usuario);

}
