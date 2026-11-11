"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import {
  Check,
  ChevronDown,
  CircleAlert,
  Fingerprint,
  LoaderCircle,
  Mail,
  Pencil,
  Plus,
  RefreshCw,
  Search,
  Smartphone,
  Trash2,
  UserRound,
  UsersRound,
  X,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { useForm } from "react-hook-form";

import { EmptyState, ErrorState, FormAlert, FullPageLoader } from "@/components/ui/feedback";
import { SelectField, TextField } from "@/components/ui/form-controls";
import * as customerDevicesApi from "@/features/customer-devices/api";
import {
  customerDeviceSchema,
  nullableTrimmed,
  type CustomerDeviceFormValues,
} from "@/features/customer-devices/schemas";
import { ApiClientError, normalizeApiError } from "@/lib/api/errors";
import { hasPermission } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";
import type { BusinessCatalogItem } from "@/types/business-service";
import type { CustomerDevice, CustomerDirectoryEntry } from "@/types/customer-device";

type Notice = { tone: "success" | "warning"; message: string };

function buildCustomerDirectory(devices: CustomerDevice[]) {
  const directory = new Map<number, CustomerDirectoryEntry>();

  devices.forEach((device) => {
    const customer = directory.get(device.customerId) ?? {
      id: device.customerId,
      documentTypeCode: device.customerDocumentTypeCode,
      documentNumber: device.customerDocumentNumber,
      name: device.customerName,
      surname: device.customerSurname,
      email: device.customerEmail,
      devices: [],
    };
    customer.devices.push(device);
    directory.set(customer.id, customer);
  });

  return Array.from(directory.values())
    .map((customer) => ({
      ...customer,
      devices: customer.devices.sort((a, b) =>
        b.registeredAt.localeCompare(a.registeredAt) || b.id - a.id,
      ),
    }))
    .sort((a, b) =>
      `${a.name} ${a.surname}`.localeCompare(`${b.name} ${b.surname}`, "es"),
    );
}

function deviceValues(
  device: CustomerDevice | null,
  initialCustomerId?: number,
): CustomerDeviceFormValues {
  return {
    customerId: device ? String(device.customerId) : initialCustomerId ? String(initialCustomerId) : "",
    deviceTypeId: device ? String(device.deviceTypeId) : "",
    brandId: device ? String(device.brandId) : "",
    model: device?.model ?? "",
    serialNumber: device?.serialNumber ?? "",
    imei: device?.imei ?? "",
    color: device?.color ?? "",
  };
}

function formatRegistrationDate(value: string) {
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return "Fecha no disponible";
  return new Intl.DateTimeFormat("es-CO", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  }).format(date);
}

function customerInitials(customer: CustomerDirectoryEntry) {
  return `${customer.name.slice(0, 1)}${customer.surname.slice(0, 1)}`.toUpperCase();
}

function CustomerDeviceEditor({
  businessId,
  device,
  customers,
  deviceTypes,
  brands,
  actorBusinessUserId,
  initialCustomerId,
  onClose,
  onSaved,
}: {
  businessId: number;
  device: CustomerDevice | null;
  customers: CustomerDirectoryEntry[];
  deviceTypes: BusinessCatalogItem[];
  brands: BusinessCatalogItem[];
  actorBusinessUserId: number;
  initialCustomerId?: number;
  onClose: () => void;
  onSaved: (notice: Notice) => Promise<void>;
}) {
  const panelRef = useRef<HTMLElement>(null);
  const closeRef = useRef<HTMLButtonElement>(null);
  const keepRef = useRef<HTMLButtonElement>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const isEditing = Boolean(device);
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<CustomerDeviceFormValues>({
    resolver: zodResolver(customerDeviceSchema),
    defaultValues: deviceValues(device, initialCustomerId),
  });

  useEffect(() => {
    closeRef.current?.focus();
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape" && !isSubmitting && !deleting) onClose();
      if (event.key !== "Tab") return;
      const focusable = Array.from(
        panelRef.current?.querySelectorAll<HTMLElement>(
          "button:not([disabled]), input:not([disabled]), select:not([disabled])",
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

  async function submit(values: CustomerDeviceFormValues) {
    setSubmitError(null);
    const baseRequest = {
      customerId: Number(values.customerId),
      deviceTypeId: Number(values.deviceTypeId),
      brandId: Number(values.brandId),
      model: values.model.trim(),
      serialNumber: nullableTrimmed(values.serialNumber),
      imei: nullableTrimmed(values.imei),
      color: values.color.trim(),
    };

    try {
      if (device) {
        await customerDevicesApi.updateCustomerDevice(businessId, device.id, {
          ...baseRequest,
          registeredByBusinessUserId: device.registeredByBusinessUserId,
          registeredAt: device.registeredAt,
          active: device.active,
        });
      } else {
        await customerDevicesApi.createCustomerDevice(businessId, {
          ...baseRequest,
          registeredByBusinessUserId: actorBusinessUserId,
          registeredAt: null,
        });
      }
      await onSaved({
        tone: "success",
        message: device
          ? "Los datos del dispositivo quedaron actualizados."
          : "El dispositivo ya forma parte del directorio del cliente.",
      });
      onClose();
    } catch (error) {
      setSubmitError(normalizeApiError(error).message);
    }
  }

  async function removeDevice() {
    if (!device) return;
    setDeleting(true);
    setSubmitError(null);
    try {
      await customerDevicesApi.deleteCustomerDevice(businessId, device.id);
      await onSaved({
        tone: "success",
        message: "El dispositivo fue desactivado y su historial permanece conservado.",
      });
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
        className="tm-review-panel tm-customer-device-editor"
        role="dialog"
        aria-modal="true"
        aria-labelledby="customer-device-editor-title"
      >
        <header className="tm-review-panel__head">
          <div>
            <h2 id="customer-device-editor-title">
              {isEditing ? "Editar dispositivo" : "Registrar dispositivo"}
            </h2>
            <p>Conserva la identificación técnica del equipo antes de abrir una orden.</p>
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

        <form className="tm-review-panel__body tm-customer-device-form" onSubmit={handleSubmit(submit)}>
          {submitError ? <FormAlert>{submitError}</FormAlert> : null}

          <fieldset>
            <legend>Propietario</legend>
            <SelectField label="Cliente" error={errors.customerId?.message} {...register("customerId")}>
              <option value="">Selecciona un cliente</option>
              {customers.map((customer) => (
                <option key={customer.id} value={customer.id}>
                  {customer.name} {customer.surname} · {customer.documentTypeCode} {customer.documentNumber}
                </option>
              ))}
            </SelectField>
          </fieldset>

          <fieldset>
            <legend>Equipo</legend>
            <div className="tm-customer-device-form__grid">
              <SelectField label="Tipo de dispositivo" error={errors.deviceTypeId?.message} {...register("deviceTypeId")}>
                <option value="">Selecciona un tipo</option>
                {deviceTypes.map((deviceType) => (
                  <option key={deviceType.id} value={deviceType.id}>{deviceType.name}</option>
                ))}
              </SelectField>
              <SelectField label="Marca" error={errors.brandId?.message} {...register("brandId")}>
                <option value="">Selecciona una marca</option>
                {brands.map((brand) => <option key={brand.id} value={brand.id}>{brand.name}</option>)}
              </SelectField>
              <TextField
                label="Modelo"
                maxLength={50}
                autoComplete="off"
                placeholder="iPhone 13 Pro"
                error={errors.model?.message}
                {...register("model")}
              />
              <TextField
                label="Color"
                maxLength={20}
                autoComplete="off"
                placeholder="Azul"
                error={errors.color?.message}
                {...register("color")}
              />
            </div>
          </fieldset>

          <fieldset>
            <legend>Identificadores técnicos</legend>
            <div className="tm-customer-device-form__grid">
              <TextField
                label="Número de serie"
                maxLength={60}
                autoComplete="off"
                placeholder="Opcional"
                error={errors.serialNumber?.message}
                hint="Puedes dejarlo vacío si el equipo no lo muestra."
                {...register("serialNumber")}
              />
              <TextField
                label="IMEI"
                maxLength={60}
                autoComplete="off"
                placeholder="Opcional"
                error={errors.imei?.message}
                hint="Debe ser único para este cliente dentro del negocio."
                {...register("imei")}
              />
            </div>
          </fieldset>

          {device ? (
            <section className="tm-customer-device-danger">
              <div>
                <h3>Desactivar dispositivo</h3>
                <p>Dejará de aparecer en este directorio; sus registros históricos se conservan.</p>
              </div>
              {confirmDelete ? (
                <div className="tm-customer-device-danger__confirm">
                  <p>Esta acción afecta inmediatamente la selección de equipos activos.</p>
                  <div>
                    <button
                      ref={keepRef}
                      className="tm-button tm-button--secondary"
                      type="button"
                      onClick={() => setConfirmDelete(false)}
                      disabled={deleting}
                    >
                      Conservar dispositivo
                    </button>
                    <button
                      className="tm-button tm-button--danger"
                      type="button"
                      onClick={() => void removeDevice()}
                      disabled={deleting}
                    >
                      {deleting ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Trash2 aria-hidden="true" />}
                      Desactivar
                    </button>
                  </div>
                </div>
              ) : (
                <button className="tm-button tm-button--danger" type="button" onClick={() => setConfirmDelete(true)}>
                  Desactivar
                </button>
              )}
            </section>
          ) : null}

          {!confirmDelete ? (
            <footer className="tm-customer-device-form__actions">
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
                disabled={isSubmitting || deleting}
              >
                {isSubmitting ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : isEditing ? <Check aria-hidden="true" /> : <Plus aria-hidden="true" />}
                {isSubmitting ? "Guardando…" : isEditing ? "Guardar cambios" : "Registrar dispositivo"}
              </button>
            </footer>
          ) : null}
        </form>
      </section>
    </div>
  );
}

function DeviceRow({
  device,
  canUpdate,
  onEdit,
}: {
  device: CustomerDevice;
  canUpdate: boolean;
  onEdit: (trigger: HTMLButtonElement) => void;
}) {
  return (
    <article className="tm-customer-device-row">
      <span className="tm-customer-device-row__icon"><Smartphone aria-hidden="true" /></span>
      <div className="tm-customer-device-row__identity">
        <div><h3>{device.brandName} {device.model}</h3><span>{device.deviceTypeName}</span></div>
        <p>{device.color} · Registrado el {formatRegistrationDate(device.registeredAt)}</p>
      </div>
      <dl className="tm-customer-device-row__identifiers">
        <div><dt>Serie</dt><dd>{device.serialNumber || "Sin registrar"}</dd></div>
        <div><dt>IMEI</dt><dd>{device.imei || "Sin registrar"}</dd></div>
      </dl>
      <div className="tm-customer-device-row__registered-by">
        <small>Recibido por</small>
        <strong>{device.registeredByBusinessUserName}</strong>
      </div>
      {canUpdate ? (
        <button className="tm-button tm-button--secondary" type="button" onClick={(event) => onEdit(event.currentTarget)}>
          <Pencil aria-hidden="true" /> Editar
        </button>
      ) : null}
    </article>
  );
}

export function CustomerDevicesWorkspace({ businessId }: { businessId: number }) {
  const { businesses, businessContext, selectBusiness } = useAuth();
  const router = useRouter();
  const [devices, setDevices] = useState<CustomerDevice[]>([]);
  const [deviceTypes, setDeviceTypes] = useState<BusinessCatalogItem[]>([]);
  const [brands, setBrands] = useState<BusinessCatalogItem[]>([]);
  const [query, setQuery] = useState("");
  const [selectedDeviceType, setSelectedDeviceType] = useState("all");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<ApiClientError | null>(null);
  const [catalogError, setCatalogError] = useState<string | null>(null);
  const [notice, setNotice] = useState<Notice | null>(null);
  const [editor, setEditor] = useState<{
    device: CustomerDevice | null;
    initialCustomerId?: number;
  } | null>(null);
  const editorTriggerRef = useRef<HTMLButtonElement | null>(null);
  const exists = businesses.some((business) => business.id === businessId);
  const contextReady = businessContext?.businessId === businessId;
  const permissions = contextReady ? businessContext.permissions : [];
  const canView = hasPermission(permissions, "CUSTOMER_DEVICE_VIEW");
  const canCreate = hasPermission(permissions, "CUSTOMER_DEVICE_CREATE");
  const canUpdate = hasPermission(permissions, "CUSTOMER_DEVICE_UPDATE");
  const canViewCatalogs = hasPermission(permissions, "BUSINESS_CATALOG_VIEW");
  const actorBusinessUserId = contextReady ? businessContext.businessUserId : null;
  const activeDeviceTypes = useMemo(() => deviceTypes.filter((item) => item.active), [deviceTypes]);
  const activeBrands = useMemo(() => brands.filter((item) => item.active), [brands]);
  const customers = useMemo(() => buildCustomerDirectory(devices), [devices]);
  const editorReady = Boolean(
    actorBusinessUserId && activeDeviceTypes.length && activeBrands.length && customers.length,
  );
  const availableDeviceTypes = useMemo(() => {
    const byId = new Map<number, string>();
    devices.forEach((device) => byId.set(device.deviceTypeId, device.deviceTypeName));
    activeDeviceTypes.forEach((item) => byId.set(item.id, item.name));
    return Array.from(byId, ([id, name]) => ({ id, name }))
      .sort((a, b) => a.name.localeCompare(b.name, "es"));
  }, [activeDeviceTypes, devices]);
  const visibleCustomers = useMemo(() => {
    const normalizedQuery = query.trim().toLocaleLowerCase("es");

    return customers.flatMap((customer) => {
      const customerMatches = !normalizedQuery || [
        customer.name,
        customer.surname,
        customer.email,
        customer.documentTypeCode,
        customer.documentNumber,
      ].some((value) => value.toLocaleLowerCase("es").includes(normalizedQuery));
      const matchingDevices = customer.devices.filter((device) => {
        const typeMatches = selectedDeviceType === "all" || device.deviceTypeId === Number(selectedDeviceType);
        if (!typeMatches) return false;
        if (customerMatches) return true;
        return [
          device.deviceTypeName,
          device.brandName,
          device.model,
          device.serialNumber ?? "",
          device.imei ?? "",
          device.color,
        ].some((value) => value.toLocaleLowerCase("es").includes(normalizedQuery));
      });

      return matchingDevices.length ? [{ ...customer, devices: matchingDevices }] : [];
    });
  }, [customers, query, selectedDeviceType]);

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

  const loadDevices = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      setDevices(await customerDevicesApi.getAllCustomerDevices(businessId));
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
      setCatalogError("Tu rol no permite consultar tipos de dispositivo y marcas para editar equipos.");
      return;
    }
    setCatalogError(null);
    try {
      const [availableDeviceTypes, availableBrands] = await Promise.all([
        customerDevicesApi.getCustomerDeviceCatalog(businessId, "DEVICE_TYPE"),
        customerDevicesApi.getCustomerDeviceCatalog(businessId, "BRAND"),
      ]);
      setDeviceTypes(availableDeviceTypes);
      setBrands(availableBrands);
      if (!availableDeviceTypes.some((item) => item.active) || !availableBrands.some((item) => item.active)) {
        setCatalogError("Configura al menos un tipo de dispositivo y una marca antes de registrar equipos.");
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
    void loadDevices();
    if (canCreate || canUpdate) void loadCatalogs();
  }, [canCreate, canUpdate, canView, contextReady, loadCatalogs, loadDevices, router]);

  const handleSaved = useCallback(async (nextNotice: Notice) => {
    setDevices(await customerDevicesApi.getAllCustomerDevices(businessId));
    setNotice(nextNotice);
  }, [businessId]);

  const closeEditor = useCallback(() => {
    setEditor(null);
    requestAnimationFrame(() => editorTriggerRef.current?.focus());
  }, []);

  const openEditor = (
    trigger: HTMLButtonElement,
    device: CustomerDevice | null,
    initialCustomerId?: number,
  ) => {
    editorTriggerRef.current = trigger;
    setNotice(null);
    setEditor({ device, initialCustomerId });
  };

  if (!exists) {
    return <div className="tm-page-stack"><ErrorState title="Negocio no disponible" message="Este negocio no está asociado a tu cuenta o ya no está disponible." /></div>;
  }
  if (error && !contextReady) {
    return <div className="tm-page-stack"><ErrorState message={error.message} onRetry={() => void selectBusiness(businessId).catch(handleContextError)} /></div>;
  }
  if (!contextReady || !canView) return <FullPageLoader label="Preparando clientes y dispositivos" />;

  const hasFilters = query.trim().length > 0 || selectedDeviceType !== "all";

  return (
    <div className="tm-page-stack tm-customer-devices-page">
      <header className="tm-workspace-heading tm-customer-devices-heading">
        <div>
          <h1>Clientes y dispositivos</h1>
          <p>Consulta quién confió un equipo a {businessContext.businessName} y conserva su identificación técnica.</p>
        </div>
        {canCreate ? (
          <button
            className="tm-button tm-button--primary"
            type="button"
            disabled={!editorReady}
            title={!editorReady ? "Necesitas clientes vinculados y catálogos disponibles" : undefined}
            onClick={(event) => openEditor(event.currentTarget, null)}
          >
            <Plus aria-hidden="true" /> Registrar dispositivo
          </button>
        ) : null}
      </header>

      {notice ? <FormAlert tone={notice.tone}>{notice.message}</FormAlert> : null}
      {catalogError && (canCreate || canUpdate) ? <FormAlert tone="warning">{catalogError}</FormAlert> : null}
      {(canCreate || canUpdate) && !actorBusinessUserId ? (
        <FormAlert tone="warning">Necesitas una membresía activa dentro del negocio para registrar o editar dispositivos.</FormAlert>
      ) : null}
      {canCreate && !loading && !customers.length ? (
        <FormAlert tone="warning">La vinculación inicial de una cuenta de cliente todavía no está disponible en este flujo.</FormAlert>
      ) : null}
      {error ? <ErrorState message={error.message} onRetry={() => void loadDevices()} /> : null}

      {!error ? (
        <section className="tm-customer-directory">
          <header className="tm-customer-directory__head">
            <div className="tm-customer-directory__identity">
              <span><UsersRound aria-hidden="true" /></span>
              <div><h2>Directorio operativo</h2><p>Clientes agrupados con los equipos registrados en este negocio.</p></div>
            </div>
            {!loading ? (
              <div className="tm-customer-directory__summary">
                <small>{customers.length} {customers.length === 1 ? "cliente" : "clientes"}</small>
                <strong>{devices.length} {devices.length === 1 ? "dispositivo activo" : "dispositivos activos"}</strong>
              </div>
            ) : null}
          </header>

          <div className="tm-customer-directory__toolbar">
            <label className="tm-customer-search" htmlFor="customer-device-search">
              <Search aria-hidden="true" />
              <span><small>Buscar en el directorio</small><input id="customer-device-search" type="search" value={query} onChange={(event) => setQuery(event.target.value)} placeholder="Nombre, documento o equipo" /></span>
              {query ? <button type="button" aria-label="Limpiar búsqueda" onClick={() => setQuery("")}><X aria-hidden="true" /></button> : null}
            </label>
            <div className="tm-customer-device-filter">
              <label htmlFor="customer-device-type-filter"><Smartphone aria-hidden="true" /><span><small>Tipo de dispositivo</small><strong>{selectedDeviceType === "all" ? "Todos" : availableDeviceTypes.find((item) => item.id === Number(selectedDeviceType))?.name ?? "Dispositivo"}</strong></span><ChevronDown aria-hidden="true" /></label>
              <select id="customer-device-type-filter" value={selectedDeviceType} onChange={(event) => setSelectedDeviceType(event.target.value)}>
                <option value="all">Todos los dispositivos</option>
                {availableDeviceTypes.map((deviceType) => <option key={deviceType.id} value={deviceType.id}>{deviceType.name}</option>)}
              </select>
            </div>
          </div>

          {loading ? (
            <div className="tm-customer-directory__loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando directorio</div>
          ) : visibleCustomers.length ? (
            <div className="tm-customer-groups">
              {visibleCustomers.map((customer) => (
                <section key={customer.id}>
                  <header>
                    <span className="tm-customer-avatar" aria-hidden="true">{customerInitials(customer)}</span>
                    <div className="tm-customer-identity">
                      <h3>{customer.name} {customer.surname}</h3>
                      <p><Fingerprint aria-hidden="true" /> {customer.documentTypeCode} {customer.documentNumber}<span aria-hidden="true">·</span><Mail aria-hidden="true" /> {customer.email}</p>
                    </div>
                    <span className="tm-customer-device-count">{customer.devices.length} {customer.devices.length === 1 ? "equipo" : "equipos"}</span>
                    {canCreate && editorReady ? (
                      <button type="button" onClick={(event) => openEditor(event.currentTarget, null, customer.id)}>
                        <Plus aria-hidden="true" /> Añadir equipo
                      </button>
                    ) : null}
                  </header>
                  <div className="tm-customer-device-list">
                    {customer.devices.map((device) => (
                      <DeviceRow
                        key={device.id}
                        device={device}
                        canUpdate={canUpdate && editorReady}
                        onEdit={(trigger) => openEditor(trigger, device)}
                      />
                    ))}
                  </div>
                </section>
              ))}
            </div>
          ) : (
            <EmptyState
              title={hasFilters ? "No encontramos coincidencias" : "Todavía no hay equipos registrados"}
              message={hasFilters ? "Prueba con otro nombre, documento, identificador o tipo de dispositivo." : "Los clientes aparecerán aquí cuando tengan un dispositivo vinculado a este negocio."}
              action={hasFilters ? <button className="tm-button tm-button--secondary" type="button" onClick={() => { setQuery(""); setSelectedDeviceType("all"); }}>Limpiar filtros</button> : undefined}
            />
          )}

          {!loading && devices.length ? (
            <footer className="tm-customer-directory__footer">
              <button className="tm-icon-button" type="button" aria-label="Actualizar directorio" onClick={() => void loadDevices()}><RefreshCw aria-hidden="true" /></button>
              <p><strong>Registro por negocio</strong><span>Los equipos de este directorio pertenecen únicamente al negocio seleccionado.</span></p>
            </footer>
          ) : null}
        </section>
      ) : null}

      {editor && actorBusinessUserId ? (
        <CustomerDeviceEditor
          businessId={businessId}
          device={editor.device}
          customers={customers}
          deviceTypes={activeDeviceTypes}
          brands={activeBrands}
          actorBusinessUserId={actorBusinessUserId}
          initialCustomerId={editor.initialCustomerId}
          onClose={closeEditor}
          onSaved={handleSaved}
        />
      ) : null}

      {!canCreate && !canUpdate ? (
        <div className="tm-service-readonly"><CircleAlert aria-hidden="true" /><p><strong>Acceso de consulta</strong><span>Puedes revisar clientes y dispositivos, pero tu rol no permite modificarlos.</span></p></div>
      ) : null}
    </div>
  );
}
