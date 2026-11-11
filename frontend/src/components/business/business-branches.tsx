"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import {
  Building2,
  Check,
  CircleAlert,
  LoaderCircle,
  MapPin,
  Pencil,
  Phone,
  Plus,
  RefreshCw,
  Route,
  ShieldCheck,
  Trash2,
  X,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { useForm } from "react-hook-form";

import {
  EmptyState,
  ErrorState,
  FormAlert,
  FullPageLoader,
} from "@/components/ui/feedback";
import { LocationPickerMap } from "@/components/business/location-picker-map";
import { SelectField, TextField } from "@/components/ui/form-controls";
import * as businessApi from "@/features/business/api";
import {
  businessBranchSchema,
  type BusinessBranchFormValues,
} from "@/features/business/branch-schemas";
import * as locationApi from "@/features/locations/api";
import { ApiClientError, normalizeApiError } from "@/lib/api/errors";
import { hasPermission } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";
import type { BusinessBranch, CreateBusinessBranch } from "@/types/business";
import type { City, Country, Department } from "@/types/location";

const emptyValues: BusinessBranchFormValues = {
  name: "",
  address: "",
  neighborhood: "",
  phone: "",
  countryId: "",
  departmentId: "",
  cityId: "",
  latitude: "",
  longitude: "",
  serviceRadiusKm: "",
  primaryBranch: false,
  changeLocation: true,
};

function branchValues(branch: BusinessBranch | null): BusinessBranchFormValues {
  if (!branch) return emptyValues;

  return {
    name: branch.name,
    address: branch.address,
    neighborhood: branch.neighborhood ?? "",
    phone: branch.phone ?? "",
    countryId: "",
    departmentId: "",
    cityId: String(branch.cityId),
    latitude: branch.latitude === null ? "" : String(branch.latitude),
    longitude: branch.longitude === null ? "" : String(branch.longitude),
    serviceRadiusKm:
      branch.serviceRadiusKm === null ? "" : String(branch.serviceRadiusKm),
    primaryBranch: branch.primaryBranch,
    changeLocation: false,
  };
}

function nullableText(value: string) {
  const trimmed = value.trim();
  return trimmed || null;
}

function nullableNumber(value: string) {
  return value.trim() ? Number(value) : null;
}

function toRequest(values: BusinessBranchFormValues): CreateBusinessBranch {
  return {
    name: values.name.trim(),
    cityId: Number(values.cityId),
    address: values.address.trim(),
    neighborhood: nullableText(values.neighborhood),
    phone: nullableText(values.phone),
    latitude: nullableNumber(values.latitude),
    longitude: nullableNumber(values.longitude),
    serviceRadiusKm: nullableNumber(values.serviceRadiusKm),
    primaryBranch: values.primaryBranch,
  };
}

function BranchEditor({
  businessId,
  branch,
  canUpdate,
  onClose,
  onSaved,
}: {
  businessId: number;
  branch: BusinessBranch | null;
  canUpdate: boolean;
  onClose: () => void;
  onSaved: (message: string) => Promise<void>;
}) {
  const panelRef = useRef<HTMLElement>(null);
  const closeRef = useRef<HTMLButtonElement>(null);
  const keepBranchRef = useRef<HTMLButtonElement>(null);
  const [countries, setCountries] = useState<Country[]>([]);
  const [departments, setDepartments] = useState<Department[]>([]);
  const [cities, setCities] = useState<City[]>([]);
  const [loadingCountries, setLoadingCountries] = useState(true);
  const [loadingDepartments, setLoadingDepartments] = useState(false);
  const [loadingCities, setLoadingCities] = useState(false);
  const [catalogError, setCatalogError] = useState<string | null>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const isEditing = Boolean(branch);
  const {
    register,
    handleSubmit,
    watch,
    setValue,
    resetField,
    formState: { errors, isSubmitting },
  } = useForm<BusinessBranchFormValues>({
    resolver: zodResolver(businessBranchSchema),
    defaultValues: branchValues(branch),
  });
  const countryId = watch("countryId");
  const departmentId = watch("departmentId");
  const changeLocation = watch("changeLocation");
  const primaryBranch = watch("primaryBranch");
  const latitude = watch("latitude");
  const longitude = watch("longitude");

  useEffect(() => {
    closeRef.current?.focus();
    const closeOnEscape = (event: KeyboardEvent) => {
      if (event.key === "Escape" && !isSubmitting && !deleting) onClose();
      if (event.key !== "Tab") return;
      const focusable = Array.from(
        panelRef.current?.querySelectorAll<HTMLElement>(
          "button:not([disabled]), input:not([disabled]), select:not([disabled]), summary",
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
    if (confirmDelete) keepBranchRef.current?.focus();
  }, [confirmDelete]);

  useEffect(() => {
    let active = true;
    setLoadingCountries(true);
    locationApi
      .getCountries()
      .then((items) => {
        if (active) setCountries(items.filter((item) => item.active));
      })
      .catch((error) => {
        if (active) setCatalogError(normalizeApiError(error).message);
      })
      .finally(() => {
        if (active) setLoadingCountries(false);
      });
    return () => {
      active = false;
    };
  }, []);

  useEffect(() => {
    let active = true;
    setDepartments([]);
    setCities([]);
    resetField("departmentId");
    if (changeLocation) resetField("cityId");
    if (!changeLocation || !countryId)
      return () => {
        active = false;
      };
    setLoadingDepartments(true);
    locationApi
      .getDepartments(Number(countryId))
      .then((items) => {
        if (active) setDepartments(items.filter((item) => item.active));
      })
      .catch((error) => {
        if (active) setCatalogError(normalizeApiError(error).message);
      })
      .finally(() => {
        if (active) setLoadingDepartments(false);
      });
    return () => {
      active = false;
    };
  }, [changeLocation, countryId, resetField]);

  useEffect(() => {
    let active = true;
    setCities([]);
    if (changeLocation) resetField("cityId");
    if (!changeLocation || !departmentId)
      return () => {
        active = false;
      };
    setLoadingCities(true);
    locationApi
      .getCities(Number(departmentId))
      .then((items) => {
        if (active) setCities(items.filter((item) => item.active));
      })
      .catch((error) => {
        if (active) setCatalogError(normalizeApiError(error).message);
      })
      .finally(() => {
        if (active) setLoadingCities(false);
      });
    return () => {
      active = false;
    };
  }, [changeLocation, departmentId, resetField]);

  async function submit(values: BusinessBranchFormValues) {
    setSubmitError(null);
    try {
      const request = toRequest(values);
      if (branch) {
        await businessApi.updateBusinessBranch(businessId, branch.id, {
          ...request,
          active: true,
        });
        await onSaved("Los datos de la sede quedaron actualizados.");
      } else {
        await businessApi.createBusinessBranch(businessId, request);
        await onSaved("La nueva sede ya está disponible en el negocio.");
      }
      onClose();
    } catch (error) {
      setSubmitError(normalizeApiError(error).message);
    }
  }

  async function removeBranch() {
    if (!branch) return;
    setDeleting(true);
    setSubmitError(null);
    try {
      await businessApi.deleteBusinessBranch(businessId, branch.id);
      await onSaved("La sede fue desactivada.");
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
        className="tm-review-panel tm-branch-editor"
        role="dialog"
        aria-modal="true"
        aria-labelledby="branch-editor-title"
      >
        <header className="tm-review-panel__head">
          <div>
            <h2 id="branch-editor-title">
              {isEditing ? `Editar ${branch?.name}` : "Registrar una sede"}
            </h2>
            <p>
              {isEditing
                ? `Sede ${branch?.branchNumber}. Actualiza la atención, ubicación y cobertura.`
                : "Añade un punto de operación al negocio."}
            </p>
          </div>
          <button
            ref={closeRef}
            className="tm-icon-button"
            type="button"
            aria-label="Cerrar"
            onClick={onClose}
            disabled={isSubmitting || deleting}
          >
            <X aria-hidden="true" />
          </button>
        </header>
        <form
          className="tm-review-panel__body tm-branch-form"
          onSubmit={handleSubmit(submit)}
        >
          {submitError ? <FormAlert>{submitError}</FormAlert> : null}
          {catalogError ? <FormAlert>{catalogError}</FormAlert> : null}

          <fieldset>
            <legend>Identificación y contacto</legend>
            <div className="tm-branch-form__grid">
              <TextField
                className="tm-field--span"
                label="Nombre de la sede"
                maxLength={120}
                autoComplete="organization"
                error={errors.name?.message}
                {...register("name")}
              />
              <TextField
                className="tm-field--span"
                label="Dirección"
                maxLength={200}
                autoComplete="street-address"
                error={errors.address?.message}
                {...register("address")}
              />
              <TextField
                label="Barrio"
                maxLength={120}
                autoComplete="address-level3"
                error={errors.neighborhood?.message}
                {...register("neighborhood")}
              />
              <TextField
                label="Teléfono"
                maxLength={30}
                autoComplete="tel"
                error={errors.phone?.message}
                {...register("phone")}
              />
            </div>
          </fieldset>

          <fieldset>
            <legend>Ubicación</legend>
            {branch && !changeLocation ? (
              <div className="tm-branch-current-location">
                <span>
                  <MapPin aria-hidden="true" />
                </span>
                <div>
                  <strong>
                    {branch.cityName}, {branch.countryName}
                  </strong>
                  <small>{branch.address}</small>
                </div>
                <button
                  type="button"
                  onClick={() => {
                    setValue("changeLocation", true);
                    setValue("countryId", String(branch.countryId));
                  }}
                >
                  Cambiar ciudad
                </button>
              </div>
            ) : (
              <div className="tm-branch-form__grid">
                <SelectField
                  label="País"
                  disabled={loadingCountries}
                  error={errors.countryId?.message}
                  {...register("countryId")}
                >
                  <option value="">
                    {loadingCountries
                      ? "Cargando países…"
                      : "Selecciona un país"}
                  </option>
                  {countries.map((country) => (
                    <option key={country.id} value={country.id}>
                      {country.name}
                    </option>
                  ))}
                </SelectField>
                <SelectField
                  label="Departamento"
                  disabled={!countryId || loadingDepartments}
                  error={errors.departmentId?.message}
                  {...register("departmentId")}
                >
                  <option value="">
                    {loadingDepartments
                      ? "Cargando departamentos…"
                      : "Selecciona un departamento"}
                  </option>
                  {departments.map((department) => (
                    <option key={department.id} value={department.id}>
                      {department.name}
                    </option>
                  ))}
                </SelectField>
                <SelectField
                  className="tm-field--span"
                  label="Ciudad"
                  disabled={!departmentId || loadingCities}
                  error={errors.cityId?.message}
                  {...register("cityId")}
                >
                  <option value="">
                    {loadingCities
                      ? "Cargando ciudades…"
                      : "Selecciona una ciudad"}
                  </option>
                  {cities.map((city) => (
                    <option key={city.id} value={city.id}>
                      {city.name}
                    </option>
                  ))}
                </SelectField>
                {branch ? (
                  <button
                    className="tm-branch-location-cancel"
                    type="button"
                    onClick={() => {
                      setValue("changeLocation", false);
                      setValue("countryId", "");
                      setValue("departmentId", "");
                      setValue("cityId", String(branch.cityId));
                    }}
                  >
                    Conservar la ciudad actual
                  </button>
                ) : null}
              </div>
            )}
          </fieldset>

          <fieldset>
            <legend>Ubicación exacta</legend>
            <p className="tm-branch-form__lead">
              Este punto es opcional y permite identificar visualmente dónde
              está la entrada de la sede.
            </p>
            <input type="hidden" {...register("latitude")} />
            <input type="hidden" {...register("longitude")} />
            <LocationPickerMap
              latitude={latitude}
              longitude={longitude}
              error={errors.latitude?.message ?? errors.longitude?.message}
              onChange={(nextLatitude, nextLongitude) => {
                setValue("latitude", nextLatitude, {
                  shouldDirty: true,
                  shouldValidate: true,
                });
                setValue("longitude", nextLongitude, {
                  shouldDirty: true,
                  shouldValidate: true,
                });
              }}
              onClear={() => {
                setValue("latitude", "", {
                  shouldDirty: true,
                  shouldValidate: true,
                });
                setValue("longitude", "", {
                  shouldDirty: true,
                  shouldValidate: true,
                });
              }}
            />
          </fieldset>

          <fieldset>
            <legend>Operación</legend>
            <div className="tm-branch-form__grid">
              <TextField
                className="tm-field--span"
                label="Radio de servicio (km)"
                inputMode="decimal"
                placeholder="8.0"
                error={errors.serviceRadiusKm?.message}
                hint="Opcional. Valor mínimo: 0,1 km."
                {...register("serviceRadiusKm")}
              />
            </div>
            <label
              className={`tm-branch-primary-control ${primaryBranch ? "is-selected" : ""}`}
            >
              <input type="checkbox" {...register("primaryBranch")} />
              <span>{primaryBranch ? <Check aria-hidden="true" /> : null}</span>
              <span>
                <strong>Usar como sede principal</strong>
                <small>Será la referencia predeterminada del negocio.</small>
              </span>
            </label>
          </fieldset>

          {branch && canUpdate ? (
            <section className="tm-branch-danger">
              <div>
                <h3>Desactivar sede</h3>
                <p>
                  Dejará de aparecer como punto operativo y en el selector del
                  negocio.
                </p>
              </div>
              {confirmDelete ? (
                <div className="tm-branch-danger__confirm">
                  <p>Esta acción no es posible si es la única sede activa.</p>
                  <div>
                    <button
                      ref={keepBranchRef}
                      className="tm-button tm-button--secondary"
                      type="button"
                      onClick={() => setConfirmDelete(false)}
                      disabled={deleting}
                    >
                      Conservar sede
                    </button>
                    <button
                      className="tm-button tm-button--danger"
                      type="button"
                      onClick={removeBranch}
                      disabled={deleting}
                    >
                      {deleting ? (
                        <LoaderCircle
                          className="tm-spinner"
                          aria-hidden="true"
                        />
                      ) : (
                        <Trash2 aria-hidden="true" />
                      )}{" "}
                      Desactivar
                    </button>
                  </div>
                </div>
              ) : (
                <button
                  className="tm-button tm-button--danger"
                  type="button"
                  onClick={() => setConfirmDelete(true)}
                >
                  Desactivar
                </button>
              )}
            </section>
          ) : null}

          {!confirmDelete ? (
            <footer className="tm-branch-form__actions">
              <button
                className="tm-button tm-button--secondary"
                type="button"
                onClick={onClose}
                disabled={isSubmitting || deleting}
              >
                Cancelar
              </button>
              <button
                className="tm-button tm-button--primary"
                type="submit"
                disabled={isSubmitting || deleting || Boolean(catalogError)}
              >
                {isSubmitting ? (
                  <LoaderCircle className="tm-spinner" aria-hidden="true" />
                ) : isEditing ? (
                  <Check aria-hidden="true" />
                ) : (
                  <Plus aria-hidden="true" />
                )}
                {isSubmitting
                  ? "Guardando…"
                  : isEditing
                    ? "Guardar cambios"
                    : "Crear sede"}
              </button>
            </footer>
          ) : null}
        </form>
      </section>
    </div>
  );
}

function BranchRow({
  branch,
  canUpdate,
  onEdit,
}: {
  branch: BusinessBranch;
  canUpdate: boolean;
  onEdit: (trigger: HTMLButtonElement) => void;
}) {
  return (
    <article className={branch.primaryBranch ? "is-primary" : ""}>
      <span className="tm-branch-row__number">
        {String(branch.branchNumber).padStart(2, "0")}
      </span>
      <div className="tm-branch-row__identity">
        <div>
          <h3>{branch.name}</h3>
          {branch.primaryBranch ? (
            <span>
              <ShieldCheck aria-hidden="true" /> Principal
            </span>
          ) : null}
        </div>
        <p>
          <MapPin aria-hidden="true" /> {branch.address}
          {branch.neighborhood ? ` · ${branch.neighborhood}` : ""}
        </p>
      </div>
      <div className="tm-branch-row__fact">
        <small>Ciudad</small>
        <strong>{branch.cityName}</strong>
        <span>{branch.countryName}</span>
      </div>
      <div className="tm-branch-row__fact">
        <small>Contacto</small>
        <strong>
          {branch.phone ? (
            <>
              <Phone aria-hidden="true" /> {branch.phone}
            </>
          ) : (
            "Sin teléfono"
          )}
        </strong>
        <span>
          {branch.serviceRadiusKm === null
            ? "Sin radio definido"
            : `${branch.serviceRadiusKm} km de cobertura`}
        </span>
      </div>
      {canUpdate ? (
        <button
          className="tm-button tm-button--secondary"
          type="button"
          onClick={(event) => onEdit(event.currentTarget)}
        >
          <Pencil aria-hidden="true" /> Editar
        </button>
      ) : null}
    </article>
  );
}

export function BusinessBranchesWorkspace({
  businessId,
}: {
  businessId: number;
}) {
  const {
    businesses,
    businessContext,
    selectBusiness,
    refreshBusinessContext,
  } = useAuth();
  const router = useRouter();
  const [branches, setBranches] = useState<BusinessBranch[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<ApiClientError | null>(null);
  const [notice, setNotice] = useState<string | null>(null);
  const [editor, setEditor] = useState<
    { mode: "create" } | { mode: "edit"; branch: BusinessBranch } | null
  >(null);
  const editorTriggerRef = useRef<HTMLButtonElement | null>(null);
  const exists = businesses.some((business) => business.id === businessId);
  const contextReady = businessContext?.businessId === businessId;
  const permissions = contextReady ? businessContext.permissions : [];
  const canView = hasPermission(permissions, "BUSINESS_BRANCH_VIEW");
  const canCreate = hasPermission(permissions, "BUSINESS_BRANCH_CREATE");
  const canUpdate = hasPermission(permissions, "BUSINESS_BRANCH_UPDATE");
  const primaryBranch = useMemo(
    () => branches.find((branch) => branch.primaryBranch),
    [branches],
  );

  const handleContextError = useCallback(
    (requestError: unknown) => {
      const normalized = normalizeApiError(requestError);
      if (normalized.status === 403) router.replace("/acceso-denegado");
      else setError(normalized);
    },
    [router],
  );

  useEffect(() => {
    if (!exists || contextReady) return;
    let active = true;
    selectBusiness(businessId).catch((requestError) => {
      if (active) handleContextError(requestError);
    });
    return () => {
      active = false;
    };
  }, [businessId, contextReady, exists, handleContextError, selectBusiness]);

  const loadBranches = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      setBranches(await businessApi.getBusinessBranches(businessId));
    } catch (requestError) {
      const normalized = normalizeApiError(requestError);
      if (normalized.status === 403) router.replace("/acceso-denegado");
      else setError(normalized);
    } finally {
      setLoading(false);
    }
  }, [businessId, router]);

  useEffect(() => {
    if (!contextReady) return;
    if (!canView) {
      router.replace("/acceso-denegado");
      return;
    }
    void loadBranches();
  }, [canView, contextReady, loadBranches, router]);

  const handleSaved = useCallback(
    async (message: string) => {
      const updatedBranches = await businessApi.getBusinessBranches(businessId);
      setBranches(updatedBranches);
      await refreshBusinessContext(businessId);
      setNotice(message);
    },
    [businessId, refreshBusinessContext],
  );

  const closeEditor = useCallback(() => {
    setEditor(null);
    requestAnimationFrame(() => editorTriggerRef.current?.focus());
  }, []);

  if (!exists)
    return (
      <div className="tm-page-stack">
        <ErrorState
          title="Negocio no disponible"
          message="Este negocio no está asociado a tu cuenta o ya no está disponible."
        />
      </div>
    );
  if (error && !contextReady)
    return (
      <div className="tm-page-stack">
        <ErrorState
          message={error.message}
          onRetry={() =>
            void selectBusiness(businessId).catch(handleContextError)
          }
        />
      </div>
    );
  if (!contextReady || !canView)
    return <FullPageLoader label="Preparando las sedes del negocio" />;

  return (
    <div className="tm-page-stack tm-branches-page">
      <header className="tm-workspace-heading tm-branches-heading">
        <div>
          <h1>Sedes</h1>
          <p>
            Administra los puntos físicos donde opera{" "}
            {businessContext.businessName}.
          </p>
        </div>
        {canCreate ? (
          <button
            className="tm-button tm-button--primary"
            type="button"
            onClick={(event) => {
              editorTriggerRef.current = event.currentTarget;
              setNotice(null);
              setEditor({ mode: "create" });
            }}
          >
            <Plus aria-hidden="true" /> Nueva sede
          </button>
        ) : null}
      </header>

      {notice ? <FormAlert tone="success">{notice}</FormAlert> : null}
      {error ? (
        <ErrorState
          message={error.message}
          onRetry={() => void loadBranches()}
        />
      ) : null}

      {!error ? (
        <section className="tm-branch-board">
          <header>
            <div className="tm-branch-board__title">
              <span>
                <Building2 aria-hidden="true" />
              </span>
              <div>
                <h2>Puntos de operación</h2>
                <p>La sede principal y las ubicaciones activas del negocio.</p>
              </div>
            </div>
            {!loading && branches.length ? (
              <div className="tm-branch-board__summary">
                <small>
                  {branches.length}{" "}
                  {branches.length === 1 ? "sede activa" : "sedes activas"}
                </small>
                <strong>{primaryBranch?.name ?? "Sin sede principal"}</strong>
              </div>
            ) : null}
          </header>

          {loading ? (
            <div className="tm-branch-board__loading">
              <LoaderCircle className="tm-spinner" aria-hidden="true" />{" "}
              Cargando sedes
            </div>
          ) : branches.length ? (
            <div className="tm-branch-list">
              {branches.map((branch) => (
                <BranchRow
                  key={branch.id}
                  branch={branch}
                  canUpdate={canUpdate}
                  onEdit={(trigger) => {
                    editorTriggerRef.current = trigger;
                    setNotice(null);
                    setEditor({ mode: "edit", branch });
                  }}
                />
              ))}
            </div>
          ) : (
            <EmptyState
              title="No hay sedes disponibles"
              message="Registra el primer punto de operación para continuar configurando el negocio."
              action={
                canCreate ? (
                  <button
                    className="tm-button tm-button--primary"
                    type="button"
                    onClick={(event) => {
                      editorTriggerRef.current = event.currentTarget;
                      setEditor({ mode: "create" });
                    }}
                  >
                    <Plus aria-hidden="true" /> Crear sede
                  </button>
                ) : undefined
              }
            />
          )}

          {!loading && branches.length ? (
            <footer className="tm-branch-board__note">
              <Route aria-hidden="true" />
              <p>
                <strong>Sedes disponibles para trabajar</strong>
                <span>
                  Al guardar, esta lista y la sede elegida se actualizan
                  automáticamente.
                </span>
              </p>
              <button
                className="tm-icon-button"
                type="button"
                aria-label="Actualizar sedes"
                onClick={() => void loadBranches()}
              >
                <RefreshCw aria-hidden="true" />
              </button>
            </footer>
          ) : null}
        </section>
      ) : null}

      {editor ? (
        <BranchEditor
          businessId={businessId}
          branch={editor.mode === "edit" ? editor.branch : null}
          canUpdate={canUpdate}
          onClose={closeEditor}
          onSaved={handleSaved}
        />
      ) : null}

      {!canCreate && !canUpdate ? (
        <div className="tm-branch-readonly">
          <CircleAlert aria-hidden="true" />
          <p>
            <strong>Acceso de consulta</strong>
            <span>
              Puedes revisar las sedes, pero tu rol no permite modificarlas.
            </span>
          </p>
        </div>
      ) : null}
    </div>
  );
}
