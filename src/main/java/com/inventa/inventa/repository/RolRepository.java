package com.inventa.inventa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventa.inventa.entity.Rol;


@Repository
public interface RolRepository extends JpaRepository<Rol, Integer>{

    Rol findByNombreRol(String nombreRol);
}
