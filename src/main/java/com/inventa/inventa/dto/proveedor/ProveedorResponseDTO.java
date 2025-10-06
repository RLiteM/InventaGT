package com.inventa.inventa.dto.proveedor;

import com.inventa.inventa.dto.contactoproveedor.ContactoProveedorResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProveedorResponseDTO {
    private Integer proveedorId;
    private String nombreEmpresa;
    private String telefono;
    private String direccion;
    private List<ContactoProveedorResponseDTO> contactos;
}
