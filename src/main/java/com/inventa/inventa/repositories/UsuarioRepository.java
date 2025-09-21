package com.inventa.inventa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.inventa.inventa.entities.Usuario;

@Repository
public interface UsuarioRepository  extends JpaRepository<Usuario, Integer> {
    Usuario findByNombreUsuario(String NombreUsuario);
    
}