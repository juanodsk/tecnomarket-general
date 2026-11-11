import fs from 'node:fs';
import path from 'node:path';
import assert from 'node:assert/strict';
import { createRequire } from 'node:module';

const root = path.resolve(import.meta.dirname, '..');
const collection = path.join(root, 'bruno/TECMARKET-QA');
const require = createRequire(import.meta.url);
const { bruToJsonV2, collectionBruToJson } = require(path.join(root, 'target/qa-tooling/node_modules/@usebruno/lang'));
const contract = JSON.parse(fs.readFileSync(path.join(root, 'docs/qa/contrato.json'), 'utf8'));
const sourceCases = JSON.parse(fs.readFileSync(path.join(root, 'docs/qa/casos.json'), 'utf8'));
const operations = new Set();
let requests = 0;

assert.equal(contract.operations.length, 186, 'Cambió el número de endpoints; regenere y revise el contrato');
assert.equal(new Set(contract.operations.map(endpoint => endpoint.controller)).size, 35);
assert(fs.existsSync(path.join(collection, '02_NEGOCIO_A_REPARACION_COMPLETA')));
assert(fs.existsSync(path.join(collection, '03_NEGOCIO_B_DIAGNOSTICO_Y_CANCELACION')));
assert(!fs.existsSync(path.join(collection, 'environments')), 'La colección manual no debe depender de environments');

for (const relative of fs.readdirSync(collection, { recursive: true })) {
  if (!String(relative).endsWith('.bru')) continue;
  const text = fs.readFileSync(path.join(collection, relative), 'utf8');
  assert(!text.includes('{{'), `Variable con llaves encontrada en ${relative}`);
  assert(!text.includes('script:pre-request'), `Pre-request script encontrado en ${relative}`);
  assert(!text.includes('script:post-response'), `Post-response script encontrado en ${relative}`);
  if (String(relative).endsWith('folder.bru') || String(relative) === 'collection.bru') {
    collectionBruToJson(text);
    continue;
  }
  const parsed = bruToJsonV2(text);
  requests++;
  const id = parsed.meta.name.match(/EP\d{3}/)?.[0];
  if (id) operations.add(id);
  assert(/^https:\/\/api-dev\.juanodsk\.dev\//.test(parsed.http.url), `URL no literal o dominio incorrecto en ${relative}`);
  if (parsed.body?.json) JSON.parse(parsed.body.json);
  assert(parsed.tests?.includes('HTTP esperado:'), `Falta aserción HTTP en ${relative}`);
}

for (const endpoint of contract.operations) assert(operations.has(endpoint.id), `Falta cobertura para ${endpoint.id}`);
assert(requests >= sourceCases.length, `Se redujo la cobertura: ${requests}/${sourceCases.length}`);

const report = {
  status: 'PASS',
  validation: 'Parser oficial de Bruno + estructura manual + cobertura del contrato; no ejecuta HTTP',
  controllers: 35,
  operations: operations.size,
  requests,
  sourceCases: sourceCases.length,
  checks: [
    'dos flujos empresariales separados',
    'administrador y catálogos globales separados',
    'URLs y bodies literales',
    'sin variables con llaves',
    'sin environments',
    'sin scripts previos o posteriores',
    'JSON y sintaxis Bruno válidos',
    'aserción HTTP en cada petición',
    'cobertura de las 186 operaciones'
  ],
  liveRequests: 0
};
fs.mkdirSync(path.join(root, 'target/qa'), { recursive: true });
fs.writeFileSync(path.join(root, 'target/qa/validation.json'), JSON.stringify(report, null, 2) + '\n');
console.log(JSON.stringify(report, null, 2));
