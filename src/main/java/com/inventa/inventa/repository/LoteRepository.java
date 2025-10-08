package com.inventa.inventa.repository;

import com.inventa.inventa.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Integer> {

    @Query("SELECT COALESCE(SUM(l.cantidadActual), 0) FROM Lote l WHERE l.fechaCaducidad BETWEEN :startDate AND :endDate")
    long countUnitsExpiringSoon(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(l.cantidadActual * dc.costoUnitarioCompra), 0) FROM Lote l JOIN l.detalleCompra dc")
    BigDecimal findTotalInventoryValue();

}
