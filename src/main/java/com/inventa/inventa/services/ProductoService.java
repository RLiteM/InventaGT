package com.inventa.inventa.services;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.inventa.inventa.entities.Producto;
import com.inventa.inventa.repositories.ProductoRepository;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Integer id) {
        return productoRepository.findById(id);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }

    public Producto buscarPorSku(String sku) {
        return productoRepository.findBySku(sku);
    }
}
