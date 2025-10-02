package com.inventa.inventa.mapper;

import org.springframework.stereotype.Component;
import com.inventa.inventa.dto.rol.RolRequestDTO;
import com.inventa.inventa.dto.rol.RolResponseDTO;
import com.inventa.inventa.entity.Rol;

@Component
public class RolMapper {

    public RolResponseDTO toResponse(Rol rol) {
        RolResponseDTO dto = new RolResponseDTO();
        dto.setRolId(rol.getRolId());
        dto.setNombreRol(rol.getNombreRol());
        return dto;
    }

    public Rol toEntity(RolRequestDTO dto) {
        Rol rol = new Rol();
        rol.setNombreRol(dto.getNombreRol());
        return rol;
    }

    public void updateEntityFromRequest(Rol rol, RolRequestDTO dto) {
        rol.setNombreRol(dto.getNombreRol());
    }
}
