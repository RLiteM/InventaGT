package com.inventa.inventa.entities;
import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "proveedor")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer proveedorId;

    @Column(nullable = false, length = 150)
    private String nombreEmpresa;

    private String telefono;
    private String direccion;

    @OneToMany(mappedBy = "proveedor")
    private List<Compra> compras = new ArrayList<>();

    @OneToMany(mappedBy = "proveedor")
    private List<ContactoProveedor> contactos = new ArrayList<>();
}

