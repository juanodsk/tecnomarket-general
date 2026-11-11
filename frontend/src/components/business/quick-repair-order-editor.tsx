"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import {
  CheckCircle2,
  LoaderCircle,
  MapPin,
  Smartphone,
  UserRound,
  Wrench,
  X,
} from "lucide-react";
import { useEffect, useMemo, useRef, useState } from "react";
import { useForm } from "react-hook-form";

import { FormAlert } from "@/components/ui/feedback";
import { SelectField, TextField } from "@/components/ui/form-controls";
import * as customerDevicesApi from "@/features/customer-devices/api";
import * as locationApi from "@/features/locations/api";
import * as repairOrdersApi from "@/features/repair-orders/api";
import {
  normalizeOptionalDateTime,
  normalizeOptionalText,
} from "@/features/repair-orders/schemas";
import {
  quickRepairOrderDefaultValues,
  quickRepairOrderSchema,
  type QuickRepairOrderFormValues,
} from "@/features/repair-orders/quick-schema";
import { normalizeApiError } from "@/lib/api/errors";
import type { BusinessContextBranch } from "@/types/business";
import type { BusinessCatalogItem, BusinessService } from "@/types/business-service";
import type { City, Country, Department, DocumentType } from "@/types/location";
import type { BusinessTechnicianOption, RepairOrder } from "@/types/repair-order";
import type { QuickRepairOrderResponse } from "@/types/quick-repair-order";

type CatalogState<T> = { items: T[]; loading: boolean };

function currentDateTimeLocal() {
  const now = new Date();
  const local = new Date(now.getTime() - now.getTimezoneOffset() * 60_000);
  return local.toISOString().slice(0, 16);
}

function newIdempotencyKey() {
  return crypto.randomUUID();
}

export function QuickRepairOrderEditor({
  businessId,
  activeBranchId,
  branches,
  services,
  onClose,
  onSaved,
}: {
  businessId: number;
  activeBranchId: number | null;
  branches: BusinessContextBranch[];
  services: BusinessService[];
  onClose: () => void;
  onSaved: (order: RepairOrder) => Promise<void>;
}) {
  const panelRef = useRef<HTMLElement>(null);
  const closeRef = useRef<HTMLButtonElement>(null);
  const idempotencyRef = useRef<{ payload: string; key: string } | null>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [catalogError, setCatalogError] = useState<string | null>(null);
  const [loadingCatalogs, setLoadingCatalogs] = useState(true);
  const [documentTypes, setDocumentTypes] = useState<DocumentType[]>([]);
  const [countries, setCountries] = useState<Country[]>([]);
  const [departments, setDepartments] = useState<Department[]>([]);
  const [cities, setCities] = useState<City[]>([]);
  const [deviceTypes, setDeviceTypes] = useState<BusinessCatalogItem[]>([]);
  const [brands, setBrands] = useState<BusinessCatalogItem[]>([]);
  const [technicians, setTechnicians] = useState<BusinessTechnicianOption[]>([]);
  const [loadingDepartments, setLoadingDepartments] = useState(false);
  const [loadingCities, setLoadingCities] = useState(false);
  const [loadingTechnicians, setLoadingTechnicians] = useState(false);
  const [technicianError, setTechnicianError] = useState<string | null>(null);
  const {
    register,
    handleSubmit,
    watch,
    setValue,
    formState: { errors, isSubmitting },
  } = useForm<QuickRepairOrderFormValues>({
    resolver: zodResolver(quickRepairOrderSchema),
    defaultValues: {
      ...quickRepairOrderDefaultValues,
      branchId: activeBranchId ? String(activeBranchId) : branches[0] ? String(branches[0].id) : "",
      registeredAt: currentDateTimeLocal(),
      receivedAt: currentDateTimeLocal(),
    },
  });

  const countryId = watch("countryId");
  const departmentId = watch("departmentId");
  const deviceTypeId = watch("deviceTypeId");
  const branchId = watch("branchId");
  const activeDeviceTypes = useMemo(() => deviceTypes.filter((item) => item.active), [deviceTypes]);
  const activeBrands = useMemo(() => brands.filter((item) => item.active), [brands]);
  const compatibleServices = useMemo(
    () => services.filter((service) => service.active && service.deviceTypeId === Number(deviceTypeId)),
    [deviceTypeId, services],
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
    let active = true;
    setLoadingCatalogs(true);
    setCatalogError(null);
    Promise.all([
      locationApi.getDocumentTypes(),
      locationApi.getCountries(),
      customerDevicesApi.getCustomerDeviceCatalog(businessId, "DEVICE_TYPE"),
      customerDevicesApi.getCustomerDeviceCatalog(businessId, "BRAND"),
    ])
      .then(([types, countryList, deviceTypeList, brandList]) => {
        if (!active) return;
        setDocumentTypes(types.filter((item) => item.active && item.appliesToPerson));
        setCountries(countryList.filter((item) => item.active));
        setDeviceTypes(deviceTypeList);
        setBrands(brandList);
      })
      .catch((error) => {
        if (active) setCatalogError(normalizeApiError(error).message);
      })
      .finally(() => {
        if (active) setLoadingCatalogs(false);
      });
    return () => { active = false; };
  }, [businessId]);

  useEffect(() => {
    let active = true;
    setDepartments([]);
    setCities([]);
    setValue("departmentId", "");
    setValue("cityId", "");
    if (!countryId) return () => { active = false; };
    setLoadingDepartments(true);
    locationApi.getDepartments(Number(countryId))
      .then((items) => { if (active) setDepartments(items.filter((item) => item.active)); })
      .catch((error) => { if (active) setCatalogError(normalizeApiError(error).message); })
      .finally(() => { if (active) setLoadingDepartments(false); });
    return () => { active = false; };
  }, [countryId, setValue]);

  useEffect(() => {
    let active = true;
    setCities([]);
    setValue("cityId", "");
    if (!departmentId) return () => { active = false; };
    setLoadingCities(true);
    locationApi.getCities(Number(departmentId))
      .then((items) => { if (active) setCities(items.filter((item) => item.active)); })
      .catch((error) => { if (active) setCatalogError(normalizeApiError(error).message); })
      .finally(() => { if (active) setLoadingCities(false); });
    return () => { active = false; };
  }, [departmentId, setValue]);

  useEffect(() => {
    setValue("businessServiceId", "");
  }, [deviceTypeId, setValue]);

  useEffect(() => {
    let active = true;
    if (!branchId) {
      setTechnicians([]);
      setValue("assignedBusinessUserId", "");
      return () => { active = false; };
    }
    setLoadingTechnicians(true);
    setTechnicianError(null);
    setValue("assignedBusinessUserId", "");
    repairOrdersApi.getRepairOrderTechnicians(businessId, Number(branchId))
      .then((items) => {
        if (!active) return;
        setTechnicians(items);
        if (!items.length) setTechnicianError("Esta sede no tiene técnicos activos disponibles.");
      })
      .catch((error) => { if (active) setTechnicianError(normalizeApiError(error).message); })
      .finally(() => { if (active) setLoadingTechnicians(false); });
    return () => { active = false; };
  }, [branchId, businessId, setValue]);

  async function submit(values: QuickRepairOrderFormValues) {
    setSubmitError(null);
    const request = {
      customer: {
        documentTypeCode: values.documentTypeCode,
        documentNumber: values.documentNumber.trim(),
        name: values.name.trim(),
        surname: values.surname.trim(),
        email: values.email.trim(),
        address: values.address.trim(),
        phone: values.phone.trim(),
        countryId: Number(values.countryId),
        departmentId: Number(values.departmentId),
        cityId: Number(values.cityId),
      },
      device: {
        deviceTypeId: Number(values.deviceTypeId),
        brandId: Number(values.brandId),
        model: values.model.trim(),
        serialNumber: normalizeOptionalText(values.serialNumber),
        imei: normalizeOptionalText(values.imei),
        color: values.color.trim(),
        registeredAt: normalizeOptionalDateTime(values.registeredAt),
      },
      order: {
        branchId: Number(values.branchId),
        businessServiceId: Number(values.businessServiceId),
        assignedBusinessUserId: Number(values.assignedBusinessUserId),
        problemDescription: values.problemDescription.trim(),
        diagnosis: normalizeOptionalText(values.diagnosis),
        estimatedPrice: values.estimatedPrice ? Number(values.estimatedPrice) : null,
        estimatedDeliveryAt: normalizeOptionalDateTime(values.estimatedDeliveryAt),
        receivedAt: normalizeOptionalDateTime(values.receivedAt),
      },
    };
    const payload = JSON.stringify(request);
    if (idempotencyRef.current?.payload !== payload) {
      idempotencyRef.current = { payload, key: newIdempotencyKey() };
    }

    try {
      const result: QuickRepairOrderResponse = await repairOrdersApi.createQuickRepairOrder(
        businessId,
        request,
        idempotencyRef.current.key,
      );
      await onSaved(result.repairOrder);
      onClose();
    } catch (error) {
      setSubmitError(normalizeApiError(error).message);
    }
  }

  return (
    <div className="tm-review-backdrop">
      <section
        ref={panelRef}
        className="tm-review-panel tm-repair-editor tm-quick-repair-editor"
        role="dialog"
        aria-modal="true"
        aria-labelledby="quick-repair-editor-title"
      >
        <header className="tm-review-panel__head">
          <div>
            <h2 id="quick-repair-editor-title">Crear orden rápida</h2>
            <p>Registra al cliente, su equipo y la recepción en una sola operación.</p>
          </div>
          <button ref={closeRef} className="tm-icon-button" type="button" aria-label="Cerrar" onClick={onClose} disabled={isSubmitting}>
            <X aria-hidden="true" />
          </button>
        </header>

        <form className="tm-review-panel__body tm-repair-form tm-quick-repair-form" onSubmit={handleSubmit(submit)} noValidate>
          {submitError ? <FormAlert>{submitError}</FormAlert> : null}
          {catalogError ? <FormAlert tone="warning">No se pudieron cargar los datos para el formulario: {catalogError}</FormAlert> : null}

          <fieldset>
            <legend><UserRound aria-hidden="true" /> Cliente</legend>
            <div className="tm-quick-repair-form__grid">
              <SelectField label="Tipo de documento" disabled={loadingCatalogs} error={errors.documentTypeCode?.message} {...register("documentTypeCode")}>
                <option value="">Selecciona una opción</option>
                {documentTypes.map((item) => <option key={item.id} value={item.code}>{item.name}</option>)}
              </SelectField>
              <TextField label="Número de documento" maxLength={30} autoComplete="off" error={errors.documentNumber?.message} {...register("documentNumber")} />
              <TextField label="Nombre" autoComplete="given-name" maxLength={50} error={errors.name?.message} {...register("name")} />
              <TextField label="Apellido" autoComplete="family-name" maxLength={50} error={errors.surname?.message} {...register("surname")} />
              <TextField label="Correo" type="email" autoComplete="email" maxLength={150} error={errors.email?.message} {...register("email")} />
              <TextField label="Teléfono" type="tel" autoComplete="tel" maxLength={30} error={errors.phone?.message} {...register("phone")} />
              <SelectField label="País" disabled={loadingCatalogs} error={errors.countryId?.message} {...register("countryId")}>
                <option value="">Selecciona un país</option>
                {countries.map((item) => <option key={item.id} value={item.id}>{item.name}</option>)}
              </SelectField>
              <SelectField label="Departamento" disabled={!countryId || loadingDepartments} error={errors.departmentId?.message} {...register("departmentId")}>
                <option value="">{loadingDepartments ? "Cargando departamentos…" : "Selecciona un departamento"}</option>
                {departments.map((item) => <option key={item.id} value={item.id}>{item.name}</option>)}
              </SelectField>
              <SelectField label="Ciudad" disabled={!departmentId || loadingCities} error={errors.cityId?.message} {...register("cityId")}>
                <option value="">{loadingCities ? "Cargando ciudades…" : "Selecciona una ciudad"}</option>
                {cities.map((item) => <option key={item.id} value={item.id}>{item.name}</option>)}
              </SelectField>
              <TextField label="Dirección" autoComplete="street-address" maxLength={200} error={errors.address?.message} {...register("address")} />
            </div>
          </fieldset>

          <fieldset>
            <legend><Smartphone aria-hidden="true" /> Equipo</legend>
            <div className="tm-quick-repair-form__grid">
              <SelectField label="Tipo de equipo" disabled={loadingCatalogs} error={errors.deviceTypeId?.message} {...register("deviceTypeId")}>
                <option value="">Selecciona un tipo</option>
                {activeDeviceTypes.map((item) => <option key={item.id} value={item.id}>{item.name}</option>)}
              </SelectField>
              <SelectField label="Marca" disabled={loadingCatalogs} error={errors.brandId?.message} {...register("brandId")}>
                <option value="">Selecciona una marca</option>
                {activeBrands.map((item) => <option key={item.id} value={item.id}>{item.name}</option>)}
              </SelectField>
              <TextField label="Modelo" maxLength={50} placeholder="Ej. iPhone 13 Pro" error={errors.model?.message} {...register("model")} />
              <TextField label="Color" maxLength={20} error={errors.color?.message} {...register("color")} />
              <TextField label="IMEI" maxLength={60} error={errors.imei?.message} {...register("imei")} />
              <TextField label="Número de serie" maxLength={60} error={errors.serialNumber?.message} {...register("serialNumber")} />
            </div>
          </fieldset>

          <fieldset>
            <legend><Wrench aria-hidden="true" /> Recepción y trabajo</legend>
            <div className="tm-quick-repair-form__grid">
              <SelectField label="Sede" error={errors.branchId?.message} {...register("branchId")}>
                <option value="">Selecciona una sede</option>
                {branches.map((branch) => <option key={branch.id} value={branch.id}>{branch.name}{branch.primaryBranch ? " · Principal" : ""}</option>)}
              </SelectField>
              <SelectField label="Técnico asignado" disabled={!branchId || loadingTechnicians} error={errors.assignedBusinessUserId?.message ?? technicianError ?? undefined} {...register("assignedBusinessUserId")}>
                <option value="">{loadingTechnicians ? "Cargando técnicos…" : "Selecciona un técnico"}</option>
                {technicians.map((technician) => <option key={technician.businessUserId} value={technician.businessUserId}>{technician.userName} {technician.userSurname} · {technician.roleName}</option>)}
              </SelectField>
              <SelectField label="Servicio" disabled={!deviceTypeId || loadingCatalogs} error={errors.businessServiceId?.message} {...register("businessServiceId")}>
                <option value="">{deviceTypeId ? "Selecciona un servicio" : "Primero selecciona el tipo de equipo"}</option>
                {compatibleServices.map((service) => <option key={service.id} value={service.id}>{service.name}</option>)}
              </SelectField>
              <TextField label="Fecha de recepción" type="datetime-local" error={errors.receivedAt?.message} {...register("receivedAt")} />
              <TextField label="Fecha estimada de entrega" type="datetime-local" error={errors.estimatedDeliveryAt?.message} {...register("estimatedDeliveryAt")} />
              <TextField label="Precio estimado" type="number" min="0" step="0.01" error={errors.estimatedPrice?.message} {...register("estimatedPrice")} />
              <div className="tm-field tm-field--span">
                <label htmlFor="quick-repair-problem">Problema reportado</label>
                <textarea id="quick-repair-problem" maxLength={2000} placeholder="Describe lo que informa el cliente y los síntomas visibles" aria-invalid={Boolean(errors.problemDescription)} {...register("problemDescription")} />
                {errors.problemDescription ? <p className="tm-field__error" role="alert">{errors.problemDescription.message}</p> : null}
              </div>
              <div className="tm-field tm-field--span">
                <label htmlFor="quick-repair-diagnosis">Diagnóstico inicial <span>Opcional</span></label>
                <textarea id="quick-repair-diagnosis" maxLength={2000} placeholder="Hallazgos iniciales, si ya se conocen" aria-invalid={Boolean(errors.diagnosis)} {...register("diagnosis")} />
                {errors.diagnosis ? <p className="tm-field__error" role="alert">{errors.diagnosis.message}</p> : null}
              </div>
              <TextField label="Fecha de registro del equipo" type="datetime-local" hint="Opcional; si se deja vacía, el backend conserva su valor predeterminado." error={errors.registeredAt?.message} {...register("registeredAt")} />
            </div>
          </fieldset>

          <p className="tm-quick-repair-form__note"><MapPin aria-hidden="true" /> La orden se registrará para la sede y el técnico seleccionados.</p>
          <footer className="tm-repair-form__actions">
            <button className="tm-button tm-button--secondary" type="button" onClick={onClose} disabled={isSubmitting}>Cancelar</button>
            <button className="tm-button tm-button--primary" type="submit" disabled={isSubmitting || loadingCatalogs || loadingDepartments || loadingCities || loadingTechnicians || !technicians.length || !activeDeviceTypes.length || !activeBrands.length || !compatibleServices.length}>
              {isSubmitting ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <CheckCircle2 aria-hidden="true" />}
              {isSubmitting ? "Creando orden…" : "Crear cliente, equipo y orden"}
            </button>
          </footer>
        </form>
      </section>
    </div>
  );
}
