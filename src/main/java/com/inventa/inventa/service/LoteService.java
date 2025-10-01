package com.inventa.inventa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.inventa.inventa.entity.Lote;
import com.inventa.inventa.repository.LoteRepository;

@Service
public class LoteService {

    private final LoteRepository loteRepository;

    public LoteService(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    public List<Lote> listar() {
        return loteRepository.findAll();
    }

    public Optional<Lote> buscarPorId(Integer id) {
        return loteRepository.findById(id);
    }

    public Lote guardar(Lote lote) {
        return loteRepository.save(lote);
    }
}
