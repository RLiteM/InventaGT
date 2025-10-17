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
import java.time.LocalDate;
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
        venta.setMontoTotal(BigDecimal.ZERO);
        Venta savedVenta = ventaRepository.save(venta);

        BigDecimal montoCalculado = BigDecimal.ZERO;

        for (DetalleVentaRequestDTO detalleDTO : ventaDTO.getDetalles()) {
            if (detalleDTO.getCantidad() == null || detalleDTO.getCantidad().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("La cantidad debe ser mayor a 0");
            }

            // Compatibilidad: si llega loteId explícito
            if (detalleDTO.getLoteId() != null) {
                Lote lote = loteRepository.findById(detalleDTO.getLoteId())
                        .orElseThrow(() -> new NotFoundException("Lote no encontrado con id: " + detalleDTO.getLoteId()));

                Producto producto = lote.getProducto();
                if (producto == null) {
                    throw new NotFoundException("No se encontró un producto asociado al lote con id: " + lote.getLoteId());
                }

                // No permitir ventas de lotes vencidos
                if (lote.getFechaCaducidad().isBefore(LocalDate.now())) {
                    throw new BadRequestException("El lote " + lote.getLoteId() + " está vencido y no puede venderse");
                }

                if (lote.getCantidadActual().compareTo(detalleDTO.getCantidad()) < 0) {
                    throw new BadRequestException("Stock insuficiente para '" + producto.getNombre() + "' en lote " + lote.getLoteId() +
                            ". Stock actual: " + lote.getCantidadActual() + ", requerido: " + detalleDTO.getCantidad());
                }

                BigDecimal precioUnitario = (detalleDTO.getPrecioUnitarioVenta() != null
                        && detalleDTO.getPrecioUnitarioVenta().compareTo(BigDecimal.ZERO) > 0)
                        ? detalleDTO.getPrecioUnitarioVenta()
                        : calcularPrecioUnitario(producto, cliente);

                // Regla: no vender por debajo del costo del lote
                BigDecimal costoUnitario = lote.getDetalleCompra() != null ? lote.getDetalleCompra().getCostoUnitarioCompra() : null;
                if (costoUnitario != null && precioUnitario.compareTo(costoUnitario) < 0) {
                    throw new BadRequestException("Precio por debajo del costo para '" + producto.getNombre() +
                            "' en lote " + lote.getLoteId() + ". Precio: " + precioUnitario + ", Costo: " + costoUnitario);
                }

                lote.setCantidadActual(lote.getCantidadActual().subtract(detalleDTO.getCantidad()));
                producto.setStockActual(producto.getStockActual().subtract(detalleDTO.getCantidad()));

                DetalleVenta detalleVenta = new DetalleVenta();
                detalleVenta.setVenta(savedVenta);
                detalleVenta.setLote(lote);
                detalleVenta.setCantidad(detalleDTO.getCantidad());
                detalleVenta.setPrecioUnitarioVenta(precioUnitario);
                BigDecimal subtotal = detalleDTO.getCantidad().multiply(precioUnitario);
                detalleVenta.setSubtotal(subtotal);
                detalleVentaRepository.save(detalleVenta);

                loteRepository.save(lote);
                productoRepository.save(producto);

                montoCalculado = montoCalculado.add(subtotal);
                continue;
            }

            // Asignación automática por productoId (FEFO)
            Integer productoId = detalleDTO.getProductoId();
            if (productoId == null) {
                throw new BadRequestException("Se requiere productoId o loteId en el detalle de venta");
            }

            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado con id: " + productoId));

            BigDecimal aVender = detalleDTO.getCantidad();
            BigDecimal disponible = loteRepository.sumCantidadDisponible(productoId, LocalDate.now());
            List<Lote> lotes = loteRepository.findLotesDisponiblesFefo(productoId, LocalDate.now());

            if (disponible.compareTo(aVender) < 0) {
                throw new BadRequestException("Stock insuficiente para '" + producto.getNombre() + "'. Disponible: " + disponible + ", requerido: " + aVender);
            }

            BigDecimal precioUnitario = (detalleDTO.getPrecioUnitarioVenta() != null
                    && detalleDTO.getPrecioUnitarioVenta().compareTo(BigDecimal.ZERO) > 0)
                    ? detalleDTO.getPrecioUnitarioVenta()
                    : calcularPrecioUnitario(producto, cliente);

            for (Lote l : lotes) {
                if (aVender.compareTo(BigDecimal.ZERO) <= 0) break;

                BigDecimal delLote = l.getCantidadActual().min(aVender);

                // Regla: no vender por debajo del costo del lote consumido
                BigDecimal costoUnitarioLote = l.getDetalleCompra() != null ? l.getDetalleCompra().getCostoUnitarioCompra() : null;
                if (costoUnitarioLote != null && precioUnitario.compareTo(costoUnitarioLote) < 0) {
                    throw new BadRequestException("Precio por debajo del costo para '" + producto.getNombre() +
                            "' en lote " + l.getLoteId() + ". Precio: " + precioUnitario + ", Costo: " + costoUnitarioLote);
                }

                l.setCantidadActual(l.getCantidadActual().subtract(delLote));
                producto.setStockActual(producto.getStockActual().subtract(delLote));

                DetalleVenta detalleVenta = new DetalleVenta();
                detalleVenta.setVenta(savedVenta);
                detalleVenta.setLote(l);
                detalleVenta.setCantidad(delLote);
                detalleVenta.setPrecioUnitarioVenta(precioUnitario);
                BigDecimal subtotal = delLote.multiply(precioUnitario);
                detalleVenta.setSubtotal(subtotal);
                detalleVentaRepository.save(detalleVenta);

                loteRepository.save(l);
                productoRepository.save(producto);

                montoCalculado = montoCalculado.add(subtotal);
                aVender = aVender.subtract(delLote);
            }
        }

        savedVenta.setMontoTotal(montoCalculado);
        return ventaRepository.save(savedVenta);
    }

    public void eliminar(Integer id) {
        ventaRepository.deleteById(id);
    }

    private BigDecimal calcularPrecioUnitario(Producto producto, Cliente cliente) {
        if (cliente != null && cliente.getTipoCliente() == Cliente.TipoCliente.Mayorista) {
            return producto.getPrecioMayorista();
        }
        return producto.getPrecioMinorista();
    }
}
