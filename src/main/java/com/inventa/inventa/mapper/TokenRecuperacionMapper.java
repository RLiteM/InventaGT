package com.inventa.inventa.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.tokenrecuperacion.TokenRecuperacionRequestDTO;
import com.inventa.inventa.dto.tokenrecuperacion.TokenRecuperacionResponseDTO;
import com.inventa.inventa.entity.TokenRecuperacion;
import com.inventa.inventa.entity.Usuario;
import com.inventa.inventa.service.UsuarioService;

@Component
public class TokenRecuperacionMapper {

    private final UsuarioService usuarioService;

    public TokenRecuperacionMapper(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void updateEntityFromRequest(TokenRecuperacion token, TokenRecuperacionRequestDTO dto, boolean esCreacion) {
        Usuario usuario = usuarioService.buscarPorId(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        token.setUsuario(usuario);
        if (dto.getToken() != null) {
            token.setToken(dto.getToken());
        } else if (esCreacion) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El token es obligatorio");
        }
        if (dto.getFechaExpiracion() != null) {
            token.setFechaExpiracion(dto.getFechaExpiracion());
        }
        token.setUsado(dto.getUsado() != null && dto.getUsado() ? 1 : 0);
    }

    public TokenRecuperacionResponseDTO toResponse(TokenRecuperacion token) {
        TokenRecuperacionResponseDTO dto = new TokenRecuperacionResponseDTO();
        dto.setTokenId(token.getTokenId());
        dto.setUsuarioId(token.getUsuario().getUsuarioId());
        dto.setNombreUsuario(token.getUsuario().getNombreCompleto());
        dto.setToken(token.getToken());
        dto.setFechaExpiracion(token.getFechaExpiracion());
        dto.setUsado(token.getUsado() == 1);
        return dto;
    }
}
