package com.inventa.inventa.dto.proveedor;

import lombok.Data;

@Data
public class ProveedorResponseDTO {
    private Integer proveedorId;
    private String nombreEmpresa;
    private String telefono;
    private String direccion;
}
