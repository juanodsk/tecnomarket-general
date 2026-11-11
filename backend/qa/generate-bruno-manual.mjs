// Rebuild the human-facing Bruno collection from the generated API contract.
// Requests intentionally contain literal values: no {{variables}}, environments,
// pre-request scripts, post-response scripts, or hidden request mutation.
import fs from 'node:fs';
import path from 'node:path';
import assert from 'node:assert/strict';
import { execFileSync } from 'node:child_process';
import { createRequire } from 'node:module';

const root = path.resolve(import.meta.dirname, '..');
const output = path.join(root, 'bruno/TECMARKET-QA');
const build = path.join(root, 'target/TECMARKET-QA-manual-build');
const generated = path.join(root, 'target/qa-generated-source');
const docsDir = path.join(root, 'docs/qa');
const require = createRequire(import.meta.url);
const { bruToJsonV2, collectionBruToJson } = require(path.join(root, 'target/qa-tooling/node_modules/@usebruno/lang'));

execFileSync(process.execPath, [path.join(root, 'qa/generate-api-qa.mjs')], { cwd: root, stdio: 'inherit' });

const contract = JSON.parse(fs.readFileSync(path.join(docsDir, 'contrato.json'), 'utf8'));
const sourceCases = JSON.parse(fs.readFileSync(path.join(docsDir, 'casos.json'), 'utf8'));
const endpointById = new Map(contract.operations.map(endpoint => [endpoint.id, endpoint]));
const write = (file, contents) => {
  fs.mkdirSync(path.dirname(file), { recursive: true });
  fs.writeFileSync(file, contents.endsWith('\n') ? contents : contents + '\n');
};
const cleanName = value => String(value).replace(/[<>:"/\\|?*]/g, '-').replace(/\s+/g, ' ').trim();
const moduleName = controller => controller.replace(/ParameterController$/, '').replace(/Controller$/, '').replace(/([a-z])([A-Z])/g, '$1 $2').toUpperCase();
const caseKind = testCase => testCase.caseId.slice(testCase.endpoint.length + 1);
const isNegativeAuth = kind => ['NO_TOKEN', 'INVALID_TOKEN', 'REVOKED'].includes(kind);
const isSecurity = kind => /NO_PERMISSION|OTHER_TENANT|SHARED|OTHER_BRANCH|OTHER_CUSTOMER|FOREIGN|INACTIVE|TECH_|JUMP_|REJECT_GLOBAL/.test(kind);
const isIntegrity = kind => /IDEMPOTENT|DUPLICATE|STATE_|NOT_DELIVERED/.test(kind);
const isValidation = kind => !['OK'].includes(kind) && !isNegativeAuth(kind) && !isSecurity(kind) && !isIntegrity(kind) && !/LOGIN_|REGISTER_|VERIFY_|REQUEST_|APPROVE_|RESOLVE_|BRANCH_|MEMBER_|PILOT|FLOW_|ROTATE|REPLAY|AFTER_|RATE_LIMIT|INVALID_AFTER|CAPTURE_|QA_/.test(kind);
const globalControllers = new Set([
  'AppModuleParameterController', 'CityParameterController', 'CountryParameterController',
  'DepartmentParameterController', 'DocumentTypeParameterController', 'PermissionParameterController',
  'RoleParameterController', 'RolePermissionParameterController', 'UserParameterController',
  'UserRoleParameterController', 'SubscriptionPlanController'
]);
const optionalControllers = new Set(['BusinessSubscriptionController', 'CommissionSettlementController', 'RepairOrderCommissionController', 'SaleController']);

const numericIds = {
  A: {
    businessId: 100001, businessAId: 100001, businessRequestId: 100002, requestAId: 100002,
    branchId: 100101, branchA1Id: 100101, branchA2Id: 100102,
    customerId: 100201, customerAUserId: 100201, customerDeviceId: 100202,
    businessUserId: 100301, receivedByBusinessUserId: 100301,
    assignedBusinessUserId: 100302, techA1BusinessUserId: 100302,
    repairOrderId: 100401, orderAId: 100401, inventoryItemId: 100501,
    quotationId: 100601, businessServiceId: 100701, serviceId: 100701
  },
  B: {
    businessId: 200001, businessBId: 200001, businessRequestId: 200002, requestBId: 200002,
    branchId: 200101, branchB1Id: 200101, branchB2Id: 200102,
    customerId: 200201, customerBUserId: 200201, customerDeviceId: 200202,
    businessUserId: 200301, receivedByBusinessUserId: 200301,
    assignedBusinessUserId: 200302, repairOrderId: 200401, orderBId: 200401,
    inventoryItemId: 200501, quotationId: 200601, businessServiceId: 200701,
    serviceId: 200701
  },
  G: { countryId: 1, departmentId: 1, cityId: 1, documentTypeId: 1, id: 900001 }
};

function hash(value) {
  let result = 0;
  for (const character of String(value)) result = (result * 31 + character.charCodeAt(0)) % 800;
  return result;
}

function numericLiteral(name, context) {
  if (name === 'countryId' || name === 'departmentId' || name === 'cityId' || name === 'documentTypeId') return numericIds.G[name];
  const table = numericIds[context] || numericIds.G;
  if (table[name] !== undefined) return table[name];
  if (name === 'missingId') return 99999999;
  return (context === 'A' ? 100000 : context === 'B' ? 200000 : 900000) + hash(name);
}

function literal(name, context, key = '') {
  const n = String(name);
  if (n === 'baseUrl') return 'https://api-dev.juanodsk.dev';
  if (n === 'origin') return 'https://frontend-dev.juanodsk.dev';
  if (n === 'runId') return context === 'B' ? 'NEGOCIO-B-001' : context === 'A' ? 'NEGOCIO-A-001' : 'GLOBAL-001';
  if (/verificationToken/i.test(n)) return `PEGAR_TOKEN_VERIFICACION_${context}`;
  if (/resetToken/i.test(n)) return `PEGAR_TOKEN_RECUPERACION_${context}`;
  if (/refreshToken/i.test(n)) return `PEGAR_COOKIE_REFRESH_${context}`;
  if (/^provider(Customer|Subscription)Id$/i.test(n)) return null;
  if (/Token$/.test(n)) return `PEGAR_TOKEN_${n.replace(/Token$/, '').toUpperCase()}`;
  if (/platformAdminPassword/i.test(n)) return 'EDITAR_CLAVE_ADMINISTRADOR_GLOBAL';
  if (/password/i.test(n)) return context === 'B' ? 'QA-TecMarket-B-2026!' : 'QA-TecMarket-A-2026!';
  if (/email/i.test(n)) {
    if (/platformAdminEmail/i.test(n)) return 'EDITAR_CORREO_ADMINISTRADOR_GLOBAL';
    if (/customerB/i.test(n)) return 'juanodiaz1414+cliente.qa@gmail.com';
    if (/customerA/i.test(n)) return 'juanodiaz1410+cliente.qa@gmail.com';
    if (/ownerB/i.test(n)) return 'juanodiaz1414@gmail.com';
    if (/ownerA/i.test(n)) return 'juanodiaz1410@gmail.com';
    if (/tech/i.test(n)) return `juanodiaz1410+tecnico.${context.toLowerCase()}.qa@gmail.com`;
    if (/admin/i.test(n)) return `juanodiaz1410+admin.${context.toLowerCase()}.qa@gmail.com`;
    if (context === 'B') return 'juanodiaz1414+actor.qa@gmail.com';
    return 'juanodiaz1410@gmail.com';
  }
  if (/documentNumber/i.test(n)) {
    if (/customerB/i.test(n)) return '55157802';
    if (/customerA/i.test(n)) return '55157801';
    if (/ownerB/i.test(n)) return '55157702';
    if (/ownerA/i.test(n)) return '55157701';
    return String((context === 'B' ? 55200000 : 55100000) + hash(n));
  }
  if (/legalFileUrl/i.test(n)) return `https://archivos-qa.invalid/documento-negocio-${context}.pdf`;
  if (/imagePath|filePath/i.test(n)) return 'C:/CAMBIAR/RUTA/imagen-prueba.png';
  if (/trialEndsAt/i.test(n)) return '2030-01-15T23:59:59';
  if (/dateTime|startsAt|endsAt|scheduledAt|registeredAt|receivedAt|estimatedDeliveryAt|expiresAt|receivedFrom|receivedTo|currentPeriodStart|currentPeriodEnd/i.test(n)) return /End$/i.test(n) ? '2030-01-31T23:59:59' : '2030-01-01T00:00:00';
  if (/date|periodStart|periodEnd/i.test(n)) return /End$/i.test(n) ? '2030-01-31' : '2030-01-01';
  if (/time/i.test(n)) return '10:00:00';
  if (/idempotency/i.test(n)) return context === 'B' ? '22222222-2222-4222-8222-222222222222' : '11111111-1111-4111-8111-111111111111';
  if (/^(q|customer|orderNumber|ticketCode)$/i.test(n)) return context === 'B' ? 'BUSQUEDA-NEGOCIO-B' : 'BUSQUEDA-NEGOCIO-A';
  if (/catalogType/i.test(n)) return 'DEVICE_TYPE';
  if (/orderSequence/i.test(n)) return 1;
  if (/minRating/i.test(n)) return 4;
  if (/^status$/i.test(n)) return 'ACTIVE';
  if (/code|sku/i.test(n)) return `QA-${context}-001`;
  if (/Id$|^id$|Ids$/.test(n) || /Id$/.test(key)) return numericLiteral(n, context);
  return `EDITAR_${n.toUpperCase()}_${context}`;
}

function replaceString(value, context, key) {
  const exact = value.match(/^\{\{(\w+)\}\}$/);
  if (exact) return literal(exact[1], context, key);
  return value.replace(/\{\{(\w+)\}\}/g, (_, name) => String(literal(name, context, key)));
}

function literalize(value, context, key = '') {
  if (Array.isArray(value)) return value.map(item => literalize(item, context, key.replace(/Ids$/, 'Id')));
  if (value && typeof value === 'object') return Object.fromEntries(Object.entries(value).map(([childKey, childValue]) => [childKey, literalize(childValue, context, childKey)]));
  if (typeof value !== 'string') return value;
  const result = replaceString(value, context, key);
  if (typeof result === 'number') return result;
  if (/Id$/.test(key) && /^\d+$/.test(result)) return Number(result);
  return result;
}

function contextFor(testCase, forced) {
  if (forced) return forced;
  if (/ownerB|customerB|businessB|branchB|_B\b/.test(testCase.actor + ' ' + testCase.caseId + ' ' + testCase.scenario)) return 'B';
  if (/ownerA|customerA|techA|adminA|sellerA|advisorA|_A\b|PILOT|FLOW_/.test(testCase.actor + ' ' + testCase.caseId + ' ' + testCase.scenario)) return 'A';
  return 'G';
}

function tokenFor(actor, context) {
  const known = {
    platformAdmin: 'PEGAR_TOKEN_ADMINISTRADOR_GLOBAL', ownerA: 'PEGAR_TOKEN_PROPIETARIO_A',
    ownerB: 'PEGAR_TOKEN_PROPIETARIO_B', customerA: 'PEGAR_TOKEN_CLIENTE_A',
    customerB: 'PEGAR_TOKEN_CLIENTE_B', techA1: 'PEGAR_TOKEN_TECNICO_A1',
    shared: 'PEGAR_TOKEN_USUARIO_COMPARTIDO', noPermission: 'PEGAR_TOKEN_SIN_PERMISO',
    actor: context === 'B' ? 'PEGAR_TOKEN_PROPIETARIO_B' : 'PEGAR_TOKEN_PROPIETARIO_A'
  };
  return known[actor] || `PEGAR_TOKEN_${String(actor).toUpperCase()}`;
}

function readSource(testCase) {
  return bruToJsonV2(fs.readFileSync(path.join(generated, testCase.request), 'utf8'));
}

function requestAuth(testCase, endpoint, context, section) {
  const kind = caseKind(testCase);
  if (endpoint.public || kind === 'NO_TOKEN') return { mode: 'none' };
  if (kind === 'INVALID_TOKEN') return { mode: 'bearer', token: 'TOKEN_INVALIDO_PARA_COMPROBAR_401' };
  const inherited = (section === 'A' && ['ownerA', 'actor'].includes(testCase.actor)) ||
    (section === 'B' && ['ownerB', 'actor'].includes(testCase.actor)) ||
    (section === 'ADMIN' && testCase.actor === 'platformAdmin');
  return inherited ? { mode: 'inherit' } : { mode: 'bearer', token: tokenFor(testCase.actor, context) };
}

function renderRequest(testCase, options = {}) {
  const endpoint = endpointById.get(testCase.endpoint);
  const parsed = readSource(testCase);
  const context = contextFor(testCase, options.context);
  const section = options.section || 'OTHER';
  const auth = requestAuth(testCase, endpoint, context, section);
  const url = replaceString(parsed.http.url, context, 'url');
  const headers = (parsed.headers || [])
    .filter(header => header.enabled !== false && header.name.toLowerCase() !== 'authorization')
    .map(header => ({ ...header, value: replaceString(header.value, context, header.name) }));
  if (headers.some(header => header.name.toLowerCase() === 'cookie')) {
    for (const header of headers) if (header.name.toLowerCase() === 'cookie') header.value = `tm_refresh=PEGAR_COOKIE_REFRESH_${context}`;
  }
  let body = null;
  if (parsed.body?.json) body = literalize(JSON.parse(parsed.body.json), context);
  if (body !== null && options.bodyPatch) body = { ...body, ...options.bodyPatch };
  const multipart = (parsed.body?.multipartForm || []).map(part => ({
    ...part,
    value: part.type === 'file' || String(part.value).includes('@file')
      ? 'C:/CAMBIAR/RUTA/imagen-prueba.png'
      : replaceString(String(part.value), context, part.name)
  }));
  const label = options.label || `${testCase.caseId} - ${testCase.scenario} - espera HTTP ${testCase.expectedHttp}`;
  const bodyMode = body !== null ? 'json' : multipart.length ? 'multipartForm' : 'none';
  let text = `meta {\n  name: ${cleanName(label)}\n  type: http\n  seq: ${options.seq || 1}\n}\n\n${testCase.method.toLowerCase()} {\n  url: ${url}\n  body: ${bodyMode}\n  auth: ${auth.mode}\n}\n\n`;
  if (auth.mode === 'bearer') text += `auth:bearer {\n  token: ${auth.token}\n}\n\n`;
  if (headers.length) text += `headers {\n${headers.map(header => `  ${header.name}: ${header.value}`).join('\n')}\n}\n\n`;
  if (body !== null) text += `body:json {\n${JSON.stringify(body, null, 2).split('\n').map(line => '  ' + line).join('\n')}\n}\n\n`;
  if (multipart.length) text += `body:multipart-form {\n${multipart.map(part => `  ${part.name}: ${part.type === 'file' ? '@file(' + part.value + ')' : part.value}`).join('\n')}\n}\n\n`;
  text += `settings {\n  encodeUrl: true\n  timeout: 30000\n}\n\n`;
  text += `tests {\n  test('HTTP esperado: ${testCase.expectedHttp}', function () {\n    expect(res.status).to.equal(${testCase.expectedHttp});\n  });\n}\n\n`;
  text += `docs {\n  Actor: ${testCase.actor}. Contexto visible: ${context === 'G' ? 'global' : 'negocio ' + context}.\n  Antes de enviar: reemplace PEGAR_, CAMBIAR_, EDITAR_ y los IDs demostrativos 100xxx/200xxx/900xxx por valores reales.\n  Precondiciones: ${testCase.prerequisites}\n  Resultado esperado: HTTP ${testCase.expectedHttp}. ${testCase.scenario}.\n  Verificación posterior: ${testCase.verification}\n  Referencia: docs/qa/catalogo.md#${testCase.endpoint.toLowerCase()} y docs/qa/matriz.csv.\n}\n`;
  assert(!text.includes('{{'), `Variable oculta en ${testCase.caseId}`);
  assert(!text.includes('script:pre-request') && !text.includes('script:post-response'), `Script oculto en ${testCase.caseId}`);
  bruToJsonV2(text);
  return text;
}

function folderFile(name, seq, token = null) {
  let text = `meta {\n  name: ${name}\n  seq: ${seq}\n}\n`;
  if (token) text += `\nauth {\n  mode: bearer\n}\n\nauth:bearer {\n  token: ${token}\n}\n`;
  collectionBruToJson(text);
  return text;
}

const folders = new Map();
function ensureFolder(relative, display, token = null) {
  if (folders.has(relative)) return;
  const seq = folders.size + 1;
  write(path.join(build, relative, 'folder.bru'), folderFile(display, seq, token));
  folders.set(relative, seq);
}

const emitted = [];
function emit(testCase, destination, options = {}) {
  ensureFolder(path.dirname(destination), cleanName(path.basename(path.dirname(destination)).replace(/^\d+_/, '').replaceAll('_', ' ')));
  const file = destination.endsWith('.bru') ? destination : `${destination}.bru`;
  write(path.join(build, file), renderRequest(testCase, options));
  emitted.push({ caseId: testCase.caseId, file: file.replaceAll('\\', '/'), duplicate: Boolean(options.duplicate), context: options.context || contextFor(testCase) });
}

fs.rmSync(build, { recursive: true, force: true });
fs.mkdirSync(build, { recursive: true });
write(path.join(build, 'bruno.json'), JSON.stringify({ version: '1', name: 'TECMARKET-QA-MANUAL', type: 'collection', ignore: ['node_modules', '.git'] }, null, 2));
write(path.join(build, 'collection.bru'), `auth {\n  mode: none\n}\n`);

ensureFolder('00_LEEME_PRIMERO', 'LEEME PRIMERO');
ensureFolder('01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES', 'ADMINISTRADOR Y CATALOGOS GLOBALES', 'PEGAR_TOKEN_ADMINISTRADOR_GLOBAL');
ensureFolder('02_NEGOCIO_A_REPARACION_COMPLETA', 'NEGOCIO A - REPARACION COMPLETA', 'PEGAR_TOKEN_PROPIETARIO_A');
ensureFolder('03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION', 'NEGOCIO B - DIAGNOSTICO Y CANCELACION', 'PEGAR_TOKEN_PROPIETARIO_B');
ensureFolder('04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES', 'SEGURIDAD ENTRE NEGOCIOS Y SEDES');
ensureFolder('05_IDENTIDAD_SESIONES_Y_RATE_LIMIT', 'IDENTIDAD SESIONES Y RATE LIMIT');
ensureFolder('06_PUBLICO_CLIENTES_Y_MARKETPLACE', 'PUBLICO CLIENTES Y MARKETPLACE');
ensureFolder('07_VALIDACIONES_Y_LIMITES', 'VALIDACIONES Y LIMITES');
ensureFolder('08_ESTADOS_E_IDEMPOTENCIA', 'ESTADOS E IDEMPOTENCIA');
ensureFolder('09_ARCHIVOS_Y_CONCURRENCIA', 'ARCHIVOS Y CONCURRENCIA');
ensureFolder('10_FUNCIONES_OPCIONALES', 'FUNCIONES OPCIONALES');

const byId = new Map(sourceCases.map(testCase => [testCase.caseId, testCase]));
const pick = id => {
  const testCase = byId.get(id);
  if (!testCase) throw new Error(`No existe el caso fuente ${id}`);
  return testCase;
};

const flowA = [
  'EP012-REGISTER_ownerA', 'EP014-VERIFY_ownerA', 'EP013-LOGIN_ownerA',
  'EP055-REQUEST_A', 'EP056-OK', 'EP013-LOGIN_platformAdmin', 'EP057-OK', 'EP058-APPROVE_A',
  'EP013-LOGIN_ownerA', 'EP036-RESOLVE_A', 'EP037-OK', 'EP025-OK', 'EP027-BRANCH_A2',
  'EP012-REGISTER_customerA', 'EP014-VERIFY_customerA', 'EP013-LOGIN_customerA',
  'EP012-REGISTER_techA1', 'EP014-VERIFY_techA1', 'EP013-LOGIN_techA1',
  'EP079-MEMBER_techA1_A', 'EP046-PILOT', 'EP102-PILOT', 'EP052-PILOT', 'EP119-PILOT',
  'EP145-PILOT', 'EP153-OK', 'EP156-OK', 'EP147-FLOW_DIAGNOSIS', 'EP149-PILOT',
  'EP147-FLOW_WAITING_APPROVAL', 'EP137-PILOT', 'EP139-OK', 'EP139-OK', 'EP147-FLOW_WAITING_PARTS',
  'EP147-FLOW_REPAIRING', 'EP159-OK', 'EP148-OK', 'EP147-FLOW_TESTING',
  'EP147-FLOW_READY_FOR_PICKUP', 'EP147-FLOW_DELIVERED', 'EP105-OK', 'EP060-OK'
];
const flowB = [
  'EP012-REGISTER_ownerB', 'EP014-VERIFY_ownerB', 'EP013-LOGIN_ownerB',
  'EP055-REQUEST_B', 'EP056-OK', 'EP013-LOGIN_platformAdmin', 'EP057-OK', 'EP058-APPROVE_B',
  'EP013-LOGIN_ownerB', 'EP036-RESOLVE_B', 'EP037-OK', 'EP025-OK', 'EP027-BRANCH_B2',
  'EP012-REGISTER_customerB', 'EP014-VERIFY_customerB', 'EP013-LOGIN_customerB',
  'EP046-PILOT', 'EP102-PILOT', 'EP052-PILOT', 'EP119-PILOT', 'EP145-PILOT',
  'EP147-FLOW_DIAGNOSIS', 'EP149-PILOT', 'EP147-STATE_DIAGNOSIS_CANCELLED', 'EP105-OK'
];
function guidedLabel(id, testCase, index, tenant, occurrence = 1) {
  const exact = {
    'EP037-OK': 'Cargar rol, permisos y sedes del negocio seleccionado',
    'EP079-MEMBER_techA1_A': 'Propietario asigna el técnico a la sede principal',
    'EP046-PILOT': 'Crear servicio ofrecido por el negocio',
    'EP102-PILOT': 'Registrar dispositivo del cliente dentro del negocio',
    'EP052-PILOT': 'Crear prueba de calidad del negocio',
    'EP119-PILOT': 'Crear repuesto en inventario',
    'EP145-PILOT': 'Crear orden de reparación',
    'EP153-OK': 'Guardar checklist inicial de la orden',
    'EP156-OK': 'Subir evidencia fotográfica de la orden',
    'EP149-PILOT': 'Registrar diagnóstico de la orden',
    'EP137-PILOT': 'Crear cotización para la orden',
    'EP159-OK': 'Agregar repuesto a la orden y descontar stock',
    'EP148-OK': 'Definir precio final de la reparación',
    'EP105-OK': 'Cliente consulta su orden por número',
    'EP060-OK': 'Cliente publica reseña después de la entrega'
  };
  const explicit = {
    'EP054': `Propietario envía solicitud para crear Negocio ${tenant}`,
    'EP055': 'Propietario consulta su solicitud y copia requestId',
    'EP056': 'Administrador consulta la bandeja de solicitudes pendientes',
    'EP057': `Administrador aprueba la solicitud del Negocio ${tenant}`,
    'EP036': `Propietario obtiene el businessId del Negocio ${tenant}`,
    'EP025': 'Propietario lista la sede principal creada automáticamente',
    'EP027': `Propietario crea la segunda sede ${tenant}2`
  };
  let label = exact[id] || explicit[id.slice(0, 5)] || testCase.scenario;
  if (id === 'EP139-OK') label = occurrence === 1 ? 'Enviar cotización al cliente con estado PENDING' : 'Aprobar cotización con estado APPROVED';
  if (id === 'EP013-LOGIN_platformAdmin') label = 'Administrador global inicia sesión y copia accessToken';
  if (id === `EP013-LOGIN_owner${tenant}` && index > 3) label = `Propietario ${tenant} vuelve a iniciar sesión después de la aprobación`;
  return `${String(index + 1).padStart(2, '0')} - ${label}`;
}
function emitGuided(flow, tenant, folder) {
  const occurrences = new Map();
  for (const [index, id] of flow.entries()) {
    const occurrence = (occurrences.get(id) || 0) + 1;
    occurrences.set(id, occurrence);
    const bodyPatch = id === 'EP139-OK' ? { statusId: occurrence === 1 ? 100801 : 100802 } : undefined;
    emit(pick(id), `${folder}/${String(index + 1).padStart(2, '0')} - ${id}`, {
      context: tenant, section: tenant, duplicate: true, seq: index + 1, bodyPatch,
      label: guidedLabel(id, pick(id), index, tenant, occurrence)
    });
  }
}
emitGuided(flowA, 'A', '02_NEGOCIO_A_REPARACION_COMPLETA/00_FLUJO_GUIADO');
emitGuided(flowB, 'B', '03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/00_FLUJO_GUIADO');

for (const testCase of sourceCases) {
  const endpoint = endpointById.get(testCase.endpoint);
  const kind = caseKind(testCase);
  const module = moduleName(endpoint.controller);
  const baseName = `${testCase.caseId} - ${cleanName(testCase.scenario)}`;
  if (kind === 'OK') {
    if (endpoint.controller === 'AuthController') {
      emit(testCase, `05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/${module}/${baseName}`, { section: 'OTHER' });
    } else if (endpoint.public || endpoint.controller === 'CustomerRepairOrderController' || endpoint.controller === 'BusinessReviewController') {
      emit(testCase, `06_PUBLICO_CLIENTES_Y_MARKETPLACE/${module}/${baseName}`, { section: 'OTHER' });
    } else if (globalControllers.has(endpoint.controller) || testCase.actor === 'platformAdmin') {
      emit(testCase, `01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/01_ENDPOINTS_VALIDOS/${module}/${baseName}`, { context: 'G', section: 'ADMIN' });
    } else if (optionalControllers.has(endpoint.controller)) {
      emit(testCase, `10_FUNCIONES_OPCIONALES/${module}/${baseName}`, { context: 'A', section: 'OTHER' });
    } else {
      emit(testCase, `02_NEGOCIO_A_REPARACION_COMPLETA/01_ENDPOINTS_VALIDOS/${module}/${baseName}`, { context: 'A', section: 'A' });
      emit(testCase, `03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/01_ENDPOINTS_VALIDOS/${module}/${baseName}`, { context: 'B', section: 'B', duplicate: true });
    }
    continue;
  }
  if (isSecurity(kind)) {
    emit(testCase, `04_SEGURIDAD_ENTRE_NEGOCIOS_Y_SEDES/${module}/${baseName}`, { section: 'OTHER' });
  } else if (testCase.request.startsWith('07-identidad') || testCase.request.startsWith('07-abuso') || endpoint.controller === 'AuthController' || isNegativeAuth(kind)) {
    emit(testCase, `05_IDENTIDAD_SESIONES_Y_RATE_LIMIT/${module}/${baseName}`, { section: 'OTHER' });
  } else if (isIntegrity(kind)) {
    emit(testCase, `08_ESTADOS_E_IDEMPOTENCIA/${module}/${baseName}`, { context: 'A', section: 'OTHER' });
  } else if (/attachment|file|image|multipart|concurr/i.test(testCase.caseId + ' ' + testCase.scenario + ' ' + endpoint.controller)) {
    emit(testCase, `09_ARCHIVOS_Y_CONCURRENCIA/${module}/${baseName}`, { context: 'A', section: 'OTHER' });
  } else if (testCase.request.startsWith('08-funciones-adicionales') || optionalControllers.has(endpoint.controller)) {
    emit(testCase, `10_FUNCIONES_OPCIONALES/${module}/${baseName}`, { context: 'A', section: 'OTHER' });
  } else if (isValidation(kind) || testCase.request.startsWith('06-endpoints')) {
    emit(testCase, `07_VALIDACIONES_Y_LIMITES/${module}/${baseName}`, { section: globalControllers.has(endpoint.controller) ? 'ADMIN' : 'OTHER' });
  } else if (testCase.actor === 'platformAdmin') {
    emit(testCase, `01_ADMINISTRADOR_Y_CATALOGOS_GLOBALES/02_PREPARACION/${module}/${baseName}`, { context: 'G', section: 'ADMIN' });
  } else if (contextFor(testCase) === 'B') {
    emit(testCase, `03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION/02_PREPARACION/${module}/${baseName}`, { context: 'B', section: 'B' });
  } else {
    emit(testCase, `02_NEGOCIO_A_REPARACION_COMPLETA/02_PREPARACION/${module}/${baseName}`, { context: 'A', section: 'A' });
  }
}

const readme = `# TecMarket QA Manual\n\nEsta colección está pensada para probar el sistema a mano y escalar a miles de casos sin adivinar qué se envía.\n\n## Reglas simples\n\n1. No hay entornos ni variables con llaves. Cada URL, ID, correo y body es visible en la petición.\n2. No hay scripts previos ni posteriores. Bruno no modifica la petición ni captura datos a escondidas.\n3. En la carpeta del Administrador, Negocio A o Negocio B abre la configuración de Auth y pega el token una sola vez. Las peticiones normales heredan ese token.\n4. Los valores 100xxx pertenecen al fixture demostrativo A; los 200xxx al B; los 900xxx son globales o inexistentes. Reemplázalos por IDs reales copiados de respuestas anteriores.\n5. Reemplaza cualquier texto que empiece por PEGAR_, CAMBIAR_ o EDITAR_ antes de enviar.\n6. Ejecuta primero 02_NEGOCIO_A.../00_FLUJO_GUIADO y después 03_NEGOCIO_B.../00_FLUJO_GUIADO. Nunca ejecutes toda la colección de golpe: incluye rechazos, rate limit, desactivación, cancelación e idempotencia.\n\n## Alta correcta de un negocio\n\nEl propietario se registra, verifica el correo, inicia sesión y envía POST /api/v1/business/requests. La respuesta queda PENDING. El administrador global inicia sesión, consulta GET /api/v1/business/requests/pending, copia el requestId y aprueba mediante PUT /api/v1/business/requests/{id}/approve. La aprobación crea el negocio, una sede principal, sus catálogos y la membresía BUSINESS_OWNER. También invalida el token anterior del propietario, por lo que este debe iniciar sesión nuevamente antes de consultar /api/v1/businesses/my y continuar.\n\n## Tokens de carpeta\n\n- 01_ADMINISTRADOR...: cambia PEGAR_TOKEN_ADMINISTRADOR_GLOBAL.\n- 02_NEGOCIO_A...: cambia PEGAR_TOKEN_PROPIETARIO_A.\n- 03_NEGOCIO_B...: cambia PEGAR_TOKEN_PROPIETARIO_B.\n- Los casos con otro actor muestran su token literal directamente en Auth.\n\n## Cómo avanzar sin perderte\n\nDespués de cada POST exitoso, copia data.id de la respuesta y reemplaza el ID demostrativo equivalente en el siguiente request. El body que ves es exactamente el que se envía. Si un caso espera 4xx, consulta su bloque Docs antes de ejecutarlo y verifica que no haya mutación.\n\nLa matriz completa está en docs/qa/matriz.csv y el contrato de los ${contract.operations.length} endpoints en docs/qa/catalogo.md.\n`;
write(path.join(build, 'README.md'), readme);
write(path.join(build, '00_LEEME_PRIMERO', 'README.md'), readme);

// Keep the documented request paths aligned with the ergonomic collection.
const preferredPathByCase = new Map();
for (const item of emitted) {
  if (!preferredPathByCase.has(item.caseId) || !item.duplicate) preferredPathByCase.set(item.caseId, item.file);
}
const manualCases = sourceCases.map(testCase => ({
  ...testCase,
  request: preferredPathByCase.get(testCase.caseId) || testCase.request
}));
write(path.join(docsDir, 'casos.json'), JSON.stringify(manualCases, null, 2));
const columns = Object.keys(manualCases[0]);
write(path.join(docsDir, 'matriz.csv'), '\uFEFF' + [columns, ...manualCases.map(testCase => columns.map(column => testCase[column] ?? ''))]
  .map(row => row.map(value => `"${String(value).replaceAll('"', '""')}"`).join(','))
  .join('\n'));
let catalog = fs.readFileSync(path.join(docsDir, 'catalogo.md'), 'utf8');
for (const testCase of sourceCases) {
  const manualPath = preferredPathByCase.get(testCase.caseId);
  if (manualPath) catalog = catalog.replaceAll(`../../bruno/TECMARKET-QA/${testCase.request}`, `../../bruno/TECMARKET-QA/${manualPath}`);
}
write(path.join(docsDir, 'catalogo.md'), catalog);
write(path.join(docsDir, 'variables-generadas.md'), `# Valores editables de Bruno\n\nLa colección manual no usa variables, environments ni expresiones con llaves. Cada petición contiene valores literales visibles.\n\n- IDs 100xxx: recursos del Negocio A.\n- IDs 200xxx: recursos del Negocio B.\n- IDs 900xxx: recursos globales o IDs deliberadamente inexistentes.\n- PEGAR_: token o cookie que debes pegar.\n- CAMBIAR_: ruta o valor obligatorio que debes sustituir.\n- EDITAR_: valor de negocio que debes revisar antes de enviar.\n\nCopia siempre los IDs reales desde data.id de la respuesta que creó el recurso. No existe captura automática.\n`);
write(path.join(docsDir, 'cobertura.json'), JSON.stringify({
  controllers: contract.controllers,
  operations: contract.operations.length,
  sourceCases: sourceCases.length,
  requests: emitted.length,
  liveExecuted: 0,
  status: 'NOT_RUN',
  style: 'manual-literal'
}, null, 2));

const bruFiles = fs.readdirSync(build, { recursive: true }).filter(file => String(file).endsWith('.bru'));
let requestCount = 0;
for (const relative of bruFiles) {
  const text = fs.readFileSync(path.join(build, relative), 'utf8');
  assert(!text.includes('{{'), `Quedó una variable en ${relative}`);
  assert(!text.includes('script:pre-request') && !text.includes('script:post-response'), `Quedó un script en ${relative}`);
  if (String(relative).endsWith('folder.bru') || String(relative) === 'collection.bru') collectionBruToJson(text);
  else { bruToJsonV2(text); requestCount++; }
}
assert(requestCount >= sourceCases.length, `Cobertura reducida: ${requestCount}/${sourceCases.length}`);
assert(emitted.some(item => item.file.startsWith('02_NEGOCIO_A_')) && emitted.some(item => item.file.startsWith('03_NEGOCIO_B_')), 'Faltan los dos flujos');

const resolvedOutput = path.resolve(output);
const expectedOutput = path.resolve(root, 'bruno/TECMARKET-QA');
assert.equal(resolvedOutput, expectedOutput, 'Destino destructivo inesperado');
assert(resolvedOutput.startsWith(path.resolve(root) + path.sep), 'Destino fuera del repositorio');
fs.rmSync(resolvedOutput, { recursive: true, force: true });
fs.renameSync(build, resolvedOutput);

const counts = {};
for (const item of emitted) counts[item.file.split('/')[0]] = (counts[item.file.split('/')[0]] || 0) + 1;
const report = {
  status: 'PASS', operations: contract.operations.length, sourceCases: sourceCases.length,
  requests: requestCount, literalVariablesRemaining: 0, preRequestScripts: 0, postResponseScripts: 0,
  folders: counts
};
write(path.join(root, 'target/qa/manual-collection-validation.json'), JSON.stringify(report, null, 2));
console.log(JSON.stringify(report, null, 2));
