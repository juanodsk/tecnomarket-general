"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import {
  ArrowLeft,
  ArrowRight,
  Ban,
  Boxes,
  Check,
  ChevronDown,
  FileBadge,
  Globe2,
  Layers3,
  LoaderCircle,
  LockKeyhole,
  Map,
  MapPin,
  Plus,
  RefreshCw,
  Save,
  Search,
  X,
} from "lucide-react";
import Link from "next/link";
import { useCallback, useEffect, useMemo, useRef, useState, type ReactNode } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";

import { PlatformAccess } from "@/components/platform/platform-workspace";
import { EmptyState, ErrorState, FormAlert } from "@/components/ui/feedback";
import * as parameterApi from "@/features/platform-parameters/api";
import { normalizeApiError } from "@/lib/api/errors";
import { hasPermission } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";
import type { PlatformPermission } from "@/types/access-control";
import type { AppModule, City, Country, Department, DocumentType } from "@/types/platform-parameters";

type ParameterView = "countries" | "departments" | "cities" | "documents" | "system";
type EditableKind = "country" | "department" | "city" | "document" | "module";
type EditorSelection = { kind: EditableKind; id: number | "new" };

interface Catalogs {
  countries: Country[];
  departments: Department[];
  cities: City[];
  documents: DocumentType[];
  modules: AppModule[];
  permissions: PlatformPermission[];
}

interface EditorValues {
  code: string;
  name: string;
  description: string;
  countryId: string;
  departmentId: string;
  appliesToPerson: boolean;
  appliesToCompany: boolean;
  active: boolean;
}

const emptyCatalogs: Catalogs = {
  countries: [], departments: [], cities: [], documents: [], modules: [], permissions: [],
};

const emptyEditorValues: EditorValues = {
  code: "", name: "", description: "", countryId: "", departmentId: "",
  appliesToPerson: true, appliesToCompany: false, active: true,
};

const kindCopy: Record<EditableKind, { singular: string; plural: string; article: "el" | "la"; newLabel: string }> = {
  country: { singular: "país", plural: "países", article: "el", newLabel: "Nuevo país" },
  department: { singular: "departamento", plural: "departamentos", article: "el", newLabel: "Nuevo departamento" },
  city: { singular: "ciudad", plural: "ciudades", article: "la", newLabel: "Nueva ciudad" },
  document: { singular: "tipo de documento", plural: "tipos de documento", article: "el", newLabel: "Nuevo tipo" },
  module: { singular: "módulo", plural: "módulos", article: "el", newLabel: "Nuevo módulo" },
};

const editorSchema = z.object({
  code: z.string(),
  name: z.string(),
  description: z.string(),
  countryId: z.string(),
  departmentId: z.string(),
  appliesToPerson: z.boolean(),
  appliesToCompany: z.boolean(),
  active: z.boolean(),
});

function schemaFor(kind: EditableKind, isNew: boolean) {
  return editorSchema.superRefine((values, context) => {
    const nameLimits: Record<EditableKind, number> = { country: 100, department: 120, city: 120, document: 50, module: 100 };
    const codeLimits: Partial<Record<EditableKind, number>> = { country: 10, department: 20, document: 15, module: 50 };
    if (!values.name.trim()) context.addIssue({ code: "custom", path: ["name"], message: "Escribe un nombre." });
    else if (values.name.trim().length > nameLimits[kind]) context.addIssue({ code: "custom", path: ["name"], message: `Usa máximo ${nameLimits[kind]} caracteres.` });
    if (kind !== "city" && (isNew || kind === "department")) {
      if (!values.code.trim()) context.addIssue({ code: "custom", path: ["code"], message: "Escribe un código." });
      else if (values.code.trim().length > (codeLimits[kind] ?? 50)) context.addIssue({ code: "custom", path: ["code"], message: `Usa máximo ${codeLimits[kind]} caracteres.` });
    }
    if (kind === "department" && !Number(values.countryId)) context.addIssue({ code: "custom", path: ["countryId"], message: "Selecciona un país." });
    if (kind === "city" && !Number(values.countryId)) context.addIssue({ code: "custom", path: ["countryId"], message: "Selecciona un país." });
    if (kind === "city" && !Number(values.departmentId)) context.addIssue({ code: "custom", path: ["departmentId"], message: "Selecciona un departamento." });
    if (kind === "document") {
      if (!values.description.trim()) context.addIssue({ code: "custom", path: ["description"], message: "Escribe una descripción." });
      else if (values.description.trim().length > 100) context.addIssue({ code: "custom", path: ["description"], message: "Usa máximo 100 caracteres." });
    }
  });
}

function StatusBadge({ active }: { active: boolean }) {
  return <span className={`tm-business-status ${active ? "tm-business-status--active" : "tm-business-status--inactive"}`}><span aria-hidden="true" />{active ? "Activo" : "Inactivo"}</span>;
}

function useParameterCatalogs() {
  const [data, setData] = useState<Catalogs>(emptyCatalogs);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const sequence = useRef(0);

  const load = useCallback(async () => {
    const request = ++sequence.current;
    setLoading(true);
    setError(null);
    try {
      const [countries, departments, cities, documents, modules, permissions] = await Promise.all([
        parameterApi.getCountries(), parameterApi.getDepartments(), parameterApi.getCities(),
        parameterApi.getDocumentTypes(), parameterApi.getModules(), parameterApi.getPermissions(),
      ]);
      if (request === sequence.current) setData({ countries, departments, cities, documents, modules, permissions });
    } catch (requestError) {
      if (request === sequence.current) setError(normalizeApiError(requestError).message);
    } finally {
      if (request === sequence.current) setLoading(false);
    }
  }, []);

  useEffect(() => {
    void load();
    return () => { sequence.current += 1; };
  }, [load]);

  return { data, loading, error, reload: load };
}

function ParameterRow({ icon, title, subtitle, active, chips, actionLabel, onOpen }: {
  icon: ReactNode;
  title: string;
  subtitle: string;
  active: boolean;
  chips?: string[];
  actionLabel: string;
  onOpen: (trigger: HTMLButtonElement) => void;
}) {
  return (
    <article className="tm-parameter-row">
      <span className="tm-parameter-row__icon">{icon}</span>
      <div className="tm-parameter-row__copy"><h3>{title}</h3><p>{subtitle}</p>{chips?.length ? <div>{chips.map((chip) => <span key={chip}>{chip}</span>)}</div> : null}</div>
      <StatusBadge active={active} />
      <button className="tm-button tm-button--secondary" type="button" onClick={(event) => onOpen(event.currentTarget)}>{actionLabel} <ArrowRight aria-hidden="true" /></button>
    </article>
  );
}

function editorName(selection: EditorSelection) {
  return selection.id === "new" ? `Crear ${kindCopy[selection.kind].singular}` : `Configurar ${kindCopy[selection.kind].singular}`;
}

function ParameterEditor({ selection, catalogs, canManage, onClose, onChanged }: {
  selection: EditorSelection;
  catalogs: Catalogs;
  canManage: boolean;
  onClose: () => void;
  onChanged: (message: string, close?: boolean) => Promise<void>;
}) {
  const isNew = selection.id === "new";
  const [loading, setLoading] = useState(!isNew);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const panelRef = useRef<HTMLElement>(null);
  const closeButtonRef = useRef<HTMLButtonElement>(null);
  const { register, handleSubmit, reset, watch, setValue, formState: { errors } } = useForm<EditorValues>({
    resolver: zodResolver(schemaFor(selection.kind, isNew)),
    defaultValues: emptyEditorValues,
  });
  const selectedCountryId = Number(watch("countryId"));
  const availableDepartments = useMemo(
    () => catalogs.departments.filter((department) => department.countryId === selectedCountryId && department.active),
    [catalogs.departments, selectedCountryId],
  );

  useEffect(() => {
    const previousOverflow = document.body.style.overflow;
    document.body.style.overflow = "hidden";
    closeButtonRef.current?.focus();
    const onKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape" && !saving) { onClose(); return; }
      if (event.key !== "Tab") return;
      const focusable = Array.from(panelRef.current?.querySelectorAll<HTMLElement>("button:not([disabled]), input:not([disabled]), textarea:not([disabled]), select:not([disabled])") ?? []);
      if (!focusable.length) return;
      const first = focusable[0]; const last = focusable[focusable.length - 1];
      if (event.shiftKey && document.activeElement === first) { event.preventDefault(); last.focus(); }
      else if (!event.shiftKey && document.activeElement === last) { event.preventDefault(); first.focus(); }
    };
    window.addEventListener("keydown", onKeyDown);
    return () => { document.body.style.overflow = previousOverflow; window.removeEventListener("keydown", onKeyDown); };
  }, [onClose, saving]);

  useEffect(() => {
    if (selection.id === "new") {
      reset({ ...emptyEditorValues, countryId: catalogs.countries.find((country) => country.active)?.id.toString() ?? "" });
      setLoading(false);
      return;
    }
    let active = true;
    async function loadItem() {
      setLoading(true); setError(null);
      try {
        if (selection.id === "new") return;
        const id = selection.id;
        if (selection.kind === "country") {
          const item = await parameterApi.getCountry(id);
          if (active) reset({ ...emptyEditorValues, code: item.code, name: item.name, active: item.active });
        } else if (selection.kind === "department") {
          const item = await parameterApi.getDepartment(id);
          if (active) reset({ ...emptyEditorValues, code: item.code, name: item.name, countryId: String(item.countryId), active: item.active });
        } else if (selection.kind === "city") {
          const item = await parameterApi.getCity(id);
          if (active) reset({ ...emptyEditorValues, name: item.name, countryId: String(item.countryId), departmentId: String(item.departmentId), active: item.active });
        } else if (selection.kind === "document") {
          const item = await parameterApi.getDocumentType(id);
          if (active) reset({ ...emptyEditorValues, code: item.code, name: item.name, description: item.description, appliesToPerson: item.appliesToPerson, appliesToCompany: item.appliesToCompany, active: item.active });
        } else {
          const item = await parameterApi.getModule(id);
          if (active) reset({ ...emptyEditorValues, code: item.code, name: item.name, active: item.active });
        }
      } catch (requestError) {
        if (active) setError(normalizeApiError(requestError).message);
      } finally { if (active) setLoading(false); }
    }
    void loadItem();
    return () => { active = false; };
  }, [catalogs.countries, reset, selection]);

  async function submit(values: EditorValues) {
    setSaving(true); setError(null);
    const code = values.code.trim().toUpperCase();
    const name = values.name.trim();
    try {
      if (selection.kind === "country") {
        if (selection.id === "new") await parameterApi.createCountry({ code, name });
        else await parameterApi.updateCountry(selection.id, { name, active: values.active });
      } else if (selection.kind === "department") {
        const request = { countryId: Number(values.countryId), code, name };
        if (selection.id === "new") await parameterApi.createDepartment(request);
        else await parameterApi.updateDepartment(selection.id, { ...request, active: values.active });
      } else if (selection.kind === "city") {
        const request = { departmentId: Number(values.departmentId), name };
        if (selection.id === "new") await parameterApi.createCity(request);
        else await parameterApi.updateCity(selection.id, { ...request, active: values.active });
      } else if (selection.kind === "document") {
        const request = { name, description: values.description.trim(), appliesToPerson: values.appliesToPerson, appliesToCompany: values.appliesToCompany };
        if (selection.id === "new") await parameterApi.createDocumentType({ code, ...request });
        else await parameterApi.updateDocumentType(selection.id, { ...request, active: values.active });
      } else {
        if (selection.id === "new") await parameterApi.createModule({ code, name });
        else await parameterApi.updateModule(selection.id, { name, active: values.active });
      }
      await onChanged(`${isNew ? "Se creó" : "Se actualizó"} ${kindCopy[selection.kind].article} ${kindCopy[selection.kind].singular} ${name}.`, true);
    } catch (requestError) {
      setError(normalizeApiError(requestError).message);
      setSaving(false);
    }
  }

  async function removeItem() {
    if (selection.id === "new") return;
    setSaving(true); setError(null);
    try {
      if (selection.kind === "country") await parameterApi.deleteCountry(selection.id);
      else if (selection.kind === "department") await parameterApi.deleteDepartment(selection.id);
      else if (selection.kind === "city") await parameterApi.deleteCity(selection.id);
      else if (selection.kind === "document") await parameterApi.deleteDocumentType(selection.id);
      else await parameterApi.deleteModule(selection.id);
      await onChanged(`Se desactivó ${kindCopy[selection.kind].article} ${kindCopy[selection.kind].singular}.`, true);
    } catch (requestError) { setError(normalizeApiError(requestError).message); setSaving(false); }
  }

  const showCode = selection.kind !== "city";
  const codeEditable = isNew || selection.kind === "department";
  const activeCountries = catalogs.countries.filter((country) => country.active || country.id === selectedCountryId);

  return (
    <div className="tm-review-backdrop">
      <section ref={panelRef} className="tm-review-panel tm-parameter-editor" role="dialog" aria-modal="true" aria-labelledby="parameter-editor-title">
        <header className="tm-review-panel__head">
          <div><span>Configuración global</span><h2 id="parameter-editor-title">{editorName(selection)}</h2><p>{isNew ? "Completa la información requerida por el sistema." : "Los cambios afectarán los formularios que utilizan este catálogo."}</p></div>
          <button ref={closeButtonRef} className="tm-icon-button" type="button" aria-label="Cerrar configuración" disabled={saving} onClick={onClose}><X aria-hidden="true" /></button>
        </header>
        <div className="tm-review-panel__body">
          {loading ? <div className="tm-business-detail-loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando información</div> : null}
          {error ? <FormAlert>{error}</FormAlert> : null}
          {!loading ? (
            <form className="tm-parameter-form" onSubmit={handleSubmit(submit)} noValidate>
              <div className="tm-access-form-grid">
                {showCode ? <label className="tm-access-field"><span>Código interno</span><input {...register("code")} readOnly={!codeEditable || !canManage} maxLength={selection.kind === "country" ? 10 : selection.kind === "document" ? 15 : selection.kind === "department" ? 20 : 50} autoComplete="off" onInput={(event) => { event.currentTarget.value = event.currentTarget.value.toUpperCase(); }} />{errors.code ? <small role="alert">{errors.code.message}</small> : !codeEditable ? <small>El backend conserva este identificador.</small> : null}</label> : null}
                {(selection.kind === "department" || selection.kind === "city") ? <label className="tm-access-field"><span>País</span><span className="tm-access-select"><select {...register("countryId")} disabled={!canManage} onChange={(event) => { setValue("countryId", event.target.value, { shouldValidate: true }); if (selection.kind === "city") setValue("departmentId", ""); }}><option value="">Selecciona un país</option>{activeCountries.map((country) => <option key={country.id} value={country.id}>{country.name}</option>)}</select><ChevronDown aria-hidden="true" /></span>{errors.countryId ? <small role="alert">{errors.countryId.message}</small> : null}</label> : null}
                {selection.kind === "city" ? <label className="tm-access-field"><span>Departamento</span><span className="tm-access-select"><select {...register("departmentId")} disabled={!canManage || !selectedCountryId}><option value="">Selecciona un departamento</option>{availableDepartments.map((department) => <option key={department.id} value={department.id}>{department.name}</option>)}</select><ChevronDown aria-hidden="true" /></span>{errors.departmentId ? <small role="alert">{errors.departmentId.message}</small> : null}</label> : null}
                <label className={`tm-access-field ${showCode && selection.kind !== "city" ? "" : "tm-access-field--wide"}`}><span>Nombre visible</span><input {...register("name")} readOnly={!canManage} maxLength={selection.kind === "document" ? 50 : selection.kind === "country" || selection.kind === "module" ? 100 : 120} autoComplete="off" />{errors.name ? <small role="alert">{errors.name.message}</small> : null}</label>
                {selection.kind === "document" ? <label className="tm-access-field tm-access-field--wide"><span>Descripción funcional</span><textarea {...register("description")} readOnly={!canManage} maxLength={100} rows={3} />{errors.description ? <small role="alert">{errors.description.message}</small> : null}</label> : null}
              </div>
              {selection.kind === "document" ? <fieldset className="tm-parameter-applicability"><legend>Disponible para</legend><label><input type="checkbox" {...register("appliesToPerson")} disabled={!canManage} /><span>{watch("appliesToPerson") ? <Check aria-hidden="true" /> : null}</span> Personas naturales</label><label><input type="checkbox" {...register("appliesToCompany")} disabled={!canManage} /><span>{watch("appliesToCompany") ? <Check aria-hidden="true" /> : null}</span> Empresas</label></fieldset> : null}
              {!isNew ? <label className="tm-parameter-active-toggle"><input type="checkbox" {...register("active")} disabled={!canManage} /><span>{watch("active") ? <Check aria-hidden="true" /> : null}</span><span><strong>Disponible en formularios</strong><small>Desmárcalo para ocultarlo sin eliminar su historial.</small></span></label> : null}
              {canManage ? <div className="tm-access-form-actions"><button className="tm-button tm-button--primary" type="submit" disabled={saving}>{saving ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Save aria-hidden="true" />} {isNew ? "Crear" : "Guardar cambios"}</button></div> : null}
            </form>
          ) : null}
          {!isNew && canManage && !loading ? <section className="tm-access-danger-zone"><div><h3>Retirar del catálogo</h3><p>El registro dejará de aparecer en nuevas selecciones. Los datos históricos se conservarán.</p></div>{!confirmDelete ? <button className="tm-button tm-button--danger" type="button" onClick={() => setConfirmDelete(true)}><Ban aria-hidden="true" /> Desactivar</button> : <div className="tm-access-confirm"><p>¿Confirmas esta desactivación?</p><div><button className="tm-button tm-button--secondary" type="button" disabled={saving} onClick={() => setConfirmDelete(false)}>Cancelar</button><button className="tm-button tm-button--danger" type="button" disabled={saving} onClick={() => void removeItem()}>{saving ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Ban aria-hidden="true" />} Confirmar</button></div></div>}</section> : null}
        </div>
      </section>
    </div>
  );
}

function SystemView({ modules, permissions, canManage, onOpen }: { modules: AppModule[]; permissions: PlatformPermission[]; canManage: boolean; onOpen: (selection: EditorSelection, trigger: HTMLButtonElement) => void }) {
  if (!modules.length) return <EmptyState title="No hay módulos registrados" message="Los módulos del sistema aparecerán aquí." />;
  return <div className="tm-system-catalog">{modules.map((module) => {
    const modulePermissions = permissions.filter((permission) => permission.moduleId === module.id);
    return <section key={module.id}><header><span><Layers3 aria-hidden="true" /></span><div><h3>{module.name}</h3><p>{modulePermissions.length} {modulePermissions.length === 1 ? "permiso asociado" : "permisos asociados"}</p></div><StatusBadge active={module.active} /><button className="tm-button tm-button--secondary" type="button" onClick={(event) => onOpen({ kind: "module", id: module.id }, event.currentTarget)}>{canManage ? "Configurar" : "Consultar"} <ArrowRight aria-hidden="true" /></button></header>{modulePermissions.length ? <div className="tm-system-permissions">{modulePermissions.map((permission) => <article key={permission.id}><LockKeyhole aria-hidden="true" /><div><h4>{permission.name}</h4><p>{permission.description}</p></div><StatusBadge active={permission.active} /></article>)}</div> : <p className="tm-system-catalog__empty">Este módulo todavía no agrupa permisos.</p>}</section>;
  })}<footer className="tm-access-catalog-note"><LockKeyhole aria-hidden="true" /><p><strong>Permisos protegidos</strong><span>Se consultan aquí y se asignan desde Roles y permisos. Su creación directa permanecerá bloqueada hasta que el backend reciba y valide el alcance.</span></p></footer></div>;
}

export function PlatformParameters() {
  const { access } = useAuth();
  const [view, setView] = useState<ParameterView>("countries");
  const [query, setQuery] = useState("");
  const [selection, setSelection] = useState<EditorSelection | null>(null);
  const [feedback, setFeedback] = useState<string | null>(null);
  const triggerRef = useRef<HTMLButtonElement | null>(null);
  const { data, loading, error, reload } = useParameterCatalogs();
  const canManage = hasPermission(access?.permissions ?? [], "PARAMETER_MANAGE");
  const normalizedQuery = query.trim().toLocaleLowerCase("es");
  const includesQuery = (...values: Array<string | undefined>) => !normalizedQuery || values.some((value) => value?.toLocaleLowerCase("es").includes(normalizedQuery));
  const filtered = {
    countries: data.countries.filter((item) => includesQuery(item.name, item.code)),
    departments: data.departments.filter((item) => includesQuery(item.name, item.code, item.countryName)),
    cities: data.cities.filter((item) => includesQuery(item.name, item.departmentName, item.countryName)),
    documents: data.documents.filter((item) => includesQuery(item.name, item.code, item.description)),
    modules: data.modules.filter((item) => includesQuery(item.name, item.code) || data.permissions.some((permission) => permission.moduleId === item.id && includesQuery(permission.name, permission.description))),
  };
  const tabs: Array<{ id: ParameterView; label: string; icon: typeof Globe2; count: number }> = [
    { id: "countries", label: "Países", icon: Globe2, count: data.countries.length },
    { id: "departments", label: "Departamentos", icon: Map, count: data.departments.length },
    { id: "cities", label: "Ciudades", icon: MapPin, count: data.cities.length },
    { id: "documents", label: "Documentos", icon: FileBadge, count: data.documents.length },
    { id: "system", label: "Sistema", icon: Boxes, count: data.modules.length },
  ];
  const viewKind: Record<ParameterView, EditableKind> = { countries: "country", departments: "department", cities: "city", documents: "document", system: "module" };

  const closeEditor = useCallback(() => { setSelection(null); requestAnimationFrame(() => triggerRef.current?.focus()); }, []);
  function openEditor(next: EditorSelection, trigger: HTMLButtonElement) { triggerRef.current = trigger; setFeedback(null); setSelection(next); }
  async function handleChanged(message: string, close = false) { setFeedback(message); if (close) setSelection(null); await reload(); }
  const currentKind = viewKind[view];
  const currentLabel = kindCopy[currentKind];

  return (
    <PlatformAccess permission="PARAMETER_VIEW">
      <div className="tm-page-stack">
        <header className="tm-workspace-heading"><div><h1>Parámetros globales</h1><p>Administra los catálogos compartidos que organizan registros, ubicaciones y accesos.</p></div><Link className="tm-button tm-button--secondary" href="/plataforma"><ArrowLeft aria-hidden="true" /> Volver al resumen</Link></header>
        {feedback ? <FormAlert tone="success">{feedback}</FormAlert> : null}
        {error && Object.values(data).some((items) => items.length) ? <FormAlert>{error}</FormAlert> : null}
        <section className="tm-parameter-workspace">
          <div className="tm-parameter-tabs" role="tablist" aria-label="Catálogos globales">{tabs.map(({ id, label, icon: Icon, count }) => <button key={id} type="button" role="tab" aria-selected={view === id} className={view === id ? "is-active" : ""} onClick={() => { setView(id); setQuery(""); }}><Icon aria-hidden="true" /><span>{label}</span><small>{count}</small></button>)}</div>
          <header className="tm-parameter-toolbar"><label><Search aria-hidden="true" /><span className="sr-only">Buscar en {currentLabel.plural}</span><input value={query} onChange={(event) => setQuery(event.target.value)} placeholder={`Buscar en ${currentLabel.plural}`} /></label><div><button className="tm-button tm-button--secondary" type="button" disabled={loading} onClick={() => void reload()}><RefreshCw className={loading ? "tm-spinner" : ""} aria-hidden="true" /> Actualizar</button>{canManage ? <button className="tm-button tm-button--primary" type="button" onClick={(event) => openEditor({ kind: currentKind, id: "new" }, event.currentTarget)}><Plus aria-hidden="true" /> {currentLabel.newLabel}</button> : null}</div></header>
          {loading && !Object.values(data).some((items) => items.length) ? <div className="tm-request-board__loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando parámetros</div> : null}
          {error && !Object.values(data).some((items) => items.length) ? <ErrorState message={error} onRetry={() => void reload()} /> : null}
          {!loading || Object.values(data).some((items) => items.length) ? <div role="tabpanel" className="tm-parameter-content">
            {view === "countries" ? filtered.countries.length ? filtered.countries.map((item) => <ParameterRow key={item.id} icon={<Globe2 aria-hidden="true" />} title={item.name} subtitle={`Código ${item.code}`} active={item.active} actionLabel={canManage ? "Configurar" : "Consultar"} onOpen={(trigger) => openEditor({ kind: "country", id: item.id }, trigger)} />) : <EmptyState title="No encontramos países" message="Ajusta la búsqueda o registra un nuevo país." /> : null}
            {view === "departments" ? filtered.departments.length ? filtered.departments.map((item) => <ParameterRow key={item.id} icon={<Map aria-hidden="true" />} title={item.name} subtitle={`${item.countryName} · Código ${item.code}`} active={item.active} actionLabel={canManage ? "Configurar" : "Consultar"} onOpen={(trigger) => openEditor({ kind: "department", id: item.id }, trigger)} />) : <EmptyState title="No encontramos departamentos" message="Ajusta la búsqueda o registra un nuevo departamento." /> : null}
            {view === "cities" ? filtered.cities.length ? filtered.cities.map((item) => <ParameterRow key={item.id} icon={<MapPin aria-hidden="true" />} title={item.name} subtitle={`${item.departmentName} · ${item.countryName}`} active={item.active} actionLabel={canManage ? "Configurar" : "Consultar"} onOpen={(trigger) => openEditor({ kind: "city", id: item.id }, trigger)} />) : <EmptyState title="No encontramos ciudades" message="Ajusta la búsqueda o registra una nueva ciudad." /> : null}
            {view === "documents" ? filtered.documents.length ? filtered.documents.map((item) => <ParameterRow key={item.id} icon={<FileBadge aria-hidden="true" />} title={item.name} subtitle={item.description} chips={[item.code, ...(item.appliesToPerson ? ["Personas"] : []), ...(item.appliesToCompany ? ["Empresas"] : [])]} active={item.active} actionLabel={canManage ? "Configurar" : "Consultar"} onOpen={(trigger) => openEditor({ kind: "document", id: item.id }, trigger)} />) : <EmptyState title="No encontramos tipos de documento" message="Ajusta la búsqueda o registra un nuevo tipo." /> : null}
            {view === "system" ? <SystemView modules={filtered.modules} permissions={data.permissions} canManage={canManage} onOpen={openEditor} /> : null}
          </div> : null}
        </section>
      </div>
      {selection ? <ParameterEditor selection={selection} catalogs={data} canManage={canManage} onClose={closeEditor} onChanged={handleChanged} /> : null}
    </PlatformAccess>
  );
}
