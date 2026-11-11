"use client";

import {
  ArrowLeft,
  ArrowRight,
  Building2,
  Check,
  ChevronLeft,
  ChevronRight,
  ClipboardCheck,
  ExternalLink,
  FileText,
  Inbox,
  LoaderCircle,
  Mail,
  MapPin,
  Phone,
  RefreshCw,
  ShieldCheck,
  UserRound,
  X,
} from "lucide-react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useMemo, useRef, useState, type ReactNode } from "react";

import { EmptyState, ErrorState, FormAlert, FullPageLoader } from "@/components/ui/feedback";
import * as businessApi from "@/features/business/api";
import { normalizeApiError } from "@/lib/api/errors";
import { hasPermission, hasRole } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";
import type { PageResponse } from "@/types/api";
import type { BusinessRequest } from "@/types/business";

const PAGE_SIZE = 20;
const PREVIEW_SIZE = 5;
const dateFormatter = new Intl.DateTimeFormat("es-CO", { dateStyle: "medium", timeStyle: "short" });

function formatDate(value: string) {
  const date = new Date(value);
  return Number.isNaN(date.getTime()) ? "Fecha no disponible" : dateFormatter.format(date);
}

function safeFileUrl(value: string) {
  try {
    const url = new URL(value);
    return url.protocol === "https:" || url.protocol === "http:" ? url.toString() : null;
  } catch {
    return null;
  }
}

export function PlatformAccess({ children, permission }: { children: ReactNode; permission: string }) {
  const { access } = useAuth();
  const router = useRouter();
  const isPlatformAdmin = hasRole(access?.roles ?? [], "PLATFORM_ADMIN");
  const hasRequiredPermission = hasPermission(access?.permissions ?? [], permission);

  useEffect(() => {
    if (!isPlatformAdmin) router.replace("/acceso-denegado");
  }, [isPlatformAdmin, router]);

  if (!isPlatformAdmin) return <FullPageLoader label="Validando acceso administrativo" />;
  if (!hasRequiredPermission) {
    return <ErrorState title="Acceso administrativo limitado" message="Tu cuenta no tiene el permiso necesario para abrir esta sección." />;
  }
  return children;
}

function usePendingRequests(page: number, size: number) {
  const [data, setData] = useState<PageResponse<BusinessRequest> | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const requestSequence = useRef(0);

  const load = useCallback(async () => {
    const sequence = ++requestSequence.current;
    setLoading(true);
    setError(null);
    try {
      const nextData = await businessApi.getPendingBusinessRequests(page, size);
      if (sequence === requestSequence.current) setData(nextData);
    } catch (requestError) {
      if (sequence === requestSequence.current) setError(normalizeApiError(requestError).message);
    } finally {
      if (sequence === requestSequence.current) setLoading(false);
    }
  }, [page, size]);

  useEffect(() => {
    void load();
    return () => { requestSequence.current += 1; };
  }, [load]);

  return { data, loading, error, reload: load };
}

function RequestPreview({ request }: { request: BusinessRequest }) {
  return (
    <article className="tm-platform-preview">
      <span className="tm-platform-preview__mark"><Building2 aria-hidden="true" /></span>
      <div><h3>{request.businessName}</h3><p>{request.requestedByUserName} · {request.cityName}</p></div>
      <time dateTime={request.createdAt}>{formatDate(request.createdAt)}</time>
    </article>
  );
}

export function PlatformDashboard() {
  const { data, loading, error, reload } = usePendingRequests(0, PREVIEW_SIZE);

  return (
    <PlatformAccess permission="BUSINESS_REQUEST_REVIEW">
      <div className="tm-page-stack">
        <header className="tm-workspace-heading">
          <div><h1>Administración de plataforma</h1><p>Revisa solicitudes y mantén el ingreso de nuevos negocios bajo control.</p></div>
          <span className="tm-status-pill"><ShieldCheck aria-hidden="true" /> Acceso global</span>
        </header>
        {loading && !data ? <FullPageLoader label="Cargando el resumen administrativo" /> : null}
        {error && !data ? <ErrorState message={error} onRetry={() => void reload()} /> : null}
        {data ? (
          <>
            <section className="tm-platform-overview" aria-label="Resumen administrativo">
              <div className="tm-platform-focus">
                <div className="tm-platform-focus__head"><span><Inbox aria-hidden="true" /></span><p>Solicitudes por revisar</p></div>
                <strong>{data.totalElements}</strong>
                <p>{data.totalElements === 1 ? "Hay una solicitud esperando decisión." : `Hay ${data.totalElements} solicitudes esperando decisión.`}</p>
                <Link className="tm-button tm-button--primary" href="/plataforma/solicitudes">Abrir bandeja <ArrowRight aria-hidden="true" /></Link>
              </div>
              <aside className="tm-platform-scope">
                <h2>Operación disponible</h2>
                <ul>
                  <li><Check aria-hidden="true" /><span><strong>Revisar solicitudes</strong><small>Consulta los datos y documentos enviados.</small></span></li>
                  <li><Check aria-hidden="true" /><span><strong>Aprobar negocios</strong><small>Crea el negocio y la relación con su propietario.</small></span></li>
                  <li><Check aria-hidden="true" /><span><strong>Registrar rechazos</strong><small>Comunica un motivo concreto al solicitante.</small></span></li>
                </ul>
              </aside>
            </section>
            <section className="tm-platform-queue">
              <header><div><h2>Próximas por revisar</h2><p>Las solicitudes más antiguas aparecen primero.</p></div><Link href="/plataforma/solicitudes">Ver todas <ArrowRight aria-hidden="true" /></Link></header>
              {error ? <FormAlert>{error}</FormAlert> : null}
              {data.items.length ? <div>{data.items.map((request) => <RequestPreview key={request.id} request={request} />)}</div> : <EmptyState title="La bandeja está al día" message="No hay solicitudes de negocio pendientes por revisar." />}
            </section>
          </>
        ) : null}
      </div>
    </PlatformAccess>
  );
}

function RequestReviewPanel({ request, onClose, onResolved }: { request: BusinessRequest; onClose: () => void; onResolved: (message: string) => Promise<void> }) {
  const [action, setAction] = useState<"approve" | "reject" | null>(null);
  const [rejectionReason, setRejectionReason] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [saving, setSaving] = useState(false);
  const panelRef = useRef<HTMLElement>(null);
  const closeButtonRef = useRef<HTMLButtonElement>(null);
  const legalFileUrl = safeFileUrl(request.legalFile);

  useEffect(() => {
    const previousOverflow = document.body.style.overflow;
    document.body.style.overflow = "hidden";
    closeButtonRef.current?.focus();
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape" && !saving) { onClose(); return; }
      if (event.key !== "Tab") return;
      const focusable = Array.from(panelRef.current?.querySelectorAll<HTMLElement>("a[href], button:not([disabled]), textarea:not([disabled])") ?? []);
      if (!focusable.length) return;
      const first = focusable[0];
      const last = focusable[focusable.length - 1];
      if (event.shiftKey && document.activeElement === first) { event.preventDefault(); last.focus(); }
      else if (!event.shiftKey && document.activeElement === last) { event.preventDefault(); first.focus(); }
    };
    window.addEventListener("keydown", handleKeyDown);
    return () => {
      document.body.style.overflow = previousOverflow;
      window.removeEventListener("keydown", handleKeyDown);
    };
  }, [onClose, saving]);

  async function approve() {
    setSaving(true); setError(null);
    try {
      await businessApi.approveBusinessRequest(request.id);
      await onResolved(`Se aprobó la solicitud de ${request.businessName}.`);
    } catch (requestError) { setError(normalizeApiError(requestError).message); setSaving(false); }
  }

  async function reject() {
    const reason = rejectionReason.trim();
    if (!reason) { setError("Escribe el motivo del rechazo antes de continuar."); return; }
    setSaving(true); setError(null);
    try {
      await businessApi.rejectBusinessRequest(request.id, { rejectionReason: reason });
      await onResolved(`Se rechazó la solicitud de ${request.businessName}.`);
    } catch (requestError) { setError(normalizeApiError(requestError).message); setSaving(false); }
  }

  return (
    <div className="tm-review-backdrop">
      <section ref={panelRef} className="tm-review-panel" role="dialog" aria-modal="true" aria-labelledby="request-review-title">
        <header className="tm-review-panel__head">
          <div><span>Solicitud #{request.id}</span><h2 id="request-review-title">{request.businessName}</h2><p>Enviada {formatDate(request.createdAt)}</p></div>
          <button ref={closeButtonRef} className="tm-icon-button" type="button" aria-label="Cerrar detalle" disabled={saving} onClick={onClose}><X aria-hidden="true" /></button>
        </header>
        <div className="tm-review-panel__body">
          <section className="tm-review-block">
            <h3>Negocio</h3>
            <dl className="tm-review-facts">
              <div><dt>Nombre comercial</dt><dd>{request.businessName}</dd></div><div><dt>Razón social</dt><dd>{request.legalName || "No registrada"}</dd></div>
              <div><dt>Documento</dt><dd>{request.documentTypeCode} {request.documentNumber}</dd></div><div><dt>Ciudad</dt><dd>{request.cityName}</dd></div>
              <div className="tm-review-facts__wide"><dt>Dirección</dt><dd>{request.address || "No registrada"}</dd></div>
            </dl>
          </section>
          <section className="tm-review-block">
            <h3>Contacto y solicitante</h3>
            <div className="tm-review-contact">
              <p><UserRound aria-hidden="true" /><span><small>Solicitante</small><strong>{request.requestedByUserName}</strong></span></p>
              <p><Mail aria-hidden="true" /><span><small>Correo</small><strong>{request.email}</strong></span></p>
              <p><Phone aria-hidden="true" /><span><small>Teléfono</small><strong>{request.phone}</strong></span></p>
            </div>
          </section>
          <section className="tm-review-block">
            <h3>Documento legal</h3>
            {legalFileUrl ? <a className="tm-legal-file" href={legalFileUrl} target="_blank" rel="noreferrer"><FileText aria-hidden="true" /><span><strong>Abrir documento adjunto</strong><small>Se abrirá en una nueva pestaña.</small></span><ExternalLink aria-hidden="true" /></a> : <p className="tm-review-unavailable">El archivo registrado no contiene una dirección web válida.</p>}
          </section>
          {error ? <FormAlert>{error}</FormAlert> : null}
          {action === "approve" ? (
            <section className="tm-review-decision tm-review-decision--approve">
              <div><ClipboardCheck aria-hidden="true" /><span><h3>Confirmar aprobación</h3><p>El backend creará el negocio y asociará al solicitante como propietario.</p></span></div>
              <div className="tm-review-decision__actions"><button className="tm-button tm-button--secondary" type="button" disabled={saving} onClick={() => setAction(null)}>Volver</button><button className="tm-button tm-button--primary" type="button" disabled={saving} onClick={() => void approve()}>{saving ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Check aria-hidden="true" />} Aprobar negocio</button></div>
            </section>
          ) : null}
          {action === "reject" ? (
            <section className="tm-review-decision tm-review-decision--reject">
              <div><h3>Motivo del rechazo</h3><p>El mensaje debe explicar qué necesita corregir el solicitante.</p></div>
              <label className="tm-textarea-field"><span>Motivo</span><textarea value={rejectionReason} maxLength={1000} rows={5} disabled={saving} onChange={(event) => setRejectionReason(event.target.value)} /><small>{rejectionReason.length}/1000 caracteres</small></label>
              <div className="tm-review-decision__actions"><button className="tm-button tm-button--secondary" type="button" disabled={saving} onClick={() => setAction(null)}>Volver</button><button className="tm-button tm-button--danger" type="button" disabled={saving} onClick={() => void reject()}>{saving ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <X aria-hidden="true" />} Rechazar solicitud</button></div>
            </section>
          ) : null}
        </div>
        {!action ? <footer className="tm-review-panel__foot"><button className="tm-button tm-button--danger" type="button" onClick={() => { setError(null); setAction("reject"); }}>Rechazar</button><button className="tm-button tm-button--primary" type="button" onClick={() => { setError(null); setAction("approve"); }}>Aprobar</button></footer> : null}
      </section>
    </div>
  );
}

export function PlatformRequests() {
  const [page, setPage] = useState(0);
  const [selected, setSelected] = useState<BusinessRequest | null>(null);
  const [feedback, setFeedback] = useState<string | null>(null);
  const reviewTriggerRef = useRef<HTMLButtonElement | null>(null);
  const { data, loading, error, reload } = usePendingRequests(page, PAGE_SIZE);
  const pageLabel = useMemo(() => !data?.totalPages ? "Página 1 de 1" : `Página ${data.page + 1} de ${data.totalPages}`, [data]);
  const closePanel = useCallback(() => {
    setSelected(null);
    requestAnimationFrame(() => reviewTriggerRef.current?.focus());
  }, []);

  async function handleResolved(message: string) {
    setSelected(null); setFeedback(message);
    if (data?.items.length === 1 && page > 0) setPage((current) => current - 1);
    else await reload();
  }

  return (
    <PlatformAccess permission="BUSINESS_REQUEST_REVIEW">
      <div className="tm-page-stack">
        <header className="tm-workspace-heading">
          <div><h1>Solicitudes de negocio</h1><p>Verifica la información enviada antes de aprobar o rechazar cada registro.</p></div>
          <Link className="tm-button tm-button--secondary" href="/plataforma"><ArrowLeft aria-hidden="true" /> Volver al resumen</Link>
        </header>
        {feedback ? <FormAlert tone="success">{feedback}</FormAlert> : null}
        {error && data ? <FormAlert>{error}</FormAlert> : null}
        <section className="tm-request-board">
          <header className="tm-request-board__head">
            <div><span className="tm-request-board__icon"><Inbox aria-hidden="true" /></span><span><h2>Bandeja pendiente</h2><p>{data ? `${data.totalElements} ${data.totalElements === 1 ? "solicitud" : "solicitudes"}` : "Consultando solicitudes"}</p></span></div>
            <button className="tm-button tm-button--secondary" type="button" disabled={loading} onClick={() => void reload()}><RefreshCw className={loading ? "tm-spinner" : ""} aria-hidden="true" /> Actualizar</button>
          </header>
          {loading && !data ? <div className="tm-request-board__loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando solicitudes</div> : null}
          {error && !data ? <ErrorState message={error} onRetry={() => void reload()} /> : null}
          {data && !data.items.length ? <EmptyState title="La bandeja está al día" message="No hay solicitudes de negocio pendientes por revisar." /> : null}
          {data?.items.length ? <div className="tm-request-list">{data.items.map((request) => (
            <article key={request.id}>
              <span className="tm-request-list__mark"><Building2 aria-hidden="true" /></span>
              <div className="tm-request-list__identity"><h3>{request.businessName}</h3><p>{request.documentTypeCode} {request.documentNumber}</p></div>
              <div className="tm-request-list__meta"><MapPin aria-hidden="true" /><span><small>Ubicación</small><strong>{request.cityName}</strong></span></div>
              <div className="tm-request-list__meta"><UserRound aria-hidden="true" /><span><small>Solicitante</small><strong>{request.requestedByUserName}</strong></span></div>
              <time dateTime={request.createdAt}>{formatDate(request.createdAt)}</time>
              <button className="tm-button tm-button--secondary" type="button" onClick={(event) => { reviewTriggerRef.current = event.currentTarget; setFeedback(null); setSelected(request); }}>Revisar <ArrowRight aria-hidden="true" /></button>
            </article>
          ))}</div> : null}
          {data && data.totalPages > 1 ? <footer className="tm-request-pagination"><button className="tm-icon-button" type="button" aria-label="Página anterior" disabled={!data.hasPrevious || loading} onClick={() => setPage((current) => Math.max(0, current - 1))}><ChevronLeft aria-hidden="true" /></button><span>{pageLabel}</span><button className="tm-icon-button" type="button" aria-label="Página siguiente" disabled={!data.hasNext || loading} onClick={() => setPage((current) => current + 1)}><ChevronRight aria-hidden="true" /></button></footer> : null}
        </section>
      </div>
      {selected ? <RequestReviewPanel request={selected} onClose={closePanel} onResolved={handleResolved} /> : null}
    </PlatformAccess>
  );
}
