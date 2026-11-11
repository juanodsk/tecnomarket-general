"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import {
  Check,
  ChevronDown,
  CircleAlert,
  ClipboardCheck,
  LoaderCircle,
  Pencil,
  Plus,
  RefreshCw,
  ShieldCheck,
  Smartphone,
  Trash2,
  X,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { useForm } from "react-hook-form";

import { EmptyState, ErrorState, FormAlert, FullPageLoader } from "@/components/ui/feedback";
import { SelectField, TextField } from "@/components/ui/form-controls";
import * as qualityTestsApi from "@/features/business-quality-tests/api";
import {
  businessQualityTestSchema,
  hasDuplicateQualityTestCode,
  normalizeQualityTestCode,
  type BusinessQualityTestFormValues,
} from "@/features/business-quality-tests/schemas";
import { ApiClientError, normalizeApiError } from "@/lib/api/errors";
import { hasPermission } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";
import type { BusinessQualityTest } from "@/types/business-quality-test";
import type { BusinessCatalogItem } from "@/types/business-service";

type Notice = { tone: "success" | "warning"; message: string };

function testValues(test: BusinessQualityTest | null, initialDeviceTypeId?: number): BusinessQualityTestFormValues {
  return {
    deviceTypeId: test ? String(test.deviceTypeId) : initialDeviceTypeId ? String(initialDeviceTypeId) : "",
    code: test?.code ?? "",
    name: test?.name ?? "",
    description: test?.description ?? "",
    sortOrder: test ? String(test.sortOrder) : "1",
    required: test?.required ?? false,
  };
}

function QualityTestEditor({
  businessId,
  test,
  tests,
  deviceTypes,
  initialDeviceTypeId,
  onClose,
  onSaved,
}: {
  businessId: number;
  test: BusinessQualityTest | null;
  tests: BusinessQualityTest[];
  deviceTypes: BusinessCatalogItem[];
  initialDeviceTypeId?: number;
  onClose: () => void;
  onSaved: (notice: Notice) => Promise<void>;
}) {
  const panelRef = useRef<HTMLElement>(null);
  const closeRef = useRef<HTMLButtonElement>(null);
  const keepRef = useRef<HTMLButtonElement>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const isEditing = Boolean(test);
  const {
    register,
    handleSubmit,
    setError,
    watch,
    formState: { errors, isSubmitting },
  } = useForm<BusinessQualityTestFormValues>({
    resolver: zodResolver(businessQualityTestSchema),
    defaultValues: testValues(test, initialDeviceTypeId),
  });
  const required = watch("required");

  useEffect(() => {
    closeRef.current?.focus();
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape" && !isSubmitting && !deleting) onClose();
      if (event.key !== "Tab") return;
      const focusable = Array.from(
        panelRef.current?.querySelectorAll<HTMLElement>(
          "button:not([disabled]), input:not([disabled]), select:not([disabled]), textarea:not([disabled])",
        ) ?? [],
      );
      if (!focusable.length) return;
      const first = focusable[0];
      const last = focusable[focusable.length - 1];
      if (event.shiftKey && document.activeElement === first) {
        event.preventDefault();
        last.focus();
      } else if (!event.shiftKey && document.activeElement === last) {
        event.preventDefault();
        first.focus();
      }
    };
    window.addEventListener("keydown", handleKeyDown);
    return () => window.removeEventListener("keydown", handleKeyDown);
  }, [deleting, isSubmitting, onClose]);

  useEffect(() => {
    if (confirmDelete) keepRef.current?.focus();
  }, [confirmDelete]);

  async function submit(values: BusinessQualityTestFormValues) {
    setSubmitError(null);
    const deviceTypeId = Number(values.deviceTypeId);
    if (hasDuplicateQualityTestCode(tests, deviceTypeId, values.code, test?.id)) {
      setError("code", { message: "Este código ya existe para el dispositivo seleccionado." });
      return;
    }

    const request = {
      deviceTypeId,
      code: normalizeQualityTestCode(values.code),
      name: values.name.trim(),
      description: values.description.trim(),
      required: values.required,
      sortOrder: Number(values.sortOrder),
    };

    try {
      if (test) {
        await qualityTestsApi.updateBusinessQualityTest(businessId, test.id, {
          ...request,
          active: test.active,
        });
      } else {
        await qualityTestsApi.createBusinessQualityTest(businessId, request);
      }
      await onSaved({
        tone: "success",
        message: test
          ? "La prueba de recepción quedó actualizada."
          : "La prueba ya forma parte del checklist del negocio.",
      });
      onClose();
    } catch (error) {
      setSubmitError(normalizeApiError(error).message);
    }
  }

  async function removeTest() {
    if (!test) return;
    setDeleting(true);
    setSubmitError(null);
    try {
      await qualityTestsApi.deleteBusinessQualityTest(businessId, test.id);
      await onSaved({ tone: "success", message: "La prueba fue desactivada y ya no aparecerá en nuevos checklists." });
      onClose();
    } catch (error) {
      setSubmitError(normalizeApiError(error).message);
      setConfirmDelete(false);
    } finally {
      setDeleting(false);
    }
  }

  return (
    <div className="tm-review-backdrop">
      <section
        ref={panelRef}
        className="tm-review-panel tm-quality-editor"
        role="dialog"
        aria-modal="true"
        aria-labelledby="quality-editor-title"
      >
        <header className="tm-review-panel__head">
          <div>
            <h2 id="quality-editor-title">{isEditing ? "Editar prueba" : "Crear prueba de recepción"}</h2>
            <p>Define una comprobación reusable para el ingreso de equipos.</p>
          </div>
          <button ref={closeRef} className="tm-icon-button" type="button" aria-label="Cerrar" onClick={onClose} disabled={isSubmitting || deleting}>
            <X aria-hidden="true" />
          </button>
        </header>

        <form className="tm-review-panel__body tm-quality-form" onSubmit={handleSubmit(submit)}>
          {submitError ? <FormAlert>{submitError}</FormAlert> : null}

          <fieldset>
            <legend>Clasificación</legend>
            <div className="tm-quality-form__grid">
              <SelectField label="Tipo de dispositivo" error={errors.deviceTypeId?.message} {...register("deviceTypeId")}>
                <option value="">Selecciona un dispositivo</option>
                {deviceTypes.map((deviceType) => <option key={deviceType.id} value={deviceType.id}>{deviceType.name}</option>)}
              </SelectField>
              <TextField label="Orden en el checklist" inputMode="numeric" min={1} error={errors.sortOrder?.message} hint="Empieza en 1." {...register("sortOrder")} />
            </div>
          </fieldset>

          <fieldset>
            <legend>Comprobación</legend>
            <div className="tm-quality-form__grid">
              <TextField className="tm-field--span" label="Nombre de la prueba" maxLength={50} autoComplete="off" placeholder="Pantalla funciona" error={errors.name?.message} {...register("name")} />
              <TextField className="tm-field--span" label="Código interno" maxLength={30} autoCapitalize="characters" autoComplete="off" placeholder="SCREEN_OK" error={errors.code?.message} hint="Se guardará en mayúsculas y debe ser único para este dispositivo." {...register("code")} />
              <div className="tm-field tm-field--span">
                <label htmlFor="quality-description">Descripción para el técnico</label>
                <textarea id="quality-description" maxLength={200} rows={4} aria-invalid={Boolean(errors.description)} aria-describedby={errors.description ? "quality-description-error" : "quality-description-hint"} {...register("description")} />
                {errors.description ? <p className="tm-field__error" id="quality-description-error" role="alert">{errors.description.message}</p> : <p className="tm-field__hint" id="quality-description-hint">Indica exactamente qué debe revisar al recibir el equipo.</p>}
              </div>
            </div>
          </fieldset>

          <label className={`tm-quality-required ${required ? "is-selected" : ""}`}>
            <input type="checkbox" {...register("required")} />
            <span>{required ? <Check aria-hidden="true" /> : null}</span>
            <span><strong>Prueba obligatoria</strong><small>El técnico deberá registrar un resultado cuando este checklist se use en una orden.</small></span>
          </label>

          {test ? (
            <section className="tm-quality-danger">
              <div><h3>Desactivar prueba</h3><p>No se ofrecerá en nuevos checklists; los registros históricos se conservan.</p></div>
              {confirmDelete ? (
                <div className="tm-quality-danger__confirm">
                  <p>La prueba dejará de estar disponible inmediatamente.</p>
                  <div>
                    <button ref={keepRef} className="tm-button tm-button--secondary" type="button" onClick={() => setConfirmDelete(false)} disabled={deleting}>Conservar prueba</button>
                    <button className="tm-button tm-button--danger" type="button" onClick={() => void removeTest()} disabled={deleting}>{deleting ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Trash2 aria-hidden="true" />} Desactivar</button>
                  </div>
                </div>
              ) : <button className="tm-button tm-button--danger" type="button" onClick={() => setConfirmDelete(true)}>Desactivar</button>}
            </section>
          ) : null}

          {!confirmDelete ? (
            <footer className="tm-quality-form__actions">
              <button className="tm-button tm-button--secondary" type="button" onClick={onClose} disabled={isSubmitting || deleting}>Cancelar</button>
              <button className="tm-button tm-button--primary" type="submit" disabled={isSubmitting || deleting}>{isSubmitting ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : isEditing ? <Check aria-hidden="true" /> : <Plus aria-hidden="true" />}{isSubmitting ? "Guardando…" : isEditing ? "Guardar cambios" : "Crear prueba"}</button>
            </footer>
          ) : null}
        </form>
      </section>
    </div>
  );
}

function QualityTestRow({
  test,
  canUpdate,
  onEdit,
}: {
  test: BusinessQualityTest;
  canUpdate: boolean;
  onEdit: (trigger: HTMLButtonElement) => void;
}) {
  return (
    <article>
      <div className="tm-quality-row__order" aria-label={`Orden ${test.sortOrder}`}>{test.sortOrder}</div>
      <div className="tm-quality-row__identity">
        <div><h3>{test.name}</h3><span>{test.required ? <ShieldCheck aria-hidden="true" /> : null}{test.required ? "Obligatoria" : "Opcional"}</span></div>
        <p>{test.description}</p>
      </div>
      <div className="tm-quality-row__code"><small>Código interno</small><strong>{test.code}</strong></div>
      {canUpdate ? <button className="tm-button tm-button--secondary" type="button" onClick={(event) => onEdit(event.currentTarget)}><Pencil aria-hidden="true" /> Editar</button> : null}
    </article>
  );
}

export function BusinessQualityTestsWorkspace({ businessId }: { businessId: number }) {
  const { businesses, businessContext, selectBusiness } = useAuth();
  const router = useRouter();
  const [tests, setTests] = useState<BusinessQualityTest[]>([]);
  const [deviceTypes, setDeviceTypes] = useState<BusinessCatalogItem[]>([]);
  const [selectedDeviceType, setSelectedDeviceType] = useState("all");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<ApiClientError | null>(null);
  const [catalogError, setCatalogError] = useState<string | null>(null);
  const [notice, setNotice] = useState<Notice | null>(null);
  const [editor, setEditor] = useState<{ test: BusinessQualityTest | null } | null>(null);
  const editorTriggerRef = useRef<HTMLButtonElement | null>(null);
  const exists = businesses.some((business) => business.id === businessId);
  const contextReady = businessContext?.businessId === businessId;
  const permissions = contextReady ? businessContext.permissions : [];
  const canView = hasPermission(permissions, "BUSINESS_QUALITY_TEST_VIEW");
  const canCreate = hasPermission(permissions, "BUSINESS_QUALITY_TEST_CREATE");
  const canUpdate = hasPermission(permissions, "BUSINESS_QUALITY_TEST_UPDATE");
  const canViewCatalogs = hasPermission(permissions, "BUSINESS_CATALOG_VIEW");
  const activeDeviceTypes = useMemo(() => deviceTypes.filter((item) => item.active), [deviceTypes]);
  const editorReady = activeDeviceTypes.length > 0;
  const availableDeviceTypes = useMemo(() => {
    const byId = new Map<number, string>();
    tests.forEach((test) => byId.set(test.deviceTypeId, test.deviceTypeName));
    activeDeviceTypes.forEach((deviceType) => byId.set(deviceType.id, deviceType.name));
    return Array.from(byId, ([id, name]) => ({ id, name })).sort((a, b) => a.name.localeCompare(b.name, "es"));
  }, [activeDeviceTypes, tests]);
  const visibleTests = useMemo(
    () => tests
      .filter((test) => test.active && (selectedDeviceType === "all" || test.deviceTypeId === Number(selectedDeviceType)))
      .sort((a, b) => a.deviceTypeName.localeCompare(b.deviceTypeName, "es") || a.sortOrder - b.sortOrder || a.name.localeCompare(b.name, "es")),
    [selectedDeviceType, tests],
  );
  const groupedTests = useMemo(() => {
    const groups = new Map<number, { id: number; name: string; tests: BusinessQualityTest[] }>();
    visibleTests.forEach((test) => {
      const group = groups.get(test.deviceTypeId) ?? { id: test.deviceTypeId, name: test.deviceTypeName, tests: [] };
      group.tests.push(test);
      groups.set(test.deviceTypeId, group);
    });
    return Array.from(groups.values());
  }, [visibleTests]);

  const handleContextError = useCallback((requestError: unknown) => {
    const normalized = normalizeApiError(requestError);
    if (normalized.status === 403) router.replace("/acceso-denegado");
    else setError(normalized);
  }, [router]);

  useEffect(() => {
    if (!exists || contextReady) return;
    let active = true;
    selectBusiness(businessId).catch((requestError) => {
      if (active) handleContextError(requestError);
    });
    return () => { active = false; };
  }, [businessId, contextReady, exists, handleContextError, selectBusiness]);

  const loadTests = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      setTests(await qualityTestsApi.getAllBusinessQualityTests(businessId));
    } catch (requestError) {
      const normalized = normalizeApiError(requestError);
      if (normalized.status === 403) router.replace("/acceso-denegado");
      else setError(normalized);
    } finally {
      setLoading(false);
    }
  }, [businessId, router]);

  const loadDeviceTypes = useCallback(async () => {
    if (!canViewCatalogs) {
      setCatalogError("Tu rol no permite consultar los tipos de dispositivo necesarios para editar pruebas.");
      return;
    }
    setCatalogError(null);
    try {
      const available = await qualityTestsApi.getDeviceTypes(businessId);
      setDeviceTypes(available);
      if (!available.some((item) => item.active)) {
        setCatalogError("Configura al menos un tipo de dispositivo antes de crear pruebas de recepción.");
      }
    } catch (requestError) {
      setCatalogError(normalizeApiError(requestError).message);
    }
  }, [businessId, canViewCatalogs]);

  useEffect(() => {
    if (!contextReady) return;
    if (!canView) {
      router.replace("/acceso-denegado");
      return;
    }
    void loadTests();
    if (canCreate || canUpdate) void loadDeviceTypes();
  }, [canCreate, canUpdate, canView, contextReady, loadDeviceTypes, loadTests, router]);

  const handleSaved = useCallback(async (nextNotice: Notice) => {
    setTests(await qualityTestsApi.getAllBusinessQualityTests(businessId));
    setNotice(nextNotice);
  }, [businessId]);

  const closeEditor = useCallback(() => {
    setEditor(null);
    requestAnimationFrame(() => editorTriggerRef.current?.focus());
  }, []);

  const openEditor = (trigger: HTMLButtonElement, test: BusinessQualityTest | null) => {
    editorTriggerRef.current = trigger;
    setNotice(null);
    setEditor({ test });
  };

  if (!exists) return <div className="tm-page-stack"><ErrorState title="Negocio no disponible" message="Este negocio no está asociado a tu cuenta o ya no está disponible." /></div>;
  if (error && !contextReady) return <div className="tm-page-stack"><ErrorState message={error.message} onRetry={() => void selectBusiness(businessId).catch(handleContextError)} /></div>;
  if (!contextReady || !canView) return <FullPageLoader label="Preparando las pruebas de recepción" />;

  return (
    <div className="tm-page-stack tm-quality-page">
      <header className="tm-workspace-heading tm-quality-heading">
        <div><h1>Pruebas de recepción</h1><p>Configura qué debe comprobar el equipo al ingresar a {businessContext.businessName}.</p></div>
        {canCreate ? <button className="tm-button tm-button--primary" type="button" disabled={!editorReady} title={!editorReady ? "Faltan tipos de dispositivo disponibles" : undefined} onClick={(event) => openEditor(event.currentTarget, null)}><Plus aria-hidden="true" /> Nueva prueba</button> : null}
      </header>

      {notice ? <FormAlert tone={notice.tone}>{notice.message}</FormAlert> : null}
      {catalogError && (canCreate || canUpdate) ? <FormAlert tone="warning">{catalogError}</FormAlert> : null}
      {error ? <ErrorState message={error.message} onRetry={() => void loadTests()} /> : null}

      {!error ? (
        <section className="tm-quality-board">
          <header className="tm-quality-board__head">
            <div className="tm-quality-board__identity"><span><ClipboardCheck aria-hidden="true" /></span><div><h2>Checklist del negocio</h2><p>Ordenado por dispositivo y secuencia de revisión.</p></div></div>
            {!loading ? <div className="tm-quality-board__summary"><small>{tests.length} {tests.length === 1 ? "prueba activa" : "pruebas activas"}</small><strong>{new Set(tests.map((test) => test.deviceTypeId)).size} {new Set(tests.map((test) => test.deviceTypeId)).size === 1 ? "tipo de dispositivo" : "tipos de dispositivo"}</strong></div> : null}
          </header>

          <div className="tm-quality-toolbar">
            <label htmlFor="quality-device-filter"><Smartphone aria-hidden="true" /><span><small>Mostrar pruebas de</small><strong>{selectedDeviceType === "all" ? "Todos los dispositivos" : availableDeviceTypes.find((item) => item.id === Number(selectedDeviceType))?.name ?? "Dispositivo"}</strong></span><ChevronDown aria-hidden="true" /></label>
            <select id="quality-device-filter" value={selectedDeviceType} onChange={(event) => setSelectedDeviceType(event.target.value)}>
              <option value="all">Todos los dispositivos</option>
              {availableDeviceTypes.map((deviceType) => <option key={deviceType.id} value={deviceType.id}>{deviceType.name}</option>)}
            </select>
          </div>

          {loading ? (
            <div className="tm-quality-board__loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando pruebas</div>
          ) : groupedTests.length ? (
            <div className="tm-quality-groups">
              {groupedTests.map((group) => (
                <section key={group.id}>
                  <header><span><Smartphone aria-hidden="true" /></span><div><h3>{group.name}</h3><p>{group.tests.length} {group.tests.length === 1 ? "comprobación configurada" : "comprobaciones configuradas"}</p></div>{canCreate ? <button type="button" onClick={(event) => { editorTriggerRef.current = event.currentTarget; setNotice(null); setEditor({ test: null }); setSelectedDeviceType(String(group.id)); }}><Plus aria-hidden="true" /> Añadir</button> : null}</header>
                  <div className="tm-quality-list">
                    {group.tests.map((test) => <QualityTestRow key={test.id} test={test} canUpdate={canUpdate && editorReady} onEdit={(trigger) => openEditor(trigger, test)} />)}
                  </div>
                </section>
              ))}
            </div>
          ) : (
            <EmptyState
              title={selectedDeviceType === "all" ? "Todavía no hay pruebas configuradas" : "Este dispositivo no tiene pruebas"}
              message="Crea comprobaciones claras para estandarizar la recepción de equipos."
              action={canCreate && editorReady ? <button className="tm-button tm-button--primary" type="button" onClick={(event) => openEditor(event.currentTarget, null)}><Plus aria-hidden="true" /> Crear prueba</button> : undefined}
            />
          )}

          {!loading && tests.length ? <footer className="tm-quality-board__footer"><button className="tm-icon-button" type="button" aria-label="Actualizar pruebas" onClick={() => void loadTests()}><RefreshCw aria-hidden="true" /></button><p><strong>Plantilla de recepción</strong><span>Estas pruebas se aplicarán según el tipo de dispositivo de cada orden.</span></p></footer> : null}
        </section>
      ) : null}

      {editor ? <QualityTestEditor businessId={businessId} test={editor.test} tests={tests} deviceTypes={activeDeviceTypes} initialDeviceTypeId={selectedDeviceType === "all" ? undefined : Number(selectedDeviceType)} onClose={closeEditor} onSaved={handleSaved} /> : null}

      {!canCreate && !canUpdate ? <div className="tm-service-readonly"><CircleAlert aria-hidden="true" /><p><strong>Acceso de consulta</strong><span>Puedes revisar las pruebas, pero tu rol no permite modificarlas.</span></p></div> : null}
    </div>
  );
}
