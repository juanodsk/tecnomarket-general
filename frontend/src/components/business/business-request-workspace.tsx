"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import {
  ArrowRight,
  Building2,
  CheckCircle2,
  Clock3,
  ExternalLink,
  FileCheck2,
  LoaderCircle,
  RefreshCw,
  RotateCcw,
  Send,
  ShieldCheck,
  XCircle,
} from "lucide-react";
import Link from "next/link";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { useForm } from "react-hook-form";

import { EmptyState, ErrorState, FormAlert } from "@/components/ui/feedback";
import { SelectField, TextField } from "@/components/ui/form-controls";
import * as authApi from "@/features/auth/api";
import * as businessApi from "@/features/business/api";
import {
  businessRequestSchema,
  type BusinessRequestFormValues,
} from "@/features/business/schemas";
import * as locationApi from "@/features/locations/api";
import { normalizeApiError } from "@/lib/api/errors";
import { hasPermission } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";
import type { BusinessRequest, BusinessRequestStatus } from "@/types/business";
import type { City, Country, Department, DocumentType } from "@/types/location";

const requestDateFormatter = new Intl.DateTimeFormat("es-CO", {
  dateStyle: "medium",
  timeStyle: "short",
});

const statusCopy: Record<BusinessRequestStatus, { label: string; description: string }> = {
  PENDING: {
    label: "Pendiente",
    description: "El equipo de TecMarket está revisando la información enviada.",
  },
  APPROVED: {
    label: "Aprobada",
    description: "El negocio fue creado y sus accesos ya pueden estar disponibles.",
  },
  REJECTED: {
    label: "Rechazada",
    description: "Revisa el motivo antes de enviar una nueva solicitud.",
  },
};

function formatDate(value: string) {
  const date = new Date(value);
  return Number.isNaN(date.getTime()) ? "Fecha no disponible" : requestDateFormatter.format(date);
}

function safeHttpUrl(value: string) {
  try {
    const url = new URL(value);
    return url.protocol === "https:" || url.protocol === "http:" ? url.toString() : null;
  } catch {
    return null;
  }
}

function RequestStatus({ status }: { status: BusinessRequestStatus }) {
  const Icon = status === "APPROVED" ? CheckCircle2 : status === "REJECTED" ? XCircle : Clock3;
  return (
    <span className={`tm-owner-request-status tm-owner-request-status--${status.toLowerCase()}`}>
      <Icon aria-hidden="true" /> {statusCopy[status].label}
    </span>
  );
}

function RequestHistory({
  requests,
  businesses,
  leaving,
  onRenewAccess,
}: {
  requests: BusinessRequest[];
  businesses: ReturnType<typeof useAuth>["businesses"];
  leaving: boolean;
  onRenewAccess: () => void;
}) {
  if (!requests.length) {
    return (
      <EmptyState
        title="Aún no has enviado solicitudes"
        message="Cuando envíes la primera, podrás consultar aquí su estado y la respuesta de revisión."
      />
    );
  }

  return (
    <div className="tm-owner-request-history">
      {requests.map((request) => {
        const documentUrl = safeHttpUrl(request.legalFile);
        const approvedBusiness = request.status === "APPROVED"
          ? businesses.find((business) =>
              business.documentTypeCode === request.documentTypeCode &&
              business.documentNumber === request.documentNumber,
            )
          : null;

        return (
          <article key={request.id}>
            <header>
              <div>
                <h3>{request.businessName}</h3>
                <p>{request.documentTypeCode} {request.documentNumber}</p>
              </div>
              <RequestStatus status={request.status} />
            </header>
            <dl>
              <div><dt>Ciudad</dt><dd>{request.cityName}</dd></div>
              <div><dt>Enviada</dt><dd><time dateTime={request.createdAt}>{formatDate(request.createdAt)}</time></dd></div>
            </dl>
            {request.status === "REJECTED" && request.rejectionReason ? (
              <div className="tm-owner-request-reason" role="note">
                <strong>Motivo de rechazo</strong>
                <p>{request.rejectionReason}</p>
              </div>
            ) : null}
            <footer>
              {documentUrl ? (
                <a href={documentUrl} target="_blank" rel="noreferrer">
                  <ExternalLink aria-hidden="true" /> Ver documento
                </a>
              ) : <span>Documento sin enlace válido</span>}
              {request.status === "APPROVED" ? approvedBusiness ? (
                <Link className="tm-button tm-button--secondary" href={`/app/negocios/${approvedBusiness.id}`}>
                  Abrir negocio <ArrowRight aria-hidden="true" />
                </Link>
              ) : (
                <button className="tm-button tm-button--secondary" type="button" disabled={leaving} onClick={onRenewAccess}>
                  {leaving ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <RotateCcw aria-hidden="true" />}
                  Volver a ingresar
                </button>
              ) : null}
            </footer>
          </article>
        );
      })}
    </div>
  );
}

function BusinessRequestForm({
  onCreated,
}: {
  onCreated: (request: BusinessRequest) => void;
}) {
  const { user } = useAuth();
  const [documentTypes, setDocumentTypes] = useState<DocumentType[]>([]);
  const [countries, setCountries] = useState<Country[]>([]);
  const [departments, setDepartments] = useState<Department[]>([]);
  const [cities, setCities] = useState<City[]>([]);
  const [loadingCatalogs, setLoadingCatalogs] = useState(true);
  const [loadingDepartments, setLoadingDepartments] = useState(false);
  const [loadingCities, setLoadingCities] = useState(false);
  const [catalogError, setCatalogError] = useState<string | null>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const {
    register,
    handleSubmit,
    watch,
    resetField,
    formState: { errors, isSubmitting },
  } = useForm<BusinessRequestFormValues>({
    resolver: zodResolver(businessRequestSchema),
    defaultValues: {
      documentTypeCode: "",
      documentNumber: "",
      businessName: "",
      legalName: "",
      email: user?.email ?? "",
      phone: user?.phone ?? "",
      countryId: "",
      departmentId: "",
      cityId: "",
      address: "",
      legalFile: "",
    },
  });
  const countryId = watch("countryId");
  const departmentId = watch("departmentId");

  useEffect(() => {
    let active = true;
    setLoadingCatalogs(true);
    Promise.all([locationApi.getDocumentTypes(), locationApi.getCountries()])
      .then(([types, countryList]) => {
        if (!active) return;
        setDocumentTypes(types.filter((item) => item.active && item.appliesToCompany));
        setCountries(countryList.filter((item) => item.active));
      })
      .catch((error) => {
        if (active) setCatalogError(normalizeApiError(error).message);
      })
      .finally(() => {
        if (active) setLoadingCatalogs(false);
      });
    return () => { active = false; };
  }, []);

  useEffect(() => {
    let active = true;
    setDepartments([]);
    setCities([]);
    resetField("departmentId");
    resetField("cityId");
    if (!countryId) return () => { active = false; };
    setLoadingDepartments(true);
    locationApi.getDepartments(Number(countryId))
      .then((items) => {
        if (active) setDepartments(items.filter((item) => item.active));
      })
      .catch((error) => {
        if (active) setCatalogError(normalizeApiError(error).message);
      })
      .finally(() => {
        if (active) setLoadingDepartments(false);
      });
    return () => { active = false; };
  }, [countryId, resetField]);

  useEffect(() => {
    let active = true;
    setCities([]);
    resetField("cityId");
    if (!departmentId) return () => { active = false; };
    setLoadingCities(true);
    locationApi.getCities(Number(departmentId))
      .then((items) => {
        if (active) setCities(items.filter((item) => item.active));
      })
      .catch((error) => {
        if (active) setCatalogError(normalizeApiError(error).message);
      })
      .finally(() => {
        if (active) setLoadingCities(false);
      });
    return () => { active = false; };
  }, [departmentId, resetField]);

  const onSubmit = handleSubmit(async (values) => {
    setSubmitError(null);
    try {
      const created = await businessApi.createBusinessRequest({
        documentTypeCode: values.documentTypeCode,
        documentNumber: values.documentNumber,
        businessName: values.businessName,
        legalName: values.legalName,
        email: values.email,
        phone: values.phone,
        cityId: Number(values.cityId),
        address: values.address,
        legalFile: values.legalFile,
      });
      onCreated(created);
    } catch (error) {
      setSubmitError(normalizeApiError(error).message);
    }
  });

  return (
    <section className="tm-owner-request-form" aria-labelledby="business-request-form-title">
      <header>
        <span><Building2 aria-hidden="true" /></span>
        <div>
          <h2 id="business-request-form-title">Solicita la creación de tu negocio</h2>
          <p>Envía la información comercial y el documento que permitirá revisarla.</p>
        </div>
      </header>
      <form onSubmit={onSubmit} noValidate>
        {submitError ? <FormAlert>{submitError}</FormAlert> : null}
        {catalogError ? <FormAlert tone="warning">{catalogError}</FormAlert> : null}
        <fieldset>
          <legend>Identificación del negocio</legend>
          <div className="tm-owner-request-form__grid">
            <SelectField label="Tipo de documento" disabled={loadingCatalogs} error={errors.documentTypeCode?.message} {...register("documentTypeCode")}>
              <option value="">Selecciona una opción</option>
              {documentTypes.map((item) => <option key={item.id} value={item.code}>{item.name}</option>)}
            </SelectField>
            <TextField label="Número de documento" placeholder="901234567-1" error={errors.documentNumber?.message} {...register("documentNumber")} />
            <TextField label="Nombre del negocio" autoComplete="organization" error={errors.businessName?.message} {...register("businessName")} />
            <TextField label="Razón social" autoComplete="organization" hint="Opcional si coincide con el nombre del negocio." error={errors.legalName?.message} {...register("legalName")} />
          </div>
        </fieldset>
        <fieldset>
          <legend>Contacto y ubicación</legend>
          <div className="tm-owner-request-form__grid">
            <TextField label="Correo del negocio" type="email" autoComplete="email" error={errors.email?.message} {...register("email")} />
            <TextField label="Teléfono" type="tel" autoComplete="tel" error={errors.phone?.message} {...register("phone")} />
            <SelectField label="País" disabled={loadingCatalogs} error={errors.countryId?.message} {...register("countryId")}>
              <option value="">Selecciona un país</option>
              {countries.map((item) => <option key={item.id} value={item.id}>{item.name}</option>)}
            </SelectField>
            <SelectField label="Departamento" disabled={!countryId || loadingDepartments} error={errors.departmentId?.message} {...register("departmentId")}>
              <option value="">Selecciona un departamento</option>
              {departments.map((item) => <option key={item.id} value={item.id}>{item.name}</option>)}
            </SelectField>
            <SelectField label="Ciudad" disabled={!departmentId || loadingCities} error={errors.cityId?.message} {...register("cityId")}>
              <option value="">Selecciona una ciudad</option>
              {cities.map((item) => <option key={item.id} value={item.id}>{item.name}</option>)}
            </SelectField>
            <TextField label="Dirección" autoComplete="street-address" hint="Opcional." error={errors.address?.message} {...register("address")} />
          </div>
        </fieldset>
        <fieldset>
          <legend>Documento de respaldo</legend>
          <TextField
            label="Enlace del documento legal"
            type="url"
            placeholder="https://archivos.ejemplo.com/camara-comercio.pdf"
            hint="El backend recibe una URL. Asegúrate de que el equipo de revisión pueda abrirla."
            error={errors.legalFile?.message}
            {...register("legalFile")}
          />
        </fieldset>
        <footer>
          <p><ShieldCheck aria-hidden="true" /> La solicitud quedará asociada a tu cuenta.</p>
          <button className="tm-button tm-button--primary" type="submit" disabled={isSubmitting || loadingCatalogs || Boolean(catalogError)}>
            {isSubmitting ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Send aria-hidden="true" />}
            Enviar solicitud
          </button>
        </footer>
      </form>
    </section>
  );
}

export function BusinessRequestWorkspace() {
  const { access, businesses } = useAuth();
  const requestSequence = useRef(0);
  const [requests, setRequests] = useState<BusinessRequest[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [leaving, setLeaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [feedback, setFeedback] = useState<string | null>(null);
  const permissions = access?.permissions ?? [];
  const canView = hasPermission(permissions, "BUSINESS_REQUEST_VIEW_OWN");
  const canCreate = hasPermission(permissions, "BUSINESS_REQUEST_CREATE");

  const loadRequests = useCallback(async (manual = false) => {
    const sequence = ++requestSequence.current;
    if (manual) setRefreshing(true);
    else setLoading(true);
    setError(null);
    try {
      const result = await businessApi.getMyBusinessRequests();
      if (sequence === requestSequence.current) setRequests(result.items);
    } catch (requestError) {
      if (sequence === requestSequence.current) setError(normalizeApiError(requestError).message);
    } finally {
      if (sequence === requestSequence.current) {
        setLoading(false);
        setRefreshing(false);
      }
    }
  }, []);

  useEffect(() => {
    if (canView) void loadRequests();
    else setLoading(false);
    return () => { requestSequence.current += 1; };
  }, [canView, loadRequests]);

  const pendingRequest = useMemo(
    () => requests.find((request) => request.status === "PENDING") ?? null,
    [requests],
  );

  function handleCreated(request: BusinessRequest) {
    setRequests((current) => [request, ...current.filter((item) => item.id !== request.id)]);
    setFeedback("La solicitud fue enviada. Puedes consultar su estado desde esta misma página.");
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

  async function renewAccess() {
    setLeaving(true);
    try {
      await authApi.logout();
    } catch {
      // La aprobación puede haber invalidado la sesión antes de este cierre explícito.
    } finally {
      window.location.replace("/login?acceso=actualizado&next=/app");
    }
  }

  if (!canView) {
    return (
      <ErrorState
        title="Solicitudes no disponibles"
        message="Tu cuenta no tiene permiso para consultar solicitudes de negocio."
      />
    );
  }

  return (
    <div className="tm-page-stack">
      <header className="tm-page-heading">
        <div><h1>Solicitudes de negocio</h1><p>Crea un negocio en TecMarket y consulta el resultado de su revisión.</p></div>
      </header>

      {feedback ? <FormAlert tone="success">{feedback}</FormAlert> : null}
      {error && !requests.length ? <ErrorState message={error} onRetry={() => void loadRequests()} /> : null}

      {pendingRequest ? (
        <section className="tm-owner-request-current" aria-labelledby="current-request-title">
          <span><Clock3 aria-hidden="true" /></span>
          <div>
            <RequestStatus status="PENDING" />
            <h2 id="current-request-title">{pendingRequest.businessName} está en revisión</h2>
            <p>Mientras esta solicitud siga pendiente no podrás enviar otra. Aquí aparecerá la decisión cuando vuelvas a consultar.</p>
            <small>Enviada {formatDate(pendingRequest.createdAt)}</small>
          </div>
          <button className="tm-button tm-button--secondary" type="button" disabled={refreshing} onClick={() => void loadRequests(true)}>
            {refreshing ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <RefreshCw aria-hidden="true" />}
            Actualizar estado
          </button>
        </section>
      ) : null}

      {!loading ? (
        <div className="tm-owner-request-layout">
          <div>
            {!pendingRequest && canCreate ? <BusinessRequestForm onCreated={handleCreated} /> : !pendingRequest ? (
              <ErrorState title="No puedes crear solicitudes" message="Tu cuenta puede consultar el historial, pero no tiene permiso para enviar una nueva solicitud." />
            ) : (
              <section className="tm-owner-request-waiting">
                <FileCheck2 aria-hidden="true" />
                <div><h2>No necesitas completar otro formulario</h2><p>Conserva esta página como punto de seguimiento. Tus datos permanecerán disponibles cuando la solicitud cambie de estado.</p></div>
              </section>
            )}
          </div>
          <aside className="tm-owner-request-log" aria-labelledby="request-history-title">
            <header>
              <div><h2 id="request-history-title">Historial</h2><p>{requests.length} {requests.length === 1 ? "solicitud" : "solicitudes"}</p></div>
              {requests.length && !pendingRequest ? (
                <button className="tm-icon-button" type="button" disabled={refreshing} aria-label="Actualizar historial" onClick={() => void loadRequests(true)}>
                  <RefreshCw className={refreshing ? "tm-spinner" : ""} aria-hidden="true" />
                </button>
              ) : null}
            </header>
            <RequestHistory requests={requests} businesses={businesses} leaving={leaving} onRenewAccess={() => void renewAccess()} />
          </aside>
        </div>
      ) : (
        <div className="tm-owner-request-loading" aria-live="polite">
          <LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando tus solicitudes
        </div>
      )}
    </div>
  );
}
