package com.inventa.inventa.controller;

import com.inventa.inventa.dto.usuario.LoginRequestDTO;
import com.inventa.inventa.dto.usuario.LoginResponseDTO;
import com.inventa.inventa.dto.usuario.ResetearPasswordRequestDTO;
import com.inventa.inventa.dto.usuario.SolicitarRecuperacionRequestDTO;
import com.inventa.inventa.service.AuthService;
import com.inventa.inventa.service.TokenRecuperacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenRecuperacionService tokenRecuperacionService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/solicitar-recuperacion")
    public ResponseEntity<Map<String, String>> solicitarRecuperacion(@Valid @RequestBody SolicitarRecuperacionRequestDTO request) {
        tokenRecuperacionService.iniciarProcesoRecuperacion(request.getEmail());
        // Se devuelve una respuesta genérica para no revelar si el correo existe o no en la base de datos.
        Map<String, String> response = Map.of("message", "Si su correo electrónico está registrado, recibirá un enlace para restablecer su contraseña.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resetear-password")
    public ResponseEntity<Map<String, String>> resetearPassword(@Valid @RequestBody ResetearPasswordRequestDTO request) {
        authService.resetearPassword(request.getToken(), request.getNuevaContrasena());
        Map<String, String> response = Map.of("message", "Contraseña actualizada correctamente.");
        return ResponseEntity.ok(response);
    }
}
