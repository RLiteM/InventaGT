package com.inventa.inventa.service;

import com.inventa.inventa.entity.TokenRecuperacion;
import com.inventa.inventa.entity.Usuario;
import com.inventa.inventa.repository.TokenRecuperacionRepository;
import com.inventa.inventa.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
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
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;

    @Value("${inventagt.frontend.url}")
    private String frontendUrl;

    public TokenRecuperacionService(TokenRecuperacionRepository tokenRepo, UsuarioService usuarioService, UsuarioRepository usuarioRepository, EmailService emailService) {
        this.tokenRepo = tokenRepo;
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }

    // ===================================
    // INICIAR PROCESO DE RECUPERACIÓN
    // ===================================
    public void iniciarProcesoRecuperacion(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            TokenRecuperacion token = generarToken(usuario);
            String urlRecuperacion = frontendUrl + "/reset-password?token=" + token.getToken();
            String emailText = "Hola " + usuario.getNombreCompleto() + ",\n\n" +
                    "Para restablecer tu contraseña, haz clic en el siguiente enlace:\n" +
                    urlRecuperacion + "\n\n" +
                    "Si no solicitaste este cambio, puedes ignorar este correo.\n\n" +
                    "El enlace expirará en 1 hora.\n\n" +
                    "Saludos,\nEl equipo de InventaGT";

            emailService.sendEmail(usuario.getCorreo(), "Recuperación de Contraseña - InventaGT", emailText);
        }
        // Si no se encuentra el usuario, no hacemos nada para evitar la enumeración de usuarios.
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
