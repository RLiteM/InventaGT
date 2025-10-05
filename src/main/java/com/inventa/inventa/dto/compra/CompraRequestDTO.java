package com.inventa.inventa.dto.compra;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.inventa.inventa.dto.detallecompra.DetalleCompraRequestDTO;

// @Data annotation is kept for completeness, but getters/setters are now explicit.
import lombok.Data;

@Data
public class CompraRequestDTO {
    private Integer proveedorId;
    private Integer usuarioId;
    private LocalDate fechaCompra;
    private String numeroFactura;
    private BigDecimal montoTotal;
    private List<DetalleCompraRequestDTO> detalles;

    // Explicit Getters
    public Integer getProveedorId() {
        return proveedorId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public List<DetalleCompraRequestDTO> getDetalles() {
        return detalles;
    }

    // Explicit Setters
    public void setProveedorId(Integer proveedorId) {
        this.proveedorId = proveedorId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public void setDetalles(List<DetalleCompraRequestDTO> detalles) {
        this.detalles = detalles;
    }
}
