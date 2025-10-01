package com.inventa.inventa.mapper;

import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.ajusteinventario.AjusteInventarioRequestDTO;
import com.inventa.inventa.dto.ajusteinventario.AjusteInventarioResponseDTO;
import com.inventa.inventa.entity.AjusteInventario;
import com.inventa.inventa.entity.AjusteInventario.TipoAjuste;
import com.inventa.inventa.entity.Lote;
import com.inventa.inventa.entity.Usuario;
import com.inventa.inventa.service.LoteService;
import com.inventa.inventa.service.UsuarioService;

@Component
public class AjusteInventarioMapper {

    private final LoteService loteService;
    private final UsuarioService usuarioService;

    public AjusteInventarioMapper(LoteService loteService, UsuarioService usuarioService) {
        this.loteService = loteService;
        this.usuarioService = usuarioService;
    }

    public void updateEntityFromRequest(AjusteInventario ajuste, AjusteInventarioRequestDTO dto) {
        if (dto.getLoteId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El identificador del lote es obligatorio");
        }
        if (dto.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El identificador del usuario es obligatorio");
        }
        Lote lote = loteService.buscarPorId(dto.getLoteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote no encontrado"));
        Usuario usuario = usuarioService.buscarPorId(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        ajuste.setLote(lote);
        ajuste.setUsuario(usuario);
        ajuste.setTipoAjuste(parseTipoAjuste(dto.getTipoAjuste()));
        ajuste.setCantidad(dto.getCantidad());
        ajuste.setMotivo(dto.getMotivo());
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

    private TipoAjuste parseTipoAjuste(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo de ajuste es obligatorio");
        }
        String normalizado = tipo.trim().toUpperCase(Locale.ROOT);
        try {
            if ("ENTRADA".equals(normalizado)) {
                return TipoAjuste.Entrada;
            }
            if ("SALIDA".equals(normalizado)) {
                return TipoAjuste.Salida;
            }
            return TipoAjuste.valueOf(capitalize(normalizado));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de ajuste inválido");
        }
    }

    private String capitalize(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        String lower = value.toLowerCase(Locale.ROOT);
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
