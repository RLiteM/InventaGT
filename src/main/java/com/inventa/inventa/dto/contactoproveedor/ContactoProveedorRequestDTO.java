package com.inventa.inventa.dto.contactoproveedor;

import lombok.Data;

@Data
public class ContactoProveedorRequestDTO {
    private Integer proveedorId;
    private String nombreCompleto;
    private String cargo;
    private String telefono;
    private String email;
}
