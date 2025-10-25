package com.inventa.inventa.service.reporte;

import com.inventa.inventa.dto.reporte.VentasDiaMetodoPagoDTO;
import com.inventa.inventa.dto.reporte.VentasDiaReporteDTO;
import com.inventa.inventa.dto.reporte.VentasDiaVentaRecienteDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class VentasDiaService {

    private final JdbcTemplate jdbcTemplate;

    public VentasDiaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public VentasDiaReporteDTO getVentasDelDia() {
        String resumenSql = "SELECT " +
                "COUNT(*) AS total_ventas, " +
                "COALESCE(SUM(monto_total), 0) AS total_monto, " +
                "ROUND(AVG(monto_total), 2) AS promedio_venta, " +
                "MAX(monto_total) AS venta_maxima, " +
                "MIN(monto_total) AS venta_minima " +
                "FROM tienda_garcia.venta " +
                "WHERE DATE(fecha_venta) = CURRENT_DATE";

        VentasDiaReporteDTO dto = jdbcTemplate.queryForObject(resumenSql, (rs, rowNum) -> mapResumen(rs));

        String metodosSql = "SELECT metodo_pago, COUNT(*) AS cantidad, SUM(monto_total) AS total " +
                "FROM tienda_garcia.venta " +
                "WHERE DATE(fecha_venta) = CURRENT_DATE " +
                "GROUP BY metodo_pago";

        List<VentasDiaMetodoPagoDTO> metodos = jdbcTemplate.query(metodosSql, (rs, rowNum) ->
                new VentasDiaMetodoPagoDTO(
                        rs.getString("metodo_pago"),
                        rs.getLong("cantidad"),
                        rs.getBigDecimal("total")
                ));

        String recientesSql = "SELECT v.venta_id, v.fecha_venta, u.nombre_completo AS vendedor, c.nombre_completo AS cliente, v.monto_total, v.metodo_pago " +
                "FROM tienda_garcia.venta v " +
                "JOIN tienda_garcia.usuario u ON v.usuario_id = u.usuario_id " +
                "JOIN tienda_garcia.cliente c ON v.cliente_id = c.cliente_id " +
                "WHERE DATE(v.fecha_venta) = CURRENT_DATE " +
                "ORDER BY v.fecha_venta DESC LIMIT 5";

        List<VentasDiaVentaRecienteDTO> recientes = jdbcTemplate.query(recientesSql, (rs, rowNum) ->
                new VentasDiaVentaRecienteDTO(
                        rs.getInt("venta_id"),
                        rs.getObject("fecha_venta", LocalDateTime.class),
                        rs.getString("vendedor"),
                        rs.getString("cliente"),
                        rs.getBigDecimal("monto_total"),
                        rs.getString("metodo_pago")
                ));

        dto.setResumenMetodosPago(metodos != null ? metodos : Collections.emptyList());
        dto.setVentasRecientes(recientes != null ? recientes : Collections.emptyList());
        return dto;
    }

    private VentasDiaReporteDTO mapResumen(ResultSet rs) throws SQLException {
        long totalVentas = rs.getLong("total_ventas");
        BigDecimal totalMonto = rs.getBigDecimal("total_monto");
        BigDecimal promedioVenta = rs.getBigDecimal("promedio_venta");
        BigDecimal ventaMaxima = rs.getBigDecimal("venta_maxima");
        BigDecimal ventaMinima = rs.getBigDecimal("venta_minima");
        return new VentasDiaReporteDTO(totalVentas, totalMonto, promedioVenta, ventaMaxima, ventaMinima, null, null);
    }
}

