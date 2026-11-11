# TecMarket Backend

Backend de TecMarket construido con Spring Boot 3, Java 21, PostgreSQL, JWT y Flyway.

## Stack

- Java 21
- Spring Boot 3.5
- Spring Security + OAuth2 Resource Server (JWT)
- Spring Data JPA
- PostgreSQL
- Flyway (migraciones)
- SpringDoc OpenAPI (Swagger)

## Requisitos

- JDK 21
- PostgreSQL 15+ (o compatible)
- Maven Wrapper (`./mvnw`) incluido en el proyecto

## Variables de entorno

Referencia: `.env.example`

Variables principales:

```env
JWT_SECRET=una_clave_super_segura_de_minimo_32_bytes
JWT_ISSUER=tecmarket
JWT_AUDIENCE=tecmarket-api
JWT_ACCESS_TOKEN_EXPIRATION_MINUTES=15
JWT_REFRESH_TOKEN_EXPIRATION_DAYS=7
CORS_ALLOWED_ORIGINS=http://localhost:3000

EMAIL_VERIFICATION_EXPIRATION_HOURS=24
EMAIL_VERIFICATION_VERIFY_URL=http://localhost:8080/api/v1/auth/verify-email
EMAIL_VERIFICATION_FROM=no-reply@tu-dominio.com

RESEND_API_KEY=re_xxxxx
RESEND_BASE_URL=https://api.resend.com
```

Notas:

- En local, `application.properties` usa por defecto `localhost:5433/tecmarket_db`.
- `spring.jpa.hibernate.ddl-auto=validate` (la base se controla con Flyway, no con auto-update de Hibernate).

## Ejecucion local

1. Crear base y credenciales en PostgreSQL:
   - DB: `tecmarket_db`
   - User: `tecmarket_user`
   - Password: `tecmarket_password`
2. Ejecutar:

```bash
./mvnw spring-boot:run
```

API base: `http://localhost:8080`

## Migraciones Flyway

- Ubicacion: `src/main/resources/db/migration`
- Convencion por modulo:
  - `db/migration/auth`
  - `db/migration/permission`
  - `db/migration/parameter`
- Guía del flujo: `docs/flyway-migrations.md`

Al iniciar la app, Flyway aplica migraciones pendientes y luego Hibernate valida el esquema.

## Swagger / OpenAPI

- UI: `http://localhost:8080/swagger-ui/index.html`
- JSON: `http://localhost:8080/v3/api-docs`

## Pruebas y validacion

Compilar:

```bash
./mvnw -DskipTests compile
```

Ejecutar tests:

```bash
./mvnw test
```

## Coleccion Bruno

Colección API para pruebas manuales:

- `bruno/TECNOMARKET`

## Alcance operativo del piloto

El piloto mantiene disponible el flujo principal: empresa/sede, cliente/dispositivo,
orden, diagnóstico, evidencia, cotización, reparación, pruebas, repuestos, entrega y
consulta autenticada del cliente. Marketplace público, suscripciones, comisiones y
ventas directas quedan sin rutas registradas por defecto.

Para habilitarlos de forma explícita después de su revisión de salida:

```properties
FEATURE_MARKETPLACE_ENABLED=true
FEATURE_SUBSCRIPTIONS_ENABLED=true
FEATURE_COMMISSIONS_ENABLED=true
FEATURE_DIRECT_SALES_ENABLED=true
```

Los `POST` de órdenes, movimientos, ventas y repuestos aceptan `Idempotency-Key`.
Diagnóstico y asignación se modifican únicamente mediante
`PATCH /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/diagnosis` y
`PATCH /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/assignment`.

Los listados operativos aceptan `page` (desde 0) y `size` (1–100; 20 por defecto).
La respuesta paginada es uniforme:

```json
{
  "success": true,
  "message": "...",
  "data": {
    "items": [],
    "page": 0,
    "size": 20,
    "totalElements": 0,
    "totalPages": 0,
    "hasNext": false,
    "hasPrevious": false
  }
}
```

## Despliegue en Dokploy

El `docker-compose.yml` de este repositorio despliega:

- `backend`: API Spring Boot, puerto interno `8080`.
- `redis`: rate limit distribuido, únicamente en la red privada interna.

Adminer no forma parte del Compose de producción. Solo existe en
`docker-compose.dev.yml`, bajo el perfil local `ops`.

PostgreSQL debe crearse por separado desde Dokploy. Después, configura en el
Compose las variables del archivo `.env.docker.example`, usando el hostname
interno entregado por Dokploy.

Pasos recomendados:

1. Crear PostgreSQL y MinIO en redes privadas de Dokploy y conservar sus URL internas.
   Aplicar `ops/postgres-least-privilege.sql` para separar el migrador Flyway del
   usuario runtime.
2. Publicar una imagen verificada y registrar su digest, commit y fecha de construcción.
3. Crear el servicio Compose desde una rama protegida y configurar los secretos de
   `.env.docker.example` en Dokploy; nunca cargar el archivo como secreto único.
4. Asignar HTTPS al servicio `backend`, puerto `8080`, y limitar
   `TRUSTED_PROXY_RANGES` a la red exacta del proxy.
5. Verificar `/actuator/health/liveness` y `/actuator/health/readiness` antes de enrutar tráfico.
6. El Compose de producción fuerza seeds y Swagger a `false`. El administrador inicial
   debe provisionarse mediante un procedimiento puntual y auditado antes de habilitar
   tráfico; no se conserva una contraseña bootstrap en el servicio normal.
7. Ejecutar los procedimientos de `ops/` sobre staging y guardar la evidencia de
   backup, restauración y rollback.

No publiques directamente PostgreSQL, Redis, MinIO ni herramientas administrativas.
El procedimiento completo está en `docs/production-readiness.md` y `ops/README.md`.

## Almacenamiento de imágenes con MinIO

El backend sube logos, portadas, imágenes de servicios y evidencias de órdenes mediante la API S3 de MinIO.
`STORAGE_MINIO_ENDPOINT` es el endpoint interno de la API S3 (puerto `9000`, nunca la
consola `9001`). `STORAGE_MINIO_PUBLIC_BASE_URL` sirve únicamente recursos públicos y
`STORAGE_MINIO_PRIVATE_DOWNLOAD_BASE_URL` firma evidencias desde un origen HTTPS
separado.

Buckets esperados:

- `business-assets`: logos y portadas públicas.
- `service-images`: imágenes públicas de servicios.
- `repair-order-evidence`: evidencias privadas por defecto, consultadas mediante URL firmada.

Configura las variables descritas en `.env.docker.example`. Los archivos admitidos son
JPG, PNG y WebP de máximo 10 MB. El backend verifica firma, MIME, extensión,
dimensiones y fotogramas; después reencodifica a PNG, elimina metadatos EXIF/GPS,
calcula SHA-256 y usa una llave interna aleatoria. Las evidencias nunca son públicas:
se entregan con URL firmada de 10 minutos y su borrado físico ocurre mediante una cola
durable después del periodo de retención.

## Documentacion interna

- Modelo DB: `docs/database-model.md`
- Esquema DrawDB: `docs/database-schema.drawdb.json`
- Validación modelo vs JPA: `docs/schema-jpa-validation.md`
