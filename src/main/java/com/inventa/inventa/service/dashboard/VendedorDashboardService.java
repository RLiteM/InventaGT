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
        String sql = "WITH bounds AS (" +
                "  SELECT date_trunc('day', (now() AT TIME ZONE 'America/Guatemala')) AS start_gt, " +
                "         date_trunc('day', (now() AT TIME ZONE 'America/Guatemala')) + INTERVAL '1 day' AS end_gt" +
                ") " +
                "SELECT " +
                "  COALESCE(SUM(v.monto_total), 0) AS total_vendido_hoy, " +
                "  COUNT(*) AS cantidad_ventas_hoy, " +
                "  COUNT(DISTINCT v.cliente_id) AS clientes_atendidos_hoy " +
                "FROM venta v, bounds b " +
                "WHERE v.usuario_id = ? " +
                "  AND (v.fecha_venta AT TIME ZONE 'America/Guatemala') >= b.start_gt " +
                "  AND (v.fecha_venta AT TIME ZONE 'America/Guatemala') <  b.end_gt";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new ResumenVendedorHoyDTO(
                rs.getBigDecimal("total_vendido_hoy"),
                rs.getLong("cantidad_ventas_hoy"),
                rs.getLong("clientes_atendidos_hoy")
        ), usuarioId);
    }
}
