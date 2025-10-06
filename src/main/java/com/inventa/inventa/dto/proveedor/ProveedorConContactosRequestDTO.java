package com.inventa.inventa.dto.proveedor;

import com.inventa.inventa.dto.contactoproveedor.ContactoProveedorCreationDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProveedorConContactosRequestDTO {
    private String nombreEmpresa;
    private String telefono;
    private String direccion;
    private List<ContactoProveedorCreationDTO> contactos;
}
