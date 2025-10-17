package com.inventa.inventa.dto.detalleventa;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DetalleVentaRequestDTO {
    // Nuevo: permitir que el front envíe productoId (preferido)
    private Integer productoId;
    private Integer loteId;
    private BigDecimal cantidad;
    // Dejado para compatibilidad; el backend calculará el precio si es nulo o incluso lo ignorará
    private BigDecimal precioUnitarioVenta;

    // Explicit Getters
    public Integer getProductoId() {
        return productoId;
    }

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
    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

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
