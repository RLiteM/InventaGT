package com.inventa.inventa.service;

import com.inventa.inventa.entity.DetalleVenta;
import com.inventa.inventa.repository.DetalleVentaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    // Listar todos
    public List<DetalleVenta> listar() {
        return detalleVentaRepository.findAll();
    }

    // Buscar por ID
    public Optional<DetalleVenta> buscarPorId(Integer id) {
        return detalleVentaRepository.findById(id);
    }

    // Guardar
    public DetalleVenta guardar(DetalleVenta detalle) {
        return detalleVentaRepository.save(detalle);
    }

    // Eliminar
    public void eliminar(DetalleVenta detalle) throws DataIntegrityViolationException {
        detalleVentaRepository.delete(detalle);
    }
}
