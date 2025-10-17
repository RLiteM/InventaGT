-- Índices para acelerar selección FEFO y sumas por producto
-- Nota: usar esquema tienda_garcia (configurado en application.properties)

-- Índice compuesto por producto y fecha de caducidad, útil para ORDER BY
CREATE INDEX IF NOT EXISTS idx_lote_producto_caducidad
    ON tienda_garcia.lote (producto_id, fecha_caducidad, lote_id);

-- Índice parcial solo sobre lotes con stock disponible
CREATE INDEX IF NOT EXISTS idx_lote_producto_disponible
    ON tienda_garcia.lote (producto_id, lote_id)
    WHERE cantidad_actual > 0;

