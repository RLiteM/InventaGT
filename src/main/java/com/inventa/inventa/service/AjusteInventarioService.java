package com.inventa.inventa.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.inventa.inventa.dto.ajusteinventario.AjusteInventarioRequestDTO;
import com.inventa.inventa.entity.AjusteInventario;
import com.inventa.inventa.entity.AjusteInventario.TipoAjuste;
import com.inventa.inventa.entity.Lote;
import com.inventa.inventa.entity.Producto;
import com.inventa.inventa.entity.Usuario;
import com.inventa.inventa.mapper.AjusteInventarioMapper;
import com.inventa.inventa.repository.AjusteInventarioRepository;
import com.inventa.inventa.repository.LoteRepository;

@Service
public class AjusteInventarioService {
    private final AjusteInventarioRepository ajusteInventarioRepository;
    private final LoteService loteService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;
    private final AjusteInventarioMapper ajusteInventarioMapper;
    private final LoteRepository loteRepository;

    public AjusteInventarioService(AjusteInventarioRepository ajusteInventarioRepository,
            LoteService loteService,
            ProductoService productoService,
            UsuarioService usuarioService,
            AjusteInventarioMapper ajusteInventarioMapper,
            LoteRepository loteRepository) {
        this.ajusteInventarioRepository = ajusteInventarioRepository;
        this.loteService = loteService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.ajusteInventarioMapper = ajusteInventarioMapper;
        this.loteRepository = loteRepository;
    }

    public List<AjusteInventario> listar() {
        return ajusteInventarioRepository.findAll();
    }

    public Optional<AjusteInventario> buscarPorId(Integer id) {
        return ajusteInventarioRepository.findById(id);
    }

    @Transactional
    public AjusteInventario guardarDesdeDto(AjusteInventarioRequestDTO dto) {
        Usuario usuario = usuarioService.buscarPorId(dto.getUsuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        validarCantidad(dto.getCantidad());

        List<AjusteInventario> ajustesCreados = new ArrayList<>();

        switch (dto.getMotivoAjuste()) {
            case AJUSTE_CONTEO:
                ajustesCreados.add(procesarAjusteConteo(dto, usuario));
                break;
            case VENCIMIENTO:
                ajustesCreados.addAll(procesarAjusteVencimiento(dto, usuario));
                break;
            case DAÑO:
                ajustesCreados.add(procesarAjusteDaño(dto, usuario));
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Motivo de ajuste no válido");
        }

        if (ajustesCreados.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo procesar el ajuste, verifique los datos (ej. no hay stock vencido para el producto).");
        }
        
        return ajustesCreados.get(0);
    }

    private AjusteInventario procesarAjusteConteo(AjusteInventarioRequestDTO dto, Usuario usuario) {
        if (dto.getLoteId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Para AJUSTE_CONTEO, se requiere el loteId.");
        }
        if (dto.getTipoAjuste() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Para AJUSTE_CONTEO, se requiere el tipoAjuste (Entrada o Salida).");
        }

        Lote lote = loteService.buscarPorId(dto.getLoteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote no encontrado: " + dto.getLoteId()));
        
        AjusteInventario ajuste = ajusteInventarioMapper.toEntity(dto, lote, usuario);
        return guardar(ajuste);
    }

    private List<AjusteInventario> procesarAjusteVencimiento(AjusteInventarioRequestDTO dto, Usuario usuario) {
        if (dto.getProductoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Para VENCIMIENTO, se requiere el productoId.");
        }
        if (dto.getTipoAjuste() != TipoAjuste.Salida) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ajustes por VENCIMIENTO solo pueden ser de tipo Salida.");
        }

        // Combine expired and non-expired lots, with expired ones first.
        List<Lote> lotesVencidos = loteRepository.findLotesVencidos(dto.getProductoId(), LocalDate.now());
        List<Lote> lotesDisponibles = loteRepository.findLotesDisponiblesFefo(dto.getProductoId(), LocalDate.now());
        
        List<Lote> lotesParaAjustar = new ArrayList<>(lotesVencidos);
        lotesParaAjustar.addAll(lotesDisponibles);

        if (lotesParaAjustar.isEmpty()) {
            return new ArrayList<>();
        }

        List<AjusteInventario> ajustesCreados = new ArrayList<>();
        BigDecimal cantidadRestantePorAjustar = dto.getCantidad();

        for (Lote lote : lotesParaAjustar) {
            if (lote.getCantidadActual() == null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    "Conflicto de datos: El Lote con ID " + lote.getLoteId() + " tiene un stock nulo (null). Por favor, corrija los datos.");
            }

            if (cantidadRestantePorAjustar.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal cantidadEnLote = lote.getCantidadActual();
            BigDecimal cantidadAAjustarEsteLote = cantidadEnLote.min(cantidadRestantePorAjustar);

            AjusteInventarioRequestDTO dtoParaLote = createDtoForSubAdjustment(dto, cantidadAAjustarEsteLote);
            
            AjusteInventario ajuste = ajusteInventarioMapper.toEntity(dtoParaLote, lote, usuario);
            ajustesCreados.add(guardar(ajuste));

            cantidadRestantePorAjustar = cantidadRestantePorAjustar.subtract(cantidadAAjustarEsteLote);
        }

        return ajustesCreados;
    }

    private AjusteInventario procesarAjusteDaño(AjusteInventarioRequestDTO dto, Usuario usuario) {
        if (dto.getTipoAjuste() != TipoAjuste.Salida) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ajustes por DAÑO solo pueden ser de tipo Salida.");
        }

        Lote lote;
        if (dto.getLoteId() != null) {
            lote = loteService.buscarPorId(dto.getLoteId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lote no encontrado: " + dto.getLoteId()));
        } else if (dto.getProductoId() != null) {
            lote = loteRepository.findLotesDisponiblesFefo(dto.getProductoId(), LocalDate.now())
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay lotes disponibles con stock para el producto " + dto.getProductoId() + " (FIFO)."));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Para DAÑO, se requiere loteId o productoId.");
        }

        if (lote.getCantidadActual() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    "Conflicto de datos: El Lote con ID " + lote.getLoteId() + " tiene un stock nulo (null). Por favor, corrija los datos.");
        }

        AjusteInventario ajuste = ajusteInventarioMapper.toEntity(dto, lote, usuario);
        return guardar(ajuste);
    }
    
    private AjusteInventarioRequestDTO createDtoForSubAdjustment(AjusteInventarioRequestDTO original, BigDecimal newQuantity) {
        AjusteInventarioRequestDTO subDto = new AjusteInventarioRequestDTO();
        subDto.setMotivoAjuste(original.getMotivoAjuste());
        subDto.setDescripcion(original.getDescripcion());
        subDto.setTipoAjuste(original.getTipoAjuste());
        subDto.setUsuarioId(original.getUsuarioId());
        subDto.setProductoId(original.getProductoId());
        subDto.setCantidad(newQuantity);
        return subDto;
    }

    @Transactional
    public AjusteInventario actualizarDesdeDto(Integer id, AjusteInventarioRequestDTO dto) {
        throw new UnsupportedOperationException("La actualización de ajustes ha sido deshabilitada temporalmente debido a la nueva lógica de creación.");
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
        throw new UnsupportedOperationException("La actualización de ajustes ha sido deshabilitada temporalmente debido a la nueva lógica de creación.");
    }

    @Transactional
    public void eliminar(Integer id) {
        throw new UnsupportedOperationException("La eliminación de ajustes ha sido deshabilitada temporalmente debido a la nueva lógica de creación.");
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
                    "El lote seleccionado con ID " + lote.getLoteId() + " no tiene un producto asociado");
        }

        // Defensive null checks for data integrity
        if (lote.getCantidadActual() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    "Conflicto de datos: El Lote con ID " + lote.getLoteId() + " tiene un stock nulo (null). Por favor, corrija los datos en la base de datos.");
        }
        if (producto.getStockActual() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, 
                    "Conflicto de datos: El Producto con ID " + producto.getProductoId() + " ('" + producto.getNombre() + "') tiene un stock nulo (null). Por favor, corrija los datos en la base de datos.");
        }

        BigDecimal stockActualLote = lote.getCantidadActual();
        BigDecimal stockActualProducto = producto.getStockActual();

        switch (tipoAjuste) {
            case Entrada:
                lote.setCantidadActual(stockActualLote.add(cantidad));
                producto.setStockActual(stockActualProducto.add(cantidad));
                break;
            case Salida:
                if (stockActualLote.compareTo(cantidad) < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "No hay existencias suficientes en el lote para registrar el ajuste de salida");
                }
                 if (stockActualProducto.compareTo(cantidad) < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "No hay existencias suficientes en el producto para registrar el ajuste de salida");
                }
                lote.setCantidadActual(stockActualLote.subtract(cantidad));
                producto.setStockActual(stockActualProducto.subtract(cantidad));
                break;
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
