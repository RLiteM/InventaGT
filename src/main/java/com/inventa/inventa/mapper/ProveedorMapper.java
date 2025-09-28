package com.inventa.inventa.mapper;

import org.springframework.stereotype.Component;

import com.inventa.inventa.dto.proveedor.ProveedorRequestDTO;
import com.inventa.inventa.dto.proveedor.ProveedorResponseDTO;
import com.inventa.inventa.entity.Proveedor;

@Component
public class ProveedorMapper {

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
        return dto;
    }
}
