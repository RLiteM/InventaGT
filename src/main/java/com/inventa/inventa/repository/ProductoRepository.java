package com.inventa.inventa.repository;

import com.inventa.inventa.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Producto findBySku(String sku);

    List<Producto> findByNombreContainingIgnoreCaseOrSkuContainingIgnoreCase(String nombre, String sku);

    @Query("SELECT DISTINCT p FROM Producto p JOIN DetalleCompra dc ON dc.producto = p WHERE dc.compra.proveedor.proveedorId = :proveedorId AND (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Producto> findByProveedorAndSearchTerm(@Param("proveedorId") Integer proveedorId, @Param("searchTerm") String searchTerm);

    List<Producto> findByCategoria_CategoriaId(Integer categoriaId);

    @Query("SELECT count(p) FROM Producto p WHERE COALESCE((SELECT SUM(l.cantidadActual) FROM Lote l WHERE l.producto = p), 0) <= :threshold")
    long countLowStockProducts(@Param("threshold") long threshold);

    @Query("SELECT p FROM Producto p WHERE p.stockActual <= p.stockMinimo")
    List<Producto> findProductosConStockCritico();

    @Query("SELECT p FROM Producto p")
    List<Producto> findAllParaResumen();
}
