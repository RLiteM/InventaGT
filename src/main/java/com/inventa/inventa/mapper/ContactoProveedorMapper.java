package com.inventa.inventa.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.contactoproveedor.ContactoProveedorRequestDTO;
import com.inventa.inventa.dto.contactoproveedor.ContactoProveedorResponseDTO;
import com.inventa.inventa.entity.ContactoProveedor;
import com.inventa.inventa.entity.Proveedor;
import com.inventa.inventa.service.ProveedorService;

@Component
public class ContactoProveedorMapper {

    private final ProveedorService proveedorService;

    public ContactoProveedorMapper(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    public void updateEntityFromRequest(ContactoProveedor contacto, ContactoProveedorRequestDTO dto) {
        if (dto.getProveedorId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El proveedor es obligatorio");
        }
        Proveedor proveedor = proveedorService.buscarPorId(dto.getProveedorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));

        contacto.setProveedor(proveedor);
        contacto.setNombreCompleto(dto.getNombreCompleto());
        contacto.setCargo(dto.getCargo());
        contacto.setTelefono(dto.getTelefono());
        contacto.setEmail(dto.getEmail());
    }

    public ContactoProveedorResponseDTO toResponse(ContactoProveedor contacto) {
        ContactoProveedorResponseDTO dto = new ContactoProveedorResponseDTO();
        dto.setContactoId(contacto.getContactoId());
        if (contacto.getProveedor() != null) {
            dto.setProveedorId(contacto.getProveedor().getProveedorId());
            dto.setProveedorNombre(contacto.getProveedor().getNombreEmpresa());
        }
        dto.setNombreCompleto(contacto.getNombreCompleto());
        dto.setCargo(contacto.getCargo());
        dto.setTelefono(contacto.getTelefono());
        dto.setEmail(contacto.getEmail());
        return dto;
    }
}
