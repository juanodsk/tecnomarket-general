# QA manual y ejecutable de TecMarket

La colección local [TECMARKET-QA](../../bruno/TECMARKET-QA/README.md) cubre las 183 operaciones del backend mediante más de 2.800 peticiones organizadas para trabajo humano. No contiene resultados reales: todos comienzan en `NOT_RUN` hasta ejecutarlos contra `https://api-dev.juanodsk.dev`.

## Orden recomendado

1. `00_LEEME_PRIMERO`: instrucciones de uso y convenciones visibles.
2. `01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES`: plataforma, permisos, roles, usuarios y catálogos globales.
3. `02_NEGOCIO_A_REPARACION_COMPLETA/00_FLUJO_GUIADO`: alta y recorrido completo hasta entrega y reseña.
4. `03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/00_FLUJO_GUIADO`: segundo tenant independiente hasta cancelación.
5. `04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES`: cruces A/B, sede, cliente y permisos.
6. `05_IDENTIDAD_SESIONES_Y_RATE_LIMIT`: login, refresh, revocación y abuso.
7. `06_PUBLICO_CLIENTES_Y_MARKETPLACE`: contratos públicos y de cliente.
8. `07_VALIDACIONES_Y_LIMITES`: campos ausentes, nulos, tipos, límites y filtros.
9. `08_ESTADOS_E_IDEMPOTENCIA`: máquina de estados, replay y conflictos.
10. `09_ARCHIVOS_Y_CONCURRENCIA` y `10_FUNCIONES_OPCIONALES`: pruebas especializadas.

## Forma de uso

- La colección no usa `environments`, `activeCase`, `runId`, scripts previos ni sustituciones con `{{...}}`.
- El URL, body, headers, cookie e IDs se ven directamente en cada petición.
- Cambia el bearer token una sola vez en la carpeta de Administrador, Negocio A o Negocio B. Las peticiones normales lo heredan. Los casos de otro actor muestran su token literal en el Auth de la petición.
- Los IDs 100xxx representan A, los 200xxx representan B y los 900xxx son globales o inexistentes. Sustitúyelos con el `data.id` real obtenido en pasos anteriores.
- Antes de enviar, busca visualmente los prefijos `PEGAR_`, `CAMBIAR_` y `EDITAR_`.
- No ejecutes toda la colección: hay casos destructivos, incompatibles y de rate limit.

## Evidencia y estado

Usa [matriz.csv](matriz.csv) para registrar `PASS`, `FAIL`, `BLOCKED` o `NOT_RUN`, junto con fecha, respuesta observada y evidencia. El [catálogo](catalogo.md) documenta cada endpoint y [casos.json](casos.json) contiene la matriz estructurada.

Un HTTP esperado no basta para aprobar mutaciones: comprueba también estado, historial, stock, aislamiento, datos públicos y ausencia de cambios después de un rechazo. Un cruce entre negocios o sedes bloquea la liberación.

## Regenerar sin llamar al backend

```powershell
node qa/generate-bruno-manual.mjs
node qa/validate-api-qa.mjs
git diff --check
```

La generación vuelve a construir únicamente `bruno/TECMARKET-QA`; conserva `bruno/TECNOMARKET`. La validación comprueba sintaxis Bruno/JSON, ausencia de variables ocultas, ausencia de scripts y cobertura de las 183 operaciones. No ejecuta HTTP ni acredita que el despliegue esté aprobado.
