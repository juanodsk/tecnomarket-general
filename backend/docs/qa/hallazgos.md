# Hallazgos de preparación y límites de evidencia

Estos puntos provienen de lectura del código y de la configuración documentada, no de una auditoría remota nueva. No se modificó lógica del backend para construir las pruebas.

1. **Colección anterior reemplazada:** la colección QA activa usa POST para VERIFY EMAIL, cookie `tm_refresh` y valores literales visibles. No conserva environments, scripts de captura ni variables con llaves. `bruno/TECNOMARKET` se mantiene como colección histórica independiente.
2. **Mensaje del rate limit:** el filtro escribe claves como `rate_limit.exceeded` directamente. La aserción funcional exige 429 y Retry-After; la traducción es un defecto de presentación separado, no una prueba fallida de conexión Redis.
3. **Costo del inventario:** InventoryItemService devuelve `unitCost=null` sin INVENTORY_COST_VIEW. InventoryItemResponse no declara exclusión de null y no se encontró configuración global NON_NULL. El caso NO_COST exige omisión como pide el contrato deseado; podría fallar por presencia de null. Registrar esa diferencia como contrato de privacidad, distinguiéndola de exponer un valor financiero real. No relajar silenciosamente el test.
4. **Rate limit de evidencias:** el filtro aplica su política de upload a rutas que terminan `/attachments`; la ruta POST real termina `/attachments/upload`. La prueba de 21 uploads debe evaluar la ruta real; un resultado sin 429 sería una brecha de cobertura que requiere corrección del backend. No se asegura protección del endpoint basándose solo en login.
5. **Roles de técnico:** RepairOrderService exige el código TECHNICIAN para assignedBusinessUserId. Un rol QA con permisos equivalentes y código distinto no sirve para asignación técnica. Los roles QA_ALLOW/QA_DENY se usan para pruebas de permisos, y el rol TECHNICIAN existente para fixtures de reparación.
6. **IDs de aprobaciones:** BusinessRequestResponse no incluye businessId. La colección resuelve el negocio por nombre único después de aprobar y falla si encuentra cero o más de una coincidencia.
7. **Rollback del seed:** los errores de contraseña corta/ciudad ausente del historial no prueban que los fixtures actuales existan. Preflight debe confirmar login y catálogos antes de cualquier caso.
8. **Migraciones y modelo visual:** el contrato de esta entrega parte del checkout con 49 migraciones. La documentación del esquema indica que DrawDB requiere sincronización. Los IDs/reglas se obtienen de Java y Flyway, no del diagrama antiguo.

## Qué se comprueba aquí

- Inventario estático, expansión de catálogos y construcción de casos con dos contextos empresariales visibles.
- Parseo de archivos mediante parser oficial de Bruno, JSON literal, autenticación declarada y ausencia de scripts de mutación.
- Consistencia entre matriz, archivos y rutas del contrato. Archivo de evidencia local: `target/qa/validation.json`.

## Qué permanece pendiente

- Importación visual en la versión de Bruno instalada por el usuario y ejecución en api-dev.
- Entrega de correo real y disponibilidad de usuarios QA verificados.
- Observación de las validaciones de negocio, costos/historial/stock y respuestas reales; los estados iniciales de la matriz no son PASS.
- Cookies con el cliente real, confianza del proxy, pruebas desde varias IP y comportamiento tras restart/réplica.
- Fallos parciales DB/MinIO, retención y eliminación física, restauración y carga: requieren infraestructura o ventana de prueba separada. No se detendrán servicios compartidos automáticamente.

No existe garantía de “cero errores” por generar documentación o miles de casos. La cobertura declarada es de operaciones y variantes identificadas; los escenarios manuales indicados en las guías complementan las peticiones y deben registrarse con evidencia antes de aceptar el MVP.
