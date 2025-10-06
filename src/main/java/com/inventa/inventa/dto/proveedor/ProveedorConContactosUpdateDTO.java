package com.inventa.inventa.dto.proveedor;

import com.inventa.inventa.dto.contactoproveedor.ContactoProveedorUpdateDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProveedorConContactosUpdateDTO {
    private String nombreEmpresa;
    private String telefono;
    private String direccion;
    private List<ContactoProveedorUpdateDTO> contactos;
}
