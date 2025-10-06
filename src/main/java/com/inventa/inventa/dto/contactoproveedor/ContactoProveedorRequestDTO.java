package com.inventa.inventa.dto.contactoproveedor;

import lombok.Data;

@Data
public class ContactoProveedorRequestDTO {
    private String nombreCompleto;
    private String cargo;
    private String telefono;
    private String email;
    private Integer proveedorId;
}