package com.inventa.inventa.dto.producto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class ProductoRapidoRequestDTO {

    @NotBlank(message = "El SKU no puede estar vacío")
    private String sku;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotNull(message = "El ID de la categoría no puede ser nulo")
    private Integer categoriaId;

    @NotNull(message = "El stock mínimo no puede ser nulo")
    @PositiveOrZero(message = "El stock mínimo debe ser cero o positivo")
    private BigDecimal stockMinimo;

    // Getters y Setters

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public BigDecimal getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(BigDecimal stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
}
