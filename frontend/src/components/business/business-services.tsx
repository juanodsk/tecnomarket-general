"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import {
  Check,
  ChevronLeft,
  ChevronRight,
  CircleAlert,
  Clock3,
  ImagePlus,
  LoaderCircle,
  Pencil,
  Plus,
  RefreshCw,
  Tag,
  Trash2,
  Wrench,
  X,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { useForm } from "react-hook-form";

import { EmptyState, ErrorState, FormAlert, FullPageLoader } from "@/components/ui/feedback";
import { SelectField, TextField } from "@/components/ui/form-controls";
import * as servicesApi from "@/features/business-services/api";
import {
  businessServiceSchema,
  validateServiceImage,
  type BusinessServiceFormValues,
} from "@/features/business-services/schemas";
import { ApiClientError, normalizeApiError } from "@/lib/api/errors";
import { hasPermission } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";
import type { PageResponse } from "@/types/api";
import type {
  BusinessCatalogItem,
  BusinessService,
  CreateBusinessService,
} from "@/types/business-service";

const PAGE_SIZE = 12;

type Notice = { tone: "success" | "warning"; message: string };

function serviceValues(service: BusinessService | null): BusinessServiceFormValues {
  return {
    serviceCategoryId: service ? String(service.serviceCategoryId) : "",
    deviceTypeId: service ? String(service.deviceTypeId) : "",
    name: service?.name ?? "",
    description: service?.description ?? "",
    basePrice: service ? String(service.basePrice) : "",
    estimatedTimeMinutes: service ? String(service.estimatedTimeMinutes) : "",
  };
}

function toRequest(values: BusinessServiceFormValues): CreateBusinessService {
  return {
    serviceCategoryId: Number(values.serviceCategoryId),
    deviceTypeId: Number(values.deviceTypeId),
    name: values.name.trim(),
    description: values.description.trim(),
    basePrice: Number(values.basePrice.replace(",", ".")),
    estimatedTimeMinutes: Number(values.estimatedTimeMinutes),
  };
}

function formatAmount(value: number) {
  return new Intl.NumberFormat("es-CO", {
    minimumFractionDigits: 0,
    maximumFractionDigits: 2,
  }).format(value);
}

function formatDuration(minutes: number) {
  if (minutes < 60) return `${minutes} min`;
  const hours = Math.floor(minutes / 60);
  const remainder = minutes % 60;
  return remainder ? `${hours} h ${remainder} min` : `${hours} h`;
}

function backgroundImage(url: string | null) {
  return url ? `url(${JSON.stringify(url)})` : undefined;
}

function ServiceEditor({
  businessId,
  service,
  categories,
  deviceTypes,
  canUpdate,
  onClose,
  onSaved,
}: {
  businessId: number;
  service: BusinessService | null;
  categories: BusinessCatalogItem[];
  deviceTypes: BusinessCatalogItem[];
  canUpdate: boolean;
  onClose: () => void;
  onSaved: (notice: Notice) => Promise<void>;
}) {
  const panelRef = useRef<HTMLElement>(null);
  const closeRef = useRef<HTMLButtonElement>(null);
  const keepServiceRef = useRef<HTMLButtonElement>(null);
  const [image, setImage] = useState<File | null>(null);
  const [imagePreview, setImagePreview] = useState<string | null>(service?.imageUrl ?? null);
  const [imageError, setImageError] = useState<string | null>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const isEditing = Boolean(service);
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<BusinessServiceFormValues>({
    resolver: zodResolver(businessServiceSchema),
    defaultValues: serviceValues(service),
  });

  useEffect(() => {
    closeRef.current?.focus();
    const closeOnEscape = (event: KeyboardEvent) => {
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
    window.addEventListener("keydown", closeOnEscape);
    return () => window.removeEventListener("keydown", closeOnEscape);
  }, [deleting, isSubmitting, onClose]);

  useEffect(() => {
    if (confirmDelete) keepServiceRef.current?.focus();
  }, [confirmDelete]);

  useEffect(() => {
    if (!image) {
      setImagePreview(service?.imageUrl ?? null);
      return;
    }
    const objectUrl = URL.createObjectURL(image);
    setImagePreview(objectUrl);
    return () => URL.revokeObjectURL(objectUrl);
  }, [image, service?.imageUrl]);

  function selectImage(file: File | undefined) {
    if (!file) return;
    const validationError = validateServiceImage(file);
    setImageError(validationError);
    if (validationError) {
      setImage(null);
      return;
    }
    setImage(file);
  }

  async function submit(values: BusinessServiceFormValues) {
    setSubmitError(null);
    const request = toRequest(values);
    try {
      const savedService = service
        ? await servicesApi.updateBusinessService(businessId, service.id, {
            ...request,
            active: service.active,
          })
        : await servicesApi.createBusinessService(businessId, request);

      if (image && canUpdate) {
        try {
          await servicesApi.uploadBusinessServiceImage(businessId, savedService.id, image);
        } catch (uploadError) {
          await onSaved({
            tone: "warning",
            message: `El servicio quedó guardado, pero la imagen no pudo cargarse: ${normalizeApiError(uploadError).message}`,
          });
          onClose();
          return;
        }
      }

      await onSaved({
        tone: "success",
        message: service
          ? "Los datos del servicio quedaron actualizados."
          : "El servicio ya está disponible en el negocio.",
      });
      onClose();
    } catch (error) {
      setSubmitError(normalizeApiError(error).message);
    }
  }

  async function removeService() {
    if (!service) return;
    setDeleting(true);
    setSubmitError(null);
    try {
      await servicesApi.deleteBusinessService(businessId, service.id);
      await onSaved({ tone: "success", message: "El servicio fue desactivado." });
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
        className="tm-review-panel tm-service-editor"
        role="dialog"
        aria-modal="true"
        aria-labelledby="service-editor-title"
      >
        <header className="tm-review-panel__head">
          <div>
            <h2 id="service-editor-title">{isEditing ? `Editar ${service?.name}` : "Crear un servicio"}</h2>
            <p>Define qué equipo atiende, el valor base y el tiempo estimado.</p>
          </div>
          <button ref={closeRef} className="tm-icon-button" type="button" aria-label="Cerrar" onClick={onClose} disabled={isSubmitting || deleting}>
            <X aria-hidden="true" />
          </button>
        </header>

        <form className="tm-review-panel__body tm-service-form" onSubmit={handleSubmit(submit)}>
          {submitError ? <FormAlert>{submitError}</FormAlert> : null}

          <fieldset>
            <legend>Clasificación</legend>
            <div className="tm-service-form__grid">
              <SelectField label="Categoría del servicio" error={errors.serviceCategoryId?.message} {...register("serviceCategoryId")}>
                <option value="">Selecciona una categoría</option>
                {categories.map((category) => <option key={category.id} value={category.id}>{category.name}</option>)}
              </SelectField>
              <SelectField label="Tipo de dispositivo" error={errors.deviceTypeId?.message} {...register("deviceTypeId")}>
                <option value="">Selecciona un dispositivo</option>
                {deviceTypes.map((deviceType) => <option key={deviceType.id} value={deviceType.id}>{deviceType.name}</option>)}
              </SelectField>
            </div>
          </fieldset>

          <fieldset>
            <legend>Información visible</legend>
            <div className="tm-service-form__grid">
              <TextField className="tm-field--span" label="Nombre del servicio" maxLength={50} autoComplete="off" error={errors.name?.message} {...register("name")} />
              <div className="tm-field tm-field--span">
                <label htmlFor="service-description">Descripción</label>
                <textarea id="service-description" maxLength={200} rows={4} aria-invalid={Boolean(errors.description)} aria-describedby={errors.description ? "service-description-error" : "service-description-hint"} {...register("description")} />
                {errors.description ? <p className="tm-field__error" id="service-description-error" role="alert">{errors.description.message}</p> : <p className="tm-field__hint" id="service-description-hint">Explica de forma breve qué incluye la atención.</p>}
              </div>
            </div>
          </fieldset>

          <fieldset>
            <legend>Estimación</legend>
            <div className="tm-service-form__grid">
              <TextField label="Precio base" inputMode="decimal" placeholder="350000.00" error={errors.basePrice?.message} hint="Valor mayor que cero; máximo dos decimales." {...register("basePrice")} />
              <TextField label="Tiempo estimado (minutos)" inputMode="numeric" placeholder="120" error={errors.estimatedTimeMinutes?.message} hint="Usa minutos completos." {...register("estimatedTimeMinutes")} />
            </div>
          </fieldset>

          {canUpdate ? (
            <fieldset>
              <legend>Imagen del servicio</legend>
              <div className="tm-service-image-control">
                <div className={`tm-service-image-control__preview ${imagePreview ? "has-image" : ""}`} style={{ backgroundImage: backgroundImage(imagePreview) }}>
                  {!imagePreview ? <ImagePlus aria-hidden="true" /> : null}
                </div>
                <div>
                  <strong>{image ? image.name : imagePreview ? "Imagen actual" : "Sin imagen"}</strong>
                  <p>JPG, PNG o WebP. Tamaño máximo: 10 MB.</p>
                  <label className="tm-button tm-button--secondary">
                    <ImagePlus aria-hidden="true" /> {imagePreview ? "Cambiar imagen" : "Seleccionar imagen"}
                    <input type="file" accept="image/jpeg,image/png,image/webp" onChange={(event) => selectImage(event.target.files?.[0])} />
                  </label>
                  {imageError ? <p className="tm-field__error" role="alert">{imageError}</p> : null}
                </div>
              </div>
            </fieldset>
          ) : null}

          {service && canUpdate ? (
            <section className="tm-service-danger">
              <div><h3>Desactivar servicio</h3><p>Dejará de estar disponible para nuevas operaciones del negocio.</p></div>
              {confirmDelete ? (
                <div className="tm-service-danger__confirm">
                  <p>El historial existente no se elimina.</p>
                  <div>
                    <button ref={keepServiceRef} className="tm-button tm-button--secondary" type="button" onClick={() => setConfirmDelete(false)} disabled={deleting}>Conservar servicio</button>
                    <button className="tm-button tm-button--danger" type="button" onClick={removeService} disabled={deleting}>
                      {deleting ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Trash2 aria-hidden="true" />} Desactivar
                    </button>
                  </div>
                </div>
              ) : <button className="tm-button tm-button--danger" type="button" onClick={() => setConfirmDelete(true)}>Desactivar</button>}
            </section>
          ) : null}

          {!confirmDelete ? (
            <footer className="tm-service-form__actions">
              <button className="tm-button tm-button--secondary" type="button" onClick={onClose} disabled={isSubmitting || deleting}>Cancelar</button>
              <button className="tm-button tm-button--primary" type="submit" disabled={isSubmitting || deleting || Boolean(imageError)}>
                {isSubmitting ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : isEditing ? <Check aria-hidden="true" /> : <Plus aria-hidden="true" />}
                {isSubmitting ? "Guardando…" : isEditing ? "Guardar cambios" : "Crear servicio"}
              </button>
            </footer>
          ) : null}
        </form>
      </section>
    </div>
  );
}

function ServiceRow({
  service,
  canUpdate,
  onEdit,
}: {
  service: BusinessService;
  canUpdate: boolean;
  onEdit: (trigger: HTMLButtonElement) => void;
}) {
  return (
    <article>
      <div className={`tm-service-row__image ${service.imageUrl ? "has-image" : ""}`} style={{ backgroundImage: backgroundImage(service.imageUrl) }} aria-hidden="true">
        {!service.imageUrl ? <Wrench /> : null}
      </div>
      <div className="tm-service-row__identity">
        <div><h3>{service.name}</h3><span><Tag aria-hidden="true" /> {service.serviceCategoryName}</span></div>
        <p>{service.description}</p>
      </div>
      <div className="tm-service-row__fact"><small>Dispositivo</small><strong>{service.deviceTypeName}</strong><span>{service.serviceCategoryName}</span></div>
      <div className="tm-service-row__fact"><small>Estimación</small><strong>{formatAmount(service.basePrice)}</strong><span><Clock3 aria-hidden="true" /> {formatDuration(service.estimatedTimeMinutes)}</span></div>
      {canUpdate ? <button className="tm-button tm-button--secondary" type="button" onClick={(event) => onEdit(event.currentTarget)}><Pencil aria-hidden="true" /> Editar</button> : null}
    </article>
  );
}

export function BusinessServicesWorkspace({ businessId }: { businessId: number }) {
  const { businesses, businessContext, selectBusiness } = useAuth();
  const router = useRouter();
  const [services, setServices] = useState<PageResponse<BusinessService> | null>(null);
  const [categories, setCategories] = useState<BusinessCatalogItem[]>([]);
  const [deviceTypes, setDeviceTypes] = useState<BusinessCatalogItem[]>([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<ApiClientError | null>(null);
  const [catalogError, setCatalogError] = useState<string | null>(null);
  const [notice, setNotice] = useState<Notice | null>(null);
  const [editor, setEditor] = useState<{ mode: "create" } | { mode: "edit"; service: BusinessService } | null>(null);
  const editorTriggerRef = useRef<HTMLButtonElement | null>(null);
  const exists = businesses.some((business) => business.id === businessId);
  const contextReady = businessContext?.businessId === businessId;
  const permissions = contextReady ? businessContext.permissions : [];
  const canView = hasPermission(permissions, "BUSINESS_SERVICE_VIEW");
  const canCreate = hasPermission(permissions, "BUSINESS_SERVICE_CREATE");
  const canUpdate = hasPermission(permissions, "BUSINESS_SERVICE_UPDATE");
  const canViewCatalogs = hasPermission(permissions, "BUSINESS_CATALOG_VIEW");
  const activeCategories = useMemo(() => categories.filter((item) => item.active), [categories]);
  const activeDeviceTypes = useMemo(() => deviceTypes.filter((item) => item.active), [deviceTypes]);
  const catalogsReady = canViewCatalogs && activeCategories.length > 0 && activeDeviceTypes.length > 0;
  const distinctCategories = useMemo(() => new Set(services?.items.map((service) => service.serviceCategoryId)).size, [services]);

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

  const loadServices = useCallback(async (targetPage: number) => {
    setLoading(true);
    setError(null);
    try {
      setServices(await servicesApi.getBusinessServices(businessId, targetPage, PAGE_SIZE));
    } catch (requestError) {
      const normalized = normalizeApiError(requestError);
      if (normalized.status === 403) router.replace("/acceso-denegado");
      else setError(normalized);
    } finally {
      setLoading(false);
    }
  }, [businessId, router]);

  const loadCatalogs = useCallback(async () => {
    if (!canViewCatalogs) {
      setCatalogError("Tu rol no permite consultar las categorías y tipos de dispositivo necesarios para editar servicios.");
      return;
    }
    setCatalogError(null);
    try {
      const [serviceCategories, availableDeviceTypes] = await Promise.all([
        servicesApi.getBusinessCatalogItems(businessId, "SERVICE_CATEGORY"),
        servicesApi.getBusinessCatalogItems(businessId, "DEVICE_TYPE"),
      ]);
      setCategories(serviceCategories);
      setDeviceTypes(availableDeviceTypes);
      if (!serviceCategories.some((item) => item.active) || !availableDeviceTypes.some((item) => item.active)) {
        setCatalogError("Configura al menos una categoría de servicio y un tipo de dispositivo antes de crear servicios.");
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
    void loadServices(page);
    if (canCreate || canUpdate) void loadCatalogs();
  }, [canCreate, canUpdate, canView, contextReady, loadCatalogs, loadServices, page, router]);

  const handleSaved = useCallback(async (nextNotice: Notice) => {
    let updatedServices = await servicesApi.getBusinessServices(businessId, page, PAGE_SIZE);
    if (page > 0 && !updatedServices.items.length) {
      const previousPage = page - 1;
      updatedServices = await servicesApi.getBusinessServices(businessId, previousPage, PAGE_SIZE);
      setPage(previousPage);
    }
    setServices(updatedServices);
    setNotice(nextNotice);
  }, [businessId, page]);

  const closeEditor = useCallback(() => {
    setEditor(null);
    requestAnimationFrame(() => editorTriggerRef.current?.focus());
  }, []);

  if (!exists) return <div className="tm-page-stack"><ErrorState title="Negocio no disponible" message="Este negocio no está asociado a tu cuenta o ya no está disponible." /></div>;
  if (error && !contextReady) return <div className="tm-page-stack"><ErrorState message={error.message} onRetry={() => void selectBusiness(businessId).catch(handleContextError)} /></div>;
  if (!contextReady || !canView) return <FullPageLoader label="Preparando los servicios del negocio" />;

  return (
    <div className="tm-page-stack tm-services-page">
      <header className="tm-workspace-heading tm-services-heading">
        <div><h1>Servicios</h1><p>Define las atenciones que ofrece {businessContext.businessName} y su estimación inicial.</p></div>
        {canCreate ? (
          <button className="tm-button tm-button--primary" type="button" disabled={!catalogsReady} title={!catalogsReady ? "Faltan catálogos disponibles" : undefined} onClick={(event) => { editorTriggerRef.current = event.currentTarget; setNotice(null); setEditor({ mode: "create" }); }}>
            <Plus aria-hidden="true" /> Nuevo servicio
          </button>
        ) : null}
      </header>

      {notice ? <FormAlert tone={notice.tone}>{notice.message}</FormAlert> : null}
      {catalogError && (canCreate || canUpdate) ? <FormAlert tone="warning">{catalogError}</FormAlert> : null}
      {error ? <ErrorState message={error.message} onRetry={() => void loadServices(page)} /> : null}

      {!error ? (
        <section className="tm-service-board">
          <header>
            <div className="tm-service-board__title"><span><Wrench aria-hidden="true" /></span><div><h2>Oferta del negocio</h2><p>Servicios activos organizados por equipo y categoría.</p></div></div>
            {!loading && services?.totalElements ? <div className="tm-service-board__summary"><small>{services.totalElements} {services.totalElements === 1 ? "servicio activo" : "servicios activos"}</small><strong>{distinctCategories} {distinctCategories === 1 ? "categoría en esta página" : "categorías en esta página"}</strong></div> : null}
          </header>

          {loading ? (
            <div className="tm-service-board__loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando servicios</div>
          ) : services?.items.length ? (
            <div className="tm-service-list">
              {services.items.map((service) => <ServiceRow key={service.id} service={service} canUpdate={canUpdate && catalogsReady} onEdit={(trigger) => { editorTriggerRef.current = trigger; setNotice(null); setEditor({ mode: "edit", service }); }} />)}
            </div>
          ) : (
            <EmptyState title="Todavía no hay servicios" message="Registra la primera atención que podrá ofrecer el negocio." action={canCreate && catalogsReady ? <button className="tm-button tm-button--primary" type="button" onClick={(event) => { editorTriggerRef.current = event.currentTarget; setEditor({ mode: "create" }); }}><Plus aria-hidden="true" /> Crear servicio</button> : undefined} />
          )}

          {!loading && services?.items.length ? (
            <footer className="tm-service-board__footer">
              <div><button className="tm-icon-button" type="button" aria-label="Actualizar servicios" onClick={() => void loadServices(page)}><RefreshCw aria-hidden="true" /></button><p><strong>Estimaciones iniciales</strong><span>El precio y el tiempo pueden ajustarse durante el diagnóstico.</span></p></div>
              {services.totalPages > 1 ? <nav aria-label="Paginación de servicios"><button className="tm-icon-button" type="button" aria-label="Página anterior" disabled={!services.hasPrevious} onClick={() => setPage((current) => Math.max(0, current - 1))}><ChevronLeft aria-hidden="true" /></button><span>Página {services.page + 1} de {services.totalPages}</span><button className="tm-icon-button" type="button" aria-label="Página siguiente" disabled={!services.hasNext} onClick={() => setPage((current) => current + 1)}><ChevronRight aria-hidden="true" /></button></nav> : null}
            </footer>
          ) : null}
        </section>
      ) : null}

      {editor ? <ServiceEditor businessId={businessId} service={editor.mode === "edit" ? editor.service : null} categories={activeCategories} deviceTypes={activeDeviceTypes} canUpdate={canUpdate} onClose={closeEditor} onSaved={handleSaved} /> : null}

      {!canCreate && !canUpdate ? <div className="tm-service-readonly"><CircleAlert aria-hidden="true" /><p><strong>Acceso de consulta</strong><span>Puedes revisar los servicios, pero tu rol no permite modificarlos.</span></p></div> : null}
    </div>
  );
}
