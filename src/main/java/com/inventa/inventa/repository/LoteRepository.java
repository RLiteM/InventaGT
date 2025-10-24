package com.inventa.inventa.repository;

import com.inventa.inventa.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Integer> {

    @Query("SELECT COALESCE(SUM(l.cantidadActual), 0) FROM Lote l WHERE l.fechaCaducidad BETWEEN :startDate AND :endDate")
    long countUnitsExpiringSoon(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(l.cantidadActual * dc.costoUnitarioCompra), 0) FROM Lote l JOIN l.detalleCompra dc")
    BigDecimal findTotalInventoryValue();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Lote l \n" +
           "WHERE l.producto.productoId = :productoId \n" +
           "  AND l.cantidadActual > 0 \n" +
           "  AND l.fechaCaducidad >= :hoy \n" +
           "ORDER BY l.fechaCaducidad ASC, l.loteId ASC")
    List<Lote> findLotesDisponiblesFefo(@Param("productoId") Integer productoId,
                                        @Param("hoy") LocalDate hoy);

    @Query("SELECT l FROM Lote l WHERE l.producto.productoId = :productoId AND l.fechaCaducidad < :hoy AND l.cantidadActual > 0 ORDER BY l.fechaCaducidad ASC")
    List<Lote> findLotesVencidos(@Param("productoId") Integer productoId, @Param("hoy") LocalDate hoy);


    @Query("SELECT COALESCE(SUM(l.cantidadActual), 0) FROM Lote l \n" +
           "WHERE l.producto.productoId = :productoId \n" +
           "  AND l.cantidadActual > 0 \n" +
           "  AND l.fechaCaducidad >= :hoy")
    BigDecimal sumCantidadDisponible(@Param("productoId") Integer productoId,
                                     @Param("hoy") LocalDate hoy);

}