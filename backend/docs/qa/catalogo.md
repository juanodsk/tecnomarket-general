# Catálogo completo de endpoints

Generado del código actual. Los ejemplos requieren sustituir variables; ningún ID se supone fijo. Para ejecutar, comenzar por [README](README.md).

| ID | Método | Ruta | Controlador |
|---|---|---|---|
| [EP001](#ep001) | GET | `/api/v1/parameters/modules` | AppModuleParameterController |
| [EP002](#ep002) | GET | `/api/v1/parameters/modules/{id}` | AppModuleParameterController |
| [EP003](#ep003) | POST | `/api/v1/parameters/modules` | AppModuleParameterController |
| [EP004](#ep004) | PUT | `/api/v1/parameters/modules/{id}` | AppModuleParameterController |
| [EP005](#ep005) | DELETE | `/api/v1/parameters/modules/{id}` | AppModuleParameterController |
| [EP006](#ep006) | GET | `/api/v1/businesses/{businessId}/appointments` | AppointmentController |
| [EP007](#ep007) | GET | `/api/v1/businesses/{businessId}/appointments/{appointmentId}` | AppointmentController |
| [EP008](#ep008) | POST | `/api/v1/businesses/{businessId}/appointments` | AppointmentController |
| [EP009](#ep009) | PUT | `/api/v1/businesses/{businessId}/appointments/{appointmentId}` | AppointmentController |
| [EP010](#ep010) | DELETE | `/api/v1/businesses/{businessId}/appointments/{appointmentId}` | AppointmentController |
| [EP011](#ep011) | PATCH | `/api/v1/businesses/{businessId}/appointments/{appointmentId}/status` | AppointmentController |
| [EP012](#ep012) | POST | `/api/v1/auth/register` | AuthController |
| [EP013](#ep013) | POST | `/api/v1/auth/login` | AuthController |
| [EP014](#ep014) | POST | `/api/v1/auth/verify-email` | AuthController |
| [EP015](#ep015) | POST | `/api/v1/auth/password-reset/request` | AuthController |
| [EP016](#ep016) | POST | `/api/v1/auth/password-reset/confirm` | AuthController |
| [EP017](#ep017) | POST | `/api/v1/auth/password/change` | AuthController |
| [EP018](#ep018) | GET | `/api/v1/auth/me` | AuthController |
| [EP019](#ep019) | GET | `/api/v1/auth/me/permissions` | AuthController |
| [EP020](#ep020) | POST | `/api/v1/auth/refresh` | AuthController |
| [EP021](#ep021) | POST | `/api/v1/auth/logout` | AuthController |
| [EP022](#ep022) | GET | `/api/v1/auth/sessions` | AuthController |
| [EP023](#ep023) | DELETE | `/api/v1/auth/sessions/{sessionId}` | AuthController |
| [EP024](#ep024) | DELETE | `/api/v1/auth/sessions` | AuthController |
| [EP025](#ep025) | GET | `/api/v1/businesses/{businessId}/branches` | BusinessBranchController |
| [EP026](#ep026) | GET | `/api/v1/businesses/{businessId}/branches/{branchId}` | BusinessBranchController |
| [EP027](#ep027) | POST | `/api/v1/businesses/{businessId}/branches` | BusinessBranchController |
| [EP028](#ep028) | PUT | `/api/v1/businesses/{businessId}/branches/{branchId}` | BusinessBranchController |
| [EP029](#ep029) | DELETE | `/api/v1/businesses/{businessId}/branches/{branchId}` | BusinessBranchController |
| [EP030](#ep030) | GET | `/api/v1/businesses/{businessId}/catalogs/{catalogType}` | BusinessCatalogController |
| [EP031](#ep031) | GET | `/api/v1/businesses/{businessId}/catalogs/{catalogType}/{itemId}` | BusinessCatalogController |
| [EP032](#ep032) | POST | `/api/v1/businesses/{businessId}/catalogs/{catalogType}` | BusinessCatalogController |
| [EP033](#ep033) | PUT | `/api/v1/businesses/{businessId}/catalogs/{catalogType}/{itemId}` | BusinessCatalogController |
| [EP034](#ep034) | DELETE | `/api/v1/businesses/{businessId}/catalogs/{catalogType}/{itemId}` | BusinessCatalogController |
| [EP035](#ep035) | GET | `/api/v1/businesses` | BusinessController |
| [EP036](#ep036) | GET | `/api/v1/businesses/my` | BusinessController |
| [EP037](#ep037) | GET | `/api/v1/businesses/{businessId}/me/context` | BusinessController |
| [EP038](#ep038) | GET | `/api/v1/businesses/{id}` | BusinessController |
| [EP039](#ep039) | POST | `/api/v1/businesses` | BusinessController |
| [EP040](#ep040) | PUT | `/api/v1/businesses/{id}` | BusinessController |
| [EP041](#ep041) | POST | `/api/v1/businesses/{id}/logo` | BusinessController |
| [EP042](#ep042) | POST | `/api/v1/businesses/{id}/cover` | BusinessController |
| [EP043](#ep043) | DELETE | `/api/v1/businesses/{id}` | BusinessController |
| [EP044](#ep044) | GET | `/api/v1/businesses/{businessId}/services` | BusinessOfferingController |
| [EP045](#ep045) | GET | `/api/v1/businesses/{businessId}/services/{offeringId}` | BusinessOfferingController |
| [EP046](#ep046) | POST | `/api/v1/businesses/{businessId}/services` | BusinessOfferingController |
| [EP047](#ep047) | PUT | `/api/v1/businesses/{businessId}/services/{offeringId}` | BusinessOfferingController |
| [EP048](#ep048) | POST | `/api/v1/businesses/{businessId}/services/{offeringId}/image` | BusinessOfferingController |
| [EP049](#ep049) | DELETE | `/api/v1/businesses/{businessId}/services/{offeringId}` | BusinessOfferingController |
| [EP050](#ep050) | GET | `/api/v1/businesses/{businessId}/quality-tests` | BusinessQualityTestController |
| [EP051](#ep051) | GET | `/api/v1/businesses/{businessId}/quality-tests/{qualityTestId}` | BusinessQualityTestController |
| [EP052](#ep052) | POST | `/api/v1/businesses/{businessId}/quality-tests` | BusinessQualityTestController |
| [EP053](#ep053) | PUT | `/api/v1/businesses/{businessId}/quality-tests/{qualityTestId}` | BusinessQualityTestController |
| [EP054](#ep054) | DELETE | `/api/v1/businesses/{businessId}/quality-tests/{qualityTestId}` | BusinessQualityTestController |
| [EP055](#ep055) | POST | `/api/v1/business/requests` | BusinessRequestController |
| [EP056](#ep056) | GET | `/api/v1/business/requests/my` | BusinessRequestController |
| [EP057](#ep057) | GET | `/api/v1/business/requests/pending` | BusinessRequestController |
| [EP058](#ep058) | PUT | `/api/v1/business/requests/{id}/approve` | BusinessRequestController |
| [EP059](#ep059) | PUT | `/api/v1/business/requests/{id}/reject` | BusinessRequestController |
| [EP060](#ep060) | POST | `/api/v1/businesses/{businessId}/reviews` | BusinessReviewController |
| [EP061](#ep061) | GET | `/api/v1/businesses/{businessId}/reviews` | BusinessReviewController |
| [EP062](#ep062) | GET | `/api/v1/businesses/{businessId}/schedules` | BusinessScheduleController |
| [EP063](#ep063) | GET | `/api/v1/businesses/{businessId}/schedules/{scheduleId}` | BusinessScheduleController |
| [EP064](#ep064) | POST | `/api/v1/businesses/{businessId}/schedules` | BusinessScheduleController |
| [EP065](#ep065) | PUT | `/api/v1/businesses/{businessId}/schedules/{scheduleId}` | BusinessScheduleController |
| [EP066](#ep066) | DELETE | `/api/v1/businesses/{businessId}/schedules/{scheduleId}` | BusinessScheduleController |
| [EP067](#ep067) | GET | `/api/v1/business-subscriptions` | BusinessSubscriptionController |
| [EP068](#ep068) | GET | `/api/v1/business-subscriptions/{id}` | BusinessSubscriptionController |
| [EP069](#ep069) | GET | `/api/v1/businesses/{businessId}/subscription` | BusinessSubscriptionController |
| [EP070](#ep070) | POST | `/api/v1/businesses/{businessId}/subscription` | BusinessSubscriptionController |
| [EP071](#ep071) | PUT | `/api/v1/business-subscriptions/{id}` | BusinessSubscriptionController |
| [EP072](#ep072) | DELETE | `/api/v1/business-subscriptions/{id}` | BusinessSubscriptionController |
| [EP073](#ep073) | GET | `/api/v1/business-subscriptions/{id}/usage` | BusinessSubscriptionController |
| [EP074](#ep074) | POST | `/api/v1/business-subscriptions/{id}/usage/recalculate` | BusinessSubscriptionController |
| [EP075](#ep075) | GET | `/api/v1/businesses/{businessId}/users` | BusinessUserController |
| [EP076](#ep076) | GET | `/api/v1/businesses/{businessId}/users/role-options` | BusinessUserController |
| [EP077](#ep077) | GET | `/api/v1/businesses/{businessId}/users/available-users` | BusinessUserController |
| [EP078](#ep078) | GET | `/api/v1/businesses/{businessId}/users/technicians` | BusinessUserController |
| [EP079](#ep079) | POST | `/api/v1/businesses/{businessId}/users` | BusinessUserController |
| [EP080](#ep080) | PUT | `/api/v1/businesses/{businessId}/users/{businessUserId}` | BusinessUserController |
| [EP081](#ep081) | DELETE | `/api/v1/businesses/{businessId}/users/{businessUserId}` | BusinessUserController |
| [EP082](#ep082) | GET | `/api/v1/parameters/cities` | CityParameterController |
| [EP083](#ep083) | GET | `/api/v1/parameters/cities/{id}` | CityParameterController |
| [EP084](#ep084) | GET | `/api/v1/parameters/countries/{countryId}/cities` | CityParameterController |
| [EP085](#ep085) | GET | `/api/v1/parameters/departments/{departmentId}/cities` | CityParameterController |
| [EP086](#ep086) | POST | `/api/v1/parameters/cities` | CityParameterController |
| [EP087](#ep087) | PUT | `/api/v1/parameters/cities/{id}` | CityParameterController |
| [EP088](#ep088) | DELETE | `/api/v1/parameters/cities/{id}` | CityParameterController |
| [EP089](#ep089) | GET | `/api/v1/businesses/{businessId}/commission-settlements/technician-summaries` | CommissionSettlementController |
| [EP090](#ep090) | GET | `/api/v1/businesses/{businessId}/commission-settlements/eligible-orders` | CommissionSettlementController |
| [EP091](#ep091) | POST | `/api/v1/businesses/{businessId}/commission-settlements` | CommissionSettlementController |
| [EP092](#ep092) | GET | `/api/v1/businesses/{businessId}/commission-settlements` | CommissionSettlementController |
| [EP093](#ep093) | GET | `/api/v1/businesses/{businessId}/commission-settlements/{settlementId}` | CommissionSettlementController |
| [EP094](#ep094) | PATCH | `/api/v1/businesses/{businessId}/commission-settlements/{settlementId}/status` | CommissionSettlementController |
| [EP095](#ep095) | GET | `/api/v1/parameters/countries` | CountryParameterController |
| [EP096](#ep096) | GET | `/api/v1/parameters/countries/{id}` | CountryParameterController |
| [EP097](#ep097) | POST | `/api/v1/parameters/countries` | CountryParameterController |
| [EP098](#ep098) | PUT | `/api/v1/parameters/countries/{id}` | CountryParameterController |
| [EP099](#ep099) | DELETE | `/api/v1/parameters/countries/{id}` | CountryParameterController |
| [EP100](#ep100) | GET | `/api/v1/businesses/{businessId}/customer-devices` | CustomerDeviceController |
| [EP101](#ep101) | GET | `/api/v1/businesses/{businessId}/customer-devices/{customerDeviceId}` | CustomerDeviceController |
| [EP102](#ep102) | POST | `/api/v1/businesses/{businessId}/customer-devices` | CustomerDeviceController |
| [EP103](#ep103) | PUT | `/api/v1/businesses/{businessId}/customer-devices/{customerDeviceId}` | CustomerDeviceController |
| [EP104](#ep104) | DELETE | `/api/v1/businesses/{businessId}/customer-devices/{customerDeviceId}` | CustomerDeviceController |
| [EP105](#ep105) | GET | `/api/v1/me/repair-orders/{orderNumber}` | CustomerRepairOrderController |
| [EP106](#ep106) | GET | `/api/v1/parameters/departments` | DepartmentParameterController |
| [EP107](#ep107) | GET | `/api/v1/parameters/departments/{id}` | DepartmentParameterController |
| [EP108](#ep108) | GET | `/api/v1/parameters/countries/{countryId}/departments` | DepartmentParameterController |
| [EP109](#ep109) | POST | `/api/v1/parameters/departments` | DepartmentParameterController |
| [EP110](#ep110) | PUT | `/api/v1/parameters/departments/{id}` | DepartmentParameterController |
| [EP111](#ep111) | DELETE | `/api/v1/parameters/departments/{id}` | DepartmentParameterController |
| [EP112](#ep112) | GET | `/api/v1/parameters/document-types` | DocumentTypeParameterController |
| [EP113](#ep113) | GET | `/api/v1/parameters/document-types/{id}` | DocumentTypeParameterController |
| [EP114](#ep114) | POST | `/api/v1/parameters/document-types` | DocumentTypeParameterController |
| [EP115](#ep115) | PUT | `/api/v1/parameters/document-types/{id}` | DocumentTypeParameterController |
| [EP116](#ep116) | DELETE | `/api/v1/parameters/document-types/{id}` | DocumentTypeParameterController |
| [EP117](#ep117) | GET | `/api/v1/businesses/{businessId}/inventory-items` | InventoryItemController |
| [EP118](#ep118) | GET | `/api/v1/businesses/{businessId}/inventory-items/{inventoryItemId}` | InventoryItemController |
| [EP119](#ep119) | POST | `/api/v1/businesses/{businessId}/inventory-items` | InventoryItemController |
| [EP120](#ep120) | PUT | `/api/v1/businesses/{businessId}/inventory-items/{inventoryItemId}` | InventoryItemController |
| [EP121](#ep121) | DELETE | `/api/v1/businesses/{businessId}/inventory-items/{inventoryItemId}` | InventoryItemController |
| [EP122](#ep122) | GET | `/api/v1/parameters/permissions` | PermissionParameterController |
| [EP123](#ep123) | GET | `/api/v1/parameters/permissions/{id}` | PermissionParameterController |
| [EP124](#ep124) | GET | `/api/v1/parameters/modules/{moduleId}/permissions` | PermissionParameterController |
| [EP125](#ep125) | POST | `/api/v1/parameters/permissions` | PermissionParameterController |
| [EP126](#ep126) | PUT | `/api/v1/parameters/permissions/{id}` | PermissionParameterController |
| [EP127](#ep127) | DELETE | `/api/v1/parameters/permissions/{id}` | PermissionParameterController |
| [EP128](#ep128) | GET | `/api/v1/public/locations/countries` | PublicLocationController |
| [EP129](#ep129) | GET | `/api/v1/public/locations/countries/{countryId}/departments` | PublicLocationController |
| [EP130](#ep130) | GET | `/api/v1/public/locations/departments/{departmentId}/cities` | PublicLocationController |
| [EP131](#ep131) | GET | `/api/v1/public/businesses` | PublicMarketplaceController |
| [EP132](#ep132) | GET | `/api/v1/public/businesses/{businessId}` | PublicMarketplaceController |
| [EP133](#ep133) | GET | `/api/v1/public/businesses/{businessId}/reviews` | PublicMarketplaceController |
| [EP134](#ep134) | GET | `/api/v1/businesses/{businessId}/quality-checklists/{ticketCode}` | QualityChecklistController |
| [EP135](#ep135) | PUT | `/api/v1/businesses/{businessId}/quality-checklists/{ticketCode}` | QualityChecklistController |
| [EP136](#ep136) | DELETE | `/api/v1/businesses/{businessId}/quality-checklists/{ticketCode}` | QualityChecklistController |
| [EP137](#ep137) | POST | `/api/v1/businesses/{businessId}/quotations` | QuotationController |
| [EP138](#ep138) | GET | `/api/v1/businesses/{businessId}/quotations/{quotationId}` | QuotationController |
| [EP139](#ep139) | PATCH | `/api/v1/businesses/{businessId}/quotations/{quotationId}/status` | QuotationController |
| [EP140](#ep140) | GET | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/quotations` | QuotationController |
| [EP141](#ep141) | GET | `/api/v1/businesses/{businessId}/commissions` | RepairOrderCommissionController |
| [EP142](#ep142) | GET | `/api/v1/businesses/{businessId}/commissions/{commissionId}` | RepairOrderCommissionController |
| [EP143](#ep143) | GET | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/commission` | RepairOrderCommissionController |
| [EP144](#ep144) | POST | `/api/v1/businesses/{businessId}/repair-orders/quick` | RepairOrderController |
| [EP145](#ep145) | POST | `/api/v1/businesses/{businessId}/repair-orders` | RepairOrderController |
| [EP146](#ep146) | GET | `/api/v1/businesses/{businessId}/repair-orders` | RepairOrderController |
| [EP147](#ep147) | PATCH | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/status` | RepairOrderController |
| [EP148](#ep148) | PATCH | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/financials` | RepairOrderController |
| [EP149](#ep149) | PATCH | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/diagnosis` | RepairOrderController |
| [EP150](#ep150) | PATCH | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/assignment` | RepairOrderController |
| [EP151](#ep151) | GET | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/history` | RepairOrderController |
| [EP152](#ep152) | GET | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/quality-checklist` | RepairOrderController |
| [EP153](#ep153) | PUT | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/quality-checklist` | RepairOrderController |
| [EP154](#ep154) | DELETE | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/quality-checklist` | RepairOrderController |
| [EP155](#ep155) | GET | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/attachments` | RepairOrderController |
| [EP156](#ep156) | POST | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/attachments/upload` | RepairOrderController |
| [EP157](#ep157) | DELETE | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/attachments/{attachmentId}` | RepairOrderController |
| [EP158](#ep158) | GET | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/parts` | RepairOrderController |
| [EP159](#ep159) | POST | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/parts` | RepairOrderController |
| [EP160](#ep160) | PUT | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/parts/{partId}` | RepairOrderController |
| [EP161](#ep161) | DELETE | `/api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/parts/{partId}` | RepairOrderController |
| [EP162](#ep162) | GET | `/api/v1/parameters/roles` | RoleParameterController |
| [EP163](#ep163) | GET | `/api/v1/parameters/roles/{id}` | RoleParameterController |
| [EP164](#ep164) | POST | `/api/v1/parameters/roles` | RoleParameterController |
| [EP165](#ep165) | PUT | `/api/v1/parameters/roles/{id}` | RoleParameterController |
| [EP166](#ep166) | DELETE | `/api/v1/parameters/roles/{id}` | RoleParameterController |
| [EP167](#ep167) | GET | `/api/v1/parameters/roles/{roleId}/permissions` | RolePermissionParameterController |
| [EP168](#ep168) | GET | `/api/v1/parameters/roles/{roleId}/permissions/assigned` | RolePermissionParameterController |
| [EP169](#ep169) | PUT | `/api/v1/parameters/roles/{roleId}/permissions` | RolePermissionParameterController |
| [EP170](#ep170) | GET | `/api/v1/businesses/{businessId}/sales` | SaleController |
| [EP171](#ep171) | GET | `/api/v1/businesses/{businessId}/sales/{saleId}` | SaleController |
| [EP172](#ep172) | GET | `/api/v1/businesses/{businessId}/sales/{saleId}/items` | SaleController |
| [EP173](#ep173) | POST | `/api/v1/businesses/{businessId}/sales` | SaleController |
| [EP174](#ep174) | GET | `/api/v1/businesses/{businessId}/stock-movements` | StockMovementController |
| [EP175](#ep175) | POST | `/api/v1/businesses/{businessId}/stock-movements` | StockMovementController |
| [EP176](#ep176) | GET | `/api/v1/subscription-plans` | SubscriptionPlanController |
| [EP177](#ep177) | GET | `/api/v1/subscription-plans/{id}` | SubscriptionPlanController |
| [EP178](#ep178) | POST | `/api/v1/subscription-plans` | SubscriptionPlanController |
| [EP179](#ep179) | PUT | `/api/v1/subscription-plans/{id}` | SubscriptionPlanController |
| [EP180](#ep180) | DELETE | `/api/v1/subscription-plans/{id}` | SubscriptionPlanController |
| [EP181](#ep181) | GET | `/api/v1/parameters/users` | UserParameterController |
| [EP182](#ep182) | GET | `/api/v1/parameters/users/{id}` | UserParameterController |
| [EP183](#ep183) | PATCH | `/api/v1/parameters/users/{id}/status` | UserParameterController |
| [EP184](#ep184) | DELETE | `/api/v1/parameters/users/{id}/sessions` | UserParameterController |
| [EP185](#ep185) | GET | `/api/v1/parameters/users/{userId}/roles` | UserRoleParameterController |
| [EP186](#ep186) | PUT | `/api/v1/parameters/users/{userId}/roles` | UserRoleParameterController |

## EP001

`GET /api/v1/parameters/modules` — List application modules

Fuente: [AppModuleParameterController:39](../../src/main/java/com/tecmarket/backend/parameter/controller/AppModuleParameterController.java#L39). Método Java: `findAll`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<AppModuleResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|


Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP001-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/APP MODULE/EP001-OK - Contrato válido.bru) · [EP001-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APP MODULE/EP001-NO_TOKEN - Sin autenticación.bru) · [EP001-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APP MODULE/EP001-INVALID_TOKEN - Token inválido.bru) · [EP001-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APP MODULE/EP001-REVOKED - Token previamente revocado.bru) · [EP001-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APP MODULE/EP001-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP002

`GET /api/v1/parameters/modules/{id}` — Find application module by ID

Fuente: [AppModuleParameterController:48](../../src/main/java/com/tecmarket/backend/parameter/controller/AppModuleParameterController.java#L48). Método Java: `findById`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<AppModuleResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP002-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/APP MODULE/EP002-OK - Contrato válido.bru) · [EP002-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APP MODULE/EP002-NO_TOKEN - Sin autenticación.bru) · [EP002-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APP MODULE/EP002-INVALID_TOKEN - Token inválido.bru) · [EP002-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APP MODULE/EP002-REVOKED - Token previamente revocado.bru) · [EP002-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APP MODULE/EP002-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP002-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP002-NOT_FOUND - ID inexistente comprobado.bru)

## EP003

`POST /api/v1/parameters/modules` — Create application module

Fuente: [AppModuleParameterController:57](../../src/main/java/com/tecmarket/backend/parameter/controller/AppModuleParameterController.java#L57). Método Java: `create`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<AppModuleResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| request | RequestBody | CreateAppModuleRequest | @Valid @RequestBody CreateAppModuleRequest request |

Body completo (CreateAppModuleRequest):

```json
{
  "code": "{{qaCode}}",
  "name": "Usuario QA"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "code": "{{qaCode}}",
  "name": "Usuario QA"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| code | String | sí | @NotBlank @Size(max = 50) |
| name | String | sí | @NotBlank @Size(max = 100) |

Casos Bruno: [EP003-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/APP MODULE/EP003-OK - Contrato válido.bru) · [EP003-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APP MODULE/EP003-NO_TOKEN - Sin autenticación.bru) · [EP003-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APP MODULE/EP003-INVALID_TOKEN - Token inválido.bru) · [EP003-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APP MODULE/EP003-REVOKED - Token previamente revocado.bru) · [EP003-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APP MODULE/EP003-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP003-code_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP003-code_MISSING - code- ausente.bru) · [EP003-code_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP003-code_NULL - code- null.bru) · [EP003-code_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP003-code_EMPTY - code- EMPTY.bru) · [EP003-code_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP003-code_BLANK - code- BLANK.bru) · [EP003-code_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP003-code_MAX_PLUS_1 - code- 51 caracteres.bru) · [EP003-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP003-name_MISSING - name- ausente.bru) · [EP003-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP003-name_NULL - name- null.bru) · [EP003-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP003-name_EMPTY - name- EMPTY.bru) · [EP003-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP003-name_BLANK - name- BLANK.bru) · [EP003-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP003-name_MAX_PLUS_1 - name- 101 caracteres.bru)

## EP004

`PUT /api/v1/parameters/modules/{id}` — Update application module

Fuente: [AppModuleParameterController:68](../../src/main/java/com/tecmarket/backend/parameter/controller/AppModuleParameterController.java#L68). Método Java: `update`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<AppModuleResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |
| request | RequestBody | UpdateAppModuleRequest | @Valid @RequestBody UpdateAppModuleRequest request |

Body completo (UpdateAppModuleRequest):

```json
{
  "name": "Usuario QA",
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "name": "Usuario QA"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| name | String | sí | @NotBlank @Size(max = 100) |
| active | Boolean | no |  |

Casos Bruno: [EP004-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/APP MODULE/EP004-OK - Contrato válido.bru) · [EP004-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APP MODULE/EP004-NO_TOKEN - Sin autenticación.bru) · [EP004-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APP MODULE/EP004-INVALID_TOKEN - Token inválido.bru) · [EP004-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APP MODULE/EP004-REVOKED - Token previamente revocado.bru) · [EP004-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APP MODULE/EP004-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP004-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP004-name_MISSING - name- ausente.bru) · [EP004-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP004-name_NULL - name- null.bru) · [EP004-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP004-name_EMPTY - name- EMPTY.bru) · [EP004-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP004-name_BLANK - name- BLANK.bru) · [EP004-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP004-name_MAX_PLUS_1 - name- 101 caracteres.bru) · [EP004-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APP MODULE/EP004-active_TYPE - active- tipo incompatible.bru)

## EP005

`DELETE /api/v1/parameters/modules/{id}` — Disable application module

Fuente: [AppModuleParameterController:79](../../src/main/java/com/tecmarket/backend/parameter/controller/AppModuleParameterController.java#L79). Método Java: `delete`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP005-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/APP MODULE/EP005-OK - Contrato válido.bru) · [EP005-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APP MODULE/EP005-NO_TOKEN - Sin autenticación.bru) · [EP005-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APP MODULE/EP005-INVALID_TOKEN - Token inválido.bru) · [EP005-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APP MODULE/EP005-REVOKED - Token previamente revocado.bru) · [EP005-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APP MODULE/EP005-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP006

`GET /api/v1/businesses/{businessId}/appointments` — List appointments by business

Fuente: [AppointmentController:40](../../src/main/java/com/tecmarket/backend/appointment/controller/AppointmentController.java#L40). Método Java: `findAll`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('APPOINTMENT_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<AppointmentResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP006-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/APPOINTMENT/EP006-OK - Contrato válido.bru) · [EP006-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP006-NO_TOKEN - Sin autenticación.bru) · [EP006-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP006-INVALID_TOKEN - Token inválido.bru) · [EP006-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP006-REVOKED - Token previamente revocado.bru) · [EP006-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP006-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP006-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP006-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP006-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP006-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP006-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP006-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP006-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP006-QUERY_0_Min - Query 0- límite inválido.bru) · [EP006-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP006-QUERY_20_Min - Query 20- límite inválido.bru) · [EP006-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP006-QUERY_20_Max - Query 20- límite inválido.bru) · [EP006-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP006-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP006-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP006-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP006-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP006-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP007

`GET /api/v1/businesses/{businessId}/appointments/{appointmentId}` — Find appointment by id

Fuente: [AppointmentController:53](../../src/main/java/com/tecmarket/backend/appointment/controller/AppointmentController.java#L53). Método Java: `findById`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('APPOINTMENT_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<AppointmentResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| appointmentId | PathVariable | Integer | @PathVariable Integer appointmentId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP007-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/APPOINTMENT/EP007-OK - Contrato válido.bru) · [EP007-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP007-NO_TOKEN - Sin autenticación.bru) · [EP007-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP007-INVALID_TOKEN - Token inválido.bru) · [EP007-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP007-REVOKED - Token previamente revocado.bru) · [EP007-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP007-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP007-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP007-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP007-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP007-NOT_FOUND - ID inexistente comprobado.bru) · [EP007-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP007-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP007-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP007-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP007-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP007-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP008

`POST /api/v1/businesses/{businessId}/appointments` — Create appointment

Fuente: [AppointmentController:64](../../src/main/java/com/tecmarket/backend/appointment/controller/AppointmentController.java#L64). Método Java: `create`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('APPOINTMENT_CREATE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<AppointmentResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| request | RequestBody | CreateAppointmentRequest | @Valid @RequestBody CreateAppointmentRequest request |

Body completo (CreateAppointmentRequest):

```json
{
  "branchId": "{{branchId}}",
  "customerId": "{{customerId}}",
  "customerDeviceId": "{{customerDeviceId}}",
  "businessServiceId": "{{businessServiceId}}",
  "statusId": "{{statusId}}",
  "scheduledAt": "{{scheduledAt}}",
  "problemDescription": "El equipo no enciende y presenta olor a quemado."
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "branchId": "{{branchId}}",
  "customerId": "{{customerId}}",
  "customerDeviceId": "{{customerDeviceId}}",
  "businessServiceId": "{{businessServiceId}}",
  "statusId": "{{statusId}}",
  "scheduledAt": "{{scheduledAt}}",
  "problemDescription": "El equipo no enciende y presenta olor a quemado."
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| branchId | Integer | sí | @NotNull |
| customerId | Integer | sí | @NotNull |
| customerDeviceId | Integer | sí | @NotNull |
| businessServiceId | Integer | sí | @NotNull |
| statusId | Integer | sí | @NotNull |
| scheduledAt | LocalDateTime | sí | @NotNull |
| problemDescription | String | sí | @NotBlank @Size(max = 200) |

Casos Bruno: [EP008-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/APPOINTMENT/EP008-OK - Contrato válido.bru) · [EP008-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP008-NO_TOKEN - Sin autenticación.bru) · [EP008-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP008-INVALID_TOKEN - Token inválido.bru) · [EP008-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP008-REVOKED - Token previamente revocado.bru) · [EP008-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP008-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP008-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP008-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP008-branchId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-branchId_MISSING - branchId- ausente.bru) · [EP008-branchId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-branchId_NULL - branchId- null.bru) · [EP008-branchId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-branchId_TYPE - branchId- tipo incompatible.bru) · [EP008-customerId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-customerId_MISSING - customerId- ausente.bru) · [EP008-customerId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-customerId_NULL - customerId- null.bru) · [EP008-customerId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-customerId_TYPE - customerId- tipo incompatible.bru) · [EP008-customerDeviceId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-customerDeviceId_MISSING - customerDeviceId- ausente.bru) · [EP008-customerDeviceId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-customerDeviceId_NULL - customerDeviceId- null.bru) · [EP008-customerDeviceId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-customerDeviceId_TYPE - customerDeviceId- tipo incompatible.bru) · [EP008-businessServiceId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-businessServiceId_MISSING - businessServiceId- ausente.bru) · [EP008-businessServiceId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-businessServiceId_NULL - businessServiceId- null.bru) · [EP008-businessServiceId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-businessServiceId_TYPE - businessServiceId- tipo incompatible.bru) · [EP008-statusId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-statusId_MISSING - statusId- ausente.bru) · [EP008-statusId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-statusId_NULL - statusId- null.bru) · [EP008-statusId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-statusId_TYPE - statusId- tipo incompatible.bru) · [EP008-scheduledAt_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-scheduledAt_MISSING - scheduledAt- ausente.bru) · [EP008-scheduledAt_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-scheduledAt_NULL - scheduledAt- null.bru) · [EP008-scheduledAt_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-scheduledAt_TYPE - scheduledAt- tipo incompatible.bru) · [EP008-problemDescription_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-problemDescription_MISSING - problemDescription- ausente.bru) · [EP008-problemDescription_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-problemDescription_NULL - problemDescription- null.bru) · [EP008-problemDescription_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-problemDescription_EMPTY - problemDescription- EMPTY.bru) · [EP008-problemDescription_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-problemDescription_BLANK - problemDescription- BLANK.bru) · [EP008-problemDescription_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP008-problemDescription_MAX_PLUS_1 - problemDescription- 201 caracteres.bru) · [EP008-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP008-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP008-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP008-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP008-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP008-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP009

`PUT /api/v1/businesses/{businessId}/appointments/{appointmentId}` — Update appointment

Fuente: [AppointmentController:76](../../src/main/java/com/tecmarket/backend/appointment/controller/AppointmentController.java#L76). Método Java: `update`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('APPOINTMENT_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<AppointmentResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| appointmentId | PathVariable | Integer | @PathVariable Integer appointmentId |
| request | RequestBody | UpdateAppointmentRequest | @Valid @RequestBody UpdateAppointmentRequest request |

Body completo (UpdateAppointmentRequest):

```json
{
  "customerId": "{{customerId}}",
  "customerDeviceId": "{{customerDeviceId}}",
  "businessServiceId": "{{businessServiceId}}",
  "statusId": "{{statusId}}",
  "scheduledAt": "{{scheduledAt}}",
  "problemDescription": "No enciende, posible daño en tarjeta lógica.",
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "customerId": "{{customerId}}",
  "customerDeviceId": "{{customerDeviceId}}",
  "businessServiceId": "{{businessServiceId}}",
  "statusId": "{{statusId}}",
  "scheduledAt": "{{scheduledAt}}",
  "problemDescription": "No enciende, posible daño en tarjeta lógica."
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| customerId | Integer | sí | @NotNull |
| customerDeviceId | Integer | sí | @NotNull |
| businessServiceId | Integer | sí | @NotNull |
| statusId | Integer | sí | @NotNull |
| scheduledAt | LocalDateTime | sí | @NotNull |
| problemDescription | String | sí | @NotBlank @Size(max = 200) |
| active | Boolean | no |  |

Casos Bruno: [EP009-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/APPOINTMENT/EP009-OK - Contrato válido.bru) · [EP009-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP009-NO_TOKEN - Sin autenticación.bru) · [EP009-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP009-INVALID_TOKEN - Token inválido.bru) · [EP009-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP009-REVOKED - Token previamente revocado.bru) · [EP009-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP009-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP009-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP009-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP009-customerId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-customerId_MISSING - customerId- ausente.bru) · [EP009-customerId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-customerId_NULL - customerId- null.bru) · [EP009-customerId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-customerId_TYPE - customerId- tipo incompatible.bru) · [EP009-customerDeviceId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-customerDeviceId_MISSING - customerDeviceId- ausente.bru) · [EP009-customerDeviceId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-customerDeviceId_NULL - customerDeviceId- null.bru) · [EP009-customerDeviceId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-customerDeviceId_TYPE - customerDeviceId- tipo incompatible.bru) · [EP009-businessServiceId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-businessServiceId_MISSING - businessServiceId- ausente.bru) · [EP009-businessServiceId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-businessServiceId_NULL - businessServiceId- null.bru) · [EP009-businessServiceId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-businessServiceId_TYPE - businessServiceId- tipo incompatible.bru) · [EP009-statusId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-statusId_MISSING - statusId- ausente.bru) · [EP009-statusId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-statusId_NULL - statusId- null.bru) · [EP009-statusId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-statusId_TYPE - statusId- tipo incompatible.bru) · [EP009-scheduledAt_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-scheduledAt_MISSING - scheduledAt- ausente.bru) · [EP009-scheduledAt_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-scheduledAt_NULL - scheduledAt- null.bru) · [EP009-scheduledAt_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-scheduledAt_TYPE - scheduledAt- tipo incompatible.bru) · [EP009-problemDescription_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-problemDescription_MISSING - problemDescription- ausente.bru) · [EP009-problemDescription_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-problemDescription_NULL - problemDescription- null.bru) · [EP009-problemDescription_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-problemDescription_EMPTY - problemDescription- EMPTY.bru) · [EP009-problemDescription_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-problemDescription_BLANK - problemDescription- BLANK.bru) · [EP009-problemDescription_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-problemDescription_MAX_PLUS_1 - problemDescription- 201 caracteres.bru) · [EP009-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP009-active_TYPE - active- tipo incompatible.bru) · [EP009-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP009-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP009-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP009-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP009-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP009-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP010

`DELETE /api/v1/businesses/{businessId}/appointments/{appointmentId}` — Disable appointment

Fuente: [AppointmentController:88](../../src/main/java/com/tecmarket/backend/appointment/controller/AppointmentController.java#L88). Método Java: `delete`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('APPOINTMENT_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| appointmentId | PathVariable | Integer | @PathVariable Integer appointmentId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP010-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/APPOINTMENT/EP010-OK - Contrato válido.bru) · [EP010-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP010-NO_TOKEN - Sin autenticación.bru) · [EP010-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP010-INVALID_TOKEN - Token inválido.bru) · [EP010-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP010-REVOKED - Token previamente revocado.bru) · [EP010-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP010-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP010-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP010-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP010-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP010-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP010-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP010-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP010-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP010-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP011

`PATCH /api/v1/businesses/{businessId}/appointments/{appointmentId}/status` — Change appointment status

Fuente: [AppointmentController:99](../../src/main/java/com/tecmarket/backend/appointment/controller/AppointmentController.java#L99). Método Java: `updateStatus`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('APPOINTMENT_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<AppointmentResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| appointmentId | PathVariable | Integer | @PathVariable Integer appointmentId |
| request | RequestBody | UpdateAppointmentStatusRequest | @Valid @RequestBody UpdateAppointmentStatusRequest request |

Body completo (UpdateAppointmentStatusRequest):

```json
{
  "statusId": "{{statusId}}"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "statusId": "{{statusId}}"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| statusId | Integer | sí | @NotNull |

Casos Bruno: [EP011-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/APPOINTMENT/EP011-OK - Contrato válido.bru) · [EP011-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP011-NO_TOKEN - Sin autenticación.bru) · [EP011-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP011-INVALID_TOKEN - Token inválido.bru) · [EP011-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/APPOINTMENT/EP011-REVOKED - Token previamente revocado.bru) · [EP011-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP011-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP011-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP011-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP011-statusId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP011-statusId_MISSING - statusId- ausente.bru) · [EP011-statusId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP011-statusId_NULL - statusId- null.bru) · [EP011-statusId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/APPOINTMENT/EP011-statusId_TYPE - statusId- tipo incompatible.bru) · [EP011-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP011-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP011-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP011-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP011-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/APPOINTMENT/EP011-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP012

`POST /api/v1/auth/register` — Register user

Fuente: [AuthController:81](../../src/main/java/com/tecmarket/backend/auth/controller/AuthController.java#L81). Método Java: `register`.

Acceso: público. Permiso del controlador: `ninguno adicional`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<AuthUserResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| request | RequestBody | RegisterRequest | @Valid @RequestBody RegisterRequest request |

Body completo (RegisterRequest):

```json
{
  "documentTypeCode": "CC",
  "documentNumber": "{{documentNumber}}",
  "name": "Usuario QA",
  "surname": "Pérez",
  "email": "{{email}}",
  "password": "{{password}}",
  "address": "Calle 10 #20-30",
  "phone": "+573001112233",
  "countryId": "{{countryId}}",
  "departmentId": "{{departmentId}}",
  "cityId": "{{cityId}}"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "documentTypeCode": "CC",
  "documentNumber": "{{documentNumber}}",
  "name": "Usuario QA",
  "surname": "Pérez",
  "email": "{{email}}",
  "password": "{{password}}",
  "address": "Calle 10 #20-30",
  "phone": "+573001112233",
  "countryId": "{{countryId}}",
  "departmentId": "{{departmentId}}",
  "cityId": "{{cityId}}"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| documentTypeCode | String | sí | @NotBlank @Size(max = 15) |
| documentNumber | String | sí | @NotBlank @Size(max = 30) |
| name | String | sí | @NotBlank @Size(max = 50) |
| surname | String | sí | @NotBlank @Size(max = 50) |
| email | String | sí | @NotBlank @Email @Size(max = 150) |
| password | String | sí | @NotBlank @Size(min = 8, max = 72) |
| address | String | sí | @NotBlank @Size(max = 200) |
| phone | String | sí | @NotBlank @Size(max = 30) |
| countryId | Integer | sí | @NotNull |
| departmentId | Integer | sí | @NotNull |
| cityId | Integer | sí | @NotNull |

Casos Bruno: [EP012-OK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-OK - Contrato válido.bru) · [EP012-documentTypeCode_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-documentTypeCode_MISSING - documentTypeCode- ausente.bru) · [EP012-documentTypeCode_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-documentTypeCode_NULL - documentTypeCode- null.bru) · [EP012-documentTypeCode_EMPTY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-documentTypeCode_EMPTY - documentTypeCode- EMPTY.bru) · [EP012-documentTypeCode_BLANK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-documentTypeCode_BLANK - documentTypeCode- BLANK.bru) · [EP012-documentTypeCode_MAX_PLUS_1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-documentTypeCode_MAX_PLUS_1 - documentTypeCode- 16 caracteres.bru) · [EP012-documentNumber_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-documentNumber_MISSING - documentNumber- ausente.bru) · [EP012-documentNumber_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-documentNumber_NULL - documentNumber- null.bru) · [EP012-documentNumber_EMPTY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-documentNumber_EMPTY - documentNumber- EMPTY.bru) · [EP012-documentNumber_BLANK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-documentNumber_BLANK - documentNumber- BLANK.bru) · [EP012-documentNumber_MAX_PLUS_1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-documentNumber_MAX_PLUS_1 - documentNumber- 31 caracteres.bru) · [EP012-name_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-name_MISSING - name- ausente.bru) · [EP012-name_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-name_NULL - name- null.bru) · [EP012-name_EMPTY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-name_EMPTY - name- EMPTY.bru) · [EP012-name_BLANK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-name_BLANK - name- BLANK.bru) · [EP012-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-name_MAX_PLUS_1 - name- 51 caracteres.bru) · [EP012-surname_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-surname_MISSING - surname- ausente.bru) · [EP012-surname_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-surname_NULL - surname- null.bru) · [EP012-surname_EMPTY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-surname_EMPTY - surname- EMPTY.bru) · [EP012-surname_BLANK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-surname_BLANK - surname- BLANK.bru) · [EP012-surname_MAX_PLUS_1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-surname_MAX_PLUS_1 - surname- 51 caracteres.bru) · [EP012-email_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-email_MISSING - email- ausente.bru) · [EP012-email_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-email_NULL - email- null.bru) · [EP012-email_EMPTY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-email_EMPTY - email- EMPTY.bru) · [EP012-email_BLANK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-email_BLANK - email- BLANK.bru) · [EP012-email_MAX_PLUS_1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-email_MAX_PLUS_1 - email- 151 caracteres.bru) · [EP012-email_EMAIL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-email_EMAIL - email- correo sin formato válido.bru) · [EP012-password_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-password_MISSING - password- ausente.bru) · [EP012-password_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-password_NULL - password- null.bru) · [EP012-password_EMPTY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-password_EMPTY - password- EMPTY.bru) · [EP012-password_BLANK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-password_BLANK - password- BLANK.bru) · [EP012-password_MAX_PLUS_1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-password_MAX_PLUS_1 - password- 73 caracteres.bru) · [EP012-address_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-address_MISSING - address- ausente.bru) · [EP012-address_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-address_NULL - address- null.bru) · [EP012-address_EMPTY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-address_EMPTY - address- EMPTY.bru) · [EP012-address_BLANK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-address_BLANK - address- BLANK.bru) · [EP012-address_MAX_PLUS_1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-address_MAX_PLUS_1 - address- 201 caracteres.bru) · [EP012-phone_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-phone_MISSING - phone- ausente.bru) · [EP012-phone_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-phone_NULL - phone- null.bru) · [EP012-phone_EMPTY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-phone_EMPTY - phone- EMPTY.bru) · [EP012-phone_BLANK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-phone_BLANK - phone- BLANK.bru) · [EP012-phone_MAX_PLUS_1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-phone_MAX_PLUS_1 - phone- 31 caracteres.bru) · [EP012-countryId_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-countryId_MISSING - countryId- ausente.bru) · [EP012-countryId_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-countryId_NULL - countryId- null.bru) · [EP012-countryId_TYPE](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-countryId_TYPE - countryId- tipo incompatible.bru) · [EP012-departmentId_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-departmentId_MISSING - departmentId- ausente.bru) · [EP012-departmentId_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-departmentId_NULL - departmentId- null.bru) · [EP012-departmentId_TYPE](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-departmentId_TYPE - departmentId- tipo incompatible.bru) · [EP012-cityId_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-cityId_MISSING - cityId- ausente.bru) · [EP012-cityId_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-cityId_NULL - cityId- null.bru) · [EP012-cityId_TYPE](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-cityId_TYPE - cityId- tipo incompatible.bru) · [EP012-REGISTER_ownerA](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-REGISTER_ownerA - Registrar ownerA.bru) · [EP012-REGISTER_ownerB](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-REGISTER_ownerB - Registrar ownerB.bru) · [EP012-REGISTER_adminA](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-REGISTER_adminA - Registrar adminA.bru) · [EP012-REGISTER_techA1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-REGISTER_techA1 - Registrar techA1.bru) · [EP012-REGISTER_techA2](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-REGISTER_techA2 - Registrar techA2.bru) · [EP012-REGISTER_advisorA1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-REGISTER_advisorA1 - Registrar advisorA1.bru) · [EP012-REGISTER_sellerA1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-REGISTER_sellerA1 - Registrar sellerA1.bru) · [EP012-REGISTER_customerA](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-REGISTER_customerA - Registrar customerA.bru) · [EP012-REGISTER_customerB](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-REGISTER_customerB - Registrar customerB.bru) · [EP012-REGISTER_outsider](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-REGISTER_outsider - Registrar outsider.bru) · [EP012-REGISTER_shared](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP012-REGISTER_shared - Registrar shared.bru)

## EP013

`POST /api/v1/auth/login` — Login user

Fuente: [AuthController:91](../../src/main/java/com/tecmarket/backend/auth/controller/AuthController.java#L91). Método Java: `login`.

Acceso: público. Permiso del controlador: `ninguno adicional`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<LoginResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| request | RequestBody | LoginRequest | @Valid @RequestBody LoginRequest request |

Body completo (LoginRequest):

```json
{
  "email": "{{email}}",
  "password": "{{password}}"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "email": "{{email}}",
  "password": "{{password}}"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| email | String | sí | @NotBlank @Email |
| password | String | sí | @NotBlank |

Casos Bruno: [EP013-OK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-OK - Contrato válido.bru) · [EP013-email_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-email_MISSING - email- ausente.bru) · [EP013-email_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-email_NULL - email- null.bru) · [EP013-email_EMPTY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-email_EMPTY - email- EMPTY.bru) · [EP013-email_BLANK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-email_BLANK - email- BLANK.bru) · [EP013-email_EMAIL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-email_EMAIL - email- correo sin formato válido.bru) · [EP013-password_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-password_MISSING - password- ausente.bru) · [EP013-password_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-password_NULL - password- null.bru) · [EP013-password_EMPTY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-password_EMPTY - password- EMPTY.bru) · [EP013-password_BLANK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-password_BLANK - password- BLANK.bru) · [EP013-LOGIN_platformAdmin](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-LOGIN_platformAdmin - Login platformAdmin.bru) · [EP013-LOGIN_ownerA](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-LOGIN_ownerA - Login ownerA.bru) · [EP013-LOGIN_ownerB](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-LOGIN_ownerB - Login ownerB.bru) · [EP013-LOGIN_adminA](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-LOGIN_adminA - Login adminA.bru) · [EP013-LOGIN_techA1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-LOGIN_techA1 - Login techA1.bru) · [EP013-LOGIN_techA2](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-LOGIN_techA2 - Login techA2.bru) · [EP013-LOGIN_advisorA1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-LOGIN_advisorA1 - Login advisorA1.bru) · [EP013-LOGIN_sellerA1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-LOGIN_sellerA1 - Login sellerA1.bru) · [EP013-LOGIN_customerA](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-LOGIN_customerA - Login customerA.bru) · [EP013-LOGIN_customerB](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-LOGIN_customerB - Login customerB.bru) · [EP013-LOGIN_outsider](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-LOGIN_outsider - Login outsider.bru) · [EP013-LOGIN_shared](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-LOGIN_shared - Login shared.bru) · [EP013-RATE_LIMIT](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP013-RATE_LIMIT - Sexto intento mismo correo e IP.bru)

## EP014

`POST /api/v1/auth/verify-email` — Verify email

Fuente: [AuthController:107](../../src/main/java/com/tecmarket/backend/auth/controller/AuthController.java#L107). Método Java: `verifyEmail`.

Acceso: público. Permiso del controlador: `ninguno adicional`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| request | RequestBody | VerifyEmailRequest | @Valid @RequestBody VerifyEmailRequest request |

Body completo (VerifyEmailRequest):

```json
{
  "token": "{{verificationToken}}"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "token": "{{verificationToken}}"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| token | String | sí | @NotBlank @Size(min = 64, max = 128) |

Casos Bruno: [EP014-OK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-OK - Contrato válido.bru) · [EP014-token_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-token_MISSING - token- ausente.bru) · [EP014-token_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-token_NULL - token- null.bru) · [EP014-token_EMPTY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-token_EMPTY - token- EMPTY.bru) · [EP014-token_BLANK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-token_BLANK - token- BLANK.bru) · [EP014-token_MAX_PLUS_1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-token_MAX_PLUS_1 - token- 129 caracteres.bru) · [EP014-VERIFY_ownerA](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-VERIFY_ownerA - Verificar ownerA.bru) · [EP014-VERIFY_ownerB](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-VERIFY_ownerB - Verificar ownerB.bru) · [EP014-VERIFY_adminA](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-VERIFY_adminA - Verificar adminA.bru) · [EP014-VERIFY_techA1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-VERIFY_techA1 - Verificar techA1.bru) · [EP014-VERIFY_techA2](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-VERIFY_techA2 - Verificar techA2.bru) · [EP014-VERIFY_advisorA1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-VERIFY_advisorA1 - Verificar advisorA1.bru) · [EP014-VERIFY_sellerA1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-VERIFY_sellerA1 - Verificar sellerA1.bru) · [EP014-VERIFY_customerA](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-VERIFY_customerA - Verificar customerA.bru) · [EP014-VERIFY_customerB](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-VERIFY_customerB - Verificar customerB.bru) · [EP014-VERIFY_outsider](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-VERIFY_outsider - Verificar outsider.bru) · [EP014-VERIFY_shared](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP014-VERIFY_shared - Verificar shared.bru)

## EP015

`POST /api/v1/auth/password-reset/request` — Request password reset

Fuente: [AuthController:114](../../src/main/java/com/tecmarket/backend/auth/controller/AuthController.java#L114). Método Java: `requestPasswordReset`.

Acceso: público. Permiso del controlador: `ninguno adicional`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| request | RequestBody | RequestPasswordResetRequest | @Valid @RequestBody RequestPasswordResetRequest request |

Body completo (RequestPasswordResetRequest):

```json
{
  "email": "{{email}}"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "email": "{{email}}"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| email | String | sí | @NotBlank @Email @Size(max = 150) |

Casos Bruno: [EP015-OK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP015-OK - Contrato válido.bru) · [EP015-email_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP015-email_MISSING - email- ausente.bru) · [EP015-email_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP015-email_NULL - email- null.bru) · [EP015-email_EMPTY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP015-email_EMPTY - email- EMPTY.bru) · [EP015-email_BLANK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP015-email_BLANK - email- BLANK.bru) · [EP015-email_MAX_PLUS_1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP015-email_MAX_PLUS_1 - email- 151 caracteres.bru) · [EP015-email_EMAIL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP015-email_EMAIL - email- correo sin formato válido.bru)

## EP016

`POST /api/v1/auth/password-reset/confirm` — Reset password

Fuente: [AuthController:122](../../src/main/java/com/tecmarket/backend/auth/controller/AuthController.java#L122). Método Java: `resetPassword`.

Acceso: público. Permiso del controlador: `ninguno adicional`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| request | RequestBody | ResetPasswordRequest | @Valid @RequestBody ResetPasswordRequest request |

Body completo (ResetPasswordRequest):

```json
{
  "token": "{{resetToken}}",
  "newPassword": "{{newPassword}}"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "token": "{{resetToken}}",
  "newPassword": "{{newPassword}}"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| token | String | sí | @NotBlank @Size(min = 32, max = 256) |
| newPassword | String | sí | @NotBlank @Size(min = 12, max = 72) |

Casos Bruno: [EP016-OK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP016-OK - Contrato válido.bru) · [EP016-token_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP016-token_MISSING - token- ausente.bru) · [EP016-token_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP016-token_NULL - token- null.bru) · [EP016-token_EMPTY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP016-token_EMPTY - token- EMPTY.bru) · [EP016-token_BLANK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP016-token_BLANK - token- BLANK.bru) · [EP016-token_MAX_PLUS_1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP016-token_MAX_PLUS_1 - token- 257 caracteres.bru) · [EP016-newPassword_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP016-newPassword_MISSING - newPassword- ausente.bru) · [EP016-newPassword_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP016-newPassword_NULL - newPassword- null.bru) · [EP016-newPassword_EMPTY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP016-newPassword_EMPTY - newPassword- EMPTY.bru) · [EP016-newPassword_BLANK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP016-newPassword_BLANK - newPassword- BLANK.bru) · [EP016-newPassword_MAX_PLUS_1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP016-newPassword_MAX_PLUS_1 - newPassword- 73 caracteres.bru)

## EP017

`POST /api/v1/auth/password/change` — Change password

Fuente: [AuthController:130](../../src/main/java/com/tecmarket/backend/auth/controller/AuthController.java#L130). Método Java: `changePassword`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `ninguno adicional`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| request | RequestBody | ChangePasswordRequest | @Valid @RequestBody ChangePasswordRequest request |

Body completo (ChangePasswordRequest):

```json
{
  "currentPassword": "{{currentPassword}}",
  "newPassword": "{{newPassword}}"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "currentPassword": "{{currentPassword}}",
  "newPassword": "{{newPassword}}"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| currentPassword | String | sí | @NotBlank @Size(max = 72) |
| newPassword | String | sí | @NotBlank @Size(min = 12, max = 72) |

Casos Bruno: [EP017-OK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP017-OK - Contrato válido.bru) · [EP017-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP017-NO_TOKEN - Sin autenticación.bru) · [EP017-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP017-INVALID_TOKEN - Token inválido.bru) · [EP017-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP017-REVOKED - Token previamente revocado.bru) · [EP017-currentPassword_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP017-currentPassword_MISSING - currentPassword- ausente.bru) · [EP017-currentPassword_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP017-currentPassword_NULL - currentPassword- null.bru) · [EP017-currentPassword_EMPTY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP017-currentPassword_EMPTY - currentPassword- EMPTY.bru) · [EP017-currentPassword_BLANK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP017-currentPassword_BLANK - currentPassword- BLANK.bru) · [EP017-currentPassword_MAX_PLUS_1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP017-currentPassword_MAX_PLUS_1 - currentPassword- 73 caracteres.bru) · [EP017-newPassword_MISSING](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP017-newPassword_MISSING - newPassword- ausente.bru) · [EP017-newPassword_NULL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP017-newPassword_NULL - newPassword- null.bru) · [EP017-newPassword_EMPTY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP017-newPassword_EMPTY - newPassword- EMPTY.bru) · [EP017-newPassword_BLANK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP017-newPassword_BLANK - newPassword- BLANK.bru) · [EP017-newPassword_MAX_PLUS_1](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP017-newPassword_MAX_PLUS_1 - newPassword- 73 caracteres.bru)

## EP018

`GET /api/v1/auth/me` — Get authenticated user

Fuente: [AuthController:142](../../src/main/java/com/tecmarket/backend/auth/controller/AuthController.java#L142). Método Java: `me`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `ninguno adicional`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<AuthUserResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|


Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP018-OK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP018-OK - Contrato válido.bru) · [EP018-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP018-NO_TOKEN - Sin autenticación.bru) · [EP018-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP018-INVALID_TOKEN - Token inválido.bru) · [EP018-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP018-REVOKED - Token previamente revocado.bru) · [EP018-AFTER_REPLAY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP018-AFTER_REPLAY - Access token de familia revocada.bru) · [EP018-INVALID_AFTER_DEACTIVATION](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP018-INVALID_AFTER_DEACTIVATION - Access inválido tras DEACTIVATION.bru) · [EP018-INVALID_AFTER_PASSWORD_CHANGE](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP018-INVALID_AFTER_PASSWORD_CHANGE - Access inválido tras PASSWORD_CHANGE.bru) · [EP018-INVALID_AFTER_PASSWORD_RESET](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP018-INVALID_AFTER_PASSWORD_RESET - Access inválido tras PASSWORD_RESET.bru) · [EP018-INVALID_AFTER_LOGOUT](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP018-INVALID_AFTER_LOGOUT - Access inválido tras LOGOUT.bru) · [EP018-INVALID_AFTER_REVOKE_ONE](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP018-INVALID_AFTER_REVOKE_ONE - Access inválido tras REVOKE_ONE.bru) · [EP018-INVALID_AFTER_REVOKE_ALL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP018-INVALID_AFTER_REVOKE_ALL - Access inválido tras REVOKE_ALL.bru) · [EP018-INVALID_AFTER_ROLE_CHANGE](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP018-INVALID_AFTER_ROLE_CHANGE - Access inválido tras ROLE_CHANGE.bru)

## EP019

`GET /api/v1/auth/me/permissions` — Get authenticated user permissions

Fuente: [AuthController:150](../../src/main/java/com/tecmarket/backend/auth/controller/AuthController.java#L150). Método Java: `mePermissions`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `ninguno adicional`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<AuthUserPermissionsResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|


Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP019-OK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP019-OK - Contrato válido.bru) · [EP019-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP019-NO_TOKEN - Sin autenticación.bru) · [EP019-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP019-INVALID_TOKEN - Token inválido.bru) · [EP019-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP019-REVOKED - Token previamente revocado.bru)

## EP020

`POST /api/v1/auth/refresh` — Refresh access token

Fuente: [AuthController:157](../../src/main/java/com/tecmarket/backend/auth/controller/AuthController.java#L157). Método Java: `refresh`.

Acceso: público. Permiso del controlador: `ninguno adicional`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<LoginResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| tm_refresh | CookieValue | String | @CookieValue(name = "tm_refresh") String refreshToken |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP020-OK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP020-OK - Contrato válido.bru) · [EP020-ROTATE](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP020-ROTATE - Rotar refresh de sesión QA.bru) · [EP020-REPLAY](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP020-REPLAY - Reusar refresh anterior.bru) · [EP020-MISSING_COOKIE](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP020-MISSING_COOKIE - Refresh sin cookie.bru) · [EP020-BAD_ORIGIN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP020-BAD_ORIGIN - Refresh con Origin ajeno.bru) · [EP020-INVALID_AFTER_DEACTIVATION](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP020-INVALID_AFTER_DEACTIVATION - Refresh inválido tras DEACTIVATION.bru) · [EP020-INVALID_AFTER_PASSWORD_CHANGE](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP020-INVALID_AFTER_PASSWORD_CHANGE - Refresh inválido tras PASSWORD_CHANGE.bru) · [EP020-INVALID_AFTER_PASSWORD_RESET](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP020-INVALID_AFTER_PASSWORD_RESET - Refresh inválido tras PASSWORD_RESET.bru) · [EP020-INVALID_AFTER_LOGOUT](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP020-INVALID_AFTER_LOGOUT - Refresh inválido tras LOGOUT.bru) · [EP020-INVALID_AFTER_REVOKE_ONE](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP020-INVALID_AFTER_REVOKE_ONE - Refresh inválido tras REVOKE_ONE.bru) · [EP020-INVALID_AFTER_REVOKE_ALL](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP020-INVALID_AFTER_REVOKE_ALL - Refresh inválido tras REVOKE_ALL.bru) · [EP020-INVALID_AFTER_ROLE_CHANGE](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP020-INVALID_AFTER_ROLE_CHANGE - Refresh inválido tras ROLE_CHANGE.bru)

## EP021

`POST /api/v1/auth/logout` — Logout user

Fuente: [AuthController:168](../../src/main/java/com/tecmarket/backend/auth/controller/AuthController.java#L168). Método Java: `logout`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `ninguno adicional`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| tm_refresh | CookieValue | String | @CookieValue(name = "tm_refresh") String refreshToken |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP021-OK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP021-OK - Contrato válido.bru) · [EP021-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP021-NO_TOKEN - Sin autenticación.bru) · [EP021-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP021-INVALID_TOKEN - Token inválido.bru) · [EP021-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP021-REVOKED - Token previamente revocado.bru)

## EP022

`GET /api/v1/auth/sessions` — List active sessions

Fuente: [AuthController:180](../../src/main/java/com/tecmarket/backend/auth/controller/AuthController.java#L180). Método Java: `sessions`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `ninguno adicional`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<AuthSessionResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|


Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP022-OK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP022-OK - Contrato válido.bru) · [EP022-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP022-NO_TOKEN - Sin autenticación.bru) · [EP022-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP022-INVALID_TOKEN - Token inválido.bru) · [EP022-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP022-REVOKED - Token previamente revocado.bru)

## EP023

`DELETE /api/v1/auth/sessions/{sessionId}` — Revoke one session

Fuente: [AuthController:188](../../src/main/java/com/tecmarket/backend/auth/controller/AuthController.java#L188). Método Java: `revokeSession`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `ninguno adicional`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| sessionId | PathVariable | UUID | @PathVariable UUID sessionId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP023-OK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP023-OK - Contrato válido.bru) · [EP023-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP023-NO_TOKEN - Sin autenticación.bru) · [EP023-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP023-INVALID_TOKEN - Token inválido.bru) · [EP023-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP023-REVOKED - Token previamente revocado.bru)

## EP024

`DELETE /api/v1/auth/sessions` — Revoke all sessions

Fuente: [AuthController:197](../../src/main/java/com/tecmarket/backend/auth/controller/AuthController.java#L197). Método Java: `revokeAllSessions`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `ninguno adicional`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|


Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP024-OK](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP024-OK - Contrato válido.bru) · [EP024-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP024-NO_TOKEN - Sin autenticación.bru) · [EP024-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP024-INVALID_TOKEN - Token inválido.bru) · [EP024-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/AUTH/EP024-REVOKED - Token previamente revocado.bru)

## EP025

`GET /api/v1/businesses/{businessId}/branches` — List business branches

Fuente: [BusinessBranchController:45](../../src/main/java/com/tecmarket/backend/business/controller/BusinessBranchController.java#L45). Método Java: `findAll`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_BRANCH_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<BusinessBranchResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP025-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS BRANCH/EP025-OK - Contrato válido.bru) · [EP025-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS BRANCH/EP025-NO_TOKEN - Sin autenticación.bru) · [EP025-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS BRANCH/EP025-INVALID_TOKEN - Token inválido.bru) · [EP025-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS BRANCH/EP025-REVOKED - Token previamente revocado.bru) · [EP025-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS BRANCH/EP025-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP025-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS BRANCH/EP025-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru)

## EP026

`GET /api/v1/businesses/{businessId}/branches/{branchId}` — Find branch by id

Fuente: [BusinessBranchController:55](../../src/main/java/com/tecmarket/backend/business/controller/BusinessBranchController.java#L55). Método Java: `findById`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_BRANCH_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessBranchResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| branchId | PathVariable | Integer | @PathVariable Integer branchId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP026-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS BRANCH/EP026-OK - Contrato válido.bru) · [EP026-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS BRANCH/EP026-NO_TOKEN - Sin autenticación.bru) · [EP026-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS BRANCH/EP026-INVALID_TOKEN - Token inválido.bru) · [EP026-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS BRANCH/EP026-REVOKED - Token previamente revocado.bru) · [EP026-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS BRANCH/EP026-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP026-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS BRANCH/EP026-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP026-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP026-NOT_FOUND - ID inexistente comprobado.bru) · [EP026-PATH_FOREIGN_BRANCH](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS BRANCH/EP026-PATH_FOREIGN_BRANCH - Sede B bajo ruta de negocio A.bru)

## EP027

`POST /api/v1/businesses/{businessId}/branches` — Create branch

Fuente: [BusinessBranchController:66](../../src/main/java/com/tecmarket/backend/business/controller/BusinessBranchController.java#L66). Método Java: `create`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_BRANCH_CREATE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<BusinessBranchResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| request | RequestBody | CreateBusinessBranchRequest | @Valid @RequestBody CreateBusinessBranchRequest request |

Body completo (CreateBusinessBranchRequest):

```json
{
  "name": "Usuario QA",
  "cityId": "{{cityId}}",
  "address": "Carrera 15 # 100-20",
  "neighborhood": "Usaquen",
  "phone": "+573001112233",
  "latitude": 4.6845,
  "longitude": -74.0478,
  "serviceRadiusKm": 8,
  "primaryBranch": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "name": "Usuario QA",
  "cityId": "{{cityId}}",
  "address": "Carrera 15 # 100-20"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| name | String | sí | @NotBlank @Size(max = 120) |
| cityId | Integer | sí | @NotNull |
| address | String | sí | @NotBlank @Size(max = 200) |
| neighborhood | String | no | @Size(max = 120) |
| phone | String | no | @Size(max = 30) |
| latitude | BigDecimal | no | @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0") |
| longitude | BigDecimal | no | @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0") |
| serviceRadiusKm | BigDecimal | no | @DecimalMin(value = "0.1") |
| primaryBranch | Boolean | no |  |

Casos Bruno: [EP027-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS BRANCH/EP027-OK - Contrato válido.bru) · [EP027-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS BRANCH/EP027-NO_TOKEN - Sin autenticación.bru) · [EP027-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS BRANCH/EP027-INVALID_TOKEN - Token inválido.bru) · [EP027-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS BRANCH/EP027-REVOKED - Token previamente revocado.bru) · [EP027-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS BRANCH/EP027-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP027-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS BRANCH/EP027-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP027-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-name_MISSING - name- ausente.bru) · [EP027-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-name_NULL - name- null.bru) · [EP027-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-name_EMPTY - name- EMPTY.bru) · [EP027-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-name_BLANK - name- BLANK.bru) · [EP027-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-name_MAX_PLUS_1 - name- 121 caracteres.bru) · [EP027-cityId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-cityId_MISSING - cityId- ausente.bru) · [EP027-cityId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-cityId_NULL - cityId- null.bru) · [EP027-cityId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-cityId_TYPE - cityId- tipo incompatible.bru) · [EP027-address_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-address_MISSING - address- ausente.bru) · [EP027-address_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-address_NULL - address- null.bru) · [EP027-address_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-address_EMPTY - address- EMPTY.bru) · [EP027-address_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-address_BLANK - address- BLANK.bru) · [EP027-address_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-address_MAX_PLUS_1 - address- 201 caracteres.bru) · [EP027-neighborhood_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-neighborhood_MAX_PLUS_1 - neighborhood- 121 caracteres.bru) · [EP027-phone_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-phone_MAX_PLUS_1 - phone- 31 caracteres.bru) · [EP027-latitude_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-latitude_TYPE - latitude- tipo incompatible.bru) · [EP027-latitude_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-latitude_DecimalMin - latitude- fuera de límite DecimalMin.bru) · [EP027-latitude_DecimalMax](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-latitude_DecimalMax - latitude- fuera de límite DecimalMax.bru) · [EP027-longitude_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-longitude_TYPE - longitude- tipo incompatible.bru) · [EP027-longitude_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-longitude_DecimalMin - longitude- fuera de límite DecimalMin.bru) · [EP027-longitude_DecimalMax](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-longitude_DecimalMax - longitude- fuera de límite DecimalMax.bru) · [EP027-serviceRadiusKm_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-serviceRadiusKm_TYPE - serviceRadiusKm- tipo incompatible.bru) · [EP027-serviceRadiusKm_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-serviceRadiusKm_DecimalMin - serviceRadiusKm- fuera de límite DecimalMin.bru) · [EP027-primaryBranch_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP027-primaryBranch_TYPE - primaryBranch- tipo incompatible.bru) · [EP027-BRANCH_A1](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS BRANCH/EP027-BRANCH_A1 - Crear sede A1.bru) · [EP027-BRANCH_A2](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS BRANCH/EP027-BRANCH_A2 - Crear sede A2.bru) · [EP027-BRANCH_B1](../../bruno/TECMARKET-QA/03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/02_PREPARACION/BUSINESS BRANCH/EP027-BRANCH_B1 - Crear sede B1.bru) · [EP027-BRANCH_B2](../../bruno/TECMARKET-QA/03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/02_PREPARACION/BUSINESS BRANCH/EP027-BRANCH_B2 - Crear sede B2.bru)

## EP028

`PUT /api/v1/businesses/{businessId}/branches/{branchId}` — Update branch

Fuente: [BusinessBranchController:78](../../src/main/java/com/tecmarket/backend/business/controller/BusinessBranchController.java#L78). Método Java: `update`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_BRANCH_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessBranchResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| branchId | PathVariable | Integer | @PathVariable Integer branchId |
| request | RequestBody | UpdateBusinessBranchRequest | @Valid @RequestBody UpdateBusinessBranchRequest request |

Body completo (UpdateBusinessBranchRequest):

```json
{
  "name": "Usuario QA",
  "cityId": "{{cityId}}",
  "address": "Carrera 15 # 100-20",
  "neighborhood": "Usaquen",
  "phone": "+573001112233",
  "latitude": 4.6845,
  "longitude": -74.0478,
  "serviceRadiusKm": 8,
  "primaryBranch": true,
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "name": "Usuario QA",
  "cityId": "{{cityId}}",
  "address": "Carrera 15 # 100-20",
  "active": true
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| name | String | sí | @NotBlank @Size(max = 120) |
| cityId | Integer | sí | @NotNull |
| address | String | sí | @NotBlank @Size(max = 200) |
| neighborhood | String | no | @Size(max = 120) |
| phone | String | no | @Size(max = 30) |
| latitude | BigDecimal | no | @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0") |
| longitude | BigDecimal | no | @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0") |
| serviceRadiusKm | BigDecimal | no | @DecimalMin(value = "0.1") |
| primaryBranch | Boolean | no |  |
| active | Boolean | sí | @NotNull |

Casos Bruno: [EP028-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS BRANCH/EP028-OK - Contrato válido.bru) · [EP028-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS BRANCH/EP028-NO_TOKEN - Sin autenticación.bru) · [EP028-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS BRANCH/EP028-INVALID_TOKEN - Token inválido.bru) · [EP028-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS BRANCH/EP028-REVOKED - Token previamente revocado.bru) · [EP028-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS BRANCH/EP028-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP028-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS BRANCH/EP028-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP028-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-name_MISSING - name- ausente.bru) · [EP028-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-name_NULL - name- null.bru) · [EP028-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-name_EMPTY - name- EMPTY.bru) · [EP028-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-name_BLANK - name- BLANK.bru) · [EP028-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-name_MAX_PLUS_1 - name- 121 caracteres.bru) · [EP028-cityId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-cityId_MISSING - cityId- ausente.bru) · [EP028-cityId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-cityId_NULL - cityId- null.bru) · [EP028-cityId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-cityId_TYPE - cityId- tipo incompatible.bru) · [EP028-address_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-address_MISSING - address- ausente.bru) · [EP028-address_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-address_NULL - address- null.bru) · [EP028-address_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-address_EMPTY - address- EMPTY.bru) · [EP028-address_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-address_BLANK - address- BLANK.bru) · [EP028-address_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-address_MAX_PLUS_1 - address- 201 caracteres.bru) · [EP028-neighborhood_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-neighborhood_MAX_PLUS_1 - neighborhood- 121 caracteres.bru) · [EP028-phone_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-phone_MAX_PLUS_1 - phone- 31 caracteres.bru) · [EP028-latitude_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-latitude_TYPE - latitude- tipo incompatible.bru) · [EP028-latitude_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-latitude_DecimalMin - latitude- fuera de límite DecimalMin.bru) · [EP028-latitude_DecimalMax](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-latitude_DecimalMax - latitude- fuera de límite DecimalMax.bru) · [EP028-longitude_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-longitude_TYPE - longitude- tipo incompatible.bru) · [EP028-longitude_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-longitude_DecimalMin - longitude- fuera de límite DecimalMin.bru) · [EP028-longitude_DecimalMax](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-longitude_DecimalMax - longitude- fuera de límite DecimalMax.bru) · [EP028-serviceRadiusKm_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-serviceRadiusKm_TYPE - serviceRadiusKm- tipo incompatible.bru) · [EP028-serviceRadiusKm_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-serviceRadiusKm_DecimalMin - serviceRadiusKm- fuera de límite DecimalMin.bru) · [EP028-primaryBranch_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-primaryBranch_TYPE - primaryBranch- tipo incompatible.bru) · [EP028-active_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-active_MISSING - active- ausente.bru) · [EP028-active_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-active_NULL - active- null.bru) · [EP028-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS BRANCH/EP028-active_TYPE - active- tipo incompatible.bru)

## EP029

`DELETE /api/v1/businesses/{businessId}/branches/{branchId}` — Delete branch

Fuente: [BusinessBranchController:90](../../src/main/java/com/tecmarket/backend/business/controller/BusinessBranchController.java#L90). Método Java: `delete`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_BRANCH_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| branchId | PathVariable | Integer | @PathVariable Integer branchId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP029-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS BRANCH/EP029-OK - Contrato válido.bru) · [EP029-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS BRANCH/EP029-NO_TOKEN - Sin autenticación.bru) · [EP029-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS BRANCH/EP029-INVALID_TOKEN - Token inválido.bru) · [EP029-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS BRANCH/EP029-REVOKED - Token previamente revocado.bru) · [EP029-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS BRANCH/EP029-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP029-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS BRANCH/EP029-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru)

## EP030

`GET /api/v1/businesses/{businessId}/catalogs/{catalogType}` — List business catalog items

Fuente: [BusinessCatalogController:38](../../src/main/java/com/tecmarket/backend/business/controller/BusinessCatalogController.java#L38). Método Java: `findAll`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_CATALOG_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<BusinessCatalogItemResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| catalogType | PathVariable | String | @PathVariable String catalogType |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP030-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS CATALOG/EP030-OK - Contrato válido.bru) · [EP030-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS CATALOG/EP030-NO_TOKEN - Sin autenticación.bru) · [EP030-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS CATALOG/EP030-INVALID_TOKEN - Token inválido.bru) · [EP030-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS CATALOG/EP030-REVOKED - Token previamente revocado.bru) · [EP030-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP030-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP030-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP030-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP030-CAT_SERVICE_CATEGORY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP030-CAT_SERVICE_CATEGORY - Catálogo SERVICE_CATEGORY.bru) · [EP030-CAT_DEVICE_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP030-CAT_DEVICE_TYPE - Catálogo DEVICE_TYPE.bru) · [EP030-CAT_BRAND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP030-CAT_BRAND - Catálogo BRAND.bru) · [EP030-CAT_APPOINTMENT_STATUS](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP030-CAT_APPOINTMENT_STATUS - Catálogo APPOINTMENT_STATUS.bru) · [EP030-CAT_REPAIR_ORDER_STATUS](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP030-CAT_REPAIR_ORDER_STATUS - Catálogo REPAIR_ORDER_STATUS.bru) · [EP030-CAT_QUOTATION_STATUS](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP030-CAT_QUOTATION_STATUS - Catálogo QUOTATION_STATUS.bru) · [EP030-CAT_ITEM_CATEGORY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP030-CAT_ITEM_CATEGORY - Catálogo ITEM_CATEGORY.bru) · [EP030-CAT_MOVEMENT_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP030-CAT_MOVEMENT_TYPE - Catálogo MOVEMENT_TYPE.bru) · [EP030-CAT_PAYMENT_METHOD](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP030-CAT_PAYMENT_METHOD - Catálogo PAYMENT_METHOD.bru) · [EP030-CAPTURE_A_SERVICE_CATEGORY](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_A_SERVICE_CATEGORY - Resolver códigos A SERVICE_CATEGORY.bru) · [EP030-CAPTURE_A_DEVICE_TYPE](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_A_DEVICE_TYPE - Resolver códigos A DEVICE_TYPE.bru) · [EP030-CAPTURE_A_BRAND](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_A_BRAND - Resolver códigos A BRAND.bru) · [EP030-CAPTURE_A_REPAIR_ORDER_STATUS](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_A_REPAIR_ORDER_STATUS - Resolver códigos A REPAIR_ORDER_STATUS.bru) · [EP030-CAPTURE_A_QUOTATION_STATUS](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_A_QUOTATION_STATUS - Resolver códigos A QUOTATION_STATUS.bru) · [EP030-CAPTURE_A_APPOINTMENT_STATUS](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_A_APPOINTMENT_STATUS - Resolver códigos A APPOINTMENT_STATUS.bru) · [EP030-CAPTURE_A_MOVEMENT_TYPE](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_A_MOVEMENT_TYPE - Resolver códigos A MOVEMENT_TYPE.bru) · [EP030-CAPTURE_A_ITEM_CATEGORY](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_A_ITEM_CATEGORY - Resolver códigos A ITEM_CATEGORY.bru) · [EP030-CAPTURE_A_PAYMENT_METHOD](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_A_PAYMENT_METHOD - Resolver códigos A PAYMENT_METHOD.bru) · [EP030-CAPTURE_B_SERVICE_CATEGORY](../../bruno/TECMARKET-QA/03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_B_SERVICE_CATEGORY - Resolver códigos B SERVICE_CATEGORY.bru) · [EP030-CAPTURE_B_DEVICE_TYPE](../../bruno/TECMARKET-QA/03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_B_DEVICE_TYPE - Resolver códigos B DEVICE_TYPE.bru) · [EP030-CAPTURE_B_BRAND](../../bruno/TECMARKET-QA/03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_B_BRAND - Resolver códigos B BRAND.bru) · [EP030-CAPTURE_B_REPAIR_ORDER_STATUS](../../bruno/TECMARKET-QA/03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_B_REPAIR_ORDER_STATUS - Resolver códigos B REPAIR_ORDER_STATUS.bru) · [EP030-CAPTURE_B_QUOTATION_STATUS](../../bruno/TECMARKET-QA/03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_B_QUOTATION_STATUS - Resolver códigos B QUOTATION_STATUS.bru) · [EP030-CAPTURE_B_APPOINTMENT_STATUS](../../bruno/TECMARKET-QA/03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_B_APPOINTMENT_STATUS - Resolver códigos B APPOINTMENT_STATUS.bru) · [EP030-CAPTURE_B_MOVEMENT_TYPE](../../bruno/TECMARKET-QA/03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_B_MOVEMENT_TYPE - Resolver códigos B MOVEMENT_TYPE.bru) · [EP030-CAPTURE_B_ITEM_CATEGORY](../../bruno/TECMARKET-QA/03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_B_ITEM_CATEGORY - Resolver códigos B ITEM_CATEGORY.bru) · [EP030-CAPTURE_B_PAYMENT_METHOD](../../bruno/TECMARKET-QA/03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/02_PREPARACION/BUSINESS CATALOG/EP030-CAPTURE_B_PAYMENT_METHOD - Resolver códigos B PAYMENT_METHOD.bru) · [EP030-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP030-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP030-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP030-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP030-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP030-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP031

`GET /api/v1/businesses/{businessId}/catalogs/{catalogType}/{itemId}` — Find business catalog item

Fuente: [BusinessCatalogController:52](../../src/main/java/com/tecmarket/backend/business/controller/BusinessCatalogController.java#L52). Método Java: `findById`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_CATALOG_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessCatalogItemResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| catalogType | PathVariable | String | @PathVariable String catalogType |
| itemId | PathVariable | Integer | @PathVariable Integer itemId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP031-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS CATALOG/EP031-OK - Contrato válido.bru) · [EP031-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS CATALOG/EP031-NO_TOKEN - Sin autenticación.bru) · [EP031-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS CATALOG/EP031-INVALID_TOKEN - Token inválido.bru) · [EP031-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS CATALOG/EP031-REVOKED - Token previamente revocado.bru) · [EP031-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP031-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP031-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP031-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP031-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP031-NOT_FOUND - ID inexistente comprobado.bru) · [EP031-CAT_SERVICE_CATEGORY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP031-CAT_SERVICE_CATEGORY - Catálogo SERVICE_CATEGORY.bru) · [EP031-CAT_DEVICE_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP031-CAT_DEVICE_TYPE - Catálogo DEVICE_TYPE.bru) · [EP031-CAT_BRAND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP031-CAT_BRAND - Catálogo BRAND.bru) · [EP031-CAT_APPOINTMENT_STATUS](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP031-CAT_APPOINTMENT_STATUS - Catálogo APPOINTMENT_STATUS.bru) · [EP031-CAT_REPAIR_ORDER_STATUS](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP031-CAT_REPAIR_ORDER_STATUS - Catálogo REPAIR_ORDER_STATUS.bru) · [EP031-CAT_QUOTATION_STATUS](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP031-CAT_QUOTATION_STATUS - Catálogo QUOTATION_STATUS.bru) · [EP031-CAT_ITEM_CATEGORY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP031-CAT_ITEM_CATEGORY - Catálogo ITEM_CATEGORY.bru) · [EP031-CAT_MOVEMENT_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP031-CAT_MOVEMENT_TYPE - Catálogo MOVEMENT_TYPE.bru) · [EP031-CAT_PAYMENT_METHOD](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP031-CAT_PAYMENT_METHOD - Catálogo PAYMENT_METHOD.bru) · [EP031-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP031-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP031-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP031-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP031-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP031-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP032

`POST /api/v1/businesses/{businessId}/catalogs/{catalogType}` — Create business catalog item

Fuente: [BusinessCatalogController:68](../../src/main/java/com/tecmarket/backend/business/controller/BusinessCatalogController.java#L68). Método Java: `create`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_CATALOG_MANAGE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<BusinessCatalogItemResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| catalogType | PathVariable | String | @PathVariable String catalogType |
| request | RequestBody | CreateBusinessCatalogItemRequest | @Valid @RequestBody CreateBusinessCatalogItemRequest request |

Body completo (CreateBusinessCatalogItemRequest):

```json
{
  "code": "{{qaCode}}",
  "name": "Usuario QA",
  "description": "Servicios asociados a cambio de pantalla.",
  "icon": "monitor-smartphone",
  "sortOrder": 1,
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "code": "{{qaCode}}",
  "name": "Usuario QA"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| code | String | sí | @NotBlank @Size(max = 50) |
| name | String | sí | @NotBlank @Size(max = 150) |
| description | String | no | @Size(max = 500) |
| icon | String | no | @Size(max = 255) |
| sortOrder | Integer | no |  |
| active | Boolean | no |  |

Casos Bruno: [EP032-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS CATALOG/EP032-OK - Contrato válido.bru) · [EP032-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS CATALOG/EP032-NO_TOKEN - Sin autenticación.bru) · [EP032-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS CATALOG/EP032-INVALID_TOKEN - Token inválido.bru) · [EP032-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS CATALOG/EP032-REVOKED - Token previamente revocado.bru) · [EP032-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP032-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP032-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP032-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP032-code_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-code_MISSING - code- ausente.bru) · [EP032-code_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-code_NULL - code- null.bru) · [EP032-code_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-code_EMPTY - code- EMPTY.bru) · [EP032-code_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-code_BLANK - code- BLANK.bru) · [EP032-code_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-code_MAX_PLUS_1 - code- 51 caracteres.bru) · [EP032-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-name_MISSING - name- ausente.bru) · [EP032-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-name_NULL - name- null.bru) · [EP032-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-name_EMPTY - name- EMPTY.bru) · [EP032-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-name_BLANK - name- BLANK.bru) · [EP032-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-name_MAX_PLUS_1 - name- 151 caracteres.bru) · [EP032-description_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-description_MAX_PLUS_1 - description- 501 caracteres.bru) · [EP032-icon_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-icon_MAX_PLUS_1 - icon- 256 caracteres.bru) · [EP032-sortOrder_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-sortOrder_TYPE - sortOrder- tipo incompatible.bru) · [EP032-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-active_TYPE - active- tipo incompatible.bru) · [EP032-CAT_SERVICE_CATEGORY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-CAT_SERVICE_CATEGORY - Catálogo SERVICE_CATEGORY.bru) · [EP032-CAT_DEVICE_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-CAT_DEVICE_TYPE - Catálogo DEVICE_TYPE.bru) · [EP032-CAT_BRAND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-CAT_BRAND - Catálogo BRAND.bru) · [EP032-CAT_APPOINTMENT_STATUS](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-CAT_APPOINTMENT_STATUS - Catálogo APPOINTMENT_STATUS.bru) · [EP032-CAT_REPAIR_ORDER_STATUS](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-CAT_REPAIR_ORDER_STATUS - Catálogo REPAIR_ORDER_STATUS.bru) · [EP032-CAT_QUOTATION_STATUS](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-CAT_QUOTATION_STATUS - Catálogo QUOTATION_STATUS.bru) · [EP032-CAT_ITEM_CATEGORY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-CAT_ITEM_CATEGORY - Catálogo ITEM_CATEGORY.bru) · [EP032-CAT_MOVEMENT_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-CAT_MOVEMENT_TYPE - Catálogo MOVEMENT_TYPE.bru) · [EP032-CAT_PAYMENT_METHOD](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP032-CAT_PAYMENT_METHOD - Catálogo PAYMENT_METHOD.bru) · [EP032-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP032-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP032-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP032-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP032-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP032-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP033

`PUT /api/v1/businesses/{businessId}/catalogs/{catalogType}/{itemId}` — Update business catalog item

Fuente: [BusinessCatalogController:85](../../src/main/java/com/tecmarket/backend/business/controller/BusinessCatalogController.java#L85). Método Java: `update`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_CATALOG_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessCatalogItemResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| catalogType | PathVariable | String | @PathVariable String catalogType |
| itemId | PathVariable | Integer | @PathVariable Integer itemId |
| request | RequestBody | UpdateBusinessCatalogItemRequest | @Valid @RequestBody UpdateBusinessCatalogItemRequest request |

Body completo (UpdateBusinessCatalogItemRequest):

```json
{
  "name": "Usuario QA",
  "description": "Servicio especializado para cambio de pantalla.",
  "icon": "monitor-smartphone",
  "sortOrder": 2,
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "name": "Usuario QA"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| name | String | sí | @NotBlank @Size(max = 150) |
| description | String | no | @Size(max = 500) |
| icon | String | no | @Size(max = 255) |
| sortOrder | Integer | no |  |
| active | Boolean | no |  |

Casos Bruno: [EP033-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS CATALOG/EP033-OK - Contrato válido.bru) · [EP033-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS CATALOG/EP033-NO_TOKEN - Sin autenticación.bru) · [EP033-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS CATALOG/EP033-INVALID_TOKEN - Token inválido.bru) · [EP033-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS CATALOG/EP033-REVOKED - Token previamente revocado.bru) · [EP033-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP033-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP033-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP033-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP033-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-name_MISSING - name- ausente.bru) · [EP033-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-name_NULL - name- null.bru) · [EP033-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-name_EMPTY - name- EMPTY.bru) · [EP033-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-name_BLANK - name- BLANK.bru) · [EP033-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-name_MAX_PLUS_1 - name- 151 caracteres.bru) · [EP033-description_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-description_MAX_PLUS_1 - description- 501 caracteres.bru) · [EP033-icon_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-icon_MAX_PLUS_1 - icon- 256 caracteres.bru) · [EP033-sortOrder_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-sortOrder_TYPE - sortOrder- tipo incompatible.bru) · [EP033-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-active_TYPE - active- tipo incompatible.bru) · [EP033-CAT_SERVICE_CATEGORY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-CAT_SERVICE_CATEGORY - Catálogo SERVICE_CATEGORY.bru) · [EP033-CAT_DEVICE_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-CAT_DEVICE_TYPE - Catálogo DEVICE_TYPE.bru) · [EP033-CAT_BRAND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-CAT_BRAND - Catálogo BRAND.bru) · [EP033-CAT_APPOINTMENT_STATUS](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-CAT_APPOINTMENT_STATUS - Catálogo APPOINTMENT_STATUS.bru) · [EP033-CAT_REPAIR_ORDER_STATUS](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-CAT_REPAIR_ORDER_STATUS - Catálogo REPAIR_ORDER_STATUS.bru) · [EP033-CAT_QUOTATION_STATUS](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-CAT_QUOTATION_STATUS - Catálogo QUOTATION_STATUS.bru) · [EP033-CAT_ITEM_CATEGORY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-CAT_ITEM_CATEGORY - Catálogo ITEM_CATEGORY.bru) · [EP033-CAT_MOVEMENT_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-CAT_MOVEMENT_TYPE - Catálogo MOVEMENT_TYPE.bru) · [EP033-CAT_PAYMENT_METHOD](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP033-CAT_PAYMENT_METHOD - Catálogo PAYMENT_METHOD.bru) · [EP033-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP033-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP033-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP033-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP033-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP033-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP034

`DELETE /api/v1/businesses/{businessId}/catalogs/{catalogType}/{itemId}` — Disable business catalog item

Fuente: [BusinessCatalogController:103](../../src/main/java/com/tecmarket/backend/business/controller/BusinessCatalogController.java#L103). Método Java: `delete`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_CATALOG_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| catalogType | PathVariable | String | @PathVariable String catalogType |
| itemId | PathVariable | Integer | @PathVariable Integer itemId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP034-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS CATALOG/EP034-OK - Contrato válido.bru) · [EP034-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS CATALOG/EP034-NO_TOKEN - Sin autenticación.bru) · [EP034-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS CATALOG/EP034-INVALID_TOKEN - Token inválido.bru) · [EP034-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS CATALOG/EP034-REVOKED - Token previamente revocado.bru) · [EP034-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP034-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP034-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP034-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP034-CAT_SERVICE_CATEGORY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP034-CAT_SERVICE_CATEGORY - Catálogo SERVICE_CATEGORY.bru) · [EP034-CAT_DEVICE_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP034-CAT_DEVICE_TYPE - Catálogo DEVICE_TYPE.bru) · [EP034-CAT_BRAND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP034-CAT_BRAND - Catálogo BRAND.bru) · [EP034-CAT_APPOINTMENT_STATUS](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP034-CAT_APPOINTMENT_STATUS - Catálogo APPOINTMENT_STATUS.bru) · [EP034-CAT_REPAIR_ORDER_STATUS](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP034-CAT_REPAIR_ORDER_STATUS - Catálogo REPAIR_ORDER_STATUS.bru) · [EP034-CAT_QUOTATION_STATUS](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP034-CAT_QUOTATION_STATUS - Catálogo QUOTATION_STATUS.bru) · [EP034-CAT_ITEM_CATEGORY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP034-CAT_ITEM_CATEGORY - Catálogo ITEM_CATEGORY.bru) · [EP034-CAT_MOVEMENT_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP034-CAT_MOVEMENT_TYPE - Catálogo MOVEMENT_TYPE.bru) · [EP034-CAT_PAYMENT_METHOD](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS CATALOG/EP034-CAT_PAYMENT_METHOD - Catálogo PAYMENT_METHOD.bru) · [EP034-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP034-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP034-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP034-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP034-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS CATALOG/EP034-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP035

`GET /api/v1/businesses` — List businesses

Fuente: [BusinessController:50](../../src/main/java/com/tecmarket/backend/business/controller/BusinessController.java#L50). Método Java: `findAll`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('BUSINESS_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<BusinessResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP035-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS/EP035-OK - Contrato válido.bru) · [EP035-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP035-NO_TOKEN - Sin autenticación.bru) · [EP035-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP035-INVALID_TOKEN - Token inválido.bru) · [EP035-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP035-REVOKED - Token previamente revocado.bru) · [EP035-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS/EP035-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP035-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP035-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP035-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP035-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP035-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP035-QUERY_0_Min - Query 0- límite inválido.bru) · [EP035-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP035-QUERY_20_Min - Query 20- límite inválido.bru) · [EP035-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP035-QUERY_20_Max - Query 20- límite inválido.bru)

## EP036

`GET /api/v1/businesses/my` — List my businesses

Fuente: [BusinessController:61](../../src/main/java/com/tecmarket/backend/business/controller/BusinessController.java#L61). Método Java: `findMyBusinesses`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `isAuthenticated()`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<BusinessResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|


Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP036-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS/EP036-OK - Contrato válido.bru) · [EP036-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP036-NO_TOKEN - Sin autenticación.bru) · [EP036-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP036-INVALID_TOKEN - Token inválido.bru) · [EP036-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP036-REVOKED - Token previamente revocado.bru) · [EP036-RESOLVE_A](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS/EP036-RESOLVE_A - Resolver negocio A.bru) · [EP036-RESOLVE_B](../../bruno/TECMARKET-QA/03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/02_PREPARACION/BUSINESS/EP036-RESOLVE_B - Resolver negocio B.bru)

## EP037

`GET /api/v1/businesses/{businessId}/me/context` — Get my context in a business

Fuente: [BusinessController:71](../../src/main/java/com/tecmarket/backend/business/controller/BusinessController.java#L71). Método Java: `findMyBusinessContext`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `isAuthenticated()`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessContextResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP037-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS/EP037-OK - Contrato válido.bru) · [EP037-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP037-NO_TOKEN - Sin autenticación.bru) · [EP037-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP037-INVALID_TOKEN - Token inválido.bru) · [EP037-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP037-REVOKED - Token previamente revocado.bru) · [EP037-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS/EP037-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru)

## EP038

`GET /api/v1/businesses/{id}` — Find business by ID

Fuente: [BusinessController:83](../../src/main/java/com/tecmarket/backend/business/controller/BusinessController.java#L83). Método Java: `findById`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('BUSINESS_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP038-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS/EP038-OK - Contrato válido.bru) · [EP038-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP038-NO_TOKEN - Sin autenticación.bru) · [EP038-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP038-INVALID_TOKEN - Token inválido.bru) · [EP038-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP038-REVOKED - Token previamente revocado.bru) · [EP038-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS/EP038-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP038-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP038-NOT_FOUND - ID inexistente comprobado.bru)

## EP039

`POST /api/v1/businesses` — Create business

Fuente: [BusinessController:93](../../src/main/java/com/tecmarket/backend/business/controller/BusinessController.java#L93). Método Java: `create`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('BUSINESS_UPDATE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<BusinessResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| request | RequestBody | CreateBusinessCatalogRequest | @Valid @RequestBody CreateBusinessCatalogRequest request |

Body completo (CreateBusinessCatalogRequest):

```json
{
  "documentTypeCode": "NIT",
  "documentNumber": "{{documentNumber}}",
  "name": "Usuario QA",
  "legalName": "Negocio QA",
  "email": "{{email}}",
  "phone": "+573001112233",
  "address": "Calle 123 #45-67",
  "description": "Centro de servicio técnico especializado en celulares y portátiles.",
  "cityId": "{{cityId}}",
  "logo": null,
  "coverImage": null
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "documentTypeCode": "NIT",
  "documentNumber": "{{documentNumber}}",
  "name": "Usuario QA",
  "legalName": "Negocio QA",
  "email": "{{email}}",
  "phone": "+573001112233",
  "address": "Calle 123 #45-67",
  "description": "Centro de servicio técnico especializado en celulares y portátiles.",
  "cityId": "{{cityId}}"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| documentTypeCode | String | sí | @NotBlank @Size(max = 15) |
| documentNumber | String | sí | @NotBlank @Size(max = 50) |
| name | String | sí | @NotBlank @Size(max = 150) |
| legalName | String | sí | @NotBlank @Size(max = 200) |
| email | String | sí | @NotBlank @Email @Size(max = 150) |
| phone | String | sí | @NotBlank @Size(max = 30) |
| address | String | sí | @NotBlank @Size(max = 200) |
| description | String | sí | @NotBlank @Size(max = 255) |
| cityId | Integer | sí | @NotNull |
| logo | String | no | @Size(max = 500) |
| coverImage | String | no | @Size(max = 500) |

Casos Bruno: [EP039-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/BUSINESS/EP039-OK - Contrato válido.bru) · [EP039-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP039-NO_TOKEN - Sin autenticación.bru) · [EP039-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP039-INVALID_TOKEN - Token inválido.bru) · [EP039-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP039-REVOKED - Token previamente revocado.bru) · [EP039-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS/EP039-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP039-documentTypeCode_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-documentTypeCode_MISSING - documentTypeCode- ausente.bru) · [EP039-documentTypeCode_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-documentTypeCode_NULL - documentTypeCode- null.bru) · [EP039-documentTypeCode_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-documentTypeCode_EMPTY - documentTypeCode- EMPTY.bru) · [EP039-documentTypeCode_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-documentTypeCode_BLANK - documentTypeCode- BLANK.bru) · [EP039-documentTypeCode_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-documentTypeCode_MAX_PLUS_1 - documentTypeCode- 16 caracteres.bru) · [EP039-documentNumber_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-documentNumber_MISSING - documentNumber- ausente.bru) · [EP039-documentNumber_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-documentNumber_NULL - documentNumber- null.bru) · [EP039-documentNumber_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-documentNumber_EMPTY - documentNumber- EMPTY.bru) · [EP039-documentNumber_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-documentNumber_BLANK - documentNumber- BLANK.bru) · [EP039-documentNumber_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-documentNumber_MAX_PLUS_1 - documentNumber- 51 caracteres.bru) · [EP039-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-name_MISSING - name- ausente.bru) · [EP039-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-name_NULL - name- null.bru) · [EP039-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-name_EMPTY - name- EMPTY.bru) · [EP039-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-name_BLANK - name- BLANK.bru) · [EP039-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-name_MAX_PLUS_1 - name- 151 caracteres.bru) · [EP039-legalName_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-legalName_MISSING - legalName- ausente.bru) · [EP039-legalName_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-legalName_NULL - legalName- null.bru) · [EP039-legalName_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-legalName_EMPTY - legalName- EMPTY.bru) · [EP039-legalName_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-legalName_BLANK - legalName- BLANK.bru) · [EP039-legalName_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-legalName_MAX_PLUS_1 - legalName- 201 caracteres.bru) · [EP039-email_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-email_MISSING - email- ausente.bru) · [EP039-email_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-email_NULL - email- null.bru) · [EP039-email_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-email_EMPTY - email- EMPTY.bru) · [EP039-email_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-email_BLANK - email- BLANK.bru) · [EP039-email_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-email_MAX_PLUS_1 - email- 151 caracteres.bru) · [EP039-email_EMAIL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-email_EMAIL - email- correo sin formato válido.bru) · [EP039-phone_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-phone_MISSING - phone- ausente.bru) · [EP039-phone_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-phone_NULL - phone- null.bru) · [EP039-phone_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-phone_EMPTY - phone- EMPTY.bru) · [EP039-phone_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-phone_BLANK - phone- BLANK.bru) · [EP039-phone_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-phone_MAX_PLUS_1 - phone- 31 caracteres.bru) · [EP039-address_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-address_MISSING - address- ausente.bru) · [EP039-address_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-address_NULL - address- null.bru) · [EP039-address_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-address_EMPTY - address- EMPTY.bru) · [EP039-address_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-address_BLANK - address- BLANK.bru) · [EP039-address_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-address_MAX_PLUS_1 - address- 201 caracteres.bru) · [EP039-description_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-description_MISSING - description- ausente.bru) · [EP039-description_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-description_NULL - description- null.bru) · [EP039-description_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-description_EMPTY - description- EMPTY.bru) · [EP039-description_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-description_BLANK - description- BLANK.bru) · [EP039-description_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-description_MAX_PLUS_1 - description- 256 caracteres.bru) · [EP039-cityId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-cityId_MISSING - cityId- ausente.bru) · [EP039-cityId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-cityId_NULL - cityId- null.bru) · [EP039-cityId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-cityId_TYPE - cityId- tipo incompatible.bru) · [EP039-logo_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP039-logo_MAX_PLUS_1 - logo- 501 caracteres.bru) · [EP039-coverImage_MAX_PLUS_1](../../bruno/TECMARKET-QA/09_ARCHIVOS_Y_CONCURRENCIA/BUSINESS/EP039-coverImage_MAX_PLUS_1 - coverImage- 501 caracteres.bru)

## EP040

`PUT /api/v1/businesses/{id}` — Update business

Fuente: [BusinessController:104](../../src/main/java/com/tecmarket/backend/business/controller/BusinessController.java#L104). Método Java: `update`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('BUSINESS_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |
| request | RequestBody | UpdateBusinessCatalogRequest | @Valid @RequestBody UpdateBusinessCatalogRequest request |

Body completo (UpdateBusinessCatalogRequest):

```json
{
  "name": "Usuario QA",
  "legalName": "Negocio QA",
  "email": "{{email}}",
  "phone": "+573104445566",
  "address": "Avenida 10 #20-30",
  "description": "Servicio técnico especializado y venta de accesorios.",
  "cityId": "{{cityId}}",
  "logo": null,
  "coverImage": null,
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "name": "Usuario QA",
  "legalName": "Negocio QA",
  "email": "{{email}}",
  "phone": "+573104445566",
  "address": "Avenida 10 #20-30",
  "description": "Servicio técnico especializado y venta de accesorios.",
  "cityId": "{{cityId}}"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| name | String | sí | @NotBlank @Size(max = 150) |
| legalName | String | sí | @NotBlank @Size(max = 200) |
| email | String | sí | @NotBlank @Email @Size(max = 150) |
| phone | String | sí | @NotBlank @Size(max = 30) |
| address | String | sí | @NotBlank @Size(max = 200) |
| description | String | sí | @NotBlank @Size(max = 255) |
| cityId | Integer | sí | @NotNull |
| logo | String | no | @Size(max = 500) |
| coverImage | String | no | @Size(max = 500) |
| active | Boolean | no |  |

Casos Bruno: [EP040-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS/EP040-OK - Contrato válido.bru) · [EP040-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP040-NO_TOKEN - Sin autenticación.bru) · [EP040-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP040-INVALID_TOKEN - Token inválido.bru) · [EP040-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP040-REVOKED - Token previamente revocado.bru) · [EP040-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS/EP040-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP040-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-name_MISSING - name- ausente.bru) · [EP040-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-name_NULL - name- null.bru) · [EP040-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-name_EMPTY - name- EMPTY.bru) · [EP040-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-name_BLANK - name- BLANK.bru) · [EP040-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-name_MAX_PLUS_1 - name- 151 caracteres.bru) · [EP040-legalName_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-legalName_MISSING - legalName- ausente.bru) · [EP040-legalName_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-legalName_NULL - legalName- null.bru) · [EP040-legalName_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-legalName_EMPTY - legalName- EMPTY.bru) · [EP040-legalName_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-legalName_BLANK - legalName- BLANK.bru) · [EP040-legalName_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-legalName_MAX_PLUS_1 - legalName- 201 caracteres.bru) · [EP040-email_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-email_MISSING - email- ausente.bru) · [EP040-email_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-email_NULL - email- null.bru) · [EP040-email_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-email_EMPTY - email- EMPTY.bru) · [EP040-email_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-email_BLANK - email- BLANK.bru) · [EP040-email_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-email_MAX_PLUS_1 - email- 151 caracteres.bru) · [EP040-email_EMAIL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-email_EMAIL - email- correo sin formato válido.bru) · [EP040-phone_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-phone_MISSING - phone- ausente.bru) · [EP040-phone_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-phone_NULL - phone- null.bru) · [EP040-phone_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-phone_EMPTY - phone- EMPTY.bru) · [EP040-phone_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-phone_BLANK - phone- BLANK.bru) · [EP040-phone_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-phone_MAX_PLUS_1 - phone- 31 caracteres.bru) · [EP040-address_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-address_MISSING - address- ausente.bru) · [EP040-address_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-address_NULL - address- null.bru) · [EP040-address_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-address_EMPTY - address- EMPTY.bru) · [EP040-address_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-address_BLANK - address- BLANK.bru) · [EP040-address_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-address_MAX_PLUS_1 - address- 201 caracteres.bru) · [EP040-description_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-description_MISSING - description- ausente.bru) · [EP040-description_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-description_NULL - description- null.bru) · [EP040-description_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-description_EMPTY - description- EMPTY.bru) · [EP040-description_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-description_BLANK - description- BLANK.bru) · [EP040-description_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-description_MAX_PLUS_1 - description- 256 caracteres.bru) · [EP040-cityId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-cityId_MISSING - cityId- ausente.bru) · [EP040-cityId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-cityId_NULL - cityId- null.bru) · [EP040-cityId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-cityId_TYPE - cityId- tipo incompatible.bru) · [EP040-logo_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-logo_MAX_PLUS_1 - logo- 501 caracteres.bru) · [EP040-coverImage_MAX_PLUS_1](../../bruno/TECMARKET-QA/09_ARCHIVOS_Y_CONCURRENCIA/BUSINESS/EP040-coverImage_MAX_PLUS_1 - coverImage- 501 caracteres.bru) · [EP040-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS/EP040-active_TYPE - active- tipo incompatible.bru)

## EP041

`POST /api/v1/businesses/{id}/logo` — Upload business logo

Fuente: [BusinessController:115](../../src/main/java/com/tecmarket/backend/business/controller/BusinessController.java#L115). Método Java: `uploadLogo`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('BUSINESS_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |
| file | RequestPart | MultipartFile | @RequestPart("file") MultipartFile file |

Body multipart: seleccionar archivo real en el campo `file`; Bruno genera el boundary. No usar JSON.

Casos Bruno: [EP041-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/BUSINESS/EP041-OK - Contrato válido.bru) · [EP041-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP041-NO_TOKEN - Sin autenticación.bru) · [EP041-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP041-INVALID_TOKEN - Token inválido.bru) · [EP041-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP041-REVOKED - Token previamente revocado.bru) · [EP041-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS/EP041-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP042

`POST /api/v1/businesses/{id}/cover` — Upload business cover

Fuente: [BusinessController:126](../../src/main/java/com/tecmarket/backend/business/controller/BusinessController.java#L126). Método Java: `uploadCover`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('BUSINESS_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |
| file | RequestPart | MultipartFile | @RequestPart("file") MultipartFile file |

Body multipart: seleccionar archivo real en el campo `file`; Bruno genera el boundary. No usar JSON.

Casos Bruno: [EP042-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/BUSINESS/EP042-OK - Contrato válido.bru) · [EP042-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP042-NO_TOKEN - Sin autenticación.bru) · [EP042-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP042-INVALID_TOKEN - Token inválido.bru) · [EP042-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP042-REVOKED - Token previamente revocado.bru) · [EP042-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS/EP042-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP043

`DELETE /api/v1/businesses/{id}` — Disable business

Fuente: [BusinessController:137](../../src/main/java/com/tecmarket/backend/business/controller/BusinessController.java#L137). Método Java: `delete`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('BUSINESS_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP043-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS/EP043-OK - Contrato válido.bru) · [EP043-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP043-NO_TOKEN - Sin autenticación.bru) · [EP043-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP043-INVALID_TOKEN - Token inválido.bru) · [EP043-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS/EP043-REVOKED - Token previamente revocado.bru) · [EP043-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS/EP043-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP044

`GET /api/v1/businesses/{businessId}/services` — List business services

Fuente: [BusinessOfferingController:42](../../src/main/java/com/tecmarket/backend/business/controller/BusinessOfferingController.java#L42). Método Java: `findAll`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_SERVICE_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<BusinessOfferingResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP044-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS OFFERING/EP044-OK - Contrato válido.bru) · [EP044-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP044-NO_TOKEN - Sin autenticación.bru) · [EP044-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP044-INVALID_TOKEN - Token inválido.bru) · [EP044-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP044-REVOKED - Token previamente revocado.bru) · [EP044-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP044-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP044-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP044-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP044-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP044-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP044-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP044-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP044-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP044-QUERY_0_Min - Query 0- límite inválido.bru) · [EP044-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP044-QUERY_20_Min - Query 20- límite inválido.bru) · [EP044-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP044-QUERY_20_Max - Query 20- límite inválido.bru) · [EP044-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP044-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP044-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP044-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP044-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP044-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP045

`GET /api/v1/businesses/{businessId}/services/{offeringId}` — Find business service by ID

Fuente: [BusinessOfferingController:59](../../src/main/java/com/tecmarket/backend/business/controller/BusinessOfferingController.java#L59). Método Java: `findById`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_SERVICE_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessOfferingResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| offeringId | PathVariable | Integer | @PathVariable Integer offeringId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP045-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS OFFERING/EP045-OK - Contrato válido.bru) · [EP045-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP045-NO_TOKEN - Sin autenticación.bru) · [EP045-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP045-INVALID_TOKEN - Token inválido.bru) · [EP045-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP045-REVOKED - Token previamente revocado.bru) · [EP045-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP045-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP045-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP045-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP045-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP045-NOT_FOUND - ID inexistente comprobado.bru) · [EP045-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP045-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP045-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP045-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP045-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP045-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP046

`POST /api/v1/businesses/{businessId}/services` — Create business service

Fuente: [BusinessOfferingController:74](../../src/main/java/com/tecmarket/backend/business/controller/BusinessOfferingController.java#L74). Método Java: `create`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_SERVICE_CREATE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<BusinessOfferingResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| request | RequestBody | CreateBusinessOfferingRequest | @Valid @RequestBody CreateBusinessOfferingRequest request |

Body completo (CreateBusinessOfferingRequest):

```json
{
  "serviceCategoryId": "{{serviceCategoryId}}",
  "deviceTypeId": "{{deviceTypeId}}",
  "name": "Usuario QA",
  "description": "Reemplazo de pantalla con repuesto compatible y prueba de display.",
  "basePrice": 350000,
  "estimatedTimeMinutes": 120
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "serviceCategoryId": "{{serviceCategoryId}}",
  "deviceTypeId": "{{deviceTypeId}}",
  "name": "Usuario QA",
  "description": "Reemplazo de pantalla con repuesto compatible y prueba de display.",
  "basePrice": 350000,
  "estimatedTimeMinutes": 120
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| serviceCategoryId | Integer | sí | @NotNull |
| deviceTypeId | Integer | sí | @NotNull |
| name | String | sí | @NotBlank @Size(max = 50) |
| description | String | sí | @NotBlank @Size(max = 200) |
| basePrice | BigDecimal | sí | @NotNull @DecimalMin(value = "0.0", inclusive = false) |
| estimatedTimeMinutes | Integer | sí | @NotNull @Positive |

Casos Bruno: [EP046-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS OFFERING/EP046-OK - Contrato válido.bru) · [EP046-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP046-NO_TOKEN - Sin autenticación.bru) · [EP046-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP046-INVALID_TOKEN - Token inválido.bru) · [EP046-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP046-REVOKED - Token previamente revocado.bru) · [EP046-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP046-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP046-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP046-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP046-serviceCategoryId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-serviceCategoryId_MISSING - serviceCategoryId- ausente.bru) · [EP046-serviceCategoryId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-serviceCategoryId_NULL - serviceCategoryId- null.bru) · [EP046-serviceCategoryId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-serviceCategoryId_TYPE - serviceCategoryId- tipo incompatible.bru) · [EP046-deviceTypeId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-deviceTypeId_MISSING - deviceTypeId- ausente.bru) · [EP046-deviceTypeId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-deviceTypeId_NULL - deviceTypeId- null.bru) · [EP046-deviceTypeId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-deviceTypeId_TYPE - deviceTypeId- tipo incompatible.bru) · [EP046-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-name_MISSING - name- ausente.bru) · [EP046-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-name_NULL - name- null.bru) · [EP046-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-name_EMPTY - name- EMPTY.bru) · [EP046-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-name_BLANK - name- BLANK.bru) · [EP046-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-name_MAX_PLUS_1 - name- 51 caracteres.bru) · [EP046-description_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-description_MISSING - description- ausente.bru) · [EP046-description_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-description_NULL - description- null.bru) · [EP046-description_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-description_EMPTY - description- EMPTY.bru) · [EP046-description_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-description_BLANK - description- BLANK.bru) · [EP046-description_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-description_MAX_PLUS_1 - description- 201 caracteres.bru) · [EP046-basePrice_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-basePrice_MISSING - basePrice- ausente.bru) · [EP046-basePrice_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-basePrice_NULL - basePrice- null.bru) · [EP046-basePrice_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-basePrice_TYPE - basePrice- tipo incompatible.bru) · [EP046-basePrice_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-basePrice_DecimalMin - basePrice- fuera de límite DecimalMin.bru) · [EP046-estimatedTimeMinutes_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-estimatedTimeMinutes_MISSING - estimatedTimeMinutes- ausente.bru) · [EP046-estimatedTimeMinutes_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-estimatedTimeMinutes_NULL - estimatedTimeMinutes- null.bru) · [EP046-estimatedTimeMinutes_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-estimatedTimeMinutes_TYPE - estimatedTimeMinutes- tipo incompatible.bru) · [EP046-estimatedTimeMinutes_Positive](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP046-estimatedTimeMinutes_Positive - estimatedTimeMinutes- fuera de límite Positive.bru) · [EP046-PILOT](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS OFFERING/EP046-PILOT - Create business service.bru) · [EP046-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP046-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP046-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP046-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP046-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP046-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP047

`PUT /api/v1/businesses/{businessId}/services/{offeringId}` — Update business service

Fuente: [BusinessOfferingController:90](../../src/main/java/com/tecmarket/backend/business/controller/BusinessOfferingController.java#L90). Método Java: `update`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_SERVICE_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessOfferingResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| offeringId | PathVariable | Integer | @PathVariable Integer offeringId |
| request | RequestBody | UpdateBusinessOfferingRequest | @Valid @RequestBody UpdateBusinessOfferingRequest request |

Body completo (UpdateBusinessOfferingRequest):

```json
{
  "serviceCategoryId": "{{serviceCategoryId}}",
  "deviceTypeId": "{{deviceTypeId}}",
  "name": "Usuario QA",
  "description": "Reemplazo de pantalla con garantía de instalación.",
  "basePrice": 390000,
  "estimatedTimeMinutes": 150,
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "serviceCategoryId": "{{serviceCategoryId}}",
  "deviceTypeId": "{{deviceTypeId}}",
  "name": "Usuario QA",
  "description": "Reemplazo de pantalla con garantía de instalación.",
  "basePrice": 390000,
  "estimatedTimeMinutes": 150
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| serviceCategoryId | Integer | sí | @NotNull |
| deviceTypeId | Integer | sí | @NotNull |
| name | String | sí | @NotBlank @Size(max = 50) |
| description | String | sí | @NotBlank @Size(max = 200) |
| basePrice | BigDecimal | sí | @NotNull @DecimalMin(value = "0.0", inclusive = false) |
| estimatedTimeMinutes | Integer | sí | @NotNull @Positive |
| active | Boolean | no |  |

Casos Bruno: [EP047-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS OFFERING/EP047-OK - Contrato válido.bru) · [EP047-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP047-NO_TOKEN - Sin autenticación.bru) · [EP047-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP047-INVALID_TOKEN - Token inválido.bru) · [EP047-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP047-REVOKED - Token previamente revocado.bru) · [EP047-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP047-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP047-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP047-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP047-serviceCategoryId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-serviceCategoryId_MISSING - serviceCategoryId- ausente.bru) · [EP047-serviceCategoryId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-serviceCategoryId_NULL - serviceCategoryId- null.bru) · [EP047-serviceCategoryId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-serviceCategoryId_TYPE - serviceCategoryId- tipo incompatible.bru) · [EP047-deviceTypeId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-deviceTypeId_MISSING - deviceTypeId- ausente.bru) · [EP047-deviceTypeId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-deviceTypeId_NULL - deviceTypeId- null.bru) · [EP047-deviceTypeId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-deviceTypeId_TYPE - deviceTypeId- tipo incompatible.bru) · [EP047-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-name_MISSING - name- ausente.bru) · [EP047-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-name_NULL - name- null.bru) · [EP047-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-name_EMPTY - name- EMPTY.bru) · [EP047-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-name_BLANK - name- BLANK.bru) · [EP047-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-name_MAX_PLUS_1 - name- 51 caracteres.bru) · [EP047-description_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-description_MISSING - description- ausente.bru) · [EP047-description_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-description_NULL - description- null.bru) · [EP047-description_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-description_EMPTY - description- EMPTY.bru) · [EP047-description_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-description_BLANK - description- BLANK.bru) · [EP047-description_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-description_MAX_PLUS_1 - description- 201 caracteres.bru) · [EP047-basePrice_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-basePrice_MISSING - basePrice- ausente.bru) · [EP047-basePrice_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-basePrice_NULL - basePrice- null.bru) · [EP047-basePrice_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-basePrice_TYPE - basePrice- tipo incompatible.bru) · [EP047-basePrice_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-basePrice_DecimalMin - basePrice- fuera de límite DecimalMin.bru) · [EP047-estimatedTimeMinutes_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-estimatedTimeMinutes_MISSING - estimatedTimeMinutes- ausente.bru) · [EP047-estimatedTimeMinutes_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-estimatedTimeMinutes_NULL - estimatedTimeMinutes- null.bru) · [EP047-estimatedTimeMinutes_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-estimatedTimeMinutes_TYPE - estimatedTimeMinutes- tipo incompatible.bru) · [EP047-estimatedTimeMinutes_Positive](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-estimatedTimeMinutes_Positive - estimatedTimeMinutes- fuera de límite Positive.bru) · [EP047-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS OFFERING/EP047-active_TYPE - active- tipo incompatible.bru) · [EP047-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP047-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP047-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP047-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP047-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP047-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP048

`POST /api/v1/businesses/{businessId}/services/{offeringId}/image` — Upload service image

Fuente: [BusinessOfferingController:107](../../src/main/java/com/tecmarket/backend/business/controller/BusinessOfferingController.java#L107). Método Java: `uploadImage`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_SERVICE_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessOfferingResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| offeringId | PathVariable | Integer | @PathVariable Integer offeringId |
| file | RequestPart | MultipartFile | @RequestPart("file") MultipartFile file |

Body multipart: seleccionar archivo real en el campo `file`; Bruno genera el boundary. No usar JSON.

Casos Bruno: [EP048-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS OFFERING/EP048-OK - Contrato válido.bru) · [EP048-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP048-NO_TOKEN - Sin autenticación.bru) · [EP048-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP048-INVALID_TOKEN - Token inválido.bru) · [EP048-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP048-REVOKED - Token previamente revocado.bru) · [EP048-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP048-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP048-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP048-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP048-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP048-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP048-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP048-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP048-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP048-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP049

`DELETE /api/v1/businesses/{businessId}/services/{offeringId}` — Disable business service

Fuente: [BusinessOfferingController:124](../../src/main/java/com/tecmarket/backend/business/controller/BusinessOfferingController.java#L124). Método Java: `delete`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_SERVICE_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| offeringId | PathVariable | Integer | @PathVariable Integer offeringId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP049-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS OFFERING/EP049-OK - Contrato válido.bru) · [EP049-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP049-NO_TOKEN - Sin autenticación.bru) · [EP049-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP049-INVALID_TOKEN - Token inválido.bru) · [EP049-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS OFFERING/EP049-REVOKED - Token previamente revocado.bru) · [EP049-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP049-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP049-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP049-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP049-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP049-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP049-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP049-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP049-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS OFFERING/EP049-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP050

`GET /api/v1/businesses/{businessId}/quality-tests` — List business quality tests

Fuente: [BusinessQualityTestController:40](../../src/main/java/com/tecmarket/backend/repairorder/controller/BusinessQualityTestController.java#L40). Método Java: `findAll`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_QUALITY_TEST_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<BusinessQualityTestResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP050-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS QUALITY TEST/EP050-OK - Contrato válido.bru) · [EP050-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS QUALITY TEST/EP050-NO_TOKEN - Sin autenticación.bru) · [EP050-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS QUALITY TEST/EP050-INVALID_TOKEN - Token inválido.bru) · [EP050-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS QUALITY TEST/EP050-REVOKED - Token previamente revocado.bru) · [EP050-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP050-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP050-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP050-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP050-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP050-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP050-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP050-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP050-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP050-QUERY_0_Min - Query 0- límite inválido.bru) · [EP050-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP050-QUERY_20_Min - Query 20- límite inválido.bru) · [EP050-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP050-QUERY_20_Max - Query 20- límite inválido.bru) · [EP050-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP050-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP050-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP050-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP050-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP050-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP051

`GET /api/v1/businesses/{businessId}/quality-tests/{qualityTestId}` — Find business quality test by id

Fuente: [BusinessQualityTestController:56](../../src/main/java/com/tecmarket/backend/repairorder/controller/BusinessQualityTestController.java#L56). Método Java: `findById`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_QUALITY_TEST_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessQualityTestResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| qualityTestId | PathVariable | Integer | @PathVariable Integer qualityTestId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP051-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS QUALITY TEST/EP051-OK - Contrato válido.bru) · [EP051-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS QUALITY TEST/EP051-NO_TOKEN - Sin autenticación.bru) · [EP051-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS QUALITY TEST/EP051-INVALID_TOKEN - Token inválido.bru) · [EP051-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS QUALITY TEST/EP051-REVOKED - Token previamente revocado.bru) · [EP051-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP051-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP051-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP051-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP051-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP051-NOT_FOUND - ID inexistente comprobado.bru) · [EP051-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP051-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP051-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP051-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP051-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP051-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP052

`POST /api/v1/businesses/{businessId}/quality-tests` — Create business quality test

Fuente: [BusinessQualityTestController:70](../../src/main/java/com/tecmarket/backend/repairorder/controller/BusinessQualityTestController.java#L70). Método Java: `create`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_QUALITY_TEST_CREATE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<BusinessQualityTestResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| request | RequestBody | CreateBusinessQualityTestRequest | @Valid @RequestBody CreateBusinessQualityTestRequest request |

Body completo (CreateBusinessQualityTestRequest):

```json
{
  "deviceTypeId": "{{deviceTypeId}}",
  "code": "{{qaCode}}",
  "name": "Usuario QA",
  "description": "Valida touch, brillo y display sin líneas.",
  "required": true,
  "sortOrder": 1
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "deviceTypeId": "{{deviceTypeId}}",
  "code": "{{qaCode}}",
  "name": "Usuario QA",
  "description": "Valida touch, brillo y display sin líneas.",
  "sortOrder": 1
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| deviceTypeId | Integer | sí | @NotNull |
| code | String | sí | @NotBlank @Size(max = 30) |
| name | String | sí | @NotBlank @Size(max = 50) |
| description | String | sí | @NotBlank @Size(max = 200) |
| required | Boolean | no |  |
| sortOrder | Integer | sí | @NotNull @Min(1) |

Casos Bruno: [EP052-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS QUALITY TEST/EP052-OK - Contrato válido.bru) · [EP052-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS QUALITY TEST/EP052-NO_TOKEN - Sin autenticación.bru) · [EP052-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS QUALITY TEST/EP052-INVALID_TOKEN - Token inválido.bru) · [EP052-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS QUALITY TEST/EP052-REVOKED - Token previamente revocado.bru) · [EP052-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP052-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP052-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP052-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP052-deviceTypeId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-deviceTypeId_MISSING - deviceTypeId- ausente.bru) · [EP052-deviceTypeId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-deviceTypeId_NULL - deviceTypeId- null.bru) · [EP052-deviceTypeId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-deviceTypeId_TYPE - deviceTypeId- tipo incompatible.bru) · [EP052-code_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-code_MISSING - code- ausente.bru) · [EP052-code_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-code_NULL - code- null.bru) · [EP052-code_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-code_EMPTY - code- EMPTY.bru) · [EP052-code_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-code_BLANK - code- BLANK.bru) · [EP052-code_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-code_MAX_PLUS_1 - code- 31 caracteres.bru) · [EP052-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-name_MISSING - name- ausente.bru) · [EP052-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-name_NULL - name- null.bru) · [EP052-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-name_EMPTY - name- EMPTY.bru) · [EP052-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-name_BLANK - name- BLANK.bru) · [EP052-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-name_MAX_PLUS_1 - name- 51 caracteres.bru) · [EP052-description_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-description_MISSING - description- ausente.bru) · [EP052-description_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-description_NULL - description- null.bru) · [EP052-description_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-description_EMPTY - description- EMPTY.bru) · [EP052-description_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-description_BLANK - description- BLANK.bru) · [EP052-description_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-description_MAX_PLUS_1 - description- 201 caracteres.bru) · [EP052-required_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-required_TYPE - required- tipo incompatible.bru) · [EP052-sortOrder_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-sortOrder_MISSING - sortOrder- ausente.bru) · [EP052-sortOrder_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-sortOrder_NULL - sortOrder- null.bru) · [EP052-sortOrder_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-sortOrder_TYPE - sortOrder- tipo incompatible.bru) · [EP052-sortOrder_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP052-sortOrder_Min - sortOrder- fuera de límite Min.bru) · [EP052-PILOT](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS QUALITY TEST/EP052-PILOT - Create business quality test.bru) · [EP052-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP052-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP052-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP052-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP052-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP052-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP053

`PUT /api/v1/businesses/{businessId}/quality-tests/{qualityTestId}` — Update business quality test

Fuente: [BusinessQualityTestController:85](../../src/main/java/com/tecmarket/backend/repairorder/controller/BusinessQualityTestController.java#L85). Método Java: `update`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_QUALITY_TEST_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessQualityTestResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| qualityTestId | PathVariable | Integer | @PathVariable Integer qualityTestId |
| request | RequestBody | UpdateBusinessQualityTestRequest | @Valid @RequestBody UpdateBusinessQualityTestRequest request |

Body completo (UpdateBusinessQualityTestRequest):

```json
{
  "deviceTypeId": "{{deviceTypeId}}",
  "code": "{{qaCode}}",
  "name": "Usuario QA",
  "description": "Valida touch, brillo, líneas y píxeles muertos.",
  "required": true,
  "sortOrder": 1,
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "deviceTypeId": "{{deviceTypeId}}",
  "code": "{{qaCode}}",
  "name": "Usuario QA",
  "description": "Valida touch, brillo, líneas y píxeles muertos.",
  "sortOrder": 1
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| deviceTypeId | Integer | sí | @NotNull |
| code | String | sí | @NotBlank @Size(max = 30) |
| name | String | sí | @NotBlank @Size(max = 50) |
| description | String | sí | @NotBlank @Size(max = 200) |
| required | Boolean | no |  |
| sortOrder | Integer | sí | @NotNull @Min(1) |
| active | Boolean | no |  |

Casos Bruno: [EP053-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS QUALITY TEST/EP053-OK - Contrato válido.bru) · [EP053-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS QUALITY TEST/EP053-NO_TOKEN - Sin autenticación.bru) · [EP053-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS QUALITY TEST/EP053-INVALID_TOKEN - Token inválido.bru) · [EP053-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS QUALITY TEST/EP053-REVOKED - Token previamente revocado.bru) · [EP053-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP053-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP053-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP053-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP053-deviceTypeId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-deviceTypeId_MISSING - deviceTypeId- ausente.bru) · [EP053-deviceTypeId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-deviceTypeId_NULL - deviceTypeId- null.bru) · [EP053-deviceTypeId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-deviceTypeId_TYPE - deviceTypeId- tipo incompatible.bru) · [EP053-code_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-code_MISSING - code- ausente.bru) · [EP053-code_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-code_NULL - code- null.bru) · [EP053-code_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-code_EMPTY - code- EMPTY.bru) · [EP053-code_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-code_BLANK - code- BLANK.bru) · [EP053-code_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-code_MAX_PLUS_1 - code- 31 caracteres.bru) · [EP053-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-name_MISSING - name- ausente.bru) · [EP053-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-name_NULL - name- null.bru) · [EP053-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-name_EMPTY - name- EMPTY.bru) · [EP053-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-name_BLANK - name- BLANK.bru) · [EP053-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-name_MAX_PLUS_1 - name- 51 caracteres.bru) · [EP053-description_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-description_MISSING - description- ausente.bru) · [EP053-description_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-description_NULL - description- null.bru) · [EP053-description_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-description_EMPTY - description- EMPTY.bru) · [EP053-description_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-description_BLANK - description- BLANK.bru) · [EP053-description_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-description_MAX_PLUS_1 - description- 201 caracteres.bru) · [EP053-required_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-required_TYPE - required- tipo incompatible.bru) · [EP053-sortOrder_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-sortOrder_MISSING - sortOrder- ausente.bru) · [EP053-sortOrder_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-sortOrder_NULL - sortOrder- null.bru) · [EP053-sortOrder_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-sortOrder_TYPE - sortOrder- tipo incompatible.bru) · [EP053-sortOrder_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-sortOrder_Min - sortOrder- fuera de límite Min.bru) · [EP053-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS QUALITY TEST/EP053-active_TYPE - active- tipo incompatible.bru) · [EP053-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP053-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP053-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP053-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP053-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP053-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP054

`DELETE /api/v1/businesses/{businessId}/quality-tests/{qualityTestId}` — Disable business quality test

Fuente: [BusinessQualityTestController:101](../../src/main/java/com/tecmarket/backend/repairorder/controller/BusinessQualityTestController.java#L101). Método Java: `delete`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_QUALITY_TEST_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| qualityTestId | PathVariable | Integer | @PathVariable Integer qualityTestId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP054-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS QUALITY TEST/EP054-OK - Contrato válido.bru) · [EP054-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS QUALITY TEST/EP054-NO_TOKEN - Sin autenticación.bru) · [EP054-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS QUALITY TEST/EP054-INVALID_TOKEN - Token inválido.bru) · [EP054-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS QUALITY TEST/EP054-REVOKED - Token previamente revocado.bru) · [EP054-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP054-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP054-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP054-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP054-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP054-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP054-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP054-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP054-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS QUALITY TEST/EP054-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP055

`POST /api/v1/business/requests` — Create business request

Fuente: [BusinessRequestController:41](../../src/main/java/com/tecmarket/backend/business/controller/BusinessRequestController.java#L41). Método Java: `create`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('BUSINESS_REQUEST_CREATE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<BusinessRequestResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| request | RequestBody | CreateBusinessRequest | @Valid @RequestBody CreateBusinessRequest request |

Body completo (CreateBusinessRequest):

```json
{
  "documentTypeCode": "NIT",
  "documentNumber": "{{documentNumber}}",
  "businessName": "Negocio QA",
  "legalName": "Negocio QA",
  "email": "{{email}}",
  "phone": "+573001112233",
  "cityId": "{{cityId}}",
  "address": "Calle 123 #45-67",
  "legalFile": "https://mi-cdn.com/docs/camara-comercio.pdf"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "documentTypeCode": "NIT",
  "documentNumber": "{{documentNumber}}",
  "businessName": "Negocio QA",
  "email": "{{email}}",
  "phone": "+573001112233",
  "cityId": "{{cityId}}",
  "legalFile": "https://mi-cdn.com/docs/camara-comercio.pdf"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| documentTypeCode | String | sí | @NotBlank @Size(max = 15) |
| documentNumber | String | sí | @NotBlank @Size(max = 50) |
| businessName | String | sí | @NotBlank @Size(max = 150) |
| legalName | String | no | @Size(max = 200) |
| email | String | sí | @NotBlank @Email @Size(max = 150) |
| phone | String | sí | @NotBlank @Size(max = 50) |
| cityId | Integer | sí | @NotNull |
| address | String | no | @Size(max = 200) |
| legalFile | String | sí | @NotBlank @Size(max = 500) |

Casos Bruno: [EP055-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS REQUEST/EP055-OK - Contrato válido.bru) · [EP055-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REQUEST/EP055-NO_TOKEN - Sin autenticación.bru) · [EP055-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REQUEST/EP055-INVALID_TOKEN - Token inválido.bru) · [EP055-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REQUEST/EP055-REVOKED - Token previamente revocado.bru) · [EP055-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS REQUEST/EP055-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP055-documentTypeCode_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-documentTypeCode_MISSING - documentTypeCode- ausente.bru) · [EP055-documentTypeCode_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-documentTypeCode_NULL - documentTypeCode- null.bru) · [EP055-documentTypeCode_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-documentTypeCode_EMPTY - documentTypeCode- EMPTY.bru) · [EP055-documentTypeCode_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-documentTypeCode_BLANK - documentTypeCode- BLANK.bru) · [EP055-documentTypeCode_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-documentTypeCode_MAX_PLUS_1 - documentTypeCode- 16 caracteres.bru) · [EP055-documentNumber_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-documentNumber_MISSING - documentNumber- ausente.bru) · [EP055-documentNumber_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-documentNumber_NULL - documentNumber- null.bru) · [EP055-documentNumber_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-documentNumber_EMPTY - documentNumber- EMPTY.bru) · [EP055-documentNumber_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-documentNumber_BLANK - documentNumber- BLANK.bru) · [EP055-documentNumber_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-documentNumber_MAX_PLUS_1 - documentNumber- 51 caracteres.bru) · [EP055-businessName_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-businessName_MISSING - businessName- ausente.bru) · [EP055-businessName_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-businessName_NULL - businessName- null.bru) · [EP055-businessName_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-businessName_EMPTY - businessName- EMPTY.bru) · [EP055-businessName_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-businessName_BLANK - businessName- BLANK.bru) · [EP055-businessName_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-businessName_MAX_PLUS_1 - businessName- 151 caracteres.bru) · [EP055-legalName_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-legalName_MAX_PLUS_1 - legalName- 201 caracteres.bru) · [EP055-email_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-email_MISSING - email- ausente.bru) · [EP055-email_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-email_NULL - email- null.bru) · [EP055-email_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-email_EMPTY - email- EMPTY.bru) · [EP055-email_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-email_BLANK - email- BLANK.bru) · [EP055-email_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-email_MAX_PLUS_1 - email- 151 caracteres.bru) · [EP055-email_EMAIL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-email_EMAIL - email- correo sin formato válido.bru) · [EP055-phone_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-phone_MISSING - phone- ausente.bru) · [EP055-phone_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-phone_NULL - phone- null.bru) · [EP055-phone_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-phone_EMPTY - phone- EMPTY.bru) · [EP055-phone_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-phone_BLANK - phone- BLANK.bru) · [EP055-phone_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-phone_MAX_PLUS_1 - phone- 51 caracteres.bru) · [EP055-cityId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-cityId_MISSING - cityId- ausente.bru) · [EP055-cityId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-cityId_NULL - cityId- null.bru) · [EP055-cityId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-cityId_TYPE - cityId- tipo incompatible.bru) · [EP055-address_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP055-address_MAX_PLUS_1 - address- 201 caracteres.bru) · [EP055-legalFile_MISSING](../../bruno/TECMARKET-QA/09_ARCHIVOS_Y_CONCURRENCIA/BUSINESS REQUEST/EP055-legalFile_MISSING - legalFile- ausente.bru) · [EP055-legalFile_NULL](../../bruno/TECMARKET-QA/09_ARCHIVOS_Y_CONCURRENCIA/BUSINESS REQUEST/EP055-legalFile_NULL - legalFile- null.bru) · [EP055-legalFile_EMPTY](../../bruno/TECMARKET-QA/09_ARCHIVOS_Y_CONCURRENCIA/BUSINESS REQUEST/EP055-legalFile_EMPTY - legalFile- EMPTY.bru) · [EP055-legalFile_BLANK](../../bruno/TECMARKET-QA/09_ARCHIVOS_Y_CONCURRENCIA/BUSINESS REQUEST/EP055-legalFile_BLANK - legalFile- BLANK.bru) · [EP055-legalFile_MAX_PLUS_1](../../bruno/TECMARKET-QA/09_ARCHIVOS_Y_CONCURRENCIA/BUSINESS REQUEST/EP055-legalFile_MAX_PLUS_1 - legalFile- 501 caracteres.bru) · [EP055-REQUEST_A](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS REQUEST/EP055-REQUEST_A - Solicitar negocio A.bru) · [EP055-REQUEST_B](../../bruno/TECMARKET-QA/03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/02_PREPARACION/BUSINESS REQUEST/EP055-REQUEST_B - Solicitar negocio B.bru)

## EP056

`GET /api/v1/business/requests/my` — List my business requests

Fuente: [BusinessRequestController:54](../../src/main/java/com/tecmarket/backend/business/controller/BusinessRequestController.java#L54). Método Java: `findMyRequests`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('BUSINESS_REQUEST_VIEW_OWN')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<BusinessRequestResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP056-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS REQUEST/EP056-OK - Contrato válido.bru) · [EP056-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REQUEST/EP056-NO_TOKEN - Sin autenticación.bru) · [EP056-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REQUEST/EP056-INVALID_TOKEN - Token inválido.bru) · [EP056-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REQUEST/EP056-REVOKED - Token previamente revocado.bru) · [EP056-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS REQUEST/EP056-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP056-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP056-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP056-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP056-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP056-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP056-QUERY_0_Min - Query 0- límite inválido.bru) · [EP056-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP056-QUERY_20_Min - Query 20- límite inválido.bru) · [EP056-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP056-QUERY_20_Max - Query 20- límite inválido.bru)

## EP057

`GET /api/v1/business/requests/pending` — List pending business requests

Fuente: [BusinessRequestController:67](../../src/main/java/com/tecmarket/backend/business/controller/BusinessRequestController.java#L67). Método Java: `findPendingRequests`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('BUSINESS_REQUEST_REVIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<BusinessRequestResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP057-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS REQUEST/EP057-OK - Contrato válido.bru) · [EP057-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REQUEST/EP057-NO_TOKEN - Sin autenticación.bru) · [EP057-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REQUEST/EP057-INVALID_TOKEN - Token inválido.bru) · [EP057-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REQUEST/EP057-REVOKED - Token previamente revocado.bru) · [EP057-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS REQUEST/EP057-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP057-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP057-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP057-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP057-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP057-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP057-QUERY_0_Min - Query 0- límite inválido.bru) · [EP057-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP057-QUERY_20_Min - Query 20- límite inválido.bru) · [EP057-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP057-QUERY_20_Max - Query 20- límite inválido.bru)

## EP058

`PUT /api/v1/business/requests/{id}/approve` — Approve business request

Fuente: [BusinessRequestController:81](../../src/main/java/com/tecmarket/backend/business/controller/BusinessRequestController.java#L81). Método Java: `approve`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('BUSINESS_REQUEST_REVIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessRequestResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP058-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/BUSINESS REQUEST/EP058-OK - Contrato válido.bru) · [EP058-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REQUEST/EP058-NO_TOKEN - Sin autenticación.bru) · [EP058-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REQUEST/EP058-INVALID_TOKEN - Token inválido.bru) · [EP058-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REQUEST/EP058-REVOKED - Token previamente revocado.bru) · [EP058-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS REQUEST/EP058-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP058-APPROVE_A](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/02_PREPARACION/BUSINESS REQUEST/EP058-APPROVE_A - Aprobar negocio A.bru) · [EP058-APPROVE_B](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/02_PREPARACION/BUSINESS REQUEST/EP058-APPROVE_B - Aprobar negocio B.bru)

## EP059

`PUT /api/v1/business/requests/{id}/reject` — Reject business request

Fuente: [BusinessRequestController:92](../../src/main/java/com/tecmarket/backend/business/controller/BusinessRequestController.java#L92). Método Java: `reject`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('BUSINESS_REQUEST_REVIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessRequestResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |
| request | RequestBody | RejectBusinessRequest | @Valid @RequestBody RejectBusinessRequest request |

Body completo (RejectBusinessRequest):

```json
{
  "rejectionReason": "Documentación incompleta o inconsistente."
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "rejectionReason": "Documentación incompleta o inconsistente."
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| rejectionReason | String | sí | @NotBlank @Size(max = 1000) |

Casos Bruno: [EP059-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/BUSINESS REQUEST/EP059-OK - Contrato válido.bru) · [EP059-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REQUEST/EP059-NO_TOKEN - Sin autenticación.bru) · [EP059-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REQUEST/EP059-INVALID_TOKEN - Token inválido.bru) · [EP059-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REQUEST/EP059-REVOKED - Token previamente revocado.bru) · [EP059-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS REQUEST/EP059-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP059-rejectionReason_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP059-rejectionReason_MISSING - rejectionReason- ausente.bru) · [EP059-rejectionReason_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP059-rejectionReason_NULL - rejectionReason- null.bru) · [EP059-rejectionReason_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP059-rejectionReason_EMPTY - rejectionReason- EMPTY.bru) · [EP059-rejectionReason_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP059-rejectionReason_BLANK - rejectionReason- BLANK.bru) · [EP059-rejectionReason_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REQUEST/EP059-rejectionReason_MAX_PLUS_1 - rejectionReason- 1001 caracteres.bru)

## EP060

`POST /api/v1/businesses/{businessId}/reviews` — Create business review

Fuente: [BusinessReviewController:38](../../src/main/java/com/tecmarket/backend/review/controller/BusinessReviewController.java#L38). Método Java: `create`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('REVIEW_CREATE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<BusinessReviewResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| request | RequestBody | CreateBusinessReviewRequest | @Valid @RequestBody CreateBusinessReviewRequest request |

Body completo (CreateBusinessReviewRequest):

```json
{
  "repairOrderId": "{{repairOrderId}}",
  "rating": 5,
  "comment": "Excelente atención, diagnóstico claro y entrega a tiempo."
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "repairOrderId": "{{repairOrderId}}",
  "rating": 5
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| repairOrderId | Integer | sí | @NotNull |
| rating | Integer | sí | @NotNull @Min(1) @Max(5) |
| comment | String | no | @Size(max = 500) |

Casos Bruno: [EP060-OK](../../bruno/TECMARKET-QA/06_PUBLICO_CLIENTES_Y_MARKETPLACE/BUSINESS REVIEW/EP060-OK - Contrato válido.bru) · [EP060-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REVIEW/EP060-NO_TOKEN - Sin autenticación.bru) · [EP060-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REVIEW/EP060-INVALID_TOKEN - Token inválido.bru) · [EP060-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REVIEW/EP060-REVOKED - Token previamente revocado.bru) · [EP060-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS REVIEW/EP060-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP060-repairOrderId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REVIEW/EP060-repairOrderId_MISSING - repairOrderId- ausente.bru) · [EP060-repairOrderId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REVIEW/EP060-repairOrderId_NULL - repairOrderId- null.bru) · [EP060-repairOrderId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REVIEW/EP060-repairOrderId_TYPE - repairOrderId- tipo incompatible.bru) · [EP060-rating_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REVIEW/EP060-rating_MISSING - rating- ausente.bru) · [EP060-rating_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REVIEW/EP060-rating_NULL - rating- null.bru) · [EP060-rating_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REVIEW/EP060-rating_TYPE - rating- tipo incompatible.bru) · [EP060-rating_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REVIEW/EP060-rating_Min - rating- fuera de límite Min.bru) · [EP060-rating_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REVIEW/EP060-rating_Max - rating- fuera de límite Max.bru) · [EP060-comment_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REVIEW/EP060-comment_MAX_PLUS_1 - comment- 501 caracteres.bru) · [EP060-NOT_DELIVERED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/BUSINESS REVIEW/EP060-NOT_DELIVERED - Reseña antes de entrega.bru) · [EP060-DUPLICATE_REVIEW](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/BUSINESS REVIEW/EP060-DUPLICATE_REVIEW - Segunda reseña de misma orden.bru)

## EP061

`GET /api/v1/businesses/{businessId}/reviews` — List business reviews

Fuente: [BusinessReviewController:50](../../src/main/java/com/tecmarket/backend/review/controller/BusinessReviewController.java#L50). Método Java: `findByBusiness`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('REVIEW_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<BusinessReviewResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP061-OK](../../bruno/TECMARKET-QA/06_PUBLICO_CLIENTES_Y_MARKETPLACE/BUSINESS REVIEW/EP061-OK - Contrato válido.bru) · [EP061-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REVIEW/EP061-NO_TOKEN - Sin autenticación.bru) · [EP061-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REVIEW/EP061-INVALID_TOKEN - Token inválido.bru) · [EP061-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS REVIEW/EP061-REVOKED - Token previamente revocado.bru) · [EP061-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS REVIEW/EP061-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP061-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS REVIEW/EP061-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP061-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REVIEW/EP061-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP061-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REVIEW/EP061-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP061-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REVIEW/EP061-QUERY_0_Min - Query 0- límite inválido.bru) · [EP061-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REVIEW/EP061-QUERY_20_Min - Query 20- límite inválido.bru) · [EP061-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS REVIEW/EP061-QUERY_20_Max - Query 20- límite inválido.bru)

## EP062

`GET /api/v1/businesses/{businessId}/schedules` — List schedules by business

Fuente: [BusinessScheduleController:41](../../src/main/java/com/tecmarket/backend/business/controller/BusinessScheduleController.java#L41). Método Java: `findAll`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_SCHEDULE_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<BusinessScheduleResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP062-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS SCHEDULE/EP062-OK - Contrato válido.bru) · [EP062-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SCHEDULE/EP062-NO_TOKEN - Sin autenticación.bru) · [EP062-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SCHEDULE/EP062-INVALID_TOKEN - Token inválido.bru) · [EP062-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SCHEDULE/EP062-REVOKED - Token previamente revocado.bru) · [EP062-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP062-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP062-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP062-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP062-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP062-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP062-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP062-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP062-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP062-QUERY_0_Min - Query 0- límite inválido.bru) · [EP062-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP062-QUERY_20_Min - Query 20- límite inválido.bru) · [EP062-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP062-QUERY_20_Max - Query 20- límite inválido.bru) · [EP062-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP062-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP062-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP062-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP062-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP062-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP063

`GET /api/v1/businesses/{businessId}/schedules/{scheduleId}` — Find schedule by id

Fuente: [BusinessScheduleController:54](../../src/main/java/com/tecmarket/backend/business/controller/BusinessScheduleController.java#L54). Método Java: `findById`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_SCHEDULE_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessScheduleResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| scheduleId | PathVariable | Integer | @PathVariable Integer scheduleId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP063-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS SCHEDULE/EP063-OK - Contrato válido.bru) · [EP063-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SCHEDULE/EP063-NO_TOKEN - Sin autenticación.bru) · [EP063-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SCHEDULE/EP063-INVALID_TOKEN - Token inválido.bru) · [EP063-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SCHEDULE/EP063-REVOKED - Token previamente revocado.bru) · [EP063-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP063-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP063-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP063-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP063-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP063-NOT_FOUND - ID inexistente comprobado.bru) · [EP063-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP063-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP063-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP063-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP063-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP063-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP064

`POST /api/v1/businesses/{businessId}/schedules` — Create schedule

Fuente: [BusinessScheduleController:65](../../src/main/java/com/tecmarket/backend/business/controller/BusinessScheduleController.java#L65). Método Java: `create`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_SCHEDULE_CREATE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<BusinessScheduleResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| request | RequestBody | CreateBusinessScheduleRequest | @Valid @RequestBody CreateBusinessScheduleRequest request |

Body completo (CreateBusinessScheduleRequest):

```json
{
  "branchId": "{{branchId}}",
  "dayOfWeek": 1,
  "openTime": "08:00:00",
  "closeTime": "17:00:00",
  "slotOrder": 1
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "branchId": "{{branchId}}",
  "dayOfWeek": 1,
  "openTime": "08:00:00",
  "closeTime": "17:00:00"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| branchId | Integer | sí | @NotNull |
| dayOfWeek | Integer | sí | @NotNull @Min(1) @Max(7) |
| openTime | LocalTime | sí | @NotNull |
| closeTime | LocalTime | sí | @NotNull |
| slotOrder | Integer | no |  |

Casos Bruno: [EP064-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS SCHEDULE/EP064-OK - Contrato válido.bru) · [EP064-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SCHEDULE/EP064-NO_TOKEN - Sin autenticación.bru) · [EP064-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SCHEDULE/EP064-INVALID_TOKEN - Token inválido.bru) · [EP064-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SCHEDULE/EP064-REVOKED - Token previamente revocado.bru) · [EP064-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP064-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP064-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP064-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP064-branchId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP064-branchId_MISSING - branchId- ausente.bru) · [EP064-branchId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP064-branchId_NULL - branchId- null.bru) · [EP064-branchId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP064-branchId_TYPE - branchId- tipo incompatible.bru) · [EP064-dayOfWeek_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP064-dayOfWeek_MISSING - dayOfWeek- ausente.bru) · [EP064-dayOfWeek_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP064-dayOfWeek_NULL - dayOfWeek- null.bru) · [EP064-dayOfWeek_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP064-dayOfWeek_TYPE - dayOfWeek- tipo incompatible.bru) · [EP064-dayOfWeek_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP064-dayOfWeek_Min - dayOfWeek- fuera de límite Min.bru) · [EP064-dayOfWeek_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP064-dayOfWeek_Max - dayOfWeek- fuera de límite Max.bru) · [EP064-openTime_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP064-openTime_MISSING - openTime- ausente.bru) · [EP064-openTime_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP064-openTime_NULL - openTime- null.bru) · [EP064-closeTime_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP064-closeTime_MISSING - closeTime- ausente.bru) · [EP064-closeTime_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP064-closeTime_NULL - closeTime- null.bru) · [EP064-slotOrder_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP064-slotOrder_TYPE - slotOrder- tipo incompatible.bru) · [EP064-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP064-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP064-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP064-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP064-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP064-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP065

`PUT /api/v1/businesses/{businessId}/schedules/{scheduleId}` — Update schedule

Fuente: [BusinessScheduleController:77](../../src/main/java/com/tecmarket/backend/business/controller/BusinessScheduleController.java#L77). Método Java: `update`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_SCHEDULE_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessScheduleResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| scheduleId | PathVariable | Integer | @PathVariable Integer scheduleId |
| request | RequestBody | UpdateBusinessScheduleRequest | @Valid @RequestBody UpdateBusinessScheduleRequest request |

Body completo (UpdateBusinessScheduleRequest):

```json
{
  "dayOfWeek": 1,
  "openTime": "08:00:00",
  "closeTime": "17:00:00",
  "slotOrder": 2,
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "dayOfWeek": 1,
  "openTime": "08:00:00",
  "closeTime": "17:00:00"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| dayOfWeek | Integer | sí | @NotNull @Min(1) @Max(7) |
| openTime | LocalTime | sí | @NotNull |
| closeTime | LocalTime | sí | @NotNull |
| slotOrder | Integer | no |  |
| active | Boolean | no |  |

Casos Bruno: [EP065-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS SCHEDULE/EP065-OK - Contrato válido.bru) · [EP065-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SCHEDULE/EP065-NO_TOKEN - Sin autenticación.bru) · [EP065-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SCHEDULE/EP065-INVALID_TOKEN - Token inválido.bru) · [EP065-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SCHEDULE/EP065-REVOKED - Token previamente revocado.bru) · [EP065-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP065-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP065-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP065-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP065-dayOfWeek_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP065-dayOfWeek_MISSING - dayOfWeek- ausente.bru) · [EP065-dayOfWeek_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP065-dayOfWeek_NULL - dayOfWeek- null.bru) · [EP065-dayOfWeek_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP065-dayOfWeek_TYPE - dayOfWeek- tipo incompatible.bru) · [EP065-dayOfWeek_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP065-dayOfWeek_Min - dayOfWeek- fuera de límite Min.bru) · [EP065-dayOfWeek_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP065-dayOfWeek_Max - dayOfWeek- fuera de límite Max.bru) · [EP065-openTime_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP065-openTime_MISSING - openTime- ausente.bru) · [EP065-openTime_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP065-openTime_NULL - openTime- null.bru) · [EP065-closeTime_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP065-closeTime_MISSING - closeTime- ausente.bru) · [EP065-closeTime_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP065-closeTime_NULL - closeTime- null.bru) · [EP065-slotOrder_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP065-slotOrder_TYPE - slotOrder- tipo incompatible.bru) · [EP065-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS SCHEDULE/EP065-active_TYPE - active- tipo incompatible.bru) · [EP065-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP065-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP065-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP065-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP065-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP065-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP066

`DELETE /api/v1/businesses/{businessId}/schedules/{scheduleId}` — Delete schedule

Fuente: [BusinessScheduleController:89](../../src/main/java/com/tecmarket/backend/business/controller/BusinessScheduleController.java#L89). Método Java: `delete`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_SCHEDULE_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| scheduleId | PathVariable | Integer | @PathVariable Integer scheduleId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP066-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS SCHEDULE/EP066-OK - Contrato válido.bru) · [EP066-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SCHEDULE/EP066-NO_TOKEN - Sin autenticación.bru) · [EP066-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SCHEDULE/EP066-INVALID_TOKEN - Token inválido.bru) · [EP066-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SCHEDULE/EP066-REVOKED - Token previamente revocado.bru) · [EP066-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP066-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP066-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP066-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP066-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP066-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP066-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP066-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP066-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SCHEDULE/EP066-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP067

`GET /api/v1/business-subscriptions` — List business subscriptions

Fuente: [BusinessSubscriptionController:50](../../src/main/java/com/tecmarket/backend/subscription/controller/BusinessSubscriptionController.java#L50). Método Java: `findAll`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('SUBSCRIPTION_VIEW')`. Requiere `FEATURE_SUBSCRIPTIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<BusinessSubscriptionResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|


Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP067-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/BUSINESS SUBSCRIPTION/EP067-OK - Contrato válido.bru) · [EP067-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP067-NO_TOKEN - Sin autenticación.bru) · [EP067-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP067-INVALID_TOKEN - Token inválido.bru) · [EP067-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP067-REVOKED - Token previamente revocado.bru) · [EP067-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SUBSCRIPTION/EP067-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP067-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP067-DISABLED - Funcionalidad deshabilitada.bru)

## EP068

`GET /api/v1/business-subscriptions/{id}` — Find business subscription by ID

Fuente: [BusinessSubscriptionController:59](../../src/main/java/com/tecmarket/backend/subscription/controller/BusinessSubscriptionController.java#L59). Método Java: `findById`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('SUBSCRIPTION_VIEW')`. Requiere `FEATURE_SUBSCRIPTIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessSubscriptionResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP068-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/BUSINESS SUBSCRIPTION/EP068-OK - Contrato válido.bru) · [EP068-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP068-NO_TOKEN - Sin autenticación.bru) · [EP068-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP068-INVALID_TOKEN - Token inválido.bru) · [EP068-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP068-REVOKED - Token previamente revocado.bru) · [EP068-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SUBSCRIPTION/EP068-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP068-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP068-DISABLED - Funcionalidad deshabilitada.bru) · [EP068-NOT_FOUND](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP068-NOT_FOUND - ID inexistente comprobado.bru)

## EP069

`GET /api/v1/businesses/{businessId}/subscription` — Find active business subscription

Fuente: [BusinessSubscriptionController:70](../../src/main/java/com/tecmarket/backend/subscription/controller/BusinessSubscriptionController.java#L70). Método Java: `findActiveByBusiness`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('SUBSCRIPTION_VIEW')`. Requiere `FEATURE_SUBSCRIPTIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessSubscriptionResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP069-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/BUSINESS SUBSCRIPTION/EP069-OK - Contrato válido.bru) · [EP069-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP069-NO_TOKEN - Sin autenticación.bru) · [EP069-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP069-INVALID_TOKEN - Token inválido.bru) · [EP069-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP069-REVOKED - Token previamente revocado.bru) · [EP069-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SUBSCRIPTION/EP069-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP069-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP069-DISABLED - Funcionalidad deshabilitada.bru)

## EP070

`POST /api/v1/businesses/{businessId}/subscription` — Create business subscription

Fuente: [BusinessSubscriptionController:81](../../src/main/java/com/tecmarket/backend/subscription/controller/BusinessSubscriptionController.java#L81). Método Java: `create`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('SUBSCRIPTION_MANAGE')`. Requiere `FEATURE_SUBSCRIPTIONS_ENABLED=true`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<BusinessSubscriptionResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| request | RequestBody | CreateBusinessSubscriptionRequest | @Valid @RequestBody CreateBusinessSubscriptionRequest request |

Body completo (CreateBusinessSubscriptionRequest):

```json
{
  "planId": "{{planId}}",
  "status": "TRIALING",
  "billingCycle": "MONTHLY",
  "currentPeriodStart": "{{currentPeriodStart}}",
  "currentPeriodEnd": "{{currentPeriodEnd}}",
  "trialEndsAt": "{{trialEndsAt}}",
  "cancelAtPeriodEnd": false,
  "paymentProvider": "MANUAL",
  "providerCustomerId": "{{providerCustomerId}}",
  "providerSubscriptionId": "{{providerSubscriptionId}}",
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "planId": "{{planId}}",
  "status": "TRIALING",
  "billingCycle": "MONTHLY",
  "currentPeriodStart": "{{currentPeriodStart}}",
  "currentPeriodEnd": "{{currentPeriodEnd}}",
  "paymentProvider": "MANUAL"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| planId | Integer | sí | @NotNull |
| status | SubscriptionStatus | sí | @NotNull |
| billingCycle | BillingCycle | sí | @NotNull |
| currentPeriodStart | LocalDateTime | sí | @NotNull |
| currentPeriodEnd | LocalDateTime | sí | @NotNull |
| trialEndsAt | LocalDateTime | no |  |
| cancelAtPeriodEnd | Boolean | no |  |
| paymentProvider | PaymentProvider | sí | @NotNull |
| providerCustomerId | String | no | @Size(max = 150) |
| providerSubscriptionId | String | no | @Size(max = 150) |
| active | Boolean | no |  |

Casos Bruno: [EP070-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/BUSINESS SUBSCRIPTION/EP070-OK - Contrato válido.bru) · [EP070-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP070-NO_TOKEN - Sin autenticación.bru) · [EP070-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP070-INVALID_TOKEN - Token inválido.bru) · [EP070-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP070-REVOKED - Token previamente revocado.bru) · [EP070-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SUBSCRIPTION/EP070-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP070-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-DISABLED - Funcionalidad deshabilitada.bru) · [EP070-planId_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-planId_MISSING - planId- ausente.bru) · [EP070-planId_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-planId_NULL - planId- null.bru) · [EP070-planId_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-planId_TYPE - planId- tipo incompatible.bru) · [EP070-status_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-status_MISSING - status- ausente.bru) · [EP070-status_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-status_NULL - status- null.bru) · [EP070-billingCycle_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-billingCycle_MISSING - billingCycle- ausente.bru) · [EP070-billingCycle_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-billingCycle_NULL - billingCycle- null.bru) · [EP070-currentPeriodStart_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-currentPeriodStart_MISSING - currentPeriodStart- ausente.bru) · [EP070-currentPeriodStart_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-currentPeriodStart_NULL - currentPeriodStart- null.bru) · [EP070-currentPeriodStart_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-currentPeriodStart_TYPE - currentPeriodStart- tipo incompatible.bru) · [EP070-currentPeriodEnd_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-currentPeriodEnd_MISSING - currentPeriodEnd- ausente.bru) · [EP070-currentPeriodEnd_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-currentPeriodEnd_NULL - currentPeriodEnd- null.bru) · [EP070-currentPeriodEnd_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-currentPeriodEnd_TYPE - currentPeriodEnd- tipo incompatible.bru) · [EP070-trialEndsAt_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-trialEndsAt_TYPE - trialEndsAt- tipo incompatible.bru) · [EP070-cancelAtPeriodEnd_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-cancelAtPeriodEnd_TYPE - cancelAtPeriodEnd- tipo incompatible.bru) · [EP070-paymentProvider_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-paymentProvider_MISSING - paymentProvider- ausente.bru) · [EP070-paymentProvider_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-paymentProvider_NULL - paymentProvider- null.bru) · [EP070-providerCustomerId_MAX_PLUS_1](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-providerCustomerId_MAX_PLUS_1 - providerCustomerId- 151 caracteres.bru) · [EP070-providerSubscriptionId_MAX_PLUS_1](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-providerSubscriptionId_MAX_PLUS_1 - providerSubscriptionId- 151 caracteres.bru) · [EP070-active_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP070-active_TYPE - active- tipo incompatible.bru)

## EP071

`PUT /api/v1/business-subscriptions/{id}` — Update business subscription

Fuente: [BusinessSubscriptionController:94](../../src/main/java/com/tecmarket/backend/subscription/controller/BusinessSubscriptionController.java#L94). Método Java: `update`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('SUBSCRIPTION_MANAGE')`. Requiere `FEATURE_SUBSCRIPTIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessSubscriptionResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |
| request | RequestBody | UpdateBusinessSubscriptionRequest | @Valid @RequestBody UpdateBusinessSubscriptionRequest request |

Body completo (UpdateBusinessSubscriptionRequest):

```json
{
  "planId": "{{planId}}",
  "status": "ACTIVE",
  "billingCycle": "MONTHLY",
  "currentPeriodStart": "{{currentPeriodStart}}",
  "currentPeriodEnd": "{{currentPeriodEnd}}",
  "trialEndsAt": "{{trialEndsAt}}",
  "cancelAtPeriodEnd": false,
  "paymentProvider": "MANUAL",
  "providerCustomerId": "{{providerCustomerId}}",
  "providerSubscriptionId": "{{providerSubscriptionId}}",
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "planId": "{{planId}}",
  "status": "ACTIVE",
  "billingCycle": "MONTHLY",
  "currentPeriodStart": "{{currentPeriodStart}}",
  "currentPeriodEnd": "{{currentPeriodEnd}}",
  "cancelAtPeriodEnd": false,
  "paymentProvider": "MANUAL",
  "active": true
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| planId | Integer | sí | @NotNull |
| status | SubscriptionStatus | sí | @NotNull |
| billingCycle | BillingCycle | sí | @NotNull |
| currentPeriodStart | LocalDateTime | sí | @NotNull |
| currentPeriodEnd | LocalDateTime | sí | @NotNull |
| trialEndsAt | LocalDateTime | no |  |
| cancelAtPeriodEnd | Boolean | sí | @NotNull |
| paymentProvider | PaymentProvider | sí | @NotNull |
| providerCustomerId | String | no | @Size(max = 150) |
| providerSubscriptionId | String | no | @Size(max = 150) |
| active | Boolean | sí | @NotNull |

Casos Bruno: [EP071-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/BUSINESS SUBSCRIPTION/EP071-OK - Contrato válido.bru) · [EP071-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP071-NO_TOKEN - Sin autenticación.bru) · [EP071-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP071-INVALID_TOKEN - Token inválido.bru) · [EP071-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP071-REVOKED - Token previamente revocado.bru) · [EP071-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SUBSCRIPTION/EP071-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP071-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-DISABLED - Funcionalidad deshabilitada.bru) · [EP071-planId_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-planId_MISSING - planId- ausente.bru) · [EP071-planId_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-planId_NULL - planId- null.bru) · [EP071-planId_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-planId_TYPE - planId- tipo incompatible.bru) · [EP071-status_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-status_MISSING - status- ausente.bru) · [EP071-status_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-status_NULL - status- null.bru) · [EP071-billingCycle_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-billingCycle_MISSING - billingCycle- ausente.bru) · [EP071-billingCycle_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-billingCycle_NULL - billingCycle- null.bru) · [EP071-currentPeriodStart_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-currentPeriodStart_MISSING - currentPeriodStart- ausente.bru) · [EP071-currentPeriodStart_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-currentPeriodStart_NULL - currentPeriodStart- null.bru) · [EP071-currentPeriodStart_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-currentPeriodStart_TYPE - currentPeriodStart- tipo incompatible.bru) · [EP071-currentPeriodEnd_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-currentPeriodEnd_MISSING - currentPeriodEnd- ausente.bru) · [EP071-currentPeriodEnd_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-currentPeriodEnd_NULL - currentPeriodEnd- null.bru) · [EP071-currentPeriodEnd_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-currentPeriodEnd_TYPE - currentPeriodEnd- tipo incompatible.bru) · [EP071-trialEndsAt_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-trialEndsAt_TYPE - trialEndsAt- tipo incompatible.bru) · [EP071-cancelAtPeriodEnd_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-cancelAtPeriodEnd_MISSING - cancelAtPeriodEnd- ausente.bru) · [EP071-cancelAtPeriodEnd_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-cancelAtPeriodEnd_NULL - cancelAtPeriodEnd- null.bru) · [EP071-cancelAtPeriodEnd_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-cancelAtPeriodEnd_TYPE - cancelAtPeriodEnd- tipo incompatible.bru) · [EP071-paymentProvider_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-paymentProvider_MISSING - paymentProvider- ausente.bru) · [EP071-paymentProvider_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-paymentProvider_NULL - paymentProvider- null.bru) · [EP071-providerCustomerId_MAX_PLUS_1](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-providerCustomerId_MAX_PLUS_1 - providerCustomerId- 151 caracteres.bru) · [EP071-providerSubscriptionId_MAX_PLUS_1](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-providerSubscriptionId_MAX_PLUS_1 - providerSubscriptionId- 151 caracteres.bru) · [EP071-active_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-active_MISSING - active- ausente.bru) · [EP071-active_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-active_NULL - active- null.bru) · [EP071-active_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP071-active_TYPE - active- tipo incompatible.bru)

## EP072

`DELETE /api/v1/business-subscriptions/{id}` — Cancel business subscription

Fuente: [BusinessSubscriptionController:106](../../src/main/java/com/tecmarket/backend/subscription/controller/BusinessSubscriptionController.java#L106). Método Java: `cancel`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('SUBSCRIPTION_MANAGE')`. Requiere `FEATURE_SUBSCRIPTIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP072-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/BUSINESS SUBSCRIPTION/EP072-OK - Contrato válido.bru) · [EP072-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP072-NO_TOKEN - Sin autenticación.bru) · [EP072-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP072-INVALID_TOKEN - Token inválido.bru) · [EP072-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP072-REVOKED - Token previamente revocado.bru) · [EP072-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SUBSCRIPTION/EP072-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP072-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP072-DISABLED - Funcionalidad deshabilitada.bru)

## EP073

`GET /api/v1/business-subscriptions/{id}/usage` — Find subscription usage

Fuente: [BusinessSubscriptionController:116](../../src/main/java/com/tecmarket/backend/subscription/controller/BusinessSubscriptionController.java#L116). Método Java: `findUsage`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('SUBSCRIPTION_VIEW')`. Requiere `FEATURE_SUBSCRIPTIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<SubscriptionUsageResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP073-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/BUSINESS SUBSCRIPTION/EP073-OK - Contrato válido.bru) · [EP073-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP073-NO_TOKEN - Sin autenticación.bru) · [EP073-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP073-INVALID_TOKEN - Token inválido.bru) · [EP073-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP073-REVOKED - Token previamente revocado.bru) · [EP073-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SUBSCRIPTION/EP073-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP073-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP073-DISABLED - Funcionalidad deshabilitada.bru)

## EP074

`POST /api/v1/business-subscriptions/{id}/usage/recalculate` — Recalculate subscription usage

Fuente: [BusinessSubscriptionController:127](../../src/main/java/com/tecmarket/backend/subscription/controller/BusinessSubscriptionController.java#L127). Método Java: `recalculateUsage`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('SUBSCRIPTION_MANAGE')`. Requiere `FEATURE_SUBSCRIPTIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<SubscriptionUsageResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP074-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/BUSINESS SUBSCRIPTION/EP074-OK - Contrato válido.bru) · [EP074-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP074-NO_TOKEN - Sin autenticación.bru) · [EP074-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP074-INVALID_TOKEN - Token inválido.bru) · [EP074-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS SUBSCRIPTION/EP074-REVOKED - Token previamente revocado.bru) · [EP074-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS SUBSCRIPTION/EP074-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP074-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/BUSINESS SUBSCRIPTION/EP074-DISABLED - Funcionalidad deshabilitada.bru)

## EP075

`GET /api/v1/businesses/{businessId}/users` — List users by business

Fuente: [BusinessUserController:44](../../src/main/java/com/tecmarket/backend/business/controller/BusinessUserController.java#L44). Método Java: `findAll`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_USER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<BusinessUserResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP075-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS USER/EP075-OK - Contrato válido.bru) · [EP075-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP075-NO_TOKEN - Sin autenticación.bru) · [EP075-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP075-INVALID_TOKEN - Token inválido.bru) · [EP075-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP075-REVOKED - Token previamente revocado.bru) · [EP075-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS USER/EP075-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP075-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS USER/EP075-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP075-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP075-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP075-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP075-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP075-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP075-QUERY_0_Min - Query 0- límite inválido.bru) · [EP075-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP075-QUERY_20_Min - Query 20- límite inválido.bru) · [EP075-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP075-QUERY_20_Max - Query 20- límite inválido.bru)

## EP076

`GET /api/v1/businesses/{businessId}/users/role-options` — List assignable business roles

Fuente: [BusinessUserController:57](../../src/main/java/com/tecmarket/backend/business/controller/BusinessUserController.java#L57). Método Java: `findRoleOptions`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAnyAuthority('BUSINESS_USER_VIEW','BUSINESS_USER_CREATE','BUSINESS_USER_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<BusinessRoleOptionResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP076-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS USER/EP076-OK - Contrato válido.bru) · [EP076-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP076-NO_TOKEN - Sin autenticación.bru) · [EP076-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP076-INVALID_TOKEN - Token inválido.bru) · [EP076-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP076-REVOKED - Token previamente revocado.bru) · [EP076-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS USER/EP076-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP076-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS USER/EP076-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru)

## EP077

`GET /api/v1/businesses/{businessId}/users/available-users` — Find available user by email for business assignment

Fuente: [BusinessUserController:67](../../src/main/java/com/tecmarket/backend/business/controller/BusinessUserController.java#L67). Método Java: `findAvailableUsers`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_USER_CREATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<BusinessUserCandidateResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| email | RequestParam | String | @RequestParam(value = "email", required = false) String email |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP077-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS USER/EP077-OK - Contrato válido.bru) · [EP077-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP077-NO_TOKEN - Sin autenticación.bru) · [EP077-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP077-INVALID_TOKEN - Token inválido.bru) · [EP077-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP077-REVOKED - Token previamente revocado.bru) · [EP077-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS USER/EP077-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP077-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS USER/EP077-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP077-FILTER_email](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP077-FILTER_email - Filtro email individual.bru)

## EP078

`GET /api/v1/businesses/{businessId}/users/technicians` — List technicians for repair orders

Fuente: [BusinessUserController:78](../../src/main/java/com/tecmarket/backend/business/controller/BusinessUserController.java#L78). Método Java: `findTechnicians`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAnyAuthority('REPAIR_ORDER_CREATE','REPAIR_ORDER_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<BusinessTechnicianOptionResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| branchId | RequestParam | Integer | @RequestParam(required = false) Integer branchId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP078-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS USER/EP078-OK - Contrato válido.bru) · [EP078-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP078-NO_TOKEN - Sin autenticación.bru) · [EP078-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP078-INVALID_TOKEN - Token inválido.bru) · [EP078-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP078-REVOKED - Token previamente revocado.bru) · [EP078-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS USER/EP078-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP078-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS USER/EP078-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP078-QUERY_branchId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP078-QUERY_branchId_TYPE - Query branchId- tipo inválido.bru) · [EP078-FILTER_branchId](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP078-FILTER_branchId - Filtro branchId individual.bru)

## EP079

`POST /api/v1/businesses/{businessId}/users` — Assign user to business

Fuente: [BusinessUserController:92](../../src/main/java/com/tecmarket/backend/business/controller/BusinessUserController.java#L92). Método Java: `create`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_USER_CREATE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<BusinessUserResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| request | RequestBody | CreateBusinessUserRequest | @Valid @RequestBody CreateBusinessUserRequest request |

Body completo (CreateBusinessUserRequest):

```json
{
  "userId": "{{userId}}",
  "roleId": "{{roleId}}",
  "branchIds": [
    "{{branchId}}"
  ],
  "commissionPercentage": 20
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "userId": "{{userId}}",
  "roleId": "{{roleId}}",
  "branchIds": [
    "{{branchId}}"
  ]
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| userId | Integer | sí | @NotNull |
| roleId | Integer | sí | @NotNull |
| branchIds | List<Integer> | sí | @NotNull @Size(min = 1, max = 50) @NotNull @Positive |
| commissionPercentage | BigDecimal | no | @DecimalMin("0.00") @DecimalMax("100.00") @Digits(integer = 3, fraction = 2) |

Casos Bruno: [EP079-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS USER/EP079-OK - Contrato válido.bru) · [EP079-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP079-NO_TOKEN - Sin autenticación.bru) · [EP079-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP079-INVALID_TOKEN - Token inválido.bru) · [EP079-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP079-REVOKED - Token previamente revocado.bru) · [EP079-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS USER/EP079-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP079-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS USER/EP079-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP079-userId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP079-userId_MISSING - userId- ausente.bru) · [EP079-userId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP079-userId_NULL - userId- null.bru) · [EP079-userId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP079-userId_TYPE - userId- tipo incompatible.bru) · [EP079-roleId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP079-roleId_MISSING - roleId- ausente.bru) · [EP079-roleId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP079-roleId_NULL - roleId- null.bru) · [EP079-roleId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP079-roleId_TYPE - roleId- tipo incompatible.bru) · [EP079-branchIds_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP079-branchIds_MISSING - branchIds- ausente.bru) · [EP079-branchIds_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP079-branchIds_NULL - branchIds- null.bru) · [EP079-branchIds_EMPTY_ARRAY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP079-branchIds_EMPTY_ARRAY - branchIds- colección vacía.bru) · [EP079-branchIds_MAX_ARRAY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP079-branchIds_MAX_ARRAY - branchIds- 51 elementos.bru) · [EP079-branchIds_Positive](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP079-branchIds_Positive - branchIds- fuera de límite Positive.bru) · [EP079-commissionPercentage_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP079-commissionPercentage_TYPE - commissionPercentage- tipo incompatible.bru) · [EP079-commissionPercentage_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP079-commissionPercentage_DecimalMin - commissionPercentage- fuera de límite DecimalMin.bru) · [EP079-commissionPercentage_DecimalMax](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP079-commissionPercentage_DecimalMax - commissionPercentage- fuera de límite DecimalMax.bru) · [EP079-MEMBER_adminA_A](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS USER/EP079-MEMBER_adminA_A - Asignar adminA a A.bru) · [EP079-MEMBER_techA1_A](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS USER/EP079-MEMBER_techA1_A - Asignar techA1 a A.bru) · [EP079-MEMBER_techA2_A](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS USER/EP079-MEMBER_techA2_A - Asignar techA2 a A.bru) · [EP079-MEMBER_advisorA1_A](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS USER/EP079-MEMBER_advisorA1_A - Asignar advisorA1 a A.bru) · [EP079-MEMBER_sellerA1_A](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS USER/EP079-MEMBER_sellerA1_A - Asignar sellerA1 a A.bru) · [EP079-MEMBER_shared_A](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/BUSINESS USER/EP079-MEMBER_shared_A - Asignar shared a A.bru) · [EP079-MEMBER_shared_B](../../bruno/TECMARKET-QA/03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/02_PREPARACION/BUSINESS USER/EP079-MEMBER_shared_B - Asignar shared a B.bru)

## EP080

`PUT /api/v1/businesses/{businessId}/users/{businessUserId}` — Update business user assignment

Fuente: [BusinessUserController:104](../../src/main/java/com/tecmarket/backend/business/controller/BusinessUserController.java#L104). Método Java: `update`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_USER_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<BusinessUserResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| businessUserId | PathVariable | Integer | @PathVariable Integer businessUserId |
| request | RequestBody | UpdateBusinessUserRequest | @Valid @RequestBody UpdateBusinessUserRequest request |

Body completo (UpdateBusinessUserRequest):

```json
{
  "roleId": "{{roleId}}",
  "branchIds": [
    "{{branchId}}"
  ],
  "active": true,
  "commissionPercentage": 20
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "roleId": "{{roleId}}",
  "branchIds": [
    "{{branchId}}"
  ]
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| roleId | Integer | sí | @NotNull |
| branchIds | List<Integer> | sí | @NotNull @Size(min = 1, max = 50) @NotNull @Positive |
| active | Boolean | no |  |
| commissionPercentage | BigDecimal | no | @DecimalMin("0.00") @DecimalMax("100.00") @Digits(integer = 3, fraction = 2) |

Casos Bruno: [EP080-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS USER/EP080-OK - Contrato válido.bru) · [EP080-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP080-NO_TOKEN - Sin autenticación.bru) · [EP080-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP080-INVALID_TOKEN - Token inválido.bru) · [EP080-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP080-REVOKED - Token previamente revocado.bru) · [EP080-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS USER/EP080-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP080-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS USER/EP080-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP080-roleId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP080-roleId_MISSING - roleId- ausente.bru) · [EP080-roleId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP080-roleId_NULL - roleId- null.bru) · [EP080-roleId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP080-roleId_TYPE - roleId- tipo incompatible.bru) · [EP080-branchIds_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP080-branchIds_MISSING - branchIds- ausente.bru) · [EP080-branchIds_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP080-branchIds_NULL - branchIds- null.bru) · [EP080-branchIds_EMPTY_ARRAY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP080-branchIds_EMPTY_ARRAY - branchIds- colección vacía.bru) · [EP080-branchIds_MAX_ARRAY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP080-branchIds_MAX_ARRAY - branchIds- 51 elementos.bru) · [EP080-branchIds_Positive](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP080-branchIds_Positive - branchIds- fuera de límite Positive.bru) · [EP080-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP080-active_TYPE - active- tipo incompatible.bru) · [EP080-commissionPercentage_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP080-commissionPercentage_TYPE - commissionPercentage- tipo incompatible.bru) · [EP080-commissionPercentage_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP080-commissionPercentage_DecimalMin - commissionPercentage- fuera de límite DecimalMin.bru) · [EP080-commissionPercentage_DecimalMax](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/BUSINESS USER/EP080-commissionPercentage_DecimalMax - commissionPercentage- fuera de límite DecimalMax.bru)

## EP081

`DELETE /api/v1/businesses/{businessId}/users/{businessUserId}` — Disable business user assignment

Fuente: [BusinessUserController:116](../../src/main/java/com/tecmarket/backend/business/controller/BusinessUserController.java#L116). Método Java: `delete`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_USER_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| businessUserId | PathVariable | Integer | @PathVariable Integer businessUserId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP081-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/BUSINESS USER/EP081-OK - Contrato válido.bru) · [EP081-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP081-NO_TOKEN - Sin autenticación.bru) · [EP081-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP081-INVALID_TOKEN - Token inválido.bru) · [EP081-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/BUSINESS USER/EP081-REVOKED - Token previamente revocado.bru) · [EP081-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS USER/EP081-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP081-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/BUSINESS USER/EP081-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru)

## EP082

`GET /api/v1/parameters/cities` — List cities

Fuente: [CityParameterController:38](../../src/main/java/com/tecmarket/backend/parameter/controller/CityParameterController.java#L38). Método Java: `findAll`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<CityResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|


Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP082-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/CITY/EP082-OK - Contrato válido.bru) · [EP082-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP082-NO_TOKEN - Sin autenticación.bru) · [EP082-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP082-INVALID_TOKEN - Token inválido.bru) · [EP082-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP082-REVOKED - Token previamente revocado.bru) · [EP082-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CITY/EP082-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP083

`GET /api/v1/parameters/cities/{id}` — Find city by ID

Fuente: [CityParameterController:47](../../src/main/java/com/tecmarket/backend/parameter/controller/CityParameterController.java#L47). Método Java: `findById`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<CityResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP083-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/CITY/EP083-OK - Contrato válido.bru) · [EP083-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP083-NO_TOKEN - Sin autenticación.bru) · [EP083-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP083-INVALID_TOKEN - Token inválido.bru) · [EP083-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP083-REVOKED - Token previamente revocado.bru) · [EP083-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CITY/EP083-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP083-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP083-NOT_FOUND - ID inexistente comprobado.bru)

## EP084

`GET /api/v1/parameters/countries/{countryId}/cities` — List cities by country

Fuente: [CityParameterController:56](../../src/main/java/com/tecmarket/backend/parameter/controller/CityParameterController.java#L56). Método Java: `findByCountryId`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<CityResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| countryId | PathVariable | Integer | @PathVariable Integer countryId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP084-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/CITY/EP084-OK - Contrato válido.bru) · [EP084-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP084-NO_TOKEN - Sin autenticación.bru) · [EP084-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP084-INVALID_TOKEN - Token inválido.bru) · [EP084-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP084-REVOKED - Token previamente revocado.bru) · [EP084-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CITY/EP084-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP085

`GET /api/v1/parameters/departments/{departmentId}/cities` — List cities by department

Fuente: [CityParameterController:65](../../src/main/java/com/tecmarket/backend/parameter/controller/CityParameterController.java#L65). Método Java: `findByDepartmentId`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<CityResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| departmentId | PathVariable | Integer | @PathVariable Integer departmentId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP085-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/CITY/EP085-OK - Contrato válido.bru) · [EP085-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP085-NO_TOKEN - Sin autenticación.bru) · [EP085-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP085-INVALID_TOKEN - Token inválido.bru) · [EP085-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP085-REVOKED - Token previamente revocado.bru) · [EP085-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CITY/EP085-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP086

`POST /api/v1/parameters/cities` — Create city

Fuente: [CityParameterController:74](../../src/main/java/com/tecmarket/backend/parameter/controller/CityParameterController.java#L74). Método Java: `create`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<CityResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| request | RequestBody | CreateCityRequest | @Valid @RequestBody CreateCityRequest request |

Body completo (CreateCityRequest):

```json
{
  "departmentId": "{{departmentId}}",
  "name": "Usuario QA"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "departmentId": "{{departmentId}}",
  "name": "Usuario QA"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| departmentId | Integer | sí | @NotNull |
| name | String | sí | @NotBlank @Size(max = 120) |

Casos Bruno: [EP086-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/CITY/EP086-OK - Contrato válido.bru) · [EP086-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP086-NO_TOKEN - Sin autenticación.bru) · [EP086-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP086-INVALID_TOKEN - Token inválido.bru) · [EP086-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP086-REVOKED - Token previamente revocado.bru) · [EP086-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CITY/EP086-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP086-departmentId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP086-departmentId_MISSING - departmentId- ausente.bru) · [EP086-departmentId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP086-departmentId_NULL - departmentId- null.bru) · [EP086-departmentId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP086-departmentId_TYPE - departmentId- tipo incompatible.bru) · [EP086-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP086-name_MISSING - name- ausente.bru) · [EP086-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP086-name_NULL - name- null.bru) · [EP086-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP086-name_EMPTY - name- EMPTY.bru) · [EP086-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP086-name_BLANK - name- BLANK.bru) · [EP086-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP086-name_MAX_PLUS_1 - name- 121 caracteres.bru)

## EP087

`PUT /api/v1/parameters/cities/{id}` — Update city

Fuente: [CityParameterController:85](../../src/main/java/com/tecmarket/backend/parameter/controller/CityParameterController.java#L85). Método Java: `update`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<CityResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |
| request | RequestBody | UpdateCityRequest | @Valid @RequestBody UpdateCityRequest request |

Body completo (UpdateCityRequest):

```json
{
  "departmentId": "{{departmentId}}",
  "name": "Usuario QA",
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "departmentId": "{{departmentId}}",
  "name": "Usuario QA"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| departmentId | Integer | sí | @NotNull |
| name | String | sí | @NotBlank @Size(max = 120) |
| active | Boolean | no |  |

Casos Bruno: [EP087-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/CITY/EP087-OK - Contrato válido.bru) · [EP087-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP087-NO_TOKEN - Sin autenticación.bru) · [EP087-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP087-INVALID_TOKEN - Token inválido.bru) · [EP087-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP087-REVOKED - Token previamente revocado.bru) · [EP087-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CITY/EP087-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP087-departmentId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP087-departmentId_MISSING - departmentId- ausente.bru) · [EP087-departmentId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP087-departmentId_NULL - departmentId- null.bru) · [EP087-departmentId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP087-departmentId_TYPE - departmentId- tipo incompatible.bru) · [EP087-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP087-name_MISSING - name- ausente.bru) · [EP087-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP087-name_NULL - name- null.bru) · [EP087-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP087-name_EMPTY - name- EMPTY.bru) · [EP087-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP087-name_BLANK - name- BLANK.bru) · [EP087-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP087-name_MAX_PLUS_1 - name- 121 caracteres.bru) · [EP087-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CITY/EP087-active_TYPE - active- tipo incompatible.bru)

## EP088

`DELETE /api/v1/parameters/cities/{id}` — Disable city

Fuente: [CityParameterController:96](../../src/main/java/com/tecmarket/backend/parameter/controller/CityParameterController.java#L96). Método Java: `delete`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP088-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/CITY/EP088-OK - Contrato válido.bru) · [EP088-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP088-NO_TOKEN - Sin autenticación.bru) · [EP088-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP088-INVALID_TOKEN - Token inválido.bru) · [EP088-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CITY/EP088-REVOKED - Token previamente revocado.bru) · [EP088-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CITY/EP088-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP089

`GET /api/v1/businesses/{businessId}/commission-settlements/technician-summaries` — List technician commission summaries

Fuente: [CommissionSettlementController:48](../../src/main/java/com/tecmarket/backend/repairorder/controller/CommissionSettlementController.java#L48). Método Java: `findTechnicianSummaries`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAnyAuthority('BUSINESS_COMMISSION_VIEW','BUSINESS_COMMISSION_MANAGE')`. Requiere `FEATURE_COMMISSIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<TechnicianCommissionSummaryResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| periodStart | RequestParam | LocalDate | @RequestParam LocalDate periodStart |
| periodEnd | RequestParam | LocalDate | @RequestParam LocalDate periodEnd |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP089-OK](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP089-OK - Contrato válido.bru) · [EP089-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP089-NO_TOKEN - Sin autenticación.bru) · [EP089-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP089-INVALID_TOKEN - Token inválido.bru) · [EP089-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP089-REVOKED - Token previamente revocado.bru) · [EP089-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COMMISSION SETTLEMENT/EP089-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP089-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COMMISSION SETTLEMENT/EP089-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP089-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP089-DISABLED - Funcionalidad deshabilitada.bru) · [EP089-QUERY_periodStart_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP089-QUERY_periodStart_TYPE - Query periodStart- tipo inválido.bru) · [EP089-QUERY_periodEnd_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP089-QUERY_periodEnd_TYPE - Query periodEnd- tipo inválido.bru)

## EP090

`GET /api/v1/businesses/{businessId}/commission-settlements/eligible-orders` — List eligible orders

Fuente: [CommissionSettlementController:62](../../src/main/java/com/tecmarket/backend/repairorder/controller/CommissionSettlementController.java#L62). Método Java: `findEligibleOrders`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAnyAuthority('BUSINESS_COMMISSION_VIEW','BUSINESS_COMMISSION_MANAGE')`. Requiere `FEATURE_COMMISSIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<EligibleCommissionOrderResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| businessUserId | RequestParam | Integer | @RequestParam Integer businessUserId |
| periodStart | RequestParam | LocalDate | @RequestParam LocalDate periodStart |
| periodEnd | RequestParam | LocalDate | @RequestParam LocalDate periodEnd |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP090-OK](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP090-OK - Contrato válido.bru) · [EP090-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP090-NO_TOKEN - Sin autenticación.bru) · [EP090-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP090-INVALID_TOKEN - Token inválido.bru) · [EP090-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP090-REVOKED - Token previamente revocado.bru) · [EP090-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COMMISSION SETTLEMENT/EP090-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP090-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COMMISSION SETTLEMENT/EP090-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP090-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP090-DISABLED - Funcionalidad deshabilitada.bru) · [EP090-QUERY_businessUserId_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP090-QUERY_businessUserId_TYPE - Query businessUserId- tipo inválido.bru) · [EP090-QUERY_periodStart_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP090-QUERY_periodStart_TYPE - Query periodStart- tipo inválido.bru) · [EP090-QUERY_periodEnd_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP090-QUERY_periodEnd_TYPE - Query periodEnd- tipo inválido.bru)

## EP091

`POST /api/v1/businesses/{businessId}/commission-settlements` — Create settlement

Fuente: [CommissionSettlementController:77](../../src/main/java/com/tecmarket/backend/repairorder/controller/CommissionSettlementController.java#L77). Método Java: `create`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_COMMISSION_MANAGE')`. Requiere `FEATURE_COMMISSIONS_ENABLED=true`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<CommissionSettlementResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| request | RequestBody | CreateCommissionSettlementRequest | @Valid @RequestBody CreateCommissionSettlementRequest request |

Body completo (CreateCommissionSettlementRequest):

```json
{
  "businessUserId": "{{businessUserId}}",
  "periodStart": "{{periodStart}}",
  "periodEnd": "{{periodEnd}}",
  "repairOrderIds": [
    "{{repairOrderId}}"
  ],
  "notes": "Liquidacion mensual de junio."
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "businessUserId": "{{businessUserId}}",
  "periodStart": "{{periodStart}}",
  "periodEnd": "{{periodEnd}}",
  "repairOrderIds": [
    "{{repairOrderId}}"
  ]
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| businessUserId | Integer | sí | @NotNull |
| periodStart | LocalDate | sí | @NotNull |
| periodEnd | LocalDate | sí | @NotNull |
| repairOrderIds | List<Integer> | sí | @NotEmpty @Size(max = 100) @NotNull @Positive |
| notes | String | no | @Size(max = 500) |

Casos Bruno: [EP091-OK](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-OK - Contrato válido.bru) · [EP091-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP091-NO_TOKEN - Sin autenticación.bru) · [EP091-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP091-INVALID_TOKEN - Token inválido.bru) · [EP091-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP091-REVOKED - Token previamente revocado.bru) · [EP091-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COMMISSION SETTLEMENT/EP091-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP091-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COMMISSION SETTLEMENT/EP091-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP091-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-DISABLED - Funcionalidad deshabilitada.bru) · [EP091-businessUserId_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-businessUserId_MISSING - businessUserId- ausente.bru) · [EP091-businessUserId_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-businessUserId_NULL - businessUserId- null.bru) · [EP091-businessUserId_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-businessUserId_TYPE - businessUserId- tipo incompatible.bru) · [EP091-periodStart_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-periodStart_MISSING - periodStart- ausente.bru) · [EP091-periodStart_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-periodStart_NULL - periodStart- null.bru) · [EP091-periodStart_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-periodStart_TYPE - periodStart- tipo incompatible.bru) · [EP091-periodEnd_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-periodEnd_MISSING - periodEnd- ausente.bru) · [EP091-periodEnd_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-periodEnd_NULL - periodEnd- null.bru) · [EP091-periodEnd_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-periodEnd_TYPE - periodEnd- tipo incompatible.bru) · [EP091-repairOrderIds_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-repairOrderIds_MISSING - repairOrderIds- ausente.bru) · [EP091-repairOrderIds_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-repairOrderIds_NULL - repairOrderIds- null.bru) · [EP091-repairOrderIds_EMPTY_ARRAY](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-repairOrderIds_EMPTY_ARRAY - repairOrderIds- colección vacía.bru) · [EP091-repairOrderIds_MAX_ARRAY](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-repairOrderIds_MAX_ARRAY - repairOrderIds- 101 elementos.bru) · [EP091-repairOrderIds_Positive](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-repairOrderIds_Positive - repairOrderIds- fuera de límite Positive.bru) · [EP091-notes_MAX_PLUS_1](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP091-notes_MAX_PLUS_1 - notes- 501 caracteres.bru)

## EP092

`GET /api/v1/businesses/{businessId}/commission-settlements` — List settlements

Fuente: [CommissionSettlementController:89](../../src/main/java/com/tecmarket/backend/repairorder/controller/CommissionSettlementController.java#L89). Método Java: `findAll`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAnyAuthority('BUSINESS_COMMISSION_VIEW','BUSINESS_COMMISSION_MANAGE')`. Requiere `FEATURE_COMMISSIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<CommissionSettlementResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| businessUserId | RequestParam | Integer | @RequestParam(required = false) Integer businessUserId |
| status | RequestParam | CommissionSettlementStatus | @RequestParam(required = false) CommissionSettlementStatus status |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP092-OK](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP092-OK - Contrato válido.bru) · [EP092-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP092-NO_TOKEN - Sin autenticación.bru) · [EP092-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP092-INVALID_TOKEN - Token inválido.bru) · [EP092-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP092-REVOKED - Token previamente revocado.bru) · [EP092-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COMMISSION SETTLEMENT/EP092-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP092-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COMMISSION SETTLEMENT/EP092-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP092-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP092-DISABLED - Funcionalidad deshabilitada.bru) · [EP092-QUERY_businessUserId_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP092-QUERY_businessUserId_TYPE - Query businessUserId- tipo inválido.bru) · [EP092-FILTER_businessUserId](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP092-FILTER_businessUserId - Filtro businessUserId individual.bru) · [EP092-FILTER_status](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP092-FILTER_status - Filtro status individual.bru) · [EP092-FILTER_businessUserId_status](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP092-FILTER_businessUserId_status - Filtros combinados businessUserId + status.bru)

## EP093

`GET /api/v1/businesses/{businessId}/commission-settlements/{settlementId}` — Find settlement

Fuente: [CommissionSettlementController:102](../../src/main/java/com/tecmarket/backend/repairorder/controller/CommissionSettlementController.java#L102). Método Java: `findById`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAnyAuthority('BUSINESS_COMMISSION_VIEW','BUSINESS_COMMISSION_MANAGE')`. Requiere `FEATURE_COMMISSIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<CommissionSettlementResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| settlementId | PathVariable | Integer | @PathVariable Integer settlementId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP093-OK](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP093-OK - Contrato válido.bru) · [EP093-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP093-NO_TOKEN - Sin autenticación.bru) · [EP093-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP093-INVALID_TOKEN - Token inválido.bru) · [EP093-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP093-REVOKED - Token previamente revocado.bru) · [EP093-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COMMISSION SETTLEMENT/EP093-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP093-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COMMISSION SETTLEMENT/EP093-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP093-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP093-DISABLED - Funcionalidad deshabilitada.bru) · [EP093-NOT_FOUND](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP093-NOT_FOUND - ID inexistente comprobado.bru)

## EP094

`PATCH /api/v1/businesses/{businessId}/commission-settlements/{settlementId}/status` — Update settlement status

Fuente: [CommissionSettlementController:114](../../src/main/java/com/tecmarket/backend/repairorder/controller/CommissionSettlementController.java#L114). Método Java: `updateStatus`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_COMMISSION_MANAGE')`. Requiere `FEATURE_COMMISSIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<CommissionSettlementResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| settlementId | PathVariable | Integer | @PathVariable Integer settlementId |
| request | RequestBody | UpdateCommissionSettlementStatusRequest | @Valid @RequestBody UpdateCommissionSettlementStatusRequest request |

Body completo (UpdateCommissionSettlementStatusRequest):

```json
{
  "status": "APPROVED"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "status": "APPROVED"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| status | CommissionSettlementStatus | sí | @NotNull |

Casos Bruno: [EP094-OK](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP094-OK - Contrato válido.bru) · [EP094-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP094-NO_TOKEN - Sin autenticación.bru) · [EP094-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP094-INVALID_TOKEN - Token inválido.bru) · [EP094-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COMMISSION SETTLEMENT/EP094-REVOKED - Token previamente revocado.bru) · [EP094-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COMMISSION SETTLEMENT/EP094-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP094-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COMMISSION SETTLEMENT/EP094-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP094-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP094-DISABLED - Funcionalidad deshabilitada.bru) · [EP094-status_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP094-status_MISSING - status- ausente.bru) · [EP094-status_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/COMMISSION SETTLEMENT/EP094-status_NULL - status- null.bru)

## EP095

`GET /api/v1/parameters/countries` — List countries

Fuente: [CountryParameterController:38](../../src/main/java/com/tecmarket/backend/parameter/controller/CountryParameterController.java#L38). Método Java: `findAll`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<CountryResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|


Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP095-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/COUNTRY/EP095-OK - Contrato válido.bru) · [EP095-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COUNTRY/EP095-NO_TOKEN - Sin autenticación.bru) · [EP095-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COUNTRY/EP095-INVALID_TOKEN - Token inválido.bru) · [EP095-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COUNTRY/EP095-REVOKED - Token previamente revocado.bru) · [EP095-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COUNTRY/EP095-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP096

`GET /api/v1/parameters/countries/{id}` — Find country by ID

Fuente: [CountryParameterController:47](../../src/main/java/com/tecmarket/backend/parameter/controller/CountryParameterController.java#L47). Método Java: `findById`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<CountryResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP096-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/COUNTRY/EP096-OK - Contrato válido.bru) · [EP096-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COUNTRY/EP096-NO_TOKEN - Sin autenticación.bru) · [EP096-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COUNTRY/EP096-INVALID_TOKEN - Token inválido.bru) · [EP096-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COUNTRY/EP096-REVOKED - Token previamente revocado.bru) · [EP096-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COUNTRY/EP096-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP096-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP096-NOT_FOUND - ID inexistente comprobado.bru)

## EP097

`POST /api/v1/parameters/countries` — Create country

Fuente: [CountryParameterController:56](../../src/main/java/com/tecmarket/backend/parameter/controller/CountryParameterController.java#L56). Método Java: `create`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<CountryResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| request | RequestBody | CreateCountryRequest | @Valid @RequestBody CreateCountryRequest request |

Body completo (CreateCountryRequest):

```json
{
  "code": "{{qaCode}}",
  "name": "Usuario QA"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "code": "{{qaCode}}",
  "name": "Usuario QA"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| code | String | sí | @NotBlank @Size(max = 10) |
| name | String | sí | @NotBlank @Size(max = 100) |

Casos Bruno: [EP097-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/COUNTRY/EP097-OK - Contrato válido.bru) · [EP097-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COUNTRY/EP097-NO_TOKEN - Sin autenticación.bru) · [EP097-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COUNTRY/EP097-INVALID_TOKEN - Token inválido.bru) · [EP097-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COUNTRY/EP097-REVOKED - Token previamente revocado.bru) · [EP097-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COUNTRY/EP097-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP097-code_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP097-code_MISSING - code- ausente.bru) · [EP097-code_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP097-code_NULL - code- null.bru) · [EP097-code_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP097-code_EMPTY - code- EMPTY.bru) · [EP097-code_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP097-code_BLANK - code- BLANK.bru) · [EP097-code_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP097-code_MAX_PLUS_1 - code- 11 caracteres.bru) · [EP097-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP097-name_MISSING - name- ausente.bru) · [EP097-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP097-name_NULL - name- null.bru) · [EP097-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP097-name_EMPTY - name- EMPTY.bru) · [EP097-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP097-name_BLANK - name- BLANK.bru) · [EP097-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP097-name_MAX_PLUS_1 - name- 101 caracteres.bru)

## EP098

`PUT /api/v1/parameters/countries/{id}` — Update country

Fuente: [CountryParameterController:67](../../src/main/java/com/tecmarket/backend/parameter/controller/CountryParameterController.java#L67). Método Java: `update`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<CountryResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |
| request | RequestBody | UpdateCountryRequest | @Valid @RequestBody UpdateCountryRequest request |

Body completo (UpdateCountryRequest):

```json
{
  "name": "Usuario QA",
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "name": "Usuario QA"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| name | String | sí | @NotBlank @Size(max = 100) |
| active | Boolean | no |  |

Casos Bruno: [EP098-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/COUNTRY/EP098-OK - Contrato válido.bru) · [EP098-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COUNTRY/EP098-NO_TOKEN - Sin autenticación.bru) · [EP098-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COUNTRY/EP098-INVALID_TOKEN - Token inválido.bru) · [EP098-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COUNTRY/EP098-REVOKED - Token previamente revocado.bru) · [EP098-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COUNTRY/EP098-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP098-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP098-name_MISSING - name- ausente.bru) · [EP098-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP098-name_NULL - name- null.bru) · [EP098-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP098-name_EMPTY - name- EMPTY.bru) · [EP098-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP098-name_BLANK - name- BLANK.bru) · [EP098-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP098-name_MAX_PLUS_1 - name- 101 caracteres.bru) · [EP098-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/COUNTRY/EP098-active_TYPE - active- tipo incompatible.bru)

## EP099

`DELETE /api/v1/parameters/countries/{id}` — Disable country

Fuente: [CountryParameterController:78](../../src/main/java/com/tecmarket/backend/parameter/controller/CountryParameterController.java#L78). Método Java: `delete`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP099-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/COUNTRY/EP099-OK - Contrato válido.bru) · [EP099-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COUNTRY/EP099-NO_TOKEN - Sin autenticación.bru) · [EP099-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COUNTRY/EP099-INVALID_TOKEN - Token inválido.bru) · [EP099-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/COUNTRY/EP099-REVOKED - Token previamente revocado.bru) · [EP099-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/COUNTRY/EP099-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP100

`GET /api/v1/businesses/{businessId}/customer-devices` — List customer devices by business

Fuente: [CustomerDeviceController:39](../../src/main/java/com/tecmarket/backend/repairorder/controller/CustomerDeviceController.java#L39). Método Java: `findAll`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('CUSTOMER_DEVICE_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<CustomerDeviceResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP100-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/CUSTOMER DEVICE/EP100-OK - Contrato válido.bru) · [EP100-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER DEVICE/EP100-NO_TOKEN - Sin autenticación.bru) · [EP100-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER DEVICE/EP100-INVALID_TOKEN - Token inválido.bru) · [EP100-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER DEVICE/EP100-REVOKED - Token previamente revocado.bru) · [EP100-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP100-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP100-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP100-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP100-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP100-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP100-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP100-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP100-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP100-QUERY_0_Min - Query 0- límite inválido.bru) · [EP100-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP100-QUERY_20_Min - Query 20- límite inválido.bru) · [EP100-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP100-QUERY_20_Max - Query 20- límite inválido.bru) · [EP100-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP100-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP100-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP100-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP100-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP100-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP101

`GET /api/v1/businesses/{businessId}/customer-devices/{customerDeviceId}` — Find customer device by id

Fuente: [CustomerDeviceController:55](../../src/main/java/com/tecmarket/backend/repairorder/controller/CustomerDeviceController.java#L55). Método Java: `findById`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('CUSTOMER_DEVICE_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<CustomerDeviceResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| customerDeviceId | PathVariable | Integer | @PathVariable Integer customerDeviceId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP101-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/CUSTOMER DEVICE/EP101-OK - Contrato válido.bru) · [EP101-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER DEVICE/EP101-NO_TOKEN - Sin autenticación.bru) · [EP101-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER DEVICE/EP101-INVALID_TOKEN - Token inválido.bru) · [EP101-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER DEVICE/EP101-REVOKED - Token previamente revocado.bru) · [EP101-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP101-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP101-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP101-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP101-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP101-NOT_FOUND - ID inexistente comprobado.bru) · [EP101-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP101-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP101-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP101-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP101-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP101-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP102

`POST /api/v1/businesses/{businessId}/customer-devices` — Create customer device

Fuente: [CustomerDeviceController:69](../../src/main/java/com/tecmarket/backend/repairorder/controller/CustomerDeviceController.java#L69). Método Java: `create`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('CUSTOMER_DEVICE_CREATE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<CustomerDeviceResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| request | RequestBody | CreateCustomerDeviceRequest | @Valid @RequestBody CreateCustomerDeviceRequest request |

Body completo (CreateCustomerDeviceRequest):

```json
{
  "customerId": "{{customerId}}",
  "deviceTypeId": "{{deviceTypeId}}",
  "brandId": "{{brandId}}",
  "model": "iPhone 13 Pro",
  "serialNumber": "SN-ABC-12345",
  "imei": "351756051523999",
  "registeredByBusinessUserId": "{{registeredByBusinessUserId}}",
  "color": "Azul",
  "registeredAt": "{{registeredAt}}"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "customerId": "{{customerId}}",
  "deviceTypeId": "{{deviceTypeId}}",
  "brandId": "{{brandId}}",
  "model": "iPhone 13 Pro",
  "registeredByBusinessUserId": "{{registeredByBusinessUserId}}",
  "color": "Azul"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| customerId | Integer | sí | @NotNull |
| deviceTypeId | Integer | sí | @NotNull |
| brandId | Integer | sí | @NotNull |
| model | String | sí | @NotBlank @Size(max = 50) |
| serialNumber | String | no | @Size(max = 60) |
| imei | String | no | @Size(max = 60) |
| registeredByBusinessUserId | Integer | sí | @NotNull |
| color | String | sí | @NotBlank @Size(max = 20) |
| registeredAt | LocalDateTime | no |  |

Casos Bruno: [EP102-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/CUSTOMER DEVICE/EP102-OK - Contrato válido.bru) · [EP102-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER DEVICE/EP102-NO_TOKEN - Sin autenticación.bru) · [EP102-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER DEVICE/EP102-INVALID_TOKEN - Token inválido.bru) · [EP102-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER DEVICE/EP102-REVOKED - Token previamente revocado.bru) · [EP102-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP102-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP102-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP102-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP102-customerId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-customerId_MISSING - customerId- ausente.bru) · [EP102-customerId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-customerId_NULL - customerId- null.bru) · [EP102-customerId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-customerId_TYPE - customerId- tipo incompatible.bru) · [EP102-deviceTypeId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-deviceTypeId_MISSING - deviceTypeId- ausente.bru) · [EP102-deviceTypeId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-deviceTypeId_NULL - deviceTypeId- null.bru) · [EP102-deviceTypeId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-deviceTypeId_TYPE - deviceTypeId- tipo incompatible.bru) · [EP102-brandId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-brandId_MISSING - brandId- ausente.bru) · [EP102-brandId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-brandId_NULL - brandId- null.bru) · [EP102-brandId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-brandId_TYPE - brandId- tipo incompatible.bru) · [EP102-model_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-model_MISSING - model- ausente.bru) · [EP102-model_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-model_NULL - model- null.bru) · [EP102-model_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-model_EMPTY - model- EMPTY.bru) · [EP102-model_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-model_BLANK - model- BLANK.bru) · [EP102-model_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-model_MAX_PLUS_1 - model- 51 caracteres.bru) · [EP102-serialNumber_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-serialNumber_MAX_PLUS_1 - serialNumber- 61 caracteres.bru) · [EP102-imei_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-imei_MAX_PLUS_1 - imei- 61 caracteres.bru) · [EP102-registeredByBusinessUserId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-registeredByBusinessUserId_MISSING - registeredByBusinessUserId- ausente.bru) · [EP102-registeredByBusinessUserId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-registeredByBusinessUserId_NULL - registeredByBusinessUserId- null.bru) · [EP102-registeredByBusinessUserId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-registeredByBusinessUserId_TYPE - registeredByBusinessUserId- tipo incompatible.bru) · [EP102-color_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-color_MISSING - color- ausente.bru) · [EP102-color_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-color_NULL - color- null.bru) · [EP102-color_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-color_EMPTY - color- EMPTY.bru) · [EP102-color_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-color_BLANK - color- BLANK.bru) · [EP102-color_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-color_MAX_PLUS_1 - color- 21 caracteres.bru) · [EP102-registeredAt_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP102-registeredAt_TYPE - registeredAt- tipo incompatible.bru) · [EP102-PILOT](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/CUSTOMER DEVICE/EP102-PILOT - Create customer device.bru) · [EP102-BODY_FOREIGN_ID](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP102-BODY_FOREIGN_ID - Rechazar relación de otro negocio en body.bru) · [EP102-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP102-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP102-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP102-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP102-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP102-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP103

`PUT /api/v1/businesses/{businessId}/customer-devices/{customerDeviceId}` — Update customer device

Fuente: [CustomerDeviceController:84](../../src/main/java/com/tecmarket/backend/repairorder/controller/CustomerDeviceController.java#L84). Método Java: `update`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('CUSTOMER_DEVICE_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<CustomerDeviceResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| customerDeviceId | PathVariable | Integer | @PathVariable Integer customerDeviceId |
| request | RequestBody | UpdateCustomerDeviceRequest | @Valid @RequestBody UpdateCustomerDeviceRequest request |

Body completo (UpdateCustomerDeviceRequest):

```json
{
  "customerId": "{{customerId}}",
  "deviceTypeId": "{{deviceTypeId}}",
  "brandId": "{{brandId}}",
  "model": "iPhone 13 Pro Max",
  "serialNumber": "SN-ABC-12345",
  "imei": "351756051523999",
  "registeredByBusinessUserId": "{{registeredByBusinessUserId}}",
  "color": "Negro",
  "registeredAt": "{{registeredAt}}",
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "customerId": "{{customerId}}",
  "deviceTypeId": "{{deviceTypeId}}",
  "brandId": "{{brandId}}",
  "model": "iPhone 13 Pro Max",
  "registeredByBusinessUserId": "{{registeredByBusinessUserId}}",
  "color": "Negro"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| customerId | Integer | sí | @NotNull |
| deviceTypeId | Integer | sí | @NotNull |
| brandId | Integer | sí | @NotNull |
| model | String | sí | @NotBlank @Size(max = 50) |
| serialNumber | String | no | @Size(max = 60) |
| imei | String | no | @Size(max = 60) |
| registeredByBusinessUserId | Integer | sí | @NotNull |
| color | String | sí | @NotBlank @Size(max = 20) |
| registeredAt | LocalDateTime | no |  |
| active | Boolean | no |  |

Casos Bruno: [EP103-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/CUSTOMER DEVICE/EP103-OK - Contrato válido.bru) · [EP103-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER DEVICE/EP103-NO_TOKEN - Sin autenticación.bru) · [EP103-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER DEVICE/EP103-INVALID_TOKEN - Token inválido.bru) · [EP103-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER DEVICE/EP103-REVOKED - Token previamente revocado.bru) · [EP103-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP103-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP103-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP103-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP103-customerId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-customerId_MISSING - customerId- ausente.bru) · [EP103-customerId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-customerId_NULL - customerId- null.bru) · [EP103-customerId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-customerId_TYPE - customerId- tipo incompatible.bru) · [EP103-deviceTypeId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-deviceTypeId_MISSING - deviceTypeId- ausente.bru) · [EP103-deviceTypeId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-deviceTypeId_NULL - deviceTypeId- null.bru) · [EP103-deviceTypeId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-deviceTypeId_TYPE - deviceTypeId- tipo incompatible.bru) · [EP103-brandId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-brandId_MISSING - brandId- ausente.bru) · [EP103-brandId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-brandId_NULL - brandId- null.bru) · [EP103-brandId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-brandId_TYPE - brandId- tipo incompatible.bru) · [EP103-model_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-model_MISSING - model- ausente.bru) · [EP103-model_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-model_NULL - model- null.bru) · [EP103-model_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-model_EMPTY - model- EMPTY.bru) · [EP103-model_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-model_BLANK - model- BLANK.bru) · [EP103-model_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-model_MAX_PLUS_1 - model- 51 caracteres.bru) · [EP103-serialNumber_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-serialNumber_MAX_PLUS_1 - serialNumber- 61 caracteres.bru) · [EP103-imei_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-imei_MAX_PLUS_1 - imei- 61 caracteres.bru) · [EP103-registeredByBusinessUserId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-registeredByBusinessUserId_MISSING - registeredByBusinessUserId- ausente.bru) · [EP103-registeredByBusinessUserId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-registeredByBusinessUserId_NULL - registeredByBusinessUserId- null.bru) · [EP103-registeredByBusinessUserId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-registeredByBusinessUserId_TYPE - registeredByBusinessUserId- tipo incompatible.bru) · [EP103-color_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-color_MISSING - color- ausente.bru) · [EP103-color_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-color_NULL - color- null.bru) · [EP103-color_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-color_EMPTY - color- EMPTY.bru) · [EP103-color_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-color_BLANK - color- BLANK.bru) · [EP103-color_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-color_MAX_PLUS_1 - color- 21 caracteres.bru) · [EP103-registeredAt_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-registeredAt_TYPE - registeredAt- tipo incompatible.bru) · [EP103-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/CUSTOMER DEVICE/EP103-active_TYPE - active- tipo incompatible.bru) · [EP103-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP103-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP103-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP103-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP103-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP103-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP104

`DELETE /api/v1/businesses/{businessId}/customer-devices/{customerDeviceId}` — Disable customer device

Fuente: [CustomerDeviceController:100](../../src/main/java/com/tecmarket/backend/repairorder/controller/CustomerDeviceController.java#L100). Método Java: `delete`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('CUSTOMER_DEVICE_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| customerDeviceId | PathVariable | Integer | @PathVariable Integer customerDeviceId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP104-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/CUSTOMER DEVICE/EP104-OK - Contrato válido.bru) · [EP104-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER DEVICE/EP104-NO_TOKEN - Sin autenticación.bru) · [EP104-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER DEVICE/EP104-INVALID_TOKEN - Token inválido.bru) · [EP104-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER DEVICE/EP104-REVOKED - Token previamente revocado.bru) · [EP104-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP104-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP104-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP104-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP104-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP104-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP104-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP104-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP104-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER DEVICE/EP104-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP105

`GET /api/v1/me/repair-orders/{orderNumber}` — Track my repair order

Fuente: [CustomerRepairOrderController:33](../../src/main/java/com/tecmarket/backend/repairorder/controller/CustomerRepairOrderController.java#L33). Método Java: `findMyOrder`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `ninguno adicional`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<CustomerRepairOrderTrackingResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| orderNumber | PathVariable | String | @PathVariable String orderNumber |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP105-OK](../../bruno/TECMARKET-QA/06_PUBLICO_CLIENTES_Y_MARKETPLACE/CUSTOMER REPAIR ORDER/EP105-OK - Contrato válido.bru) · [EP105-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER REPAIR ORDER/EP105-NO_TOKEN - Sin autenticación.bru) · [EP105-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER REPAIR ORDER/EP105-INVALID_TOKEN - Token inválido.bru) · [EP105-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/CUSTOMER REPAIR ORDER/EP105-REVOKED - Token previamente revocado.bru) · [EP105-OTHER_CUSTOMER](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/CUSTOMER REPAIR ORDER/EP105-OTHER_CUSTOMER - Cliente B no ve orden de cliente A.bru)

## EP106

`GET /api/v1/parameters/departments` — List departments

Fuente: [DepartmentParameterController:45](../../src/main/java/com/tecmarket/backend/parameter/controller/DepartmentParameterController.java#L45). Método Java: `findAll`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<DepartmentResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|


Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP106-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/DEPARTMENT/EP106-OK - Contrato válido.bru) · [EP106-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP106-NO_TOKEN - Sin autenticación.bru) · [EP106-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP106-INVALID_TOKEN - Token inválido.bru) · [EP106-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP106-REVOKED - Token previamente revocado.bru) · [EP106-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/DEPARTMENT/EP106-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP107

`GET /api/v1/parameters/departments/{id}` — Find department by ID

Fuente: [DepartmentParameterController:54](../../src/main/java/com/tecmarket/backend/parameter/controller/DepartmentParameterController.java#L54). Método Java: `findById`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<DepartmentResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP107-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/DEPARTMENT/EP107-OK - Contrato válido.bru) · [EP107-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP107-NO_TOKEN - Sin autenticación.bru) · [EP107-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP107-INVALID_TOKEN - Token inválido.bru) · [EP107-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP107-REVOKED - Token previamente revocado.bru) · [EP107-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/DEPARTMENT/EP107-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP107-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP107-NOT_FOUND - ID inexistente comprobado.bru)

## EP108

`GET /api/v1/parameters/countries/{countryId}/departments` — List departments by country

Fuente: [DepartmentParameterController:63](../../src/main/java/com/tecmarket/backend/parameter/controller/DepartmentParameterController.java#L63). Método Java: `findByCountryId`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAnyAuthority('PARAMETER_VIEW','CATALOG_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<DepartmentResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| countryId | PathVariable | Integer | @PathVariable Integer countryId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP108-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/DEPARTMENT/EP108-OK - Contrato válido.bru) · [EP108-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP108-NO_TOKEN - Sin autenticación.bru) · [EP108-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP108-INVALID_TOKEN - Token inválido.bru) · [EP108-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP108-REVOKED - Token previamente revocado.bru) · [EP108-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/DEPARTMENT/EP108-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP109

`POST /api/v1/parameters/departments` — Create department

Fuente: [DepartmentParameterController:72](../../src/main/java/com/tecmarket/backend/parameter/controller/DepartmentParameterController.java#L72). Método Java: `create`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<DepartmentResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| request | RequestBody | CreateDepartmentRequest | @Valid @RequestBody CreateDepartmentRequest request |

Body completo (CreateDepartmentRequest):

```json
{
  "countryId": "{{countryId}}",
  "code": "{{qaCode}}",
  "name": "Usuario QA"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "countryId": "{{countryId}}",
  "code": "{{qaCode}}",
  "name": "Usuario QA"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| countryId | Integer | sí | @NotNull |
| code | String | sí | @NotBlank @Size(max = 20) |
| name | String | sí | @NotBlank @Size(max = 120) |

Casos Bruno: [EP109-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/DEPARTMENT/EP109-OK - Contrato válido.bru) · [EP109-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP109-NO_TOKEN - Sin autenticación.bru) · [EP109-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP109-INVALID_TOKEN - Token inválido.bru) · [EP109-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP109-REVOKED - Token previamente revocado.bru) · [EP109-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/DEPARTMENT/EP109-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP109-countryId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP109-countryId_MISSING - countryId- ausente.bru) · [EP109-countryId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP109-countryId_NULL - countryId- null.bru) · [EP109-countryId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP109-countryId_TYPE - countryId- tipo incompatible.bru) · [EP109-code_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP109-code_MISSING - code- ausente.bru) · [EP109-code_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP109-code_NULL - code- null.bru) · [EP109-code_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP109-code_EMPTY - code- EMPTY.bru) · [EP109-code_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP109-code_BLANK - code- BLANK.bru) · [EP109-code_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP109-code_MAX_PLUS_1 - code- 21 caracteres.bru) · [EP109-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP109-name_MISSING - name- ausente.bru) · [EP109-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP109-name_NULL - name- null.bru) · [EP109-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP109-name_EMPTY - name- EMPTY.bru) · [EP109-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP109-name_BLANK - name- BLANK.bru) · [EP109-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP109-name_MAX_PLUS_1 - name- 121 caracteres.bru)

## EP110

`PUT /api/v1/parameters/departments/{id}` — Update department

Fuente: [DepartmentParameterController:83](../../src/main/java/com/tecmarket/backend/parameter/controller/DepartmentParameterController.java#L83). Método Java: `update`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<DepartmentResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |
| request | RequestBody | UpdateDepartmentRequest | @Valid @RequestBody UpdateDepartmentRequest request |

Body completo (UpdateDepartmentRequest):

```json
{
  "countryId": "{{countryId}}",
  "code": "{{qaCode}}",
  "name": "Usuario QA",
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "countryId": "{{countryId}}",
  "code": "{{qaCode}}",
  "name": "Usuario QA"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| countryId | Integer | sí | @NotNull |
| code | String | sí | @NotBlank @Size(max = 20) |
| name | String | sí | @NotBlank @Size(max = 120) |
| active | Boolean | no |  |

Casos Bruno: [EP110-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/DEPARTMENT/EP110-OK - Contrato válido.bru) · [EP110-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP110-NO_TOKEN - Sin autenticación.bru) · [EP110-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP110-INVALID_TOKEN - Token inválido.bru) · [EP110-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP110-REVOKED - Token previamente revocado.bru) · [EP110-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/DEPARTMENT/EP110-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP110-countryId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP110-countryId_MISSING - countryId- ausente.bru) · [EP110-countryId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP110-countryId_NULL - countryId- null.bru) · [EP110-countryId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP110-countryId_TYPE - countryId- tipo incompatible.bru) · [EP110-code_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP110-code_MISSING - code- ausente.bru) · [EP110-code_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP110-code_NULL - code- null.bru) · [EP110-code_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP110-code_EMPTY - code- EMPTY.bru) · [EP110-code_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP110-code_BLANK - code- BLANK.bru) · [EP110-code_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP110-code_MAX_PLUS_1 - code- 21 caracteres.bru) · [EP110-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP110-name_MISSING - name- ausente.bru) · [EP110-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP110-name_NULL - name- null.bru) · [EP110-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP110-name_EMPTY - name- EMPTY.bru) · [EP110-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP110-name_BLANK - name- BLANK.bru) · [EP110-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP110-name_MAX_PLUS_1 - name- 121 caracteres.bru) · [EP110-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DEPARTMENT/EP110-active_TYPE - active- tipo incompatible.bru)

## EP111

`DELETE /api/v1/parameters/departments/{id}` — Disable department

Fuente: [DepartmentParameterController:94](../../src/main/java/com/tecmarket/backend/parameter/controller/DepartmentParameterController.java#L94). Método Java: `delete`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP111-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/DEPARTMENT/EP111-OK - Contrato válido.bru) · [EP111-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP111-NO_TOKEN - Sin autenticación.bru) · [EP111-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP111-INVALID_TOKEN - Token inválido.bru) · [EP111-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DEPARTMENT/EP111-REVOKED - Token previamente revocado.bru) · [EP111-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/DEPARTMENT/EP111-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP112

`GET /api/v1/parameters/document-types` — List document types

Fuente: [DocumentTypeParameterController:38](../../src/main/java/com/tecmarket/backend/parameter/controller/DocumentTypeParameterController.java#L38). Método Java: `findAll`.

Acceso: público. Permiso del controlador: `permitAll()`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<DocumentTypeResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|


Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP112-OK](../../bruno/TECMARKET-QA/06_PUBLICO_CLIENTES_Y_MARKETPLACE/DOCUMENT TYPE/EP112-OK - Contrato válido.bru)

## EP113

`GET /api/v1/parameters/document-types/{id}` — Find document type by ID

Fuente: [DocumentTypeParameterController:47](../../src/main/java/com/tecmarket/backend/parameter/controller/DocumentTypeParameterController.java#L47). Método Java: `findById`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `permitAll()`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<DocumentTypeResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP113-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/DOCUMENT TYPE/EP113-OK - Contrato válido.bru) · [EP113-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DOCUMENT TYPE/EP113-NO_TOKEN - Sin autenticación.bru) · [EP113-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DOCUMENT TYPE/EP113-INVALID_TOKEN - Token inválido.bru) · [EP113-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DOCUMENT TYPE/EP113-REVOKED - Token previamente revocado.bru) · [EP113-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP113-NOT_FOUND - ID inexistente comprobado.bru)

## EP114

`POST /api/v1/parameters/document-types` — Create document type

Fuente: [DocumentTypeParameterController:56](../../src/main/java/com/tecmarket/backend/parameter/controller/DocumentTypeParameterController.java#L56). Método Java: `create`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<DocumentTypeResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| request | RequestBody | CreateDocumentTypeRequest | @Valid @RequestBody CreateDocumentTypeRequest request |

Body completo (CreateDocumentTypeRequest):

```json
{
  "code": "{{qaCode}}",
  "name": "Usuario QA",
  "description": "Documento de identidad colombiano",
  "appliesToPerson": true,
  "appliesToCompany": false
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "code": "{{qaCode}}",
  "name": "Usuario QA",
  "description": "Documento de identidad colombiano",
  "appliesToPerson": true,
  "appliesToCompany": false
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| code | String | sí | @NotBlank @Size(max = 15) |
| name | String | sí | @NotBlank @Size(max = 50) |
| description | String | sí | @NotBlank @Size(max = 100) |
| appliesToPerson | Boolean | sí | @NotNull |
| appliesToCompany | Boolean | sí | @NotNull |

Casos Bruno: [EP114-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/DOCUMENT TYPE/EP114-OK - Contrato válido.bru) · [EP114-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DOCUMENT TYPE/EP114-NO_TOKEN - Sin autenticación.bru) · [EP114-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DOCUMENT TYPE/EP114-INVALID_TOKEN - Token inválido.bru) · [EP114-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DOCUMENT TYPE/EP114-REVOKED - Token previamente revocado.bru) · [EP114-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/DOCUMENT TYPE/EP114-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP114-code_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-code_MISSING - code- ausente.bru) · [EP114-code_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-code_NULL - code- null.bru) · [EP114-code_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-code_EMPTY - code- EMPTY.bru) · [EP114-code_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-code_BLANK - code- BLANK.bru) · [EP114-code_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-code_MAX_PLUS_1 - code- 16 caracteres.bru) · [EP114-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-name_MISSING - name- ausente.bru) · [EP114-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-name_NULL - name- null.bru) · [EP114-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-name_EMPTY - name- EMPTY.bru) · [EP114-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-name_BLANK - name- BLANK.bru) · [EP114-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-name_MAX_PLUS_1 - name- 51 caracteres.bru) · [EP114-description_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-description_MISSING - description- ausente.bru) · [EP114-description_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-description_NULL - description- null.bru) · [EP114-description_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-description_EMPTY - description- EMPTY.bru) · [EP114-description_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-description_BLANK - description- BLANK.bru) · [EP114-description_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-description_MAX_PLUS_1 - description- 101 caracteres.bru) · [EP114-appliesToPerson_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-appliesToPerson_MISSING - appliesToPerson- ausente.bru) · [EP114-appliesToPerson_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-appliesToPerson_NULL - appliesToPerson- null.bru) · [EP114-appliesToPerson_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-appliesToPerson_TYPE - appliesToPerson- tipo incompatible.bru) · [EP114-appliesToCompany_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-appliesToCompany_MISSING - appliesToCompany- ausente.bru) · [EP114-appliesToCompany_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-appliesToCompany_NULL - appliesToCompany- null.bru) · [EP114-appliesToCompany_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP114-appliesToCompany_TYPE - appliesToCompany- tipo incompatible.bru)

## EP115

`PUT /api/v1/parameters/document-types/{id}` — Update document type

Fuente: [DocumentTypeParameterController:68](../../src/main/java/com/tecmarket/backend/parameter/controller/DocumentTypeParameterController.java#L68). Método Java: `update`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<DocumentTypeResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |
| request | RequestBody | UpdateDocumentTypeRequest | @Valid @RequestBody UpdateDocumentTypeRequest request |

Body completo (UpdateDocumentTypeRequest):

```json
{
  "name": "Usuario QA",
  "description": "Documento de identidad colombiano",
  "appliesToPerson": true,
  "appliesToCompany": false,
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "name": "Usuario QA",
  "description": "Documento de identidad colombiano",
  "appliesToPerson": true,
  "appliesToCompany": false
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| name | String | sí | @NotBlank @Size(max = 50) |
| description | String | sí | @NotBlank @Size(max = 100) |
| appliesToPerson | Boolean | sí | @NotNull |
| appliesToCompany | Boolean | sí | @NotNull |
| active | Boolean | no |  |

Casos Bruno: [EP115-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/DOCUMENT TYPE/EP115-OK - Contrato válido.bru) · [EP115-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DOCUMENT TYPE/EP115-NO_TOKEN - Sin autenticación.bru) · [EP115-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DOCUMENT TYPE/EP115-INVALID_TOKEN - Token inválido.bru) · [EP115-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DOCUMENT TYPE/EP115-REVOKED - Token previamente revocado.bru) · [EP115-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/DOCUMENT TYPE/EP115-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP115-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-name_MISSING - name- ausente.bru) · [EP115-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-name_NULL - name- null.bru) · [EP115-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-name_EMPTY - name- EMPTY.bru) · [EP115-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-name_BLANK - name- BLANK.bru) · [EP115-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-name_MAX_PLUS_1 - name- 51 caracteres.bru) · [EP115-description_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-description_MISSING - description- ausente.bru) · [EP115-description_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-description_NULL - description- null.bru) · [EP115-description_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-description_EMPTY - description- EMPTY.bru) · [EP115-description_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-description_BLANK - description- BLANK.bru) · [EP115-description_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-description_MAX_PLUS_1 - description- 101 caracteres.bru) · [EP115-appliesToPerson_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-appliesToPerson_MISSING - appliesToPerson- ausente.bru) · [EP115-appliesToPerson_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-appliesToPerson_NULL - appliesToPerson- null.bru) · [EP115-appliesToPerson_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-appliesToPerson_TYPE - appliesToPerson- tipo incompatible.bru) · [EP115-appliesToCompany_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-appliesToCompany_MISSING - appliesToCompany- ausente.bru) · [EP115-appliesToCompany_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-appliesToCompany_NULL - appliesToCompany- null.bru) · [EP115-appliesToCompany_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-appliesToCompany_TYPE - appliesToCompany- tipo incompatible.bru) · [EP115-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/DOCUMENT TYPE/EP115-active_TYPE - active- tipo incompatible.bru)

## EP116

`DELETE /api/v1/parameters/document-types/{id}` — Disable document type

Fuente: [DocumentTypeParameterController:79](../../src/main/java/com/tecmarket/backend/parameter/controller/DocumentTypeParameterController.java#L79). Método Java: `delete`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP116-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/DOCUMENT TYPE/EP116-OK - Contrato válido.bru) · [EP116-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DOCUMENT TYPE/EP116-NO_TOKEN - Sin autenticación.bru) · [EP116-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DOCUMENT TYPE/EP116-INVALID_TOKEN - Token inválido.bru) · [EP116-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/DOCUMENT TYPE/EP116-REVOKED - Token previamente revocado.bru) · [EP116-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/DOCUMENT TYPE/EP116-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP117

`GET /api/v1/businesses/{businessId}/inventory-items` — List inventory items

Fuente: [InventoryItemController:39](../../src/main/java/com/tecmarket/backend/inventory/controller/InventoryItemController.java#L39). Método Java: `findAll`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('INVENTORY_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<InventoryItemResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP117-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/INVENTORY ITEM/EP117-OK - Contrato válido.bru) · [EP117-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/INVENTORY ITEM/EP117-NO_TOKEN - Sin autenticación.bru) · [EP117-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/INVENTORY ITEM/EP117-INVALID_TOKEN - Token inválido.bru) · [EP117-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/INVENTORY ITEM/EP117-REVOKED - Token previamente revocado.bru) · [EP117-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP117-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP117-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP117-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP117-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP117-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP117-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP117-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP117-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP117-QUERY_0_Min - Query 0- límite inválido.bru) · [EP117-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP117-QUERY_20_Min - Query 20- límite inválido.bru) · [EP117-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP117-QUERY_20_Max - Query 20- límite inválido.bru) · [EP117-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP117-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP117-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP117-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP117-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP117-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP118

`GET /api/v1/businesses/{businessId}/inventory-items/{inventoryItemId}` — Find inventory item by id

Fuente: [InventoryItemController:52](../../src/main/java/com/tecmarket/backend/inventory/controller/InventoryItemController.java#L52). Método Java: `findById`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('INVENTORY_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<InventoryItemResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| inventoryItemId | PathVariable | Integer | @PathVariable Integer inventoryItemId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP118-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/INVENTORY ITEM/EP118-OK - Contrato válido.bru) · [EP118-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/INVENTORY ITEM/EP118-NO_TOKEN - Sin autenticación.bru) · [EP118-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/INVENTORY ITEM/EP118-INVALID_TOKEN - Token inválido.bru) · [EP118-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/INVENTORY ITEM/EP118-REVOKED - Token previamente revocado.bru) · [EP118-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP118-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP118-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP118-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP118-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP118-NOT_FOUND - ID inexistente comprobado.bru) · [EP118-OTHER_BRANCH](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP118-OTHER_BRANCH - Inventario A2 inaccesible a técnico A1.bru) · [EP118-NO_COST](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP118-NO_COST - Ocultar costo a técnico.bru) · [EP118-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP118-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP118-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP118-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP118-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP118-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP119

`POST /api/v1/businesses/{businessId}/inventory-items` — Create inventory item

Fuente: [InventoryItemController:63](../../src/main/java/com/tecmarket/backend/inventory/controller/InventoryItemController.java#L63). Método Java: `create`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('INVENTORY_CREATE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<InventoryItemResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| request | RequestBody | CreateInventoryItemRequest | @Valid @RequestBody CreateInventoryItemRequest request |

Body completo (CreateInventoryItemRequest):

```json
{
  "branchId": "{{branchId}}",
  "name": "Usuario QA",
  "itemCategoryId": "{{itemCategoryId}}",
  "description": "Batería original con garantía de 6 meses.",
  "sku": "{{qaCode}}",
  "quantity": 25,
  "minimumStock": 5,
  "unitCost": 85000,
  "salePrice": 120000
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "branchId": "{{branchId}}",
  "name": "Usuario QA",
  "itemCategoryId": "{{itemCategoryId}}",
  "sku": "{{qaCode}}",
  "quantity": 25,
  "minimumStock": 5,
  "unitCost": 85000,
  "salePrice": 120000
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| branchId | Integer | sí | @NotNull |
| name | String | sí | @NotBlank @Size(max = 150) |
| itemCategoryId | Integer | sí | @NotNull |
| description | String | no | @Size(max = 500) |
| sku | String | sí | @NotBlank @Size(max = 80) |
| quantity | Integer | sí | @NotNull @Min(0) |
| minimumStock | Integer | sí | @NotNull @Min(0) |
| unitCost | BigDecimal | sí | @NotNull @DecimalMin(value = "0.0", inclusive = false) |
| salePrice | BigDecimal | sí | @NotNull @DecimalMin(value = "0.0", inclusive = false) |

Casos Bruno: [EP119-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/INVENTORY ITEM/EP119-OK - Contrato válido.bru) · [EP119-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/INVENTORY ITEM/EP119-NO_TOKEN - Sin autenticación.bru) · [EP119-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/INVENTORY ITEM/EP119-INVALID_TOKEN - Token inválido.bru) · [EP119-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/INVENTORY ITEM/EP119-REVOKED - Token previamente revocado.bru) · [EP119-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP119-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP119-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP119-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP119-branchId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-branchId_MISSING - branchId- ausente.bru) · [EP119-branchId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-branchId_NULL - branchId- null.bru) · [EP119-branchId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-branchId_TYPE - branchId- tipo incompatible.bru) · [EP119-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-name_MISSING - name- ausente.bru) · [EP119-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-name_NULL - name- null.bru) · [EP119-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-name_EMPTY - name- EMPTY.bru) · [EP119-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-name_BLANK - name- BLANK.bru) · [EP119-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-name_MAX_PLUS_1 - name- 151 caracteres.bru) · [EP119-itemCategoryId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-itemCategoryId_MISSING - itemCategoryId- ausente.bru) · [EP119-itemCategoryId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-itemCategoryId_NULL - itemCategoryId- null.bru) · [EP119-itemCategoryId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-itemCategoryId_TYPE - itemCategoryId- tipo incompatible.bru) · [EP119-description_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-description_MAX_PLUS_1 - description- 501 caracteres.bru) · [EP119-sku_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-sku_MISSING - sku- ausente.bru) · [EP119-sku_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-sku_NULL - sku- null.bru) · [EP119-sku_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-sku_EMPTY - sku- EMPTY.bru) · [EP119-sku_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-sku_BLANK - sku- BLANK.bru) · [EP119-sku_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-sku_MAX_PLUS_1 - sku- 81 caracteres.bru) · [EP119-quantity_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-quantity_MISSING - quantity- ausente.bru) · [EP119-quantity_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-quantity_NULL - quantity- null.bru) · [EP119-quantity_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-quantity_TYPE - quantity- tipo incompatible.bru) · [EP119-quantity_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-quantity_Min - quantity- fuera de límite Min.bru) · [EP119-minimumStock_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-minimumStock_MISSING - minimumStock- ausente.bru) · [EP119-minimumStock_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-minimumStock_NULL - minimumStock- null.bru) · [EP119-minimumStock_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-minimumStock_TYPE - minimumStock- tipo incompatible.bru) · [EP119-minimumStock_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-minimumStock_Min - minimumStock- fuera de límite Min.bru) · [EP119-unitCost_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-unitCost_MISSING - unitCost- ausente.bru) · [EP119-unitCost_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-unitCost_NULL - unitCost- null.bru) · [EP119-unitCost_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-unitCost_TYPE - unitCost- tipo incompatible.bru) · [EP119-unitCost_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-unitCost_DecimalMin - unitCost- fuera de límite DecimalMin.bru) · [EP119-salePrice_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-salePrice_MISSING - salePrice- ausente.bru) · [EP119-salePrice_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-salePrice_NULL - salePrice- null.bru) · [EP119-salePrice_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-salePrice_TYPE - salePrice- tipo incompatible.bru) · [EP119-salePrice_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP119-salePrice_DecimalMin - salePrice- fuera de límite DecimalMin.bru) · [EP119-PILOT](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/INVENTORY ITEM/EP119-PILOT - Create inventory item.bru) · [EP119-BODY_FOREIGN_ID](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP119-BODY_FOREIGN_ID - Rechazar relación de otro negocio en body.bru) · [EP119-DUPLICATE_SKU](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/INVENTORY ITEM/EP119-DUPLICATE_SKU - SKU repetido en mismo negocio.bru) · [EP119-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP119-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP119-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP119-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP119-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP119-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP120

`PUT /api/v1/businesses/{businessId}/inventory-items/{inventoryItemId}` — Update inventory item

Fuente: [InventoryItemController:75](../../src/main/java/com/tecmarket/backend/inventory/controller/InventoryItemController.java#L75). Método Java: `update`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('INVENTORY_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<InventoryItemResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| inventoryItemId | PathVariable | Integer | @PathVariable Integer inventoryItemId |
| request | RequestBody | UpdateInventoryItemRequest | @Valid @RequestBody UpdateInventoryItemRequest request |

Body completo (UpdateInventoryItemRequest):

```json
{
  "name": "Usuario QA",
  "itemCategoryId": "{{itemCategoryId}}",
  "description": "Batería original con garantía de 6 meses.",
  "sku": "{{qaCode}}",
  "quantity": 25,
  "minimumStock": 5,
  "unitCost": 85000,
  "salePrice": 120000,
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "name": "Usuario QA",
  "itemCategoryId": "{{itemCategoryId}}",
  "sku": "{{qaCode}}",
  "quantity": 25,
  "minimumStock": 5,
  "unitCost": 85000,
  "salePrice": 120000
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| name | String | sí | @NotBlank @Size(max = 150) |
| itemCategoryId | Integer | sí | @NotNull |
| description | String | no | @Size(max = 500) |
| sku | String | sí | @NotBlank @Size(max = 80) |
| quantity | Integer | sí | @NotNull @Min(0) |
| minimumStock | Integer | sí | @NotNull @Min(0) |
| unitCost | BigDecimal | sí | @NotNull @DecimalMin(value = "0.0", inclusive = false) |
| salePrice | BigDecimal | sí | @NotNull @DecimalMin(value = "0.0", inclusive = false) |
| active | Boolean | no |  |

Casos Bruno: [EP120-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/INVENTORY ITEM/EP120-OK - Contrato válido.bru) · [EP120-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/INVENTORY ITEM/EP120-NO_TOKEN - Sin autenticación.bru) · [EP120-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/INVENTORY ITEM/EP120-INVALID_TOKEN - Token inválido.bru) · [EP120-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/INVENTORY ITEM/EP120-REVOKED - Token previamente revocado.bru) · [EP120-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP120-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP120-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP120-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP120-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-name_MISSING - name- ausente.bru) · [EP120-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-name_NULL - name- null.bru) · [EP120-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-name_EMPTY - name- EMPTY.bru) · [EP120-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-name_BLANK - name- BLANK.bru) · [EP120-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-name_MAX_PLUS_1 - name- 151 caracteres.bru) · [EP120-itemCategoryId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-itemCategoryId_MISSING - itemCategoryId- ausente.bru) · [EP120-itemCategoryId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-itemCategoryId_NULL - itemCategoryId- null.bru) · [EP120-itemCategoryId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-itemCategoryId_TYPE - itemCategoryId- tipo incompatible.bru) · [EP120-description_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-description_MAX_PLUS_1 - description- 501 caracteres.bru) · [EP120-sku_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-sku_MISSING - sku- ausente.bru) · [EP120-sku_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-sku_NULL - sku- null.bru) · [EP120-sku_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-sku_EMPTY - sku- EMPTY.bru) · [EP120-sku_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-sku_BLANK - sku- BLANK.bru) · [EP120-sku_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-sku_MAX_PLUS_1 - sku- 81 caracteres.bru) · [EP120-quantity_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-quantity_MISSING - quantity- ausente.bru) · [EP120-quantity_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-quantity_NULL - quantity- null.bru) · [EP120-quantity_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-quantity_TYPE - quantity- tipo incompatible.bru) · [EP120-quantity_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-quantity_Min - quantity- fuera de límite Min.bru) · [EP120-minimumStock_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-minimumStock_MISSING - minimumStock- ausente.bru) · [EP120-minimumStock_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-minimumStock_NULL - minimumStock- null.bru) · [EP120-minimumStock_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-minimumStock_TYPE - minimumStock- tipo incompatible.bru) · [EP120-minimumStock_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-minimumStock_Min - minimumStock- fuera de límite Min.bru) · [EP120-unitCost_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-unitCost_MISSING - unitCost- ausente.bru) · [EP120-unitCost_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-unitCost_NULL - unitCost- null.bru) · [EP120-unitCost_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-unitCost_TYPE - unitCost- tipo incompatible.bru) · [EP120-unitCost_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-unitCost_DecimalMin - unitCost- fuera de límite DecimalMin.bru) · [EP120-salePrice_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-salePrice_MISSING - salePrice- ausente.bru) · [EP120-salePrice_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-salePrice_NULL - salePrice- null.bru) · [EP120-salePrice_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-salePrice_TYPE - salePrice- tipo incompatible.bru) · [EP120-salePrice_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-salePrice_DecimalMin - salePrice- fuera de límite DecimalMin.bru) · [EP120-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/INVENTORY ITEM/EP120-active_TYPE - active- tipo incompatible.bru) · [EP120-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP120-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP120-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP120-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP120-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP120-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP121

`DELETE /api/v1/businesses/{businessId}/inventory-items/{inventoryItemId}` — Disable inventory item

Fuente: [InventoryItemController:91](../../src/main/java/com/tecmarket/backend/inventory/controller/InventoryItemController.java#L91). Método Java: `delete`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('INVENTORY_DELETE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| inventoryItemId | PathVariable | Integer | @PathVariable Integer inventoryItemId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP121-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/INVENTORY ITEM/EP121-OK - Contrato válido.bru) · [EP121-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/INVENTORY ITEM/EP121-NO_TOKEN - Sin autenticación.bru) · [EP121-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/INVENTORY ITEM/EP121-INVALID_TOKEN - Token inválido.bru) · [EP121-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/INVENTORY ITEM/EP121-REVOKED - Token previamente revocado.bru) · [EP121-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP121-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP121-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP121-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP121-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP121-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP121-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP121-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP121-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/INVENTORY ITEM/EP121-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP122

`GET /api/v1/parameters/permissions` — List permissions

Fuente: [PermissionParameterController:38](../../src/main/java/com/tecmarket/backend/parameter/controller/PermissionParameterController.java#L38). Método Java: `findAll`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<PermissionResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|


Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP122-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/PERMISSION/EP122-OK - Contrato válido.bru) · [EP122-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP122-NO_TOKEN - Sin autenticación.bru) · [EP122-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP122-INVALID_TOKEN - Token inválido.bru) · [EP122-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP122-REVOKED - Token previamente revocado.bru) · [EP122-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/PERMISSION/EP122-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP123

`GET /api/v1/parameters/permissions/{id}` — Find permission by ID

Fuente: [PermissionParameterController:47](../../src/main/java/com/tecmarket/backend/parameter/controller/PermissionParameterController.java#L47). Método Java: `findById`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PermissionResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP123-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/PERMISSION/EP123-OK - Contrato válido.bru) · [EP123-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP123-NO_TOKEN - Sin autenticación.bru) · [EP123-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP123-INVALID_TOKEN - Token inválido.bru) · [EP123-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP123-REVOKED - Token previamente revocado.bru) · [EP123-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/PERMISSION/EP123-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP123-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP123-NOT_FOUND - ID inexistente comprobado.bru)

## EP124

`GET /api/v1/parameters/modules/{moduleId}/permissions` — List permissions by module

Fuente: [PermissionParameterController:56](../../src/main/java/com/tecmarket/backend/parameter/controller/PermissionParameterController.java#L56). Método Java: `findByModuleId`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<PermissionResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| moduleId | PathVariable | Integer | @PathVariable Integer moduleId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP124-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/PERMISSION/EP124-OK - Contrato válido.bru) · [EP124-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP124-NO_TOKEN - Sin autenticación.bru) · [EP124-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP124-INVALID_TOKEN - Token inválido.bru) · [EP124-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP124-REVOKED - Token previamente revocado.bru) · [EP124-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/PERMISSION/EP124-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP125

`POST /api/v1/parameters/permissions` — Create permission

Fuente: [PermissionParameterController:65](../../src/main/java/com/tecmarket/backend/parameter/controller/PermissionParameterController.java#L65). Método Java: `create`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<PermissionResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| request | RequestBody | CreatePermissionRequest | @Valid @RequestBody CreatePermissionRequest request |

Body completo (CreatePermissionRequest):

```json
{
  "moduleId": "{{moduleId}}",
  "code": "{{qaCode}}",
  "name": "Usuario QA",
  "description": "Permite consultar productos, repuestos y accesorios del inventario."
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "moduleId": "{{moduleId}}",
  "code": "{{qaCode}}",
  "name": "Usuario QA",
  "description": "Permite consultar productos, repuestos y accesorios del inventario."
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| moduleId | Integer | sí | @NotNull |
| code | String | sí | @NotBlank @Size(max = 80) |
| name | String | sí | @NotBlank @Size(max = 120) |
| description | String | sí | @NotBlank @Size(max = 255) |

Casos Bruno: [EP125-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/PERMISSION/EP125-OK - Contrato válido.bru) · [EP125-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP125-NO_TOKEN - Sin autenticación.bru) · [EP125-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP125-INVALID_TOKEN - Token inválido.bru) · [EP125-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP125-REVOKED - Token previamente revocado.bru) · [EP125-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/PERMISSION/EP125-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP125-moduleId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-moduleId_MISSING - moduleId- ausente.bru) · [EP125-moduleId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-moduleId_NULL - moduleId- null.bru) · [EP125-moduleId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-moduleId_TYPE - moduleId- tipo incompatible.bru) · [EP125-code_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-code_MISSING - code- ausente.bru) · [EP125-code_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-code_NULL - code- null.bru) · [EP125-code_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-code_EMPTY - code- EMPTY.bru) · [EP125-code_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-code_BLANK - code- BLANK.bru) · [EP125-code_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-code_MAX_PLUS_1 - code- 81 caracteres.bru) · [EP125-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-name_MISSING - name- ausente.bru) · [EP125-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-name_NULL - name- null.bru) · [EP125-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-name_EMPTY - name- EMPTY.bru) · [EP125-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-name_BLANK - name- BLANK.bru) · [EP125-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-name_MAX_PLUS_1 - name- 121 caracteres.bru) · [EP125-description_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-description_MISSING - description- ausente.bru) · [EP125-description_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-description_NULL - description- null.bru) · [EP125-description_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-description_EMPTY - description- EMPTY.bru) · [EP125-description_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-description_BLANK - description- BLANK.bru) · [EP125-description_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP125-description_MAX_PLUS_1 - description- 256 caracteres.bru)

## EP126

`PUT /api/v1/parameters/permissions/{id}` — Update permission

Fuente: [PermissionParameterController:76](../../src/main/java/com/tecmarket/backend/parameter/controller/PermissionParameterController.java#L76). Método Java: `update`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PermissionResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |
| request | RequestBody | UpdatePermissionRequest | @Valid @RequestBody UpdatePermissionRequest request |

Body completo (UpdatePermissionRequest):

```json
{
  "moduleId": "{{moduleId}}",
  "name": "Usuario QA",
  "description": "Permite consultar productos, repuestos y accesorios del inventario.",
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "moduleId": "{{moduleId}}",
  "name": "Usuario QA",
  "description": "Permite consultar productos, repuestos y accesorios del inventario."
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| moduleId | Integer | sí | @NotNull |
| name | String | sí | @NotBlank @Size(max = 120) |
| description | String | sí | @NotBlank @Size(max = 255) |
| active | Boolean | no |  |

Casos Bruno: [EP126-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/PERMISSION/EP126-OK - Contrato válido.bru) · [EP126-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP126-NO_TOKEN - Sin autenticación.bru) · [EP126-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP126-INVALID_TOKEN - Token inválido.bru) · [EP126-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP126-REVOKED - Token previamente revocado.bru) · [EP126-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/PERMISSION/EP126-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP126-moduleId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP126-moduleId_MISSING - moduleId- ausente.bru) · [EP126-moduleId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP126-moduleId_NULL - moduleId- null.bru) · [EP126-moduleId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP126-moduleId_TYPE - moduleId- tipo incompatible.bru) · [EP126-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP126-name_MISSING - name- ausente.bru) · [EP126-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP126-name_NULL - name- null.bru) · [EP126-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP126-name_EMPTY - name- EMPTY.bru) · [EP126-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP126-name_BLANK - name- BLANK.bru) · [EP126-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP126-name_MAX_PLUS_1 - name- 121 caracteres.bru) · [EP126-description_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP126-description_MISSING - description- ausente.bru) · [EP126-description_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP126-description_NULL - description- null.bru) · [EP126-description_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP126-description_EMPTY - description- EMPTY.bru) · [EP126-description_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP126-description_BLANK - description- BLANK.bru) · [EP126-description_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP126-description_MAX_PLUS_1 - description- 256 caracteres.bru) · [EP126-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PERMISSION/EP126-active_TYPE - active- tipo incompatible.bru)

## EP127

`DELETE /api/v1/parameters/permissions/{id}` — Disable permission

Fuente: [PermissionParameterController:87](../../src/main/java/com/tecmarket/backend/parameter/controller/PermissionParameterController.java#L87). Método Java: `delete`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP127-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/PERMISSION/EP127-OK - Contrato válido.bru) · [EP127-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP127-NO_TOKEN - Sin autenticación.bru) · [EP127-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP127-INVALID_TOKEN - Token inválido.bru) · [EP127-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/PERMISSION/EP127-REVOKED - Token previamente revocado.bru) · [EP127-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/PERMISSION/EP127-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP128

`GET /api/v1/public/locations/countries` — List public countries

Fuente: [PublicLocationController:43](../../src/main/java/com/tecmarket/backend/parameter/controller/PublicLocationController.java#L43). Método Java: `findCountries`.

Acceso: público. Permiso del controlador: `ninguno adicional`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<CountryResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|


Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP128-OK](../../bruno/TECMARKET-QA/06_PUBLICO_CLIENTES_Y_MARKETPLACE/PUBLIC LOCATION/EP128-OK - Contrato válido.bru)

## EP129

`GET /api/v1/public/locations/countries/{countryId}/departments` — List public departments by country

Fuente: [PublicLocationController:51](../../src/main/java/com/tecmarket/backend/parameter/controller/PublicLocationController.java#L51). Método Java: `findDepartmentsByCountry`.

Acceso: público. Permiso del controlador: `ninguno adicional`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<DepartmentResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| countryId | PathVariable | Integer | @PathVariable Integer countryId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP129-OK](../../bruno/TECMARKET-QA/06_PUBLICO_CLIENTES_Y_MARKETPLACE/PUBLIC LOCATION/EP129-OK - Contrato válido.bru)

## EP130

`GET /api/v1/public/locations/departments/{departmentId}/cities` — List public cities by department

Fuente: [PublicLocationController:59](../../src/main/java/com/tecmarket/backend/parameter/controller/PublicLocationController.java#L59). Método Java: `findCitiesByDepartment`.

Acceso: público. Permiso del controlador: `ninguno adicional`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<CityResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| departmentId | PathVariable | Integer | @PathVariable Integer departmentId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP130-OK](../../bruno/TECMARKET-QA/06_PUBLICO_CLIENTES_Y_MARKETPLACE/PUBLIC LOCATION/EP130-OK - Contrato válido.bru)

## EP131

`GET /api/v1/public/businesses` — Search public businesses

Fuente: [PublicMarketplaceController:36](../../src/main/java/com/tecmarket/backend/review/controller/PublicMarketplaceController.java#L36). Método Java: `findBusinesses`.

Acceso: público. Permiso del controlador: `ninguno adicional`. Requiere `FEATURE_MARKETPLACE_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<PublicBusinessResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| q | RequestParam | String | @RequestParam(required = false) String q |
| cityId | RequestParam | Integer | @RequestParam(required = false) Integer cityId |
| minRating | RequestParam | BigDecimal | @RequestParam(required = false) @DecimalMin("0.0") @DecimalMax("5.0") BigDecimal minRating |
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |
| ratingAverage | RequestParam | String | @RequestParam(defaultValue = "ratingAverage") String sortBy |
| desc | RequestParam | String | @RequestParam(defaultValue = "desc") String sortDir |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP131-OK](../../bruno/TECMARKET-QA/06_PUBLICO_CLIENTES_Y_MARKETPLACE/PUBLIC MARKETPLACE/EP131-OK - Contrato válido.bru) · [EP131-DISABLED](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP131-DISABLED - Funcionalidad deshabilitada.bru) · [EP131-QUERY_cityId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP131-QUERY_cityId_TYPE - Query cityId- tipo inválido.bru) · [EP131-QUERY_minRating_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP131-QUERY_minRating_TYPE - Query minRating- tipo inválido.bru) · [EP131-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP131-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP131-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP131-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP131-FILTER_q](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP131-FILTER_q - Filtro q individual.bru) · [EP131-FILTER_cityId](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP131-FILTER_cityId - Filtro cityId individual.bru) · [EP131-QUERY_minRating_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP131-QUERY_minRating_DecimalMin - Query minRating- límite inválido.bru) · [EP131-QUERY_minRating_DecimalMax](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP131-QUERY_minRating_DecimalMax - Query minRating- límite inválido.bru) · [EP131-FILTER_minRating](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP131-FILTER_minRating - Filtro minRating individual.bru) · [EP131-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP131-QUERY_0_Min - Query 0- límite inválido.bru) · [EP131-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP131-QUERY_20_Min - Query 20- límite inválido.bru) · [EP131-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP131-QUERY_20_Max - Query 20- límite inválido.bru) · [EP131-FILTER_q_cityId](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP131-FILTER_q_cityId - Filtros combinados q + cityId.bru) · [EP131-FILTER_q_minRating](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP131-FILTER_q_minRating - Filtros combinados q + minRating.bru) · [EP131-FILTER_cityId_minRating](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP131-FILTER_cityId_minRating - Filtros combinados cityId + minRating.bru)

## EP132

`GET /api/v1/public/businesses/{businessId}` — Find public business detail

Fuente: [PublicMarketplaceController:57](../../src/main/java/com/tecmarket/backend/review/controller/PublicMarketplaceController.java#L57). Método Java: `findBusinessById`.

Acceso: público. Permiso del controlador: `ninguno adicional`. Requiere `FEATURE_MARKETPLACE_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PublicBusinessDetailResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP132-OK](../../bruno/TECMARKET-QA/06_PUBLICO_CLIENTES_Y_MARKETPLACE/PUBLIC MARKETPLACE/EP132-OK - Contrato válido.bru) · [EP132-DISABLED](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP132-DISABLED - Funcionalidad deshabilitada.bru)

## EP133

`GET /api/v1/public/businesses/{businessId}/reviews` — List public business reviews

Fuente: [PublicMarketplaceController:64](../../src/main/java/com/tecmarket/backend/review/controller/PublicMarketplaceController.java#L64). Método Java: `findBusinessReviews`.

Acceso: público. Permiso del controlador: `ninguno adicional`. Requiere `FEATURE_MARKETPLACE_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<PublicBusinessReviewResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP133-OK](../../bruno/TECMARKET-QA/06_PUBLICO_CLIENTES_Y_MARKETPLACE/PUBLIC MARKETPLACE/EP133-OK - Contrato válido.bru) · [EP133-DISABLED](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP133-DISABLED - Funcionalidad deshabilitada.bru) · [EP133-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP133-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP133-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP133-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP133-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP133-QUERY_0_Min - Query 0- límite inválido.bru) · [EP133-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP133-QUERY_20_Min - Query 20- límite inválido.bru) · [EP133-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/PUBLIC MARKETPLACE/EP133-QUERY_20_Max - Query 20- límite inválido.bru) · [EP133-EXACT_PRIVACY](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/PUBLIC MARKETPLACE/EP133-EXACT_PRIVACY - JSON público exacto de reseñas.bru)

## EP134

`GET /api/v1/businesses/{businessId}/quality-checklists/{ticketCode}` — Consultar checklist por ticket

Fuente: [QualityChecklistController:34](../../src/main/java/com/tecmarket/backend/repairorder/controller/QualityChecklistController.java#L34). Método Java: `findByTicket`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_QUALITY_CHECK_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<QualityChecklistResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| ticketCode | PathVariable | String | @PathVariable String ticketCode |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP134-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/QUALITY CHECKLIST/EP134-OK - Contrato válido.bru) · [EP134-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUALITY CHECKLIST/EP134-NO_TOKEN - Sin autenticación.bru) · [EP134-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUALITY CHECKLIST/EP134-INVALID_TOKEN - Token inválido.bru) · [EP134-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUALITY CHECKLIST/EP134-REVOKED - Token previamente revocado.bru) · [EP134-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUALITY CHECKLIST/EP134-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP134-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUALITY CHECKLIST/EP134-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru)

## EP135

`PUT /api/v1/businesses/{businessId}/quality-checklists/{ticketCode}` — Guardar checklist por ticket

Fuente: [QualityChecklistController:45](../../src/main/java/com/tecmarket/backend/repairorder/controller/QualityChecklistController.java#L45). Método Java: `upsertByTicket`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_QUALITY_CHECK_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<QualityChecklistResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| ticketCode | PathVariable | String | @PathVariable String ticketCode |
| request | RequestBody | UpsertQualityChecklistRequest | @Valid @RequestBody UpsertQualityChecklistRequest request |

Body completo (UpsertQualityChecklistRequest):

```json
{
  "repairOrderId": "{{repairOrderId}}",
  "items": [
    {
      "businessQualityTestId": "{{businessQualityTestId}}",
      "result": "SI",
      "notes": "Pantalla responde correctamente en toda el área táctil."
    }
  ]
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "items": [
    {
      "businessQualityTestId": "{{businessQualityTestId}}",
      "result": "SI"
    }
  ]
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| repairOrderId | Integer | no | @Positive |
| items | List<QualityChecklistItemRequest> | sí | @NotEmpty @Size(max = 100) @Valid |

Casos Bruno: [EP135-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/QUALITY CHECKLIST/EP135-OK - Contrato válido.bru) · [EP135-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUALITY CHECKLIST/EP135-NO_TOKEN - Sin autenticación.bru) · [EP135-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUALITY CHECKLIST/EP135-INVALID_TOKEN - Token inválido.bru) · [EP135-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUALITY CHECKLIST/EP135-REVOKED - Token previamente revocado.bru) · [EP135-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUALITY CHECKLIST/EP135-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP135-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUALITY CHECKLIST/EP135-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP135-repairOrderId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUALITY CHECKLIST/EP135-repairOrderId_TYPE - repairOrderId- tipo incompatible.bru) · [EP135-repairOrderId_Positive](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUALITY CHECKLIST/EP135-repairOrderId_Positive - repairOrderId- fuera de límite Positive.bru) · [EP135-items_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUALITY CHECKLIST/EP135-items_MISSING - items- ausente.bru) · [EP135-items_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUALITY CHECKLIST/EP135-items_NULL - items- null.bru) · [EP135-items_EMPTY_ARRAY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUALITY CHECKLIST/EP135-items_EMPTY_ARRAY - items- colección vacía.bru) · [EP135-items_MAX_ARRAY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUALITY CHECKLIST/EP135-items_MAX_ARRAY - items- 101 elementos.bru) · [EP135-items_businessQualityTestId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUALITY CHECKLIST/EP135-items_businessQualityTestId_MISSING - items[0].businessQualityTestId- ausente.bru) · [EP135-items_result_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUALITY CHECKLIST/EP135-items_result_MISSING - items[0].result- ausente.bru)

## EP136

`DELETE /api/v1/businesses/{businessId}/quality-checklists/{ticketCode}` — Eliminar checklist por ticket

Fuente: [QualityChecklistController:58](../../src/main/java/com/tecmarket/backend/repairorder/controller/QualityChecklistController.java#L58). Método Java: `deleteByTicket`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_QUALITY_CHECK_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| ticketCode | PathVariable | String | @PathVariable String ticketCode |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP136-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/QUALITY CHECKLIST/EP136-OK - Contrato válido.bru) · [EP136-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUALITY CHECKLIST/EP136-NO_TOKEN - Sin autenticación.bru) · [EP136-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUALITY CHECKLIST/EP136-INVALID_TOKEN - Token inválido.bru) · [EP136-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUALITY CHECKLIST/EP136-REVOKED - Token previamente revocado.bru) · [EP136-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUALITY CHECKLIST/EP136-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP136-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUALITY CHECKLIST/EP136-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru)

## EP137

`POST /api/v1/businesses/{businessId}/quotations` — Create quotation

Fuente: [QuotationController:43](../../src/main/java/com/tecmarket/backend/quotation/controller/QuotationController.java#L43). Método Java: `create`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('QUOTATION_CREATE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<QuotationResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| request | RequestBody | CreateQuotationRequest | @Valid @RequestBody CreateQuotationRequest request |

Body completo (CreateQuotationRequest):

```json
{
  "repairOrderId": "{{repairOrderId}}",
  "notes": "Cotización válida por 5 días calendario.",
  "expiresAt": "{{expiresAt}}",
  "items": [
    {
      "description": "Cambio de batería original",
      "quantity": 1,
      "unitPrice": 180000
    }
  ]
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "repairOrderId": "{{repairOrderId}}",
  "items": [
    {
      "description": "Cambio de batería original",
      "quantity": 1,
      "unitPrice": 180000
    }
  ]
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| repairOrderId | Integer | sí | @NotNull |
| notes | String | no | @Size(max = 500) |
| expiresAt | LocalDateTime | no |  |
| items | List<CreateQuotationItemRequest> | sí | @NotEmpty @Size(max = 100) @Valid |

Casos Bruno: [EP137-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/QUOTATION/EP137-OK - Contrato válido.bru) · [EP137-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUOTATION/EP137-NO_TOKEN - Sin autenticación.bru) · [EP137-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUOTATION/EP137-INVALID_TOKEN - Token inválido.bru) · [EP137-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUOTATION/EP137-REVOKED - Token previamente revocado.bru) · [EP137-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP137-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP137-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP137-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP137-repairOrderId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP137-repairOrderId_MISSING - repairOrderId- ausente.bru) · [EP137-repairOrderId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP137-repairOrderId_NULL - repairOrderId- null.bru) · [EP137-repairOrderId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP137-repairOrderId_TYPE - repairOrderId- tipo incompatible.bru) · [EP137-notes_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP137-notes_MAX_PLUS_1 - notes- 501 caracteres.bru) · [EP137-expiresAt_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP137-expiresAt_TYPE - expiresAt- tipo incompatible.bru) · [EP137-items_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP137-items_MISSING - items- ausente.bru) · [EP137-items_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP137-items_NULL - items- null.bru) · [EP137-items_EMPTY_ARRAY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP137-items_EMPTY_ARRAY - items- colección vacía.bru) · [EP137-items_MAX_ARRAY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP137-items_MAX_ARRAY - items- 101 elementos.bru) · [EP137-items_description_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP137-items_description_MISSING - items[0].description- ausente.bru) · [EP137-items_quantity_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP137-items_quantity_MISSING - items[0].quantity- ausente.bru) · [EP137-items_unitPrice_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP137-items_unitPrice_MISSING - items[0].unitPrice- ausente.bru) · [EP137-PILOT](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/QUOTATION/EP137-PILOT - Create quotation.bru) · [EP137-BODY_FOREIGN_ID](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP137-BODY_FOREIGN_ID - Rechazar relación de otro negocio en body.bru) · [EP137-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP137-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP137-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP137-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP137-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP137-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP138

`GET /api/v1/businesses/{businessId}/quotations/{quotationId}` — Get quotation detail

Fuente: [QuotationController:55](../../src/main/java/com/tecmarket/backend/quotation/controller/QuotationController.java#L55). Método Java: `findById`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('QUOTATION_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<QuotationResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| quotationId | PathVariable | Integer | @PathVariable Integer quotationId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP138-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/QUOTATION/EP138-OK - Contrato válido.bru) · [EP138-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUOTATION/EP138-NO_TOKEN - Sin autenticación.bru) · [EP138-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUOTATION/EP138-INVALID_TOKEN - Token inválido.bru) · [EP138-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUOTATION/EP138-REVOKED - Token previamente revocado.bru) · [EP138-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP138-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP138-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP138-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP138-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP138-NOT_FOUND - ID inexistente comprobado.bru) · [EP138-OTHER_BRANCH](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP138-OTHER_BRANCH - Cotización A2 inaccesible a técnico A1.bru) · [EP138-SHARED_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP138-SHARED_SCOPE - Permiso obtenido en A no habilita B.bru) · [EP138-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP138-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP138-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP138-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP138-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP138-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP139

`PATCH /api/v1/businesses/{businessId}/quotations/{quotationId}/status` — Change quotation status

Fuente: [QuotationController:66](../../src/main/java/com/tecmarket/backend/quotation/controller/QuotationController.java#L66). Método Java: `updateStatus`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('QUOTATION_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<QuotationResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| quotationId | PathVariable | Integer | @PathVariable Integer quotationId |
| request | RequestBody | UpdateQuotationStatusRequest | @Valid @RequestBody UpdateQuotationStatusRequest request |

Body completo (UpdateQuotationStatusRequest):

```json
{
  "statusId": "{{statusId}}",
  "notes": "Cliente aprobó por WhatsApp."
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "statusId": "{{statusId}}"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| statusId | Integer | sí | @NotNull |
| notes | String | no | @Size(max = 500) |

Casos Bruno: [EP139-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/QUOTATION/EP139-OK - Contrato válido.bru) · [EP139-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUOTATION/EP139-NO_TOKEN - Sin autenticación.bru) · [EP139-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUOTATION/EP139-INVALID_TOKEN - Token inválido.bru) · [EP139-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUOTATION/EP139-REVOKED - Token previamente revocado.bru) · [EP139-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP139-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP139-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP139-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP139-statusId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP139-statusId_MISSING - statusId- ausente.bru) · [EP139-statusId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP139-statusId_NULL - statusId- null.bru) · [EP139-statusId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP139-statusId_TYPE - statusId- tipo incompatible.bru) · [EP139-notes_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP139-notes_MAX_PLUS_1 - notes- 501 caracteres.bru) · [EP139-STATE_DRAFT_DRAFT](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_DRAFT_DRAFT - Cotización DRAFT a DRAFT.bru) · [EP139-STATE_DRAFT_PENDING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_DRAFT_PENDING - Cotización DRAFT a PENDING.bru) · [EP139-STATE_DRAFT_APPROVED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_DRAFT_APPROVED - Cotización DRAFT a APPROVED.bru) · [EP139-STATE_DRAFT_REJECTED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_DRAFT_REJECTED - Cotización DRAFT a REJECTED.bru) · [EP139-STATE_DRAFT_EXPIRED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_DRAFT_EXPIRED - Cotización DRAFT a EXPIRED.bru) · [EP139-STATE_DRAFT_CANCELLED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_DRAFT_CANCELLED - Cotización DRAFT a CANCELLED.bru) · [EP139-STATE_PENDING_DRAFT](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_PENDING_DRAFT - Cotización PENDING a DRAFT.bru) · [EP139-STATE_PENDING_PENDING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_PENDING_PENDING - Cotización PENDING a PENDING.bru) · [EP139-STATE_PENDING_APPROVED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_PENDING_APPROVED - Cotización PENDING a APPROVED.bru) · [EP139-STATE_PENDING_REJECTED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_PENDING_REJECTED - Cotización PENDING a REJECTED.bru) · [EP139-STATE_PENDING_EXPIRED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_PENDING_EXPIRED - Cotización PENDING a EXPIRED.bru) · [EP139-STATE_PENDING_CANCELLED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_PENDING_CANCELLED - Cotización PENDING a CANCELLED.bru) · [EP139-STATE_APPROVED_DRAFT](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_APPROVED_DRAFT - Cotización APPROVED a DRAFT.bru) · [EP139-STATE_APPROVED_PENDING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_APPROVED_PENDING - Cotización APPROVED a PENDING.bru) · [EP139-STATE_APPROVED_APPROVED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_APPROVED_APPROVED - Cotización APPROVED a APPROVED.bru) · [EP139-STATE_APPROVED_REJECTED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_APPROVED_REJECTED - Cotización APPROVED a REJECTED.bru) · [EP139-STATE_APPROVED_EXPIRED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_APPROVED_EXPIRED - Cotización APPROVED a EXPIRED.bru) · [EP139-STATE_APPROVED_CANCELLED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_APPROVED_CANCELLED - Cotización APPROVED a CANCELLED.bru) · [EP139-STATE_REJECTED_DRAFT](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_REJECTED_DRAFT - Cotización REJECTED a DRAFT.bru) · [EP139-STATE_REJECTED_PENDING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_REJECTED_PENDING - Cotización REJECTED a PENDING.bru) · [EP139-STATE_REJECTED_APPROVED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_REJECTED_APPROVED - Cotización REJECTED a APPROVED.bru) · [EP139-STATE_REJECTED_REJECTED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_REJECTED_REJECTED - Cotización REJECTED a REJECTED.bru) · [EP139-STATE_REJECTED_EXPIRED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_REJECTED_EXPIRED - Cotización REJECTED a EXPIRED.bru) · [EP139-STATE_REJECTED_CANCELLED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_REJECTED_CANCELLED - Cotización REJECTED a CANCELLED.bru) · [EP139-STATE_EXPIRED_DRAFT](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_EXPIRED_DRAFT - Cotización EXPIRED a DRAFT.bru) · [EP139-STATE_EXPIRED_PENDING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_EXPIRED_PENDING - Cotización EXPIRED a PENDING.bru) · [EP139-STATE_EXPIRED_APPROVED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_EXPIRED_APPROVED - Cotización EXPIRED a APPROVED.bru) · [EP139-STATE_EXPIRED_REJECTED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_EXPIRED_REJECTED - Cotización EXPIRED a REJECTED.bru) · [EP139-STATE_EXPIRED_EXPIRED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_EXPIRED_EXPIRED - Cotización EXPIRED a EXPIRED.bru) · [EP139-STATE_EXPIRED_CANCELLED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_EXPIRED_CANCELLED - Cotización EXPIRED a CANCELLED.bru) · [EP139-STATE_CANCELLED_DRAFT](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_CANCELLED_DRAFT - Cotización CANCELLED a DRAFT.bru) · [EP139-STATE_CANCELLED_PENDING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_CANCELLED_PENDING - Cotización CANCELLED a PENDING.bru) · [EP139-STATE_CANCELLED_APPROVED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_CANCELLED_APPROVED - Cotización CANCELLED a APPROVED.bru) · [EP139-STATE_CANCELLED_REJECTED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_CANCELLED_REJECTED - Cotización CANCELLED a REJECTED.bru) · [EP139-STATE_CANCELLED_EXPIRED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_CANCELLED_EXPIRED - Cotización CANCELLED a EXPIRED.bru) · [EP139-STATE_CANCELLED_CANCELLED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/QUOTATION/EP139-STATE_CANCELLED_CANCELLED - Cotización CANCELLED a CANCELLED.bru) · [EP139-EXPIRE_TOO_EARLY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP139-EXPIRE_TOO_EARLY - No expirar antes de la fecha.bru) · [EP139-APPROVE_EXPIRED](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/QUOTATION/EP139-APPROVE_EXPIRED - No aprobar después del vencimiento.bru) · [EP139-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP139-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP139-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP139-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP139-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP139-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP140

`GET /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/quotations` — List quotations by repair order

Fuente: [QuotationController:84](../../src/main/java/com/tecmarket/backend/quotation/controller/QuotationController.java#L84). Método Java: `findAllByRepairOrder`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('QUOTATION_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<QuotationResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |
| page | RequestParam | Integer | @RequestParam(name = "page", defaultValue = "0") @Min(0) Integer page |
| size | RequestParam | Integer | @RequestParam(name = "size", defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP140-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/QUOTATION/EP140-OK - Contrato válido.bru) · [EP140-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUOTATION/EP140-NO_TOKEN - Sin autenticación.bru) · [EP140-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUOTATION/EP140-INVALID_TOKEN - Token inválido.bru) · [EP140-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/QUOTATION/EP140-REVOKED - Token previamente revocado.bru) · [EP140-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP140-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP140-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP140-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP140-QUERY_page_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP140-QUERY_page_TYPE - Query page- tipo inválido.bru) · [EP140-QUERY_size_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP140-QUERY_size_TYPE - Query size- tipo inválido.bru) · [EP140-QUERY_page_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP140-QUERY_page_Min - Query page- límite inválido.bru) · [EP140-QUERY_size_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP140-QUERY_size_Min - Query size- límite inválido.bru) · [EP140-QUERY_size_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/QUOTATION/EP140-QUERY_size_Max - Query size- límite inválido.bru) · [EP140-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP140-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP140-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP140-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP140-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/QUOTATION/EP140-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP141

`GET /api/v1/businesses/{businessId}/commissions` — List repair commissions

Fuente: [RepairOrderCommissionController:44](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderCommissionController.java#L44). Método Java: `findAll`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAnyAuthority('BUSINESS_COMMISSION_VIEW','BUSINESS_COMMISSION_MANAGE')`. Requiere `FEATURE_COMMISSIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<RepairOrderCommissionResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| businessUserId | RequestParam | Integer | @RequestParam(required = false) Integer businessUserId |
| status | RequestParam | RepairOrderCommissionStatus | @RequestParam(required = false) RepairOrderCommissionStatus status |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP141-OK](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/REPAIR ORDER COMMISSION/EP141-OK - Contrato válido.bru) · [EP141-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER COMMISSION/EP141-NO_TOKEN - Sin autenticación.bru) · [EP141-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER COMMISSION/EP141-INVALID_TOKEN - Token inválido.bru) · [EP141-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER COMMISSION/EP141-REVOKED - Token previamente revocado.bru) · [EP141-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER COMMISSION/EP141-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP141-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER COMMISSION/EP141-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP141-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/REPAIR ORDER COMMISSION/EP141-DISABLED - Funcionalidad deshabilitada.bru) · [EP141-QUERY_businessUserId_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/REPAIR ORDER COMMISSION/EP141-QUERY_businessUserId_TYPE - Query businessUserId- tipo inválido.bru) · [EP141-FILTER_businessUserId](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/REPAIR ORDER COMMISSION/EP141-FILTER_businessUserId - Filtro businessUserId individual.bru) · [EP141-FILTER_status](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/REPAIR ORDER COMMISSION/EP141-FILTER_status - Filtro status individual.bru) · [EP141-FILTER_businessUserId_status](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/REPAIR ORDER COMMISSION/EP141-FILTER_businessUserId_status - Filtros combinados businessUserId + status.bru)

## EP142

`GET /api/v1/businesses/{businessId}/commissions/{commissionId}` — Find repair commission

Fuente: [RepairOrderCommissionController:57](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderCommissionController.java#L57). Método Java: `findById`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAnyAuthority('BUSINESS_COMMISSION_VIEW','BUSINESS_COMMISSION_MANAGE')`. Requiere `FEATURE_COMMISSIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<RepairOrderCommissionResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| commissionId | PathVariable | Integer | @PathVariable Integer commissionId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP142-OK](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/REPAIR ORDER COMMISSION/EP142-OK - Contrato válido.bru) · [EP142-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER COMMISSION/EP142-NO_TOKEN - Sin autenticación.bru) · [EP142-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER COMMISSION/EP142-INVALID_TOKEN - Token inválido.bru) · [EP142-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER COMMISSION/EP142-REVOKED - Token previamente revocado.bru) · [EP142-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER COMMISSION/EP142-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP142-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER COMMISSION/EP142-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP142-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/REPAIR ORDER COMMISSION/EP142-DISABLED - Funcionalidad deshabilitada.bru) · [EP142-NOT_FOUND](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/REPAIR ORDER COMMISSION/EP142-NOT_FOUND - ID inexistente comprobado.bru)

## EP143

`GET /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/commission` — Find commission by repair order

Fuente: [RepairOrderCommissionController:69](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderCommissionController.java#L69). Método Java: `findByRepairOrder`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAnyAuthority('BUSINESS_COMMISSION_VIEW','BUSINESS_COMMISSION_MANAGE')`. Requiere `FEATURE_COMMISSIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<RepairOrderCommissionResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP143-OK](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/REPAIR ORDER COMMISSION/EP143-OK - Contrato válido.bru) · [EP143-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER COMMISSION/EP143-NO_TOKEN - Sin autenticación.bru) · [EP143-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER COMMISSION/EP143-INVALID_TOKEN - Token inválido.bru) · [EP143-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER COMMISSION/EP143-REVOKED - Token previamente revocado.bru) · [EP143-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER COMMISSION/EP143-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP143-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER COMMISSION/EP143-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP143-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/REPAIR ORDER COMMISSION/EP143-DISABLED - Funcionalidad deshabilitada.bru)

## EP144

`POST /api/v1/businesses/{businessId}/repair-orders/quick` — Create quick repair order

Fuente: [RepairOrderController:70](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L70). Método Java: `createQuick`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('CUSTOMER_DEVICE_CREATE') and hasAuthority('REPAIR_ORDER_CREATE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<QuickRepairOrderResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| request | RequestBody | CreateQuickRepairOrderRequest | @Valid @RequestBody CreateQuickRepairOrderRequest request |
| Idempotency-Key | RequestHeader | String | @RequestHeader("Idempotency-Key") String idempotencyKey |

Body completo (CreateQuickRepairOrderRequest):

```json
{
  "customer": {
    "documentTypeCode": "CC",
    "documentNumber": "{{documentNumber}}",
    "name": "Usuario QA",
    "surname": "Gomez",
    "email": "{{email}}",
    "address": "Calle 10 #20-30",
    "phone": "+573001112233",
    "countryId": "{{countryId}}",
    "departmentId": "{{departmentId}}",
    "cityId": "{{cityId}}"
  },
  "device": {
    "deviceTypeId": "{{deviceTypeId}}",
    "brandId": "{{brandId}}",
    "model": "iPhone 13 Pro",
    "serialNumber": "SN-ABC-12345",
    "imei": "351756051523999",
    "color": "Azul",
    "registeredAt": "{{registeredAt}}"
  },
  "order": {
    "branchId": "{{branchId}}",
    "businessServiceId": "{{businessServiceId}}",
    "assignedBusinessUserId": "{{assignedBusinessUserId}}",
    "problemDescription": "No enciende y no responde al cargar.",
    "diagnosis": "Posible falla en power IC.",
    "estimatedPrice": 150000,
    "estimatedDeliveryAt": "{{estimatedDeliveryAt}}",
    "receivedAt": "{{receivedAt}}"
  }
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "customer": {
    "documentTypeCode": "CC",
    "documentNumber": "{{documentNumber}}",
    "name": "Usuario QA",
    "surname": "Gomez",
    "email": "{{email}}",
    "address": "Calle 10 #20-30",
    "phone": "+573001112233",
    "countryId": "{{countryId}}",
    "departmentId": "{{departmentId}}",
    "cityId": "{{cityId}}"
  },
  "device": {
    "deviceTypeId": "{{deviceTypeId}}",
    "brandId": "{{brandId}}",
    "model": "iPhone 13 Pro",
    "color": "Azul"
  },
  "order": {
    "branchId": "{{branchId}}",
    "businessServiceId": "{{businessServiceId}}",
    "assignedBusinessUserId": "{{assignedBusinessUserId}}",
    "problemDescription": "No enciende y no responde al cargar."
  }
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| customer | QuickRepairOrderCustomerRequest | sí | @Valid @NotNull |
| device | QuickRepairOrderDeviceRequest | sí | @Valid @NotNull |
| order | QuickRepairOrderDetailsRequest | sí | @Valid @NotNull |

Casos Bruno: [EP144-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP144-OK - Contrato válido.bru) · [EP144-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP144-NO_TOKEN - Sin autenticación.bru) · [EP144-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP144-INVALID_TOKEN - Token inválido.bru) · [EP144-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP144-REVOKED - Token previamente revocado.bru) · [EP144-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP144-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP144-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP144-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP144-customer_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP144-customer_MISSING - customer- ausente.bru) · [EP144-customer_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP144-customer_NULL - customer- null.bru) · [EP144-device_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP144-device_MISSING - device- ausente.bru) · [EP144-device_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP144-device_NULL - device- null.bru) · [EP144-order_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP144-order_MISSING - order- ausente.bru) · [EP144-order_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP144-order_NULL - order- null.bru) · [EP144-IDEMPOTENT_FIRST](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP144-IDEMPOTENT_FIRST - Primera ejecución idempotente.bru) · [EP144-IDEMPOTENT_REPLAY](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP144-IDEMPOTENT_REPLAY - Reintento con mismo body y clave.bru) · [EP144-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP144-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP144-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP144-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP144-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP144-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP145

`POST /api/v1/businesses/{businessId}/repair-orders` — Create repair order

Fuente: [RepairOrderController:84](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L84). Método Java: `create`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('REPAIR_ORDER_CREATE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<RepairOrderResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| request | RequestBody | CreateRepairOrderRequest | @Valid @RequestBody CreateRepairOrderRequest request |
| Idempotency-Key | RequestHeader | String | @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey |

Body completo (CreateRepairOrderRequest):

```json
{
  "branchId": "{{branchId}}",
  "customerId": "{{customerId}}",
  "customerDeviceId": "{{customerDeviceId}}",
  "appointmentId": "{{appointmentId}}",
  "businessServiceId": "{{businessServiceId}}",
  "assignedBusinessUserId": "{{assignedBusinessUserId}}",
  "problemDescription": "No enciende y no responde al cargar.",
  "diagnosis": "Posible falla en power IC.",
  "estimatedPrice": 150000,
  "estimatedDeliveryAt": "{{estimatedDeliveryAt}}",
  "receivedAt": "{{receivedAt}}"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "branchId": "{{branchId}}",
  "customerId": "{{customerId}}",
  "customerDeviceId": "{{customerDeviceId}}",
  "businessServiceId": "{{businessServiceId}}",
  "assignedBusinessUserId": "{{assignedBusinessUserId}}",
  "problemDescription": "No enciende y no responde al cargar."
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| branchId | Integer | sí | @NotNull |
| customerId | Integer | sí | @NotNull |
| customerDeviceId | Integer | sí | @NotNull |
| appointmentId | Integer | no |  |
| businessServiceId | Integer | sí | @NotNull |
| assignedBusinessUserId | Integer | sí | @NotNull |
| problemDescription | String | sí | @NotBlank @Size(max = 2000) |
| diagnosis | String | no | @Size(max = 2000) |
| estimatedPrice | BigDecimal | no | @DecimalMin(value = "0.00") |
| estimatedDeliveryAt | LocalDateTime | no |  |
| receivedAt | LocalDateTime | no |  |

Casos Bruno: [EP145-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP145-OK - Contrato válido.bru) · [EP145-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP145-NO_TOKEN - Sin autenticación.bru) · [EP145-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP145-INVALID_TOKEN - Token inválido.bru) · [EP145-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP145-REVOKED - Token previamente revocado.bru) · [EP145-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP145-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP145-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP145-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP145-branchId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-branchId_MISSING - branchId- ausente.bru) · [EP145-branchId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-branchId_NULL - branchId- null.bru) · [EP145-branchId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-branchId_TYPE - branchId- tipo incompatible.bru) · [EP145-customerId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-customerId_MISSING - customerId- ausente.bru) · [EP145-customerId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-customerId_NULL - customerId- null.bru) · [EP145-customerId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-customerId_TYPE - customerId- tipo incompatible.bru) · [EP145-customerDeviceId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-customerDeviceId_MISSING - customerDeviceId- ausente.bru) · [EP145-customerDeviceId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-customerDeviceId_NULL - customerDeviceId- null.bru) · [EP145-customerDeviceId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-customerDeviceId_TYPE - customerDeviceId- tipo incompatible.bru) · [EP145-appointmentId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-appointmentId_TYPE - appointmentId- tipo incompatible.bru) · [EP145-businessServiceId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-businessServiceId_MISSING - businessServiceId- ausente.bru) · [EP145-businessServiceId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-businessServiceId_NULL - businessServiceId- null.bru) · [EP145-businessServiceId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-businessServiceId_TYPE - businessServiceId- tipo incompatible.bru) · [EP145-assignedBusinessUserId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-assignedBusinessUserId_MISSING - assignedBusinessUserId- ausente.bru) · [EP145-assignedBusinessUserId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-assignedBusinessUserId_NULL - assignedBusinessUserId- null.bru) · [EP145-assignedBusinessUserId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-assignedBusinessUserId_TYPE - assignedBusinessUserId- tipo incompatible.bru) · [EP145-problemDescription_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-problemDescription_MISSING - problemDescription- ausente.bru) · [EP145-problemDescription_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-problemDescription_NULL - problemDescription- null.bru) · [EP145-problemDescription_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-problemDescription_EMPTY - problemDescription- EMPTY.bru) · [EP145-problemDescription_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-problemDescription_BLANK - problemDescription- BLANK.bru) · [EP145-problemDescription_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-problemDescription_MAX_PLUS_1 - problemDescription- 2001 caracteres.bru) · [EP145-diagnosis_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-diagnosis_MAX_PLUS_1 - diagnosis- 2001 caracteres.bru) · [EP145-estimatedPrice_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-estimatedPrice_TYPE - estimatedPrice- tipo incompatible.bru) · [EP145-estimatedPrice_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-estimatedPrice_DecimalMin - estimatedPrice- fuera de límite DecimalMin.bru) · [EP145-estimatedDeliveryAt_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-estimatedDeliveryAt_TYPE - estimatedDeliveryAt- tipo incompatible.bru) · [EP145-receivedAt_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP145-receivedAt_TYPE - receivedAt- tipo incompatible.bru) · [EP145-PILOT](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/REPAIR ORDER/EP145-PILOT - Create repair order.bru) · [EP145-IDEMPOTENT_FIRST](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP145-IDEMPOTENT_FIRST - Primera ejecución idempotente.bru) · [EP145-IDEMPOTENT_REPLAY](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP145-IDEMPOTENT_REPLAY - Reintento con mismo body y clave.bru) · [EP145-IDEMPOTENT_CONFLICT](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP145-IDEMPOTENT_CONFLICT - Misma clave con body diferente.bru) · [EP145-BODY_FOREIGN_ID](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP145-BODY_FOREIGN_ID - Rechazar relación de otro negocio en body.bru) · [EP145-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP145-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP145-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP145-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP145-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP145-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP146

`GET /api/v1/businesses/{businessId}/repair-orders` — List repair orders by business

Fuente: [RepairOrderController:98](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L98). Método Java: `findAll`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('REPAIR_ORDER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<RepairOrderResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| branchId | RequestParam | Integer | @RequestParam(required = false) Integer branchId |
| orderNumber | RequestParam | String | @RequestParam(required = false) String orderNumber |
| orderSequence | RequestParam | Integer | @RequestParam(required = false) Integer orderSequence |
| statusId | RequestParam | Integer | @RequestParam(required = false) Integer statusId |
| customerId | RequestParam | Integer | @RequestParam(required = false) Integer customerId |
| customer | RequestParam | String | @RequestParam(required = false) String customer |
| receivedFrom | RequestParam | LocalDateTime | @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime receivedFrom |
| receivedTo | RequestParam | LocalDateTime | @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime receivedTo |
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |
| receivedAt | RequestParam | String | @RequestParam(defaultValue = "receivedAt") String sortBy |
| desc | RequestParam | String | @RequestParam(defaultValue = "desc") String sortDir |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP146-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP146-OK - Contrato válido.bru) · [EP146-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP146-NO_TOKEN - Sin autenticación.bru) · [EP146-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP146-INVALID_TOKEN - Token inválido.bru) · [EP146-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP146-REVOKED - Token previamente revocado.bru) · [EP146-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP146-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP146-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP146-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP146-QUERY_branchId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-QUERY_branchId_TYPE - Query branchId- tipo inválido.bru) · [EP146-QUERY_orderSequence_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-QUERY_orderSequence_TYPE - Query orderSequence- tipo inválido.bru) · [EP146-QUERY_statusId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-QUERY_statusId_TYPE - Query statusId- tipo inválido.bru) · [EP146-QUERY_customerId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-QUERY_customerId_TYPE - Query customerId- tipo inválido.bru) · [EP146-QUERY_receivedFrom_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-QUERY_receivedFrom_TYPE - Query receivedFrom- tipo inválido.bru) · [EP146-QUERY_receivedTo_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-QUERY_receivedTo_TYPE - Query receivedTo- tipo inválido.bru) · [EP146-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP146-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP146-FILTER_branchId](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_branchId - Filtro branchId individual.bru) · [EP146-FILTER_orderNumber](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_orderNumber - Filtro orderNumber individual.bru) · [EP146-FILTER_orderSequence](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_orderSequence - Filtro orderSequence individual.bru) · [EP146-FILTER_statusId](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_statusId - Filtro statusId individual.bru) · [EP146-FILTER_customerId](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_customerId - Filtro customerId individual.bru) · [EP146-FILTER_customer](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_customer - Filtro customer individual.bru) · [EP146-FILTER_receivedFrom](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_receivedFrom - Filtro receivedFrom individual.bru) · [EP146-FILTER_receivedTo](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_receivedTo - Filtro receivedTo individual.bru) · [EP146-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-QUERY_0_Min - Query 0- límite inválido.bru) · [EP146-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-QUERY_20_Min - Query 20- límite inválido.bru) · [EP146-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-QUERY_20_Max - Query 20- límite inválido.bru) · [EP146-FILTER_branchId_orderNumber](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_branchId_orderNumber - Filtros combinados branchId + orderNumber.bru) · [EP146-FILTER_branchId_orderSequence](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_branchId_orderSequence - Filtros combinados branchId + orderSequence.bru) · [EP146-FILTER_branchId_statusId](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_branchId_statusId - Filtros combinados branchId + statusId.bru) · [EP146-FILTER_branchId_customerId](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_branchId_customerId - Filtros combinados branchId + customerId.bru) · [EP146-FILTER_branchId_customer](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_branchId_customer - Filtros combinados branchId + customer.bru) · [EP146-FILTER_branchId_receivedFrom](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_branchId_receivedFrom - Filtros combinados branchId + receivedFrom.bru) · [EP146-FILTER_branchId_receivedTo](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_branchId_receivedTo - Filtros combinados branchId + receivedTo.bru) · [EP146-FILTER_orderNumber_orderSequence](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_orderNumber_orderSequence - Filtros combinados orderNumber + orderSequence.bru) · [EP146-FILTER_orderNumber_statusId](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_orderNumber_statusId - Filtros combinados orderNumber + statusId.bru) · [EP146-FILTER_orderNumber_customerId](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_orderNumber_customerId - Filtros combinados orderNumber + customerId.bru) · [EP146-FILTER_orderNumber_customer](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_orderNumber_customer - Filtros combinados orderNumber + customer.bru) · [EP146-FILTER_orderNumber_receivedFrom](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_orderNumber_receivedFrom - Filtros combinados orderNumber + receivedFrom.bru) · [EP146-FILTER_orderNumber_receivedTo](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_orderNumber_receivedTo - Filtros combinados orderNumber + receivedTo.bru) · [EP146-FILTER_orderSequence_statusId](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_orderSequence_statusId - Filtros combinados orderSequence + statusId.bru) · [EP146-FILTER_orderSequence_customerId](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_orderSequence_customerId - Filtros combinados orderSequence + customerId.bru) · [EP146-FILTER_orderSequence_customer](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_orderSequence_customer - Filtros combinados orderSequence + customer.bru) · [EP146-FILTER_orderSequence_receivedFrom](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_orderSequence_receivedFrom - Filtros combinados orderSequence + receivedFrom.bru) · [EP146-FILTER_orderSequence_receivedTo](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_orderSequence_receivedTo - Filtros combinados orderSequence + receivedTo.bru) · [EP146-FILTER_statusId_customerId](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_statusId_customerId - Filtros combinados statusId + customerId.bru) · [EP146-FILTER_statusId_customer](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_statusId_customer - Filtros combinados statusId + customer.bru) · [EP146-FILTER_statusId_receivedFrom](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_statusId_receivedFrom - Filtros combinados statusId + receivedFrom.bru) · [EP146-FILTER_statusId_receivedTo](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_statusId_receivedTo - Filtros combinados statusId + receivedTo.bru) · [EP146-FILTER_customerId_customer](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_customerId_customer - Filtros combinados customerId + customer.bru) · [EP146-FILTER_customerId_receivedFrom](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_customerId_receivedFrom - Filtros combinados customerId + receivedFrom.bru) · [EP146-FILTER_customerId_receivedTo](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_customerId_receivedTo - Filtros combinados customerId + receivedTo.bru) · [EP146-FILTER_customer_receivedFrom](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_customer_receivedFrom - Filtros combinados customer + receivedFrom.bru) · [EP146-FILTER_customer_receivedTo](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_customer_receivedTo - Filtros combinados customer + receivedTo.bru) · [EP146-FILTER_receivedFrom_receivedTo](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP146-FILTER_receivedFrom_receivedTo - Filtros combinados receivedFrom + receivedTo.bru) · [EP146-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP146-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP146-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP146-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP146-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP146-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP147

`PATCH /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/status` — Change repair order status

Fuente: [RepairOrderController:135](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L135). Método Java: `updateStatus`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAnyAuthority('REPAIR_ORDER_UPDATE', 'REPAIR_ORDER_CLOSE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<RepairOrderResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |
| request | RequestBody | UpdateRepairOrderStatusRequest | @Valid @RequestBody UpdateRepairOrderStatusRequest request |

Body completo (UpdateRepairOrderStatusRequest):

```json
{
  "statusId": "{{statusId}}",
  "comment": "Se finalizó diagnóstico y pasa a reparación.",
  "visibility": "INTERNAL"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "statusId": "{{statusId}}"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| statusId | Integer | sí | @NotNull |
| comment | String | no | @Size(max = 2000) |
| visibility | RepairOrderHistoryVisibility | no |  |

Casos Bruno: [EP147-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP147-OK - Contrato válido.bru) · [EP147-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP147-NO_TOKEN - Sin autenticación.bru) · [EP147-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP147-INVALID_TOKEN - Token inválido.bru) · [EP147-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP147-REVOKED - Token previamente revocado.bru) · [EP147-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP147-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP147-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP147-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP147-statusId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP147-statusId_MISSING - statusId- ausente.bru) · [EP147-statusId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP147-statusId_NULL - statusId- null.bru) · [EP147-statusId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP147-statusId_TYPE - statusId- tipo incompatible.bru) · [EP147-comment_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP147-comment_MAX_PLUS_1 - comment- 2001 caracteres.bru) · [EP147-FLOW_DIAGNOSIS](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/REPAIR ORDER/EP147-FLOW_DIAGNOSIS - Transición a DIAGNOSIS.bru) · [EP147-FLOW_WAITING_APPROVAL](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/REPAIR ORDER/EP147-FLOW_WAITING_APPROVAL - Transición a WAITING_APPROVAL.bru) · [EP147-FLOW_WAITING_PARTS](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/REPAIR ORDER/EP147-FLOW_WAITING_PARTS - Transición a WAITING_PARTS.bru) · [EP147-FLOW_REPAIRING](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/REPAIR ORDER/EP147-FLOW_REPAIRING - Transición a REPAIRING.bru) · [EP147-FLOW_TESTING](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/REPAIR ORDER/EP147-FLOW_TESTING - Transición a TESTING.bru) · [EP147-FLOW_READY_FOR_PICKUP](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/REPAIR ORDER/EP147-FLOW_READY_FOR_PICKUP - Transición a READY_FOR_PICKUP.bru) · [EP147-FLOW_DELIVERED](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/REPAIR ORDER/EP147-FLOW_DELIVERED - Transición a DELIVERED.bru) · [EP147-JUMP_DELIVERED](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP147-JUMP_DELIVERED - Prohibir RECEIVED a DELIVERED.bru) · [EP147-TECH_CLOSE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP147-TECH_CLOSE - Técnico no entrega orden.bru) · [EP147-STATE_RECEIVED_RECEIVED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_RECEIVED_RECEIVED - RECEIVED a RECEIVED.bru) · [EP147-STATE_RECEIVED_DIAGNOSIS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_RECEIVED_DIAGNOSIS - RECEIVED a DIAGNOSIS.bru) · [EP147-STATE_RECEIVED_WAITING_APPROVAL](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_RECEIVED_WAITING_APPROVAL - RECEIVED a WAITING_APPROVAL.bru) · [EP147-STATE_RECEIVED_WAITING_PARTS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_RECEIVED_WAITING_PARTS - RECEIVED a WAITING_PARTS.bru) · [EP147-STATE_RECEIVED_REPAIRING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_RECEIVED_REPAIRING - RECEIVED a REPAIRING.bru) · [EP147-STATE_RECEIVED_TESTING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_RECEIVED_TESTING - RECEIVED a TESTING.bru) · [EP147-STATE_RECEIVED_READY_FOR_PICKUP](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_RECEIVED_READY_FOR_PICKUP - RECEIVED a READY_FOR_PICKUP.bru) · [EP147-STATE_RECEIVED_DELIVERED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_RECEIVED_DELIVERED - RECEIVED a DELIVERED.bru) · [EP147-STATE_RECEIVED_CANCELLED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_RECEIVED_CANCELLED - RECEIVED a CANCELLED.bru) · [EP147-STATE_DIAGNOSIS_RECEIVED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DIAGNOSIS_RECEIVED - DIAGNOSIS a RECEIVED.bru) · [EP147-STATE_DIAGNOSIS_DIAGNOSIS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DIAGNOSIS_DIAGNOSIS - DIAGNOSIS a DIAGNOSIS.bru) · [EP147-STATE_DIAGNOSIS_WAITING_APPROVAL](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DIAGNOSIS_WAITING_APPROVAL - DIAGNOSIS a WAITING_APPROVAL.bru) · [EP147-STATE_DIAGNOSIS_WAITING_PARTS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DIAGNOSIS_WAITING_PARTS - DIAGNOSIS a WAITING_PARTS.bru) · [EP147-STATE_DIAGNOSIS_REPAIRING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DIAGNOSIS_REPAIRING - DIAGNOSIS a REPAIRING.bru) · [EP147-STATE_DIAGNOSIS_TESTING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DIAGNOSIS_TESTING - DIAGNOSIS a TESTING.bru) · [EP147-STATE_DIAGNOSIS_READY_FOR_PICKUP](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DIAGNOSIS_READY_FOR_PICKUP - DIAGNOSIS a READY_FOR_PICKUP.bru) · [EP147-STATE_DIAGNOSIS_DELIVERED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DIAGNOSIS_DELIVERED - DIAGNOSIS a DELIVERED.bru) · [EP147-STATE_DIAGNOSIS_CANCELLED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DIAGNOSIS_CANCELLED - DIAGNOSIS a CANCELLED.bru) · [EP147-STATE_WAITING_APPROVAL_RECEIVED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_APPROVAL_RECEIVED - WAITING_APPROVAL a RECEIVED.bru) · [EP147-STATE_WAITING_APPROVAL_DIAGNOSIS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_APPROVAL_DIAGNOSIS - WAITING_APPROVAL a DIAGNOSIS.bru) · [EP147-STATE_WAITING_APPROVAL_WAITING_APPROVAL](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_APPROVAL_WAITING_APPROVAL - WAITING_APPROVAL a WAITING_APPROVAL.bru) · [EP147-STATE_WAITING_APPROVAL_WAITING_PARTS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_APPROVAL_WAITING_PARTS - WAITING_APPROVAL a WAITING_PARTS.bru) · [EP147-STATE_WAITING_APPROVAL_REPAIRING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_APPROVAL_REPAIRING - WAITING_APPROVAL a REPAIRING.bru) · [EP147-STATE_WAITING_APPROVAL_TESTING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_APPROVAL_TESTING - WAITING_APPROVAL a TESTING.bru) · [EP147-STATE_WAITING_APPROVAL_READY_FOR_PICKUP](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_APPROVAL_READY_FOR_PICKUP - WAITING_APPROVAL a READY_FOR_PICKUP.bru) · [EP147-STATE_WAITING_APPROVAL_DELIVERED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_APPROVAL_DELIVERED - WAITING_APPROVAL a DELIVERED.bru) · [EP147-STATE_WAITING_APPROVAL_CANCELLED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_APPROVAL_CANCELLED - WAITING_APPROVAL a CANCELLED.bru) · [EP147-STATE_WAITING_PARTS_RECEIVED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_PARTS_RECEIVED - WAITING_PARTS a RECEIVED.bru) · [EP147-STATE_WAITING_PARTS_DIAGNOSIS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_PARTS_DIAGNOSIS - WAITING_PARTS a DIAGNOSIS.bru) · [EP147-STATE_WAITING_PARTS_WAITING_APPROVAL](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_PARTS_WAITING_APPROVAL - WAITING_PARTS a WAITING_APPROVAL.bru) · [EP147-STATE_WAITING_PARTS_WAITING_PARTS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_PARTS_WAITING_PARTS - WAITING_PARTS a WAITING_PARTS.bru) · [EP147-STATE_WAITING_PARTS_REPAIRING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_PARTS_REPAIRING - WAITING_PARTS a REPAIRING.bru) · [EP147-STATE_WAITING_PARTS_TESTING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_PARTS_TESTING - WAITING_PARTS a TESTING.bru) · [EP147-STATE_WAITING_PARTS_READY_FOR_PICKUP](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_PARTS_READY_FOR_PICKUP - WAITING_PARTS a READY_FOR_PICKUP.bru) · [EP147-STATE_WAITING_PARTS_DELIVERED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_PARTS_DELIVERED - WAITING_PARTS a DELIVERED.bru) · [EP147-STATE_WAITING_PARTS_CANCELLED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_WAITING_PARTS_CANCELLED - WAITING_PARTS a CANCELLED.bru) · [EP147-STATE_REPAIRING_RECEIVED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_REPAIRING_RECEIVED - REPAIRING a RECEIVED.bru) · [EP147-STATE_REPAIRING_DIAGNOSIS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_REPAIRING_DIAGNOSIS - REPAIRING a DIAGNOSIS.bru) · [EP147-STATE_REPAIRING_WAITING_APPROVAL](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_REPAIRING_WAITING_APPROVAL - REPAIRING a WAITING_APPROVAL.bru) · [EP147-STATE_REPAIRING_WAITING_PARTS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_REPAIRING_WAITING_PARTS - REPAIRING a WAITING_PARTS.bru) · [EP147-STATE_REPAIRING_REPAIRING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_REPAIRING_REPAIRING - REPAIRING a REPAIRING.bru) · [EP147-STATE_REPAIRING_TESTING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_REPAIRING_TESTING - REPAIRING a TESTING.bru) · [EP147-STATE_REPAIRING_READY_FOR_PICKUP](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_REPAIRING_READY_FOR_PICKUP - REPAIRING a READY_FOR_PICKUP.bru) · [EP147-STATE_REPAIRING_DELIVERED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_REPAIRING_DELIVERED - REPAIRING a DELIVERED.bru) · [EP147-STATE_REPAIRING_CANCELLED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_REPAIRING_CANCELLED - REPAIRING a CANCELLED.bru) · [EP147-STATE_TESTING_RECEIVED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_TESTING_RECEIVED - TESTING a RECEIVED.bru) · [EP147-STATE_TESTING_DIAGNOSIS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_TESTING_DIAGNOSIS - TESTING a DIAGNOSIS.bru) · [EP147-STATE_TESTING_WAITING_APPROVAL](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_TESTING_WAITING_APPROVAL - TESTING a WAITING_APPROVAL.bru) · [EP147-STATE_TESTING_WAITING_PARTS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_TESTING_WAITING_PARTS - TESTING a WAITING_PARTS.bru) · [EP147-STATE_TESTING_REPAIRING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_TESTING_REPAIRING - TESTING a REPAIRING.bru) · [EP147-STATE_TESTING_TESTING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_TESTING_TESTING - TESTING a TESTING.bru) · [EP147-STATE_TESTING_READY_FOR_PICKUP](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_TESTING_READY_FOR_PICKUP - TESTING a READY_FOR_PICKUP.bru) · [EP147-STATE_TESTING_DELIVERED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_TESTING_DELIVERED - TESTING a DELIVERED.bru) · [EP147-STATE_TESTING_CANCELLED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_TESTING_CANCELLED - TESTING a CANCELLED.bru) · [EP147-STATE_READY_FOR_PICKUP_RECEIVED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_READY_FOR_PICKUP_RECEIVED - READY_FOR_PICKUP a RECEIVED.bru) · [EP147-STATE_READY_FOR_PICKUP_DIAGNOSIS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_READY_FOR_PICKUP_DIAGNOSIS - READY_FOR_PICKUP a DIAGNOSIS.bru) · [EP147-STATE_READY_FOR_PICKUP_WAITING_APPROVAL](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_READY_FOR_PICKUP_WAITING_APPROVAL - READY_FOR_PICKUP a WAITING_APPROVAL.bru) · [EP147-STATE_READY_FOR_PICKUP_WAITING_PARTS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_READY_FOR_PICKUP_WAITING_PARTS - READY_FOR_PICKUP a WAITING_PARTS.bru) · [EP147-STATE_READY_FOR_PICKUP_REPAIRING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_READY_FOR_PICKUP_REPAIRING - READY_FOR_PICKUP a REPAIRING.bru) · [EP147-STATE_READY_FOR_PICKUP_TESTING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_READY_FOR_PICKUP_TESTING - READY_FOR_PICKUP a TESTING.bru) · [EP147-STATE_READY_FOR_PICKUP_READY_FOR_PICKUP](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_READY_FOR_PICKUP_READY_FOR_PICKUP - READY_FOR_PICKUP a READY_FOR_PICKUP.bru) · [EP147-STATE_READY_FOR_PICKUP_DELIVERED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_READY_FOR_PICKUP_DELIVERED - READY_FOR_PICKUP a DELIVERED.bru) · [EP147-STATE_READY_FOR_PICKUP_CANCELLED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_READY_FOR_PICKUP_CANCELLED - READY_FOR_PICKUP a CANCELLED.bru) · [EP147-STATE_DELIVERED_RECEIVED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DELIVERED_RECEIVED - DELIVERED a RECEIVED.bru) · [EP147-STATE_DELIVERED_DIAGNOSIS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DELIVERED_DIAGNOSIS - DELIVERED a DIAGNOSIS.bru) · [EP147-STATE_DELIVERED_WAITING_APPROVAL](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DELIVERED_WAITING_APPROVAL - DELIVERED a WAITING_APPROVAL.bru) · [EP147-STATE_DELIVERED_WAITING_PARTS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DELIVERED_WAITING_PARTS - DELIVERED a WAITING_PARTS.bru) · [EP147-STATE_DELIVERED_REPAIRING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DELIVERED_REPAIRING - DELIVERED a REPAIRING.bru) · [EP147-STATE_DELIVERED_TESTING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DELIVERED_TESTING - DELIVERED a TESTING.bru) · [EP147-STATE_DELIVERED_READY_FOR_PICKUP](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DELIVERED_READY_FOR_PICKUP - DELIVERED a READY_FOR_PICKUP.bru) · [EP147-STATE_DELIVERED_DELIVERED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DELIVERED_DELIVERED - DELIVERED a DELIVERED.bru) · [EP147-STATE_DELIVERED_CANCELLED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_DELIVERED_CANCELLED - DELIVERED a CANCELLED.bru) · [EP147-STATE_CANCELLED_RECEIVED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_CANCELLED_RECEIVED - CANCELLED a RECEIVED.bru) · [EP147-STATE_CANCELLED_DIAGNOSIS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_CANCELLED_DIAGNOSIS - CANCELLED a DIAGNOSIS.bru) · [EP147-STATE_CANCELLED_WAITING_APPROVAL](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_CANCELLED_WAITING_APPROVAL - CANCELLED a WAITING_APPROVAL.bru) · [EP147-STATE_CANCELLED_WAITING_PARTS](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_CANCELLED_WAITING_PARTS - CANCELLED a WAITING_PARTS.bru) · [EP147-STATE_CANCELLED_REPAIRING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_CANCELLED_REPAIRING - CANCELLED a REPAIRING.bru) · [EP147-STATE_CANCELLED_TESTING](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_CANCELLED_TESTING - CANCELLED a TESTING.bru) · [EP147-STATE_CANCELLED_READY_FOR_PICKUP](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_CANCELLED_READY_FOR_PICKUP - CANCELLED a READY_FOR_PICKUP.bru) · [EP147-STATE_CANCELLED_DELIVERED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_CANCELLED_DELIVERED - CANCELLED a DELIVERED.bru) · [EP147-STATE_CANCELLED_CANCELLED](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP147-STATE_CANCELLED_CANCELLED - CANCELLED a CANCELLED.bru) · [EP147-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP147-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP147-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP147-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP147-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP147-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP148

`PATCH /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/financials` — Update repair order final price

Fuente: [RepairOrderController:148](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L148). Método Java: `updateFinancials`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('REPAIR_ORDER_FINANCIAL_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<RepairOrderResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |
| request | RequestBody | UpdateRepairOrderFinancialsRequest | @Valid @RequestBody UpdateRepairOrderFinancialsRequest request |

Body completo (UpdateRepairOrderFinancialsRequest):

```json
{
  "finalPrice": 300000
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "finalPrice": 300000
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| finalPrice | BigDecimal | sí | @NotNull @DecimalMin("0.00") @Digits(integer = 10, fraction = 2) |

Casos Bruno: [EP148-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP148-OK - Contrato válido.bru) · [EP148-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP148-NO_TOKEN - Sin autenticación.bru) · [EP148-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP148-INVALID_TOKEN - Token inválido.bru) · [EP148-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP148-REVOKED - Token previamente revocado.bru) · [EP148-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP148-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP148-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP148-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP148-finalPrice_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP148-finalPrice_MISSING - finalPrice- ausente.bru) · [EP148-finalPrice_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP148-finalPrice_NULL - finalPrice- null.bru) · [EP148-finalPrice_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP148-finalPrice_TYPE - finalPrice- tipo incompatible.bru) · [EP148-finalPrice_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP148-finalPrice_DecimalMin - finalPrice- fuera de límite DecimalMin.bru) · [EP148-TECH_PRICE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP148-TECH_PRICE - Técnico no cambia precio.bru) · [EP148-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP148-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP148-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP148-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP148-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP148-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP149

`PATCH /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/diagnosis` — Update repair order diagnosis

Fuente: [RepairOrderController:161](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L161). Método Java: `updateDiagnosis`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('REPAIR_ORDER_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<RepairOrderResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |
| request | RequestBody | UpdateRepairOrderDiagnosisRequest | @Valid @RequestBody UpdateRepairOrderDiagnosisRequest request |

Body completo (UpdateRepairOrderDiagnosisRequest):

```json
{
  "diagnosis": "Falla confirmada en el circuito de carga."
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "diagnosis": "Falla confirmada en el circuito de carga."
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| diagnosis | String | sí | @NotBlank @Size(max = 2000) |

Casos Bruno: [EP149-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP149-OK - Contrato válido.bru) · [EP149-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP149-NO_TOKEN - Sin autenticación.bru) · [EP149-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP149-INVALID_TOKEN - Token inválido.bru) · [EP149-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP149-REVOKED - Token previamente revocado.bru) · [EP149-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP149-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP149-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP149-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP149-diagnosis_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP149-diagnosis_MISSING - diagnosis- ausente.bru) · [EP149-diagnosis_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP149-diagnosis_NULL - diagnosis- null.bru) · [EP149-diagnosis_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP149-diagnosis_EMPTY - diagnosis- EMPTY.bru) · [EP149-diagnosis_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP149-diagnosis_BLANK - diagnosis- BLANK.bru) · [EP149-diagnosis_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP149-diagnosis_MAX_PLUS_1 - diagnosis- 2001 caracteres.bru) · [EP149-PILOT](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/REPAIR ORDER/EP149-PILOT - Update repair order diagnosis.bru) · [EP149-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP149-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP149-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP149-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP149-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP149-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP150

`PATCH /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/assignment` — Assign repair order technician

Fuente: [RepairOrderController:174](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L174). Método Java: `updateAssignment`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('REPAIR_ORDER_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<RepairOrderResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |
| request | RequestBody | UpdateRepairOrderAssignmentRequest | @Valid @RequestBody UpdateRepairOrderAssignmentRequest request |

Body completo (UpdateRepairOrderAssignmentRequest):

```json
{
  "assignedBusinessUserId": "{{assignedBusinessUserId}}"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "assignedBusinessUserId": "{{assignedBusinessUserId}}"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| assignedBusinessUserId | Integer | sí | @NotNull |

Casos Bruno: [EP150-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP150-OK - Contrato válido.bru) · [EP150-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP150-NO_TOKEN - Sin autenticación.bru) · [EP150-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP150-INVALID_TOKEN - Token inválido.bru) · [EP150-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP150-REVOKED - Token previamente revocado.bru) · [EP150-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP150-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP150-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP150-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP150-assignedBusinessUserId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP150-assignedBusinessUserId_MISSING - assignedBusinessUserId- ausente.bru) · [EP150-assignedBusinessUserId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP150-assignedBusinessUserId_NULL - assignedBusinessUserId- null.bru) · [EP150-assignedBusinessUserId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP150-assignedBusinessUserId_TYPE - assignedBusinessUserId- tipo incompatible.bru) · [EP150-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP150-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP150-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP150-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP150-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP150-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP151

`GET /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/history` — Get repair order history

Fuente: [RepairOrderController:187](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L187). Método Java: `findHistory`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('REPAIR_ORDER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<RepairOrderHistoryResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP151-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP151-OK - Contrato válido.bru) · [EP151-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP151-NO_TOKEN - Sin autenticación.bru) · [EP151-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP151-INVALID_TOKEN - Token inválido.bru) · [EP151-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP151-REVOKED - Token previamente revocado.bru) · [EP151-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP151-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP151-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP151-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP151-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP151-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP151-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP151-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP151-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP151-QUERY_0_Min - Query 0- límite inválido.bru) · [EP151-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP151-QUERY_20_Min - Query 20- límite inválido.bru) · [EP151-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP151-QUERY_20_Max - Query 20- límite inválido.bru) · [EP151-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP151-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP151-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP151-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP151-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP151-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP152

`GET /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/quality-checklist` — Get repair order quality checklist

Fuente: [RepairOrderController:207](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L207). Método Java: `findQualityChecklist`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_QUALITY_CHECK_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<QualityChecklistResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP152-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP152-OK - Contrato válido.bru) · [EP152-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP152-NO_TOKEN - Sin autenticación.bru) · [EP152-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP152-INVALID_TOKEN - Token inválido.bru) · [EP152-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP152-REVOKED - Token previamente revocado.bru) · [EP152-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP152-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP152-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP152-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP152-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP152-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP152-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP152-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP152-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP152-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP153

`PUT /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/quality-checklist` — Save repair order quality checklist

Fuente: [RepairOrderController:223](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L223). Método Java: `upsertQualityChecklist`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_QUALITY_CHECK_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<QualityChecklistResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |
| request | RequestBody | UpsertQualityChecklistRequest | @Valid @RequestBody UpsertQualityChecklistRequest request |

Body completo (UpsertQualityChecklistRequest):

```json
{
  "repairOrderId": "{{repairOrderId}}",
  "items": [
    {
      "businessQualityTestId": "{{businessQualityTestId}}",
      "result": "SI",
      "notes": "Pantalla responde correctamente en toda el área táctil."
    }
  ]
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "items": [
    {
      "businessQualityTestId": "{{businessQualityTestId}}",
      "result": "SI"
    }
  ]
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| repairOrderId | Integer | no | @Positive |
| items | List<QualityChecklistItemRequest> | sí | @NotEmpty @Size(max = 100) @Valid |

Casos Bruno: [EP153-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP153-OK - Contrato válido.bru) · [EP153-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP153-NO_TOKEN - Sin autenticación.bru) · [EP153-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP153-INVALID_TOKEN - Token inválido.bru) · [EP153-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP153-REVOKED - Token previamente revocado.bru) · [EP153-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP153-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP153-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP153-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP153-repairOrderId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP153-repairOrderId_TYPE - repairOrderId- tipo incompatible.bru) · [EP153-repairOrderId_Positive](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP153-repairOrderId_Positive - repairOrderId- fuera de límite Positive.bru) · [EP153-items_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP153-items_MISSING - items- ausente.bru) · [EP153-items_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP153-items_NULL - items- null.bru) · [EP153-items_EMPTY_ARRAY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP153-items_EMPTY_ARRAY - items- colección vacía.bru) · [EP153-items_MAX_ARRAY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP153-items_MAX_ARRAY - items- 101 elementos.bru) · [EP153-items_businessQualityTestId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP153-items_businessQualityTestId_MISSING - items[0].businessQualityTestId- ausente.bru) · [EP153-items_result_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP153-items_result_MISSING - items[0].result- ausente.bru) · [EP153-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP153-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP153-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP153-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP153-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP153-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP154

`DELETE /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/quality-checklist` — Delete repair order quality checklist

Fuente: [RepairOrderController:241](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L241). Método Java: `deleteQualityChecklist`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('BUSINESS_QUALITY_CHECK_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP154-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP154-OK - Contrato válido.bru) · [EP154-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP154-NO_TOKEN - Sin autenticación.bru) · [EP154-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP154-INVALID_TOKEN - Token inválido.bru) · [EP154-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP154-REVOKED - Token previamente revocado.bru) · [EP154-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP154-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP154-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP154-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP154-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP154-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP154-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP154-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP154-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP154-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP155

`GET /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/attachments` — List repair order attachments

Fuente: [RepairOrderController:252](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L252). Método Java: `findAttachments`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('REPAIR_ORDER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<RepairOrderAttachmentResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP155-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP155-OK - Contrato válido.bru) · [EP155-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP155-NO_TOKEN - Sin autenticación.bru) · [EP155-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP155-INVALID_TOKEN - Token inválido.bru) · [EP155-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP155-REVOKED - Token previamente revocado.bru) · [EP155-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP155-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP155-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP155-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP155-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP155-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP155-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP155-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP155-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP155-QUERY_0_Min - Query 0- límite inválido.bru) · [EP155-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP155-QUERY_20_Min - Query 20- límite inválido.bru) · [EP155-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP155-QUERY_20_Max - Query 20- límite inválido.bru) · [EP155-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP155-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP155-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP155-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP155-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP155-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP156

`POST /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/attachments/upload` — Upload repair order evidence

Fuente: [RepairOrderController:270](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L270). Método Java: `uploadAttachment`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('REPAIR_ORDER_UPLOAD_EVIDENCE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<RepairOrderAttachmentResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |
| file | RequestPart | MultipartFile | @RequestPart("file") MultipartFile file |
| description | RequestPart | String | @RequestPart(value = "description", required = false) String description |

Body multipart: seleccionar archivo real en el campo `file`; Bruno genera el boundary. No usar JSON.

Casos Bruno: [EP156-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP156-OK - Contrato válido.bru) · [EP156-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP156-NO_TOKEN - Sin autenticación.bru) · [EP156-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP156-INVALID_TOKEN - Token inválido.bru) · [EP156-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP156-REVOKED - Token previamente revocado.bru) · [EP156-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP156-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP156-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP156-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP156-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP156-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP156-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP156-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP156-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP156-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP157

`DELETE /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/attachments/{attachmentId}` — Delete repair order attachment

Fuente: [RepairOrderController:289](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L289). Método Java: `deleteAttachment`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('REPAIR_ORDER_UPLOAD_EVIDENCE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |
| attachmentId | PathVariable | Integer | @PathVariable Integer attachmentId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP157-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP157-OK - Contrato válido.bru) · [EP157-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP157-NO_TOKEN - Sin autenticación.bru) · [EP157-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP157-INVALID_TOKEN - Token inválido.bru) · [EP157-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP157-REVOKED - Token previamente revocado.bru) · [EP157-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP157-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP157-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP157-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP157-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP157-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP157-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP157-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP157-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP157-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP158

`GET /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/parts` — List repair order parts

Fuente: [RepairOrderController:305](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L305). Método Java: `findParts`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('REPAIR_ORDER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<RepairOrderPartResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP158-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP158-OK - Contrato válido.bru) · [EP158-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP158-NO_TOKEN - Sin autenticación.bru) · [EP158-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP158-INVALID_TOKEN - Token inválido.bru) · [EP158-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP158-REVOKED - Token previamente revocado.bru) · [EP158-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP158-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP158-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP158-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP158-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP158-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP158-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP158-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP158-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP158-QUERY_0_Min - Query 0- límite inválido.bru) · [EP158-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP158-QUERY_20_Min - Query 20- límite inválido.bru) · [EP158-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP158-QUERY_20_Max - Query 20- límite inválido.bru) · [EP158-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP158-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP158-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP158-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP158-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP158-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP159

`POST /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/parts` — Create repair order part

Fuente: [RepairOrderController:323](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L323). Método Java: `createPart`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('REPAIR_ORDER_UPDATE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<RepairOrderPartResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |
| request | RequestBody | CreateRepairOrderPartRequest | @Valid @RequestBody CreateRepairOrderPartRequest request |
| Idempotency-Key | RequestHeader | String | @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey |

Body completo (CreateRepairOrderPartRequest):

```json
{
  "inventoryItemId": "{{inventoryItemId}}",
  "quantity": 1,
  "unitPrice": 85000,
  "usedByBusinessUserId": "{{usedByBusinessUserId}}"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "inventoryItemId": "{{inventoryItemId}}",
  "quantity": 1,
  "unitPrice": 85000,
  "usedByBusinessUserId": "{{usedByBusinessUserId}}"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| inventoryItemId | Integer | sí | @NotNull |
| quantity | Integer | sí | @NotNull @Positive |
| unitPrice | BigDecimal | sí | @NotNull @DecimalMin(value = "0.0", inclusive = false) |
| usedByBusinessUserId | Integer | sí | @NotNull |

Casos Bruno: [EP159-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP159-OK - Contrato válido.bru) · [EP159-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP159-NO_TOKEN - Sin autenticación.bru) · [EP159-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP159-INVALID_TOKEN - Token inválido.bru) · [EP159-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP159-REVOKED - Token previamente revocado.bru) · [EP159-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP159-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP159-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP159-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP159-inventoryItemId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP159-inventoryItemId_MISSING - inventoryItemId- ausente.bru) · [EP159-inventoryItemId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP159-inventoryItemId_NULL - inventoryItemId- null.bru) · [EP159-inventoryItemId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP159-inventoryItemId_TYPE - inventoryItemId- tipo incompatible.bru) · [EP159-quantity_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP159-quantity_MISSING - quantity- ausente.bru) · [EP159-quantity_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP159-quantity_NULL - quantity- null.bru) · [EP159-quantity_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP159-quantity_TYPE - quantity- tipo incompatible.bru) · [EP159-quantity_Positive](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP159-quantity_Positive - quantity- fuera de límite Positive.bru) · [EP159-unitPrice_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP159-unitPrice_MISSING - unitPrice- ausente.bru) · [EP159-unitPrice_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP159-unitPrice_NULL - unitPrice- null.bru) · [EP159-unitPrice_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP159-unitPrice_TYPE - unitPrice- tipo incompatible.bru) · [EP159-unitPrice_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP159-unitPrice_DecimalMin - unitPrice- fuera de límite DecimalMin.bru) · [EP159-usedByBusinessUserId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP159-usedByBusinessUserId_MISSING - usedByBusinessUserId- ausente.bru) · [EP159-usedByBusinessUserId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP159-usedByBusinessUserId_NULL - usedByBusinessUserId- null.bru) · [EP159-usedByBusinessUserId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP159-usedByBusinessUserId_TYPE - usedByBusinessUserId- tipo incompatible.bru) · [EP159-IDEMPOTENT_FIRST](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP159-IDEMPOTENT_FIRST - Primera ejecución idempotente.bru) · [EP159-IDEMPOTENT_REPLAY](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP159-IDEMPOTENT_REPLAY - Reintento con mismo body y clave.bru) · [EP159-IDEMPOTENT_CONFLICT](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/REPAIR ORDER/EP159-IDEMPOTENT_CONFLICT - Misma clave con body diferente.bru) · [EP159-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP159-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP159-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP159-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP159-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP159-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP160

`PUT /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/parts/{partId}` — Update repair order part

Fuente: [RepairOrderController:342](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L342). Método Java: `updatePart`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('REPAIR_ORDER_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<RepairOrderPartResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |
| partId | PathVariable | Integer | @PathVariable Integer partId |
| request | RequestBody | UpdateRepairOrderPartRequest | @Valid @RequestBody UpdateRepairOrderPartRequest request |

Body completo (UpdateRepairOrderPartRequest):

```json
{
  "inventoryItemId": "{{inventoryItemId}}",
  "quantity": 2,
  "unitPrice": 85000,
  "usedByBusinessUserId": "{{usedByBusinessUserId}}"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "inventoryItemId": "{{inventoryItemId}}",
  "quantity": 2,
  "unitPrice": 85000,
  "usedByBusinessUserId": "{{usedByBusinessUserId}}"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| inventoryItemId | Integer | sí | @NotNull |
| quantity | Integer | sí | @NotNull @Positive |
| unitPrice | BigDecimal | sí | @NotNull @DecimalMin(value = "0.0", inclusive = false) |
| usedByBusinessUserId | Integer | sí | @NotNull |

Casos Bruno: [EP160-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP160-OK - Contrato válido.bru) · [EP160-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP160-NO_TOKEN - Sin autenticación.bru) · [EP160-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP160-INVALID_TOKEN - Token inválido.bru) · [EP160-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP160-REVOKED - Token previamente revocado.bru) · [EP160-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP160-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP160-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP160-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP160-inventoryItemId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP160-inventoryItemId_MISSING - inventoryItemId- ausente.bru) · [EP160-inventoryItemId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP160-inventoryItemId_NULL - inventoryItemId- null.bru) · [EP160-inventoryItemId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP160-inventoryItemId_TYPE - inventoryItemId- tipo incompatible.bru) · [EP160-quantity_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP160-quantity_MISSING - quantity- ausente.bru) · [EP160-quantity_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP160-quantity_NULL - quantity- null.bru) · [EP160-quantity_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP160-quantity_TYPE - quantity- tipo incompatible.bru) · [EP160-quantity_Positive](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP160-quantity_Positive - quantity- fuera de límite Positive.bru) · [EP160-unitPrice_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP160-unitPrice_MISSING - unitPrice- ausente.bru) · [EP160-unitPrice_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP160-unitPrice_NULL - unitPrice- null.bru) · [EP160-unitPrice_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP160-unitPrice_TYPE - unitPrice- tipo incompatible.bru) · [EP160-unitPrice_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP160-unitPrice_DecimalMin - unitPrice- fuera de límite DecimalMin.bru) · [EP160-usedByBusinessUserId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP160-usedByBusinessUserId_MISSING - usedByBusinessUserId- ausente.bru) · [EP160-usedByBusinessUserId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP160-usedByBusinessUserId_NULL - usedByBusinessUserId- null.bru) · [EP160-usedByBusinessUserId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/REPAIR ORDER/EP160-usedByBusinessUserId_TYPE - usedByBusinessUserId- tipo incompatible.bru) · [EP160-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP160-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP160-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP160-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP160-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP160-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP161

`DELETE /api/v1/businesses/{businessId}/repair-orders/{repairOrderId}/parts/{partId}` — Delete repair order part

Fuente: [RepairOrderController:360](../../src/main/java/com/tecmarket/backend/repairorder/controller/RepairOrderController.java#L360). Método Java: `deletePart`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('REPAIR_ORDER_UPDATE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| repairOrderId | PathVariable | Integer | @PathVariable Integer repairOrderId |
| partId | PathVariable | Integer | @PathVariable Integer partId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP161-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/REPAIR ORDER/EP161-OK - Contrato válido.bru) · [EP161-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP161-NO_TOKEN - Sin autenticación.bru) · [EP161-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP161-INVALID_TOKEN - Token inválido.bru) · [EP161-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/REPAIR ORDER/EP161-REVOKED - Token previamente revocado.bru) · [EP161-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP161-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP161-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP161-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP161-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP161-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP161-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP161-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP161-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/REPAIR ORDER/EP161-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP162

`GET /api/v1/parameters/roles` — List roles

Fuente: [RoleParameterController:38](../../src/main/java/com/tecmarket/backend/parameter/controller/RoleParameterController.java#L38). Método Java: `findAll`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<RoleResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|


Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP162-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/ROLE/EP162-OK - Contrato válido.bru) · [EP162-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE/EP162-NO_TOKEN - Sin autenticación.bru) · [EP162-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE/EP162-INVALID_TOKEN - Token inválido.bru) · [EP162-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE/EP162-REVOKED - Token previamente revocado.bru) · [EP162-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/ROLE/EP162-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP163

`GET /api/v1/parameters/roles/{id}` — Find role by ID

Fuente: [RoleParameterController:47](../../src/main/java/com/tecmarket/backend/parameter/controller/RoleParameterController.java#L47). Método Java: `findById`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<RoleResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP163-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/ROLE/EP163-OK - Contrato válido.bru) · [EP163-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE/EP163-NO_TOKEN - Sin autenticación.bru) · [EP163-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE/EP163-INVALID_TOKEN - Token inválido.bru) · [EP163-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE/EP163-REVOKED - Token previamente revocado.bru) · [EP163-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/ROLE/EP163-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP163-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP163-NOT_FOUND - ID inexistente comprobado.bru)

## EP164

`POST /api/v1/parameters/roles` — Create role

Fuente: [RoleParameterController:56](../../src/main/java/com/tecmarket/backend/parameter/controller/RoleParameterController.java#L56). Método Java: `create`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<RoleResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| request | RequestBody | CreateRoleRequest | @Valid @RequestBody CreateRoleRequest request |

Body completo (CreateRoleRequest):

```json
{
  "code": "{{qaCode}}",
  "name": "Usuario QA",
  "description": "Usuario encargado de diagnosticar y reparar equipos.",
  "scope": "BUSINESS"
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "code": "{{qaCode}}",
  "name": "Usuario QA",
  "description": "Usuario encargado de diagnosticar y reparar equipos.",
  "scope": "BUSINESS"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| code | String | sí | @NotBlank @Size(max = 50) |
| name | String | sí | @NotBlank @Size(max = 100) |
| description | String | sí | @NotBlank @Size(max = 255) |
| scope | String | sí | @NotBlank @Size(max = 30) |

Casos Bruno: [EP164-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/ROLE/EP164-OK - Contrato válido.bru) · [EP164-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE/EP164-NO_TOKEN - Sin autenticación.bru) · [EP164-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE/EP164-INVALID_TOKEN - Token inválido.bru) · [EP164-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE/EP164-REVOKED - Token previamente revocado.bru) · [EP164-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/ROLE/EP164-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP164-code_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-code_MISSING - code- ausente.bru) · [EP164-code_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-code_NULL - code- null.bru) · [EP164-code_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-code_EMPTY - code- EMPTY.bru) · [EP164-code_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-code_BLANK - code- BLANK.bru) · [EP164-code_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-code_MAX_PLUS_1 - code- 51 caracteres.bru) · [EP164-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-name_MISSING - name- ausente.bru) · [EP164-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-name_NULL - name- null.bru) · [EP164-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-name_EMPTY - name- EMPTY.bru) · [EP164-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-name_BLANK - name- BLANK.bru) · [EP164-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-name_MAX_PLUS_1 - name- 101 caracteres.bru) · [EP164-description_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-description_MISSING - description- ausente.bru) · [EP164-description_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-description_NULL - description- null.bru) · [EP164-description_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-description_EMPTY - description- EMPTY.bru) · [EP164-description_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-description_BLANK - description- BLANK.bru) · [EP164-description_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-description_MAX_PLUS_1 - description- 256 caracteres.bru) · [EP164-scope_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-scope_MISSING - scope- ausente.bru) · [EP164-scope_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-scope_NULL - scope- null.bru) · [EP164-scope_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-scope_EMPTY - scope- EMPTY.bru) · [EP164-scope_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-scope_BLANK - scope- BLANK.bru) · [EP164-scope_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP164-scope_MAX_PLUS_1 - scope- 31 caracteres.bru) · [EP164-QA_ROLE_ALLOW](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/02_PREPARACION/ROLE/EP164-QA_ROLE_ALLOW - Crear rol QA ALLOW.bru) · [EP164-QA_ROLE_DENY](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/02_PREPARACION/ROLE/EP164-QA_ROLE_DENY - Crear rol QA DENY.bru)

## EP165

`PUT /api/v1/parameters/roles/{id}` — Update role

Fuente: [RoleParameterController:67](../../src/main/java/com/tecmarket/backend/parameter/controller/RoleParameterController.java#L67). Método Java: `update`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<RoleResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |
| request | RequestBody | UpdateRoleRequest | @Valid @RequestBody UpdateRoleRequest request |

Body completo (UpdateRoleRequest):

```json
{
  "name": "Usuario QA",
  "description": "Usuario encargado de supervisar diagnosticos y reparaciones.",
  "scope": "BUSINESS",
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "name": "Usuario QA",
  "description": "Usuario encargado de supervisar diagnosticos y reparaciones.",
  "scope": "BUSINESS"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| name | String | sí | @NotBlank @Size(max = 100) |
| description | String | sí | @NotBlank @Size(max = 255) |
| scope | String | sí | @NotBlank @Size(max = 30) |
| active | Boolean | no |  |

Casos Bruno: [EP165-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/ROLE/EP165-OK - Contrato válido.bru) · [EP165-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE/EP165-NO_TOKEN - Sin autenticación.bru) · [EP165-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE/EP165-INVALID_TOKEN - Token inválido.bru) · [EP165-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE/EP165-REVOKED - Token previamente revocado.bru) · [EP165-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/ROLE/EP165-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP165-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP165-name_MISSING - name- ausente.bru) · [EP165-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP165-name_NULL - name- null.bru) · [EP165-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP165-name_EMPTY - name- EMPTY.bru) · [EP165-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP165-name_BLANK - name- BLANK.bru) · [EP165-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP165-name_MAX_PLUS_1 - name- 101 caracteres.bru) · [EP165-description_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP165-description_MISSING - description- ausente.bru) · [EP165-description_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP165-description_NULL - description- null.bru) · [EP165-description_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP165-description_EMPTY - description- EMPTY.bru) · [EP165-description_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP165-description_BLANK - description- BLANK.bru) · [EP165-description_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP165-description_MAX_PLUS_1 - description- 256 caracteres.bru) · [EP165-scope_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP165-scope_MISSING - scope- ausente.bru) · [EP165-scope_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP165-scope_NULL - scope- null.bru) · [EP165-scope_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP165-scope_EMPTY - scope- EMPTY.bru) · [EP165-scope_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP165-scope_BLANK - scope- BLANK.bru) · [EP165-scope_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP165-scope_MAX_PLUS_1 - scope- 31 caracteres.bru) · [EP165-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE/EP165-active_TYPE - active- tipo incompatible.bru)

## EP166

`DELETE /api/v1/parameters/roles/{id}` — Disable role

Fuente: [RoleParameterController:78](../../src/main/java/com/tecmarket/backend/parameter/controller/RoleParameterController.java#L78). Método Java: `delete`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP166-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/ROLE/EP166-OK - Contrato válido.bru) · [EP166-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE/EP166-NO_TOKEN - Sin autenticación.bru) · [EP166-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE/EP166-INVALID_TOKEN - Token inválido.bru) · [EP166-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE/EP166-REVOKED - Token previamente revocado.bru) · [EP166-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/ROLE/EP166-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP167

`GET /api/v1/parameters/roles/{roleId}/permissions` — List permissions assigned to role

Fuente: [RolePermissionParameterController:40](../../src/main/java/com/tecmarket/backend/parameter/controller/RolePermissionParameterController.java#L40). Método Java: `findPermissionsByRoleId`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<RolePermissionOptionResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| roleId | PathVariable | Integer | @PathVariable Integer roleId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP167-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/ROLE PERMISSION/EP167-OK - Contrato válido.bru) · [EP167-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE PERMISSION/EP167-NO_TOKEN - Sin autenticación.bru) · [EP167-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE PERMISSION/EP167-INVALID_TOKEN - Token inválido.bru) · [EP167-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE PERMISSION/EP167-REVOKED - Token previamente revocado.bru) · [EP167-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/ROLE PERMISSION/EP167-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP168

`GET /api/v1/parameters/roles/{roleId}/permissions/assigned` — List only assigned permissions for role

Fuente: [RolePermissionParameterController:54](../../src/main/java/com/tecmarket/backend/parameter/controller/RolePermissionParameterController.java#L54). Método Java: `findAssignedPermissionsByRoleId`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<RoleAssignedPermissionResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| roleId | PathVariable | Integer | @PathVariable Integer roleId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP168-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/ROLE PERMISSION/EP168-OK - Contrato válido.bru) · [EP168-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE PERMISSION/EP168-NO_TOKEN - Sin autenticación.bru) · [EP168-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE PERMISSION/EP168-INVALID_TOKEN - Token inválido.bru) · [EP168-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE PERMISSION/EP168-REVOKED - Token previamente revocado.bru) · [EP168-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/ROLE PERMISSION/EP168-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP169

`PUT /api/v1/parameters/roles/{roleId}/permissions` — Replace role permissions

Fuente: [RolePermissionParameterController:68](../../src/main/java/com/tecmarket/backend/parameter/controller/RolePermissionParameterController.java#L68). Método Java: `updatePermissions`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<RolePermissionOptionResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| roleId | PathVariable | Integer | @PathVariable Integer roleId |
| request | RequestBody | UpdateRolePermissionsRequest | @Valid @RequestBody UpdateRolePermissionsRequest request |

Body completo (UpdateRolePermissionsRequest):

```json
{
  "permissionIds": [
    "{{permissionId}}"
  ]
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "permissionIds": [
    "{{permissionId}}"
  ]
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| permissionIds | List<Integer> | sí | @NotNull @Size(max = 200) @NotNull @Positive |

Casos Bruno: [EP169-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/ROLE PERMISSION/EP169-OK - Contrato válido.bru) · [EP169-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE PERMISSION/EP169-NO_TOKEN - Sin autenticación.bru) · [EP169-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE PERMISSION/EP169-INVALID_TOKEN - Token inválido.bru) · [EP169-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/ROLE PERMISSION/EP169-REVOKED - Token previamente revocado.bru) · [EP169-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/ROLE PERMISSION/EP169-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP169-permissionIds_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE PERMISSION/EP169-permissionIds_MISSING - permissionIds- ausente.bru) · [EP169-permissionIds_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE PERMISSION/EP169-permissionIds_NULL - permissionIds- null.bru) · [EP169-permissionIds_MAX_ARRAY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE PERMISSION/EP169-permissionIds_MAX_ARRAY - permissionIds- 201 elementos.bru) · [EP169-permissionIds_Positive](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/ROLE PERMISSION/EP169-permissionIds_Positive - permissionIds- fuera de límite Positive.bru) · [EP169-QA_ALLOW](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/02_PREPARACION/ROLE PERMISSION/EP169-QA_ALLOW - Asignar QUOTATION_VIEW al rol QA ALLOW.bru) · [EP169-QA_DENY](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/02_PREPARACION/ROLE PERMISSION/EP169-QA_DENY - Vaciar permisos del rol QA DENY.bru) · [EP169-REJECT_GLOBAL](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/ROLE PERMISSION/EP169-REJECT_GLOBAL - Rechazar permiso GLOBAL en rol BUSINESS.bru)

## EP170

`GET /api/v1/businesses/{businessId}/sales` — List sales

Fuente: [SaleController:45](../../src/main/java/com/tecmarket/backend/sale/controller/SaleController.java#L45). Método Java: `findAll`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('SALE_VIEW')`. Requiere `FEATURE_DIRECT_SALES_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<SaleResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP170-OK](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP170-OK - Contrato válido.bru) · [EP170-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SALE/EP170-NO_TOKEN - Sin autenticación.bru) · [EP170-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SALE/EP170-INVALID_TOKEN - Token inválido.bru) · [EP170-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SALE/EP170-REVOKED - Token previamente revocado.bru) · [EP170-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP170-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP170-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP170-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP170-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP170-DISABLED - Funcionalidad deshabilitada.bru) · [EP170-QUERY_0_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP170-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP170-QUERY_20_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP170-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP170-QUERY_0_Min](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP170-QUERY_0_Min - Query 0- límite inválido.bru) · [EP170-QUERY_20_Min](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP170-QUERY_20_Min - Query 20- límite inválido.bru) · [EP170-QUERY_20_Max](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP170-QUERY_20_Max - Query 20- límite inválido.bru) · [EP170-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP170-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP170-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP170-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP170-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP170-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP171

`GET /api/v1/businesses/{businessId}/sales/{saleId}` — Get sale detail

Fuente: [SaleController:58](../../src/main/java/com/tecmarket/backend/sale/controller/SaleController.java#L58). Método Java: `findById`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('SALE_VIEW')`. Requiere `FEATURE_DIRECT_SALES_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<SaleResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| saleId | PathVariable | Integer | @PathVariable Integer saleId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP171-OK](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP171-OK - Contrato válido.bru) · [EP171-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SALE/EP171-NO_TOKEN - Sin autenticación.bru) · [EP171-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SALE/EP171-INVALID_TOKEN - Token inválido.bru) · [EP171-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SALE/EP171-REVOKED - Token previamente revocado.bru) · [EP171-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP171-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP171-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP171-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP171-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP171-DISABLED - Funcionalidad deshabilitada.bru) · [EP171-NOT_FOUND](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP171-NOT_FOUND - ID inexistente comprobado.bru) · [EP171-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP171-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP171-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP171-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP171-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP171-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP172

`GET /api/v1/businesses/{businessId}/sales/{saleId}/items` — List sale items

Fuente: [SaleController:69](../../src/main/java/com/tecmarket/backend/sale/controller/SaleController.java#L69). Método Java: `findItems`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('SALE_VIEW')`. Requiere `FEATURE_DIRECT_SALES_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<SaleItemResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| saleId | PathVariable | Integer | @PathVariable Integer saleId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP172-OK](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP172-OK - Contrato válido.bru) · [EP172-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SALE/EP172-NO_TOKEN - Sin autenticación.bru) · [EP172-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SALE/EP172-INVALID_TOKEN - Token inválido.bru) · [EP172-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SALE/EP172-REVOKED - Token previamente revocado.bru) · [EP172-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP172-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP172-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP172-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP172-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP172-DISABLED - Funcionalidad deshabilitada.bru) · [EP172-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP172-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP172-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP172-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP172-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP172-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP173

`POST /api/v1/businesses/{businessId}/sales` — Create sale

Fuente: [SaleController:80](../../src/main/java/com/tecmarket/backend/sale/controller/SaleController.java#L80). Método Java: `create`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('SALE_CREATE')`. Requiere `FEATURE_DIRECT_SALES_ENABLED=true`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<SaleResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| request | RequestBody | CreateSaleRequest | @Valid @RequestBody CreateSaleRequest request |
| Idempotency-Key | RequestHeader | String | @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey |

Body completo (CreateSaleRequest):

```json
{
  "branchId": "{{branchId}}",
  "customerId": "{{customerId}}",
  "paymentMethodId": "{{paymentMethodId}}",
  "items": [
    {
      "inventoryItemId": "{{inventoryItemId}}",
      "quantity": 2
    }
  ]
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "branchId": "{{branchId}}",
  "paymentMethodId": "{{paymentMethodId}}",
  "items": [
    {
      "inventoryItemId": "{{inventoryItemId}}",
      "quantity": 2
    }
  ]
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| branchId | Integer | sí | @NotNull |
| customerId | Integer | no |  |
| paymentMethodId | Integer | sí | @NotNull |
| items | List<CreateSaleItemRequest> | sí | @NotEmpty @Size(max = 100) @Valid |

Casos Bruno: [EP173-OK](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP173-OK - Contrato válido.bru) · [EP173-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SALE/EP173-NO_TOKEN - Sin autenticación.bru) · [EP173-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SALE/EP173-INVALID_TOKEN - Token inválido.bru) · [EP173-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SALE/EP173-REVOKED - Token previamente revocado.bru) · [EP173-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP173-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP173-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP173-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP173-DISABLED](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP173-DISABLED - Funcionalidad deshabilitada.bru) · [EP173-branchId_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP173-branchId_MISSING - branchId- ausente.bru) · [EP173-branchId_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP173-branchId_NULL - branchId- null.bru) · [EP173-branchId_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP173-branchId_TYPE - branchId- tipo incompatible.bru) · [EP173-customerId_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP173-customerId_TYPE - customerId- tipo incompatible.bru) · [EP173-paymentMethodId_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP173-paymentMethodId_MISSING - paymentMethodId- ausente.bru) · [EP173-paymentMethodId_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP173-paymentMethodId_NULL - paymentMethodId- null.bru) · [EP173-paymentMethodId_TYPE](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP173-paymentMethodId_TYPE - paymentMethodId- tipo incompatible.bru) · [EP173-items_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP173-items_MISSING - items- ausente.bru) · [EP173-items_NULL](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP173-items_NULL - items- null.bru) · [EP173-items_EMPTY_ARRAY](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP173-items_EMPTY_ARRAY - items- colección vacía.bru) · [EP173-items_MAX_ARRAY](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP173-items_MAX_ARRAY - items- 101 elementos.bru) · [EP173-items_inventoryItemId_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP173-items_inventoryItemId_MISSING - items[0].inventoryItemId- ausente.bru) · [EP173-items_quantity_MISSING](../../bruno/TECMARKET-QA/10_FUNCIONES_OPCIONALES/SALE/EP173-items_quantity_MISSING - items[0].quantity- ausente.bru) · [EP173-IDEMPOTENT_FIRST](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/SALE/EP173-IDEMPOTENT_FIRST - Primera ejecución idempotente.bru) · [EP173-IDEMPOTENT_REPLAY](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/SALE/EP173-IDEMPOTENT_REPLAY - Reintento con mismo body y clave.bru) · [EP173-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP173-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP173-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP173-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP173-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SALE/EP173-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP174

`GET /api/v1/businesses/{businessId}/stock-movements` — List stock movements

Fuente: [StockMovementController:40](../../src/main/java/com/tecmarket/backend/inventory/controller/StockMovementController.java#L40). Método Java: `findAll`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('INVENTORY_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<StockMovementResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| inventoryItemId | RequestParam | Integer | @RequestParam(required = false) Integer inventoryItemId |
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP174-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/STOCK MOVEMENT/EP174-OK - Contrato válido.bru) · [EP174-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/STOCK MOVEMENT/EP174-NO_TOKEN - Sin autenticación.bru) · [EP174-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/STOCK MOVEMENT/EP174-INVALID_TOKEN - Token inválido.bru) · [EP174-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/STOCK MOVEMENT/EP174-REVOKED - Token previamente revocado.bru) · [EP174-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/STOCK MOVEMENT/EP174-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP174-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/STOCK MOVEMENT/EP174-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP174-QUERY_inventoryItemId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP174-QUERY_inventoryItemId_TYPE - Query inventoryItemId- tipo inválido.bru) · [EP174-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP174-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP174-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP174-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP174-FILTER_inventoryItemId](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP174-FILTER_inventoryItemId - Filtro inventoryItemId individual.bru) · [EP174-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP174-QUERY_0_Min - Query 0- límite inválido.bru) · [EP174-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP174-QUERY_20_Min - Query 20- límite inválido.bru) · [EP174-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP174-QUERY_20_Max - Query 20- límite inválido.bru) · [EP174-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/STOCK MOVEMENT/EP174-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP174-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/STOCK MOVEMENT/EP174-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP174-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/STOCK MOVEMENT/EP174-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP175

`POST /api/v1/businesses/{businessId}/stock-movements` — Create stock movement

Fuente: [StockMovementController:58](../../src/main/java/com/tecmarket/backend/inventory/controller/StockMovementController.java#L58). Método Java: `create`.

Acceso: negocio; consultar sede/propiedad en guía. Permiso del controlador: `hasAuthority('INVENTORY_UPDATE')`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<StockMovementResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| businessId | PathVariable | Integer | @PathVariable Integer businessId |
| request | RequestBody | CreateStockMovementRequest | @Valid @RequestBody CreateStockMovementRequest request |
| Idempotency-Key | RequestHeader | String | @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey |

Body completo (CreateStockMovementRequest):

```json
{
  "branchId": "{{branchId}}",
  "inventoryItemId": "{{inventoryItemId}}",
  "movementTypeId": "{{movementTypeId}}",
  "quantity": 3,
  "repairOrderId": "{{repairOrderId}}",
  "saleItemId": "{{saleItemId}}",
  "notes": "Ajuste por conteo físico."
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "branchId": "{{branchId}}",
  "inventoryItemId": "{{inventoryItemId}}",
  "movementTypeId": "{{movementTypeId}}",
  "quantity": 3
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| branchId | Integer | sí | @NotNull |
| inventoryItemId | Integer | sí | @NotNull |
| movementTypeId | Integer | sí | @NotNull |
| quantity | Integer | sí | @NotNull @Min(1) |
| repairOrderId | Integer | no |  |
| saleItemId | Integer | no |  |
| notes | String | no | @Size(max = 500) |

Casos Bruno: [EP175-OK](../../bruno/TECMARKET-QA/02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/STOCK MOVEMENT/EP175-OK - Contrato válido.bru) · [EP175-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/STOCK MOVEMENT/EP175-NO_TOKEN - Sin autenticación.bru) · [EP175-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/STOCK MOVEMENT/EP175-INVALID_TOKEN - Token inválido.bru) · [EP175-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/STOCK MOVEMENT/EP175-REVOKED - Token previamente revocado.bru) · [EP175-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/STOCK MOVEMENT/EP175-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP175-OTHER_TENANT](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/STOCK MOVEMENT/EP175-OTHER_TENANT - Usuario B intenta acceder al negocio A.bru) · [EP175-branchId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP175-branchId_MISSING - branchId- ausente.bru) · [EP175-branchId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP175-branchId_NULL - branchId- null.bru) · [EP175-branchId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP175-branchId_TYPE - branchId- tipo incompatible.bru) · [EP175-inventoryItemId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP175-inventoryItemId_MISSING - inventoryItemId- ausente.bru) · [EP175-inventoryItemId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP175-inventoryItemId_NULL - inventoryItemId- null.bru) · [EP175-inventoryItemId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP175-inventoryItemId_TYPE - inventoryItemId- tipo incompatible.bru) · [EP175-movementTypeId_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP175-movementTypeId_MISSING - movementTypeId- ausente.bru) · [EP175-movementTypeId_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP175-movementTypeId_NULL - movementTypeId- null.bru) · [EP175-movementTypeId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP175-movementTypeId_TYPE - movementTypeId- tipo incompatible.bru) · [EP175-quantity_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP175-quantity_MISSING - quantity- ausente.bru) · [EP175-quantity_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP175-quantity_NULL - quantity- null.bru) · [EP175-quantity_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP175-quantity_TYPE - quantity- tipo incompatible.bru) · [EP175-quantity_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP175-quantity_Min - quantity- fuera de límite Min.bru) · [EP175-repairOrderId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP175-repairOrderId_TYPE - repairOrderId- tipo incompatible.bru) · [EP175-saleItemId_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP175-saleItemId_TYPE - saleItemId- tipo incompatible.bru) · [EP175-notes_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/STOCK MOVEMENT/EP175-notes_MAX_PLUS_1 - notes- 501 caracteres.bru) · [EP175-IDEMPOTENT_FIRST](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/STOCK MOVEMENT/EP175-IDEMPOTENT_FIRST - Primera ejecución idempotente.bru) · [EP175-IDEMPOTENT_REPLAY](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/STOCK MOVEMENT/EP175-IDEMPOTENT_REPLAY - Reintento con mismo body y clave.bru) · [EP175-IDEMPOTENT_CONFLICT](../../bruno/TECMARKET-QA/08_ESTADOS_E_IDEMPOTENCIA/STOCK MOVEMENT/EP175-IDEMPOTENT_CONFLICT - Misma clave con body diferente.bru) · [EP175-SHARED_PERMISSION_SCOPE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/STOCK MOVEMENT/EP175-SHARED_PERMISSION_SCOPE - Permiso de A no autoriza operación en B.bru) · [EP175-INACTIVE_MEMBERSHIP](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/STOCK MOVEMENT/EP175-INACTIVE_MEMBERSHIP - Membresía inactiva no autoriza operación.bru) · [EP175-INACTIVE_ROLE](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/STOCK MOVEMENT/EP175-INACTIVE_ROLE - Rol empresarial inactivo no concede permisos.bru)

## EP176

`GET /api/v1/subscription-plans` — List subscription plans

Fuente: [SubscriptionPlanController:47](../../src/main/java/com/tecmarket/backend/subscription/controller/SubscriptionPlanController.java#L47). Método Java: `findAll`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('SUBSCRIPTION_VIEW')`. Requiere `FEATURE_SUBSCRIPTIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<SubscriptionPlanResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|


Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP176-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/SUBSCRIPTION PLAN/EP176-OK - Contrato válido.bru) · [EP176-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SUBSCRIPTION PLAN/EP176-NO_TOKEN - Sin autenticación.bru) · [EP176-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SUBSCRIPTION PLAN/EP176-INVALID_TOKEN - Token inválido.bru) · [EP176-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SUBSCRIPTION PLAN/EP176-REVOKED - Token previamente revocado.bru) · [EP176-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SUBSCRIPTION PLAN/EP176-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP176-DISABLED](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP176-DISABLED - Funcionalidad deshabilitada.bru)

## EP177

`GET /api/v1/subscription-plans/{id}` — Find subscription plan by ID

Fuente: [SubscriptionPlanController:56](../../src/main/java/com/tecmarket/backend/subscription/controller/SubscriptionPlanController.java#L56). Método Java: `findById`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('SUBSCRIPTION_VIEW')`. Requiere `FEATURE_SUBSCRIPTIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<SubscriptionPlanResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP177-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/SUBSCRIPTION PLAN/EP177-OK - Contrato válido.bru) · [EP177-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SUBSCRIPTION PLAN/EP177-NO_TOKEN - Sin autenticación.bru) · [EP177-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SUBSCRIPTION PLAN/EP177-INVALID_TOKEN - Token inválido.bru) · [EP177-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SUBSCRIPTION PLAN/EP177-REVOKED - Token previamente revocado.bru) · [EP177-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SUBSCRIPTION PLAN/EP177-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP177-DISABLED](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP177-DISABLED - Funcionalidad deshabilitada.bru) · [EP177-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP177-NOT_FOUND - ID inexistente comprobado.bru)

## EP178

`POST /api/v1/subscription-plans` — Create subscription plan

Fuente: [SubscriptionPlanController:67](../../src/main/java/com/tecmarket/backend/subscription/controller/SubscriptionPlanController.java#L67). Método Java: `create`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('SUBSCRIPTION_MANAGE')`. Requiere `FEATURE_SUBSCRIPTIONS_ENABLED=true`.

Éxito: **201**. Respuesta: `ResponseEntity<ApiResponse<SubscriptionPlanResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| request | RequestBody | CreateSubscriptionPlanRequest | @Valid @RequestBody CreateSubscriptionPlanRequest request |

Body completo (CreateSubscriptionPlanRequest):

```json
{
  "code": "{{qaCode}}",
  "name": "Usuario QA",
  "monthlyPrice": 99000,
  "yearlyPrice": 999000,
  "includedUsers": 4,
  "maxBusinesses": 1,
  "maxBranches": 2,
  "features": "[\"QA\"]",
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "code": "{{qaCode}}",
  "name": "Usuario QA",
  "monthlyPrice": 99000,
  "yearlyPrice": 999000,
  "includedUsers": 4,
  "maxBusinesses": 1,
  "maxBranches": 2,
  "features": "[\"QA\"]"
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| code | String | sí | @NotBlank @Size(max = 50) |
| name | String | sí | @NotBlank @Size(max = 120) |
| monthlyPrice | BigDecimal | sí | @NotNull @DecimalMin(value = "0.00") |
| yearlyPrice | BigDecimal | sí | @NotNull @DecimalMin(value = "0.00") |
| includedUsers | Integer | sí | @NotNull @Min(1) |
| maxBusinesses | Integer | sí | @NotNull @Min(1) |
| maxBranches | Integer | sí | @NotNull @Min(1) |
| features | String | sí | @NotBlank |
| active | Boolean | no |  |

Casos Bruno: [EP178-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/SUBSCRIPTION PLAN/EP178-OK - Contrato válido.bru) · [EP178-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SUBSCRIPTION PLAN/EP178-NO_TOKEN - Sin autenticación.bru) · [EP178-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SUBSCRIPTION PLAN/EP178-INVALID_TOKEN - Token inválido.bru) · [EP178-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SUBSCRIPTION PLAN/EP178-REVOKED - Token previamente revocado.bru) · [EP178-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SUBSCRIPTION PLAN/EP178-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP178-DISABLED](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-DISABLED - Funcionalidad deshabilitada.bru) · [EP178-code_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-code_MISSING - code- ausente.bru) · [EP178-code_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-code_NULL - code- null.bru) · [EP178-code_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-code_EMPTY - code- EMPTY.bru) · [EP178-code_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-code_BLANK - code- BLANK.bru) · [EP178-code_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-code_MAX_PLUS_1 - code- 51 caracteres.bru) · [EP178-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-name_MISSING - name- ausente.bru) · [EP178-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-name_NULL - name- null.bru) · [EP178-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-name_EMPTY - name- EMPTY.bru) · [EP178-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-name_BLANK - name- BLANK.bru) · [EP178-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-name_MAX_PLUS_1 - name- 121 caracteres.bru) · [EP178-monthlyPrice_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-monthlyPrice_MISSING - monthlyPrice- ausente.bru) · [EP178-monthlyPrice_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-monthlyPrice_NULL - monthlyPrice- null.bru) · [EP178-monthlyPrice_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-monthlyPrice_TYPE - monthlyPrice- tipo incompatible.bru) · [EP178-monthlyPrice_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-monthlyPrice_DecimalMin - monthlyPrice- fuera de límite DecimalMin.bru) · [EP178-yearlyPrice_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-yearlyPrice_MISSING - yearlyPrice- ausente.bru) · [EP178-yearlyPrice_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-yearlyPrice_NULL - yearlyPrice- null.bru) · [EP178-yearlyPrice_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-yearlyPrice_TYPE - yearlyPrice- tipo incompatible.bru) · [EP178-yearlyPrice_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-yearlyPrice_DecimalMin - yearlyPrice- fuera de límite DecimalMin.bru) · [EP178-includedUsers_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-includedUsers_MISSING - includedUsers- ausente.bru) · [EP178-includedUsers_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-includedUsers_NULL - includedUsers- null.bru) · [EP178-includedUsers_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-includedUsers_TYPE - includedUsers- tipo incompatible.bru) · [EP178-includedUsers_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-includedUsers_Min - includedUsers- fuera de límite Min.bru) · [EP178-maxBusinesses_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-maxBusinesses_MISSING - maxBusinesses- ausente.bru) · [EP178-maxBusinesses_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-maxBusinesses_NULL - maxBusinesses- null.bru) · [EP178-maxBusinesses_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-maxBusinesses_TYPE - maxBusinesses- tipo incompatible.bru) · [EP178-maxBusinesses_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-maxBusinesses_Min - maxBusinesses- fuera de límite Min.bru) · [EP178-maxBranches_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-maxBranches_MISSING - maxBranches- ausente.bru) · [EP178-maxBranches_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-maxBranches_NULL - maxBranches- null.bru) · [EP178-maxBranches_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-maxBranches_TYPE - maxBranches- tipo incompatible.bru) · [EP178-maxBranches_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-maxBranches_Min - maxBranches- fuera de límite Min.bru) · [EP178-features_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-features_MISSING - features- ausente.bru) · [EP178-features_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-features_NULL - features- null.bru) · [EP178-features_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-features_EMPTY - features- EMPTY.bru) · [EP178-features_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-features_BLANK - features- BLANK.bru) · [EP178-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP178-active_TYPE - active- tipo incompatible.bru)

## EP179

`PUT /api/v1/subscription-plans/{id}` — Update subscription plan

Fuente: [SubscriptionPlanController:79](../../src/main/java/com/tecmarket/backend/subscription/controller/SubscriptionPlanController.java#L79). Método Java: `update`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('SUBSCRIPTION_MANAGE')`. Requiere `FEATURE_SUBSCRIPTIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<SubscriptionPlanResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |
| request | RequestBody | UpdateSubscriptionPlanRequest | @Valid @RequestBody UpdateSubscriptionPlanRequest request |

Body completo (UpdateSubscriptionPlanRequest):

```json
{
  "code": "{{qaCode}}",
  "name": "Usuario QA",
  "monthlyPrice": 99000,
  "yearlyPrice": 999000,
  "includedUsers": 4,
  "maxBusinesses": 1,
  "maxBranches": 2,
  "features": "[\"QA\"]",
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "code": "{{qaCode}}",
  "name": "Usuario QA",
  "monthlyPrice": 99000,
  "yearlyPrice": 999000,
  "includedUsers": 4,
  "maxBusinesses": 1,
  "maxBranches": 2,
  "features": "[\"QA\"]",
  "active": true
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| code | String | sí | @NotBlank @Size(max = 50) |
| name | String | sí | @NotBlank @Size(max = 120) |
| monthlyPrice | BigDecimal | sí | @NotNull @DecimalMin(value = "0.00") |
| yearlyPrice | BigDecimal | sí | @NotNull @DecimalMin(value = "0.00") |
| includedUsers | Integer | sí | @NotNull @Min(1) |
| maxBusinesses | Integer | sí | @NotNull @Min(1) |
| maxBranches | Integer | sí | @NotNull @Min(1) |
| features | String | sí | @NotBlank |
| active | Boolean | sí | @NotNull |

Casos Bruno: [EP179-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/SUBSCRIPTION PLAN/EP179-OK - Contrato válido.bru) · [EP179-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SUBSCRIPTION PLAN/EP179-NO_TOKEN - Sin autenticación.bru) · [EP179-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SUBSCRIPTION PLAN/EP179-INVALID_TOKEN - Token inválido.bru) · [EP179-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SUBSCRIPTION PLAN/EP179-REVOKED - Token previamente revocado.bru) · [EP179-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SUBSCRIPTION PLAN/EP179-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP179-DISABLED](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-DISABLED - Funcionalidad deshabilitada.bru) · [EP179-code_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-code_MISSING - code- ausente.bru) · [EP179-code_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-code_NULL - code- null.bru) · [EP179-code_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-code_EMPTY - code- EMPTY.bru) · [EP179-code_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-code_BLANK - code- BLANK.bru) · [EP179-code_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-code_MAX_PLUS_1 - code- 51 caracteres.bru) · [EP179-name_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-name_MISSING - name- ausente.bru) · [EP179-name_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-name_NULL - name- null.bru) · [EP179-name_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-name_EMPTY - name- EMPTY.bru) · [EP179-name_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-name_BLANK - name- BLANK.bru) · [EP179-name_MAX_PLUS_1](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-name_MAX_PLUS_1 - name- 121 caracteres.bru) · [EP179-monthlyPrice_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-monthlyPrice_MISSING - monthlyPrice- ausente.bru) · [EP179-monthlyPrice_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-monthlyPrice_NULL - monthlyPrice- null.bru) · [EP179-monthlyPrice_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-monthlyPrice_TYPE - monthlyPrice- tipo incompatible.bru) · [EP179-monthlyPrice_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-monthlyPrice_DecimalMin - monthlyPrice- fuera de límite DecimalMin.bru) · [EP179-yearlyPrice_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-yearlyPrice_MISSING - yearlyPrice- ausente.bru) · [EP179-yearlyPrice_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-yearlyPrice_NULL - yearlyPrice- null.bru) · [EP179-yearlyPrice_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-yearlyPrice_TYPE - yearlyPrice- tipo incompatible.bru) · [EP179-yearlyPrice_DecimalMin](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-yearlyPrice_DecimalMin - yearlyPrice- fuera de límite DecimalMin.bru) · [EP179-includedUsers_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-includedUsers_MISSING - includedUsers- ausente.bru) · [EP179-includedUsers_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-includedUsers_NULL - includedUsers- null.bru) · [EP179-includedUsers_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-includedUsers_TYPE - includedUsers- tipo incompatible.bru) · [EP179-includedUsers_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-includedUsers_Min - includedUsers- fuera de límite Min.bru) · [EP179-maxBusinesses_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-maxBusinesses_MISSING - maxBusinesses- ausente.bru) · [EP179-maxBusinesses_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-maxBusinesses_NULL - maxBusinesses- null.bru) · [EP179-maxBusinesses_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-maxBusinesses_TYPE - maxBusinesses- tipo incompatible.bru) · [EP179-maxBusinesses_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-maxBusinesses_Min - maxBusinesses- fuera de límite Min.bru) · [EP179-maxBranches_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-maxBranches_MISSING - maxBranches- ausente.bru) · [EP179-maxBranches_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-maxBranches_NULL - maxBranches- null.bru) · [EP179-maxBranches_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-maxBranches_TYPE - maxBranches- tipo incompatible.bru) · [EP179-maxBranches_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-maxBranches_Min - maxBranches- fuera de límite Min.bru) · [EP179-features_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-features_MISSING - features- ausente.bru) · [EP179-features_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-features_NULL - features- null.bru) · [EP179-features_EMPTY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-features_EMPTY - features- EMPTY.bru) · [EP179-features_BLANK](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-features_BLANK - features- BLANK.bru) · [EP179-active_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-active_MISSING - active- ausente.bru) · [EP179-active_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-active_NULL - active- null.bru) · [EP179-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP179-active_TYPE - active- tipo incompatible.bru)

## EP180

`DELETE /api/v1/subscription-plans/{id}` — Disable subscription plan

Fuente: [SubscriptionPlanController:91](../../src/main/java/com/tecmarket/backend/subscription/controller/SubscriptionPlanController.java#L91). Método Java: `delete`.

Acceso: usuario autenticado; ver reglas de guía. Permiso del controlador: `hasAuthority('SUBSCRIPTION_MANAGE')`. Requiere `FEATURE_SUBSCRIPTIONS_ENABLED=true`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP180-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/SUBSCRIPTION PLAN/EP180-OK - Contrato válido.bru) · [EP180-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SUBSCRIPTION PLAN/EP180-NO_TOKEN - Sin autenticación.bru) · [EP180-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SUBSCRIPTION PLAN/EP180-INVALID_TOKEN - Token inválido.bru) · [EP180-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/SUBSCRIPTION PLAN/EP180-REVOKED - Token previamente revocado.bru) · [EP180-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/SUBSCRIPTION PLAN/EP180-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP180-DISABLED](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/SUBSCRIPTION PLAN/EP180-DISABLED - Funcionalidad deshabilitada.bru)

## EP181

`GET /api/v1/parameters/users` — List users

Fuente: [UserParameterController:46](../../src/main/java/com/tecmarket/backend/parameter/controller/UserParameterController.java#L46). Método Java: `findAll`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<PageResponse<UserParameterResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| 0 | RequestParam | Integer | @RequestParam(defaultValue = "0") @Min(0) Integer page |
| 20 | RequestParam | Integer | @RequestParam(defaultValue = "20") @Min(1) @Max(100) Integer size |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP181-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/USER/EP181-OK - Contrato válido.bru) · [EP181-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER/EP181-NO_TOKEN - Sin autenticación.bru) · [EP181-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER/EP181-INVALID_TOKEN - Token inválido.bru) · [EP181-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER/EP181-REVOKED - Token previamente revocado.bru) · [EP181-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/USER/EP181-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP181-QUERY_0_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/USER/EP181-QUERY_0_TYPE - Query 0- tipo inválido.bru) · [EP181-QUERY_20_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/USER/EP181-QUERY_20_TYPE - Query 20- tipo inválido.bru) · [EP181-QUERY_0_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/USER/EP181-QUERY_0_Min - Query 0- límite inválido.bru) · [EP181-QUERY_20_Min](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/USER/EP181-QUERY_20_Min - Query 20- límite inválido.bru) · [EP181-QUERY_20_Max](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/USER/EP181-QUERY_20_Max - Query 20- límite inválido.bru)

## EP182

`GET /api/v1/parameters/users/{id}` — Find user by ID

Fuente: [UserParameterController:59](../../src/main/java/com/tecmarket/backend/parameter/controller/UserParameterController.java#L59). Método Java: `findById`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<UserParameterResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP182-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/USER/EP182-OK - Contrato válido.bru) · [EP182-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER/EP182-NO_TOKEN - Sin autenticación.bru) · [EP182-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER/EP182-INVALID_TOKEN - Token inválido.bru) · [EP182-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER/EP182-REVOKED - Token previamente revocado.bru) · [EP182-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/USER/EP182-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP182-NOT_FOUND](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/USER/EP182-NOT_FOUND - ID inexistente comprobado.bru)

## EP183

`PATCH /api/v1/parameters/users/{id}/status` — Update user status

Fuente: [UserParameterController:70](../../src/main/java/com/tecmarket/backend/parameter/controller/UserParameterController.java#L70). Método Java: `updateStatus`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<UserParameterResponse>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |
| request | RequestBody | UpdateUserStatusRequest | @Valid @RequestBody UpdateUserStatusRequest request |

Body completo (UpdateUserStatusRequest):

```json
{
  "active": true
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "active": true
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| active | Boolean | sí | @NotNull |

Casos Bruno: [EP183-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/USER/EP183-OK - Contrato válido.bru) · [EP183-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER/EP183-NO_TOKEN - Sin autenticación.bru) · [EP183-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER/EP183-INVALID_TOKEN - Token inválido.bru) · [EP183-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER/EP183-REVOKED - Token previamente revocado.bru) · [EP183-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/USER/EP183-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP183-active_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/USER/EP183-active_MISSING - active- ausente.bru) · [EP183-active_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/USER/EP183-active_NULL - active- null.bru) · [EP183-active_TYPE](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/USER/EP183-active_TYPE - active- tipo incompatible.bru) · [EP183-DEACTIVATE_QA](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER/EP183-DEACTIVATE_QA - Desactivar usuario QA con sesión abierta.bru)

## EP184

`DELETE /api/v1/parameters/users/{id}/sessions` — Revoke all user sessions

Fuente: [UserParameterController:82](../../src/main/java/com/tecmarket/backend/parameter/controller/UserParameterController.java#L82). Método Java: `revokeAllSessions`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<Void>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| id | PathVariable | Integer | @PathVariable Integer id |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP184-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/USER/EP184-OK - Contrato válido.bru) · [EP184-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER/EP184-NO_TOKEN - Sin autenticación.bru) · [EP184-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER/EP184-INVALID_TOKEN - Token inválido.bru) · [EP184-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER/EP184-REVOKED - Token previamente revocado.bru) · [EP184-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/USER/EP184-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP185

`GET /api/v1/parameters/users/{userId}/roles` — List roles assigned to user

Fuente: [UserRoleParameterController:38](../../src/main/java/com/tecmarket/backend/parameter/controller/UserRoleParameterController.java#L38). Método Java: `findRolesByUserId`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_VIEW')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<UserRoleOptionResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| userId | PathVariable | Integer | @PathVariable Integer userId |

Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.

Casos Bruno: [EP185-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/USER ROLE/EP185-OK - Contrato válido.bru) · [EP185-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER ROLE/EP185-NO_TOKEN - Sin autenticación.bru) · [EP185-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER ROLE/EP185-INVALID_TOKEN - Token inválido.bru) · [EP185-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER ROLE/EP185-REVOKED - Token previamente revocado.bru) · [EP185-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/USER ROLE/EP185-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru)

## EP186

`PUT /api/v1/parameters/users/{userId}/roles` — Replace user roles

Fuente: [UserRoleParameterController:51](../../src/main/java/com/tecmarket/backend/parameter/controller/UserRoleParameterController.java#L51). Método Java: `updateRoles`.

Acceso: catálogo global / plataforma según permiso. Permiso del controlador: `hasAuthority('PARAMETER_MANAGE')`.

Éxito: **200**. Respuesta: `ResponseEntity<ApiResponse<List<UserRoleOptionResponse>>>`. Ver definiciones en [contrato.json](contrato.json).

| Parámetro | Ubicación | Tipo | Declaración / restricciones |
|---|---|---|---|
| userId | PathVariable | Integer | @PathVariable Integer userId |
| request | RequestBody | UpdateUserRolesRequest | @Valid @RequestBody UpdateUserRolesRequest request |

Body completo (UpdateUserRolesRequest):

```json
{
  "roleIds": [
    "{{roleId}}"
  ]
}
```

Body mínimo por Bean Validation (también exige relaciones/estados válidos):

```json
{
  "roleIds": [
    "{{roleId}}"
  ]
}
```

Campos y validaciones:

| Campo | Tipo | Obligatorio | Restricciones |
|---|---|---|---|
| roleIds | List<Integer> | sí | @NotNull @Size(max = 20) @NotNull @Positive |

Casos Bruno: [EP186-OK](../../bruno/TECMARKET-QA/01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/USER ROLE/EP186-OK - Contrato válido.bru) · [EP186-NO_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER ROLE/EP186-NO_TOKEN - Sin autenticación.bru) · [EP186-INVALID_TOKEN](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER ROLE/EP186-INVALID_TOKEN - Token inválido.bru) · [EP186-REVOKED](../../bruno/TECMARKET-QA/05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/USER ROLE/EP186-REVOKED - Token previamente revocado.bru) · [EP186-NO_PERMISSION](../../bruno/TECMARKET-QA/04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/USER ROLE/EP186-NO_PERMISSION - Actor válido sin permiso en recurso existente.bru) · [EP186-roleIds_MISSING](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/USER ROLE/EP186-roleIds_MISSING - roleIds- ausente.bru) · [EP186-roleIds_NULL](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/USER ROLE/EP186-roleIds_NULL - roleIds- null.bru) · [EP186-roleIds_MAX_ARRAY](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/USER ROLE/EP186-roleIds_MAX_ARRAY - roleIds- 21 elementos.bru) · [EP186-roleIds_Positive](../../bruno/TECMARKET-QA/07_VALIDACIONES_Y_LIMITES/USER ROLE/EP186-roleIds_Positive - roleIds- fuera de límite Positive.bru)
