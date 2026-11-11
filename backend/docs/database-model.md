# Contexto de Base de Datos para Codex

Este documento debe guardarse como `docs/database-model.md`.

El esquema completo exportado desde DrawDB debe guardarse como:

- `docs/database-schema.drawdb.json`

Codex debe revisar este documento y el JSON de DrawDB antes de crear entidades JPA, migraciones Flyway, DTOs, mappers, repositories o services relacionados con base de datos.

---

## 1. Resumen del Proyecto

TecMarket es una plataforma multitenant para servicios técnicos.

El sistema combina:

1. Marketplace público de servicios técnicos.
2. Panel interno para que cada empresa gestione su operación.

Funcionalidades principales:

- Registro de usuarios.
- Solicitud de creación de empresas.
- Administración de negocios.
- Usuarios internos por negocio.
- Citas.
- Dispositivos de clientes.
- Órdenes de reparación.
- Evidencias fotográficas.
- Pruebas de ingreso configurables por negocio.
- Cotizaciones.
- Inventario.
- Ventas directas de accesorios/productos.
- Movimientos de stock.
- Reseñas.
- Permisos por rol.

---

## 2. Multitenancy

El sistema es multitenant mediante `business_id`.

Toda tabla que tenga `business_id` pertenece a una empresa/tenant.

Tablas tenant importantes:

- `business_user`
- `customer_device`
- `business_service`
- `business_schedule`
- `business_quality_test`
- `appointment`
- `repair_order`
- `quotation`
- `inventory_item`
- `sale`

Regla principal:

Un usuario de una empresa no debe poder ver, modificar o eliminar información de otra empresa.

Toda consulta sobre tablas con `business_id` debe filtrar por el negocio correspondiente.

Ejemplo correcto:

    findByIdAndBusinessId(Long id, Long businessId)

Ejemplo a evitar en entidades tenant:

    findById(Long id)

A menos que el contexto de empresa ya se haya validado previamente.

---

## 3. Convenciones

### Base de datos

- Tablas en `snake_case`.
- Columnas en `snake_case`.
- Llaves primarias llamadas `id`.
- Llaves foráneas con formato `<entidad>_id`.

Ejemplos:

- `business_id`
- `customer_id`
- `repair_order_id`
- `inventory_item_id`
- `payment_method_id`

### Java

- Entidades en `PascalCase`.
- Atributos en `camelCase`.
- Usar `@Table(name = "...")`.
- Usar `@Column(name = "...")`.

Ejemplo:

    @Entity
    @Table(name = "repair_order")
    public class RepairOrder {

        @Column(name = "order_number")
        private String orderNumber;
    }

---

## 4. Tipos Java Recomendados

- `INTEGER` -> `Long` para IDs y llaves foráneas.
- `VARCHAR` -> `String`.
- `TEXT` -> `String`.
- `BOOLEAN` -> `Boolean`.
- `DECIMAL` -> `BigDecimal`.
- `TIMESTAMP` -> `LocalDateTime`.
- `TIME` -> `LocalTime`.

No usar `Double` ni `Float` para dinero.

Usar siempre `BigDecimal` para:

- `base_price`
- `estimated_price`
- `final_price`
- `total_amount`
- `unit_cost`
- `sale_price`
- `unit_price`
- `total_cost`
- `total_price`

---

## 5. Campos Comunes

La mayoría de tablas de negocio incluyen:

- `active`
- `created_at`
- `updated_at`
- `deleted_at`

El sistema usa eliminación lógica.

Para eliminar normalmente:

- `active = false`
- `deleted_at = now()`

No borrar físicamente registros salvo confirmación explícita.

---

## 6. Módulo User

Tablas:

- `app_user`
- `document_type`
- `role`
- `user_role`

### app_user

Usuario global de la plataforma.

Puede ser cliente, administrador de plataforma o usuario interno de una empresa.

Campos principales:

- `id`
- `document_number`
- `document_type_id`
- `name`
- `surname`
- `email`
- `password`
- `security_version`
- `address`
- `phone`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Restricciones:

- `email` es único.
- `document_number + document_type_id` es único.

Regla:

El usuario es global. No duplicar usuarios por empresa.
`security_version` inicia en `0` y aumenta cuando cambia la identidad de seguridad
del usuario. Los JWT y refresh tokens emitidos con una versión anterior se rechazan.

### document_type

Catálogo de tipos de documento.

Campos:

- `id`
- `code`
- `name`
- `description`
- `applies_to_person`
- `applies_to_company`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Ejemplos:

- `CC`
- `CE`
- `NIT`
- `PASSPORT`

### role

Catálogo de roles.

Campos:

- `id`
- `code`
- `name`
- `description`
- `scope`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Ejemplos:

- `CUSTOMER`
- `PLATFORM_ADMIN`
- `BUSINESS_OWNER`
- `BUSINESS_ADMIN`
- `TECHNICIAN`
- `RECEPTIONIST`
- `SELLER`

`scope` puede ser:

- `GLOBAL`
- `BUSINESS`

### user_role

Relación usuario-rol global.

Campos:

- `id`
- `user_id`
- `role_id`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Restricción:

- `user_id + role_id` único.

---

## 7. Módulo Business

Tablas:

- `business`
- `business_request`
- `business_user`
- `business_schedule`
- `business_service`
- `service_category`
- `country`
- `city`

### business

Representa una empresa o negocio de servicio técnico.

Es el tenant principal.

Campos:

- `id`
- `document_type_id`
- `document_number`
- `name`
- `legal_name`
- `email`
- `phone`
- `address`
- `description`
- `city_id`
- `logo`
- `logo_bucket_name`
- `logo_object_key`
- `cover_image`
- `cover_image_bucket_name`
- `cover_image_object_key`
- `rating_average`
- `rating_count`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Restricción:

- `document_type_id + document_number` único.

### business_request

Solicitud para crear una empresa.

Campos:

- `id`
- `requested_by_user_id`
- `document_type_id`
- `document_number`
- `business_name`
- `legal_name`
- `email`
- `phone`
- `city_id`
- `address`
- `legal_file`
- `status`
- `reviewed_by_user_id`
- `reviewed_at`
- `rejection_reason`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Estados sugeridos:

- `PENDING`
- `APPROVED`
- `REJECTED`

### business_user

Relaciona un usuario global con una empresa y un rol dentro de esa empresa.

Campos:

- `id`
- `business_id`
- `user_id`
- `role_id`
- `commission_percentage`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Restricción:

- `user_id + role_id + business_id` único.

Regla:

Un usuario puede pertenecer a varias empresas.

`commission_percentage` representa el porcentaje de comisión del técnico dentro
de ese negocio. Solo aplica al rol `TECHNICIAN`; para los demás roles se conserva
en `0.00`.

### business_schedule

Horarios de atención de la empresa.

Campos:

- `id`
- `business_id`
- `day_of_week`
- `open_time`
- `close_time`
- `slot_order`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Permite varias jornadas el mismo día usando `slot_order`.

Ejemplo:

- Lunes 08:00 a 12:00
- Lunes 14:00 a 18:00

### service_category

Catálogo global de categorías de servicio.

Campos:

- `id`
- `code`
- `name`
- `description`
- `icon`
- `sort_order`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Ejemplos:

- `SCREEN_REPAIR`
- `BATTERY_CHANGE`
- `DIAGNOSTIC`
- `WATER_DAMAGE`
- `SOFTWARE_REPAIR`

### business_service

Servicios ofrecidos por una empresa.

Campos:

- `id`
- `business_id`
- `service_category_id`
- `device_type_id`
- `name`
- `description`
- `base_price`
- `estimated_time_minutes`
- `image_url`
- `image_bucket_name`
- `image_object_key`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Regla:

`business_service` pertenece a una empresa.

### country

Catálogo de países.

Campos:

- `id`
- `code`
- `name`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

### city

Catálogo de ciudades.

Campos:

- `id`
- `country_id`
- `name`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Restricción:

- `country_id + name` único.

---

## 8. Módulo Device

Tablas:

- `customer_device`
- `device_type`
- `brand`

Este módulo puede estar inicialmente dentro de `business` o separarse luego.

### device_type

Catálogo de tipos de dispositivo.

Campos:

- `id`
- `name`
- `code`
- `description`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Ejemplos:

- `PHONE`
- `TABLET`
- `LAPTOP`
- `DESKTOP`
- `CONSOLE`
- `SMARTWATCH`

### brand

Catálogo global de marcas.

Campos:

- `id`
- `name`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

### customer_device

Dispositivo de un cliente registrado dentro de una empresa.

Campos:

- `id`
- `business_id`
- `customer_id`
- `device_type_id`
- `brand_id`
- `model`
- `serial_number`
- `imei`
- `registered_by_business_user_id`
- `color`
- `registered_at`
- `created_at`
- `updated_at`
- `deleted_at`

Restricciones:

- `business_id + imei` único para dispositivos activos cuando el IMEI no está vacío.
- `id + business_id + customer_id` único.

Reglas críticas:

- `customer_device` pertenece a una empresa.
- Un cliente puede tener el mismo dispositivo registrado en diferentes empresas.
- Un IMEI activo no puede quedar asociado a dos clientes distintos dentro de la misma empresa.
- `appointment.business_id` debe coincidir con `customer_device.business_id`.
- `appointment.customer_id` debe coincidir con `customer_device.customer_id`.
- `repair_order.business_id` debe coincidir con `customer_device.business_id`.
- `repair_order.customer_id` debe coincidir con `customer_device.customer_id`.

---

## 9. Módulo Appointment

Tablas:

- `appointment`
- `appointment_status`

### appointment_status

Catálogo de estados de cita.

Campos:

- `id`
- `code`
- `name`
- `description`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Estados sugeridos:

- `SCHEDULED`
- `CONFIRMED`
- `CANCELLED`
- `COMPLETED`
- `NO_SHOW`

### appointment

Cita agendada por cliente o asesor.

Campos:

- `id`
- `business_id`
- `customer_id`
- `customer_device_id`
- `business_service_id`
- `status_id`
- `scheduled_at`
- `problem_description`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Reglas:

- La cita pertenece a una empresa.
- El dispositivo debe pertenecer al mismo negocio y cliente.
- El servicio debe pertenecer al mismo negocio.

---

## 10. Módulo Repair Order

Tablas:

- `repair_order`
- `repair_order_status`
- `repair_order_history`
- `repair_order_attachment`
- `business_quality_test`
- `repair_order_quality_check`
- `repair_order_part`
- `commission_settlement`
- `repair_order_commission`

### repair_order

Entidad operativa central.

Campos:

- `id`
- `order_number`
- `business_id`
- `customer_id`
- `customer_device_id`
- `appointment_id`
- `business_service_id`
- `status_id`
- `received_by_business_user_id`
- `assigned_business_user_id`
- `problem_description`
- `diagnosis`
- `estimated_price`
- `final_price`
- `received_at`
- `estimated_delivery_at`
- `delivered_at`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Restricción:

- `business_id + order_number` único.

Reglas:

- El número de orden es único por empresa.
- Toda orden pertenece a una empresa.
- Toda orden pertenece a un cliente.
- Toda orden pertenece a un dispositivo del cliente.
- Toda orden tiene usuario que recibe.
- Toda orden tiene usuario responsable asignado.

### repair_order_status

Catálogo de estados de orden.

Campos:

- `id`
- `code`
- `name`
- `description`
- `sort_order`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Estados operativos del piloto:

- `RECEIVED`
- `DIAGNOSIS`
- `WAITING_APPROVAL`
- `WAITING_PARTS`
- `REPAIRING`
- `TESTING`
- `READY_FOR_PICKUP`
- `DELIVERED`
- `CANCELLED`

### repair_order_history

Historial o línea de tiempo de una orden.

Campos:

- `id`
- `repair_order_id`
- `status_id`
- `created_by_user_id`
- `comment`
- `visibility`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

La tabla es append-only desde V48: PostgreSQL rechaza cualquier `UPDATE` o `DELETE`.
Correcciones o novedades se registran como un nuevo evento, conservando actor, estado y fecha.

Valores sugeridos para `visibility`:

- `INTERNAL`
- `CUSTOMER`

### repair_order_attachment

Evidencias fotográficas.

Campos:

- `id`
- `repair_order_id`
- `uploaded_by_business_user_id`
- `file_url`
- `bucket_name`
- `object_key`
- `original_file_name`
- `content_type`
- `file_size`
- `checksum_sha256`
- `description`
- `deleted_by_business_user_id`
- `retention_until`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Regla:

No guardar imágenes directamente en PostgreSQL.

Guardar en storage externo privado y en base de datos solo metadatos:

- bucket y llave interna aleatoria
- nombre original saneado, MIME normalizado y tamaño
- checksum SHA-256 de la imagen reencodificada
- `description`
- usuario que subió la evidencia
- usuario que la retiró y fecha mínima de retención
- fecha de creación

La URL de lectura no se persiste: se firma por pocos minutos al consultar. La baja es
lógica y la eliminación física se procesa después del commit mediante
`storage_object_deletion_task`. Un trigger impide modificar el contenido y los
metadatos originales de una evidencia; solo permite su baja auditada y retención.

### storage_object_deletion_task

Outbox durable para eliminar objetos de MinIO sin acoplar la operación irreversible a
la transacción de negocio.

Campos:

- `id`
- `bucket_name`
- `object_key`
- `reason`
- `source_type`
- `source_id`
- `scheduled_at`
- `status` (`PENDING`, `COMPLETED`, `FAILED`)
- `attempt_count`
- `last_error`
- `created_at`
- `updated_at`
- `completed_at`

Las evidencias se programan al finalizar su retención. Los recursos públicos
reemplazados se programan inmediatamente, pero el worker solo puede ver la tarea una
vez confirmada la transacción.

### business_quality_test

Pruebas configurables por empresa y tipo de dispositivo.

Campos:

- `id`
- `business_id`
- `device_type_id`
- `code`
- `name`
- `description`
- `required`
- `sort_order`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Restricción:

- `business_id + device_type_id + code` único.

Ejemplos:

- Pantalla funciona.
- Micrófono funciona.
- Altavoz funciona.
- Cámaras funcionan.
- Puerto de carga funciona.
- Equipo tiene humedad.

### repair_order_quality_check

Resultado de las pruebas realizadas en una orden.

Campos:

- `id`
- `repair_order_id`
- `business_quality_test_id`
- `checked_by_user_id`
- `result`
- `notes`
- `active`
- `created_at`
- `updated_at`
- `checked_at`

Valores sugeridos para `result`:

- `OK`
- `FAILED`
- `NOT_APPLICABLE`
- `NOT_TESTED`

### repair_order_part

Repuestos usados en una reparación.

Campos:

- `id`
- `repair_order_id`
- `inventory_item_id`
- `quantity`
- `unit_price`
- `total_price`
- `used_by_business_user_id`
- `idempotency_key`
- `idempotency_fingerprint`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Regla:

- `repair_order_part` se usa para repuestos dentro de una reparación.
- `sale_item` se usa para ventas directas de accesorios o productos.

Cuando se agrega un repuesto a una orden, también debe generarse un `stock_movement` de tipo `OUT`.
El alta acepta `Idempotency-Key`; la combinación con `repair_order_id` es única y un
reintento equivalente no vuelve a descontar inventario.

### commission_settlement

Cabecera de una liquidación agrupada para un técnico. El administrador selecciona
las órdenes que desea liquidar dentro de un periodo y la liquidación avanza como
unidad.

Campos:

- `id`
- `settlement_code`
- `business_id`
- `business_user_id`
- `period_start`
- `period_end`
- `orders_count`
- `total_amount`
- `status`
- `notes`
- `generated_at`
- `approved_at`
- `paid_at`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Estados:

- `DRAFT`
- `APPROVED`
- `PAID`

### repair_order_commission

Detalle histórico de la comisión de cada orden incluida en una liquidación.

Campos:

- `id`
- `business_id`
- `repair_order_id`
- `business_user_id`
- `settlement_id`
- `order_total`
- `parts_total`
- `commission_base`
- `commission_percentage`
- `commission_amount`
- `status`
- `generated_at`
- `approved_at`
- `paid_at`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Reglas:

- Existe máximo una comisión por orden de reparación.
- El usuario asignado a la orden debe tener rol `TECHNICIAN`.
- La comisión se genera cuando el administrador selecciona la orden al crear una
  liquidación, no por el estado de la orden.
- Una orden es liquidable cuando tiene técnico asignado, precio final y no
  pertenece a otra liquidación.
- `commission_base = order_total - parts_total`.
- `commission_amount = commission_base * commission_percentage / 100`.
- `parts_total` corresponde a la suma cobrada en `repair_order_part.total_price`.
- Los valores se almacenan como una fotografía histórica y no cambian si luego
  se modifica el porcentaje del técnico.
- El estado del detalle acompaña el estado de su liquidación: `PENDING`,
  `APPROVED` o `PAID`.

---

## 11. Módulo Quotation

Tablas:

- `quotation`
- `quotation_status`
- `quotation_item`

### quotation_status

Catálogo de estados de cotización.

Estados sugeridos:

- `PENDING`
- `APPROVED`
- `REJECTED`
- `EXPIRED`
- `CANCELLED`

### quotation

Cotización asociada a una orden.

Campos:

- `id`
- `repair_order_id`
- `business_id`
- `customer_id`
- `total_amount`
- `quotation_status_id`
- `notes`
- `expires_at`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

### quotation_item

Detalle de cotización.

Campos:

- `id`
- `quotation_id`
- `description`
- `quantity`
- `unit_price`
- `total_price`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

---

## 12. Módulo Inventory

Tablas:

- `inventory_item`
- `item_category`
- `stock_movement`
- `movement_type`

### item_category

Catálogo de categorías de inventario.

Ejemplos:

- `ACCESSORY`
- `SPARE_PART`
- `SUPPLY`
- `TOOL`

### inventory_item

Producto, repuesto, accesorio o insumo dentro del inventario de una empresa.

Campos:

- `id`
- `business_id`
- `name`
- `item_category_id`
- `description`
- `sku`
- `quantity`
- `minimum_stock`
- `unit_cost`
- `sale_price`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Restricción:

- `business_id + sku` único.

Regla:

El SKU es interno de cada empresa.

Dos empresas pueden usar el mismo SKU.

Una misma empresa no puede repetir el mismo SKU.

### movement_type

Catálogo de tipos de movimiento.

Ejemplos:

- `IN`
- `OUT`
- `ADJUSTMENT`
- `RETURN`

### stock_movement

Movimiento de inventario.

Campos:

- `id`
- `inventory_item_id`
- `repair_order_id`
- `business_user_id`
- `movement_type_id`
- `sale_item_id`
- `quantity`
- `notes`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Reglas:

Un movimiento puede venir de:

- entrada de inventario
- salida por reparación
- salida por venta directa
- ajuste manual
- devolución

`repair_order_id` es opcional.

`sale_item_id` es opcional.

No se deben exigir ambos al mismo tiempo.

---

## 13. Módulo Sale

Tablas:

- `sale`
- `sale_item`
- `payment_method`

### payment_method

Catálogo de métodos de pago.

Ejemplos:

- `CASH`
- `CARD`
- `TRANSFER`
- `NEQUI`
- `DAVIPLATA`
- `PSE`

### sale

Venta directa de accesorios, productos o repuestos fuera de una orden.

Campos:

- `id`
- `business_id`
- `customer_id`
- `sold_by_business_user_id`
- `total_amount`
- `payment_method_id`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Reglas:

- La venta pertenece a una empresa.
- `customer_id` es opcional.
- Un cliente puede comprar sin estar registrado.
- `sold_by_business_user_id` identifica quién realizó la venta.

### sale_item

Detalle de productos vendidos.

Campos:

- `id`
- `sale_id`
- `inventory_item_id`
- `quantity`
- `unit_cost`
- `unit_price`
- `total_cost`
- `total_price`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Reglas:

Se guardan costos y precios históricos.

La ganancia no se guarda como campo.

La ganancia se calcula así:

    total_price - total_cost

Al crear un `sale_item`, también debe crearse un `stock_movement` de tipo `OUT`.

---

## 14. Módulo Review

Tablas:

- `business_review`

### business_review

Reseña de cliente a empresa después de una reparación.

Campos:

- `id`
- `business_id`
- `customer_id`
- `repair_order_id`
- `rating`
- `comment`
- `active`
- `created_at`
- `updated_at`
- `deleted_at`

Restricción:

- `repair_order_id` único.

Regla:

Una orden solo puede tener una reseña.

---

## 15. Módulo Permission

Tablas:

- `app_module`
- `permission`
- `role_permission`
- `role`
- `business_user`

### app_module

Catálogo de módulos del sistema.

Ejemplos:

- `REPAIR_ORDER`
- `INVENTORY`
- `SALE`
- `QUOTATION`
- `APPOINTMENT`
- `BUSINESS`
- `USER_MANAGEMENT`
- `REPORT`

### permission

Catálogo de permisos.

Ejemplos:

- `REPAIR_ORDER_CREATE`
- `REPAIR_ORDER_VIEW`
- `REPAIR_ORDER_UPDATE`
- `REPAIR_ORDER_CLOSE`
- `REPAIR_ORDER_UPLOAD_EVIDENCE`
- `INVENTORY_VIEW`
- `INVENTORY_CREATE`
- `INVENTORY_UPDATE`
- `INVENTORY_DELETE`
- `SALE_CREATE`
- `SALE_VIEW`
- `QUOTATION_CREATE`
- `QUOTATION_VIEW`
- `REPORT_VIEW`

### role_permission

Relaciona roles con permisos.

Restricción:

- `role_id + permission_id` único.

Regla:

En V1 los permisos se asignan por rol.

No implementar permisos personalizados por usuario en V1.

La tabla `business_user_permission` queda para V2.

---

## 16. Reglas de Implementación Backend

### Entidades JPA

Al crear entidades JPA:

- No usar Lombok `@Data`.
- Usar `@Getter`.
- Usar `@Setter`.
- Usar `@NoArgsConstructor`.
- Usar `@AllArgsConstructor` si aplica.
- Usar `@Builder` solo cuando sea útil.
- Usar `@Entity`.
- Usar `@Table(name = "...")`.
- Usar `@Column(name = "...")`.

Ejemplo:

    @Entity
    @Table(name = "inventory_item")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class InventoryItem {
    }

### DTOs

No exponer entidades directamente en la API.

Usar DTOs para entrada y salida.

Ejemplos:

- `CreateInventoryItemRequest`
- `UpdateInventoryItemRequest`
- `InventoryItemResponse`

### Mappers

Usar MapStruct para convertir entre entidades y DTOs.

Ejemplo:

    @Mapper(componentModel = "spring")
    public interface InventoryItemMapper {
    }

### Services

La lógica de negocio debe estar en servicios.

Los controladores no deben contener lógica de negocio.

Los controladores solo deben:

- recibir request
- validar entrada básica
- llamar servicio
- devolver response

### Repositories

Los repositorios deben extender `JpaRepository`.

Para entidades tenant, preferir métodos que incluyan `businessId`.

Ejemplos:

    Optional<InventoryItem> findByIdAndBusinessId(Long id, Long businessId);

    List<InventoryItem> findByBusinessIdAndActiveTrue(Long businessId);

---

## 17. Reglas Críticas que Codex Debe Respetar

1. No renombrar tablas sin confirmación.
2. No renombrar columnas sin confirmación.
3. No eliminar columnas sin confirmación.
4. No cambiar relaciones entre tablas sin confirmación.
5. No quitar `business_id` de tablas tenant.
6. No crear consultas tenant sin filtrar por `business_id`.
7. No usar `Double` ni `Float` para dinero.
8. Usar `BigDecimal` para dinero.
9. No exponer entidades directamente en respuestas REST.
10. No implementar `business_user_permission` en V1.
11. No guardar imágenes directamente en PostgreSQL.
12. No guardar ganancia como campo si puede calcularse.
13. No borrar registros físicamente si la tabla tiene `active` y `deleted_at`.
14. No mezclar ventas directas con repuestos usados en reparación.
15. No usar `sale_item` para repuestos de una reparación.
16. No usar `repair_order_part` para ventas directas.

---

## 17.1 Tablas No Documentadas Previamente

### department

Departamentos/estados dentro de un país.

| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | serial | PK |
| country_id | integer | FK → country(id), NOT NULL |
| name | varchar(100) | NOT NULL |
| active | boolean | NOT NULL, DEFAULT TRUE |
| created_at | timestamp | NOT NULL, DEFAULT NOW() |
| updated_at | timestamp | |
| deleted_at | timestamp | |

Índice único: `(country_id, name)` donde `deleted_at IS NULL`.

---

### business_branch

Sucursales de un negocio.

| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | serial | PK |
| business_id | integer | FK → business(id), NOT NULL |
| branch_number | integer | NOT NULL |
| name | varchar(100) | NOT NULL |
| address | varchar(255) | |
| city_id | integer | FK → city(id) |
| phone | varchar(20) | |
| active | boolean | NOT NULL, DEFAULT TRUE |
| created_at | timestamp | NOT NULL, DEFAULT NOW() |
| updated_at | timestamp | |
| deleted_at | timestamp | |

Índice único: `(business_id, branch_number)` donde `deleted_at IS NULL`.

---

### business_catalog_item

Catálogos configurables por negocio (tipos de dispositivo, marcas, estados de reparación, categorías de inventario, métodos de pago, tipos de movimiento, estados de cotización, estados de cita).

| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | serial | PK |
| business_id | integer | FK → business(id), NOT NULL |
| catalog_type | varchar(50) | NOT NULL (enum: DEVICE_TYPE, BRAND, REPAIR_ORDER_STATUS, etc.) |
| code | varchar(50) | NOT NULL |
| name | varchar(100) | NOT NULL |
| description | text | |
| sort_order | integer | DEFAULT 0 |
| active | boolean | NOT NULL, DEFAULT TRUE |
| created_at | timestamp | NOT NULL, DEFAULT NOW() |
| updated_at | timestamp | |
| deleted_at | timestamp | |

Índice único: `(business_id, catalog_type, code)` donde `deleted_at IS NULL`.

Al crear o aprobar un negocio se inicializan automaticamente los catalogos operativos base. La operacion es idempotente: solo inserta codigos faltantes y conserva las personalizaciones existentes. Los estados de citas, ordenes y cotizaciones, junto con los tipos de movimiento, se crean como elementos protegidos.

---

### business_user_branch

Asignación de usuarios de negocio a sucursales.

| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | serial | PK |
| business_user_id | integer | FK → business_user(id), NOT NULL |
| branch_id | integer | FK → business_branch(id), NOT NULL |
| active | boolean | NOT NULL, DEFAULT TRUE |
| created_at | timestamp | NOT NULL, DEFAULT NOW() |
| updated_at | timestamp | |
| deleted_at | timestamp | |

Índice único: `(business_user_id, branch_id)` donde `deleted_at IS NULL`.

---

### repair_order_commission

Comisiones calculadas por orden de reparación para técnicos.

| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | serial | PK |
| repair_order_id | integer | FK → repair_order(id), NOT NULL |
| business_id | integer | FK → business(id), NOT NULL |
| technician_business_user_id | integer | FK → business_user(id), NOT NULL |
| commission_percentage | decimal(5,2) | NOT NULL |
| base_amount | decimal(12,2) | NOT NULL |
| commission_amount | decimal(12,2) | NOT NULL |
| status | varchar(20) | NOT NULL (PENDING, SETTLED, CANCELLED) |
| active | boolean | NOT NULL, DEFAULT TRUE |
| created_at | timestamp | NOT NULL, DEFAULT NOW() |
| updated_at | timestamp | |
| deleted_at | timestamp | |

---

### commission_settlement

Liquidaciones de comisiones agrupadas por técnico.

| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | serial | PK |
| business_id | integer | FK → business(id), NOT NULL |
| branch_id | integer | FK → business_branch(id), NOT NULL |
| technician_business_user_id | integer | FK → business_user(id), NOT NULL |
| settled_by_user_id | integer | FK → app_user(id), NOT NULL |
| total_amount | decimal(12,2) | NOT NULL |
| commission_count | integer | NOT NULL |
| notes | text | |
| status | varchar(20) | NOT NULL (PENDING, COMPLETED, CANCELLED) |
| active | boolean | NOT NULL, DEFAULT TRUE |
| created_at | timestamp | NOT NULL, DEFAULT NOW() |
| updated_at | timestamp | |
| deleted_at | timestamp | |

---

### repair_order_sequence

Secuencia numérica por sucursal para generar números de orden.

| Columna | Tipo | Restricciones |
|---------|------|---------------|
| business_id | integer | NOT NULL |
| branch_id | integer | NOT NULL |
| next_sequence | integer | NOT NULL, DEFAULT 1 |
| created_at | timestamp | NOT NULL, DEFAULT NOW() |
| updated_at | timestamp | |

Índice único: `(business_id, branch_id)`.

---

### subscription (tablas de suscripción)

Planes de suscripción para negocios.

| Tabla | Descripción |
|-------|-------------|
| `subscription_plan` | Planes disponibles (FREE, BASIC, PRO) |
| `business_subscription` | Suscripción activa de cada negocio |

---

### refresh_token

Tokens de refresco para autenticación JWT.

| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | serial | PK |
| user_id | integer | FK → app_user(id), NOT NULL |
| token_hash | varchar(64) | Hash SHA-256, NOT NULL, UNIQUE |
| family_id | uuid | Familia de rotacion, NOT NULL |
| expires_at | timestamp | NOT NULL |
| revoked | boolean | NOT NULL, DEFAULT FALSE |
| revoked_at | timestamp | |
| last_used_at | timestamp | |
| client_description | varchar(255) | Dispositivo o agente de cliente normalizado |
| ip_address | varchar(64) | IP resuelta después de validar el proxy confiable |
| replaced_by_token_hash | varchar(64) | Hash del token que reemplazo al actual |
| issued_security_version | integer | Versión de seguridad del usuario al emitirlo, NOT NULL |
| created_at | timestamp | NOT NULL, DEFAULT NOW() |

El token opaco solo se entrega al cliente. La base de datos almacena su hash. Cada uso rota el token dentro de la misma familia y la reutilizacion de un token revocado invalida toda la familia. La rotación también compara `issued_security_version` con `app_user.security_version` para impedir que una carrera concurrente reactive una sesión revocada.

---

### password_reset_token

Tokens de recuperación de contraseña de un solo uso.

| Columna | Tipo | Restricciones |
|---------|------|---------------|
| id | serial | PK |
| user_id | integer | FK → app_user(id), NOT NULL |
| token_hash | varchar(64) | Hash SHA-256, NOT NULL, UNIQUE |
| expires_at | timestamp | NOT NULL |
| used_at | timestamp | |
| created_at | timestamp | NOT NULL, DEFAULT NOW() |

La respuesta de solicitud de recuperación es siempre genérica. Al restablecer la
contraseña se consume el token y se revocan todas las familias de refresh del usuario.

---

## 18. Endurecimiento del contrato MVP (V39-V49)

- `permission.scope` es obligatorio y solo admite `GLOBAL` o `BUSINESS`. Los roles
  internos no pueden recibir permisos globales y los servicios de plataforma vuelven
  a comprobar que el actor sea administrador global.
- `repair_order`, `appointment`, `quotation`, `sale`, `inventory_item` y sus actores
  críticos usan claves foráneas compuestas para impedir asociaciones entre negocios o
  sucursales aun si una validación Java futura falla.
- Cantidades, costos, precios, totales y fechas operativas tienen restricciones `CHECK`.
- `sale`, `repair_order` y `stock_movement` incluyen `idempotency_key` e
  `idempotency_fingerprint`, con índices únicos parciales por tenant/sucursal.
- El precio final exige `REPAIR_ORDER_FINANCIAL_UPDATE`; entregar o cancelar exige
  `REPAIR_ORDER_CLOSE`.
- Las órdenes y cotizaciones usan máquinas de estados explícitas. Las mutaciones de
  orden/cotización y los lotes de inventario usan bloqueos pesimistas en orden estable.
- `repair_order_part` tiene idempotencia por orden y `repair_order_history` es
  inmutable en la base de datos. Diagnóstico, precio, asignación, cotización,
  evidencia, repuestos y entrega producen eventos de auditoría.
- Los IDs continúan siendo `INTEGER` durante el piloto para no introducir una migración
  masiva sin valor inmediato para el MVP.
- V49 completa FK compuestas para cotizaciones, reseñas, comisiones y movimientos
  vinculados a órdenes. También valida totales calculados de cotizaciones, ventas y
  repuestos, y agrega índices para los listados paginados del contrato operativo.
- Los listados operativos usan una envoltura común con `items`, `page`, `size`,
  `totalElements`, `totalPages`, `hasNext` y `hasPrevious`. El tamaño máximo es 100.
- `spring.jpa.open-in-view=false` se exige en los perfiles base, pruebas y producción;
  los DTO se construyen dentro de transacciones de solo lectura o escritura.
- V38 conserva las sesiones existentes mientras las transforma a hashes y familias
  de rotación. No contiene `TRUNCATE`; las migraciones posteriores tampoco truncan
  implícitamente tablas de autenticación u operación.

---

## 19. Archivos Relacionados

Este resumen debe mantenerse junto con:

- `docs/database-schema.drawdb.json`

Si el modelo cambia en DrawDB, se debe actualizar:

1. `docs/database-schema.drawdb.json`
2. `docs/database-model.md`
3. Migraciones Flyway correspondientes.
4. Entidades JPA relacionadas.
5. DTOs, mappers y servicios afectados.

La fuente ejecutable actual es Flyway hasta V49. `database-schema.drawdb.json` debe
regenerarse desde una base PostgreSQL migrada antes de congelar el contrato visual;
no debe usarse por sí solo para desplegar ni validar el esquema.
