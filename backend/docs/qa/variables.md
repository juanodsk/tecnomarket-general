# Datos visibles de la colección Bruno

La colección manual ya no usa variables de Bruno ni un environment. Todos los valores se editan directamente en la petición o en el Auth de la carpeta.

## Convenciones

| Valor visible | Significado | Acción |
|---|---|---|
| `100xxx` | Recurso demostrativo del Negocio A | Reemplazar por el ID real de A |
| `200xxx` | Recurso demostrativo del Negocio B | Reemplazar por el ID real de B |
| `900xxx` o `99999999` | Recurso global o ID inexistente | Confirmar el caso antes de enviar |
| `PEGAR_...` | Token JWT, refresh cookie o token recibido por correo | Pegar el valor real localmente |
| `CAMBIAR_...` | Ruta de archivo u otro dato obligatorio | Sustituir antes de enviar |
| `EDITAR_...` | Dato de negocio que requiere revisión | Sustituir por un valor válido o el valor inválido que exige el caso |

## Origen de los IDs principales

| Dato | Cómo obtenerlo |
|---|---|
| `countryId`, `departmentId`, `cityId` | Consultar los endpoints públicos de ubicación y copiar `data[].id` |
| `businessId` | Aprobar la solicitud y consultar el negocio; copiar `data.id` o el ID del resultado exacto |
| `branchId` | Crear/listar la sede del negocio correcto y copiar `data.id` |
| `businessUserId` | Crear/listar la membresía empresarial y copiar su `data.id`; no usar el ID global del usuario |
| IDs de catálogos del negocio | Consultar el tipo dentro del mismo negocio; no reutilizar IDs de A en B |
| `customerDeviceId` | Crear el dispositivo para el cliente y negocio seleccionados |
| `repairOrderId` y `orderNumber` | Crear la orden y copiar ambos campos de su respuesta |
| `quotationId`, `inventoryItemId`, `attachmentId`, `partId` | Copiar `data.id` de la creación correspondiente |
| `sessionId` | Listar sesiones del mismo usuario autenticado |

Después de cada creación, copia manualmente el ID de la respuesta al siguiente request. Esta decisión es intencional: el valor enviado siempre queda visible y Bruno no altera el body mediante scripts.

## Autenticación

- Configura `PEGAR_TOKEN_ADMINISTRADOR_GLOBAL` en el Auth de `01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES`.
- Configura `PEGAR_TOKEN_PROPIETARIO_A` en el Auth de `02_NEGOCIO_A_REPARACION_COMPLETA`.
- Configura `PEGAR_TOKEN_PROPIETARIO_B` en el Auth de `03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION`.
- Cuando un caso requiere cliente, técnico, usuario compartido o actor sin permiso, el Auth de esa petición muestra exactamente qué token debes pegar.

No guardes JWT, refresh cookies ni contraseñas reales en archivos rastreados por Git. `bruno/` permanece excluido del repositorio.
