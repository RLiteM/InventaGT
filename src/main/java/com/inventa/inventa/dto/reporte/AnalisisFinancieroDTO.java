package com.inventa.inventa.dto.reporte;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalisisFinancieroDTO {
    private BigDecimal ingresosTotales;
    private BigDecimal costoDeVentas;
    private BigDecimal beneficioBruto;
}
