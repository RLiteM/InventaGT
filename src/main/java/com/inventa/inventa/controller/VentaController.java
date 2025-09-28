package com.inventa.inventa.controller;

import java.util.Arrays;
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

import com.inventa.inventa.dto.venta.VentaRequestDTO;
import com.inventa.inventa.dto.venta.VentaResponseDTO;
import com.inventa.inventa.entity.Cliente;
import com.inventa.inventa.entity.Usuario;
import com.inventa.inventa.entity.Venta;
import com.inventa.inventa.service.ClienteService;
import com.inventa.inventa.service.UsuarioService;
import com.inventa.inventa.service.VentaService;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final UsuarioService usuarioService;
    private final ClienteService clienteService;

    public VentaController(VentaService ventaService, UsuarioService usuarioService, ClienteService clienteService) {
        this.ventaService = ventaService;
        this.usuarioService = usuarioService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<VentaResponseDTO> listar() {
        return ventaService.listar().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public VentaResponseDTO obtenerPorId(@PathVariable Integer id) {
        Venta venta = ventaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
        return toResponse(venta);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VentaResponseDTO crear(@RequestBody VentaRequestDTO dto) {
        Venta venta = new Venta();
        aplicarCambios(venta, dto, true);
        return toResponse(ventaService.guardar(venta));
    }

    @PutMapping("/{id}")
    public VentaResponseDTO actualizar(@PathVariable Integer id, @RequestBody VentaRequestDTO dto) {
        Venta venta = ventaService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));
        aplicarCambios(venta, dto, false);
        return toResponse(ventaService.guardar(venta));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (ventaService.buscarPorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada");
        }
        ventaService.eliminar(id);
    }

    private void aplicarCambios(Venta venta, VentaRequestDTO dto, boolean esCreacion) {
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

    private Venta.MetodoPago parseMetodoPago(String metodoPago) {
        return Arrays.stream(Venta.MetodoPago.values())
                .filter(valor -> valor.name().equalsIgnoreCase(metodoPago))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Método de pago inválido"));
    }

    private VentaResponseDTO toResponse(Venta venta) {
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
}
