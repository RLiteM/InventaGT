package com.inventa.inventa.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.inventa.inventa.entity.Rol;
import com.inventa.inventa.repository.RolRepository;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public Optional<Rol> buscarPorId(Integer id) {
        return rolRepository.findById(id);
    }
}
