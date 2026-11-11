# Validación local de la entrega

Código inventariado: `b04d869f3d73559f83ab5de1503e02473a1aaadf`. Fecha de preparación: 2026-09-12. Esta evidencia corresponde a los artefactos de QA, no a una aprobación del backend.

| Comprobación | Resultado |
|---|---|
| Inventario de controladores | 35 controladores, 183 combinaciones método/ruta únicas |
| Peticiones y matriz | 2.715 casos fuente, 2.853 peticiones manuales y 2.715 rutas principales verificadas |
| Cobertura de estados | 81 pares de estados de orden, 36 pares de cotización y dos condiciones de vencimiento |
| Permisos entre negocios | 63 operaciones con casos de permiso obtenido en A, membresía B inactiva y rol B inactivo |
| Parser de Bruno | Las 2.853 peticiones y todos los `folder.bru` fueron aceptados por `@usebruno/lang` 0.39.0 |
| Solicitudes visibles | Cero expresiones con llaves, cero environments y cero scripts previos/posteriores en la colección manual |
| Matriz CSV | Importación y comparación de cada celda con el registro JSON; 15 columnas |
| PowerShell | Sintaxis del auxiliar de concurrencia comprobada; no ejecutado contra desarrollo |
| Git | `git diff --check` sin errores y colección Bruno confirmada como ignorada |
| HTTP contra api-dev | Cero solicitudes enviadas durante esta preparación |

Estado inicial: ningún caso ha sido ejecutado contra la API. Los casos que dependen de correo deben registrarse como `BLOCKED` si no llega el token. No hay casos marcados `PASS` como si hubieran sido ejecutados contra el despliegue.

El parser verifica compatibilidad sintáctica, no sustituye la importación visual en la versión instalada de Bruno ni la prueba de comportamiento del servidor. Conservar los resultados reales en la copia local de la matriz y registrar defectos con evidencia saneada. Para regeneraciones, el informe vigente del validador se escribe en `target/qa/validation.json`; actualizar este resumen si cambia el contrato o el número de casos.
