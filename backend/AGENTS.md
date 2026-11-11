# AGENTS.md — Contexto del proyecto TecMarket para Codex / asistentes de código

> Coloca este archivo en la raíz del repositorio del backend. Su objetivo es darle a Codex, Copilot u otro asistente de código contexto suficiente para generar código consistente con la arquitectura, reglas de negocio, modelo de datos y decisiones técnicas del proyecto.

---

## 1. Identidad del proyecto

**Nombre provisional:** TecMarket  
**Tipo de producto:** Plataforma marketplace + sistema interno de gestión para empresas de servicio técnico.  
**Backend:** Java 21 + Spring Boot 3.5.x.  
**Base de datos:** PostgreSQL.  
**Arquitectura:** Monolito modular por módulos funcionales.  
**Cliente objetivo:** Empresas o negocios de servicio técnico que reparan celulares, tablets, computadores, MacBooks, consolas, smartwatches y equipos similares.

TecMarket combina dos mundos:

1. **Marketplace público** donde los clientes pueden encontrar negocios de servicio técnico, revisar reputación, servicios, ubicación y solicitar/agendar atención.
2. **Panel interno de cada empresa** donde cada negocio gestiona sus servicios, citas, órdenes de reparación, pruebas de ingreso, evidencias, cotizaciones, inventario, ventas, técnicos, permisos y reportes.

---

## 2. Problema que resuelve

Los negocios de servicio técnico suelen manejar procesos manuales, WhatsApp, Excel o registros en papel. Esto genera problemas como:

- Poca trazabilidad de órdenes.
- Clientes llamando constantemente para preguntar por el estado.
- Falta de evidencia fotográfica del estado del equipo.
- Pérdida de control sobre repuestos e inventario.
- Dificultad para separar ganancias por reparación, repuestos y accesorios.
- Poca confianza del cliente al elegir dónde reparar.
- Procesos internos desorganizados entre asesores y técnicos.

La plataforma debe permitir que:

- El cliente encuentre un negocio confiable.
- El negocio reciba y gestione órdenes.
- El cliente pueda consultar el estado de su reparación.
- El técnico pueda subir evidencias fotográficas.
- El negocio pueda crear cotizaciones.
- El negocio pueda manejar inventario, ventas y repuestos.
- La empresa tenga auditoría y trazabilidad interna.

---

## 3. Alcance funcional de V1

La V1 debe enfocarse en el flujo principal de operación.

### Incluido en V1

- Registro y login general de usuarios.
- Roles y permisos por rol.
- Solicitud de creación de empresa.
- Administración de negocios.
- Usuarios internos del negocio: dueño, asesor, técnico, vendedor, etc.
- Servicios ofrecidos por negocio.
- Horarios del negocio.
- Registro de cliente y dispositivo por negocio.
- Citas/agendamiento.
- Órdenes de reparación.
- Estados de reparación.
- Historial/novedades de la orden.
- Pruebas de ingreso configurables por negocio.
- Evidencias fotográficas por orden.
- Cotizaciones y detalle de cotización.
- Inventario de productos/repuestos/accesorios.
- Movimientos de stock.
- Repuestos usados en órdenes.
- Ventas directas de accesorios/productos.
- Métodos de pago para ventas directas.
- Reseñas del negocio después de una reparación.

### Fuera de V1 o V2+

- Permisos personalizados por usuario (`business_user_permission`).
- Facturación electrónica.
- Pasarela de pago real.
- Chat completo o asistente IA.
- WhatsApp automatizado avanzado.
- Planes/suscripciones de negocios.
- Auditoría técnica global avanzada.
- Microservicios.

---

## 4. Decisión de arquitectura

Usar **monolito modular por módulos funcionales**.

No usar microservicios en V1. El sistema tiene muchos módulos, pero todavía es mejor mantenerlos en un solo backend para reducir complejidad operativa.

### Estructura recomendada

```txt
src/main/java/com/tecmarket/backend
├── BackendApplication.java
│
├── auth
│   ├── controller
│   ├── service
│   ├── dto
│   └── security
│
├── user
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   └── mapper
│
├── business
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   └── mapper
│
├── appointment
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   └── mapper
│
├── repairorder
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   └── mapper
│
├── inventory
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   └── mapper
│
├── sale
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   └── mapper
│
├── quotation
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   └── mapper
│
├── review
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   └── mapper
│
├── permission
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   └── mapper
│
├── common
│   ├── exception
│   ├── response
│   ├── pagination
│   ├── validation
│   └── util
│
└── config
    ├── SecurityConfig.java
    ├── CorsConfig.java
    ├── SwaggerConfig.java
    └── PersistenceConfig.java
```

### Regla de módulos

- Cada módulo debe tener sus propios `controller`, `service`, `repository`, `entity`, `dto` y `mapper`.
- Evitar que un módulo use directamente repositories de otro módulo.
- Si un módulo necesita lógica de otro, debe usar un `Service` público del otro módulo.
- Ejemplo correcto:

```txt
repairorder.service → inventory.service
```

- Ejemplo a evitar:

```txt
repairorder.service → inventory.repository
```

---

## 5. Stack técnico definido

### Backend

- Java 21.
- Spring Boot 3.5.x.
- Maven.
- Spring Web.
- Spring Data JPA.
- Spring Security.
- OAuth2 Resource Server / JWT.
- Bean Validation.
- PostgreSQL Driver.
- Flyway para migraciones, aunque inicialmente puede estar desactivado en desarrollo.
- Springdoc OpenAPI / Swagger.
- Lombok.
- MapStruct.
- Actuator.
- Testcontainers para pruebas con PostgreSQL.

### Base de datos

- PostgreSQL.
- Adminer para administración local.

### Docker local

- `postgres`.
- `adminer`.
- `backend`.

---

## 6. Principios de diseño backend

### 6.1 DTOs

Usar DTOs para entrada y salida. No exponer entidades JPA directamente en controllers.

Ejemplo:

```java
public record CreateRepairOrderRequest(
    Long businessId,
    Long customerId,
    Long customerDeviceId,
    Long receivedByBusinessUserId,
    Long assignedBusinessUserId,
    String problemDescription
) {}
```

### 6.2 Validaciones

Usar anotaciones de Jakarta Validation:

```java
@NotNull
@NotBlank
@Email
@Positive
@Size
```

### 6.3 Transacciones

Los métodos que creen o modifiquen datos deben usar `@Transactional` en el service.

Ejemplo:

```java
@Transactional
public RepairOrderResponse create(CreateRepairOrderRequest request) {
    // validar negocio, cliente, dispositivo, responsable
    // crear orden
    // crear historial inicial
    // guardar
}
```

### 6.4 Mappers

Usar MapStruct para convertir Entity ↔ DTO.

### 6.5 Respuestas

Usar respuestas consistentes. Idealmente una clase común como:

```java
public record ApiResponse<T>(
    boolean success,
    String message,
    T data
) {}
```

### 6.6 Fechas

Usar `LocalDateTime` para timestamps y `LocalTime` para horarios.

### 6.7 Soft delete

Muchas tablas tienen:

```txt
active
created_at
updated_at
deleted_at
```

Regla:

- No borrar físicamente registros importantes.
- Desactivar con `active = false` y/o `deleted_at`.

---

## 7. Multitenancy

El sistema usa multitenancy por `business_id`.

```txt
business = tenant
```

### Reglas principales

- `app_user` es global.
- `business` representa cada negocio/taller.
- `business_user` representa un usuario dentro de un negocio.
- `customer_device` pertenece a un negocio y a un cliente.
- `repair_order`, `appointment`, `inventory_item`, `business_service`, `sale`, `quotation`, etc. deben estar asociados a `business_id`.
- Nunca permitir que un negocio use datos de otro negocio.

### Validaciones obligatorias

Al crear una orden, validar:

```txt
repair_order.business_id == customer_device.business_id
repair_order.customer_id == customer_device.customer_id
```

Al crear una cita, validar:

```txt
appointment.business_id == customer_device.business_id
appointment.customer_id == customer_device.customer_id
```

Al usar un repuesto en una orden, validar:

```txt
inventory_item.business_id == repair_order.business_id
```

Al hacer una venta, validar:

```txt
inventory_item.business_id == sale.business_id
```

### No confiar solo en el frontend

Todas estas validaciones deben estar en backend.

---

## 8. Modelo de datos por módulo

Esta sección resume las tablas principales que ya existen o deben implementarse.

### 8.1 Usuarios y documentos

Tablas:

```txt
app_user
document_type
role
user_role
```

Responsabilidades:

- Registro de usuarios.
- Login general.
- Tipos de documento: CC, NIT, pasaporte, cédula de extranjería.
- Roles globales.

Reglas:

- `email` es único global.
- `document_type_id + document_number` es único para usuarios.

---

### 8.2 Negocios y multitenancy

Tablas:

```txt
business
business_request
business_user
business_schedule
business_service
service_category
```

Responsabilidades:

- Crear y aprobar solicitudes de negocio.
- Asociar usuarios a negocios.
- Configurar servicios ofrecidos.
- Configurar horarios por jornada.

Reglas:

- `business` es el tenant.
- `business_user` define usuario + negocio + rol.
- `business_service` pertenece a un negocio.
- `business_schedule` permite múltiples jornadas por día.

---

### 8.3 Ubicación

Tablas:

```txt
country
city
```

Reglas:

- `country.code` único.
- `city.country_id + city.name` único.

---

### 8.4 Dispositivos del cliente

Tablas:

```txt
customer_device
device_type
brand
```

Responsabilidades:

- Registrar dispositivos del cliente por negocio.
- Controlar tipo de dispositivo y marca.

Reglas:

- El cliente es global.
- El dispositivo está registrado dentro de un negocio.
- `customer_device.business_id` es obligatorio.
- `registered_by_business_user_id` indica quién registró el equipo internamente.
- Validar que el dispositivo usado en una orden o cita pertenezca al mismo negocio y cliente.

---

### 8.5 Citas

Tablas:

```txt
appointment
appointment_status
```

Responsabilidades:

- Permitir agendamiento.
- Asociar cita a negocio, cliente, dispositivo y servicio.

Estados sugeridos:

```txt
PENDING
CONFIRMED
CANCELLED
COMPLETED
NO_SHOW
```

---

### 8.6 Órdenes de reparación

Tablas:

```txt
repair_order
repair_order_status
repair_order_history
repair_order_attachment
repair_order_quality_check
repair_order_part
```

Responsabilidades:

- Crear orden de servicio.
- Gestionar estado actual.
- Guardar historial de novedades.
- Registrar evidencias fotográficas.
- Registrar pruebas de ingreso.
- Asociar repuestos usados.

Estados sugeridos:

```txt
RECEIVED
DIAGNOSIS
WAITING_APPROVAL
WAITING_PARTS
REPAIRING
TESTING
READY_FOR_PICKUP
DELIVERED
CANCELLED
```

Reglas:

- `business_id + order_number` debe ser único.
- `received_by_business_user_id` indica quién recibió el equipo.
- `assigned_business_user_id` indica quién está asignado a la reparación.
- `repair_order_history.visibility` puede ser:

```txt
PRIVATE
CUSTOMER
SHARED
```

---

### 8.7 Pruebas de ingreso

Tablas:

```txt
business_quality_test
repair_order_quality_check
```

Responsabilidades:

- Cada negocio configura sus propias pruebas.
- Cada orden guarda los resultados de esas pruebas.

Resultados sugeridos:

```txt
OK
FAILED
NOT_APPLICABLE
NOT_TESTED
```

---

### 8.8 Evidencias fotográficas

Tabla:

```txt
repair_order_attachment
```

Responsabilidad:

- Guardar evidencias de una orden.
- El archivo real debe guardarse en storage externo.
- La base de datos solo guarda `file_url` y `description`.

Reglas:

- Solo permitir imágenes en V1.
- Idealmente convertir imágenes a WebP.
- `uploaded_by_business_user_id` indica quién subió la evidencia.

---

### 8.9 Cotizaciones

Tablas:

```txt
quotation
quotation_status
quotation_item
```

Responsabilidades:

- Crear cotización sobre una orden.
- Asociar detalle de ítems.
- Permitir aceptar, rechazar, cancelar o expirar.

Estados sugeridos:

```txt
PENDING
APPROVED
REJECTED
EXPIRED
CANCELLED
```

---

### 8.10 Inventario

Tablas:

```txt
inventory_item
item_category
stock_movement
movement_type
```

Responsabilidades:

- Controlar inventario por negocio.
- Clasificar productos/repuestos/accesorios.
- Controlar entradas, salidas y ajustes.

Categorías sugeridas:

```txt
ACCESSORY
SPARE_PART
SUPPLY
TOOL
```

Tipos de movimiento sugeridos:

```txt
IN
OUT
ADJUSTMENT
RETURN
```

Reglas:

- `inventory_item.business_id + sku` único.
- `sku` es el código interno del negocio.
- `stock_movement.repair_order_id` opcional.
- `stock_movement.sale_item_id` opcional.
- Una salida de inventario puede venir de reparación o venta directa.

---

### 8.11 Ventas directas

Tablas:

```txt
sale
sale_item
payment_method
```

Responsabilidades:

- Vender accesorios o productos fuera de una reparación.
- Calcular ganancias por venta directa.

Reglas:

- `sale.customer_id` puede ser null.
- `sale_item` guarda valores históricos: `unit_cost`, `unit_price`, `total_cost`, `total_price`.
- Ganancia se calcula como:

```txt
total_price - total_cost
```

---

### 8.12 Reseñas

Tabla:

```txt
business_review
```

Responsabilidades:

- Calificar negocio después de una orden.
- Alimentar `business.rating_average` y `business.rating_count`.

Reglas:

- Una orden solo debe tener una reseña.
- `repair_order_id` debe ser único.

---

### 8.13 Permisos

Tablas:

```txt
module
permission
role_permission
```

Responsabilidades:

- Agrupar permisos por módulo.
- Asignar permisos a roles.

Reglas V1:

- No usar permisos personalizados por usuario todavía.
- V1 usa permisos por rol.
- V2 puede agregar `business_user_permission`.

Permisos sugeridos:

```txt
REPAIR_ORDER_CREATE
REPAIR_ORDER_VIEW
REPAIR_ORDER_UPDATE
REPAIR_ORDER_CLOSE
REPAIR_ORDER_UPLOAD_EVIDENCE
INVENTORY_VIEW
INVENTORY_CREATE
INVENTORY_UPDATE
SALE_CREATE
SALE_VIEW
QUOTATION_CREATE
REPORT_VIEW
BUSINESS_USER_CREATE
```

Módulos sugeridos:

```txt
REPAIR_ORDER
INVENTORY
SALE
QUOTATION
APPOINTMENT
BUSINESS
USER_MANAGEMENT
REPORT
REVIEW
```

---

## 9. Reglas de negocio críticas

### 9.1 No mezclar tenants

Nunca permitir que un negocio vea o modifique datos de otro negocio.

Todo endpoint del panel interno debe filtrar por `business_id`.

### 9.2 No exponer entidades JPA

Los controllers siempre deben recibir requests y devolver responses.

### 9.3 Registrar historial de órdenes

Cada cambio importante de una orden debe crear un registro en `repair_order_history`.

Ejemplos:

- Orden creada.
- Estado cambiado.
- Diagnóstico agregado.
- Cotización enviada.
- Cotización aprobada.
- Equipo listo para entrega.
- Orden entregada.

### 9.4 Inventario transaccional

Cuando se venda un accesorio o se use un repuesto:

- Crear `sale_item` o `repair_order_part`.
- Crear `stock_movement` tipo `OUT`.
- Descontar `inventory_item.quantity`.

Todo debe ocurrir en una sola transacción.

### 9.5 Valores históricos

No depender de `inventory_item.sale_price` o `unit_cost` para reportes antiguos.

Guardar valores históricos en:

- `sale_item.unit_cost`
- `sale_item.unit_price`
- `sale_item.total_cost`
- `sale_item.total_price`
- `repair_order_part.unit_price`
- `repair_order_part.total_price`

Si se agregan costos a `repair_order_part`, también guardar:

- `unit_cost`
- `total_cost`

---

## 10. Convenciones de paquetes

### Controllers

- Paquete: `module.controller`
- Sufijo: `Controller`
- Ruta base: `/api/v1/...`

Ejemplo:

```java
@RestController
@RequestMapping("/api/v1/repair-orders")
public class RepairOrderController {
}
```

### Services

- Paquete: `module.service`
- Sufijo: `Service`
- Contienen reglas de negocio.
- Usar `@Transactional` cuando modifiquen datos.

### Repositories

- Paquete: `module.repository`
- Extienden `JpaRepository`.

### Entities

- Paquete: `module.entity`
- Representan tablas.
- No usar `@Data` de Lombok en entidades JPA.
- Preferir `@Getter`, `@Setter`, `@NoArgsConstructor`.

### DTOs

- Paquete: `module.dto`
- Usar records si es conveniente.
- Separar request y response.

Ejemplos:

```txt
CreateRepairOrderRequest
RepairOrderResponse
UpdateRepairOrderStatusRequest
```

### Mappers

- Paquete: `module.mapper`
- Usar MapStruct.

---

## 11. API base sugerida

### Auth

```txt
POST /api/v1/auth/login
POST /api/v1/auth/register
POST /api/v1/auth/refresh
```

### Business

```txt
POST /api/v1/business-requests
GET  /api/v1/businesses
GET  /api/v1/businesses/{id}
POST /api/v1/businesses/{id}/users
GET  /api/v1/businesses/{id}/services
POST /api/v1/businesses/{id}/services
GET  /api/v1/businesses/{id}/schedule
POST /api/v1/businesses/{id}/schedule
```

### Appointments

```txt
POST /api/v1/appointments
GET  /api/v1/businesses/{businessId}/appointments
PATCH /api/v1/appointments/{id}/status
```

### Repair Orders

```txt
POST /api/v1/repair-orders
GET  /api/v1/businesses/{businessId}/repair-orders
GET  /api/v1/repair-orders/{id}
PATCH /api/v1/repair-orders/{id}/status
POST /api/v1/repair-orders/{id}/history
POST /api/v1/repair-orders/{id}/attachments
POST /api/v1/repair-orders/{id}/quality-checks
POST /api/v1/repair-orders/{id}/parts
```

### Inventory

```txt
POST /api/v1/businesses/{businessId}/inventory/items
GET  /api/v1/businesses/{businessId}/inventory/items
POST /api/v1/businesses/{businessId}/inventory/movements
GET  /api/v1/businesses/{businessId}/inventory/movements
```

### Sales

```txt
POST /api/v1/sales
GET  /api/v1/businesses/{businessId}/sales
GET  /api/v1/sales/{id}
```

### Quotations

```txt
POST /api/v1/quotations
GET  /api/v1/repair-orders/{repairOrderId}/quotations
PATCH /api/v1/quotations/{id}/status
```

### Reviews

```txt
POST /api/v1/business-reviews
GET  /api/v1/businesses/{businessId}/reviews
```

---

## 12. Docker y entorno local

El proyecto debe poder levantarse con Docker Compose.

Servicios:

```txt
postgres
adminer
backend
```

Archivos esperados:

```txt
.env
.env.example
docker-compose.yml
Dockerfile
.dockerignore
```

Adminer:

```txt
http://localhost:8081
```

Backend:

```txt
http://localhost:8080
```

Swagger:

```txt
http://localhost:8080/swagger-ui.html
```

Health:

```txt
http://localhost:8080/actuator/health
```

---

## 13. Tareas técnicas pendientes detectadas

Antes de avanzar demasiado, revisar y corregir:

1. Eliminar tablas temporales `plantilla_tabla`.
2. Eliminar relaciones duplicadas `fk_inventory_item_id_repair_order_part`.
3. En `permission`, cambiar `description integer` a `description varchar` o `text`.
4. En `permission`, `module_id` debería ser `not null`.
5. Agregar unique en `role_permission(role_id, permission_id)`.
6. Revisar si `repair_order_part` debe guardar también `unit_cost` y `total_cost` para reportes de utilidad por repuesto.
7. Evaluar si `business_review.rating` debe ser `integer` en vez de `decimal` si se usarán estrellas de 1 a 5.
8. En `stock_movement`, `sale_item_id` debe ser opcional.
9. Validar en backend todas las relaciones multitenant.

---

## 14. Qué NO debe hacer Codex sin confirmación

No hacer estos cambios sin pedir confirmación:

- Cambiar arquitectura modular a microservicios.
- Cambiar PostgreSQL por otra base de datos.
- Eliminar `business_id` de tablas tenant.
- Exponer entidades JPA directamente en controllers.
- Crear flujos de pago reales con pasarela.
- Implementar WhatsApp o IA avanzada.
- Cambiar el modelo de permisos V1 a permisos por usuario.
- Renombrar masivamente tablas sin confirmación.

---

## 15. Prioridad de implementación sugerida

### Fase 1 — Base técnica

1. Configuración Docker.
2. PostgreSQL + Adminer.
3. Configuración Spring Boot.
4. CORS.
5. Swagger.
6. Estructura modular.
7. Manejo global de errores.

### Fase 2 — Seguridad

1. Auth.
2. JWT.
3. Usuarios.
4. Roles.
5. Módulos y permisos.

### Fase 3 — Negocios

1. Business.
2. Business request.
3. Business user.
4. Business services.
5. Business schedule.

### Fase 4 — Citas y dispositivos

1. Customer device.
2. Appointment.
3. Appointment status.

### Fase 5 — Órdenes

1. Repair order.
2. Repair order status.
3. Repair order history.
4. Quality checks.
5. Attachments.

### Fase 6 — Cotizaciones

1. Quotation status.
2. Quotation.
3. Quotation item.

### Fase 7 — Inventario y ventas

1. Item category.
2. Inventory item.
3. Movement type.
4. Stock movement.
5. Repair order part.
6. Payment method.
7. Sale.
8. Sale item.

### Fase 8 — Marketplace

1. Business review.
2. Rating average/rating count.
3. Búsqueda por ciudad, dispositivo y categoría de servicio.

---

## 16. Notas para generación de código

Cuando Codex genere código:

- Mantener nombres en inglés.
- Usar paquetes por módulo funcional.
- Respetar `business_id` como tenant.
- Usar DTOs y mappers.
- Usar validaciones en requests.
- Usar `@Transactional` en operaciones que modifiquen múltiples tablas.
- No mezclar lógica de inventario dentro de controllers.
- No llamar repositories de otros módulos directamente si existe service.
- Crear excepciones específicas por dominio.
- Mantener endpoints bajo `/api/v1`.
- Preferir respuestas claras y consistentes.
- Crear tests unitarios básicos para services importantes.
- En frontend, todo texto visible en componentes `Select` debe mostrarse en español mediante mapeo/formateo de etiquetas. No renderizar directamente valores crudos de base de datos (por ejemplo `BUSINESS`, `GLOBAL`, `ACTIVE`, etc.) en la UI.

---

## 17. Resumen corto para Codex

TecMarket es un backend Spring Boot para un marketplace y sistema interno de gestión de servicios técnicos. La arquitectura es monolito modular. `business` es el tenant. Los usuarios son globales mediante `app_user`, y los usuarios internos de una empresa se representan con `business_user`. El sistema debe manejar negocios, servicios, horarios, citas, dispositivos por negocio, órdenes de reparación, pruebas de ingreso, evidencias, cotizaciones, inventario, ventas directas, reseñas y permisos por rol. No usar microservicios en V1. Respetar siempre aislamiento por `business_id`.

## 18. Contexto de Base de Datos

Antes de crear o modificar entidades JPA, migraciones Flyway, DTOs, mappers, repositories o services relacionados con base de datos, revisar siempre:

- `docs/database-model.md`
- `docs/database-schema.drawdb.json`

Este proyecto usa PostgreSQL como base de datos principal.

El modelo de base de datos está diseñado en DrawDB y exportado en:

- `docs/database-schema.drawdb.json`

También debe mantenerse un resumen legible del modelo de datos en:

- `docs/database-model.md`

Antes de crear o modificar cualquier entidad JPA, repositorio, migración Flyway, DTO, mapper o servicio que dependa del esquema de base de datos, siempre revisa estos archivos.

## Reglas de Diseño de Base de Datos

La base de datos sigue estas convenciones:

- Los nombres de las tablas usan `snake_case`.
- Las entidades Java deben usar `PascalCase`.
- Los campos Java deben usar `camelCase`.
- Las columnas de base de datos deben mantenerse en `snake_case`.
- Usa `@Table(name = "...")` en cada entidad.
- Usa `@Column(name = "...")` para cada campo persistido.
- Las llaves primarias se llaman `id`.
- Las llaves foráneas usan el formato `<entidad>_id`.
- La mayoría de tablas de negocio incluyen:
  - `active`
  - `created_at`
  - `updated_at`
  - `deleted_at`

## Mapeo de Tipos Java

Usa estos tipos Java para las columnas de base de datos:

- `INTEGER` → `Long` para IDs y llaves foráneas.
- `VARCHAR` → `String`.
- `TEXT` → `String`.
- `BOOLEAN` → `Boolean`.
- `DECIMAL` → `BigDecimal`.
- `TIMESTAMP` → `LocalDateTime`.
- `TIME` → `LocalTime`.

No uses `Double` ni `Float` para campos de dinero. Usa siempre `BigDecimal`.

## Reglas para Entidades JPA

Al crear entidades JPA:

- No uses Lombok `@Data` en entidades.
- Prefiere:
  - `@Getter`
  - `@Setter`
  - `@NoArgsConstructor`
  - `@AllArgsConstructor`
  - `@Builder` solo cuando sea útil
- Evita exponer entidades directamente en respuestas de la API.
- Usa DTOs para requests y responses.
- Usa MapStruct para mapear entre entidades y DTOs.
- No pongas lógica de negocio en los controladores.
- No accedas a repositorios directamente desde los controladores.
- Los servicios son responsables de las reglas de negocio.

## Auditoría y Eliminación Lógica

La mayoría de tablas usan eliminación lógica con:

- `active`
- `deleted_at`

No elimines filas físicamente a menos que se solicite explícitamente.

Para operaciones normales de eliminación:

- Asigna `active = false`.
- Asigna `deleted_at = now()`.

## Reglas de Multitenancy

Este sistema es multitenant mediante `business_id`.

Cualquier tabla que tenga `business_id` pertenece a una empresa/tenant específico.

Tablas importantes dentro del contexto de tenant:

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

Nunca permitas que un usuario de una empresa acceda o modifique registros de otra empresa.

Al implementar consultas para datos pertenecientes a un tenant, siempre filtra por `business_id`.

Ejemplo:

```java
findByIdAndBusinessId(Long id, Long businessId)
```
