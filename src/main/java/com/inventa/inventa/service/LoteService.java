package com.inventa.inventa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.inventa.inventa.entity.Lote;
import com.inventa.inventa.repository.LoteRepository;

@Service
public class LoteService {

    private final LoteRepository loteRepository;

    public LoteService(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    // Listar todos los lotes
    public List<Lote> listar() {
        return loteRepository.findAll();
    }

    // Buscar lote por ID
    public Optional<Lote> buscarPorId(Integer id) {
        return loteRepository.findById(id);
    }

    // Guardar o actualizar un lote
    public Lote guardar(Lote lote) {
        return loteRepository.save(lote);
    }

    // Eliminar lote por ID
    public void eliminarPorId(Integer id) {
        try {
            loteRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw e; // Lo maneja el controller
        }
    }
}
