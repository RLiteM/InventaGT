package com.inventa.inventa.controller;

import com.inventa.inventa.dto.usuario.LoginRequestDTO;
import com.inventa.inventa.dto.usuario.LoginResponseDTO;
import com.inventa.inventa.service.AuthService;
import org.springframework.web.bind.annotation.*;

import com.inventa.inventa.dto.usuario.LoginRequestDTO;
import com.inventa.inventa.dto.usuario.LoginResponseDTO;
import com.inventa.inventa.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
