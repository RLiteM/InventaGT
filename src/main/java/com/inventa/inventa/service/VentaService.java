package com.inventa.inventa.service;

import com.inventa.inventa.dto.venta.VentaRequestDTO;
import com.inventa.inventa.dto.detalleventa.DetalleVentaRequestDTO;
import com.inventa.inventa.entity.*;
import com.inventa.inventa.repository.*;
import com.inventa.inventa.exceptions.BadRequestException;
import com.inventa.inventa.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.inventa.inventa.entity.Venta.MetodoPago;

@Service
public class VentaService {
    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final LoteRepository loteRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    public VentaService(VentaRepository ventaRepository, DetalleVentaRepository detalleVentaRepository,
                        LoteRepository loteRepository, ProductoRepository productoRepository,
                        UsuarioRepository usuarioRepository, ClienteRepository clienteRepository) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.loteRepository = loteRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    public Optional<Venta> buscarPorId(Integer id) {
        return ventaRepository.findById(id);
    }

    public Venta guardar(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Transactional
    public Venta crearVentaCompleta(VentaRequestDTO ventaDTO) {
        Usuario usuario = usuarioRepository.findById(ventaDTO.getUsuarioId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id: " + ventaDTO.getUsuarioId()));
        Cliente cliente = clienteRepository.findById(ventaDTO.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con id: " + ventaDTO.getClienteId()));

        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setCliente(cliente);
        venta.setFechaVenta(java.time.LocalDateTime.now());
        venta.setMetodoPago(MetodoPago.valueOf(ventaDTO.getMetodoPago()));
        venta.setMontoTotal(BigDecimal.ZERO); // Se calculará
        Venta savedVenta = ventaRepository.save(venta);

        BigDecimal montoCalculado = BigDecimal.ZERO;

        for (DetalleVentaRequestDTO detalleDTO : ventaDTO.getDetalles()) {
            Lote lote = loteRepository.findById(detalleDTO.getLoteId())
                    .orElseThrow(() -> new NotFoundException("Lote no encontrado con id: " + detalleDTO.getLoteId()));
            
            Producto producto = lote.getProducto();

            if (lote.getCantidadActual().compareTo(detalleDTO.getCantidad()) < 0) {
                throw new BadRequestException("Stock insuficiente para '" + producto.getNombre() + "' en lote " + lote.getLoteId() +
                        ". Stock actual: " + lote.getCantidadActual() + ", requerido: " + detalleDTO.getCantidad());
            }

            lote.setCantidadActual(lote.getCantidadActual().subtract(detalleDTO.getCantidad()));
            producto.setStockActual(producto.getStockActual().subtract(detalleDTO.getCantidad()));

            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setVenta(savedVenta);
            detalleVenta.setLote(lote);
            detalleVenta.setCantidad(detalleDTO.getCantidad());
            detalleVenta.setPrecioUnitarioVenta(detalleDTO.getPrecioUnitarioVenta());
            BigDecimal subtotal = detalleDTO.getCantidad().multiply(detalleDTO.getPrecioUnitarioVenta());
            detalleVenta.setSubtotal(subtotal);
            detalleVentaRepository.save(detalleVenta);
            
            loteRepository.save(lote);
            productoRepository.save(producto);

            montoCalculado = montoCalculado.add(subtotal);
        }

        savedVenta.setMontoTotal(montoCalculado);
        return ventaRepository.save(savedVenta);
    }

    public void eliminar(Integer id) {
        ventaRepository.deleteById(id);
    }
}