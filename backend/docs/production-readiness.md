# TecMarket production readiness

Este documento es la puerta operativa del piloto. El código o una imagen local no
autorizan por sí solos un despliegue.

## Automatizado por CI

1. Compilación Java 21 y suite Maven completa.
2. Migración Flyway sobre PostgreSQL real mediante Testcontainers.
3. Validación de espacios y marcadores de conflicto.
4. Escaneo de secretos con Gitleaks.
5. CodeQL, SBOM CycloneDX y escaneo Trivy de la imagen.
6. Construcción de la imagen solo después de la verificación.

## Configuración obligatoria

- Perfil `docker,prod`, `ddl-auto=validate`, Flyway activo y Swagger desactivado.
- JWT, PostgreSQL, Redis, Resend y MinIO entregados por el gestor de secretos; ningún
  valor real se guarda en Git o Compose.
- PostgreSQL usa dos identidades: `SPRING_FLYWAY_USERNAME` posee los objetos y aplica
  migraciones; `SPRING_DATASOURCE_USERNAME` solo recibe DML, secuencias y ejecución de
  funciones. Deben tener contraseñas diferentes.
- `CORS_ALLOWED_ORIGINS` exacto y `TRUSTED_PROXY_RANGES` limitado exclusivamente a la
  red del proxy Dokploy. Nunca confiar en `X-Forwarded-For` desde Internet.
- Refresh cookie `Secure`, `HttpOnly`, `SameSite=Lax`; Origin obligatorio en refresh y
  logout. Para probar un frontend HTTP local contra un backend HTTPS remoto, usar
  temporalmente `SameSite=None`, agregar el origen local exacto y activar
  `CORS_ALLOW_HTTP_LOCALHOST=true`; producción normal conserva el opt-in desactivado.
  Access token corto y mantenido en memoria por el cliente.
- Validación de `security_version` contra PostgreSQL en cada request autenticado y
  revocación total después de contraseña, estado de usuario o cambios de rol.
- Claim `sid` enlazado a una familia activa de refresh tokens: logout, revocación
  individual y replay invalidan inmediatamente sus access tokens.
- Rate limit distribuido en Redis por IP, correo normalizado hasheado, operación y
  combinación IP-cuenta; Redis es fail-closed en producción.
- PostgreSQL, Redis y MinIO sin puertos públicos. Adminer permanece fuera del perfil
  normal y requiere un acceso privado adicional cuando se habilite.
- Orígenes HTTPS distintos para assets públicos y evidencias privadas. El proxy de
  ambos debe enviar `X-Content-Type-Options: nosniff`; el bucket de evidencias no puede
  aceptar lectura anónima y las URLs firmadas no superan 15 minutos.
- Worker `storage_object_deletion_task` monitorizado: alertar tareas `FAILED`, conservar
  evidencia al menos `STORAGE_EVIDENCE_RETENTION_DAYS` y no borrar objetos directamente
  dentro de transacciones de negocio.
- `ProductionConfigurationValidator` impide el arranque si PostgreSQL, JWT, Redis,
  CORS, proxy, cookies, Flyway, Swagger, seeds o URLs públicas no cumplen el contrato
  seguro. `ProductionStorageConfigurationValidator` hace lo mismo para MinIO.
- El backend se ejecuta sin root, con filesystem de solo lectura, capabilities
  eliminadas, límite de procesos, CPU/memoria, rotación de logs y shutdown ordenado.
- Las imágenes base de Java, Maven y Redis están fijadas por digest. Dokploy debe
  registrar también el digest de la imagen backend, PostgreSQL y MinIO; un tag por sí
  solo no constituye una versión desplegable.

## Backups, restauración y rollback

Antes del piloto el operador debe completar y conservar evidencia de:

1. Backup PostgreSQL diario cifrado, externo al host y con retención definida.
2. Versionado o replicación de los buckets privados de evidencia.
3. Restauración completa en staging y comprobación de conteos, adjuntos y login.
4. RPO objetivo máximo de 24 horas y RTO objetivo máximo de 4 horas para el piloto.
5. Rollback de aplicación a la imagen anterior. Las migraciones son forward-only; si
   una migración no es compatible hacia atrás, se detiene el despliegue antes de aplicarla.

Los scripts reproducibles están en `ops/`:

- `postgres-backup.sh` y `minio-backup.sh` generan artefactos cifrados y checksum.
- `postgres-restore.sh` y `minio-restore.sh` exigen confirmación exacta del destino.
- `rollback-backend.sh` solo acepta una imagen anterior fijada por digest y exige
  confirmar compatibilidad con el esquema actual.

La programación recomendada para el piloto es backup diario a las `02:00 UTC`, copia
externa inmediata, retención diaria de 14 días y cuatro copias semanales. La contraseña
de cifrado y los archivos `PGPASSFILE` deben venir del gestor de secretos y nunca viajar
junto al backup. Esto satisface el objetivo RPO de 24 horas; el ensayo cronometrado en
staging debe demostrar el RTO de 4 horas.

## Observabilidad

- Consumir `/actuator/health/liveness` y `/actuator/health/readiness` sin mostrar detalles.
- Readiness depende de PostgreSQL, Redis y los tres buckets obligatorios de MinIO;
  liveness solo representa el estado interno del proceso.
- Recolectar logs JSON con `X-Correlation-ID` y métricas Prometheus.
- Alertar por 5xx, latencia p95, fallos de login, rechazo de rate limit, conexiones DB,
  espacio de disco, errores MinIO y expiración del certificado TLS.

## QA manual obligatorio de staging

- Matriz rol × negocio × sucursal, incluyendo todos los intentos cruzados negativos.
- Flujo cliente/dispositivo → orden → evidencia → cotización → reparación → repuesto →
  entrega → consulta del cliente.
- Replay de refresh, revocación, recuperación de contraseña y desactivación de usuario.
- Doble venta, doble uso de repuesto, reintentos con `Idempotency-Key` y stock negativo.
- Archivos corruptos, MIME/extensión falsos, doble extensión, múltiples fotogramas,
  dimensiones/píxeles excesivos, EXIF/GPS, URL expirada y caída parcial DB/MinIO.
- DAST y carga sobre login, búsqueda, órdenes y evidencia.

## Go-live

No abrir producción hasta que CI esté verde con Docker disponible, restauración y
rollback estén ensayados, no existan P0/P1 abiertos y el responsable del piloto firme
la aceptación del flujo completo.

## Registro de evidencia de liberación

Antes de promover una imagen se debe registrar:

| Evidencia | Valor obligatorio |
|---|---|
| Commit Git | SHA completo |
| Backend | Imagen con `@sha256` |
| PostgreSQL | Versión y digest |
| MinIO | `RELEASE` exacto y digest |
| Redis | Digest declarado en Compose |
| Flyway | Versión final aplicada y `success=true` |
| Backup | Archivo, checksum, fecha y ubicación externa |
| Restore | Inicio, fin, duración, conteos y responsable |
| Rollback | Imagen anterior, duración y resultado de readiness |
| TLS | Dominio, emisor y fecha de expiración |

Los scripts y la CI validan el mecanismo, pero **no sustituyen el ensayo real sobre
staging ni la activación de alertas en Dokploy**. Esos dos elementos siguen siendo una
puerta humana previa al despliegue.
