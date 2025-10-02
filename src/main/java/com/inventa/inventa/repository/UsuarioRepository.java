package com.inventa.inventa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventa.inventa.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByNombreUsuario(String NombreUsuario);

    Usuario findByCuiDpi(String cuiDpi);
}