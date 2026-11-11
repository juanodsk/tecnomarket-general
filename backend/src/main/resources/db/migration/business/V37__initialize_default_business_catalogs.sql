INSERT INTO business_catalog_item (
    business_id,
    catalog_type,
    code,
    name,
    description,
    sort_order,
    protected_item,
    active,
    created_at,
    updated_at
)
SELECT business.id,
       catalog.catalog_type,
       catalog.code,
       catalog.name,
       catalog.description,
       catalog.sort_order,
       catalog.protected_item,
       TRUE,
       NOW(),
       NOW()
FROM business
CROSS JOIN (
    VALUES
        ('SERVICE_CATEGORY', 'DIAGNOSTIC', 'Diagnostico', 'Revision y diagnostico del equipo.', 10, FALSE),
        ('SERVICE_CATEGORY', 'SCREEN_REPAIR', 'Reparacion de pantalla', 'Cambio o reparacion de pantalla.', 20, FALSE),
        ('SERVICE_CATEGORY', 'BATTERY_CHANGE', 'Cambio de bateria', 'Reemplazo de bateria.', 30, FALSE),
        ('SERVICE_CATEGORY', 'CHARGING_PORT_REPAIR', 'Reparacion de puerto de carga', 'Revision o reemplazo del puerto de carga.', 40, FALSE),
        ('SERVICE_CATEGORY', 'WATER_DAMAGE', 'Dano por humedad', 'Diagnostico y tratamiento de equipos con humedad.', 50, FALSE),
        ('SERVICE_CATEGORY', 'SOFTWARE_REPAIR', 'Reparacion de software', 'Actualizacion, recuperacion o configuracion de software.', 60, FALSE),
        ('SERVICE_CATEGORY', 'MAINTENANCE', 'Mantenimiento', 'Limpieza y mantenimiento preventivo.', 70, FALSE),
        ('SERVICE_CATEGORY', 'OTHER', 'Otro servicio', 'Otros servicios tecnicos.', 80, FALSE),

        ('DEVICE_TYPE', 'PHONE', 'Celular', 'Telefono movil.', 10, FALSE),
        ('DEVICE_TYPE', 'TABLET', 'Tablet', 'Tableta electronica.', 20, FALSE),
        ('DEVICE_TYPE', 'LAPTOP', 'Portatil', 'Computador portatil.', 30, FALSE),
        ('DEVICE_TYPE', 'DESKTOP', 'Computador de escritorio', 'Computador de escritorio.', 40, FALSE),
        ('DEVICE_TYPE', 'CONSOLE', 'Consola', 'Consola de videojuegos.', 50, FALSE),
        ('DEVICE_TYPE', 'SMARTWATCH', 'Reloj inteligente', 'Reloj o dispositivo vestible.', 60, FALSE),
        ('DEVICE_TYPE', 'OTHER', 'Otro dispositivo', 'Otro tipo de dispositivo.', 70, FALSE),

        ('BRAND', 'APPLE', 'Apple', NULL, 10, FALSE),
        ('BRAND', 'SAMSUNG', 'Samsung', NULL, 20, FALSE),
        ('BRAND', 'XIAOMI', 'Xiaomi', NULL, 30, FALSE),
        ('BRAND', 'MOTOROLA', 'Motorola', NULL, 40, FALSE),
        ('BRAND', 'HUAWEI', 'Huawei', NULL, 50, FALSE),
        ('BRAND', 'OPPO', 'Oppo', NULL, 60, FALSE),
        ('BRAND', 'REALME', 'Realme', NULL, 70, FALSE),
        ('BRAND', 'HONOR', 'Honor', NULL, 80, FALSE),
        ('BRAND', 'LENOVO', 'Lenovo', NULL, 90, FALSE),
        ('BRAND', 'HP', 'HP', NULL, 100, FALSE),
        ('BRAND', 'DELL', 'Dell', NULL, 110, FALSE),
        ('BRAND', 'ASUS', 'Asus', NULL, 120, FALSE),
        ('BRAND', 'ACER', 'Acer', NULL, 130, FALSE),
        ('BRAND', 'SONY', 'Sony', NULL, 140, FALSE),
        ('BRAND', 'MICROSOFT', 'Microsoft', NULL, 150, FALSE),
        ('BRAND', 'NINTENDO', 'Nintendo', NULL, 160, FALSE),
        ('BRAND', 'OTHER', 'Otra marca', NULL, 170, FALSE),

        ('APPOINTMENT_STATUS', 'SCHEDULED', 'Agendada', 'Cita registrada y pendiente de confirmacion.', 10, TRUE),
        ('APPOINTMENT_STATUS', 'CONFIRMED', 'Confirmada', 'Cita confirmada por el negocio.', 20, TRUE),
        ('APPOINTMENT_STATUS', 'COMPLETED', 'Completada', 'La atencion programada fue completada.', 30, TRUE),
        ('APPOINTMENT_STATUS', 'NO_SHOW', 'No asistio', 'El cliente no asistio a la cita.', 40, TRUE),
        ('APPOINTMENT_STATUS', 'CANCELLED', 'Cancelada', 'La cita fue cancelada.', 50, TRUE),

        ('REPAIR_ORDER_STATUS', 'RECEIVED', 'Recibida', 'Equipo recibido por el negocio.', 10, TRUE),
        ('REPAIR_ORDER_STATUS', 'DIAGNOSIS', 'En diagnostico', 'Equipo en proceso de diagnostico.', 20, TRUE),
        ('REPAIR_ORDER_STATUS', 'WAITING_APPROVAL', 'Esperando aprobacion', 'Diagnostico o cotizacion pendiente de aprobacion.', 30, TRUE),
        ('REPAIR_ORDER_STATUS', 'WAITING_PARTS', 'Esperando repuestos', 'Reparacion en espera de repuestos.', 40, TRUE),
        ('REPAIR_ORDER_STATUS', 'REPAIRING', 'En reparacion', 'Equipo en proceso de reparacion.', 50, TRUE),
        ('REPAIR_ORDER_STATUS', 'TESTING', 'En pruebas', 'Equipo reparado en proceso de pruebas.', 60, TRUE),
        ('REPAIR_ORDER_STATUS', 'READY_FOR_PICKUP', 'Listo para entrega', 'Equipo listo para ser recogido.', 70, TRUE),
        ('REPAIR_ORDER_STATUS', 'DELIVERED', 'Entregada', 'Equipo entregado al cliente.', 80, TRUE),
        ('REPAIR_ORDER_STATUS', 'CANCELLED', 'Cancelada', 'Orden cancelada.', 90, TRUE),

        ('QUOTATION_STATUS', 'DRAFT', 'Borrador', 'Cotizacion en preparacion.', 10, TRUE),
        ('QUOTATION_STATUS', 'PENDING', 'Pendiente', 'Cotizacion enviada y pendiente de respuesta.', 20, TRUE),
        ('QUOTATION_STATUS', 'APPROVED', 'Aprobada', 'Cotizacion aprobada por el cliente.', 30, TRUE),
        ('QUOTATION_STATUS', 'REJECTED', 'Rechazada', 'Cotizacion rechazada por el cliente.', 40, TRUE),
        ('QUOTATION_STATUS', 'EXPIRED', 'Vencida', 'Cotizacion vencida.', 50, TRUE),
        ('QUOTATION_STATUS', 'CANCELLED', 'Cancelada', 'Cotizacion cancelada.', 60, TRUE),

        ('ITEM_CATEGORY', 'ACCESSORY', 'Accesorio', 'Accesorios disponibles para venta.', 10, FALSE),
        ('ITEM_CATEGORY', 'SPARE_PART', 'Repuesto', 'Repuestos utilizados en reparaciones.', 20, FALSE),
        ('ITEM_CATEGORY', 'SUPPLY', 'Insumo', 'Insumos consumibles del negocio.', 30, FALSE),
        ('ITEM_CATEGORY', 'TOOL', 'Herramienta', 'Herramientas de trabajo.', 40, FALSE),

        ('MOVEMENT_TYPE', 'IN', 'Entrada', 'Ingreso de unidades al inventario.', 10, TRUE),
        ('MOVEMENT_TYPE', 'OUT', 'Salida', 'Salida de unidades del inventario.', 20, TRUE),
        ('MOVEMENT_TYPE', 'ADJUSTMENT', 'Ajuste', 'Ajuste positivo de inventario.', 30, TRUE),
        ('MOVEMENT_TYPE', 'RETURN', 'Devolucion', 'Devolucion de unidades al inventario.', 40, TRUE),

        ('PAYMENT_METHOD', 'CASH', 'Efectivo', NULL, 10, FALSE),
        ('PAYMENT_METHOD', 'CARD', 'Tarjeta', NULL, 20, FALSE),
        ('PAYMENT_METHOD', 'TRANSFER', 'Transferencia', NULL, 30, FALSE),
        ('PAYMENT_METHOD', 'NEQUI', 'Nequi', NULL, 40, FALSE),
        ('PAYMENT_METHOD', 'DAVIPLATA', 'Daviplata', NULL, 50, FALSE),
        ('PAYMENT_METHOD', 'PSE', 'PSE', NULL, 60, FALSE)
) AS catalog(catalog_type, code, name, description, sort_order, protected_item)
WHERE business.deleted_at IS NULL
  AND NOT EXISTS (
      SELECT 1
      FROM business_catalog_item existing
      WHERE existing.business_id = business.id
        AND existing.catalog_type = catalog.catalog_type
        AND existing.code = catalog.code
        AND existing.deleted_at IS NULL
  );
