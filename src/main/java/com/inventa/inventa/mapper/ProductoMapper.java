package com.inventa.inventa.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.producto.ProductoNombreSkuDTO;
import com.inventa.inventa.dto.producto.ProductoRequestDTO;
import com.inventa.inventa.dto.producto.ProductoResponseDTO;
import com.inventa.inventa.entity.Categoria;
import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.service.CategoriaService;

@Component
public class ProductoMapper {

    private final CategoriaService categoriaService;

    public ProductoMapper(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public void updateEntityFromRequest(Producto producto, ProductoRequestDTO dto) {
        producto.setSku(dto.getSku());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setUltimoCosto(dto.getUltimoCosto());
        producto.setPrecioMinorista(dto.getPrecioMinorista());
        producto.setPrecioMayorista(dto.getPrecioMayorista());
        producto.setStockActual(dto.getStockActual());
        producto.setStockMinimo(dto.getStockMinimo());
        producto.setUnidadMedida(dto.getUnidadMedida());

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaService.buscarPorId(dto.getCategoriaId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
            producto.setCategoria(categoria);
        } else {
            producto.setCategoria(null);
        }
    }

    public ProductoResponseDTO toResponse(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setProductoId(producto.getProductoId());
        dto.setSku(producto.getSku());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setUltimoCosto(producto.getUltimoCosto());
        dto.setPrecioMinorista(producto.getPrecioMinorista());
        dto.setPrecioMayorista(producto.getPrecioMayorista());
        dto.setStockActual(producto.getStockActual());
        dto.setStockMinimo(producto.getStockMinimo());
        dto.setUnidadMedida(producto.getUnidadMedida());

        if (producto.getCategoria() != null) {
            dto.setCategoriaId(producto.getCategoria().getCategoriaId());
            dto.setCategoriaNombre(producto.getCategoria().getNombre());
        }

        return dto;
    }

    public ProductoNombreSkuDTO toNombreSkuDTO(Producto producto) {
        return new ProductoNombreSkuDTO(producto.getNombre(), producto.getSku());
    }
}
