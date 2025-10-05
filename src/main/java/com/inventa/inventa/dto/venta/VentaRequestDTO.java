package com.inventa.inventa.dto.venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.inventa.inventa.dto.detalleventa.DetalleVentaRequestDTO;

import lombok.Data;

@Data
public class VentaRequestDTO {
    private Integer usuarioId;
    private Integer clienteId;
    private LocalDateTime fechaVenta;
    private BigDecimal montoTotal;
    private String metodoPago;
    private List<DetalleVentaRequestDTO> detalles;

    // Explicit Getters
    public Integer getUsuarioId() {
        return usuarioId;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public List<DetalleVentaRequestDTO> getDetalles() {
        return detalles;
    }

    // Explicit Setters
    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public void setDetalles(List<DetalleVentaRequestDTO> detalles) {
        this.detalles = detalles;
    }
}
