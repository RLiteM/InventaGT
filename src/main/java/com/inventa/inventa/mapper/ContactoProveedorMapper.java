package com.inventa.inventa.mapper;

import com.inventa.inventa.dto.contactoproveedor.ContactoProveedorResponseDTO;
import com.inventa.inventa.entity.ContactoProveedor;
import org.springframework.stereotype.Component;

@Component
public class ContactoProveedorMapper {

    public ContactoProveedorResponseDTO toResponse(ContactoProveedor contacto) {
        if (contacto == null) {
            return null;
        }
        ContactoProveedorResponseDTO dto = new ContactoProveedorResponseDTO();
        dto.setId(contacto.getContactoId());
        dto.setNombreCompleto(contacto.getNombreCompleto());
        dto.setCargo(contacto.getCargo());
        dto.setTelefono(contacto.getTelefono());
        dto.setEmail(contacto.getEmail());
        return dto;
    }
}