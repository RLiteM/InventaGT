package com.inventa.inventa.mapper;

import org.springframework.stereotype.Component;

import com.inventa.inventa.dto.categoria.CategoriaRequestDTO;
import com.inventa.inventa.dto.categoria.CategoriaResponseDTO;
import com.inventa.inventa.entity.Categoria;

@Component
public class CategoriaMapper {

    public void updateEntityFromRequest(Categoria categoria, CategoriaRequestDTO dto) {
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
    }

    public CategoriaResponseDTO toResponse(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setCategoriaId(categoria.getCategoriaId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        return dto;
    }
}
