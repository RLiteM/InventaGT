package com.inventa.inventa.dto.cliente;

import lombok.Data;

@Data
public class ClienteRequestDTO {
    private String nombreCompleto;
    private String identificacionFiscal;
    private String telefono;
    private String direccion;
    private String tipoCliente;
}
