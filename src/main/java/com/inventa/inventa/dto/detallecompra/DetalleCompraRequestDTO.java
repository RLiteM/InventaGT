package com.inventa.inventa.dto.detallecompra;

import java.math.BigDecimal;

import lombok.Data;
import java.time.LocalDate;

@Data
public class DetalleCompraRequestDTO {
    private Integer productoId;
    private BigDecimal cantidad;
    private BigDecimal costoUnitarioCompra;
    private LocalDate fechaCaducidad;

    // Explicit Getters
    public Integer getProductoId() {
        return productoId;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public BigDecimal getCostoUnitarioCompra() {
        return costoUnitarioCompra;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad;
    }

    // Explicit Setters
    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public void setCostoUnitarioCompra(BigDecimal costoUnitarioCompra) {
        this.costoUnitarioCompra = costoUnitarioCompra;
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }
}
