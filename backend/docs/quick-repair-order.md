# Orden rápida de reparación

## Objetivo

Crear o reutilizar al cliente, crear o reutilizar su dispositivo y generar la orden de reparación mediante una única operación transaccional.

```http
POST /api/v1/businesses/{businessId}/repair-orders/quick
Authorization: Bearer <access-token>
Idempotency-Key: <uuid-o-clave-unica-de-8-a-100-caracteres>
Content-Type: application/json
```

El actor necesita simultáneamente `CUSTOMER_DEVICE_CREATE` y `REPAIR_ORDER_CREATE` dentro del negocio seleccionado. La sede también debe estar dentro de su contexto operativo.

## Body

```json
{
  "customer": {
    "documentTypeCode": "CC",
    "documentNumber": "123456789",
    "name": "Laura",
    "surname": "Gómez",
    "email": "laura@example.com",
    "address": "Calle 10 #20-30",
    "phone": "+573001112233",
    "countryId": 1,
    "departmentId": 2,
    "cityId": 3
  },
  "device": {
    "deviceTypeId": 11,
    "brandId": 12,
    "model": "iPhone 13 Pro",
    "serialNumber": "SN-ABC-12345",
    "imei": "351756051523999",
    "color": "Azul",
    "registeredAt": "2026-09-24T10:30:00"
  },
  "order": {
    "branchId": 21,
    "businessServiceId": 22,
    "assignedBusinessUserId": 23,
    "problemDescription": "No enciende y no responde al cargar.",
    "diagnosis": null,
    "estimatedPrice": 150000,
    "estimatedDeliveryAt": "2026-09-27T18:00:00",
    "receivedAt": "2026-09-24T10:30:00"
  }
}
```

`registeredAt`, `serialNumber`, `imei`, `diagnosis`, `estimatedPrice`, `estimatedDeliveryAt` y `receivedAt` pueden ser `null` u omitirse cuando el contrato de validación lo permite. `appointmentId` no forma parte del alta rápida.

## Comportamiento

1. Valida ambos permisos contra el `businessId` solicitado.
2. Bloquea la sede durante la operación y comprueba `Idempotency-Key` antes de crear datos.
3. Busca al usuario global por correo y por documento.
4. Si ambos identificadores pertenecen al mismo usuario activo, lo reutiliza sin sobrescribir silenciosamente su perfil.
5. Si no existe, crea una cuenta `CUSTOMER` con una contraseña aleatoria desconocida para el empleado.
6. Reutiliza el dispositivo cuando el IMEI ya pertenece al mismo cliente y los datos esenciales coinciden; nunca permite que el mismo IMEI activo pertenezca a dos clientes del negocio.
7. Deriva el empleado que recibe desde el JWT, valida la sede, el servicio y el técnico y crea la orden con su historial inicial.
8. Para una cuenta nueva, genera un token de un solo uso y envía el enlace de creación de contraseña.

Un fallo de validación, identidad, dispositivo, orden o envío de correo revierte los cambios de base de datos de la operación.

## Respuesta 201

```json
{
  "success": true,
  "message": "Cliente, dispositivo y orden creados correctamente.",
  "data": {
    "customer": {
      "id": 80,
      "documentTypeCode": "CC",
      "documentNumber": "123456789",
      "name": "Laura",
      "surname": "Gómez",
      "email": "laura@example.com",
      "phone": "+573001112233",
      "emailVerified": false
    },
    "device": {},
    "repairOrder": {}
  }
}
```

`device` usa `CustomerDeviceResponse` y `repairOrder` usa `RepairOrderResponse`.

## Identidad e invitación

- Documento y correo resueltos a usuarios diferentes producen `409 Conflict`.
- Una cuenta existente no se modifica ni recibe una nueva invitación automáticamente.
- Una cuenta nueva recibe un enlace con el token en `#token=...`; el fragmento no viaja al servidor al abrir la URL.
- El frontend obtiene el token del fragmento y envía:

```http
POST /api/v1/auth/password-reset/confirm
```

```json
{
  "token": "token-recibido-en-el-fragmento",
  "newPassword": "NuevaContraseñaSegura123!"
}
```

Al consumir el token se establece la contraseña, se confirma el correo y se invalidan las sesiones anteriores. La contraseña nunca se envía ni se muestra al empleado del taller.

## Integración frontend

- Mostrar el botón `Orden rápida` únicamente cuando el contexto activo incluya ambos permisos requeridos.
- Usar un panel o modal amplio con tres secciones visibles: `Cliente`, `Dispositivo` y `Orden`.
- Generar una nueva clave de idempotencia al iniciar el formulario y conservarla durante reintentos del mismo envío.
- Deshabilitar el botón durante el envío, pero no borrar el formulario ante errores recuperables.
- Después del `201`, mostrar el número de orden y explicar que el cliente nuevo recibirá instrucciones para crear su contraseña.
- Configurar `PASSWORD_RESET_URL` con la ruta pública real del frontend; en el frontend actual es `/restablecer-contrasena`.
- Esa pantalla debe leer `window.location.hash`, extraer `token` y utilizar el endpoint de confirmación anterior. No convertirlo en query parameter: el fragmento evita que el token viaje en la petición HTTP inicial, logs del proxy y encabezados `Referer`.
