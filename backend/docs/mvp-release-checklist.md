# Secuencia de liberación del MVP

1. Consolidar la rama: revisar el working tree, separar migraciones, seguridad, pruebas
   y documentación en commits pequeños y revisables.
2. Ejecutar `./mvnw -B verify` con Docker activo; la prueba PostgreSQL no puede quedar
   omitida en CI.
3. Ejecutar Gitleaks, CodeQL, SBOM y Trivy sin hallazgos críticos aplicables.
4. Desplegar en staging con secretos nuevos, Redis obligatorio y MinIO privado.
5. Ejecutar la matriz de autorización y el flujo operativo completo del piloto.
6. Ensayar backup/restore y rollback, guardar evidencia y activar alertas.
7. Habilitar únicamente el piloto de 1–3 talleres.
8. Mantener fuera del piloto marketplace abierto, suscripciones/comisiones, agenda
   pública y ventas directas no esenciales mediante rutas no publicadas en el cliente.
9. Abrir marketplace solo después de una segunda revisión de privacidad, moderación,
   abuso, carga y soporte.

La evidencia, comandos y criterios de bloqueo del punto 7 están definidos en
`docs/qa-release-evidence.md` y `qa/README.md`.

Condición final: cero P0/P1, CI verde, PostgreSQL/Flyway verificado, restauración y
rollback comprobados, y aceptación del flujo extremo a extremo.

## Evidencias concretas del punto 6

- [ ] `ProductionConfigurationValidator` arranca con secretos reales y rechaza valores ejemplo.
- [ ] Imagen backend desplegada por digest y etiquetas OCI coincidentes con el commit.
- [ ] PostgreSQL, Redis y MinIO sin puertos públicos.
- [ ] Readiness comprueba DB, Redis y MinIO; liveness no depende de servicios externos.
- [ ] Dashboard y alertas para 5xx, latencia, login, rate limit, DB, MinIO, disco y TLS.
- [ ] Backup PostgreSQL cifrado y externo con checksum.
- [ ] Backup de los tres buckets MinIO cifrado y externo con checksum.
- [ ] Restauración PostgreSQL/MinIO cronometrada en staging dentro del RTO.
- [ ] Rollback a imagen anterior por digest y smoke test de readiness.
- [ ] Registro firmado con RPO/RTO, responsables y fecha del ensayo.
