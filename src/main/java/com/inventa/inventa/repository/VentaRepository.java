package com.inventa.inventa.repository;

import com.inventa.inventa.dto.reporte.TopProductoDTO;
import com.inventa.inventa.dto.reporte.VentasPorMesDTO;
import com.inventa.inventa.entity.Venta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {

    @Query("SELECT COALESCE(SUM(v.montoTotal), 0) FROM Venta v")
    BigDecimal findTotalRevenue();

    @Query("SELECT COALESCE(SUM(dv.cantidad * dc.costoUnitarioCompra), 0) " +
           "FROM Venta v " +
           "JOIN v.detalles dv " +
           "JOIN dv.lote l " +
           "JOIN l.detalleCompra dc")
    BigDecimal findTotalCostOfGoodsSold();

    @Query(name = "Venta.findMonthlySalesLast12Months", nativeQuery = true)
    List<VentasPorMesDTO> findMonthlySalesLast12Months();

    @Query("SELECT new com.inventa.inventa.dto.reporte.TopProductoDTO(p.nombre, p.sku, SUM(dv.cantidad)) " +
           "FROM DetalleVenta dv " +
           "JOIN dv.lote l " +
           "JOIN l.producto p " +
           "GROUP BY p.productoId, p.nombre, p.sku " +
           "ORDER BY SUM(dv.cantidad) DESC")
    List<TopProductoDTO> findTopSellingProductsByUnits(Pageable pageable);

}
