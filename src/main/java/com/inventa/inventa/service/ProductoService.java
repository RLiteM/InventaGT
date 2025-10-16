package com.inventa.inventa.service;

import com.inventa.inventa.dto.producto.ProductoRapidoRequestDTO;
import com.inventa.inventa.dto.producto.ProductoRequestDTO;
import com.inventa.inventa.dto.producto.ProductoResponseDTO;
import com.inventa.inventa.entity.Categoria;
import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.exceptions.BadRequestException;
import com.inventa.inventa.exceptions.NotFoundException;
import com.inventa.inventa.mapper.ProductoMapper;
import com.inventa.inventa.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final CategoriaService categoriaService;

    public ProductoService(ProductoRepository productoRepository, ProductoMapper productoMapper, CategoriaService categoriaService) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
        this.categoriaService = categoriaService;
    }

    public List<Producto> listar(String searchTerm, Integer proveedorId) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return productoRepository.findAll();
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

    public Producto crearProductoRapido(ProductoRapidoRequestDTO dto) {
        // 1. Validar que el SKU no exista
        if (productoRepository.findBySku(dto.getSku()) != null) {
            throw new BadRequestException("Ya existe un producto con el SKU: " + dto.getSku());
        }

        // 2. Obtener la categoría
        Categoria categoria = categoriaService.buscarPorId(dto.getCategoriaId())
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con id: " + dto.getCategoriaId()));

        // 3. Crear la nueva entidad Producto
        Producto producto = new Producto();
        producto.setSku(dto.getSku());
        producto.setNombre(dto.getNombre());
        producto.setCategoria(categoria);
        producto.setStockMinimo(dto.getStockMinimo());

        // 4. Asignar valores por defecto
        producto.setDescripcion(""); // Opcional: descripción vacía
        producto.setUltimoCosto(BigDecimal.ZERO);
        producto.setPrecioMinorista(BigDecimal.ZERO);
        producto.setPrecioMayorista(BigDecimal.ZERO);
        producto.setStockActual(BigDecimal.ZERO);
        producto.setUnidadMedida("Unidad"); // Valor por defecto

        // 5. Guardar y devolver el nuevo producto
        return productoRepository.save(producto);
    }

    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }

    public Producto buscarPorSku(String sku) {
        return productoRepository.findBySku(sku);
    }

    public ProductoResponseDTO partialUpdate(Integer id, ProductoRequestDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con id: " + id));

        if (dto.getSku() != null) {
            producto.setSku(dto.getSku());
        }
        if (dto.getNombre() != null) {
            producto.setNombre(dto.getNombre());
        }
        if (dto.getDescripcion() != null) {
            producto.setDescripcion(dto.getDescripcion());
        }
        if (dto.getUltimoCosto() != null) {
            producto.setUltimoCosto(dto.getUltimoCosto());
        }
        if (dto.getPrecioMinorista() != null) {
            producto.setPrecioMinorista(dto.getPrecioMinorista());
        }
        if (dto.getPrecioMayorista() != null) {
            producto.setPrecioMayorista(dto.getPrecioMayorista());
        }
        if (dto.getStockActual() != null) {
            producto.setStockActual(dto.getStockActual());
        }
        if (dto.getStockMinimo() != null) {
            producto.setStockMinimo(dto.getStockMinimo());
        }
        if (dto.getUnidadMedida() != null) {
            producto.setUnidadMedida(dto.getUnidadMedida());
        }
        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaService.buscarPorId(dto.getCategoriaId())
                    .orElseThrow(() -> new NotFoundException("Categoría no encontrada"));
            producto.setCategoria(categoria);
        }

        Producto updatedProducto = productoRepository.save(producto);
        return productoMapper.toResponse(updatedProducto);
    }
}