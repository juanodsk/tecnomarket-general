# Evidencia y puertas de QA del MVP

## Automatizado en el repositorio

| Riesgo | Evidencia ejecutable | Criterio |
|---|---|---|
| Migraciones | `PostgresFlywayMigrationTest` | V1 a V38 y V39 a V49 sobre PostgreSQL real, sin pendientes |
| Concurrencia de stock | `PostgresFlywayMigrationTest` e `InventoryStockServiceTest` | Dos consumos concurrentes no producen stock negativo |
| Autorización | `BusinessAuthorizationMatrixTest` y pruebas de guards/services | Rol, tenant y sede ajenos reciben denegación |
| Privacidad pública | `PublicMarketplacePrivacyIntegrationTest` | El JSON público contiene únicamente los campos permitidos |
| Sesiones | pruebas de auth, refresh y `security_version` | Replay, revocación, desactivación y cambio de contraseña invalidan sesión |
| Archivos | `MinioStorageServiceTest` y pruebas de adjuntos | Firma, MIME, extensión, píxeles, EXIF, retención y rollback cubiertos |
| Contenedor | `qa/container-smoke.sh` | Perfil prod completo, dependencias reales, headers y endpoints cerrados |
| DAST | `qa/dast-zap.sh` | Sin alertas altas sin aceptar; medias documentadas |
| Carga | `qa/load/k6-mvp.js` | Errores < 1 %, p95 < 1.5 s, p99 < 3 s |

## Evidencia que requiere staging

Para cada candidato se debe adjuntar al registro de liberación:

- commit e imagen por digest;
- salida verde de CI y smoke;
- reportes ZAP y k6, URL y fecha de ejecución;
- resultado del recorrido manual completo: cliente/dispositivo, orden, diagnóstico,
  evidencia, cotización, reparación, repuesto, pruebas y entrega;
- restore y rollback cronometrados, RPO/RTO observado;
- revisión de logs y alertas sin datos personales o secretos.

## Piloto controlado

El piloto empieza con uno a tres talleres, usuarios nominativos, soporte manual y un
canal de incidente definido. Marketplace, suscripciones, comisiones y ventas directas
permanecen deshabilitados. Durante al menos una semana se registran disponibilidad,
5xx, p95, rechazos de autenticación, rate limit, fallos DB/MinIO y tickets operativos.

No se habilita el marketplace abierto hasta completar una segunda revisión de
privacidad, moderación de reseñas/contenido, abuso, capacidad y procedimiento de baja.

## Regla go/no-go

La liberación se bloquea con cualquier P0/P1 abierto, CI o smoke rojo, vulnerabilidad
alta aplicable, cruce tenant/sede, exposición de PII, pérdida/duplicado de stock,
restore o rollback no ensayado, alerta ZAP alta sin resolver o incumplimiento sostenido
de los umbrales de carga.
