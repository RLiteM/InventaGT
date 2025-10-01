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
        entity.setTipoAjuste(TipoAjuste.valueOf(dto.getTipoAjuste()));
        entity.setCantidad(dto.getCantidad());
        entity.setMotivo(dto.getMotivo());
        return entity;
    }

    public AjusteInventarioResponseDTO toResponse(AjusteInventario ajuste) {
        AjusteInventarioResponseDTO dto = new AjusteInventarioResponseDTO();
        dto.setAjusteId(ajuste.getAjusteId());
        if (ajuste.getLote() != null) {
            dto.setLoteId(ajuste.getLote().getLoteId());
            if (ajuste.getLote().getProducto() != null) {
                dto.setProductoId(ajuste.getLote().getProducto().getProductoId());
                dto.setProductoNombre(ajuste.getLote().getProducto().getNombre());
            }
        }
        if (ajuste.getUsuario() != null) {
            dto.setUsuarioId(ajuste.getUsuario().getUsuarioId());
            dto.setUsuarioNombre(ajuste.getUsuario().getNombreCompleto());
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