package com.tecmarket.backend.business.service;

import com.tecmarket.backend.business.entity.Business;
import com.tecmarket.backend.business.entity.BusinessCatalogItem;
import com.tecmarket.backend.business.entity.BusinessCatalogType;
import com.tecmarket.backend.business.repository.BusinessCatalogItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BusinessCatalogInitializerService {

    private static final List<CatalogTemplate> DEFAULT_CATALOGS = List.of(
            template(BusinessCatalogType.SERVICE_CATEGORY, "DIAGNOSTIC", "Diagnostico", "Revision y diagnostico del equipo.", 10, false),
            template(BusinessCatalogType.SERVICE_CATEGORY, "SCREEN_REPAIR", "Reparacion de pantalla", "Cambio o reparacion de pantalla.", 20, false),
            template(BusinessCatalogType.SERVICE_CATEGORY, "BATTERY_CHANGE", "Cambio de bateria", "Reemplazo de bateria.", 30, false),
            template(BusinessCatalogType.SERVICE_CATEGORY, "CHARGING_PORT_REPAIR", "Reparacion de puerto de carga", "Revision o reemplazo del puerto de carga.", 40, false),
            template(BusinessCatalogType.SERVICE_CATEGORY, "WATER_DAMAGE", "Dano por humedad", "Diagnostico y tratamiento de equipos con humedad.", 50, false),
            template(BusinessCatalogType.SERVICE_CATEGORY, "SOFTWARE_REPAIR", "Reparacion de software", "Actualizacion, recuperacion o configuracion de software.", 60, false),
            template(BusinessCatalogType.SERVICE_CATEGORY, "MAINTENANCE", "Mantenimiento", "Limpieza y mantenimiento preventivo.", 70, false),
            template(BusinessCatalogType.SERVICE_CATEGORY, "OTHER", "Otro servicio", "Otros servicios tecnicos.", 80, false),

            template(BusinessCatalogType.DEVICE_TYPE, "PHONE", "Celular", "Telefono movil.", 10, false),
            template(BusinessCatalogType.DEVICE_TYPE, "TABLET", "Tablet", "Tableta electronica.", 20, false),
            template(BusinessCatalogType.DEVICE_TYPE, "LAPTOP", "Portatil", "Computador portatil.", 30, false),
            template(BusinessCatalogType.DEVICE_TYPE, "DESKTOP", "Computador de escritorio", "Computador de escritorio.", 40, false),
            template(BusinessCatalogType.DEVICE_TYPE, "CONSOLE", "Consola", "Consola de videojuegos.", 50, false),
            template(BusinessCatalogType.DEVICE_TYPE, "SMARTWATCH", "Reloj inteligente", "Reloj o dispositivo vestible.", 60, false),
            template(BusinessCatalogType.DEVICE_TYPE, "OTHER", "Otro dispositivo", "Otro tipo de dispositivo.", 70, false),

            template(BusinessCatalogType.BRAND, "APPLE", "Apple", null, 10, false),
            template(BusinessCatalogType.BRAND, "SAMSUNG", "Samsung", null, 20, false),
            template(BusinessCatalogType.BRAND, "XIAOMI", "Xiaomi", null, 30, false),
            template(BusinessCatalogType.BRAND, "MOTOROLA", "Motorola", null, 40, false),
            template(BusinessCatalogType.BRAND, "HUAWEI", "Huawei", null, 50, false),
            template(BusinessCatalogType.BRAND, "OPPO", "Oppo", null, 60, false),
            template(BusinessCatalogType.BRAND, "REALME", "Realme", null, 70, false),
            template(BusinessCatalogType.BRAND, "HONOR", "Honor", null, 80, false),
            template(BusinessCatalogType.BRAND, "LENOVO", "Lenovo", null, 90, false),
            template(BusinessCatalogType.BRAND, "HP", "HP", null, 100, false),
            template(BusinessCatalogType.BRAND, "DELL", "Dell", null, 110, false),
            template(BusinessCatalogType.BRAND, "ASUS", "Asus", null, 120, false),
            template(BusinessCatalogType.BRAND, "ACER", "Acer", null, 130, false),
            template(BusinessCatalogType.BRAND, "SONY", "Sony", null, 140, false),
            template(BusinessCatalogType.BRAND, "MICROSOFT", "Microsoft", null, 150, false),
            template(BusinessCatalogType.BRAND, "NINTENDO", "Nintendo", null, 160, false),
            template(BusinessCatalogType.BRAND, "OTHER", "Otra marca", null, 170, false),

            template(BusinessCatalogType.APPOINTMENT_STATUS, "SCHEDULED", "Agendada", "Cita registrada y pendiente de confirmacion.", 10, true),
            template(BusinessCatalogType.APPOINTMENT_STATUS, "CONFIRMED", "Confirmada", "Cita confirmada por el negocio.", 20, true),
            template(BusinessCatalogType.APPOINTMENT_STATUS, "COMPLETED", "Completada", "La atencion programada fue completada.", 30, true),
            template(BusinessCatalogType.APPOINTMENT_STATUS, "NO_SHOW", "No asistio", "El cliente no asistio a la cita.", 40, true),
            template(BusinessCatalogType.APPOINTMENT_STATUS, "CANCELLED", "Cancelada", "La cita fue cancelada.", 50, true),

            template(BusinessCatalogType.REPAIR_ORDER_STATUS, "RECEIVED", "Recibida", "Equipo recibido por el negocio.", 10, true),
            template(BusinessCatalogType.REPAIR_ORDER_STATUS, "DIAGNOSIS", "En diagnostico", "Equipo en proceso de diagnostico.", 20, true),
            template(BusinessCatalogType.REPAIR_ORDER_STATUS, "WAITING_APPROVAL", "Esperando aprobacion", "Diagnostico o cotizacion pendiente de aprobacion.", 30, true),
            template(BusinessCatalogType.REPAIR_ORDER_STATUS, "WAITING_PARTS", "Esperando repuestos", "Reparacion en espera de repuestos.", 40, true),
            template(BusinessCatalogType.REPAIR_ORDER_STATUS, "REPAIRING", "En reparacion", "Equipo en proceso de reparacion.", 50, true),
            template(BusinessCatalogType.REPAIR_ORDER_STATUS, "TESTING", "En pruebas", "Equipo reparado en proceso de pruebas.", 60, true),
            template(BusinessCatalogType.REPAIR_ORDER_STATUS, "READY_FOR_PICKUP", "Listo para entrega", "Equipo listo para ser recogido.", 70, true),
            template(BusinessCatalogType.REPAIR_ORDER_STATUS, "DELIVERED", "Entregada", "Equipo entregado al cliente.", 80, true),
            template(BusinessCatalogType.REPAIR_ORDER_STATUS, "CANCELLED", "Cancelada", "Orden cancelada.", 90, true),

            template(BusinessCatalogType.QUOTATION_STATUS, "DRAFT", "Borrador", "Cotizacion en preparacion.", 10, true),
            template(BusinessCatalogType.QUOTATION_STATUS, "PENDING", "Pendiente", "Cotizacion enviada y pendiente de respuesta.", 20, true),
            template(BusinessCatalogType.QUOTATION_STATUS, "APPROVED", "Aprobada", "Cotizacion aprobada por el cliente.", 30, true),
            template(BusinessCatalogType.QUOTATION_STATUS, "REJECTED", "Rechazada", "Cotizacion rechazada por el cliente.", 40, true),
            template(BusinessCatalogType.QUOTATION_STATUS, "EXPIRED", "Vencida", "Cotizacion vencida.", 50, true),
            template(BusinessCatalogType.QUOTATION_STATUS, "CANCELLED", "Cancelada", "Cotizacion cancelada.", 60, true),

            template(BusinessCatalogType.ITEM_CATEGORY, "ACCESSORY", "Accesorio", "Accesorios disponibles para venta.", 10, false),
            template(BusinessCatalogType.ITEM_CATEGORY, "SPARE_PART", "Repuesto", "Repuestos utilizados en reparaciones.", 20, false),
            template(BusinessCatalogType.ITEM_CATEGORY, "SUPPLY", "Insumo", "Insumos consumibles del negocio.", 30, false),
            template(BusinessCatalogType.ITEM_CATEGORY, "TOOL", "Herramienta", "Herramientas de trabajo.", 40, false),

            template(BusinessCatalogType.MOVEMENT_TYPE, "IN", "Entrada", "Ingreso de unidades al inventario.", 10, true),
            template(BusinessCatalogType.MOVEMENT_TYPE, "OUT", "Salida", "Salida de unidades del inventario.", 20, true),
            template(BusinessCatalogType.MOVEMENT_TYPE, "ADJUSTMENT", "Ajuste", "Ajuste positivo de inventario.", 30, true),
            template(BusinessCatalogType.MOVEMENT_TYPE, "RETURN", "Devolucion", "Devolucion de unidades al inventario.", 40, true),

            template(BusinessCatalogType.PAYMENT_METHOD, "CASH", "Efectivo", null, 10, false),
            template(BusinessCatalogType.PAYMENT_METHOD, "CARD", "Tarjeta", null, 20, false),
            template(BusinessCatalogType.PAYMENT_METHOD, "TRANSFER", "Transferencia", null, 30, false),
            template(BusinessCatalogType.PAYMENT_METHOD, "NEQUI", "Nequi", null, 40, false),
            template(BusinessCatalogType.PAYMENT_METHOD, "DAVIPLATA", "Daviplata", null, 50, false),
            template(BusinessCatalogType.PAYMENT_METHOD, "PSE", "PSE", null, 60, false));

    private final BusinessCatalogItemRepository businessCatalogItemRepository;

    public BusinessCatalogInitializerService(BusinessCatalogItemRepository businessCatalogItemRepository) {
        this.businessCatalogItemRepository = businessCatalogItemRepository;
    }

    @Transactional
    public void initialize(Business business) {
        Set<CatalogKey> existing = new HashSet<>();
        businessCatalogItemRepository.findByBusinessIdAndDeletedAtIsNull(business.getId())
                .forEach(item -> existing.add(new CatalogKey(item.getCatalogType(), item.getCode())));

        List<BusinessCatalogItem> missing = DEFAULT_CATALOGS.stream()
                .filter(template -> existing.add(new CatalogKey(template.type(), template.code())))
                .map(template -> toEntity(business, template))
                .toList();

        if (!missing.isEmpty()) {
            businessCatalogItemRepository.saveAll(missing);
        }
    }

    private BusinessCatalogItem toEntity(Business business, CatalogTemplate template) {
        BusinessCatalogItem item = new BusinessCatalogItem();
        item.setBusiness(business);
        item.setCatalogType(template.type());
        item.setCode(template.code());
        item.setName(template.name());
        item.setDescription(template.description());
        item.setSortOrder(template.sortOrder());
        item.setProtectedItem(template.protectedItem());
        item.setActive(true);
        item.setDeletedAt(null);
        return item;
    }

    private static CatalogTemplate template(
            BusinessCatalogType type,
            String code,
            String name,
            String description,
            Integer sortOrder,
            boolean protectedItem) {
        return new CatalogTemplate(type, code, name, description, sortOrder, protectedItem);
    }

    private record CatalogKey(BusinessCatalogType type, String code) {
    }

    private record CatalogTemplate(
            BusinessCatalogType type,
            String code,
            String name,
            String description,
            Integer sortOrder,
            boolean protectedItem) {
    }
}
