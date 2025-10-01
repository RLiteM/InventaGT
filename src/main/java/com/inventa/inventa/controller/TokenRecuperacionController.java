package com.inventa.inventa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.tokenrecuperacion.TokenRecuperacionRequestDTO;
import com.inventa.inventa.dto.tokenrecuperacion.TokenRecuperacionResponseDTO;
import com.inventa.inventa.entity.TokenRecuperacion;
import com.inventa.inventa.mapper.TokenRecuperacionMapper;
import com.inventa.inventa.service.TokenRecuperacionService;

@RestController
@RequestMapping("/api/tokenrecuperacion")
public class TokenRecuperacionController {

    private final TokenRecuperacionService tokenService;
    private final TokenRecuperacionMapper tokenMapper;

    public TokenRecuperacionController(TokenRecuperacionService tokenService, TokenRecuperacionMapper tokenMapper) {
        this.tokenService = tokenService;
        this.tokenMapper = tokenMapper;
    }

    @GetMapping
    public List<TokenRecuperacionResponseDTO> listar() {
        return tokenService.listar().stream()
                .map(tokenMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{token}")
    public TokenRecuperacionResponseDTO obtenerPorToken(@PathVariable String token) {
        TokenRecuperacion tokenRec = tokenService.buscarPorToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token no encontrado"));
        return tokenMapper.toResponse(tokenRec);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TokenRecuperacionResponseDTO crear(@RequestBody TokenRecuperacionRequestDTO dto) {
        TokenRecuperacion token = tokenService.generarToken(
                tokenService.obtenerUsuarioPorId(dto.getUsuarioId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"))
        );
        return tokenMapper.toResponse(token);
    }

    @PutMapping("/usar/{token}")
    public void marcarComoUsado(@PathVariable String token) {
        if (!tokenService.validarToken(token)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido o expirado");
        }
        tokenService.marcarComoUsado(token);
    }
}

