package com.inventa.inventa.service;

import com.inventa.inventa.dto.compra.CompraRequestDTO;
import com.inventa.inventa.dto.detallecompra.DetalleCompraRequestDTO;
import com.inventa.inventa.entity.*;
import com.inventa.inventa.repository.*;
import com.inventa.inventa.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CompraService {
    private final CompraRepository compraRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final ProductoRepository productoRepository;
    private final LoteRepository loteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProveedorRepository proveedorRepository;

    public CompraService(CompraRepository compraRepository, DetalleCompraRepository detalleCompraRepository,
                         ProductoRepository productoRepository, LoteRepository loteRepository,
                         UsuarioRepository usuarioRepository, ProveedorRepository proveedorRepository) {
        this.compraRepository = compraRepository;
        this.detalleCompraRepository = detalleCompraRepository;
        this.productoRepository = productoRepository;
        this.loteRepository = loteRepository;
        this.usuarioRepository = usuarioRepository;
        this.proveedorRepository = proveedorRepository;
    }

    public List<Compra> listar() {
        return compraRepository.findAll();
    }

    public Optional<Compra> buscarPorId(Integer id) {
        return compraRepository.findById(id);
    }

    // Este método se mantiene para actualizaciones simples u otros casos de uso.
    public Compra guardar(Compra compra) {
        return compraRepository.save(compra);
    }

    @Transactional
    public Compra crearCompraCompleta(CompraRequestDTO compraDTO) {
        // 1. Validar y obtener entidades relacionadas (Usuario, Proveedor)
        Usuario usuario = usuarioRepository.findById(compraDTO.getUsuarioId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id: " + compraDTO.getUsuarioId()));
        Proveedor proveedor = proveedorRepository.findById(compraDTO.getProveedorId())
                .orElseThrow(() -> new NotFoundException("Proveedor no encontrado con id: " + compraDTO.getProveedorId()));

        // 2. Crear la entidad Compra principal
        Compra compra = new Compra();
        compra.setUsuario(usuario);
        compra.setProveedor(proveedor);
        compra.setFechaCompra(compraDTO.getFechaCompra());
        compra.setNumeroFactura(compraDTO.getNumeroFactura());
        compra.setMontoTotal(BigDecimal.ZERO); // Se calculará a partir de los detalles
        Compra savedCompra = compraRepository.save(compra);

        BigDecimal montoCalculado = BigDecimal.ZERO;

        // 3. Procesar cada detalle de la compra
        for (DetalleCompraRequestDTO detalleDTO : compraDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new NotFoundException("Producto no encontrado con id: " + detalleDTO.getProductoId()));

            // Crear y guardar el DetalleCompra
            DetalleCompra detalleCompra = new DetalleCompra();
            detalleCompra.setCompra(savedCompra);
            detalleCompra.setProducto(producto);
            detalleCompra.setCantidad(detalleDTO.getCantidad());
            detalleCompra.setCostoUnitarioCompra(detalleDTO.getCostoUnitarioCompra());
            BigDecimal subtotal = detalleDTO.getCantidad().multiply(detalleDTO.getCostoUnitarioCompra());
            detalleCompra.setSubtotal(subtotal);
            DetalleCompra savedDetalleCompra = detalleCompraRepository.save(detalleCompra);

            // Crear y guardar el Lote asociado
            Lote lote = new Lote();
            lote.setProducto(producto);
            lote.setDetalleCompra(savedDetalleCompra);
            lote.setFechaCaducidad(detalleDTO.getFechaCaducidad());
            lote.setCantidadInicial(detalleDTO.getCantidad());
            lote.setCantidadActual(detalleDTO.getCantidad());
            loteRepository.save(lote);

            // Actualizar el stock y costo del producto
            producto.setStockActual(producto.getStockActual().add(detalleDTO.getCantidad()));
            producto.setUltimoCosto(detalleDTO.getCostoUnitarioCompra());
            productoRepository.save(producto);

            montoCalculado = montoCalculado.add(subtotal);
        }

        // 4. Actualizar el monto total en la compra y guardarla finalmente
        savedCompra.setMontoTotal(montoCalculado);
        return compraRepository.save(savedCompra);
    }

    public void eliminar(Integer id) {
        // Considerar la lógica de negocio aquí: ¿eliminar una compra debería revertir el stock?
        // Por ahora, se mantiene la eliminación simple.
        compraRepository.deleteById(id);
    }
}