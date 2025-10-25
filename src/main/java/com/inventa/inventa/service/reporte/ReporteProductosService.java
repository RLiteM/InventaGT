package com.inventa.inventa.service.reporte;

import com.inventa.inventa.dto.reporte.ProductoSinStockItemDTO;
import com.inventa.inventa.dto.reporte.ProductosResumenStockDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class ReporteProductosService {

    private final JdbcTemplate jdbcTemplate;

    public ReporteProductosService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ProductosResumenStockDTO getResumenStock() {
        String sql = "SELECT " +
                "COALESCE((SELECT COUNT(*) FROM tienda_garcia.producto), 0) AS total_productos, " +
                "COALESCE((SELECT SUM(stock_actual) FROM tienda_garcia.producto), 0) AS stock_total, " +
                "COALESCE((SELECT SUM(stock_actual * ultimo_costo) FROM tienda_garcia.producto), 0) AS valor_inventario, " +
                "COALESCE((SELECT COUNT(*) FROM tienda_garcia.producto WHERE stock_actual <= 0), 0) AS productos_sin_stock, " +
                "COALESCE((SELECT COUNT(*) FROM tienda_garcia.producto WHERE stock_actual <= stock_minimo), 0) AS productos_bajo_stock";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapResumen(rs));
    }

    public List<ProductoSinStockItemDTO> listarProductosSinStock() {
        String sql = "SELECT sku, nombre, stock_actual FROM tienda_garcia.producto WHERE stock_actual <= 0 ORDER BY nombre ASC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ProductoSinStockItemDTO(
                rs.getString("sku"),
                rs.getString("nombre"),
                rs.getBigDecimal("stock_actual")
        ));
    }

    private ProductosResumenStockDTO mapResumen(ResultSet rs) throws SQLException {
        long totalProductos = rs.getLong("total_productos");
        BigDecimal stockTotal = rs.getBigDecimal("stock_total");
        BigDecimal valorInventario = rs.getBigDecimal("valor_inventario");
        long productosSinStock = rs.getLong("productos_sin_stock");
        long productosBajoStock = rs.getLong("productos_bajo_stock");
        return new ProductosResumenStockDTO(totalProductos, stockTotal, valorInventario, productosSinStock, productosBajoStock);
    }
}

