package com.inventa.inventa.dto.contactoproveedor;

import lombok.Data;

@Data
public class ContactoProveedorUpdateDTO {
    private Integer id; // Puede ser null para contactos nuevos
    private String nombreCompleto;
    private String cargo;
    private String telefono;
    private String email;
}
