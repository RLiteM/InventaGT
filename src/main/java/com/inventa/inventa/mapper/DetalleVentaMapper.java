package com.inventa.inventa.mapper;

import com.inventa.inventa.dto.detalleventa.DetalleVentaRequestDTO;
import com.inventa.inventa.dto.detalleventa.DetalleVentaResponseDTO;
import com.inventa.inventa.entity.DetalleVenta;
import com.inventa.inventa.entity.Lote;
import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.entity.Venta;
import com.inventa.inventa.repository.LoteRepository;
import com.inventa.inventa.repository.ProductoRepository;
import com.inventa.inventa.repository.VentaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class DetalleVentaMapper {

    private final VentaRepository ventaRepository;
    private final LoteRepository loteRepository;
    private final ProductoRepository productoRepository;

    public DetalleVentaMapper(VentaRepository ventaRepository,
                              LoteRepository loteRepository,
                              ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.loteRepository = loteRepository;
        this.productoRepository = productoRepository;
    }

    public void updateEntityFromRequest(DetalleVenta detalle, DetalleVentaRequestDTO dto) {
        Venta venta = ventaRepository.findById(dto.getVentaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
        Lote lote = loteRepository.findById(dto.getLoteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote no encontrado"));

        detalle.setVenta(venta);
        detalle.setLote(lote);
        detalle.setCantidad(dto.getCantidad());
        detalle.setPrecioUnitarioVenta(dto.getPrecioUnitarioVenta());
        detalle.setSubtotal(dto.getSubtotal());
    }

    public DetalleVentaResponseDTO toResponse(DetalleVenta detalle) {
        DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();
        dto.setDetalleId(detalle.getDetalleId());

        if (detalle.getVenta() != null) {
            dto.setVentaId(detalle.getVenta().getVentaId());
        }
        if (detalle.getLote() != null) {
            dto.setLoteId(detalle.getLote().getLoteId());
            // Obtenemos el producto asociado al lote
            Producto producto = detalle.getLote().getProducto();
            if (producto != null) {
                dto.setProductoId(producto.getProductoId());
                dto.setProductoNombre(producto.getNombre());
            }
        }
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitarioVenta(detalle.getPrecioUnitarioVenta());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }
}
