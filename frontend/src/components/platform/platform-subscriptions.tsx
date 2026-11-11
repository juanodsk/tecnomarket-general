"use client";

import {
  AlertTriangle,
  ArrowLeft,
  ArrowRight,
  Building2,
  CalendarClock,
  CheckCircle2,
  Database,
  HardDrive,
  Layers3,
  LoaderCircle,
  MapPin,
  PackageCheck,
  ReceiptText,
  RefreshCw,
  Search,
  UsersRound,
  X,
} from "lucide-react";
import Link from "next/link";
import { useCallback, useEffect, useMemo, useRef, useState, type ReactNode } from "react";

import { PlatformAccess } from "@/components/platform/platform-workspace";
import { EmptyState, ErrorState, FormAlert } from "@/components/ui/feedback";
import * as subscriptionApi from "@/features/platform-subscriptions/api";
import { normalizeApiError, type ApiClientError } from "@/lib/api/errors";
import { hasPermission } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";
import type {
  BillingCycle,
  BusinessSubscription,
  PaymentProvider,
  SubscriptionStatus,
  SubscriptionUsage,
} from "@/types/subscription";

const dateFormatter = new Intl.DateTimeFormat("es-CO", { dateStyle: "medium" });
const dateTimeFormatter = new Intl.DateTimeFormat("es-CO", { dateStyle: "medium", timeStyle: "short" });

const statusLabels: Record<SubscriptionStatus, string> = {
  TRIALING: "En prueba",
  ACTIVE: "Activa",
  PAST_DUE: "Pago pendiente",
  CANCELLED: "Cancelada",
  EXPIRED: "Vencida",
};

const cycleLabels: Record<BillingCycle, string> = { MONTHLY: "Mensual", YEARLY: "Anual" };
const providerLabels: Record<PaymentProvider, string> = {
  MANUAL: "Administración manual",
  STRIPE: "Stripe",
  WOMPI: "Wompi",
  MERCADOPAGO: "Mercado Pago",
};

function formatDate(value: string | null, withTime = false) {
  if (!value) return "No definida";
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) return "Fecha no disponible";
  return withTime ? dateTimeFormatter.format(date) : dateFormatter.format(date);
}

function formatStorage(bytes: number) {
  if (!Number.isFinite(bytes) || bytes <= 0) return "0 B";
  const units = ["B", "KB", "MB", "GB", "TB"];
  const unit = Math.min(Math.floor(Math.log(bytes) / Math.log(1024)), units.length - 1);
  const value = bytes / (1024 ** unit);
  return `${new Intl.NumberFormat("es-CO", { maximumFractionDigits: value >= 10 ? 0 : 1 }).format(value)} ${units[unit]}`;
}

function SubscriptionStatusBadge({ status }: { status: SubscriptionStatus }) {
  const tone = status === "ACTIVE" ? "active" : status === "TRIALING" ? "trial" : status === "PAST_DUE" ? "warning" : "inactive";
  return <span className={`tm-subscription-status tm-subscription-status--${tone}`}><span aria-hidden="true" />{statusLabels[status]}</span>;
}

function useSubscriptions() {
  const [data, setData] = useState<BusinessSubscription[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<ApiClientError | null>(null);
  const sequence = useRef(0);

  const load = useCallback(async () => {
    const request = ++sequence.current;
    setLoading(true); setError(null);
    try {
      const subscriptions = await subscriptionApi.getBusinessSubscriptions();
      if (request === sequence.current) setData(subscriptions);
    } catch (requestError) {
      if (request === sequence.current) setError(normalizeApiError(requestError));
    } finally { if (request === sequence.current) setLoading(false); }
  }, []);

  useEffect(() => {
    void load();
    return () => { sequence.current += 1; };
  }, [load]);

  return { data, loading, error, reload: load };
}

function UsageFact({ icon, label, value }: { icon: ReactNode; label: string; value: string | number }) {
  return <div><span>{icon}</span><dt>{label}</dt><dd>{value}</dd></div>;
}

function SubscriptionDetail({ subscriptionId, canManage, onClose }: { subscriptionId: number; canManage: boolean; onClose: () => void }) {
  const [subscription, setSubscription] = useState<BusinessSubscription | null>(null);
  const [usage, setUsage] = useState<SubscriptionUsage | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [confirmRecalculation, setConfirmRecalculation] = useState(false);
  const [feedback, setFeedback] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const panelRef = useRef<HTMLElement>(null);
  const closeButtonRef = useRef<HTMLButtonElement>(null);

  const load = useCallback(async () => {
    setLoading(true); setError(null);
    try {
      const [nextSubscription, nextUsage] = await Promise.all([
        subscriptionApi.getBusinessSubscription(subscriptionId),
        subscriptionApi.getSubscriptionUsage(subscriptionId),
      ]);
      setSubscription(nextSubscription); setUsage(nextUsage);
    } catch (requestError) { setError(normalizeApiError(requestError).message); }
    finally { setLoading(false); }
  }, [subscriptionId]);

  useEffect(() => { void load(); }, [load]);

  useEffect(() => {
    const previousOverflow = document.body.style.overflow;
    document.body.style.overflow = "hidden";
    closeButtonRef.current?.focus();
    const onKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape" && !saving) { onClose(); return; }
      if (event.key !== "Tab") return;
      const focusable = Array.from(panelRef.current?.querySelectorAll<HTMLElement>("button:not([disabled])") ?? []);
      if (!focusable.length) return;
      const first = focusable[0]; const last = focusable[focusable.length - 1];
      if (event.shiftKey && document.activeElement === first) { event.preventDefault(); last.focus(); }
      else if (!event.shiftKey && document.activeElement === last) { event.preventDefault(); first.focus(); }
    };
    window.addEventListener("keydown", onKeyDown);
    return () => { document.body.style.overflow = previousOverflow; window.removeEventListener("keydown", onKeyDown); };
  }, [onClose, saving]);

  async function recalculate() {
    setSaving(true); setError(null); setFeedback(null);
    try {
      const nextUsage = await subscriptionApi.recalculateSubscriptionUsage(subscriptionId);
      setUsage(nextUsage);
      setFeedback("El uso fue recalculado con la información actual del negocio.");
      setConfirmRecalculation(false);
    } catch (requestError) { setError(normalizeApiError(requestError).message); }
    finally { setSaving(false); }
  }

  return (
    <div className="tm-review-backdrop">
      <section ref={panelRef} className="tm-review-panel tm-subscription-panel" role="dialog" aria-modal="true" aria-labelledby="subscription-detail-title">
        <header className="tm-review-panel__head">
          <div><span>Suscripción #{subscriptionId}</span><h2 id="subscription-detail-title">{subscription?.businessName ?? "Detalle de suscripción"}</h2><p>{subscription ? `${subscription.planName} · ${cycleLabels[subscription.billingCycle]}` : "Consultando plan y consumo"}</p></div>
          <button ref={closeButtonRef} className="tm-icon-button" type="button" aria-label="Cerrar detalle" disabled={saving} onClick={onClose}><X aria-hidden="true" /></button>
        </header>
        <div className="tm-review-panel__body">
          {loading ? <div className="tm-business-detail-loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando suscripción</div> : null}
          {error ? <FormAlert>{error}</FormAlert> : null}
          {feedback ? <FormAlert tone="success">{feedback}</FormAlert> : null}
          {!loading && subscription ? (
            <>
              <section className="tm-subscription-summary">
                <span><PackageCheck aria-hidden="true" /></span>
                <div><h3>{subscription.planName}</h3><p>Plan asignado a {subscription.businessName}</p></div>
                <SubscriptionStatusBadge status={subscription.status} />
              </section>
              {subscription.cancelAtPeriodEnd ? <div className="tm-subscription-notice"><CalendarClock aria-hidden="true" /><p><strong>Finalización programada</strong><span>La suscripción terminará al cerrar el periodo actual.</span></p></div> : null}
              <section className="tm-review-block">
                <h3>Vigencia y facturación</h3>
                <dl className="tm-review-facts">
                  <div><dt>Ciclo</dt><dd>{cycleLabels[subscription.billingCycle]}</dd></div>
                  <div><dt>Gestión del pago</dt><dd>{providerLabels[subscription.paymentProvider]}</dd></div>
                  <div><dt>Inicio del periodo</dt><dd>{formatDate(subscription.currentPeriodStart)}</dd></div>
                  <div><dt>Fin del periodo</dt><dd>{formatDate(subscription.currentPeriodEnd)}</dd></div>
                  {subscription.trialEndsAt ? <div className="tm-review-facts__wide"><dt>Fin del periodo de prueba</dt><dd>{formatDate(subscription.trialEndsAt)}</dd></div> : null}
                </dl>
              </section>
              <section className="tm-subscription-usage">
                <header><div><h3>Uso actual</h3><p>{usage ? `Actualizado ${formatDate(usage.updatedAt, true)}` : "Sin medición disponible"}</p></div>{canManage && usage ? <button className="tm-button tm-button--secondary" type="button" disabled={saving} onClick={() => setConfirmRecalculation(true)}><RefreshCw aria-hidden="true" /> Recalcular uso</button> : null}</header>
                {usage ? <dl><UsageFact icon={<UsersRound aria-hidden="true" />} label="Usuarios" value={usage.usersCount} /><UsageFact icon={<ReceiptText aria-hidden="true" />} label="Órdenes" value={usage.ordersCount} /><UsageFact icon={<MapPin aria-hidden="true" />} label="Sedes" value={usage.branchesCount} /><UsageFact icon={<HardDrive aria-hidden="true" />} label="Almacenamiento" value={formatStorage(usage.storageUsed)} /></dl> : <EmptyState title="Uso no disponible" message="No existe una medición para esta suscripción." />}
              </section>
              {confirmRecalculation ? <section className="tm-subscription-confirm"><AlertTriangle aria-hidden="true" /><div><h3>¿Recalcular el uso ahora?</h3><p>Se volverán a contar usuarios activos, órdenes y sedes del negocio. El almacenamiento conservará el valor registrado por el backend.</p><div><button className="tm-button tm-button--secondary" type="button" disabled={saving} onClick={() => setConfirmRecalculation(false)}>Cancelar</button><button className="tm-button tm-button--primary" type="button" disabled={saving} onClick={() => void recalculate()}>{saving ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <RefreshCw aria-hidden="true" />} Confirmar recálculo</button></div></div></section> : null}
            </>
          ) : null}
        </div>
      </section>
    </div>
  );
}

export function PlatformSubscriptions() {
  const { access } = useAuth();
  const { data, loading, error, reload } = useSubscriptions();
  const [query, setQuery] = useState("");
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const detailTriggerRef = useRef<HTMLButtonElement | null>(null);
  const canManage = hasPermission(access?.permissions ?? [], "SUBSCRIPTION_MANAGE");
  const normalizedQuery = query.trim().toLocaleLowerCase("es");
  const filtered = useMemo(() => data.filter((subscription) => !normalizedQuery || [subscription.businessName, subscription.planName, statusLabels[subscription.status], cycleLabels[subscription.billingCycle]].some((value) => value.toLocaleLowerCase("es").includes(normalizedQuery))), [data, normalizedQuery]);
  const activeCount = data.filter((subscription) => subscription.status === "ACTIVE").length;
  const attentionCount = data.filter((subscription) => subscription.status === "PAST_DUE" || subscription.status === "EXPIRED").length;

  const closeDetail = useCallback(() => {
    setSelectedId(null);
    requestAnimationFrame(() => detailTriggerRef.current?.focus());
  }, []);

  return (
    <PlatformAccess permission="SUBSCRIPTION_VIEW">
      <div className="tm-page-stack">
        <header className="tm-workspace-heading"><div><h1>Suscripciones</h1><p>Consulta el plan y el consumo registrado para cada negocio de la plataforma.</p></div><Link className="tm-button tm-button--secondary" href="/plataforma"><ArrowLeft aria-hidden="true" /> Volver al resumen</Link></header>
        <section className="tm-subscription-board">
          <header className="tm-subscription-toolbar">
            <label><Search aria-hidden="true" /><span className="sr-only">Buscar suscripciones</span><input value={query} onChange={(event) => setQuery(event.target.value)} placeholder="Buscar por negocio, plan o estado" /></label>
            <div><span><CheckCircle2 aria-hidden="true" /> {activeCount} activas</span><span className={attentionCount ? "has-attention" : ""}><AlertTriangle aria-hidden="true" /> {attentionCount} requieren atención</span><button className="tm-button tm-button--secondary" type="button" disabled={loading} onClick={() => void reload()}><RefreshCw className={loading ? "tm-spinner" : ""} aria-hidden="true" /> Actualizar</button></div>
          </header>
          {loading && !data.length ? <div className="tm-request-board__loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando suscripciones</div> : null}
          {error && !data.length ? error.status === 404 ? <section className="tm-subscription-disabled"><Database aria-hidden="true" /><div><h2>Suscripciones no disponibles</h2><p>La función está deshabilitada en el backend desplegado. Activa <strong>FEATURE_SUBSCRIPTIONS_ENABLED</strong> para habilitar esta consulta.</p></div></section> : <ErrorState message={error.message} onRetry={() => void reload()} /> : null}
          {error && data.length ? <FormAlert>{error.message}</FormAlert> : null}
          {!loading && !error && !filtered.length ? <EmptyState title={data.length ? "Sin coincidencias" : "No hay suscripciones registradas"} message={data.length ? "Prueba con otro negocio, plan o estado." : "Las suscripciones asignadas a negocios aparecerán aquí."} /> : null}
          {filtered.length ? <div className="tm-subscription-list">{filtered.map((subscription) => <article key={subscription.id}>
            <span className="tm-subscription-list__mark"><Building2 aria-hidden="true" /></span>
            <div className="tm-subscription-list__identity"><h3>{subscription.businessName}</h3><p>{subscription.planName}</p></div>
            <SubscriptionStatusBadge status={subscription.status} />
            <div className="tm-subscription-list__fact"><Layers3 aria-hidden="true" /><span><small>Facturación</small><strong>{cycleLabels[subscription.billingCycle]} · {providerLabels[subscription.paymentProvider]}</strong></span></div>
            <div className="tm-subscription-list__fact"><CalendarClock aria-hidden="true" /><span><small>Fin del periodo</small><strong>{formatDate(subscription.currentPeriodEnd)}</strong></span></div>
            <button className="tm-button tm-button--secondary" type="button" onClick={(event) => { detailTriggerRef.current = event.currentTarget; setSelectedId(subscription.id); }}>Ver uso <ArrowRight aria-hidden="true" /></button>
          </article>)}</div> : null}
        </section>
      </div>
      {selectedId !== null ? <SubscriptionDetail subscriptionId={selectedId} canManage={canManage} onClose={closeDetail} /> : null}
    </PlatformAccess>
  );
}
