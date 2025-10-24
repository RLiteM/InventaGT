package com.inventa.inventa.dto.ajusteinventario;

import com.inventa.inventa.entity.AjusteInventario.TipoAjuste;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AjusteInventarioRequestDTO {

    public enum MotivoAjuste {
        AJUSTE_CONTEO,
        VENCIMIENTO,
        DAÑO
    }

    private MotivoAjuste motivoAjuste;
    private String descripcion;
    private TipoAjuste tipoAjuste;
    private BigDecimal cantidad;
    private Integer productoId;
    private Integer loteId;
    private Integer usuarioId;
    private LocalDateTime fechaAjuste;
}