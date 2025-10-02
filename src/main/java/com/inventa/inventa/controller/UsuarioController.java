package com.inventa.inventa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.usuario.UsuarioRequestDTO;
import com.inventa.inventa.dto.usuario.UsuarioResponseDTO;
import com.inventa.inventa.entity.Usuario;
import com.inventa.inventa.mapper.UsuarioMapper;
import com.inventa.inventa.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return usuarioService.listar().stream().map(usuarioMapper::toResponse).collect(Collectors.toList());
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
        Usuario usuario = new Usuario();
        usuarioMapper.updateEntityFromRequest(usuario, dto, true);
        return usuarioMapper.toResponse(usuarioService.guardar(usuario));
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO actualizar(@PathVariable Integer id, @RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        usuarioMapper.updateEntityFromRequest(usuario, dto, false);
        return usuarioMapper.toResponse(usuarioService.guardar(usuario));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (usuarioService.buscarPorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        usuarioService.eliminar(id);
    }

    @GetMapping("/nombre/{nombreUsuario}")
    public UsuarioResponseDTO obtenerPorNombreUsuario(@PathVariable String nombreUsuario) {
        Usuario usuario = usuarioService.buscarPorNombreUsuario(nombreUsuario);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ese nombre de usuario");
        }
        return usuarioMapper.toResponse(usuario);
    }
}
