package com.inventa.inventa.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.cliente.ClienteRequestDTO;
import com.inventa.inventa.dto.cliente.ClienteResponseDTO;
import com.inventa.inventa.entity.Cliente;
import com.inventa.inventa.mapper.ClienteMapper;
import com.inventa.inventa.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }

    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return clienteService.listar().stream().map(clienteMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO obtenerPorId(@PathVariable Integer id) {
        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        return clienteMapper.toResponse(cliente);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDTO crear(@RequestBody ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        clienteMapper.updateEntityFromRequest(cliente, dto);
        return clienteMapper.toResponse(clienteService.guardar(cliente));
    }

    @PutMapping("/{id}")
    public ClienteResponseDTO actualizar(@PathVariable Integer id, @RequestBody ClienteRequestDTO dto) {
        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        clienteMapper.updateEntityFromRequest(cliente, dto);
        return clienteMapper.toResponse(clienteService.guardar(cliente));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        if (clienteService.buscarPorId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado");
        }
        clienteService.eliminar(id);
    }

    @GetMapping("/identificacion/{identificacion}")
    public ClienteResponseDTO obtenerPorIdentificacionFiscal(@PathVariable String identificacion) {
        Cliente cliente = clienteService.buscarPorIdentificacionFiscal(identificacion);
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Cliente no encontrado con esa identificación fiscal");
        }
        return clienteMapper.toResponse(cliente);
    }
}
