package com.inventa.inventa.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.inventa.inventa.entities.Compra;
import com.inventa.inventa.repositories.CompraRepository;

@Service
public class CompraService {
    private final CompraRepository compraRepository;

    public CompraService(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    public List<Compra> listar() {
        return compraRepository.findAll();
    }

    public Optional<Compra> buscarPorId(Integer id) {
        return compraRepository.findById(id);
    }

    public Compra guardar(Compra compra) {
        return compraRepository.save(compra);
    }

    public void eliminar(Integer id) {
        compraRepository.deleteById(id);
    }
}
