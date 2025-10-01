package com.inventa.inventa.service;

import com.inventa.inventa.entity.DetalleCompra;
import com.inventa.inventa.repository.DetalleCompraRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleCompraService {

    private final DetalleCompraRepository detalleCompraRepository;

    public DetalleCompraService(DetalleCompraRepository detalleCompraRepository) {
        this.detalleCompraRepository = detalleCompraRepository;
    }

    // Listar todos los detalles
    public List<DetalleCompra> listar() {
        return detalleCompraRepository.findAll();
    }

    // Buscar por ID
    public Optional<DetalleCompra> buscarPorId(Integer id) {
        return detalleCompraRepository.findById(id);
    }

    // Guardar un detalle
    public DetalleCompra guardar(DetalleCompra detalle) {
        return detalleCompraRepository.save(detalle);
    }

    // Eliminar un detalle por entidad
    public void eliminar(DetalleCompra detalle) throws DataIntegrityViolationException {
        detalleCompraRepository.delete(detalle);
    }
}
