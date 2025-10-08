package com.inventa.inventa.entity;

import com.inventa.inventa.dto.reporte.VentasPorMesDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NamedNativeQuery(
    name = "Venta.findMonthlySalesLast12Months",
    query = "SELECT TO_CHAR(v.fecha_venta, 'YYYY-MM') as mes, SUM(v.monto_total) as total " +
            "FROM venta v " +
            "WHERE v.fecha_venta >= CURRENT_DATE - INTERVAL '12' MONTH " +
            "GROUP BY mes " +
            "ORDER BY mes ASC",
    resultSetMapping = "VentasPorMesMapping"
)
@SqlResultSetMapping(
    name = "VentasPorMesMapping",
    classes = @ConstructorResult(
        targetClass = VentasPorMesDTO.class,
        columns = {
            @ColumnResult(name = "mes", type = String.class),
            @ColumnResult(name = "total", type = BigDecimal.class)
        }
    )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ventaId;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_venta", nullable = false, insertable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaVenta;

    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 20)
    private MetodoPago metodoPago;

    @OneToMany(mappedBy = "venta")
    private List<DetalleVenta> detalles = new ArrayList<>();

    // Enum que refleja exactamente el CHECK de PostgreSQL
    public enum MetodoPago {
        Efectivo,
        Tarjeta,
        Transferencia
    }
}
