package com.inventa.inventa.mapper;

import com.inventa.inventa.dto.proveedor.ProveedorRequestDTO;
import com.inventa.inventa.dto.proveedor.ProveedorResponseDTO;
import com.inventa.inventa.dto.proveedor.ProveedorSimpleDTO;
import com.inventa.inventa.entity.Proveedor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProveedorMapper {

    private final ContactoProveedorMapper contactoProveedorMapper;

    public ProveedorMapper(ContactoProveedorMapper contactoProveedorMapper) {
        this.contactoProveedorMapper = contactoProveedorMapper;
    }

    public void updateEntityFromRequest(Proveedor proveedor, ProveedorRequestDTO dto) {
        proveedor.setNombreEmpresa(dto.getNombreEmpresa());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDireccion(dto.getDireccion());
    }

    public ProveedorResponseDTO toResponse(Proveedor proveedor) {
        ProveedorResponseDTO dto = new ProveedorResponseDTO();
        dto.setProveedorId(proveedor.getProveedorId());
        dto.setNombreEmpresa(proveedor.getNombreEmpresa());
        dto.setTelefono(proveedor.getTelefono());
        dto.setDireccion(proveedor.getDireccion());
        if (proveedor.getContactos() != null) {
            dto.setContactos(proveedor.getContactos().stream()
                    .map(contactoProveedorMapper::toResponse)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public ProveedorSimpleDTO toSimpleResponse(Proveedor proveedor) {
        ProveedorSimpleDTO dto = new ProveedorSimpleDTO();
        dto.setProveedorId(proveedor.getProveedorId());
        dto.setNombreEmpresa(proveedor.getNombreEmpresa());
        return dto;
    }
}
