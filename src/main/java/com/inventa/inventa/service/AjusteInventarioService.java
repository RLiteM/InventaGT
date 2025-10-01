package com.inventa.inventa.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.entity.AjusteInventario;
import com.inventa.inventa.entity.AjusteInventario.TipoAjuste;
import com.inventa.inventa.entity.Lote;
import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.repository.AjusteInventarioRepository;


@Service
public class AjusteInventarioService {
    private final AjusteInventarioRepository ajusteInventarioRepository;
    private final LoteService loteService;
    private final ProductoService productoService;

    public AjusteInventarioService(AjusteInventarioRepository ajusteInventarioRepository, LoteService loteService,
            ProductoService productoService) {
        this.ajusteInventarioRepository = ajusteInventarioRepository;
        this.loteService = loteService;
        this.productoService = productoService;
    }

    public List<AjusteInventario> listar() {
        return ajusteInventarioRepository.findAll();
    }

    public Optional<AjusteInventario> buscarPorId(Integer id) {
        return ajusteInventarioRepository.findById(id);
    }

    @Transactional
    public AjusteInventario guardar(AjusteInventario ajuste) {
        validarCantidad(ajuste.getCantidad());

        Lote lote = obtenerLoteGestionado(ajuste.getLote());
        ajuste.setLote(lote);

        aplicarAjusteSobreInventario(lote, ajuste.getTipoAjuste(), ajuste.getCantidad());

        productoService.guardar(lote.getProducto());
        loteService.guardar(lote);

        return ajusteInventarioRepository.save(ajuste);
    }

    @Transactional
    public AjusteInventario actualizar(Integer id, AjusteInventario ajusteActualizado) {
        validarCantidad(ajusteActualizado.getCantidad());

        AjusteInventario existente = ajusteInventarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ajuste no encontrado"));

        revertirImpactoInventario(existente);

        Lote loteNuevo = obtenerLoteGestionado(ajusteActualizado.getLote());

        existente.setLote(loteNuevo);
        existente.setUsuario(ajusteActualizado.getUsuario());
        existente.setTipoAjuste(ajusteActualizado.getTipoAjuste());
        existente.setCantidad(ajusteActualizado.getCantidad());
        existente.setMotivo(ajusteActualizado.getMotivo());

        aplicarAjusteSobreInventario(loteNuevo, existente.getTipoAjuste(), existente.getCantidad());

        productoService.guardar(loteNuevo.getProducto());
        loteService.guardar(loteNuevo);

        return ajusteInventarioRepository.save(existente);
    }

    @Transactional
    public void eliminar(Integer id) {
        AjusteInventario ajuste = ajusteInventarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ajuste no encontrado"));

        revertirImpactoInventario(ajuste);

        ajusteInventarioRepository.delete(ajuste);
    }

    private void validarCantidad(BigDecimal cantidad) {
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La cantidad debe ser mayor a cero");
        }
    }

    private Lote obtenerLoteGestionado(Lote lote) {
        if (lote == null || lote.getLoteId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lote inválido para el ajuste");
        }
        return loteService.buscarPorId(lote.getLoteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote no encontrado"));
    }

    private void aplicarAjusteSobreInventario(Lote lote, TipoAjuste tipoAjuste, BigDecimal cantidad) {
        if (tipoAjuste == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo de ajuste es obligatorio");
        }
        Producto producto = lote.getProducto();
        if (producto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El lote seleccionado no tiene un producto asociado");
        }

        BigDecimal stockActualLote = lote.getCantidadActual();
        BigDecimal stockActualProducto = producto.getStockActual();

        switch (tipoAjuste) {
            case Entrada -> {
                lote.setCantidadActual(stockActualLote.add(cantidad));
                producto.setStockActual(stockActualProducto.add(cantidad));
            }
            case Salida -> {
                if (stockActualLote.compareTo(cantidad) < 0 || stockActualProducto.compareTo(cantidad) < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "No hay existencias suficientes para registrar el ajuste de salida");
                }
                lote.setCantidadActual(stockActualLote.subtract(cantidad));
                producto.setStockActual(stockActualProducto.subtract(cantidad));
            }
        }
    }

    private void revertirImpactoInventario(AjusteInventario ajuste) {
        Lote lote = obtenerLoteGestionado(ajuste.getLote());
        Producto producto = lote.getProducto();
        BigDecimal cantidad = ajuste.getCantidad();

        ajuste.setLote(lote);

        if (ajuste.getTipoAjuste() == TipoAjuste.Entrada) {
            if (lote.getCantidadActual().compareTo(cantidad) < 0 || producto.getStockActual().compareTo(cantidad) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "No es posible revertir el ajuste porque dejaría existencias negativas");
            }
            lote.setCantidadActual(lote.getCantidadActual().subtract(cantidad));
            producto.setStockActual(producto.getStockActual().subtract(cantidad));
        } else {
            lote.setCantidadActual(lote.getCantidadActual().add(cantidad));
            producto.setStockActual(producto.getStockActual().add(cantidad));
        }

        productoService.guardar(producto);
        loteService.guardar(lote);
    }
}
