package com.inventa.inventa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.compra.CompraRequestDTO;
import com.inventa.inventa.dto.compra.CompraResponseDTO;
import com.inventa.inventa.entity.Compra;
import com.inventa.inventa.entity.Proveedor;
import com.inventa.inventa.entity.Usuario;
import com.inventa.inventa.service.CompraService;
import com.inventa.inventa.service.ProveedorService;
import com.inventa.inventa.service.UsuarioService;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;
    private final ProveedorService proveedorService;
    private final UsuarioService usuarioService;

    public CompraController(CompraService compraService, ProveedorService proveedorService, UsuarioService usuarioService) {
        this.compraService = compraService;
        this.proveedorService = proveedorService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<CompraResponseDTO> listar() {
        return compraService.listar().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CompraResponseDTO obtenerPorId(@PathVariable Integer id) {
        Compra compra = compraService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada"));
        return toResponse(compra);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompraResponseDTO crear(@RequestBody CompraRequestDTO dto) {
        Compra compra = new Compra();
        aplicarCambios(compra, dto);
        return toResponse(compraService.guardar(compra));
    }

    @PutMapping("/{id}")
    public CompraResponseDTO actualizar(@PathVariable Integer id, @RequestBody CompraRequestDTO dto) {
        Compra compra = compraService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada"));
        aplicarCambios(compra, dto);
        return toResponse(compraService.guardar(compra));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (compraService.buscarPorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Compra no encontrada");
        }
        compraService.eliminar(id);
    }

    private void aplicarCambios(Compra compra, CompraRequestDTO dto) {
        Proveedor proveedor = proveedorService.buscarPorId(dto.getProveedorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));
        Usuario usuario = usuarioService.buscarPorId(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        compra.setProveedor(proveedor);
        compra.setUsuario(usuario);
        compra.setFechaCompra(dto.getFechaCompra());
        compra.setNumeroFactura(dto.getNumeroFactura());
        compra.setMontoTotal(dto.getMontoTotal());
    }

    private CompraResponseDTO toResponse(Compra compra) {
        CompraResponseDTO dto = new CompraResponseDTO();
        dto.setCompraId(compra.getCompraId());
        if (compra.getProveedor() != null) {
            dto.setProveedorId(compra.getProveedor().getProveedorId());
            dto.setProveedorNombre(compra.getProveedor().getNombreEmpresa());
        }
        if (compra.getUsuario() != null) {
            dto.setUsuarioId(compra.getUsuario().getUsuarioId());
            dto.setUsuarioNombre(compra.getUsuario().getNombreCompleto());
        }
        dto.setFechaCompra(compra.getFechaCompra());
        dto.setNumeroFactura(compra.getNumeroFactura());
        dto.setMontoTotal(compra.getMontoTotal());
        return dto;
    }
}
