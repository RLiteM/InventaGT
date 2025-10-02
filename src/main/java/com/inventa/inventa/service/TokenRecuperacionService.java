package com.inventa.inventa.service;

import com.inventa.inventa.entity.TokenRecuperacion;
import com.inventa.inventa.entity.Usuario;
import com.inventa.inventa.repository.TokenRecuperacionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenRecuperacionService {

    private final TokenRecuperacionRepository tokenRepo;
    private final UsuarioService usuarioService;

    public TokenRecuperacionService(TokenRecuperacionRepository tokenRepo, UsuarioService usuarioService) {
        this.tokenRepo = tokenRepo;
        this.usuarioService = usuarioService;
    }

    // =========================
    // GENERAR TOKEN NUEVO
    // =========================
    public TokenRecuperacion generarToken(Usuario usuario) {
        tokenRepo.deleteByUsuario(usuario);

        TokenRecuperacion token = new TokenRecuperacion();
        token.setUsuario(usuario);
        token.setToken(UUID.randomUUID().toString());
        token.setFechaExpiracion(LocalDateTime.now().plusHours(1));
        token.setUsado(0);

        return tokenRepo.save(token);
    }

    // =========================
    // VALIDAR Y MARCAR USO
    // =========================
    public boolean validarToken(String tokenStr) {
        Optional<TokenRecuperacion> tokenOpt = tokenRepo.findByToken(tokenStr);
        return tokenOpt.isPresent() &&
               tokenOpt.get().getUsado() == 0 &&
               tokenOpt.get().getFechaExpiracion().isAfter(LocalDateTime.now());
    }

    public void marcarComoUsado(String tokenStr) {
        tokenRepo.findByToken(tokenStr).ifPresent(t -> {
            t.setUsado(1);
            tokenRepo.save(t);
        });
    }

    // =========================
    // MÉTODOS AUXILIARES
    // =========================
    public Optional<TokenRecuperacion> buscarPorToken(String token) {
        return tokenRepo.findByToken(token);
    }

    public List<TokenRecuperacion> listar() {
        return tokenRepo.findAll();
    }

    public Optional<Usuario> obtenerUsuarioPorId(Integer usuarioId) {
        return usuarioService.buscarPorId(usuarioId);
    }

    // =========================
    // ELIMINAR POR ID
    // =========================
    public void eliminar(Integer id) throws DataIntegrityViolationException {
        TokenRecuperacion token = tokenRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token no encontrado"));
        tokenRepo.delete(token);
    }
}
