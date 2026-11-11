"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import {
  Banknote,
  Camera,
  CalendarClock,
  Check,
  CheckCircle2,
  ChevronLeft,
  ChevronRight,
  CircleAlert,
  ClipboardList,
  Eye,
  ExternalLink,
  Filter,
  FlaskConical,
  History,
  ImagePlus,
  LoaderCircle,
  MapPin,
  ReceiptText,
  PackageCheck,
  PackageOpen,
  PauseCircle,
  Plus,
  RefreshCw,
  Search,
  Save,
  Smartphone,
  Trash2,
  Upload,
  UserRound,
  Wrench,
  Zap,
  X,
  XCircle,
  type LucideIcon,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useMemo, useRef, useState, type FormEvent } from "react";
import { useForm } from "react-hook-form";

import { EmptyState, ErrorState, FormAlert, FullPageLoader } from "@/components/ui/feedback";
import { SelectField, TextField } from "@/components/ui/form-controls";
import { QuickRepairOrderEditor } from "@/components/business/quick-repair-order-editor";
import { QuotationEditor } from "@/components/business/quotation-editor";
import * as servicesApi from "@/features/business-services/api";
import * as customerDevicesApi from "@/features/customer-devices/api";
import * as repairOrdersApi from "@/features/repair-orders/api";
import { getQuotationStatusTargets } from "@/features/quotations/transitions";
import { getRepairOrderStatusTargets } from "@/features/repair-orders/transitions";
import {
  normalizeOptionalDateTime,
  normalizeOptionalText,
  REPAIR_ORDER_EVIDENCE_DESCRIPTION_MAX_LENGTH,
  repairOrderSchema,
  validateRepairOrderEvidence,
  type RepairOrderFormValues,
} from "@/features/repair-orders/schemas";
import { ApiClientError, normalizeApiError } from "@/lib/api/errors";
import { hasPermission } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";
import type { PageResponse } from "@/types/api";
import type { BusinessContextBranch } from "@/types/business";
import type { BusinessCatalogItem, BusinessService } from "@/types/business-service";
import type { Quotation } from "@/types/quotation";
import type { CustomerDevice, CustomerDirectoryEntry } from "@/types/customer-device";
import type {
  BusinessTechnicianOption,
  RepairOrderAttachment,
  RepairOrder,
  RepairOrderFilters,
  RepairOrderHistoryEntry,
  RepairOrderHistoryVisibility,
} from "@/types/repair-order";

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

function formatDateTime(value: string | null) {
  if (!value) return "Sin definir";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return "Fecha no disponible";
  return new Intl.DateTimeFormat("es-CO", {
    day: "2-digit",
    month: "short",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  }).format(date);
}

function formatFileSize(value: number) {
  if (value < 1024 * 1024) return `${Math.max(1, Math.round(value / 1024))} KB`;
  return `${new Intl.NumberFormat("es-CO", { maximumFractionDigits: 1 }).format(value / (1024 * 1024))} MB`;
}

function formatMoney(value: number | null) {
  if (value === null) return "Sin estimar";
  return new Intl.NumberFormat("es-CO", {
    style: "currency",
    currency: "COP",
    maximumFractionDigits: 0,
  }).format(value);
}

function statusClass(code: string) {
  return `is-${code.toLocaleLowerCase("es").replaceAll("_", "-")}`;
}

function repairStatusIcon(code: string): LucideIcon {
  switch (code) {
    case "RECEIVED":
      return PackageOpen;
    case "DIAGNOSIS":
      return Search;
    case "WAITING_APPROVAL":
      return PauseCircle;
    case "WAITING_PARTS":
      return CalendarClock;
    case "REPAIRING":
      return Wrench;
    case "TESTING":
      return FlaskConical;
    case "READY_FOR_PICKUP":
      return PackageCheck;
    case "DELIVERED":
      return CheckCircle2;
    case "CANCELLED":
      return XCircle;
    default:
      return ClipboardList;
  }
}

function defaultFormValues(activeBranchId: number | null): RepairOrderFormValues {
  return {
    branchId: activeBranchId ? String(activeBranchId) : "",
    customerId: "",
    customerDeviceId: "",
    appointmentId: "",
    businessServiceId: "",
    assignedBusinessUserId: "",
    problemDescription: "",
    diagnosis: "",
    estimatedPrice: "",
    estimatedDeliveryAt: "",
    receivedAt: "",
  };
}

function RepairOrderEditor({
  businessId,
  activeBranchId,
  branches,
  customers,
  services,
  onClose,
  onSaved,
}: {
  businessId: number;
  activeBranchId: number | null;
  branches: BusinessContextBranch[];
  customers: CustomerDirectoryEntry[];
  services: BusinessService[];
  onClose: () => void;
  onSaved: (order: RepairOrder) => Promise<void>;
}) {
  const panelRef = useRef<HTMLElement>(null);
  const closeRef = useRef<HTMLButtonElement>(null);
  const [technicians, setTechnicians] = useState<BusinessTechnicianOption[]>([]);
  const [techniciansLoading, setTechniciansLoading] = useState(false);
  const [techniciansError, setTechniciansError] = useState<string | null>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const {
    register,
    handleSubmit,
    watch,
    setValue,
    formState: { errors, isSubmitting },
  } = useForm<RepairOrderFormValues>({
    resolver: zodResolver(repairOrderSchema),
    defaultValues: defaultFormValues(activeBranchId),
  });
  const selectedBranchId = watch("branchId");
  const selectedCustomerId = watch("customerId");
  const selectedDeviceId = watch("customerDeviceId");
  const selectedCustomer = customers.find((customer) => customer.id === Number(selectedCustomerId));
  const availableDevices = useMemo(() => selectedCustomer?.devices ?? [], [selectedCustomer]);
  const selectedDevice = availableDevices.find((device) => device.id === Number(selectedDeviceId));
  const compatibleServices = useMemo(
    () => services.filter((service) => !selectedDevice || service.deviceTypeId === selectedDevice.deviceTypeId),
    [selectedDevice, services],
  );

  useEffect(() => {
    closeRef.current?.focus();
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape" && !isSubmitting) onClose();
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
  }, [isSubmitting, onClose]);

  useEffect(() => {
    if (!selectedDeviceId) return;
    if (!availableDevices.some((device) => device.id === Number(selectedDeviceId))) {
      setValue("customerDeviceId", "", { shouldValidate: true });
      setValue("businessServiceId", "");
    }
  }, [availableDevices, selectedDeviceId, setValue]);

  useEffect(() => {
    if (!selectedBranchId) {
      setTechnicians([]);
      setValue("assignedBusinessUserId", "");
      return;
    }
    let active = true;
    setTechniciansLoading(true);
    setTechniciansError(null);
    repairOrdersApi.getRepairOrderTechnicians(businessId, Number(selectedBranchId))
      .then((items) => {
        if (!active) return;
        setTechnicians(items);
        setValue("assignedBusinessUserId", "");
        if (!items.length) setTechniciansError("Esta sede no tiene técnicos activos disponibles.");
      })
      .catch((error) => {
        if (active) setTechniciansError(normalizeApiError(error).message);
      })
      .finally(() => {
        if (active) setTechniciansLoading(false);
      });
    return () => { active = false; };
  }, [businessId, selectedBranchId, setValue]);

  async function submit(values: RepairOrderFormValues) {
    setSubmitError(null);
    try {
      const created = await repairOrdersApi.createRepairOrder(businessId, {
        branchId: Number(values.branchId),
        customerId: Number(values.customerId),
        customerDeviceId: Number(values.customerDeviceId),
        appointmentId: values.appointmentId ? Number(values.appointmentId) : null,
        businessServiceId: Number(values.businessServiceId),
        assignedBusinessUserId: Number(values.assignedBusinessUserId),
        problemDescription: values.problemDescription.trim(),
        diagnosis: normalizeOptionalText(values.diagnosis),
        estimatedPrice: values.estimatedPrice ? Number(values.estimatedPrice) : null,
        estimatedDeliveryAt: normalizeOptionalDateTime(values.estimatedDeliveryAt),
        receivedAt: normalizeOptionalDateTime(values.receivedAt),
      });
      await onSaved(created);
      onClose();
    } catch (error) {
      setSubmitError(normalizeApiError(error).message);
    }
  }

  return (
    <div className="tm-review-backdrop">
      <section
        ref={panelRef}
        className="tm-review-panel tm-repair-editor"
        role="dialog"
        aria-modal="true"
        aria-labelledby="repair-editor-title"
      >
        <header className="tm-review-panel__head">
          <div>
            <h2 id="repair-editor-title">Recibir equipo</h2>
            <p>La orden se creará en estado Recibida y quedará ligada a la sede seleccionada.</p>
          </div>
          <button ref={closeRef} className="tm-icon-button" type="button" aria-label="Cerrar" onClick={onClose} disabled={isSubmitting}>
            <X aria-hidden="true" />
          </button>
        </header>

        <form className="tm-review-panel__body tm-repair-form" onSubmit={handleSubmit(submit)}>
          {submitError ? <FormAlert>{submitError}</FormAlert> : null}
          <fieldset>
            <legend>Recepción</legend>
            <div className="tm-repair-form__grid">
              <SelectField label="Sede" error={errors.branchId?.message} {...register("branchId")}>
                <option value="">Selecciona una sede</option>
                {branches.map((branch) => (
                  <option key={branch.id} value={branch.id}>{branch.name}{branch.primaryBranch ? " · Principal" : ""}</option>
                ))}
              </SelectField>
              <SelectField
                label="Técnico asignado"
                error={errors.assignedBusinessUserId?.message ?? techniciansError ?? undefined}
                disabled={!selectedBranchId || techniciansLoading}
                {...register("assignedBusinessUserId")}
              >
                <option value="">{techniciansLoading ? "Cargando técnicos…" : "Selecciona un técnico"}</option>
                {technicians.map((technician) => (
                  <option key={technician.businessUserId} value={technician.businessUserId}>
                    {technician.userName} {technician.userSurname} · {technician.roleName}
                  </option>
                ))}
              </SelectField>
            </div>
            <TextField label="Fecha y hora de recepción" type="datetime-local" hint="Si la dejas vacía, se registrará la hora actual." {...register("receivedAt")} />
          </fieldset>

          <fieldset>
            <legend>Cliente y dispositivo</legend>
            <div className="tm-repair-form__grid">
              <SelectField label="Cliente" error={errors.customerId?.message} {...register("customerId")}>
                <option value="">Selecciona un cliente</option>
                {customers.map((customer) => (
                  <option key={customer.id} value={customer.id}>
                    {customer.name} {customer.surname} · {customer.documentTypeCode} {customer.documentNumber}
                  </option>
                ))}
              </SelectField>
              <SelectField label="Dispositivo" error={errors.customerDeviceId?.message} disabled={!selectedCustomerId} {...register("customerDeviceId")}>
                <option value="">{selectedCustomerId ? "Selecciona un dispositivo" : "Primero selecciona un cliente"}</option>
                {availableDevices.map((device) => (
                  <option key={device.id} value={device.id}>{device.brandName} {device.model} · {device.deviceTypeName}</option>
                ))}
              </SelectField>
            </div>
          </fieldset>

          <fieldset>
            <legend>Trabajo solicitado</legend>
            <SelectField label="Servicio" error={errors.businessServiceId?.message} disabled={!selectedDeviceId} {...register("businessServiceId")}>
              <option value="">{selectedDeviceId ? "Selecciona un servicio compatible" : "Primero selecciona un dispositivo"}</option>
              {compatibleServices.map((service) => (
                <option key={service.id} value={service.id}>{service.name} · {service.deviceTypeName}</option>
              ))}
            </SelectField>
            <div className="tm-field">
              <label htmlFor="repair-problem">Problema reportado</label>
              <textarea id="repair-problem" maxLength={2000} placeholder="Describe lo que informa el cliente y los síntomas visibles" aria-invalid={Boolean(errors.problemDescription)} {...register("problemDescription")} />
              {errors.problemDescription ? <p className="tm-field__error" role="alert">{errors.problemDescription.message}</p> : null}
            </div>
            <div className="tm-field">
              <label htmlFor="repair-diagnosis">Diagnóstico inicial <span>(opcional)</span></label>
              <textarea id="repair-diagnosis" maxLength={2000} placeholder="Registra una observación técnica solo si ya existe" aria-invalid={Boolean(errors.diagnosis)} {...register("diagnosis")} />
              {errors.diagnosis ? <p className="tm-field__error" role="alert">{errors.diagnosis.message}</p> : null}
            </div>
          </fieldset>

          <fieldset>
            <legend>Estimación</legend>
            <div className="tm-repair-form__grid">
              <TextField label="Valor estimado" type="number" min="0" step="0.01" placeholder="0" error={errors.estimatedPrice?.message} {...register("estimatedPrice")} />
              <TextField label="Entrega estimada" type="datetime-local" {...register("estimatedDeliveryAt")} />
            </div>
          </fieldset>

          <footer className="tm-repair-form__actions">
            <button className="tm-button tm-button--secondary" type="button" onClick={onClose} disabled={isSubmitting}>Cancelar</button>
            <button className="tm-button tm-button--primary" type="submit" disabled={isSubmitting || techniciansLoading || !technicians.length}>
              {isSubmitting ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Plus aria-hidden="true" />}
              {isSubmitting ? "Creando orden…" : "Crear orden"}
            </button>
          </footer>
        </form>
      </section>
    </div>
  );
}

function RepairOrderDetail({
  businessId,
  order,
  canUpdate,
  canClose,
  canViewOrderStatuses,
  canUploadEvidence,
  canViewQuotations,
  canCreateQuotations,
  canUpdateQuotations,
  canViewQuotationStatuses,
  onClose,
  onUpdated,
}: {
  businessId: number;
  order: RepairOrder;
  canUpdate: boolean;
  canClose: boolean;
  canViewOrderStatuses: boolean;
  canUploadEvidence: boolean;
  canViewQuotations: boolean;
  canCreateQuotations: boolean;
  canUpdateQuotations: boolean;
  canViewQuotationStatuses: boolean;
  onClose: () => void;
  onUpdated: (order: RepairOrder) => void;
}) {
  const router = useRouter();
  const panelRef = useRef<HTMLElement>(null);
  const closeRef = useRef<HTMLButtonElement>(null);
  const StatusIcon = repairStatusIcon(order.statusCode);
  const [history, setHistory] = useState<PageResponse<RepairOrderHistoryEntry> | null>(null);
  const [historyPage, setHistoryPage] = useState(0);
  const [historyReload, setHistoryReload] = useState(0);
  const [historyLoading, setHistoryLoading] = useState(true);
  const [historyError, setHistoryError] = useState<string | null>(null);
  const [orderStatuses, setOrderStatuses] = useState<BusinessCatalogItem[]>([]);
  const [orderStatusesLoading, setOrderStatusesLoading] = useState(false);
  const [orderStatusesError, setOrderStatusesError] = useState<string | null>(null);
  const [orderStatusesReload, setOrderStatusesReload] = useState(0);
  const [orderStatusEditorOpen, setOrderStatusEditorOpen] = useState(false);
  const [orderStatusId, setOrderStatusId] = useState("");
  const [orderStatusComment, setOrderStatusComment] = useState("");
  const [orderStatusVisibility, setOrderStatusVisibility] = useState<RepairOrderHistoryVisibility>("INTERNAL");
  const [orderStatusError, setOrderStatusError] = useState<string | null>(null);
  const [orderStatusSaving, setOrderStatusSaving] = useState(false);
  const [orderStatusNotice, setOrderStatusNotice] = useState<string | null>(null);
  const [quotations, setQuotations] = useState<PageResponse<Quotation> | null>(null);
  const [quotationsPage, setQuotationsPage] = useState(0);
  const [quotationsReload, setQuotationsReload] = useState(0);
  const [quotationsLoading, setQuotationsLoading] = useState(true);
  const [quotationsError, setQuotationsError] = useState<string | null>(null);
  const [quotationEditorOpen, setQuotationEditorOpen] = useState(false);
  const [quotationNotice, setQuotationNotice] = useState<string | null>(null);
  const [quotationStatuses, setQuotationStatuses] = useState<BusinessCatalogItem[]>([]);
  const [quotationStatusesLoading, setQuotationStatusesLoading] = useState(false);
  const [quotationStatusesError, setQuotationStatusesError] = useState<string | null>(null);
  const [quotationStatusesReload, setQuotationStatusesReload] = useState(0);
  const [quotationStatusEditingId, setQuotationStatusEditingId] = useState<number | null>(null);
  const [quotationStatusId, setQuotationStatusId] = useState("");
  const [quotationStatusNotes, setQuotationStatusNotes] = useState("");
  const [quotationStatusError, setQuotationStatusError] = useState<string | null>(null);
  const [quotationStatusSaving, setQuotationStatusSaving] = useState(false);
  const [diagnosis, setDiagnosis] = useState(order.diagnosis ?? "");
  const [diagnosisSaving, setDiagnosisSaving] = useState(false);
  const [diagnosisError, setDiagnosisError] = useState<string | null>(null);
  const [diagnosisSaved, setDiagnosisSaved] = useState(false);
  const [attachments, setAttachments] = useState<PageResponse<RepairOrderAttachment> | null>(null);
  const [attachmentsPage, setAttachmentsPage] = useState(0);
  const [attachmentsReload, setAttachmentsReload] = useState(0);
  const [attachmentsLoading, setAttachmentsLoading] = useState(true);
  const [attachmentsError, setAttachmentsError] = useState<string | null>(null);
  const [selectedEvidence, setSelectedEvidence] = useState<File | null>(null);
  const [evidenceDescription, setEvidenceDescription] = useState("");
  const [uploadingEvidence, setUploadingEvidence] = useState(false);
  const [evidenceFormError, setEvidenceFormError] = useState<string | null>(null);
  const [attachmentActionError, setAttachmentActionError] = useState<string | null>(null);
  const [confirmDeleteAttachmentId, setConfirmDeleteAttachmentId] = useState<number | null>(null);
  const [deletingAttachmentId, setDeletingAttachmentId] = useState<number | null>(null);
  const evidenceInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    let active = true;
    if (!canViewQuotations) {
      setQuotations(null);
      setQuotationsError(null);
      setQuotationsLoading(false);
      return () => { active = false; };
    }
    setQuotationsLoading(true);
    setQuotationsError(null);
    repairOrdersApi.getRepairOrderQuotations(businessId, order.id, quotationsPage, 20)
      .then((result) => { if (active) setQuotations(result); })
      .catch((error: unknown) => {
        if (!active) return;
        const normalized = normalizeApiError(error);
        if (normalized.status === 403) router.replace("/acceso-denegado");
        else setQuotationsError(normalized.message);
      })
      .finally(() => { if (active) setQuotationsLoading(false); });
    return () => { active = false; };
  }, [businessId, canViewQuotations, order.id, quotationsPage, quotationsReload, router]);

  useEffect(() => {
    let active = true;
    if (!canViewQuotations || !canUpdateQuotations || !canViewQuotationStatuses) {
      setQuotationStatuses([]);
      setQuotationStatusesLoading(false);
      setQuotationStatusesError(null);
      return () => { active = false; };
    }
    setQuotationStatusesLoading(true);
    setQuotationStatusesError(null);
    servicesApi.getBusinessCatalogItems(businessId, "QUOTATION_STATUS")
      .then((result) => { if (active) setQuotationStatuses(result); })
      .catch((error: unknown) => { if (active) setQuotationStatusesError(normalizeApiError(error).message); })
      .finally(() => { if (active) setQuotationStatusesLoading(false); });
    return () => { active = false; };
  }, [businessId, canViewQuotations, canUpdateQuotations, canViewQuotationStatuses, quotationStatusesReload]);

  useEffect(() => {
    let active = true;
    if ((!canUpdate && !canClose) || !canViewOrderStatuses) {
      setOrderStatuses([]);
      setOrderStatusesLoading(false);
      setOrderStatusesError(null);
      return () => { active = false; };
    }
    setOrderStatuses([]);
    setOrderStatusesLoading(true);
    setOrderStatusesError(null);
    servicesApi.getBusinessCatalogItems(businessId, "REPAIR_ORDER_STATUS")
      .then((result) => { if (active) setOrderStatuses(result); })
      .catch((error: unknown) => { if (active) setOrderStatusesError(normalizeApiError(error).message); })
      .finally(() => { if (active) setOrderStatusesLoading(false); });
    return () => { active = false; };
  }, [businessId, canClose, canUpdate, canViewOrderStatuses, orderStatusesReload]);

  useEffect(() => {
    let active = true;
    setHistoryLoading(true);
    setHistoryError(null);
    repairOrdersApi.getRepairOrderHistory(businessId, order.id, historyPage, 20)
      .then((result) => { if (active) setHistory(result); })
      .catch((error: unknown) => {
        if (!active) return;
        const normalized = normalizeApiError(error);
        if (normalized.status === 403) router.replace("/acceso-denegado");
        else setHistoryError(normalized.message);
      })
      .finally(() => { if (active) setHistoryLoading(false); });
    return () => { active = false; };
  }, [businessId, historyPage, historyReload, order.id, router]);

  useEffect(() => {
    let active = true;
    setAttachmentsLoading(true);
    setAttachmentsError(null);
    repairOrdersApi.getRepairOrderAttachments(businessId, order.id, attachmentsPage, 20)
      .then((result) => { if (active) setAttachments(result); })
      .catch((error: unknown) => {
        if (!active) return;
        const normalized = normalizeApiError(error);
        if (normalized.status === 403) router.replace("/acceso-denegado");
        else setAttachmentsError(normalized.message);
      })
      .finally(() => { if (active) setAttachmentsLoading(false); });
    return () => { active = false; };
  }, [attachmentsPage, attachmentsReload, businessId, order.id, router]);

  useEffect(() => {
    setDiagnosis(order.diagnosis ?? "");
    setDiagnosisError(null);
    setDiagnosisSaved(false);
  }, [order.diagnosis, order.id]);

  async function saveDiagnosis() {
    const trimmedDiagnosis = diagnosis.trim();
    if (!trimmedDiagnosis) {
      setDiagnosisError("Escribe el diagnóstico antes de guardarlo.");
      return;
    }
    if (trimmedDiagnosis.length > 2000) {
      setDiagnosisError("El diagnóstico no puede superar los 2000 caracteres.");
      return;
    }

    setDiagnosisSaving(true);
    setDiagnosisError(null);
    setDiagnosisSaved(false);
    try {
      const updatedOrder = await repairOrdersApi.updateRepairOrderDiagnosis(businessId, order.id, trimmedDiagnosis);
      setDiagnosis(updatedOrder.diagnosis ?? "");
      setDiagnosisSaved(true);
      onUpdated(updatedOrder);
      setHistoryPage(0);
      setHistoryReload((current) => current + 1);
    } catch (error) {
      const normalized = normalizeApiError(error);
      if (normalized.status === 403) router.replace("/acceso-denegado");
      else setDiagnosisError(normalized.message);
    } finally {
      setDiagnosisSaving(false);
    }
  }

  const statusTargets = getRepairOrderStatusTargets(order.statusCode, orderStatuses, {
    canUpdate,
    canClose,
    hasFinalPrice: order.finalPrice !== null,
  });

  function openOrderStatusEditor() {
    setOrderStatusId(statusTargets[0] ? String(statusTargets[0].id) : "");
    setOrderStatusComment("");
    setOrderStatusVisibility("INTERNAL");
    setOrderStatusError(null);
    setOrderStatusNotice(null);
    setOrderStatusEditorOpen(true);
  }

  async function saveOrderStatus(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setOrderStatusError(null);
    const selectedStatus = statusTargets.find((status) => String(status.id) === orderStatusId);
    if (!selectedStatus) {
      setOrderStatusError("Selecciona un estado permitido para esta orden.");
      return;
    }
    const comment = orderStatusComment.trim();
    if (comment.length > 2000) {
      setOrderStatusError("El comentario no puede superar los 2000 caracteres.");
      return;
    }
    if (orderStatusVisibility === "CUSTOMER" && !comment) {
      setOrderStatusError("Escribe el comentario que verá el cliente o cambia la visibilidad a interna.");
      return;
    }

    setOrderStatusSaving(true);
    try {
      const updated = await repairOrdersApi.updateRepairOrderStatus(businessId, order.id, {
        statusId: selectedStatus.id,
        ...(comment ? { comment, visibility: orderStatusVisibility } : {}),
      });
      onUpdated(updated);
      setOrderStatusEditorOpen(false);
      setOrderStatusNotice(`La orden cambió a ${updated.statusName}.`);
      setHistoryPage(0);
      setHistoryReload((current) => current + 1);
    } catch (error: unknown) {
      const normalized = normalizeApiError(error);
      if (normalized.status === 403) router.replace("/acceso-denegado");
      else setOrderStatusError(normalized.message);
    } finally {
      setOrderStatusSaving(false);
    }
  }

  function openQuotationStatusEditor(quotation: Quotation) {
    const targets = getQuotationStatusTargets(quotation.statusCode, quotationStatuses);
    setQuotationStatusEditingId(quotation.id);
    setQuotationStatusId(targets[0] ? String(targets[0].id) : "");
    setQuotationStatusNotes(quotation.notes ?? "");
    setQuotationStatusError(null);
  }

  async function saveQuotationStatus(event: FormEvent<HTMLFormElement>, quotationId: number) {
    event.preventDefault();
    setQuotationStatusError(null);
    const selectedStatus = quotationStatuses.find((status) => status.active && String(status.id) === quotationStatusId);
    if (!selectedStatus || quotationStatusId.length === 0) {
      setQuotationStatusError("Selecciona el nuevo estado de la cotización.");
      return;
    }
    if (quotationStatusNotes.length > 500) {
      setQuotationStatusError("Las notas no pueden superar los 500 caracteres.");
      return;
    }

    setQuotationStatusSaving(true);
    try {
      const updated = await repairOrdersApi.updateRepairOrderQuotationStatus(businessId, quotationId, {
        statusId: selectedStatus.id,
        ...(quotationStatusNotes.trim() ? { notes: quotationStatusNotes.trim() } : {}),
      });
      setQuotationStatusEditingId(null);
      setQuotationNotice(`La cotización ${updated.id} cambió a ${updated.statusName}.`);
      setQuotationsPage(0);
      setQuotationsReload((current) => current + 1);
    } catch (error: unknown) {
      const normalized = normalizeApiError(error);
      if (normalized.status === 403) router.replace("/acceso-denegado");
      else setQuotationStatusError(normalized.message);
    } finally {
      setQuotationStatusSaving(false);
    }
  }

  function chooseEvidence(file: File | undefined) {
    setEvidenceFormError(null);
    if (!file) {
      setSelectedEvidence(null);
      return;
    }
    const validationError = validateRepairOrderEvidence(file);
    if (validationError) {
      setSelectedEvidence(null);
      setEvidenceFormError(validationError);
      if (evidenceInputRef.current) evidenceInputRef.current.value = "";
      return;
    }
    setSelectedEvidence(file);
  }

  async function uploadEvidence(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setEvidenceFormError(null);
    setAttachmentActionError(null);
    if (!selectedEvidence) {
      setEvidenceFormError("Selecciona una imagen para cargar.");
      return;
    }
    const validationError = validateRepairOrderEvidence(selectedEvidence);
    if (validationError) {
      setEvidenceFormError(validationError);
      return;
    }
    if (evidenceDescription.trim().length > REPAIR_ORDER_EVIDENCE_DESCRIPTION_MAX_LENGTH) {
      setEvidenceFormError("La descripción no puede superar los 500 caracteres.");
      return;
    }

    setUploadingEvidence(true);
    try {
      await repairOrdersApi.uploadRepairOrderAttachment(
        businessId,
        order.id,
        selectedEvidence,
        evidenceDescription,
      );
      setSelectedEvidence(null);
      setEvidenceDescription("");
      if (evidenceInputRef.current) evidenceInputRef.current.value = "";
      setAttachmentsPage(0);
      setAttachmentsReload((current) => current + 1);
    } catch (error) {
      const normalized = normalizeApiError(error);
      if (normalized.status === 403) router.replace("/acceso-denegado");
      else setEvidenceFormError(normalized.message);
    } finally {
      setUploadingEvidence(false);
    }
  }

  async function deleteEvidence(attachmentId: number) {
    setDeletingAttachmentId(attachmentId);
    setAttachmentActionError(null);
    try {
      await repairOrdersApi.deleteRepairOrderAttachment(businessId, order.id, attachmentId);
      setConfirmDeleteAttachmentId(null);
      if (attachments?.items.length === 1 && attachments.page > 0) {
        setAttachmentsPage((current) => Math.max(0, current - 1));
      } else {
        setAttachmentsReload((current) => current + 1);
      }
    } catch (error) {
      const normalized = normalizeApiError(error);
      if (normalized.status === 403) router.replace("/acceso-denegado");
      else setAttachmentActionError(normalized.message);
    } finally {
      setDeletingAttachmentId(null);
    }
  }

  useEffect(() => {
    closeRef.current?.focus();
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape") onClose();
      if (event.key !== "Tab") return;
      const focusable = Array.from(panelRef.current?.querySelectorAll<HTMLElement>("button:not([disabled]), textarea:not([disabled]), input:not([disabled]), select:not([disabled]), [href]") ?? []);
      if (!focusable.length) return;
      if (event.shiftKey && document.activeElement === focusable[0]) {
        event.preventDefault();
        focusable[focusable.length - 1].focus();
      } else if (!event.shiftKey && document.activeElement === focusable[focusable.length - 1]) {
        event.preventDefault();
        focusable[0].focus();
      }
    };
    window.addEventListener("keydown", handleKeyDown);
    return () => window.removeEventListener("keydown", handleKeyDown);
  }, [onClose]);

  return (
    <div className="tm-review-backdrop">
      <section ref={panelRef} className="tm-review-panel tm-repair-detail" role="dialog" aria-modal="true" aria-labelledby="repair-detail-title">
        <header className="tm-review-panel__head">
          <div>
            <h2 id="repair-detail-title">Orden {order.orderNumber}</h2>
            <p>Información registrada al recibir el equipo.</p>
          </div>
          <button ref={closeRef} className="tm-icon-button" type="button" aria-label="Cerrar" onClick={onClose}><X aria-hidden="true" /></button>
        </header>
        <div className="tm-review-panel__body tm-repair-detail__body">
          <div className="tm-repair-status-row">
            <div className={`tm-repair-status ${statusClass(order.statusCode)}`}><StatusIcon aria-hidden="true" /> {order.statusName}</div>
            {statusTargets.length ? <button className="tm-button tm-button--secondary" type="button" onClick={openOrderStatusEditor}>{orderStatusEditorOpen ? "Cerrar cambio" : "Cambiar estado"}</button> : null}
          </div>
          {orderStatusNotice ? <FormAlert tone="success">{orderStatusNotice}</FormAlert> : null}
          {(canUpdate || canClose) && !canViewOrderStatuses ? <p className="tm-repair-detail__hint">Tu rol necesita permiso para consultar los catálogos del negocio y ofrecer cambios de estado.</p> : null}
          {(canUpdate || canClose) && canViewOrderStatuses && orderStatusesLoading ? <p className="tm-repair-history__state"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando estados permitidos…</p> : null}
          {(canUpdate || canClose) && canViewOrderStatuses && orderStatusesError ? <div className="tm-repair-history__error" role="alert"><p>{orderStatusesError}</p><button className="tm-button tm-button--secondary" type="button" onClick={() => setOrderStatusesReload((current) => current + 1)}>Reintentar estados</button></div> : null}
          {order.statusCode.toUpperCase() === "READY_FOR_PICKUP" && order.finalPrice === null ? <p className="tm-repair-detail__hint">Para marcar esta orden como entregada, primero debe registrarse el valor final.</p> : null}
          {orderStatusEditorOpen ? (
            <form className="tm-repair-status-form" onSubmit={(event) => void saveOrderStatus(event)}>
              <label className="tm-field" htmlFor="repair-order-next-status">
                <span>Siguiente estado</span>
                <select id="repair-order-next-status" value={orderStatusId} required disabled={orderStatusSaving} onChange={(event) => setOrderStatusId(event.target.value)}>
                  {statusTargets.map((status) => <option key={status.id} value={status.id}>{status.name}</option>)}
                </select>
              </label>
              <label className="tm-field tm-repair-status-form__comment" htmlFor="repair-order-status-comment">
                <span>Comentario <small>(opcional; hasta 2000 caracteres)</small></span>
                <textarea id="repair-order-status-comment" value={orderStatusComment} maxLength={2000} rows={3} disabled={orderStatusSaving} onChange={(event) => setOrderStatusComment(event.target.value)} />
                <small>{orderStatusComment.length}/2000</small>
              </label>
              <label className="tm-field" htmlFor="repair-order-status-visibility">
                <span>Visibilidad del comentario</span>
                <select id="repair-order-status-visibility" value={orderStatusVisibility} disabled={orderStatusSaving} onChange={(event) => setOrderStatusVisibility(event.target.value as RepairOrderHistoryVisibility)}>
                  <option value="INTERNAL">Solo equipo del negocio</option>
                  <option value="CUSTOMER">Visible para el cliente</option>
                </select>
              </label>
              <p className="tm-repair-detail__hint">Los cambios de estado se registran en el historial. Si eliges visibilidad para el cliente, el comentario es obligatorio.</p>
              {orderStatusError ? <FormAlert>{orderStatusError}</FormAlert> : null}
              <div className="tm-repair-status-form__actions">
                <button className="tm-button tm-button--secondary" type="button" disabled={orderStatusSaving} onClick={() => { setOrderStatusEditorOpen(false); setOrderStatusError(null); }}>Cancelar</button>
                <button className="tm-button tm-button--primary" type="submit" disabled={orderStatusSaving || !orderStatusId}>
                  {orderStatusSaving ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Check aria-hidden="true" />}
                  {orderStatusSaving ? "Guardando…" : "Guardar estado"}
                </button>
              </div>
            </form>
          ) : null}
          <section>
            <h3>Atención</h3>
            <dl className="tm-repair-facts">
              <div><dt>Sede</dt><dd>{order.branchName}</dd></div>
              <div><dt>Servicio</dt><dd>{order.businessServiceName}</dd></div>
              <div><dt>Recibida</dt><dd>{formatDateTime(order.receivedAt)}</dd></div>
              <div><dt>Entrega estimada</dt><dd>{formatDateTime(order.estimatedDeliveryAt)}</dd></div>
              <div><dt>Recibió</dt><dd>{order.receivedByBusinessUserName}</dd></div>
              <div><dt>Técnico</dt><dd>{order.assignedBusinessUserName}</dd></div>
            </dl>
          </section>
          <section>
            <h3>Cliente y equipo</h3>
            <dl className="tm-repair-facts">
              <div><dt>Cliente</dt><dd>{order.customerName} {order.customerSurname}</dd></div>
              <div><dt>Correo</dt><dd>{order.customerEmail}</dd></div>
              <div className="is-wide"><dt>Dispositivo</dt><dd>{order.customerDeviceLabel}</dd></div>
            </dl>
          </section>
          <section>
            <h3>Problema reportado</h3>
            <p>{order.problemDescription}</p>
          </section>
          {canViewQuotations || canCreateQuotations ? <section className="tm-repair-quotations" aria-labelledby="repair-order-quotations-title">
            <div className="tm-repair-detail__section-head">
              <h3 id="repair-order-quotations-title"><ReceiptText aria-hidden="true" /> Cotizaciones</h3>
              <div className="tm-repair-quotations__head-actions">
                {canViewQuotations && quotations ? <span>{quotations.totalElements} {quotations.totalElements === 1 ? "cotización" : "cotizaciones"}</span> : null}
                {canCreateQuotations ? <button className="tm-button tm-button--secondary" type="button" onClick={() => { setQuotationNotice(null); setQuotationEditorOpen((current) => !current); }}>{quotationEditorOpen ? "Cerrar" : "Nueva cotización"}</button> : null}
                {canViewQuotations ? <button className="tm-icon-button" type="button" aria-label="Actualizar cotizaciones" disabled={quotationsLoading} onClick={() => setQuotationsReload((current) => current + 1)}><RefreshCw aria-hidden="true" /></button> : null}
              </div>
            </div>
            {quotationNotice ? <FormAlert tone="success">{quotationNotice}</FormAlert> : null}
            {quotationEditorOpen ? <QuotationEditor businessId={businessId} repairOrderId={order.id} onCancel={() => setQuotationEditorOpen(false)} onCreated={(quotation) => {
              setQuotationNotice(`La cotización ${quotation.id} quedó creada como ${quotation.statusName.toLocaleLowerCase("es-CO")}.`);
              setQuotationEditorOpen(false);
              setQuotationsPage(0);
              if (canViewQuotations) setQuotationsReload((current) => current + 1);
            }} /> : null}
            {canViewQuotations && quotationsLoading ? <p className="tm-repair-history__state"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando cotizaciones…</p> : null}
            {canViewQuotations && quotationsError ? <div className="tm-repair-history__error" role="alert"><p>{quotationsError}</p><button className="tm-button tm-button--secondary" type="button" onClick={() => { setQuotationsError(null); setQuotationsReload((current) => current + 1); }}>Reintentar</button></div> : null}
            {canUpdateQuotations && !canViewQuotationStatuses ? <p className="tm-repair-history__state">Tu rol necesita permiso para consultar catálogos del negocio para ofrecer cambios de estado.</p> : null}
            {canUpdateQuotations && canViewQuotationStatuses && quotationStatusesLoading ? <p className="tm-repair-history__state"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando estados…</p> : null}
            {canUpdateQuotations && canViewQuotationStatuses && quotationStatusesError ? <div className="tm-repair-history__error" role="alert"><p>{quotationStatusesError}</p><button className="tm-button tm-button--secondary" type="button" onClick={() => setQuotationStatusesReload((current) => current + 1)}>Reintentar estados</button></div> : null}
            {canViewQuotations && !quotationsLoading && !quotationsError && quotations?.items.length === 0 ? <p className="tm-repair-history__state">Esta orden todavía no tiene cotizaciones.</p> : null}
            {canViewQuotations && !quotationsLoading && quotations?.items.length ? (
              <div className="tm-repair-quotations__list">
                {quotations.items.map((quotation) => {
                  const targetStatuses = getQuotationStatusTargets(quotation.statusCode, quotationStatuses);
                  return <article className="tm-repair-quotations__item" key={quotation.id}>
                    <header>
                      <div><strong>{formatMoney(quotation.totalAmount)}</strong><span className={`tm-repair-quotations__status ${statusClass(quotation.statusCode)}`}>{quotation.statusName}</span></div>
                      <small>Creada {formatDateTime(quotation.createdAt)}</small>
                    </header>
                    {quotation.items.length ? (
                      <ul>
                        {quotation.items.map((item) => (
                          <li key={item.id}><span>{item.description} <small>× {item.quantity}</small></span><strong>{formatMoney(item.totalPrice)}</strong></li>
                        ))}
                      </ul>
                    ) : null}
                    {quotation.notes ? <p>{quotation.notes}</p> : null}
                    {quotation.expiresAt ? <small className="tm-repair-quotations__expires">Vence {formatDateTime(quotation.expiresAt)}</small> : null}
                    {canUpdateQuotations && canViewQuotationStatuses && targetStatuses.length ? <div className="tm-quotation-status__actions"><button className="tm-button tm-button--secondary" type="button" onClick={() => openQuotationStatusEditor(quotation)}>{quotationStatusEditingId === quotation.id ? "Cerrar cambios" : "Cambiar estado"}</button></div> : null}
                    {quotationStatusEditingId === quotation.id ? (
                      <form className="tm-quotation-status-form" onSubmit={(event) => void saveQuotationStatus(event, quotation.id)}>
                        <label className="tm-field" htmlFor={`quotation-status-${quotation.id}`}>
                          <span>Nuevo estado</span>
                          <select id={`quotation-status-${quotation.id}`} value={quotationStatusId} required disabled={quotationStatusSaving} onChange={(event) => setQuotationStatusId(event.target.value)}>
                            {targetStatuses.map((status) => <option key={status.id} value={status.id}>{status.name}</option>)}
                          </select>
                        </label>
                        <label className="tm-field tm-quotation-status-form__notes" htmlFor={`quotation-status-notes-${quotation.id}`}>
                          <span>Notas de la cotización <small>(opcional; vacío conserva las actuales)</small></span>
                          <textarea id={`quotation-status-notes-${quotation.id}`} value={quotationStatusNotes} maxLength={500} rows={2} disabled={quotationStatusSaving} onChange={(event) => setQuotationStatusNotes(event.target.value)} />
                          <small>{quotationStatusNotes.length}/500</small>
                        </label>
                        {quotationStatusError ? <FormAlert>{quotationStatusError}</FormAlert> : null}
                        <div className="tm-quotation-status-form__actions">
                          <button className="tm-button tm-button--secondary" type="button" disabled={quotationStatusSaving} onClick={() => { setQuotationStatusEditingId(null); setQuotationStatusError(null); }}>Cancelar</button>
                          <button className="tm-button tm-button--primary" type="submit" disabled={quotationStatusSaving || !quotationStatusId}>
                            {quotationStatusSaving ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Check aria-hidden="true" />}
                            {quotationStatusSaving ? "Guardando…" : "Guardar estado"}
                          </button>
                        </div>
                      </form>
                    ) : null}
                  </article>
                })}
              </div>
            ) : null}
            {canViewQuotations && quotations && quotations.totalPages > 1 ? (
              <div className="tm-repair-history__pagination">
                <button className="tm-icon-button" type="button" aria-label="Cotizaciones más recientes" disabled={!quotations.hasPrevious || quotationsLoading} onClick={() => setQuotationsPage((current) => Math.max(0, current - 1))}><ChevronLeft aria-hidden="true" /></button>
                <span>Página {quotations.page + 1} de {quotations.totalPages}</span>
                <button className="tm-icon-button" type="button" aria-label="Cotizaciones anteriores" disabled={!quotations.hasNext || quotationsLoading} onClick={() => setQuotationsPage((current) => current + 1)}><ChevronRight aria-hidden="true" /></button>
              </div>
            ) : null}
          </section> : null}
          <section>
            <div className="tm-repair-detail__section-head">
              <h3>Diagnóstico técnico</h3>
              <span>{diagnosis.length}/2000</span>
            </div>
            {canUpdate ? (
              <>
                <label className="sr-only" htmlFor="repair-order-diagnosis">Diagnóstico técnico</label>
                <textarea
                  id="repair-order-diagnosis"
                  className="tm-repair-detail__diagnosis"
                  value={diagnosis}
                  maxLength={2000}
                  onChange={(event) => { setDiagnosis(event.target.value); setDiagnosisSaved(false); }}
                  placeholder="Describe la causa identificada y los hallazgos técnicos."
                  aria-invalid={Boolean(diagnosisError)}
                  aria-describedby={diagnosisError ? "repair-order-diagnosis-error" : "repair-order-diagnosis-help"}
                  disabled={diagnosisSaving}
                />
                <p id="repair-order-diagnosis-help" className="tm-repair-detail__hint">Este cambio quedará registrado en el historial interno de la orden.</p>
                {diagnosisError ? <p id="repair-order-diagnosis-error" className="tm-field__error" role="alert">{diagnosisError}</p> : null}
                {diagnosisSaved ? <p className="tm-repair-detail__saved" role="status">Diagnóstico guardado.</p> : null}
                <button className="tm-button tm-button--primary tm-repair-detail__save" type="button" onClick={() => void saveDiagnosis()} disabled={diagnosisSaving || !diagnosis.trim()}>
                  {diagnosisSaving ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Save aria-hidden="true" />}
                  {diagnosisSaving ? "Guardando…" : "Guardar diagnóstico"}
                </button>
              </>
            ) : <p>{order.diagnosis ?? "Aún no se ha registrado un diagnóstico."}</p>}
          </section>
          <section className="tm-repair-evidence" aria-labelledby="repair-order-evidence-title">
            <div className="tm-repair-detail__section-head">
              <h3 id="repair-order-evidence-title"><Camera aria-hidden="true" /> Evidencias fotográficas</h3>
              <div className="tm-repair-evidence__head-actions">
                {attachments ? <span>{attachments.totalElements} {attachments.totalElements === 1 ? "imagen" : "imágenes"}</span> : null}
                <button className="tm-icon-button" type="button" aria-label="Actualizar evidencias" disabled={attachmentsLoading} onClick={() => setAttachmentsReload((current) => current + 1)}><RefreshCw aria-hidden="true" /></button>
              </div>
            </div>

            {canUploadEvidence ? (
              <form className="tm-repair-evidence__form" onSubmit={(event) => void uploadEvidence(event)}>
                <div className="tm-repair-evidence__upload-row">
                  <label className="tm-button tm-button--secondary tm-repair-evidence__select" htmlFor="repair-evidence-file">
                    <ImagePlus aria-hidden="true" />
                    {selectedEvidence ? "Cambiar imagen" : "Seleccionar imagen"}
                    <input
                      ref={evidenceInputRef}
                      className="sr-only"
                      id="repair-evidence-file"
                      type="file"
                      accept="image/jpeg,image/png,image/webp"
                      aria-describedby="repair-evidence-file-help"
                      disabled={uploadingEvidence}
                      onChange={(event) => chooseEvidence(event.target.files?.[0])}
                    />
                  </label>
                  <p id="repair-evidence-file-help">JPG, PNG o WebP · máximo 10 MB</p>
                </div>
                {selectedEvidence ? (
                  <p className="tm-repair-evidence__selected"><strong>{selectedEvidence.name}</strong><span>{formatFileSize(selectedEvidence.size)}</span></p>
                ) : null}
                <div className="tm-field">
                  <label htmlFor="repair-evidence-description">Descripción <span>(opcional)</span></label>
                  <textarea
                    id="repair-evidence-description"
                    maxLength={REPAIR_ORDER_EVIDENCE_DESCRIPTION_MAX_LENGTH}
                    value={evidenceDescription}
                    onChange={(event) => setEvidenceDescription(event.target.value)}
                    placeholder="Ej.: rayón visible en el marco antes de la reparación"
                    disabled={uploadingEvidence}
                  />
                  <p className="tm-repair-evidence__counter">{evidenceDescription.length}/{REPAIR_ORDER_EVIDENCE_DESCRIPTION_MAX_LENGTH}</p>
                </div>
                {evidenceFormError ? <p className="tm-field__error" role="alert">{evidenceFormError}</p> : null}
                <button className="tm-button tm-button--primary" type="submit" disabled={uploadingEvidence || !selectedEvidence}>
                  {uploadingEvidence ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Upload aria-hidden="true" />}
                  {uploadingEvidence ? "Cargando evidencia…" : "Cargar evidencia"}
                </button>
              </form>
            ) : null}

            {attachmentActionError ? <p className="tm-field__error" role="alert">{attachmentActionError}</p> : null}
            {attachmentsLoading ? <p className="tm-repair-history__state"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando evidencias…</p> : null}
            {attachmentsError ? <div className="tm-repair-history__error" role="alert"><p>{attachmentsError}</p><button className="tm-button tm-button--secondary" type="button" onClick={() => { setAttachmentsError(null); setAttachmentsReload((current) => current + 1); }}>Reintentar</button></div> : null}
            {!attachmentsLoading && !attachmentsError && attachments?.items.length === 0 ? <p className="tm-repair-history__state">Aún no hay evidencias asociadas a esta orden.</p> : null}
            {!attachmentsLoading && attachments?.items.length ? (
              <div className="tm-repair-evidence__grid">
                {attachments.items.map((attachment) => (
                  <article className="tm-repair-evidence__item" key={attachment.id}>
                    <a className="tm-repair-evidence__preview" href={attachment.fileUrl} target="_blank" rel="noreferrer" aria-label={`Abrir imagen ${attachment.originalFileName}`}>
                      {/* eslint-disable-next-line @next/next/no-img-element */}
                      <img src={attachment.fileUrl} alt={attachment.description || attachment.originalFileName} loading="lazy" referrerPolicy="no-referrer" />
                      <span><ExternalLink aria-hidden="true" /> Abrir imagen</span>
                    </a>
                    <div className="tm-repair-evidence__meta">
                      <strong title={attachment.originalFileName}>{attachment.originalFileName}</strong>
                      <p>{attachment.description || "Sin descripción"}</p>
                      <small>{attachment.uploadedByName} · {formatDateTime(attachment.createdAt)}</small>
                      <small>{formatFileSize(attachment.fileSize)}</small>
                      {canUploadEvidence ? (
                        <div className="tm-repair-evidence__actions">
                          {confirmDeleteAttachmentId === attachment.id ? (
                            <>
                              <span>¿Eliminar esta evidencia?</span>
                              <button className="tm-button tm-button--danger" type="button" disabled={deletingAttachmentId !== null} onClick={() => void deleteEvidence(attachment.id)}>
                                {deletingAttachmentId === attachment.id ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Trash2 aria-hidden="true" />}
                                Confirmar
                              </button>
                              <button className="tm-button tm-button--secondary" type="button" disabled={deletingAttachmentId !== null} onClick={() => setConfirmDeleteAttachmentId(null)}>Conservar</button>
                            </>
                          ) : (
                            <button className="tm-button tm-button--secondary tm-repair-evidence__delete" type="button" disabled={deletingAttachmentId !== null} onClick={() => { setAttachmentActionError(null); setConfirmDeleteAttachmentId(attachment.id); }}><Trash2 aria-hidden="true" /> Eliminar</button>
                          )}
                        </div>
                      ) : null}
                    </div>
                  </article>
                ))}
              </div>
            ) : null}
            {attachments && attachments.totalPages > 1 ? (
              <div className="tm-repair-history__pagination">
                <button className="tm-icon-button" type="button" aria-label="Evidencias más recientes" disabled={!attachments.hasPrevious || attachmentsLoading} onClick={() => setAttachmentsPage((current) => Math.max(0, current - 1))}><ChevronLeft aria-hidden="true" /></button>
                <span>Página {attachments.page + 1} de {attachments.totalPages}</span>
                <button className="tm-icon-button" type="button" aria-label="Evidencias anteriores" disabled={!attachments.hasNext || attachmentsLoading} onClick={() => setAttachmentsPage((current) => current + 1)}><ChevronRight aria-hidden="true" /></button>
              </div>
            ) : null}
          </section>
          <section className="tm-repair-history" aria-labelledby="repair-order-history-title">
            <div className="tm-repair-detail__section-head">
              <h3 id="repair-order-history-title"><History aria-hidden="true" /> Historial de la orden</h3>
              {history ? <span>{history.totalElements} {history.totalElements === 1 ? "registro" : "registros"}</span> : null}
            </div>
            {historyLoading ? <p className="tm-repair-history__state"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando historial…</p> : null}
            {historyError ? <div className="tm-repair-history__error" role="alert"><p>{historyError}</p><button className="tm-button tm-button--secondary" type="button" onClick={() => { setHistoryError(null); setHistoryReload((current) => current + 1); }}>Reintentar</button></div> : null}
            {!historyLoading && !historyError && history?.items.length === 0 ? <p className="tm-repair-history__state">Todavía no hay movimientos registrados.</p> : null}
            {!historyLoading && history?.items.length ? (
              <ol className="tm-repair-history__list">
                {history.items.map((entry) => (
                  <li key={entry.id}>
                    <span className="tm-repair-history__marker" aria-hidden="true" />
                    <div>
                      <p>{entry.comment}</p>
                      <small>{entry.createdByUserName} · {formatDateTime(entry.createdAt)}</small>
                      <span className={`tm-repair-history__visibility is-${entry.visibility.toLowerCase()}`}>
                        {entry.visibility === "CUSTOMER" ? "Visible para el cliente" : "Registro interno"}
                      </span>
                    </div>
                  </li>
                ))}
              </ol>
            ) : null}
            {history && history.totalPages > 1 ? (
              <div className="tm-repair-history__pagination">
                <button className="tm-icon-button" type="button" aria-label="Historial más reciente" disabled={!history.hasPrevious || historyLoading} onClick={() => setHistoryPage((current) => Math.max(0, current - 1))}><ChevronLeft aria-hidden="true" /></button>
                <span>Página {history.page + 1} de {history.totalPages}</span>
                <button className="tm-icon-button" type="button" aria-label="Historial anterior" disabled={!history.hasNext || historyLoading} onClick={() => setHistoryPage((current) => current + 1)}><ChevronRight aria-hidden="true" /></button>
              </div>
            ) : null}
          </section>
          <section>
            <h3>Valores</h3>
            <dl className="tm-repair-facts">
              <div><dt>Estimado</dt><dd>{formatMoney(order.estimatedPrice)}</dd></div>
              <div><dt>Valor final</dt><dd>{order.finalPrice === null ? "Pendiente" : formatMoney(order.finalPrice)}</dd></div>
            </dl>
          </section>
        </div>
        <footer className="tm-review-panel__foot">
          <button className="tm-button tm-button--secondary" type="button" onClick={onClose}>Cerrar detalle</button>
        </footer>
      </section>
    </div>
  );
}

export function BusinessRepairOrdersWorkspace({ businessId }: { businessId: number }) {
  const { businesses, businessContext, activeBranchId, selectBusiness } = useAuth();
  const router = useRouter();
  const [orders, setOrders] = useState<PageResponse<RepairOrder> | null>(null);
  const [devices, setDevices] = useState<CustomerDevice[]>([]);
  const [services, setServices] = useState<BusinessService[]>([]);
  const [statuses, setStatuses] = useState<BusinessCatalogItem[]>([]);
  const [page, setPage] = useState(0);
  const [filters, setFilters] = useState<RepairOrderFilters>({});
  const [draftFilters, setDraftFilters] = useState({ orderNumber: "", customer: "", branchId: "", statusId: "", receivedFrom: "", receivedTo: "" });
  const [loading, setLoading] = useState(true);
  const [editorDataLoading, setEditorDataLoading] = useState(false);
  const [error, setError] = useState<ApiClientError | null>(null);
  const [editorDataError, setEditorDataError] = useState<string | null>(null);
  const [notice, setNotice] = useState<Notice | null>(null);
  const [editorOpen, setEditorOpen] = useState(false);
  const [quickEditorOpen, setQuickEditorOpen] = useState(false);
  const [detail, setDetail] = useState<RepairOrder | null>(null);
  const overlayTriggerRef = useRef<HTMLButtonElement | null>(null);
  const exists = businesses.some((business) => business.id === businessId);
  const contextReady = businessContext?.businessId === businessId;
  const permissions = contextReady ? businessContext.permissions : [];
  const canView = hasPermission(permissions, "REPAIR_ORDER_VIEW");
  const canCreate = hasPermission(permissions, "REPAIR_ORDER_CREATE");
  const canCreateCustomerDevice = hasPermission(permissions, "CUSTOMER_DEVICE_CREATE");
  const canViewDevices = hasPermission(permissions, "CUSTOMER_DEVICE_VIEW");
  const canViewServices = hasPermission(permissions, "BUSINESS_SERVICE_VIEW");
  const canViewCatalogs = hasPermission(permissions, "BUSINESS_CATALOG_VIEW");
  const customers = useMemo(() => buildCustomerDirectory(devices), [devices]);
  const activeServices = useMemo(() => services.filter((service) => service.active), [services]);
  const activeStatuses = useMemo(
    () => statuses.filter((status) => status.active).sort((a, b) => (a.sortOrder ?? 0) - (b.sortOrder ?? 0)),
    [statuses],
  );
  const editorReady = Boolean(businessContext?.branches.length && customers.length && activeServices.length);
  const canQuickCreate = canCreate && canCreateCustomerDevice;
  const canQuickReadOptions = canViewCatalogs && canViewServices;

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

  const loadOrders = useCallback(async (targetPage = page, activeFilters = filters) => {
    setLoading(true);
    setError(null);
    try {
      setOrders(await repairOrdersApi.getRepairOrders(businessId, targetPage, PAGE_SIZE, activeFilters));
    } catch (requestError) {
      const normalized = normalizeApiError(requestError);
      if (normalized.status === 403) router.replace("/acceso-denegado");
      else setError(normalized);
    } finally {
      setLoading(false);
    }
  }, [businessId, filters, page, router]);

  const loadSupportingData = useCallback(async () => {
    setEditorDataLoading(true);
    setEditorDataError(null);
    try {
      const [deviceResult, serviceResult, statusResult] = await Promise.allSettled([
        canCreate && canViewDevices ? customerDevicesApi.getAllCustomerDevices(businessId) : Promise.resolve([]),
        canCreate && canViewServices ? servicesApi.getAllBusinessServices(businessId) : Promise.resolve([]),
        canViewCatalogs ? servicesApi.getBusinessCatalogItems(businessId, "REPAIR_ORDER_STATUS") : Promise.resolve([]),
      ]);
      const availableDevices = deviceResult.status === "fulfilled" ? deviceResult.value : [];
      const availableServices = serviceResult.status === "fulfilled" ? serviceResult.value : [];
      setDevices(availableDevices);
      setServices(availableServices);
      if (statusResult.status === "fulfilled") setStatuses(statusResult.value);

      const requestErrors = [deviceResult, serviceResult, statusResult]
        .filter((result): result is PromiseRejectedResult => result.status === "rejected")
        .map((result) => normalizeApiError(result.reason).message);
      if (canCreate && (!canViewDevices || !canViewServices)) {
        setEditorDataError("Tu rol necesita consultar clientes y servicios para crear una orden.");
      } else if (requestErrors.length) {
        setEditorDataError(requestErrors.join(" "));
      } else if (canCreate && (!availableDevices.length || !availableServices.some((service) => service.active))) {
        setEditorDataError("Necesitas al menos un cliente con dispositivo y un servicio activo antes de recibir un equipo.");
      }
    } finally {
      setEditorDataLoading(false);
    }
  }, [businessId, canCreate, canViewCatalogs, canViewDevices, canViewServices]);

  useEffect(() => {
    if (!contextReady) return;
    if (!canView) {
      router.replace("/acceso-denegado");
      return;
    }
    void loadOrders(page, filters);
    void loadSupportingData();
  }, [canView, contextReady, filters, loadOrders, loadSupportingData, page, router]);

  const closeOverlay = useCallback(() => {
    setEditorOpen(false);
    setQuickEditorOpen(false);
    setDetail(null);
    requestAnimationFrame(() => overlayTriggerRef.current?.focus());
  }, []);

  function openEditor(trigger: HTMLButtonElement) {
    overlayTriggerRef.current = trigger;
    setNotice(null);
    setEditorOpen(true);
  }

  function openQuickEditor(trigger: HTMLButtonElement) {
    overlayTriggerRef.current = trigger;
    setNotice(null);
    setQuickEditorOpen(true);
  }

  function openDetail(trigger: HTMLButtonElement, order: RepairOrder) {
    overlayTriggerRef.current = trigger;
    setDetail(order);
  }

  function applyFilters(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const nextFilters: RepairOrderFilters = {
      orderNumber: draftFilters.orderNumber.trim() || undefined,
      customer: draftFilters.customer.trim() || undefined,
      branchId: draftFilters.branchId ? Number(draftFilters.branchId) : undefined,
      statusId: draftFilters.statusId ? Number(draftFilters.statusId) : undefined,
      receivedFrom: draftFilters.receivedFrom ? `${draftFilters.receivedFrom}T00:00:00` : undefined,
      receivedTo: draftFilters.receivedTo ? `${draftFilters.receivedTo}T23:59:59` : undefined,
    };
    setPage(0);
    setFilters(nextFilters);
  }

  function clearFilters() {
    setDraftFilters({ orderNumber: "", customer: "", branchId: "", statusId: "", receivedFrom: "", receivedTo: "" });
    setPage(0);
    setFilters({});
  }

  async function handleCreated(order: RepairOrder) {
    setNotice({ tone: "success", message: `La orden ${order.orderNumber} quedó creada en estado ${order.statusName}.` });
    setPage(0);
    await loadOrders(0, filters);
  }

  if (!exists) return <div className="tm-page-stack"><ErrorState title="Negocio no disponible" message="Este negocio no está asociado a tu cuenta o ya no está disponible." /></div>;
  if (error && !contextReady) return <div className="tm-page-stack"><ErrorState message={error.message} onRetry={() => void selectBusiness(businessId).catch(handleContextError)} /></div>;
  if (!contextReady || !canView) return <FullPageLoader label="Preparando órdenes de reparación" />;

  return (
    <div className="tm-page-stack tm-repairs-page">
      <header className="tm-workspace-heading tm-repairs-heading">
        <div>
          <h1>Órdenes de reparación</h1>
          <p>Recibe equipos, localiza cada servicio y consulta la información inicial registrada en {businessContext.businessName}.</p>
        </div>
        {canCreate ? (
          <div className="tm-repair-heading__actions">
            <button className="tm-button tm-button--secondary" type="button" disabled={!editorReady || editorDataLoading} title={!editorReady ? "Completa los datos necesarios para crear una orden" : undefined} onClick={(event) => openEditor(event.currentTarget)}>
              {editorDataLoading ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Plus aria-hidden="true" />}
              Recibir equipo
            </button>
            {canQuickCreate ? (
              <button
                className="tm-button tm-button--primary"
                type="button"
                disabled={!canQuickReadOptions || editorDataLoading || !businessContext.branches.length || !activeServices.length}
                title={!canViewCatalogs ? "Tu rol necesita consultar los catálogos del negocio para registrar el equipo." : !canViewServices ? "Tu rol necesita consultar los servicios del negocio." : undefined}
                onClick={(event) => openQuickEditor(event.currentTarget)}
              >
                <Zap aria-hidden="true" /> Orden rápida
              </button>
            ) : null}
          </div>
        ) : null}
      </header>

      {notice ? <FormAlert tone={notice.tone}>{notice.message}</FormAlert> : null}
      {editorDataError && canCreate ? <FormAlert tone="warning">{editorDataError}</FormAlert> : null}

      <form className="tm-repair-filters" onSubmit={applyFilters} aria-label="Filtros de órdenes">
        <div className="tm-repair-filters__search">
          <Search aria-hidden="true" />
          <label htmlFor="repair-customer-search">Cliente</label>
          <input id="repair-customer-search" placeholder="Nombre, documento o correo" value={draftFilters.customer} onChange={(event) => setDraftFilters((current) => ({ ...current, customer: event.target.value }))} />
        </div>
        <div className="tm-repair-filters__search">
          <ClipboardList aria-hidden="true" />
          <label htmlFor="repair-number-search">Número de orden</label>
          <input id="repair-number-search" placeholder="OS-001-01-000001" value={draftFilters.orderNumber} onChange={(event) => setDraftFilters((current) => ({ ...current, orderNumber: event.target.value }))} />
        </div>
        <label><span>Sede</span><select value={draftFilters.branchId} onChange={(event) => setDraftFilters((current) => ({ ...current, branchId: event.target.value }))}><option value="">Todas</option>{businessContext.branches.map((branch) => <option key={branch.id} value={branch.id}>{branch.name}</option>)}</select></label>
        {activeStatuses.length ? <label><span>Estado</span><select value={draftFilters.statusId} onChange={(event) => setDraftFilters((current) => ({ ...current, statusId: event.target.value }))}><option value="">Todos</option>{activeStatuses.map((status) => <option key={status.id} value={status.id}>{status.name}</option>)}</select></label> : null}
        <label><span>Desde</span><input type="date" value={draftFilters.receivedFrom} onChange={(event) => setDraftFilters((current) => ({ ...current, receivedFrom: event.target.value }))} /></label>
        <label><span>Hasta</span><input type="date" value={draftFilters.receivedTo} onChange={(event) => setDraftFilters((current) => ({ ...current, receivedTo: event.target.value }))} /></label>
        <div className="tm-repair-filters__actions">
          <button className="tm-button tm-button--secondary" type="button" onClick={clearFilters}>Limpiar</button>
          <button className="tm-button tm-button--primary" type="submit"><Filter aria-hidden="true" /> Aplicar</button>
        </div>
      </form>

      {error ? <ErrorState message={error.message} onRetry={() => void loadOrders(page, filters)} /> : null}
      {!error ? (
        <section className="tm-repair-ledger">
          <header className="tm-repair-ledger__head">
            <div><span><ClipboardList aria-hidden="true" /></span><div><h2>Registro operativo</h2><p>Órdenes visibles en las sedes permitidas para tu rol.</p></div></div>
            {!loading && orders ? <p><small>Total encontrado</small><strong>{orders.totalElements} {orders.totalElements === 1 ? "orden" : "órdenes"}</strong></p> : null}
          </header>
          {loading ? <div className="tm-repair-ledger__loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando órdenes</div> : orders?.items.length ? (
            <div className="tm-repair-list">
              {orders.items.map((order) => (
                <RepairOrderRow key={order.id} order={order} onOpenDetail={openDetail} />
              ))}
            </div>
          ) : <EmptyState title="No hay órdenes con estos criterios" message="Ajusta los filtros o recibe el primer equipo para comenzar el registro operativo." action={canCreate && editorReady ? <button className="tm-button tm-button--primary" type="button" onClick={(event) => openEditor(event.currentTarget)}><Plus aria-hidden="true" /> Recibir primer equipo</button> : undefined} />}
          {!loading && orders?.totalElements ? <footer className="tm-repair-ledger__footer"><button className="tm-icon-button" type="button" aria-label="Actualizar órdenes" onClick={() => void loadOrders(page, filters)}><RefreshCw aria-hidden="true" /></button><p><strong>Página {orders.page + 1} de {Math.max(orders.totalPages, 1)}</strong><span>Mostrando {orders.items.length} de {orders.totalElements} órdenes.</span></p><div aria-label="Paginación de órdenes"><button className="tm-icon-button" type="button" aria-label="Página anterior" disabled={!orders.hasPrevious} onClick={() => setPage((current) => Math.max(0, current - 1))}><ChevronLeft aria-hidden="true" /></button><button className="tm-icon-button" type="button" aria-label="Página siguiente" disabled={!orders.hasNext} onClick={() => setPage((current) => current + 1)}><ChevronRight aria-hidden="true" /></button></div></footer> : null}
        </section>
      ) : null}

      {editorOpen ? <RepairOrderEditor businessId={businessId} activeBranchId={activeBranchId} branches={businessContext.branches} customers={customers} services={activeServices} onClose={closeOverlay} onSaved={handleCreated} /> : null}
      {quickEditorOpen ? <QuickRepairOrderEditor businessId={businessId} activeBranchId={activeBranchId} branches={businessContext.branches} services={activeServices} onClose={closeOverlay} onSaved={handleCreated} /> : null}
      {detail ? <RepairOrderDetail businessId={businessId} order={detail} canUpdate={hasPermission(permissions, "REPAIR_ORDER_UPDATE")} canClose={hasPermission(permissions, "REPAIR_ORDER_CLOSE")} canViewOrderStatuses={canViewCatalogs} canUploadEvidence={hasPermission(permissions, "REPAIR_ORDER_UPLOAD_EVIDENCE")} canViewQuotations={hasPermission(permissions, "QUOTATION_VIEW")} canCreateQuotations={hasPermission(permissions, "QUOTATION_CREATE")} canUpdateQuotations={hasPermission(permissions, "QUOTATION_UPDATE")} canViewQuotationStatuses={canViewCatalogs} onClose={closeOverlay} onUpdated={(updatedOrder) => {
        setDetail(updatedOrder);
        setOrders((current) => current ? { ...current, items: current.items.map((item) => item.id === updatedOrder.id ? updatedOrder : item) } : current);
      }} /> : null}
      {!canCreate ? <div className="tm-service-readonly"><CircleAlert aria-hidden="true" /><p><strong>Acceso de consulta</strong><span>Puedes revisar las órdenes, pero tu rol no permite recibir equipos.</span></p></div> : null}
    </div>
  );
}

function RepairOrderRow({
  order,
  onOpenDetail,
}: {
  order: RepairOrder;
  onOpenDetail: (trigger: HTMLButtonElement, order: RepairOrder) => void;
}) {
  const StatusIcon = repairStatusIcon(order.statusCode);

  return (
    <article className="tm-repair-row">
                  <div className="tm-repair-row__number"><strong>{order.orderNumber}</strong><span>{formatDateTime(order.receivedAt)}</span></div>
                  <div className="tm-repair-row__customer"><span><UserRound aria-hidden="true" /></span><div><h3>{order.customerName} {order.customerSurname}</h3><p>{order.customerEmail}</p></div></div>
                  <div className="tm-repair-row__work"><p><Smartphone aria-hidden="true" /><span>{order.customerDeviceLabel}</span></p><p><Wrench aria-hidden="true" /><span>{order.businessServiceName}</span></p><p><MapPin aria-hidden="true" /><span>{order.branchName}</span></p></div>
                  <div className="tm-repair-row__assignment"><small>Técnico asignado</small><strong>{order.assignedBusinessUserName}</strong><span><Banknote aria-hidden="true" /> {formatMoney(order.estimatedPrice)}</span></div>
      <div className="tm-repair-row__actions"><span className={`tm-repair-status ${statusClass(order.statusCode)}`}><StatusIcon aria-hidden="true" /> {order.statusName}</span><button className="tm-button tm-button--secondary" type="button" onClick={(event) => onOpenDetail(event.currentTarget, order)}><Eye aria-hidden="true" /> Ver detalle</button></div>
    </article>
  );
}
