package com.inventa.inventa.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.inventa.inventa.dto.usuario.UsuarioRequestDTO;
import com.inventa.inventa.dto.usuario.UsuarioResponseDTO;
import com.inventa.inventa.entity.Rol;
import com.inventa.inventa.entity.Usuario;
import com.inventa.inventa.service.RolService;

@Component
public class UsuarioMapper {

    private final RolService rolService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioMapper(RolService rolService, PasswordEncoder passwordEncoder) {
        this.rolService = rolService;
        this.passwordEncoder = passwordEncoder;
    }

    public void updateEntityFromRequest(Usuario usuario, UsuarioRequestDTO dto, boolean esCreacion) {
        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setCuiDpi(dto.getCuiDpi());
        usuario.setNombreUsuario(dto.getNombreUsuario());

        if (esCreacion || (dto.getContrasena() != null && !dto.getContrasena().isBlank())) {
            usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        }

        if (dto.getRolId() != null) {
            Rol rol = rolService.buscarPorId(dto.getRolId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));
            usuario.setRol(rol);
        } else if (esCreacion) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol es obligatorio");
        }

        usuario.setCorreo(dto.getCorreo());
        usuario.setTelefono(dto.getTelefono());
    }

    public Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setCuiDpi(dto.getCuiDpi());
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setContrasena(dto.getContrasena()); // La codificación se hace en el servicio
        usuario.setCorreo(dto.getCorreo());
        usuario.setTelefono(dto.getTelefono());

        if (dto.getRolId() != null) {
            Rol rol = rolService.buscarPorId(dto.getRolId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));
            usuario.setRol(rol);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol es obligatorio para crear un usuario");
        }

        return usuario;
    }

    public UsuarioResponseDTO toResponse(Usuario usuario) {
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
