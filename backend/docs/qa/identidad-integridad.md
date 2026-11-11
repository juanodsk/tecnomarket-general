# Recorridos de identidad, integridad e infraestructura

Usar [README](README.md) primero. Cada escenario requiere una cuenta u orden QA propia; no ejecutar pruebas de sesión con el administrador de bootstrap. Los tokens se guardan solo en Bruno local. Todas las pruebas siguientes están pendientes de ejecución real.

## Sesiones y permisos

Antes de cada escenario: nuevo LOGIN de la cuenta QA; copiar su access a sessionAccessToken, refresh a sessionRefreshToken, userId a qaSessionUserId. EP018 con ese token debe responder 200. Abrir segunda sesión si se comprobará revocación individual frente a global. No actualizar los tokens guardados después de la acción que los revoca.

| Acción | Petición y entrada | Comprobación |
|---|---|---|
| Rotación | EP020-ROTATE, cookie de sesión actual | 200, nueva cookie, sin refresh en JSON; conservar previousRefreshToken |
| Replay | EP020-REPLAY con previousRefreshToken | 401; EP018-AFTER_REPLAY también 401 incluso con access emitido por ROTATE |
| Logout | EP021-OK con Authorization de esa sesión y cookie correspondiente | 200, cookie expirada; EP018-INVALID_AFTER_LOGOUT y EP020-INVALID_AFTER_LOGOUT dan 401 |
| Contraseña | EP017-OK, currentPassword correcto y newPassword válido | 200; ambos tokens anteriores 401; login con vieja clave 401 y con nueva 200 |
| Recuperación | EP015-OK a buzón QA y EP016-OK con token recibido y newPassword | Respuesta genérica de solicitud; confirmación 200, tokens anteriores 401; repetir token de recuperación debe rechazarse |
| Desactivación | EP180-DEACTIVATE_QA usando platformAdmin | 200; sesión anterior 401; login rechazado. Reactivar mediante EP180-OK active=true; nueva sesión válida |
| Revocar sesión | EP022 para localizar sessionId de sesión dedicada, EP023-OK | Sesión elegida y su refresh 401; segunda familia distinta sigue válida |
| Revocar todas propias | EP024-OK | Todas las familias de ese usuario 401; otro usuario mantiene sesión |
| Revocar todas por admin | EP181-OK con id=qaSessionUserId | Igual efecto, sin cambiar contraseña. Usar tokens de QA para las comprobaciones |
| Cambiar permisos | EP166 sobre rol QA_ALLOW, conceder/quitar un permiso BUSINESS | Token previo 401, login nuevo refleja permisos; no modificar TECHNICIAN/OWNER globalmente |
| Cambiar membresía/rol | EP079 con cuerpo completo y rol/sedes QA, o EP080 | Validar token viejo y permisos efectivos después de login nuevo; no asumir que solo cambiar sedes revoca el JWT: probar el acceso efectivo también |

Casos de Origin: habilitar REFRESH_COOKIE_REQUIRE_ORIGIN=true en desarrollo antes de ejecutarlos y registrar valor anterior. Refresh/logout con Origin permitido deben funcionar; Origin ausente/ajeno, 403. Sin cookie en refresh y con Origin permitido, 400. Mantener cookie jar automático desactivado para no ocultar la ausencia de cookie.

Enumeración: solicitar recuperación para correo existente e inexistente y comparar HTTP y mensaje; no revelar existencia. Verificación: token ausente/corto/alterado/expirado/reutilizado; registrar HTTP exacto del servicio y no aceptar éxito. Un token válido recibido para usuario A no debe verificar B. Ninguna respuesta/log exportado debe incluir passwordHash, hashes de tokens o claves internas.

## Rate limit y proxy

Separar estos casos del resto. No lanzar una colección con cientos de registros/login sin pausas.

1. Esperar 65 s sin peticiones de login; elegir qaAbuseEmail de una cuenta sintética inexistente.
2. Enviar cinco LOGIN con ese correo y clave errónea; confirmar 401. Ejecutar EP013-RATE_LIMIT como sexto antes de 60 s: 429 y Retry-After=60.
3. Esperar 65 s y reenviar una vez: 401, ya no 429.
4. Repetir con variantes de mayúsculas/espacios del mismo correo: deben compartir contador. Separar la prueba de normalización del límite global por IP para evitar inferencias falsas.
5. Para demostrar límite por cuenta entre IP diferentes, usar dos clientes con salidas de red realmente distintas. No basta cambiar X-Forwarded-For. Mantener la suma por cuenta sobre el límite y cada IP por debajo del suyo; registrar ambas IP sin credenciales.
6. Para probar confianza del proxy, comparar X-Forwarded-For enviado por cliente con IP real resuelta y comportamiento del límite; requiere configuración/logs del proxy. Si no tienes ese acceso, marcar BLOCKED. No asumir validación distribuida completa por un solo 429.

Redis caído y respuesta 503 requieren un entorno aislado/ventana de mantenimiento explícita. No apagar Redis del desarrollo compartido como parte del recorrido normal. La prueba unitaria de fallback no reemplaza esta prueba operativa.

## Idempotencia y concurrencia

Las peticiones FIRST/REPLAY/CONFLICT están generadas para los cuatro POST que aceptan Idempotency-Key: orden (EP142), repuesto (EP156), venta (EP170) y movimiento (EP172). Primera y repetición deben devolver el mismo data.id; releer stock, movimientos e historial para comprobar ausencia de duplicación. Misma clave con payload diferente debe dar 409. Una nueva clave representa una nueva operación.

Para venta, usar REPLAY y luego cambiar quantity del primer ítem manteniendo misma clave; esperar 409. Verificar también claves iguales con distintos actores/negocios para detectar atribución incorrecta: no deben devolver un recurso ajeno.

El script `qa/Invoke-QaConcurrency.ps1` permite disparar dos POST concurrentes preparados por ti y verifica HTTPs, IDs y saldo final. No se ejecuta durante generación/validación de documentación.

Prueba de última unidad (ventas habilitadas):

1. Crear un artículo QA de stock 1 y precio 150. Crear dos solicitudes de venta válidas con una unidad del mismo artículo y claves distintas. Confirmar EP117.quantity=1 inmediatamente antes.
2. Crear archivo local ignorado `bruno/TECMARKET-QA/resultados/race.json` con URL de ventas, URL GET del artículo, body válido de EP170 (IDs numéricos reales), snapshot `beforeQuantity=1`, `expectedAfterQuantity=0`, `mode=LastUnit`. No incluir tokens: el script los pide como SecureString.
3. Ejecutar `pwsh -File qa/Invoke-QaConcurrency.ps1 -ConfigPath 'bruno/TECMARKET-QA/resultados/race.json'` y suministrar token del actor QA con SALE_CREATE e INVENTORY_VIEW.
4. Exigir un 201 y un 409, saldo final 0. Consultar EP171 y ventas: exactamente una venta efectiva y un movimiento OUT.
5. Repetir con POST de repuestos EP156 sobre orden mutable y stock 1. Una alta 201 y otra 409; una sola pieza vinculada y un movimiento. Crear un fixture nuevo.

Modo Idempotent: body de consumo de 1 unidad, cantidad inicial 2/final 1, misma clave en ambos POST. Dos 201 con mismo data.id; un solo consumo. El script distingue timeout de rechazo: timeout significa FAIL/resultado incierto; releer antes de reintentar.

Plantilla de `race.json` para venta de la última unidad. Sustituir los textos `REEMPLAZAR_*` y los tres ceros por IDs numéricos capturados; los ceros no son datos válidos ni IDs asumidos. No incluir Authorization ni contraseña en este archivo.

```json
{
  "url": "https://api-dev.juanodsk.dev/api/v1/businesses/REEMPLAZAR_BUSINESS_ID/sales",
  "inventoryUrl": "https://api-dev.juanodsk.dev/api/v1/businesses/REEMPLAZAR_BUSINESS_ID/inventory-items/REEMPLAZAR_INVENTORY_ID",
  "mode": "LastUnit",
  "beforeQuantity": 1,
  "expectedAfterQuantity": 0,
  "body": {
    "branchId": 0,
    "customerId": null,
    "paymentMethodId": 0,
    "items": [{"inventoryItemId": 0, "quantity": 1}]
  }
}
```

`branchId` es la sede del artículo; `paymentMethodId` sale del catálogo PAYMENT_METHOD del mismo negocio. `inventoryItemId` debe coincidir con el de inventoryUrl. Para probar repuestos, sustituir url por la ruta exacta de EP156 y body por su ejemplo válido, manteniendo la misma lectura de inventario. Para modo Idempotent usar un artículo nuevo con cantidad 2, cambiar mode a `Idempotent`, beforeQuantity a 2 y expectedAfterQuantity a 1.

La simultaneidad HTTP no garantiza que ambas transacciones se solapen; registrar duración y repetir con nuevos fixtures si se necesita confianza estadística. Las pruebas existentes de locks/PostgreSQL complementan esta evidencia.

## Archivos y privacidad

Usar una orden mutable y EP153 (evidencia), EP040/41 (logo/portada) y EP047 (servicio). Crear imágenes sintéticas locales; no usar fotos de clientes.

| Caso | Entrada | Expectativa |
|---|---|---|
| Normal | PNG/JPEG válido dentro de límites | 200 para imágenes empresariales, 201 para evidencia; listar y descargar correctamente |
| Vacío | Archivo de 0 bytes | 400, sin nueva fila/objeto visible |
| Falso | Texto renombrado .jpg | 400 |
| MIME discordante | Bytes PNG con tipo image/jpeg | 400 |
| Doble extensión | prueba.exe.jpg | 400 |
| Corrupto | PNG truncado | 400 |
| Dimensiones/píxeles | Imagen válida que exceda límite configurado pero no body máximo | 400 |
| Sobre tamaño HTTP | Archivo mayor que spring.servlet.multipart.max-file-size | 413 si llega al backend; si bloquea el proxy, registrar status/body de ese componente por separado |
| EXIF/GPS | JPEG sintético con metadatos controlados | Carga válida reencodificada; descargar resultado y comprobar que no tiene metadatos originales |
| Privada | Evidencia de otra orden/tenant | Endpoint autenticado rechaza; bucket no da acceso anónimo |
| URL firmada | URL obtenida con actor autorizado | Descarga válida antes de vencer y rechazo tras vencimiento; no compartir query firmada en reportes |
| Borrado lógico | Baja de evidencia en orden mutable | Desaparece de listado; eliminación física sigue retención/outbox, no se exige borrado inmediato |

Los fixtures de borde deben construirse con límites efectivos de StorageProperties/Dokploy; no crear una imagen bomba en la máquina del usuario. Para corrupción, dimensiones y EXIF también existen pruebas en `MinioStorageServiceTest`.

Para probar atomicidad DB/MinIO, usar entorno aislado y pruebas de rollback/outbox; registrar BLOCKED si solo tienes desarrollo compartido. No atribuir “sin pérdida ante fallos” a una carga normal exitosa. Los atributos nosniff/TLS del origen público se revisan sobre el dominio de storage, no únicamente sobre api-dev.

## Funciones adicionales: segunda fase

1. Con los flags en false, ejecutar casos DISABLED usando token válido con permiso suficiente (platformAdmin para confirmar registro de rutas): esperar 404, no aceptar 401 como evidencia de deshabilitación.
2. Habilitar una función por vez con su variable FEATURE_* y redesplegar. Repetir health y login. No habilitar todas para ocultar dependencias.
3. Marketplace: EP130 con filtros, EP131 y EP132-EXACT_PRIVACY sobre negocio QA con reseña. Todas las reseñas deben tener los cuatro campos públicos; cliente identificable o ID interno expuesto=FAIL.
4. Ventas: EP170 válido, EP168/169, stock y costos; repetición e inventario insuficiente; vendedor sin SALE_COST_VIEW no debe obtener costos/margen.
5. Comisiones: generar orden QA entregada y técnico con porcentaje; EP088/89 con periodo de entrega → EP090 seleccionando repairOrderIds elegibles → EP092 → EP093 APPROVED → EP093 PAID. Probar orden ya liquidada, técnico ajeno, periodo inválido y reapertura terminal; verificar sumas y estados con EP139/141.
6. Suscripciones: EP175 plan QA, EP173/174, EP069 para negocio sin suscripción activa (si el sistema creó una de prueba, terminarla mediante EP071 antes y registrar snapshot), EP068/67/72 y EP073 recalcular. Actualizar EP070 con fechas válidas y proveedor MANUAL; desactivar al terminar. No introducir pasarela real.
7. Restaurar los cuatro flags a sus valores originales y confirmar casos DISABLED. Registrar operaciones pendientes y recursos QA que quedan retenidos.
