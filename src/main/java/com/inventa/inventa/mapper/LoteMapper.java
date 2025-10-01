package com.inventa.inventa.mapper;

import org.springframework.stereotype.Component;
import com.inventa.inventa.dto.lote.LoteRequestDTO;
import com.inventa.inventa.dto.lote.LoteResponseDTO;
import com.inventa.inventa.entity.Lote;
import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.entity.DetalleCompra;

@Component
public class LoteMapper {

    public Lote toEntity(LoteRequestDTO dto, Producto producto, DetalleCompra detalleCompra) {
        Lote lote = new Lote();
        lote.setProducto(producto);
        lote.setDetalleCompra(detalleCompra);
        lote.setFechaCaducidad(dto.getFechaCaducidad());
        lote.setCantidadInicial(dto.getCantidadInicial());
        lote.setCantidadActual(dto.getCantidadActual());
        return lote;
    }

    public LoteResponseDTO toResponse(Lote lote) {
        LoteResponseDTO dto = new LoteResponseDTO();
        dto.setLoteId(lote.getLoteId());
        dto.setProductoId(lote.getProducto().getProductoId());
        dto.setDetalleCompraId(lote.getDetalleCompra().getDetalleCompraId());
        dto.setFechaCaducidad(lote.getFechaCaducidad());
        dto.setCantidadInicial(lote.getCantidadInicial());
        dto.setCantidadActual(lote.getCantidadActual());
        return dto;
    }
}
