package com.inventa.inventa.entities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clienteId;

    @Column(nullable = false, length = 150)
    private String nombreCompleto;

    @Column(unique = true, length = 20)
    private String identificacionFiscal;

    private String telefono;
    private String direccion;

    @Column(nullable = false, length = 20)
    private String tipoCliente; // Minorista o Mayorista
}

