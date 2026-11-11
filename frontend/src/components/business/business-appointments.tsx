"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import {
  BadgeCheck,
  CalendarDays,
  CalendarClock,
  Check,
  CheckCircle2,
  ChevronLeft,
  ChevronRight,
  CircleAlert,
  Clock3,
  LoaderCircle,
  MapPin,
  Pencil,
  Plus,
  RefreshCw,
  Smartphone,
  Trash2,
  UserX,
  UserRound,
  Wrench,
  X,
  XCircle,
  type LucideIcon,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { useForm } from "react-hook-form";

import { EmptyState, ErrorState, FormAlert, FullPageLoader } from "@/components/ui/feedback";
import { SelectField, TextField } from "@/components/ui/form-controls";
import * as appointmentsApi from "@/features/appointments/api";
import {
  appointmentSchema,
  normalizeScheduledAt,
  toDateTimeLocal,
  type AppointmentFormValues,
} from "@/features/appointments/schemas";
import * as servicesApi from "@/features/business-services/api";
import * as customerDevicesApi from "@/features/customer-devices/api";
import { ApiClientError, normalizeApiError } from "@/lib/api/errors";
import { hasPermission } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";
import type { PageResponse } from "@/types/api";
import type { Appointment } from "@/types/appointment";
import type { BusinessContextBranch } from "@/types/business";
import type { BusinessCatalogItem, BusinessService } from "@/types/business-service";
import type { CustomerDevice, CustomerDirectoryEntry } from "@/types/customer-device";

const PAGE_SIZE = 12;

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

  return Array.from(directory.values()).sort((a, b) =>
    `${a.name} ${a.surname}`.localeCompare(`${b.name} ${b.surname}`, "es"),
  );
}

function appointmentValues(
  appointment: Appointment | null,
  activeBranchId: number | null,
  statuses: BusinessCatalogItem[],
): AppointmentFormValues {
  const initialStatus = statuses.find((status) => status.code === "SCHEDULED") ?? statuses[0];

  return {
    branchId: appointment ? String(appointment.branchId) : activeBranchId ? String(activeBranchId) : "",
    customerId: appointment ? String(appointment.customerId) : "",
    customerDeviceId: appointment ? String(appointment.customerDeviceId) : "",
    businessServiceId: appointment ? String(appointment.businessServiceId) : "",
    statusId: appointment ? String(appointment.statusId) : initialStatus ? String(initialStatus.id) : "",
    scheduledAt: appointment ? toDateTimeLocal(appointment.scheduledAt) : "",
    problemDescription: appointment?.problemDescription ?? "",
  };
}

function formatDay(value: string) {
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return "Fecha no disponible";
  return new Intl.DateTimeFormat("es-CO", {
    weekday: "long",
    day: "numeric",
    month: "long",
    year: "numeric",
  }).format(date);
}

function formatTime(value: string) {
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return "Hora no disponible";
  return new Intl.DateTimeFormat("es-CO", {
    hour: "2-digit",
    minute: "2-digit",
    hour12: true,
  }).format(date);
}

function dateKey(value: string) {
  return value.slice(0, 10);
}

function statusClass(code: string) {
  const normalized = code.toLocaleLowerCase("es").replaceAll("_", "-");
  return `is-${normalized}`;
}

function statusIcon(code: string): LucideIcon {
  switch (code) {
    case "SCHEDULED":
      return CalendarClock;
    case "CONFIRMED":
      return BadgeCheck;
    case "COMPLETED":
      return CheckCircle2;
    case "NO_SHOW":
      return UserX;
    case "CANCELLED":
      return XCircle;
    default:
      return CalendarDays;
  }
}

function AppointmentEditor({
  businessId,
  appointment,
  activeBranchId,
  branches,
  customers,
  services,
  statuses,
  onClose,
  onSaved,
}: {
  businessId: number;
  appointment: Appointment | null;
  activeBranchId: number | null;
  branches: BusinessContextBranch[];
  customers: CustomerDirectoryEntry[];
  services: BusinessService[];
  statuses: BusinessCatalogItem[];
  onClose: () => void;
  onSaved: (notice: Notice, removed?: boolean) => Promise<void>;
}) {
  const panelRef = useRef<HTMLElement>(null);
  const closeRef = useRef<HTMLButtonElement>(null);
  const keepRef = useRef<HTMLButtonElement>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const isEditing = Boolean(appointment);
  const {
    register,
    handleSubmit,
    watch,
    setValue,
    formState: { errors, isSubmitting },
  } = useForm<AppointmentFormValues>({
    resolver: zodResolver(appointmentSchema),
    defaultValues: appointmentValues(appointment, activeBranchId, statuses),
  });
  const selectedCustomerId = watch("customerId");
  const selectedDeviceId = watch("customerDeviceId");
  const selectedCustomer = customers.find((customer) => customer.id === Number(selectedCustomerId));
  const availableDevices = useMemo(() => selectedCustomer?.devices ?? [], [selectedCustomer]);

  useEffect(() => {
    if (!selectedDeviceId) return;
    if (!availableDevices.some((device) => device.id === Number(selectedDeviceId))) {
      setValue("customerDeviceId", "", { shouldValidate: true });
    }
  }, [availableDevices, selectedDeviceId, setValue]);

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

  async function submit(values: AppointmentFormValues) {
    setSubmitError(null);
    const sharedRequest = {
      customerId: Number(values.customerId),
      customerDeviceId: Number(values.customerDeviceId),
      businessServiceId: Number(values.businessServiceId),
      statusId: Number(values.statusId),
      scheduledAt: normalizeScheduledAt(values.scheduledAt),
      problemDescription: values.problemDescription.trim(),
    };

    try {
      if (appointment) {
        await appointmentsApi.updateAppointment(businessId, appointment.id, {
          ...sharedRequest,
          active: appointment.active,
        });
      } else {
        await appointmentsApi.createAppointment(businessId, {
          ...sharedRequest,
          branchId: Number(values.branchId),
        });
      }
      await onSaved({
        tone: "success",
        message: appointment
          ? "La cita quedó actualizada."
          : "La cita quedó registrada en la agenda.",
      });
      onClose();
    } catch (error) {
      setSubmitError(normalizeApiError(error).message);
    }
  }

  async function removeAppointment() {
    if (!appointment) return;
    setDeleting(true);
    setSubmitError(null);
    try {
      await appointmentsApi.deleteAppointment(businessId, appointment.id);
      await onSaved(
        {
          tone: "success",
          message: "La cita fue desactivada y dejó de aparecer en la agenda.",
        },
        true,
      );
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
        className="tm-review-panel tm-appointment-editor"
        role="dialog"
        aria-modal="true"
        aria-labelledby="appointment-editor-title"
      >
        <header className="tm-review-panel__head">
          <div>
            <h2 id="appointment-editor-title">{isEditing ? "Editar cita" : "Agendar cita"}</h2>
            <p>Relaciona la visita con una sede, un equipo y un servicio del negocio.</p>
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

        <form className="tm-review-panel__body tm-appointment-form" onSubmit={handleSubmit(submit)}>
          {submitError ? <FormAlert>{submitError}</FormAlert> : null}

          <fieldset>
            <legend>Atención</legend>
            {appointment ? (
              <div className="tm-appointment-locked-branch">
                <MapPin aria-hidden="true" />
                <span><small>Sede de la cita</small><strong>{appointment.branchName}</strong></span>
                <input type="hidden" {...register("branchId")} />
              </div>
            ) : (
              <SelectField label="Sede" error={errors.branchId?.message} {...register("branchId")}>
                <option value="">Selecciona una sede</option>
                {branches.map((branch) => (
                  <option key={branch.id} value={branch.id}>
                    {branch.name}{branch.primaryBranch ? " · Principal" : ""}
                  </option>
                ))}
              </SelectField>
            )}
            <div className="tm-appointment-form__grid">
              <TextField
                label="Fecha y hora"
                type="datetime-local"
                error={errors.scheduledAt?.message}
                {...register("scheduledAt")}
              />
              <SelectField label="Estado" error={errors.statusId?.message} {...register("statusId")}>
                <option value="">Selecciona un estado</option>
                {statuses.map((status) => <option key={status.id} value={status.id}>{status.name}</option>)}
              </SelectField>
            </div>
          </fieldset>

          <fieldset>
            <legend>Cliente y dispositivo</legend>
            <div className="tm-appointment-form__grid">
              <SelectField label="Cliente" error={errors.customerId?.message} {...register("customerId")}>
                <option value="">Selecciona un cliente</option>
                {customers.map((customer) => (
                  <option key={customer.id} value={customer.id}>
                    {customer.name} {customer.surname} · {customer.documentTypeCode} {customer.documentNumber}
                  </option>
                ))}
              </SelectField>
              <SelectField
                label="Dispositivo"
                error={errors.customerDeviceId?.message}
                disabled={!selectedCustomerId}
                {...register("customerDeviceId")}
              >
                <option value="">{selectedCustomerId ? "Selecciona un dispositivo" : "Primero selecciona un cliente"}</option>
                {availableDevices.map((device) => (
                  <option key={device.id} value={device.id}>
                    {device.brandName} {device.model} · {device.deviceTypeName}
                  </option>
                ))}
              </SelectField>
            </div>
          </fieldset>

          <fieldset>
            <legend>Solicitud</legend>
            <SelectField label="Servicio" error={errors.businessServiceId?.message} {...register("businessServiceId")}>
              <option value="">Selecciona un servicio</option>
              {services.map((service) => (
                <option key={service.id} value={service.id}>{service.name} · {service.deviceTypeName}</option>
              ))}
            </SelectField>
            <div className="tm-field">
              <label htmlFor="appointment-problem-description">Motivo de la cita</label>
              <textarea
                id="appointment-problem-description"
                maxLength={200}
                placeholder="Describe brevemente el problema reportado"
                aria-invalid={Boolean(errors.problemDescription)}
                aria-describedby={errors.problemDescription ? "appointment-problem-description-error" : undefined}
                {...register("problemDescription")}
              />
              {errors.problemDescription ? (
                <p className="tm-field__error" id="appointment-problem-description-error" role="alert">
                  {errors.problemDescription.message}
                </p>
              ) : null}
            </div>
          </fieldset>

          {appointment ? (
            <section className="tm-appointment-danger">
              <div>
                <h3>Desactivar cita</h3>
                <p>La cita dejará de aparecer en la agenda activa.</p>
              </div>
              {confirmDelete ? (
                <div className="tm-appointment-danger__confirm">
                  <p>El registro permanecerá disponible en el historial del negocio.</p>
                  <div>
                    <button
                      ref={keepRef}
                      className="tm-button tm-button--secondary"
                      type="button"
                      onClick={() => setConfirmDelete(false)}
                      disabled={deleting}
                    >
                      Conservar cita
                    </button>
                    <button
                      className="tm-button tm-button--danger"
                      type="button"
                      onClick={() => void removeAppointment()}
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
            <footer className="tm-appointment-form__actions">
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
                {isSubmitting ? "Guardando…" : isEditing ? "Guardar cambios" : "Agendar cita"}
              </button>
            </footer>
          ) : null}
        </form>
      </section>
    </div>
  );
}

function AppointmentRow({
  appointment,
  statuses,
  canUpdate,
  editorAvailable,
  updatingStatus,
  onStatusChange,
  onEdit,
}: {
  appointment: Appointment;
  statuses: BusinessCatalogItem[];
  canUpdate: boolean;
  editorAvailable: boolean;
  updatingStatus: boolean;
  onStatusChange: (statusId: number) => void;
  onEdit: (trigger: HTMLButtonElement) => void;
}) {
  const StatusIcon = statusIcon(appointment.statusCode);

  return (
    <article className="tm-appointment-row">
      <div className="tm-appointment-row__time">
        <Clock3 aria-hidden="true" />
        <strong>{formatTime(appointment.scheduledAt)}</strong>
      </div>
      <div className="tm-appointment-row__customer">
        <span><UserRound aria-hidden="true" /></span>
        <div>
          <h3>{appointment.customerName} {appointment.customerSurname}</h3>
          <p>{appointment.customerEmail}</p>
        </div>
      </div>
      <div className="tm-appointment-row__work">
        <p><Smartphone aria-hidden="true" /><span>{appointment.customerDeviceLabel}</span></p>
        <p><Wrench aria-hidden="true" /><span>{appointment.businessServiceName}</span></p>
        <p><MapPin aria-hidden="true" /><span>{appointment.branchName}</span></p>
      </div>
      <p className="tm-appointment-row__problem">{appointment.problemDescription}</p>
      <div className="tm-appointment-row__actions">
        {canUpdate && statuses.length ? (
          <label className={`tm-appointment-status ${statusClass(appointment.statusCode)}`}>
            <span className="sr-only">Estado de la cita de {appointment.customerName}</span>
            {updatingStatus ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <StatusIcon aria-hidden="true" />}
            <select
              aria-label={`Estado de la cita de ${appointment.customerName} ${appointment.customerSurname}`}
              value={appointment.statusId}
              onChange={(event) => onStatusChange(Number(event.target.value))}
              disabled={updatingStatus}
            >
              {statuses.map((status) => <option key={status.id} value={status.id}>{status.name}</option>)}
            </select>
          </label>
        ) : (
          <span className={`tm-appointment-status tm-appointment-status--static ${statusClass(appointment.statusCode)}`}>
            <StatusIcon aria-hidden="true" /> {appointment.statusName}
          </span>
        )}
        {canUpdate ? (
          <button
            className="tm-button tm-button--secondary"
            type="button"
            disabled={!editorAvailable}
            title={!editorAvailable ? "La información necesaria para editar aún no está disponible" : undefined}
            onClick={(event) => onEdit(event.currentTarget)}
          >
            <Pencil aria-hidden="true" /> Editar
          </button>
        ) : null}
      </div>
    </article>
  );
}

export function BusinessAppointmentsWorkspace({ businessId }: { businessId: number }) {
  const { businesses, businessContext, activeBranchId, selectBusiness } = useAuth();
  const router = useRouter();
  const [appointments, setAppointments] = useState<PageResponse<Appointment> | null>(null);
  const [devices, setDevices] = useState<CustomerDevice[]>([]);
  const [services, setServices] = useState<BusinessService[]>([]);
  const [statuses, setStatuses] = useState<BusinessCatalogItem[]>([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(true);
  const [editorDataLoading, setEditorDataLoading] = useState(false);
  const [error, setError] = useState<ApiClientError | null>(null);
  const [editorDataError, setEditorDataError] = useState<string | null>(null);
  const [notice, setNotice] = useState<Notice | null>(null);
  const [editor, setEditor] = useState<Appointment | null | undefined>(undefined);
  const [updatingStatusId, setUpdatingStatusId] = useState<number | null>(null);
  const editorTriggerRef = useRef<HTMLButtonElement | null>(null);
  const exists = businesses.some((business) => business.id === businessId);
  const contextReady = businessContext?.businessId === businessId;
  const permissions = contextReady ? businessContext.permissions : [];
  const canView = hasPermission(permissions, "APPOINTMENT_VIEW");
  const canCreate = hasPermission(permissions, "APPOINTMENT_CREATE");
  const canUpdate = hasPermission(permissions, "APPOINTMENT_UPDATE");
  const canViewDevices = hasPermission(permissions, "CUSTOMER_DEVICE_VIEW");
  const canViewServices = hasPermission(permissions, "BUSINESS_SERVICE_VIEW");
  const canViewCatalogs = hasPermission(permissions, "BUSINESS_CATALOG_VIEW");
  const customers = useMemo(() => buildCustomerDirectory(devices), [devices]);
  const activeServices = useMemo(() => services.filter((service) => service.active), [services]);
  const activeStatuses = useMemo(
    () => statuses.filter((status) => status.active).sort((a, b) => (a.sortOrder ?? 0) - (b.sortOrder ?? 0)),
    [statuses],
  );
  const editorReady = Boolean(
    businessContext?.branches.length && customers.length && activeServices.length && activeStatuses.length,
  );

  const groupedAppointments = useMemo(() => {
    const groups = new Map<string, Appointment[]>();
    appointments?.items.forEach((appointment) => {
      const key = dateKey(appointment.scheduledAt);
      const group = groups.get(key) ?? [];
      group.push(appointment);
      groups.set(key, group);
    });
    return Array.from(groups.entries());
  }, [appointments]);

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

  const loadAppointments = useCallback(async (targetPage = page) => {
    setLoading(true);
    setError(null);
    try {
      setAppointments(await appointmentsApi.getAppointments(businessId, targetPage, PAGE_SIZE));
    } catch (requestError) {
      const normalized = normalizeApiError(requestError);
      if (normalized.status === 403) router.replace("/acceso-denegado");
      else setError(normalized);
    } finally {
      setLoading(false);
    }
  }, [businessId, page, router]);

  const loadEditorData = useCallback(async () => {
    if (!canViewDevices || !canViewServices || !canViewCatalogs) {
      setEditorDataError("Tu rol necesita consultar clientes, servicios y estados de cita para preparar una cita.");
      return;
    }
    setEditorDataLoading(true);
    setEditorDataError(null);
    try {
      const [availableDevices, availableServices, availableStatuses] = await Promise.all([
        customerDevicesApi.getAllCustomerDevices(businessId),
        servicesApi.getAllBusinessServices(businessId),
        servicesApi.getBusinessCatalogItems(businessId, "APPOINTMENT_STATUS"),
      ]);
      setDevices(availableDevices);
      setServices(availableServices);
      setStatuses(availableStatuses);

      if (!availableDevices.length || !availableServices.some((service) => service.active) || !availableStatuses.some((status) => status.active)) {
        setEditorDataError("Necesitas al menos un cliente con dispositivo, un servicio y un estado de cita activos.");
      }
    } catch (requestError) {
      setEditorDataError(normalizeApiError(requestError).message);
    } finally {
      setEditorDataLoading(false);
    }
  }, [businessId, canViewCatalogs, canViewDevices, canViewServices]);

  useEffect(() => {
    if (!contextReady) return;
    if (!canView) {
      router.replace("/acceso-denegado");
      return;
    }
    void loadAppointments(page);
    if (canCreate || canUpdate) void loadEditorData();
  }, [canCreate, canUpdate, canView, contextReady, loadAppointments, loadEditorData, page, router]);

  const closeEditor = useCallback(() => {
    setEditor(undefined);
    requestAnimationFrame(() => editorTriggerRef.current?.focus());
  }, []);

  const openEditor = (trigger: HTMLButtonElement, appointment: Appointment | null) => {
    editorTriggerRef.current = trigger;
    setNotice(null);
    setEditor(appointment);
  };

  const handleSaved = useCallback(async (nextNotice: Notice, removed = false) => {
    setNotice(nextNotice);
    const targetPage = removed && appointments?.items.length === 1 && page > 0 ? page - 1 : page;
    if (targetPage !== page) setPage(targetPage);
    else await loadAppointments(targetPage);
  }, [appointments?.items.length, loadAppointments, page]);

  async function changeStatus(appointmentId: number, statusId: number) {
    setUpdatingStatusId(appointmentId);
    setNotice(null);
    try {
      const updated = await appointmentsApi.updateAppointmentStatus(businessId, appointmentId, statusId);
      setAppointments((current) => current ? {
        ...current,
        items: current.items.map((appointment) => appointment.id === appointmentId ? updated : appointment),
      } : current);
      setNotice({ tone: "success", message: "El estado de la cita quedó actualizado." });
    } catch (requestError) {
      setNotice({ tone: "warning", message: normalizeApiError(requestError).message });
    } finally {
      setUpdatingStatusId(null);
    }
  }

  if (!exists) {
    return <div className="tm-page-stack"><ErrorState title="Negocio no disponible" message="Este negocio no está asociado a tu cuenta o ya no está disponible." /></div>;
  }
  if (error && !contextReady) {
    return <div className="tm-page-stack"><ErrorState message={error.message} onRetry={() => void selectBusiness(businessId).catch(handleContextError)} /></div>;
  }
  if (!contextReady || !canView) return <FullPageLoader label="Preparando la agenda" />;

  return (
    <div className="tm-page-stack tm-appointments-page">
      <header className="tm-workspace-heading tm-appointments-heading">
        <div>
          <h1>Citas</h1>
          <p>Organiza las visitas programadas de {businessContext.businessName} y conserva el equipo asociado a cada atención.</p>
        </div>
        {canCreate ? (
          <button
            className="tm-button tm-button--primary"
            type="button"
            disabled={!editorReady || editorDataLoading}
            title={!editorReady ? "Completa los datos necesarios para agendar" : undefined}
            onClick={(event) => openEditor(event.currentTarget, null)}
          >
            {editorDataLoading ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Plus aria-hidden="true" />}
            Agendar cita
          </button>
        ) : null}
      </header>

      {notice ? <FormAlert tone={notice.tone}>{notice.message}</FormAlert> : null}
      {editorDataError && (canCreate || canUpdate) ? <FormAlert tone="warning">{editorDataError}</FormAlert> : null}
      {error ? <ErrorState message={error.message} onRetry={() => void loadAppointments(page)} /> : null}

      {!error ? (
        <section className="tm-appointment-board">
          <header className="tm-appointment-board__head">
            <div className="tm-appointment-board__identity">
              <span><CalendarDays aria-hidden="true" /></span>
              <div>
                <h2>Agenda operativa</h2>
                <p>Consulta las atenciones programadas y los datos necesarios para recibir cada equipo.</p>
              </div>
            </div>
            {!loading && appointments ? (
              <div className="tm-appointment-board__summary">
                <small>Total registrado</small>
                <strong>{appointments.totalElements} {appointments.totalElements === 1 ? "cita" : "citas"}</strong>
              </div>
            ) : null}
          </header>

          {loading ? (
            <div className="tm-appointment-board__loading">
              <LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando agenda
            </div>
          ) : groupedAppointments.length ? (
            <div className="tm-appointment-days">
              {groupedAppointments.map(([day, dayAppointments]) => (
                <section key={day}>
                  <header>
                    <CalendarDays aria-hidden="true" />
                    <h3>{formatDay(dayAppointments[0].scheduledAt)}</h3>
                    <span>{dayAppointments.length} {dayAppointments.length === 1 ? "cita" : "citas"}</span>
                  </header>
                  <div className="tm-appointment-list">
                    {dayAppointments.map((appointment) => (
                      <AppointmentRow
                        key={appointment.id}
                        appointment={appointment}
                        statuses={activeStatuses}
                        canUpdate={canUpdate}
                        editorAvailable={editorReady && !editorDataLoading}
                        updatingStatus={updatingStatusId === appointment.id}
                        onStatusChange={(statusId) => void changeStatus(appointment.id, statusId)}
                        onEdit={(trigger) => openEditor(trigger, appointment)}
                      />
                    ))}
                  </div>
                </section>
              ))}
            </div>
          ) : (
            <EmptyState
              title="Todavía no hay citas activas"
              message="Las visitas programadas aparecerán aquí cuando el negocio registre su primera cita."
              action={canCreate && editorReady ? (
                <button className="tm-button tm-button--primary" type="button" onClick={(event) => openEditor(event.currentTarget, null)}>
                  <Plus aria-hidden="true" /> Agendar primera cita
                </button>
              ) : undefined}
            />
          )}

          {!loading && appointments?.totalElements ? (
            <footer className="tm-appointment-board__footer">
              <button className="tm-icon-button" type="button" aria-label="Actualizar agenda" onClick={() => void loadAppointments(page)}>
                <RefreshCw aria-hidden="true" />
              </button>
              <p>
                <strong>Página {appointments.page + 1} de {Math.max(appointments.totalPages, 1)}</strong>
                <span>Mostrando {appointments.items.length} de {appointments.totalElements} citas.</span>
              </p>
              <div className="tm-appointment-pagination" aria-label="Paginación de citas">
                <button
                  className="tm-icon-button"
                  type="button"
                  aria-label="Página anterior"
                  disabled={!appointments.hasPrevious}
                  onClick={() => setPage((current) => Math.max(0, current - 1))}
                >
                  <ChevronLeft aria-hidden="true" />
                </button>
                <button
                  className="tm-icon-button"
                  type="button"
                  aria-label="Página siguiente"
                  disabled={!appointments.hasNext}
                  onClick={() => setPage((current) => current + 1)}
                >
                  <ChevronRight aria-hidden="true" />
                </button>
              </div>
            </footer>
          ) : null}
        </section>
      ) : null}

      {editor !== undefined ? (
        <AppointmentEditor
          businessId={businessId}
          appointment={editor}
          activeBranchId={activeBranchId}
          branches={businessContext.branches}
          customers={customers}
          services={activeServices}
          statuses={activeStatuses}
          onClose={closeEditor}
          onSaved={handleSaved}
        />
      ) : null}

      {!canCreate && !canUpdate ? (
        <div className="tm-service-readonly">
          <CircleAlert aria-hidden="true" />
          <p><strong>Acceso de consulta</strong><span>Puedes revisar la agenda, pero tu rol no permite modificar citas.</span></p>
        </div>
      ) : null}
    </div>
  );
}
