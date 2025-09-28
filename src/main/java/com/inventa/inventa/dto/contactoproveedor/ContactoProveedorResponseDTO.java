package com.inventa.inventa.dto.contactoproveedor;

import lombok.Data;

@Data
public class ContactoProveedorResponseDTO {
    private Integer contactoId;
    private Integer proveedorId;
    private String proveedorNombre;
    private String nombreCompleto;
    private String cargo;
    private String telefono;
    private String email;
}
