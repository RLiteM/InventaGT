package com.inventa.inventa.repositories;

import com.inventa.inventa.entities.TokenRecuperacion;
import com.inventa.inventa.entities.Usuario;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRecuperacionRepository extends JpaRepository<TokenRecuperacion, Integer> {
 Optional<TokenRecuperacion> findByToken(String token);

    Optional<TokenRecuperacion> findByUsuario(Usuario usuario);

    void deleteByUsuario(Usuario usuario);

}
