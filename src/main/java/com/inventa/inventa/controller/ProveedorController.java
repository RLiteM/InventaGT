package com.inventa.inventa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.proveedor.ProveedorRequestDTO;
import com.inventa.inventa.dto.proveedor.ProveedorResponseDTO;
import com.inventa.inventa.entity.Proveedor;
import com.inventa.inventa.service.ProveedorService;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public List<ProveedorResponseDTO> listar() {
        return proveedorService.listar().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProveedorResponseDTO obtenerPorId(@PathVariable Integer id) {
        Proveedor proveedor = proveedorService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));
        return toResponse(proveedor);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProveedorResponseDTO crear(@RequestBody ProveedorRequestDTO dto) {
        Proveedor proveedor = new Proveedor();
        aplicarCambios(proveedor, dto);
        return toResponse(proveedorService.guardar(proveedor));
    }

    @PutMapping("/{id}")
    public ProveedorResponseDTO actualizar(@PathVariable Integer id, @RequestBody ProveedorRequestDTO dto) {
        Proveedor proveedor = proveedorService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));
        aplicarCambios(proveedor, dto);
        return toResponse(proveedorService.guardar(proveedor));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (proveedorService.buscarPorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado");
        }
        proveedorService.eliminar(id);
    }

    private void aplicarCambios(Proveedor proveedor, ProveedorRequestDTO dto) {
        proveedor.setNombreEmpresa(dto.getNombreEmpresa());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDireccion(dto.getDireccion());
    }

    private ProveedorResponseDTO toResponse(Proveedor proveedor) {
        ProveedorResponseDTO dto = new ProveedorResponseDTO();
        dto.setProveedorId(proveedor.getProveedorId());
        dto.setNombreEmpresa(proveedor.getNombreEmpresa());
        dto.setTelefono(proveedor.getTelefono());
        dto.setDireccion(proveedor.getDireccion());
        return dto;
    }
}
