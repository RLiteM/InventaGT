package com.inventa.inventa.mapper;

import com.inventa.inventa.dto.contactoproveedor.ContactoProveedorRequestDTO;
import com.inventa.inventa.dto.contactoproveedor.ContactoProveedorResponseDTO;
import com.inventa.inventa.entity.ContactoProveedor;
import com.inventa.inventa.entity.Proveedor;
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

    public void updateEntityFromRequest(ContactoProveedor contacto, ContactoProveedorRequestDTO dto) {
        contacto.setNombreCompleto(dto.getNombreCompleto());
        contacto.setCargo(dto.getCargo());
        contacto.setTelefono(dto.getTelefono());
        contacto.setEmail(dto.getEmail());
        if (dto.getProveedorId() != null) {
            Proveedor proveedor = new Proveedor();
            proveedor.setProveedorId(dto.getProveedorId());
            contacto.setProveedor(proveedor);
        }
    }
}
