package com.inventa.inventa.mapper;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.cliente.ClienteRequestDTO;
import com.inventa.inventa.dto.cliente.ClienteResponseDTO;
import com.inventa.inventa.entity.Cliente;

@Component
public class ClienteMapper {

    public void updateEntityFromRequest(Cliente cliente, ClienteRequestDTO dto) {
        cliente.setNombreCompleto(dto.getNombreCompleto());
        cliente.setIdentificacionFiscal(dto.getIdentificacionFiscal());
        cliente.setTelefono(dto.getTelefono());
        cliente.setDireccion(dto.getDireccion());
        if (dto.getTipoCliente() != null) {
            cliente.setTipoCliente(parseTipoCliente(dto.getTipoCliente()));
        }
    }

    public ClienteResponseDTO toResponse(Cliente cliente) {
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
