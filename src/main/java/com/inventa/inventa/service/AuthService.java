package com.inventa.inventa.service;

import com.inventa.inventa.dto.usuario.LoginRequestDTO;
import com.inventa.inventa.dto.usuario.LoginResponseDTO;
import com.inventa.inventa.entity.Usuario;
import com.inventa.inventa.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

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
}