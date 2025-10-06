package com.inventa.inventa.controller;

import com.inventa.inventa.dto.proveedor.ProveedorConContactosRequestDTO;
import com.inventa.inventa.dto.proveedor.ProveedorConContactosUpdateDTO;
import com.inventa.inventa.dto.proveedor.ProveedorResponseDTO;
import com.inventa.inventa.entity.Proveedor;
import com.inventa.inventa.mapper.ProveedorMapper;
import com.inventa.inventa.service.ProveedorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;
    private final ProveedorMapper proveedorMapper;

    public ProveedorController(ProveedorService proveedorService, ProveedorMapper proveedorMapper) {
        this.proveedorService = proveedorService;
        this.proveedorMapper = proveedorMapper;
    }

    @GetMapping
    public List<ProveedorResponseDTO> listar() {
        return proveedorService.listar().stream().map(proveedorMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProveedorResponseDTO obtenerPorId(@PathVariable Integer id) {
        Proveedor proveedor = proveedorService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));
        return proveedorMapper.toResponse(proveedor);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProveedorResponseDTO crear(@RequestBody ProveedorConContactosRequestDTO dto) {
        Proveedor proveedorGuardado = proveedorService.crearProveedorConContactos(dto);
        return proveedorMapper.toResponse(proveedorGuardado);
    }

    @PutMapping("/{id}")
    public ProveedorResponseDTO actualizar(@PathVariable Integer id, @RequestBody ProveedorConContactosUpdateDTO dto) {
        Proveedor proveedorActualizado = proveedorService.actualizarProveedorConContactos(id, dto);
        return proveedorMapper.toResponse(proveedorActualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (proveedorService.buscarPorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado");
        }
        proveedorService.eliminar(id);
    }

    @GetMapping("/telefono/{telefono}")
    public ProveedorResponseDTO obtenerPorTelefono(@PathVariable String telefono) {
        Proveedor proveedor = proveedorService.buscarPorTelefono(telefono);
        if (proveedor == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado con ese teléfono");
        }
        return proveedorMapper.toResponse(proveedor);
    }
}
