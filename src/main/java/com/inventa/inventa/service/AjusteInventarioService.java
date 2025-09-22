package com.inventa.inventa.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.inventa.inventa.entity.AjusteInventario;
import com.inventa.inventa.repository.AjusteInventarioRepository;

@Service
public class AjusteInventarioService {
    private final AjusteInventarioRepository ajusteInventarioRepository;

    public AjusteInventarioService(AjusteInventarioRepository ajusteInventarioRepository) {
        this.ajusteInventarioRepository = ajusteInventarioRepository;
    }

    public List<AjusteInventario> listar() {
        return ajusteInventarioRepository.findAll();
    }

    public Optional<AjusteInventario> buscarPorId(Integer id) {
        return ajusteInventarioRepository.findById(id);
    }

    public AjusteInventario guardar(AjusteInventario ajuste) {
        return ajusteInventarioRepository.save(ajuste);
    }

    public void eliminar(Integer id) {
        ajusteInventarioRepository.deleteById(id);
    }
}
