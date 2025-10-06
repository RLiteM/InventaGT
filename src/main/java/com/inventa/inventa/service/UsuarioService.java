package com.inventa.inventa.service;

import com.inventa.inventa.dto.usuario.UsuarioRequestDTO;
import com.inventa.inventa.entity.Rol;
import com.inventa.inventa.entity.Usuario;
import com.inventa.inventa.exceptions.NotFoundException;
import com.inventa.inventa.repository.RolRepository;
import com.inventa.inventa.repository.UsuarioRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAllWithRol();
    }

    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Usuario guardar(Usuario usuario) {
        // Codificar contraseña si es nueva o ha cambiado
        // Esta lógica es para la creación, la actualización se maneja abajo
        usuario.setContrasena(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario actualizarUsuario(Integer id, UsuarioRequestDTO dto, Usuario currentUser) {
        Usuario usuarioAActualizar = buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Usuario a actualizar no encontrado"));

        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));

        // Si no es admin, solo puede editarse a sí mismo
        if (!isAdmin && !currentUser.getUsuarioId().equals(id)) {
            throw new AccessDeniedException("No tienes permiso para editar a otros usuarios.");
        }

        // Actualizar campos seguros
        usuarioAActualizar.setNombreCompleto(dto.getNombreCompleto());
        usuarioAActualizar.setCuiDpi(dto.getCuiDpi());
        usuarioAActualizar.setCorreo(dto.getCorreo());
        usuarioAActualizar.setTelefono(dto.getTelefono());

        // Si se provee una nueva contraseña, codificarla y actualizarla
        if (dto.getContrasena() != null && !dto.getContrasena().isEmpty()) {
            usuarioAActualizar.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        }

        // Solo el admin puede cambiar el rol
        if (isAdmin) {
            if (dto.getRolId() != null && !dto.getRolId().equals(usuarioAActualizar.getRol().getRolId())) {
                Rol nuevoRol = rolRepository.findById(dto.getRolId())
                        .orElseThrow(() -> new NotFoundException("El nuevo rol no fue encontrado"));
                usuarioAActualizar.setRol(nuevoRol);
            }
        }

        return usuarioRepository.save(usuarioAActualizar);
    }

    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    public Optional<Usuario> buscarPorCuiDpi(String cuiDpi) {
        return usuarioRepository.findByCuiDpi(cuiDpi);
    }
}