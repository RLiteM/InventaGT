package com.inventa.inventa.service.dashboard;

import com.inventa.inventa.dto.dashboard.ResumenVendedorHoyDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class VendedorDashboardService {

    private final JdbcTemplate jdbcTemplate;

    public VendedorDashboardService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ResumenVendedorHoyDTO getResumenVendedorHoy(Integer usuarioId) {
        String sql = "SELECT " +
                "COUNT(*) AS cantidad_ventas_hoy, " +
                "COUNT(DISTINCT v.cliente_id) AS clientes_atendidos_hoy, " +
                "COALESCE(SUM(v.monto_total), 0) AS total_vendido_hoy " +
                "FROM tienda_garcia.venta v " +
                "WHERE v.usuario_id = ? " +
                "AND DATE(v.fecha_venta) = CURRENT_DATE";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new ResumenVendedorHoyDTO(
                rs.getBigDecimal("total_vendido_hoy"),
                rs.getLong("cantidad_ventas_hoy"),
                rs.getLong("clientes_atendidos_hoy")
        ), usuarioId);
    }
}
