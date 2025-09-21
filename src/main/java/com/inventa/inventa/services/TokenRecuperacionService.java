package com.inventa.inventa.services;

import com.inventa.inventa.entities.TokenRecuperacion;
import com.inventa.inventa.entities.Usuario;
import com.inventa.inventa.repositories.TokenRecuperacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenRecuperacionService {

    private final TokenRecuperacionRepository tokenRepo;

    public TokenRecuperacionService(TokenRecuperacionRepository tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    // Generar un nuevo token y guardarlo
    public TokenRecuperacion generarToken(Usuario usuario) {
        // Antes de guardar uno nuevo, eliminamos los anteriores de este usuario
        tokenRepo.deleteByUsuario(usuario);

        TokenRecuperacion token = new TokenRecuperacion();
        token.setUsuario(usuario);
        token.setToken(UUID.randomUUID().toString()); // Genera token único
        token.setFechaExpiracion(LocalDateTime.now().plusHours(1)); // Expira en 1 hora
        token.setUsado(0); // 0 = no usado

        return tokenRepo.save(token);
    }

    // Validar un token
    public boolean validarToken(String tokenStr) {
        Optional<TokenRecuperacion> tokenOpt = tokenRepo.findByToken(tokenStr);

        return tokenOpt.isPresent() &&
               tokenOpt.get().getUsado() == 0 &&
               tokenOpt.get().getFechaExpiracion().isAfter(LocalDateTime.now());
    }

    // Marcar token como usado
    public void marcarComoUsado(String tokenStr) {
        tokenRepo.findByToken(tokenStr).ifPresent(t -> {
            t.setUsado(1);
            tokenRepo.save(t);
        });
    }


}
