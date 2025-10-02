package com.inventa.inventa.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.lote.LoteRequestDTO;
import com.inventa.inventa.dto.lote.LoteResponseDTO;
import com.inventa.inventa.entity.Lote;
import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.entity.DetalleCompra;
import com.inventa.inventa.repository.ProductoRepository;
import com.inventa.inventa.repository.DetalleCompraRepository;

@Component
public class LoteMapper {

    private final ProductoRepository productoRepository;
    private final DetalleCompraRepository detalleCompraRepository;

    public LoteMapper(ProductoRepository productoRepository,
                      DetalleCompraRepository detalleCompraRepository) {
        this.productoRepository = productoRepository;
        this.detalleCompraRepository = detalleCompraRepository;
    }

    // Actualizar entidad desde DTO
    public void updateEntityFromRequest(Lote lote, LoteRequestDTO dto) {
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        DetalleCompra detalleCompra = detalleCompraRepository.findById(dto.getDetalleCompraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DetalleCompra no encontrado"));

        lote.setProducto(producto);
        lote.setDetalleCompra(detalleCompra);
        lote.setFechaCaducidad(dto.getFechaCaducidad());
        lote.setCantidadInicial(dto.getCantidadInicial());
        lote.setCantidadActual(dto.getCantidadActual());
    }

    // Convertir entidad a DTO de respuesta
    public LoteResponseDTO toResponse(Lote lote) {
        LoteResponseDTO dto = new LoteResponseDTO();
        dto.setLoteId(lote.getLoteId());

        if (lote.getProducto() != null) {
            dto.setProductoId(lote.getProducto().getProductoId());
            dto.setProductoNombre(lote.getProducto().getNombre()); // Aquí nunca será null
            dto.setSkuProducto(lote.getProducto().getSku());       // Aquí tampoco
        }

        if (lote.getDetalleCompra() != null) {
            dto.setDetalleCompraId(lote.getDetalleCompra().getDetalleCompraId());
        }

        dto.setFechaCaducidad(lote.getFechaCaducidad());
        dto.setCantidadInicial(lote.getCantidadInicial());
        dto.setCantidadActual(lote.getCantidadActual());

        return dto;
    }
}
