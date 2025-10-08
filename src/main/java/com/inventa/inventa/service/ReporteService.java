package com.inventa.inventa.service;

import com.inventa.inventa.dto.reporte.*;
import com.inventa.inventa.entity.Cliente;
import com.inventa.inventa.repository.ClienteRepository;
import com.inventa.inventa.repository.LoteRepository;
import com.inventa.inventa.repository.ProductoRepository;
import com.inventa.inventa.repository.VentaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReporteService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final LoteRepository loteRepository;
    private final ProductoRepository productoRepository;

    private static final long LOW_STOCK_THRESHOLD = 10;

    public ReporteService(VentaRepository ventaRepository, ClienteRepository clienteRepository, LoteRepository loteRepository, ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.loteRepository = loteRepository;
        this.productoRepository = productoRepository;
    }

    public DashboardAvanzadoDTO getDashboardAvanzado() {
        AnalisisFinancieroDTO financieroDTO = buildAnalisisFinanciero();
        AnalisisVentasDTO ventasDTO = buildAnalisisVentas();
        AnalisisClientesDTO clientesDTO = buildAnalisisClientes();
        AnalisisInventarioDTO inventarioDTO = buildAnalisisInventario();

        return new DashboardAvanzadoDTO(financieroDTO, ventasDTO, clientesDTO, inventarioDTO);
    }

    private AnalisisFinancieroDTO buildAnalisisFinanciero() {
        BigDecimal ingresosTotales = ventaRepository.findTotalRevenue();
        BigDecimal costoDeVentas = ventaRepository.findTotalCostOfGoodsSold();
        BigDecimal beneficioBruto = ingresosTotales.subtract(costoDeVentas);
        return new AnalisisFinancieroDTO(ingresosTotales, costoDeVentas, beneficioBruto);
    }

    private AnalisisVentasDTO buildAnalisisVentas() {
        long volumenTotalVentas = ventaRepository.count();
        List<VentasPorMesDTO> ventasPorMes = ventaRepository.findMonthlySalesLast12Months();
        Pageable top5 = PageRequest.of(0, 5);
        List<TopProductoDTO> topProductos = ventaRepository.findTopSellingProductsByUnits(top5);
        return new AnalisisVentasDTO(volumenTotalVentas, ventasPorMes, topProductos);
    }

    private AnalisisClientesDTO buildAnalisisClientes() {
        long totalMayoristas = clienteRepository.countByTipoCliente(Cliente.TipoCliente.Mayorista);
        long totalMinoristas = clienteRepository.countByTipoCliente(Cliente.TipoCliente.Minorista);
        return new AnalisisClientesDTO(totalMayoristas, totalMinoristas);
    }

    private AnalisisInventarioDTO buildAnalisisInventario() {
        long totalProductos = productoRepository.count();
        BigDecimal valorTotalInventario = loteRepository.findTotalInventoryValue();
        long conteoProductosBajoStock = productoRepository.countLowStockProducts(LOW_STOCK_THRESHOLD);
        LocalDate today = LocalDate.now();
        LocalDate in30Days = today.plusDays(30);
        long unidadesProximasAVencer = loteRepository.countUnitsExpiringSoon(today, in30Days);
        return new AnalisisInventarioDTO(totalProductos, valorTotalInventario, conteoProductosBajoStock, unidadesProximasAVencer);
    }
}
