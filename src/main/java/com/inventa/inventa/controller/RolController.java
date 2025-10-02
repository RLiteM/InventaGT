package com.inventa.inventa.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.inventa.inventa.dto.rol.RolResponseDTO;
import com.inventa.inventa.entity.Rol;
import com.inventa.inventa.mapper.RolMapper;
import com.inventa.inventa.service.RolService;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;
    private final RolMapper rolMapper;

    public RolController(RolService rolService, RolMapper rolMapper) {
        this.rolService = rolService;
        this.rolMapper = rolMapper;
    }

    @GetMapping
    public List<RolResponseDTO> listar() {
        return rolService.obtenerTodos().stream()
                .map(rolMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RolResponseDTO obtenerPorId(@PathVariable Integer id) {
        Rol rol = rolService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));
        return rolMapper.toResponse(rol);
    }

    @GetMapping("/nombre/{nombre}")
    public RolResponseDTO obtenerPorNombre(@PathVariable String nombre) {
        Rol rol = rolService.buscarPorNombre(nombre)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));
        return rolMapper.toResponse(rol);
    }
}
