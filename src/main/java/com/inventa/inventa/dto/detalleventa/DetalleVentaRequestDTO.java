package com.inventa.inventa.dto.detalleventa;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DetalleVentaRequestDTO {
    private Integer loteId;
    private BigDecimal cantidad;
    private BigDecimal precioUnitarioVenta;

    // Explicit Getters
    public Integer getLoteId() {
        return loteId;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitarioVenta() {
        return precioUnitarioVenta;
    }

    // Explicit Setters
    public void setLoteId(Integer loteId) {
        this.loteId = loteId;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecioUnitarioVenta(BigDecimal precioUnitarioVenta) {
        this.precioUnitarioVenta = precioUnitarioVenta;
    }
}

