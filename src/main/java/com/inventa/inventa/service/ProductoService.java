package com.inventa.inventa.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.dto.producto.ActualizarPreciosDTO;
import com.inventa.inventa.dto.producto.ProductoResponseDTO;
import com.inventa.inventa.exceptions.NotFoundException;
import com.inventa.inventa.mapper.ProductoMapper;
import com.inventa.inventa.dto.producto.ProductoNombreSkuDTO;
import java.util.stream.Collectors;
import com.inventa.inventa.repository.ProductoRepository;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    public ProductoService(ProductoRepository productoRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    public ProductoResponseDTO updatePrices(Integer id, ActualizarPreciosDTO preciosDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con id: " + id));

        if (preciosDTO.getPrecioMinorista() != null) {
            producto.setPrecioMinorista(preciosDTO.getPrecioMinorista());
        }
        if (preciosDTO.getPrecioMayorista() != null) {
            producto.setPrecioMayorista(preciosDTO.getPrecioMayorista());
        }

        Producto updatedProducto = productoRepository.save(producto);
        return productoMapper.toResponse(updatedProducto);
    }

    public List<ProductoResponseDTO> listar(String searchTerm, Integer proveedorId) {
        List<Producto> productos;
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            productos = productoRepository.findAll();
        } else if (proveedorId != null) {
            productos = productoRepository.findByProveedorAndSearchTerm(proveedorId, searchTerm);
        } else {
            productos = productoRepository.findByNombreContainingIgnoreCaseOrSkuContainingIgnoreCase(searchTerm, searchTerm);
        }
        return productos.stream().map(productoMapper::toResponse).collect(Collectors.toList());
    }

    public List<ProductoNombreSkuDTO> listarTodos() {
        return productoRepository.findAll().stream().map(productoMapper::toNombreSkuDTO).collect(Collectors.toList());
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
