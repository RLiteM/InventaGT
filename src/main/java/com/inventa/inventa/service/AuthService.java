package com.inventa.inventa.service;

import com.inventa.inventa.dto.usuario.LoginRequestDTO;
import com.inventa.inventa.dto.usuario.LoginResponseDTO;
import com.inventa.inventa.entity.TokenRecuperacion;
import com.inventa.inventa.entity.Usuario;
import com.inventa.inventa.exceptions.BadRequestException;
import com.inventa.inventa.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRecuperacionService tokenRecuperacionService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getNombreUsuario(),
                        request.getContrasena()
                )
        );

        Usuario usuario = usuarioRepository.findByNombreUsuario(request.getNombreUsuario())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado tras una autenticación exitosa"));

        String jwtToken = jwtService.generateToken(usuario);

        LoginResponseDTO response = new LoginResponseDTO();
        response.setUsuarioId(usuario.getUsuarioId());
        response.setNombreCompleto(usuario.getNombreCompleto());
        response.setRol(usuario.getRol().getNombreRol());
        response.setMensaje("Login exitoso");
        response.setToken(jwtToken);

        return response;
    }

    @Transactional
    public void resetearPassword(String token, String nuevaContrasena) {
        if (!tokenRecuperacionService.validarToken(token)) {
            throw new BadRequestException("Token inválido o expirado.");
        }

        TokenRecuperacion tokenRec = tokenRecuperacionService.buscarPorToken(token)
                .orElseThrow(() -> new BadRequestException("Token no encontrado."));

        Usuario usuario = tokenRec.getUsuario();
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);

        tokenRecuperacionService.marcarComoUsado(token);
    }
}