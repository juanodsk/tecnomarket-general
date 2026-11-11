# QA de liberación del MVP

Esta carpeta contiene pruebas operativas que complementan `mvn verify`. Ningún script
incluye credenciales: todos los secretos se generan de forma efímera o se reciben por
variables de entorno.

## Colección Bruno manual

La colección local `bruno/TECMARKET-QA` se genera con valores literales y dos
flujos empresariales independientes. No usa environments, variables con llaves
ni scripts que modifiquen una petición.

```powershell
node qa/generate-bruno-manual.mjs
node qa/validate-api-qa.mjs
```

El primer comando reconstruye solo la colección QA y conserva la colección
histórica `bruno/TECNOMARKET`. El segundo valida los 186 endpoints y las 2.000+
peticiones sin llamar al despliegue.

## Smoke del contenedor

Construye la imagen y valida el perfil `prod` contra PostgreSQL, Redis y MinIO reales:

```bash
docker build --pull -t tecmarket-backend:qa .
bash qa/container-smoke.sh tecmarket-backend:qa
```

El smoke aplica Flyway con un usuario distinto al usuario de ejecución, arranca el
contenedor sin capacidades y con filesystem de solo lectura, y valida readiness,
errores sin detalles, encabezados de seguridad, Swagger desactivado y Prometheus
protegido. Los contenedores y secretos son temporales.

## DAST sobre staging

La prueba es pasiva y está pensada para la URL pública de staging. No ejecutar un
escaneo activo contra producción ni contra sistemas de terceros.

```bash
QA_TARGET_URL=https://staging.example.com bash qa/dast-zap.sh
```

Los reportes HTML, Markdown y JSON quedan en `target/qa/zap`. Una alerta de nivel
alto o una exposición de datos bloquea la liberación; las alertas medias deben ser
triadas y documentadas.

## Carga de lecturas del piloto

Usa una cuenta dedicada de QA con datos sintéticos y sin permisos administrativos:

```bash
docker run --rm -i \
  -e QA_BASE_URL=https://staging.example.com \
  -e QA_EMAIL \
  -e QA_PASSWORD \
  -e QA_BUSINESS_ID \
  -e QA_ORDER_ID \
  -v "$PWD/qa/load:/scripts:ro" \
  grafana/k6:latest@sha256:5221b620a4f874faff6e32ba597aa667c058391fe4898b1c6f6377f062c6cdec \
  run /scripts/k6-mvp.js
```

Por defecto ejecuta dos minutos, diez usuarios virtuales para búsqueda, órdenes y
archivos, y un login por segundo. Los umbrales son menos de 1 % de errores, p95 menor
a 1.5 segundos y p99 menor a 3 segundos. Ajustar gradualmente la carga a la capacidad
objetivo; no probar carga contra producción.

El workflow manual `Staging DAST and load gate` ejecuta estas dos puertas y publica
los reportes como artefactos. La cuenta sintética se configura mediante los secretos
`QA_EMAIL` y `QA_PASSWORD` del environment protegido `staging`; k6 es opcional para
evitar lanzar carga accidentalmente.
