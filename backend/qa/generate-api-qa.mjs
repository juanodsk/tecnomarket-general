// Generate reviewable QA artifacts from the checked-out Java contract. No HTTP calls.
import fs from 'node:fs';
import path from 'node:path';
import { execFileSync } from 'node:child_process';
const root = path.resolve(import.meta.dirname, '..');
const javaRoot = path.join(root, 'src/main/java');
const docs = path.join(root, 'docs/qa');
// This is an internal, generated source collection. The human-facing Bruno
// collection is rebuilt by generate-bruno-manual.mjs with literal values and
// an ergonomic folder layout.
const collection = path.join(root, 'target/qa-generated-source');
const walk = dir => fs.readdirSync(dir, { withFileTypes: true }).flatMap(e => e.isDirectory() ? walk(path.join(dir,e.name)) : [path.join(dir,e.name)]);
const java = walk(javaRoot).filter(p => p.endsWith('.java'));
const sources = new Map(java.map(p => [path.basename(p,'.java'), {p, text:fs.readFileSync(p,'utf8')}]));
const rel = p => path.relative(root,p).replaceAll('\\','/');
const write = (p, text) => { if(p.endsWith('.md'))text=text.replace(/[\t ]+$/gm,''); fs.mkdirSync(path.dirname(p),{recursive:true}); fs.writeFileSync(p,text.endsWith('\n') ? text : text+'\n'); };
export function balanced(s,start,open='(',close=')') {
  let depth=0, quoted=false, escape=false;
  for(let i=start;i<s.length;i++) {
    const c=s[i]; if(quoted){if(escape)escape=false;else if(c==='\\')escape=true;else if(c==='"')quoted=false;continue;}
    if(c==='"'){quoted=true;continue;} if(c===open)depth++; if(c===close && --depth===0)return i;
  } throw Error('Unbalanced Java expression: '+s.slice(start,start+100));
}
function split(s) {
  let start=0, stack=[],quote=false,escape=false; const out=[];
  for(let i=0;i<s.length;i++) { const c=s[i];if(quote){if(escape)escape=false;else if(c==='\\')escape=true;else if(c==='"')quote=false;continue;}
    if(c==='"')quote=true;else if('(<{['.includes(c))stack.push(c);else if(')>}]'.includes(c))stack.pop();else if(c===','&&!stack.length){out.push(s.slice(start,i));start=i+1;}}
  return [...out,s.slice(start)].map(x=>x.trim()).filter(Boolean);
}
function annotations(s) {
  const a=[]; let clean='',pos=0;const re=/@(\w+)(\s*\()?/g;let m;
  while((m=re.exec(s))){ clean+=s.slice(pos,m.index); let end=re.lastIndex,args='';if(m[2]){end=balanced(s,end-1)+1;args=s.slice(re.lastIndex,end-1);}
    a.push({name:m[1],args});pos=end;re.lastIndex=end;}
  return {a,clean:(clean+s.slice(pos)).replace(/\s+/g,' ').trim()};
}
function attr(a,name,fallback='') {const m=a?.match(new RegExp('(?:^|[,\\s])'+name+'\\s*=\\s*("(?:[^"\\\\]|\\\\.)*"|[^,}]+)'));if(!m)return fallback;try{return JSON.parse(m[1]);}catch{return m[1].trim();}}
function firstString(s){const m=s.match(/"(?:[^"\\]|\\.)*"/);return m?JSON.parse(m[0]):'';}
function fields(raw) {return split(raw).map(v=>{const {a,clean}=annotations(v);const m=clean.match(/^(.*?)\s+(\w+)$/);if(!m)throw Error('Cannot parse field '+v);return {name:m[2],type:m[1].replace(/\s*([<>])\s*/g,'$1'),annotations:a,required:a.some(v=>['NotNull','NotBlank','NotEmpty'].includes(v.name)),raw:v};});}
const records={};const enums={};
for(const [name,{text,p}] of sources){const r=text.match(/public record \w+\s*\(/);if(r){const start=r.index+r[0].length-1;records[name]={fields:fields(text.slice(start+1,balanced(text,start))),source:rel(p)};}
  const e=text.match(/public enum \w+\s*\{([^;}]*)/);if(e)enums[name]=e[1].split(',').map(x=>x.trim()).filter(x=>/^\w+$/.test(x));}
const controllers=[];const endpoints=[];
for(const [name,{text,p}] of sources){if(!/@RestController\b/.test(text))continue;controllers.push(name);const ci=text.indexOf('public class');const header=text.slice(0,ci);const base=firstString(header.match(/@RequestMapping\(([^\n]*)\)/)?.[1]||'');
  const feature=header.match(/@ConditionalOnProperty\([^)]*name\s*=\s*"([^"]+)"/ )?.[1]||'';
  const re=/@(Get|Post|Put|Patch|Delete)Mapping\b/g;let m;let previous=ci;
  while((m=re.exec(text))){let end=re.lastIndex, args='';if(text[end]==='('){end=balanced(text,end)+1;args=text.slice(re.lastIndex+1,end-1);}
    const methodStart=text.indexOf('public ',end),paramsStart=text.indexOf('(',methodStart),paramsEnd=balanced(text,paramsStart);const bodyStart=text.indexOf('{',paramsEnd),bodyEnd=balanced(text,bodyStart,'{','}');
    const signature=text.slice(methodStart,paramsStart);const fn=signature.match(/(\w+)\s*$/)[1];const body=text.slice(bodyStart+1,bodyEnd);const before=text.slice(previous,m.index);
    const ann=annotations(before).a;const permission=ann.find(x=>x.name==='PreAuthorize')?.args||'';
    const operation=ann.find(x=>x.name==='Operation')?.args||'';
    let route=base+firstString(args);const method=m[1].toUpperCase();const params=fields(text.slice(paramsStart+1,paramsEnd));
    const dto=params.find(x=>x.annotations.some(a=>a.name==='RequestBody'))?.type||null;
    const pub=route.startsWith('/api/v1/public/') || (name==='AuthController' && ['register','login','refresh','verifyEmail','requestPasswordReset','resetPassword'].includes(fn)) || (route==='/api/v1/parameters/document-types'&&method==='GET');
    endpoints.push({id:'',controller:name,fn,method,route,permission:firstString(permission),summary:attr(operation,'summary',fn),params,dto,response:signature.slice(7).replace(/\s+\w+\s*$/,'').trim(),http:body.includes('HttpStatus.CREATED')?201:body.includes('noContent()')?204:200,public:pub,feature,source:rel(p),line:text.slice(0,m.index).split('\n').length,body});
    previous=bodyEnd+1;re.lastIndex=bodyEnd+1;
  }
}
endpoints.sort((a,b)=>a.controller.localeCompare(b.controller)||a.line-b.line).forEach((e,i)=>e.id='EP'+String(i+1).padStart(3,'0'));
if(endpoints.length!==186 || controllers.length!==35)throw Error(`Contract changed: ${endpoints.length} operations / ${controllers.length} controllers. Review inventory before updating baseline.`);
const vars = new Map();
function variable(name,origin,type='String'){if(!vars.has(name))vars.set(name,{origin,type});return '{{'+name+'}}';}
function sample(f,context,minimal=false) {
  const n=f.name,t=f.type;const schema=f.annotations.find(a=>a.name==='Schema')?.args||'';const example=attr(schema,'example',null);
  if(t.startsWith('List<')) {const inner=t.slice(5,-1);return [sample({...f,name:n.replace(/Ids$/,'Id'),type:inner,annotations:[]},context,minimal)];}
  if(records[t])return bodyFor(t,minimal);
  if(enums[t])return example&&enums[t].includes(example)?example:enums[t][0];
  if(['logo','coverImage','imageUrl'].includes(n))return null;
  if(n==='token')return variable(context==='ResetPasswordRequest'?'resetToken':'verificationToken','Token recibido por correo; introducir manualmente, nunca en Git');
  if(/password/i.test(n))return variable(n,'Secreto local del actor de prueba');
  if(n==='email')return variable('email','Correo controlado del actor o negocio QA');
  if(n==='documentNumber')return variable('documentNumber','Documento sintético único por runId y actor; distinto para cada registro');
  if(n.endsWith('Id'))return variable(n,`Consultar variables.md: recurso activo del negocio/sede seleccionados (${context})`,t);
  if(t==='Boolean'||t==='boolean')return example===false||example==='false'?false:true;
  if(t==='LocalDateTime')return variable(n,'Fecha ISO local futura; relaciones inicio < fin; configurar en preflight');
  if(t==='LocalDate')return variable(n,'Fecha ISO yyyy-MM-dd; periodo que incluya las órdenes QA');
  if(t==='LocalTime')return n==='closeTime'?'17:00:00':'08:00:00';
  if(['Integer','Long','BigDecimal','Double','int'].includes(t)) {if(example!==null&&!isNaN(Number(example)))return Number(example);return /quantity/i.test(n)?1:1;}
  if(n==='scope')return 'BUSINESS';if(n==='features')return '["QA"]';
  if(['q','customer','orderNumber','ticketCode'].includes(n))return variable(n,'Valor exacto del fixture seleccionado; ver variables.md');
  if(n==='code'||n==='sku')return variable('qaCode','QA + runId corto + sufijo del recurso; único para cada creación');
  if(n==='name')return 'Usuario QA';
  if(['businessName','legalName'].includes(n))return 'Negocio QA';
  if(example!==null&&example!=='null')return example;
  if(t==='String')return 'Dato QA';
  throw Error(`No sample for ${context}.${n}: ${t}`);
}
function bodyFor(dto,minimal=false){return Object.fromEntries(records[dto].fields.filter(f=>!minimal||f.required).map(f=>[f.name,sample(f,dto,minimal)]));}
const paramName=p=> {const a=p.annotations.find(x=>['PathVariable','RequestParam','RequestHeader','CookieValue','RequestPart'].includes(x.name));return attr(a?.args,'name',attr(a?.args,'value',firstString(a?.args||'')))||p.name;};
function urlFor(e){let url=e.route.replace(/\{(\w+)\}/g,(_,n)=>variable(n,'Parámetro de ruta: ver origen por operación en variables.md',n.endsWith('Id')||n==='id'?'Integer':'String'));const qs=e.params.filter(p=>p.annotations.some(a=>a.name==='RequestParam')).filter(p=>{const a=p.annotations.find(a=>a.name==='RequestParam');return attr(a.args,'required',true)!==false || attr(a.args,'defaultValue',null)!==null;});
  if(qs.length)url+='?'+qs.map(p=>{const a=p.annotations.find(a=>a.name==='RequestParam');return paramName(p)+'='+attr(a.args,'defaultValue',sample(p,e.controller));}).join('&');return '{{baseUrl}}'+url;}
const cases=[];const artifacts=[];
function addCase(e,kind,expected,description,options={}) {
  const id=`${e.id}-${kind}`;const folder=options.folder||`06-endpoints/${e.controller}`;const file=`${folder}/${id}.bru`;
  const actor=options.actor || (e.public?'anonymous':e.controller==='CustomerRepairOrderController'||(e.controller==='BusinessReviewController'&&e.method==='POST')?'customerA':e.route.includes('/parameters/')||e.controller.includes('Subscription')||(e.controller==='BusinessController'&&e.method==='POST')||e.fn==='approve'||e.fn==='reject'?'platformAdmin':'actor');
  let body=options.body!==undefined?options.body:e.dto?bodyFor(e.dto):null;
  let url=options.url||urlFor(e);const method=options.method||e.method;
  const parts=e.params.filter(p=>p.annotations.some(a=>a.name==='RequestPart'));
  const headers={'Accept-Language':'es'};if(body!==null)headers['Content-Type']='application/json';
  if(!e.public&&kind!=='NO_TOKEN')headers.Authorization=kind==='INVALID_TOKEN'?'Bearer qa-invalid-token':`Bearer {{${actor}Token}}`;
  if(kind==='REVOKED')headers.Authorization='Bearer {{revokedToken}}';
  if(e.params.some(p=>p.annotations.some(a=>a.name==='CookieValue'))) {headers.Cookie='tm_refresh={{refreshToken}}';headers.Origin='{{origin}}';}
  if(e.params.some(p=>p.annotations.some(a=>a.name==='RequestHeader'&&paramName(p)==='Idempotency-Key')))headers['Idempotency-Key']='{{idempotencyKey}}';
  Object.assign(headers,options.headers||{});for(const k of Object.keys(headers))if(headers[k]===null)delete headers[k];
  const templated=JSON.stringify({url,body,headers});const needed=[...new Set([...templated.matchAll(/\{\{(\w+)\}\}/g)].map(m=>m[1]))];
  if(parts.some(p=>p.type==='MultipartFile'))needed.push('imagePath');
  for(const n of needed)if(!vars.has(n))variable(n,'Entrada local indicada en README y variables.md');
  const prerequisites=options.pre||`Datos QA activos; actor ${actor}; ${e.feature?'fase 2 y FEATURE_'+e.feature.replaceAll('-','_').toUpperCase()+'=true; ':''}${e.dto?'body válido según contrato; ':''}recurso preparado para ${e.fn}.`;
  const after=options.after|| (method==='GET'?'Comprobar campos, pertenencia y ausencia de datos ajenos.':'Consultar recurso/listado e historial con propietario; comparar antes/después. En rechazo no debe haber mutación.');
  const row={caseId:id,endpoint:e.id,method,path:e.route,scenario:description,actor,prerequisites,expectedHttp:expected,verification:after,request:file,status:options.blocked?'BLOCKED':'NOT_RUN',observed:'',date:'',evidence:'',reason:options.blocked||''};cases.push(row);
  const typeMap=Object.fromEntries(Object.entries(records).map(([k,v])=>[k,Object.fromEntries(v.fields.map(f=>[f.name,f.type]))]));
  const relevant={};function includeType(t){if(!typeMap[t]||relevant[t])return;relevant[t]=typeMap[t];Object.values(typeMap[t]).forEach(v=>includeType(v.replace(/^List<(.*)>$/,'$1')));}includeType(e.dto);
  const typed=body!==null?`\nconst schemas=${JSON.stringify(relevant)};\nconst render=(v,type)=>{if(v===null)return null;if(Array.isArray(v))return v.map(x=>render(x,(type||'').replace(/^List<(.*)>$/,'$1')));if(typeof v==='object')return Object.fromEntries(Object.entries(v).map(([k,x])=>[k,render(x,schemas[type]?.[k])]));if(typeof v!=='string')return v;const exact=v.match(/^\\{\\{(\\w+)\\}\\}$/);if(exact){const x=read(exact[1]);if(['Integer','Long','BigDecimal','Double','int'].includes(type)){const n=Number(x);if(!Number.isFinite(n))throw new Error('BLOCKED: variable numérica '+exact[1]);return n;}return x;}return v.replace(/\\{\\{(\\w+)\\}\\}/g,(_,key)=>String(read(key)));};\nreq.setBody(JSON.stringify(render(${JSON.stringify(body)},${JSON.stringify(e.dto)})));\n`:'';
  const pre=`const fromScopes = key => bru.getVar(key) ?? bru.getRequestVar(key) ?? bru.getFolderVar(key) ?? bru.getCollectionVar(key) ?? bru.getEnvVar(key);\nconst configuredRunId = fromScopes('runId');\nif (configuredRunId === undefined || configuredRunId === null || configuredRunId === '' || String(configuredRunId).startsWith('REQUIRED')) bru.setVar('runId', new Date().toISOString().replace(/\\D/g, '').slice(0, 14));\nconst read = key => fromScopes(key);\nconst required = ${JSON.stringify(needed)};\nfor (const key of required) { const value = read(key); if (value === undefined || value === null || value === '' || String(value).startsWith('REQUIRED')) throw new Error('Falta la variable '+key+'. Defínela en Vars de la petición o en el entorno seleccionado.'); }\n${typed}${options.preScript||''}`;
  const check=`test('${id}: HTTP ${expected}', function () { expect(res.status).to.equal(${expected}); });\ntest('${id}: estructura de respuesta', function () { const b=res.getBody(); expect(b).to.be.an('object'); expect(b.success).to.equal(${expected<400}); expect(b).to.have.property('message'); expect(b).to.have.property('data'); });\n${options.tests||''}`;
  const post=options.postScript|| ((expected<300&&method==='POST'&&!e.public)?`if (res.status === ${expected}) { const d=res.getBody().data; if(d && d.id !== undefined) bru.setVar('${e.id}_id', d.id); }`: '');
  const content=`meta {\n  name: ${id} ${description.replace(/[\r\n]/g,' ')}\n  type: http\n  seq: ${cases.length}\n}\n\n${method.toLowerCase()} {\n  url: ${url}\n  body: ${body!==null?'json':parts.length?'multipartForm':'none'}\n  auth: none\n}\n\nheaders {\n${Object.entries(headers).map(([k,v])=>'  '+k+': '+v).join('\n')}\n}\n\n${body!==null?'body:json {\n'+JSON.stringify(body,null,2).split('\n').map(l=>'  '+l).join('\n')+'\n}\n\n':''}${body===null&&parts.length?'body:multipart-form {\n'+parts.map(p=>'  '+paramName(p)+': '+(p.type==='MultipartFile'?'@file({{imagePath}})':'QA evidencia')).join('\n')+'\n}\n\n':''}settings {\n  encodeUrl: true\n  timeout: 30000\n}\n\nscript:pre-request {\n${pre.split('\n').map(l=>'  '+l).join('\n')}\n}\n\n${post?'script:post-response {\n'+post.split('\n').map(l=>'  '+l).join('\n')+'\n}\n\n':''}tests {\n${check.split('\n').map(l=>'  '+l).join('\n')}\n}\n\ndocs {\n  ${prerequisites}\n  ${after}\n  Caso: docs/qa/matriz.csv; contrato: docs/qa/catalogo.md#${e.id.toLowerCase()}\n}\n`;
  artifacts.push({file,content});return row;
}
for(const e of endpoints) {
  e.sample=e.dto?bodyFor(e.dto):null;e.minimum=e.dto?bodyFor(e.dto,true):null;e.url=urlFor(e);
  addCase(e,'OK',e.http,'Contrato válido');
  if(!e.public){addCase(e,'NO_TOKEN',401,'Sin autenticación');addCase(e,'INVALID_TOKEN',401,'Token inválido');addCase(e,'REVOKED',401,'Token previamente revocado');
    if(/has(Any)?Authority/.test(e.permission))addCase(e,'NO_PERMISSION',403,'Actor válido sin permiso en recurso existente',{actor:'noPermission'});
    if(e.route.includes('{businessId}')&&!e.controller.includes('Subscription')&&!(e.controller==='BusinessReviewController'&&e.method==='POST'))addCase(e,'OTHER_TENANT',403,'Usuario B intenta acceder al negocio A',{actor:'ownerB',pre:'ownerB tiene el permiso de la operación en B, no pertenece a A. URL y body contienen únicamente recursos válidos de A.'});
  }
  if(e.feature)addCase(e,'DISABLED',404,'Funcionalidad deshabilitada',{pre:`Desactivar FEATURE_${e.feature.replaceAll('-','_').toUpperCase()} y redesplegar. Usar token válido con todos los permisos para no medir 401/403.`});
  if(e.dto){const all=records[e.dto].fields;for(const f of all){if(f.required){for(const mode of ['MISSING','NULL']){const b=structuredClone(e.sample);if(mode==='MISSING')delete b[f.name];else b[f.name]=null;addCase(e,`${f.name}_${mode}`,400,`${f.name}: ${mode==='NULL'?'null':'ausente'}`,{body:b});}}
      if(f.annotations.some(a=>a.name==='NotBlank'))for(const [suffix,v] of [['EMPTY',''],['BLANK','   ']])addCase(e,`${f.name}_${suffix}`,400,`${f.name}: ${suffix}`,{body:{...e.sample,[f.name]:v}});
      const size=f.annotations.find(a=>a.name==='Size');if(size){const max=Number(attr(size.args,'max',0));if(max&&f.type==='String')addCase(e,`${f.name}_MAX_PLUS_1`,400,`${f.name}: ${max+1} caracteres`,{body:{...e.sample,[f.name]:'x'.repeat(max+1)}});}
      if(f.annotations.some(a=>a.name==='Email'))addCase(e,`${f.name}_EMAIL`,400,`${f.name}: correo sin formato válido`,{body:{...e.sample,[f.name]:'not-an-email'}});
      if(f.type.startsWith('List<')){
        if(f.annotations.some(a=>a.name==='NotEmpty')||Number(attr(size?.args,'min',0))>0)addCase(e,`${f.name}_EMPTY_ARRAY`,400,`${f.name}: colección vacía`,{body:{...e.sample,[f.name]:[]}});
        const max=Number(attr(size?.args,'max',0));if(max)addCase(e,`${f.name}_MAX_ARRAY`,400,`${f.name}: ${max+1} elementos`,{body:{...e.sample,[f.name]:Array.from({length:max+1},()=>e.sample[f.name][0])}});
        const inner=f.type.slice(5,-1);if(records[inner]&&f.annotations.some(a=>a.name==='Valid'))for(const nested of records[inner].fields.filter(x=>x.required)){
          const item={...e.sample[f.name][0]};delete item[nested.name];addCase(e,`${f.name}_${nested.name}_MISSING`,400,`${f.name}[0].${nested.name}: ausente`,{body:{...e.sample,[f.name]:[item]}});}
      }
      if(['Integer','Long','BigDecimal','Boolean','LocalDate','LocalDateTime'].includes(f.type))addCase(e,`${f.name}_TYPE`,400,`${f.name}: tipo incompatible`,{body:{...e.sample,[f.name]:{invalid:true}}});
      for(const a of f.annotations.filter(a=>['Min','Max','DecimalMin','DecimalMax','Positive'].includes(a.name))){const bound=a.name==='Positive'?0:Number(attr(a.args,'value',firstString(a.args)||a.args));const value=a.name.endsWith('Max')?bound+1:a.name==='Positive'?0:bound-1;if(Number.isFinite(value))addCase(e,`${f.name}_${a.name}`,400,`${f.name}: fuera de límite ${a.name}`,{body:{...e.sample,[f.name]:value}});}
    }}
  for(const p of e.params.filter(p=>p.annotations.some(a=>a.name==='RequestParam'))){const a=p.annotations.find(a=>a.name==='RequestParam');if(['Integer','BigDecimal','LocalDate','LocalDateTime'].includes(p.type)){const name=paramName(p);let u=e.url.replace(new RegExp('([?&])'+name+'=[^&]*&?'),'$1').replace(/[?&]$/,'');u+=(u.includes('?')?'&':'?')+name+'=invalid';addCase(e,`QUERY_${name}_TYPE`,400,`Query ${name}: tipo inválido`,{url:u});}}
  const setQuery=(u,n,v)=>{u=u.replace(new RegExp('([?&])'+n+'=[^&]*&?'),'$1').replace(/[?&]$/,'');return u+(u.includes('?')?'&':'?')+n+'='+v;};
  const queries=e.params.filter(p=>p.annotations.some(a=>a.name==='RequestParam'));
  for(const p of queries){const n=paramName(p);for(const a of p.annotations.filter(a=>['Min','Max','DecimalMin','DecimalMax'].includes(a.name))){const bound=Number(firstString(a.args)||a.args);if(Number.isFinite(bound))addCase(e,`QUERY_${n}_${a.name}`,400,`Query ${n}: límite inválido`,{url:setQuery(e.url,n,a.name.endsWith('Max')?bound+1:bound-1)});}
    if(p.annotations.some(a=>a.name==='RequestParam'&&attr(a.args,'required',true)===false))addCase(e,`FILTER_${n}`,e.http,`Filtro ${n} individual`,{url:setQuery(e.url,n,variable(n,'Filtro: valor exacto de una fila QA del negocio seleccionado',p.type)),pre:'Preparar al menos una coincidencia y una fila que no coincida; comparar todos los resultados con el filtro.'});}
  const filters=queries.filter(p=>p.annotations.some(a=>a.name==='RequestParam'&&attr(a.args,'required',true)===false));
  for(let i=0;i<filters.length;i++)for(let j=i+1;j<filters.length;j++){const a=paramName(filters[i]),b=paramName(filters[j]);addCase(e,`FILTER_${a}_${b}`,e.http,`Filtros combinados ${a} + ${b}`,{url:setQuery(setQuery(e.url,a,variable(a,'Filtro del fixture')),b,variable(b,'Filtro del fixture')),pre:'Elegir valores de la misma fila QA. Incluir registros que cumplan solo uno; verificar intersección y ausencia de filas ajenas.'});}
  if(e.method==='GET'&&e.route.match(/\{(?:id|\w+Id)\}$/)&&!e.public&&!e.route.endsWith('{businessId}')){
    const last=e.route.match(/\{(\w+)\}$/)[1];addCase(e,'NOT_FOUND',404,'ID inexistente comprobado',{url:e.url.replace('{{'+last+'}}','{{missingId}}'),pre:'missingId es un entero positivo verificado inexistente; actor con permiso y todos los padres válidos.'});}
  if(e.route.includes('{catalogType}'))for(const type of enums.BusinessCatalogType)addCase(e,`CAT_${type}`,e.http,`Catálogo ${type}`,{url:e.url.replace('{{catalogType}}',type),pre:`Crear recurso QA no protegido en catálogo ${type} del negocio A antes de PUT/DELETE/GET por ID. No usar estados sembrados protegidos.`});
}
// Curated journeys extend the generated field-level tests and share the same registry.
const { buildJourneys } = await import('./qa-journeys.mjs');
buildJourneys({endpoints,addCase,bodyFor,variable});
const manifest=endpoints.map(({body,...e})=>e);
write(path.join(docs,'contrato.json'),JSON.stringify({commit:execFileSync('git',['rev-parse','HEAD'],{cwd:root,encoding:'utf8'}).trim(),controllers:controllers.length,operations:manifest,records,enums},null,2));
let md='# Catálogo completo de endpoints\n\nGenerado del código actual. Los ejemplos requieren sustituir variables; ningún ID se supone fijo. Para ejecutar, comenzar por [README](README.md).\n\n';
md+='| ID | Método | Ruta | Controlador |\n|---|---|---|---|\n'+endpoints.map(e=>`| [${e.id}](#${e.id.toLowerCase()}) | ${e.method} | \`${e.route}\` | ${e.controller} |`).join('\n')+'\n\n';
for(const e of endpoints){md+=`## ${e.id}\n\n\`${e.method} ${e.route}\` — ${e.summary}\n\nFuente: [${e.controller}:${e.line}](../../${e.source}#L${e.line}). Método Java: \`${e.fn}\`.\n\nAcceso: ${e.public?'público':e.route.includes('{businessId}')?'negocio; consultar sede/propiedad en guía':e.route.includes('/parameters/')?'catálogo global / plataforma según permiso':'usuario autenticado; ver reglas de guía'}. Permiso del controlador: \`${e.permission||'ninguno adicional'}\`. ${e.feature?'Requiere `FEATURE_'+e.feature.replaceAll('-','_').toUpperCase()+'=true`.':''}\n\nÉxito: **${e.http}**. Respuesta: \`${e.response}\`. Ver definiciones en [contrato.json](contrato.json).\n\n`;
  md+='| Parámetro | Ubicación | Tipo | Declaración / restricciones |\n|---|---|---|---|\n'+e.params.filter(p=>!p.annotations.some(a=>a.name==='AuthenticationPrincipal')&&!['HttpServletRequest','HttpServletResponse'].includes(p.type)).map(p=>`| ${paramName(p)} | ${p.annotations.map(a=>a.name).filter(n=>/Request|Path|Cookie/.test(n)).join(', ')} | ${p.type} | ${p.raw.replace(/\s+/g,' ').replaceAll('|','\\|')} |`).join('\n')+'\n\n';
  if(e.dto){md+=`Body completo (${e.dto}):\n\n\`\`\`json\n${JSON.stringify(e.sample,null,2)}\n\`\`\`\n\nBody mínimo por Bean Validation (también exige relaciones/estados válidos):\n\n\`\`\`json\n${JSON.stringify(e.minimum,null,2)}\n\`\`\`\n\nCampos y validaciones:\n\n| Campo | Tipo | Obligatorio | Restricciones |\n|---|---|---|---|\n`+records[e.dto].fields.map(f=>`| ${f.name} | ${f.type} | ${f.required?'sí':'no'} | ${f.annotations.filter(a=>a.name!=='Schema').map(a=>'@'+a.name+(a.args?'('+a.args+')':'')).join(' ')} |`).join('\n')+'\n\n';}
  else md+=e.params.some(p=>p.type==='MultipartFile')?'Body multipart: seleccionar archivo real en el campo `file`; Bruno genera el boundary. No usar JSON.\n\n':'Sin body JSON. Las cookies/headers, cuando aparecen arriba, siguen siendo obligatorios.\n\n';
  md+='Casos Bruno: '+cases.filter(c=>c.endpoint===e.id).map(c=>`[${c.caseId}](../../bruno/TECMARKET-QA/${c.request})`).join(' · ')+'\n\n';}
write(path.join(docs,'catalogo.md'),md.trimEnd());
const cols=Object.keys(cases[0]);write(path.join(docs,'matriz.csv'),'\uFEFF'+[cols,...cases.map(c=>cols.map(k=>c[k]??''))].map(row=>row.map(v=>'"'+String(v).replaceAll('"','""')+'"').join(',')).join('\n'));
write(path.join(docs,'casos.json'),JSON.stringify(cases,null,2));
write(path.join(collection,'bruno.json'),JSON.stringify({version:'1',name:'TECMARKET-QA',type:'collection',ignore:['node_modules','.git']},null,2));
for(const a of artifacts)write(path.join(collection,a.file),a.content);
for(const folder of [...new Set(artifacts.map(a=>a.file.split('/')[0]))])write(path.join(collection,folder,'folder.bru'),`meta {\n  name: ${folder}\n  seq: ${Number(folder.slice(0,2))}\n}`);
// Never overwrite a user's configured environment.
const env=path.join(collection,'environments','Development.bru');
const environmentTemplate=`vars {\n  baseUrl: https://api-dev.juanodsk.dev\n  runId: \n  origin: \n${[...vars.keys()].filter(k=>!['baseUrl','runId','origin'].includes(k)).sort().map(k=>'  '+k+': ').join('\n')}\n}`;
if(!fs.existsSync(env))write(env,environmentTemplate);
write(path.join(collection,'environments','Development.example.bru'),environmentTemplate);
write(path.join(docs,'variables-generadas.md'),'# Variables detectadas\n\nNo rellenar secretos en este documento. Usar el entorno local de Bruno. IDs globales se resuelven por código/nombre; IDs de operación se capturan por actor/negocio/sede.\n\n| Variable | Tipo | Origen / requisito |\n|---|---|---|\n'+[...vars].sort().map(([n,v])=>`| ${n} | ${v.type} | ${v.origin} |`).join('\n'));
write(path.join(docs,'cobertura.json'),JSON.stringify({controllers:controllers.length,operations:endpoints.length,cases:cases.length,requests:artifacts.length,liveExecuted:0,status:'NOT_RUN',blockedByEmail:true},null,2));
console.log(JSON.stringify({controllers:controllers.length,operations:endpoints.length,cases:cases.length,collection:rel(collection)},null,2));
