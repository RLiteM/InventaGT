package com.inventa.inventa.service;

import java.util.Optional;
import java.util.List;
import org.springframework.stereotype.Service;

import com.inventa.inventa.entity.Rol;
import com.inventa.inventa.repository.RolRepository;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<Rol> obtenerTodos() {
        return rolRepository.findAll();
    }

    public Optional<Rol> buscarPorId(Integer id) {
        return rolRepository.findById(id);
    }

    public Rol crear(Rol rol) {
        return rolRepository.save(rol);
    }

    public Rol actualizar(Integer id, Rol rolActualizado) {
        return rolRepository.findById(id)
                .map(rol -> {
                    rol.setNombreRol(rolActualizado.getNombreRol());
                    return rolRepository.save(rol);
                })
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
    }

    public void eliminar(Integer id) {
        if (!rolRepository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado");
        }
        rolRepository.deleteById(id);
    }

    public Optional<Rol> buscarPorNombre(String nombreRol) {
        return Optional.ofNullable(rolRepository.findByNombreRol(nombreRol));
    }
}
