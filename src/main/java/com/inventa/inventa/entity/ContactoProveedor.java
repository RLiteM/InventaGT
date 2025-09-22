package com.inventa.inventa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contacto_proveedor")
public class ContactoProveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer contactoId;

    @ManyToOne
    @JoinColumn(name = "proveedor_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Proveedor proveedor;

    @Column(name = "nombre_completo", nullable = false, length = 150)
    private String nombreCompleto;

    @Column(length = 100)
    private String cargo;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String email;
}
