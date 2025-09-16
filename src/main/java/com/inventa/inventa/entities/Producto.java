package com.inventa.inventa.entities;
import jakarta.persistence.*;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productoId;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(nullable = false, length = 150)
    private String nombre;

    private String descripcion;

    private BigDecimal ultimoCosto;
    private BigDecimal precioMinorista;
    private BigDecimal precioMayorista;
    private BigDecimal stockActual;
    private BigDecimal stockMinimo;

    @Column(length = 20, nullable = false)
    private String unidadMedida;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}

