package com.inventa.inventa.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.repository.ProductoRepository;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listar(String searchTerm, Integer proveedorId) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return new java.util.ArrayList<>(); // No buscar si no hay término de búsqueda
        }

        if (proveedorId != null) {
            return productoRepository.findByProveedorAndSearchTerm(proveedorId, searchTerm);
        } else {
            return productoRepository.findByNombreContainingIgnoreCaseOrSkuContainingIgnoreCase(searchTerm, searchTerm);
        }
    }

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public List<Producto> findByCategoriaId(Integer categoriaId) {
        return productoRepository.findByCategoria_CategoriaId(categoriaId);
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
