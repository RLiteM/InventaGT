package com.inventa.inventa.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.cliente.ClienteRequestDTO;
import com.inventa.inventa.dto.cliente.ClienteResponseDTO;
import com.inventa.inventa.entity.Cliente;
import com.inventa.inventa.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return clienteService.listar().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO obtenerPorId(@PathVariable Integer id) {
        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        return toResponse(cliente);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDTO crear(@RequestBody ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        aplicarCambios(cliente, dto);
        return toResponse(clienteService.guardar(cliente));
    }

    @PutMapping("/{id}")
    public ClienteResponseDTO actualizar(@PathVariable Integer id, @RequestBody ClienteRequestDTO dto) {
        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        aplicarCambios(cliente, dto);
        return toResponse(clienteService.guardar(cliente));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (clienteService.buscarPorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        clienteService.eliminar(id);
    }

    private void aplicarCambios(Cliente cliente, ClienteRequestDTO dto) {
        cliente.setNombreCompleto(dto.getNombreCompleto());
        cliente.setIdentificacionFiscal(dto.getIdentificacionFiscal());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
        if (dto.getTipoCliente() != null) {
            cliente.setTipoCliente(parseTipoCliente(dto.getTipoCliente()));
        }
    }

    private ClienteResponseDTO toResponse(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setClienteId(cliente.getClienteId());
        dto.setNombreCompleto(cliente.getNombreCompleto());
        dto.setIdentificacionFiscal(cliente.getIdentificacionFiscal());
        dto.setTelefono(cliente.getTelefono());
        dto.setDireccion(cliente.getDireccion());
        dto.setTipoCliente(cliente.getTipoCliente() != null ? cliente.getTipoCliente().name() : null);
        return dto;
    }

    private Cliente.TipoCliente parseTipoCliente(String tipoCliente) {
        return Arrays.stream(Cliente.TipoCliente.values())
                .filter(valor -> valor.name().equalsIgnoreCase(tipoCliente))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de cliente inválido"));
    }
}
