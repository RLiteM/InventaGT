package com.inventa.inventa.mapper;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.venta.VentaRequestDTO;
import com.inventa.inventa.dto.venta.VentaResponseDTO;
import com.inventa.inventa.entity.Cliente;
import com.inventa.inventa.entity.Usuario;
import com.inventa.inventa.entity.Venta;
import com.inventa.inventa.service.ClienteService;
import com.inventa.inventa.service.UsuarioService;

@Component
public class VentaMapper {

    private final UsuarioService usuarioService;
    private final ClienteService clienteService;

    public VentaMapper(UsuarioService usuarioService, ClienteService clienteService) {
        this.usuarioService = usuarioService;
        this.clienteService = clienteService;
    }

    public void updateEntityFromRequest(Venta venta, VentaRequestDTO dto, boolean esCreacion) {
        Usuario usuario = usuarioService.buscarPorId(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        Cliente cliente = clienteService.buscarPorId(dto.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));

        venta.setUsuario(usuario);
        venta.setCliente(cliente);
        venta.setMontoTotal(dto.getMontoTotal());
        if (dto.getMetodoPago() != null) {
            venta.setMetodoPago(parseMetodoPago(dto.getMetodoPago()));
        } else if (esCreacion) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El método de pago es obligatorio");
        }
        if (dto.getFechaVenta() != null) {
            venta.setFechaVenta(dto.getFechaVenta());
        }
    }

    public VentaResponseDTO toResponse(Venta venta) {
        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setVentaId(venta.getVentaId());
        if (venta.getUsuario() != null) {
            dto.setUsuarioId(venta.getUsuario().getUsuarioId());
            dto.setUsuarioNombre(venta.getUsuario().getNombreCompleto());
        }
        if (venta.getCliente() != null) {
            dto.setClienteId(venta.getCliente().getClienteId());
            dto.setClienteNombre(venta.getCliente().getNombreCompleto());
        }
        dto.setFechaVenta(venta.getFechaVenta());
        dto.setMontoTotal(venta.getMontoTotal());
        dto.setMetodoPago(venta.getMetodoPago() != null ? venta.getMetodoPago().name() : null);
        return dto;
    }

    private Venta.MetodoPago parseMetodoPago(String metodoPago) {
        return Arrays.stream(Venta.MetodoPago.values())
                .filter(valor -> valor.name().equalsIgnoreCase(metodoPago))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Método de pago inválido"));
    }
}
