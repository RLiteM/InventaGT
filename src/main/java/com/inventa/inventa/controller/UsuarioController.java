package com.inventa.inventa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.usuario.UsuarioRequestDTO;
import com.inventa.inventa.dto.usuario.UsuarioResponseDTO;
import com.inventa.inventa.entity.Rol;
import com.inventa.inventa.entity.Usuario;
import com.inventa.inventa.repository.RolRepository;
import com.inventa.inventa.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;

    public UsuarioController(UsuarioService usuarioService, RolRepository rolRepository) {
        this.usuarioService = usuarioService;
        this.rolRepository = rolRepository;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return usuarioService.listar().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO obtenerPorId(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return toResponse(usuario);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO crear(@RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        aplicarCambios(usuario, dto, true);
        return toResponse(usuarioService.guardar(usuario));
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO actualizar(@PathVariable Integer id, @RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        aplicarCambios(usuario, dto, false);
        return toResponse(usuarioService.guardar(usuario));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (usuarioService.buscarPorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        usuarioService.eliminar(id);
    }

    private void aplicarCambios(Usuario usuario, UsuarioRequestDTO dto, boolean esCreacion) {
        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setCuiDpi(dto.getCuiDpi());
        usuario.setNombreUsuario(dto.getNombreUsuario());
        if (esCreacion || (dto.getContrasena() != null && !dto.getContrasena().isBlank())) {
            usuario.setContrasena(dto.getContrasena());
        }
        if (dto.getRolId() != null) {
            usuario.setRol(obtenerRol(dto.getRolId()));
        } else if (esCreacion) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol es obligatorio");
        }
        usuario.setCorreo(dto.getCorreo());
        usuario.setTelefono(dto.getTelefono());
    }

    private Rol obtenerRol(Integer rolId) {
        return rolRepository.findById(rolId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));
    }

    private UsuarioResponseDTO toResponse(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setUsuarioId(usuario.getUsuarioId());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setCuiDpi(usuario.getCuiDpi());
        dto.setNombreUsuario(usuario.getNombreUsuario());
        if (usuario.getRol() != null) {
            dto.setRolId(usuario.getRol().getRolId());
            dto.setNombreRol(usuario.getRol().getNombreRol());
        }
        dto.setFechaCreacion(usuario.getFechaCreacion());
        dto.setCorreo(usuario.getCorreo());
        dto.setTelefono(usuario.getTelefono());
        return dto;
    }
}
