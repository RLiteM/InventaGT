package com.inventa.inventa.service.dashboard;

import com.inventa.inventa.dto.dashboard.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardService {

    private final JdbcTemplate jdbcTemplate;

    public DashboardService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * KPI principales: total productos, stock, valor inventario, ventas y ganancia del mes, total clientes.
     */
    public ResumenDashboardDTO getResumenDashboard() {
        String sql = "SELECT " +
            "COALESCE((SELECT COUNT(*) FROM producto), 0) AS totalProductos, " +
            "COALESCE((SELECT SUM(stock_actual) FROM producto), 0) AS stockTotal, " +
            "COALESCE((SELECT SUM(stock_actual * ultimo_costo) FROM producto), 0) AS valorInventario, " +
            "COALESCE((SELECT SUM(monto_total) FROM venta), 0) AS totalVentasHistorico, " +
            "COALESCE((SELECT SUM(dv.cantidad * (dv.precio_unitario_venta - p.ultimo_costo)) FROM detalle_venta dv JOIN lote l ON dv.lote_id = l.lote_id JOIN producto p ON l.producto_id = p.producto_id), 0) AS gananciaBrutaHistorica, " +
            "COALESCE((SELECT COUNT(*) FROM cliente), 0) AS totalClientes";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new ResumenDashboardDTO(
            rs.getLong("totalProductos"),
            rs.getBigDecimal("stockTotal"),
            rs.getBigDecimal("valorInventario"),
            rs.getBigDecimal("totalVentasHistorico"),
            rs.getBigDecimal("gananciaBrutaHistorica"),
            rs.getLong("totalClientes")
        ));
    }

    /**
     * Alertas: Productos con stock bajo el mínimo y lotes próximos a caducar (30 días).
     */
    public AlertasDashboardDTO getAlertasDashboard() {
        String stockSql = "SELECT producto_id, nombre, sku, stock_actual, stock_minimo FROM producto WHERE stock_actual <= stock_minimo ORDER BY stock_actual ASC LIMIT 10";
        List<AlertasDashboardDTO.AlertaStockDTO> stockAlerts = jdbcTemplate.query(stockSql, (rs, rowNum) -> new AlertasDashboardDTO.AlertaStockDTO(
            rs.getInt("producto_id"),
            rs.getString("nombre"),
            rs.getString("sku"),
            rs.getBigDecimal("stock_actual"),
            rs.getBigDecimal("stock_minimo")
        ));

        String caducidadSql = "SELECT l.lote_id, p.nombre, l.fecha_caducidad, l.cantidad_actual, p.sku FROM lote l JOIN producto p ON l.producto_id = p.producto_id WHERE l.fecha_caducidad BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '30 day' AND l.cantidad_actual > 0 ORDER BY l.fecha_caducidad ASC LIMIT 10";
        List<AlertasDashboardDTO.AlertaCaducidadDTO> caducidadAlerts = jdbcTemplate.query(caducidadSql, (rs, rowNum) -> new AlertasDashboardDTO.AlertaCaducidadDTO(
            rs.getInt("lote_id"),
            rs.getString("nombre"),
            rs.getObject("fecha_caducidad", LocalDate.class),
            rs.getBigDecimal("cantidad_actual"),
            rs.getString("sku")
        ));

        return new AlertasDashboardDTO(stockAlerts, caducidadAlerts);
    }

    /**
     * Movimientos: Últimas 5 entradas (compras/ajustes) y 5 salidas (ventas/ajustes).
     */
    public MovimientosRecientesDTO getMovimientosRecientes() {
        String entradasSql = "(SELECT c.fecha_compra AS fecha, 'Compra' AS tipo, dc.cantidad, p.nombre AS producto_nombre, u.nombre_usuario AS usuario FROM detalle_compra dc JOIN compra c ON dc.compra_id = c.compra_id JOIN producto p ON dc.producto_id = p.producto_id JOIN usuario u ON c.usuario_id = u.usuario_id ORDER BY c.fecha_compra DESC LIMIT 5) " +
                             "UNION ALL " +
                             "(SELECT ai.fecha_ajuste::date AS fecha, 'Ajuste Entrada' AS tipo, ai.cantidad, p.nombre AS producto_nombre, u.nombre_usuario AS usuario FROM ajuste_inventario ai JOIN lote l ON ai.lote_id = l.lote_id JOIN producto p ON l.producto_id = p.producto_id JOIN usuario u ON ai.usuario_id = u.usuario_id WHERE ai.tipo_ajuste = 'Entrada' ORDER BY ai.fecha_ajuste DESC LIMIT 5) ORDER BY fecha DESC LIMIT 5";
        List<MovimientosRecientesDTO.MovimientoInventarioDTO> entradas = jdbcTemplate.query(entradasSql, (rs, rowNum) -> new MovimientosRecientesDTO.MovimientoInventarioDTO(
            rs.getObject("fecha", LocalDate.class),
            rs.getString("tipo"),
            rs.getBigDecimal("cantidad"),
            rs.getString("producto_nombre"),
            rs.getString("usuario")
        ));

        String salidasSql = "(SELECT v.fecha_venta::date AS fecha, 'Venta' AS tipo, dv.cantidad, p.nombre AS producto_nombre, u.nombre_usuario AS usuario FROM detalle_venta dv JOIN venta v ON dv.venta_id = v.venta_id JOIN lote l ON dv.lote_id = l.lote_id JOIN producto p ON l.producto_id = p.producto_id JOIN usuario u ON v.usuario_id = u.usuario_id ORDER BY v.fecha_venta DESC LIMIT 5) " +
                            "UNION ALL " +
                            "(SELECT ai.fecha_ajuste::date AS fecha, 'Ajuste Salida' AS tipo, ai.cantidad, p.nombre AS producto_nombre, u.nombre_usuario AS usuario FROM ajuste_inventario ai JOIN lote l ON ai.lote_id = l.lote_id JOIN producto p ON l.producto_id = p.producto_id JOIN usuario u ON ai.usuario_id = u.usuario_id WHERE ai.tipo_ajuste = 'Salida' ORDER BY ai.fecha_ajuste DESC LIMIT 5) ORDER BY fecha DESC LIMIT 5";
        List<MovimientosRecientesDTO.MovimientoInventarioDTO> salidas = jdbcTemplate.query(salidasSql, (rs, rowNum) -> new MovimientosRecientesDTO.MovimientoInventarioDTO(
            rs.getObject("fecha", LocalDate.class),
            rs.getString("tipo"),
            rs.getBigDecimal("cantidad"),
            rs.getString("producto_nombre"),
            rs.getString("usuario")
        ));

        return new MovimientosRecientesDTO(entradas, salidas);
    }

    /**
     * Top 5 productos más vendidos por unidades.
     */
    public List<TopProductoDashboardDTO> getTopProductosMasVendidos() {
        String sql = "SELECT p.producto_id, p.nombre, p.sku, SUM(dv.cantidad) AS totalUnidadesVendidas, SUM(dv.subtotal) AS ingresosGenerados " +
                     "FROM detalle_venta dv " +
                     "JOIN lote l ON dv.lote_id = l.lote_id " +
                     "JOIN producto p ON l.producto_id = p.producto_id " +
                     "GROUP BY p.producto_id, p.nombre, p.sku " +
                     "ORDER BY totalUnidadesVendidas DESC LIMIT 5";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new TopProductoDashboardDTO(
            rs.getInt("producto_id"),
            rs.getString("nombre"),
            rs.getString("sku"),
            rs.getLong("totalUnidadesVendidas"),
            rs.getBigDecimal("ingresosGenerados")
        ));
    }

    /**
     * Top 5 clientes con más compras por monto.
     */
    public List<TopClienteDashboardDTO> getTopClientes() {
        String sql = "SELECT c.cliente_id, c.nombre_completo, COUNT(v.venta_id) AS totalCompras, SUM(v.monto_total) AS montoTotalGastado " +
                     "FROM venta v " +
                     "JOIN cliente c ON v.cliente_id = c.cliente_id " +
                     "GROUP BY c.cliente_id, c.nombre_completo " +
                     "ORDER BY montoTotalGastado DESC LIMIT 5";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new TopClienteDashboardDTO(
            rs.getInt("cliente_id"),
            rs.getString("nombre_completo"),
            rs.getLong("totalCompras"),
            rs.getBigDecimal("montoTotalGastado")
        ));
    }

    /**
     * Tendencia de ventas de los últimos 12 meses.
     */
    public List<TendenciaVentaDTO> getTendenciaVentas() {
        String sql = "SELECT TO_CHAR(date_trunc('month', fecha_venta), 'YYYY-MM') AS mes, SUM(monto_total) AS totalVentas " +
                     "FROM venta " +
                     "WHERE fecha_venta >= date_trunc('month', CURRENT_DATE) - INTERVAL '11 month' " +
                     "GROUP BY mes " +
                     "ORDER BY mes ASC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new TendenciaVentaDTO(
            rs.getString("mes"),
            rs.getBigDecimal("totalVentas")
        ));
    }

    /**
     * Valor total del inventario agrupado por categoría.
     */
    public List<ValorCategoriaDTO> getValorInventarioPorCategoria() {
        String sql = "SELECT cat.nombre AS categoria, SUM(p.stock_actual * p.ultimo_costo) AS valorInventario, COUNT(p.producto_id) AS totalProductos " +
                     "FROM producto p " +
                     "JOIN categoria cat ON p.categoria_id = cat.categoria_id " +
                     "WHERE p.stock_actual > 0 " +
                     "GROUP BY cat.nombre " +
                     "ORDER BY valorInventario DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ValorCategoriaDTO(
            rs.getString("categoria"),
            rs.getBigDecimal("valorInventario"),
            rs.getLong("totalProductos")
        ));
    }
}
