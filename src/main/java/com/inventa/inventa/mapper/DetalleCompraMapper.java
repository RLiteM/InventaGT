package com.inventa.inventa.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.detallecompra.DetalleCompraRequestDTO;
import com.inventa.inventa.dto.detallecompra.DetalleCompraResponseDTO;
import com.inventa.inventa.entity.Compra;
import com.inventa.inventa.entity.DetalleCompra;
import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.repository.CompraRepository;
import com.inventa.inventa.repository.ProductoRepository;

@Component
public class DetalleCompraMapper {

    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;

    public DetalleCompraMapper(CompraRepository compraRepository, ProductoRepository productoRepository) {
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
    }

    public DetalleCompraResponseDTO toResponse(DetalleCompra detalle) {
        DetalleCompraResponseDTO dto = new DetalleCompraResponseDTO();
        dto.setDetalleCompraId(detalle.getDetalleCompraId());
        if (detalle.getCompra() != null) {
            dto.setCompraId(detalle.getCompra().getCompraId());
        }
        if (detalle.getProducto() != null) {
            dto.setProductoId(detalle.getProducto().getProductoId());
            dto.setProductoNombre(detalle.getProducto().getNombre());
        }
        dto.setCantidad(detalle.getCantidad());
        dto.setCostoUnitarioCompra(detalle.getCostoUnitarioCompra());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }
}
