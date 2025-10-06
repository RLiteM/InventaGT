package com.inventa.inventa.controller;

import com.inventa.inventa.dto.usuario.UsuarioRequestDTO;
import com.inventa.inventa.dto.usuario.UsuarioResponseDTO;
import com.inventa.inventa.entity.Usuario;
import com.inventa.inventa.mapper.UsuarioMapper;
import com.inventa.inventa.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return usuarioService.listar().stream()
                .map(usuarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO obtenerPorId(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return usuarioMapper.toResponse(usuario);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO crear(@RequestBody UsuarioRequestDTO dto) {
        Usuario nuevoUsuario = usuarioMapper.toEntity(dto);
        return usuarioMapper.toResponse(usuarioService.guardar(nuevoUsuario));
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO actualizar(@PathVariable Integer id, @RequestBody UsuarioRequestDTO dto, @AuthenticationPrincipal Usuario currentUser) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, dto, currentUser);
        return usuarioMapper.toResponse(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
    }
}