package com.inventa.inventa.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.compra.CompraRequestDTO;
import com.inventa.inventa.dto.compra.CompraResponseDTO;
import com.inventa.inventa.entity.Compra;
import com.inventa.inventa.entity.Proveedor;
import com.inventa.inventa.entity.Usuario;
import com.inventa.inventa.service.ProveedorService;
import com.inventa.inventa.service.UsuarioService;

@Component
public class CompraMapper {

    private final ProveedorService proveedorService;
    private final UsuarioService usuarioService;

    public CompraMapper(ProveedorService proveedorService, UsuarioService usuarioService) {
        this.proveedorService = proveedorService;
        this.usuarioService = usuarioService;
    }

    public void updateEntityFromRequest(Compra compra, CompraRequestDTO dto) {
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

    public CompraResponseDTO toResponse(Compra compra) {
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
