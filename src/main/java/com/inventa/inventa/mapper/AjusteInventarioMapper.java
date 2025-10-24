package com.inventa.inventa.mapper;

import org.springframework.stereotype.Component;

import com.inventa.inventa.dto.ajusteinventario.AjusteInventarioRequestDTO;
import com.inventa.inventa.dto.ajusteinventario.AjusteInventarioResponseDTO;
import com.inventa.inventa.entity.AjusteInventario;
import com.inventa.inventa.entity.AjusteInventario.TipoAjuste;
import com.inventa.inventa.entity.Lote;
import com.inventa.inventa.entity.Usuario;

@Component
public class AjusteInventarioMapper {

    public AjusteInventario toEntity(AjusteInventarioRequestDTO dto, Lote lote, Usuario usuario) {
        AjusteInventario entity = new AjusteInventario();
        entity.setLote(lote);
        entity.setUsuario(usuario);
        entity.setTipoAjuste(dto.getTipoAjuste());
        entity.setCantidad(dto.getCantidad());

        String motivo = dto.getMotivoAjuste().name();
        if (dto.getDescripcion() != null && !dto.getDescripcion().trim().isEmpty()) {
            motivo += ": " + dto.getDescripcion().trim();
        }
        entity.setMotivo(motivo);

        return entity;
    }

    public AjusteInventarioResponseDTO toResponse(AjusteInventario ajuste) {
        AjusteInventarioResponseDTO dto = new AjusteInventarioResponseDTO();
        dto.setAjusteId(ajuste.getAjusteId());
        if (ajuste.getLote() != null) {
            dto.setLoteId(ajuste.getLote().getLoteId());
            Producto producto = ajuste.getLote().getProducto();
            if (producto != null) {
                dto.setProductoId(producto.getProductoId());
                dto.setProductoNombre(producto.getNombre());
            }
        }
        if (ajuste.getUsuario() != null) {
            dto.setUsuarioId(ajuste.getUsuario().getUsuarioId());
            if (ajuste.getUsuario().getNombreCompleto() != null) {
                dto.setUsuarioNombre(ajuste.getUsuario().getNombreCompleto());
            }
        }
        dto.setFechaAjuste(ajuste.getFechaAjuste());
        if (ajuste.getTipoAjuste() != null) {
            dto.setTipoAjuste(ajuste.getTipoAjuste().name());
        }
        dto.setCantidad(ajuste.getCantidad());
        dto.setMotivo(ajuste.getMotivo());
        return dto;
    }
}