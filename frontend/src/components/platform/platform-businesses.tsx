"use client";

import {
  ArrowLeft,
  ArrowRight,
  Building2,
  ChevronLeft,
  ChevronRight,
  LoaderCircle,
  Mail,
  MapPin,
  Phone,
  RefreshCw,
  Star,
  X,
} from "lucide-react";
import Link from "next/link";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";

import { PlatformAccess } from "@/components/platform/platform-workspace";
import { EmptyState, ErrorState, FormAlert } from "@/components/ui/feedback";
import * as businessApi from "@/features/business/api";
import { normalizeApiError } from "@/lib/api/errors";
import type { PageResponse } from "@/types/api";
import type { Business } from "@/types/business";

const PAGE_SIZE = 20;
const ratingFormatter = new Intl.NumberFormat("es-CO", {
  minimumFractionDigits: 1,
  maximumFractionDigits: 1,
});

function useBusinesses(page: number) {
  const [data, setData] = useState<PageResponse<Business> | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const requestSequence = useRef(0);

  const load = useCallback(async () => {
    const sequence = ++requestSequence.current;
    setLoading(true);
    setError(null);
    try {
      const businesses = await businessApi.getBusinesses(page, PAGE_SIZE);
      if (sequence === requestSequence.current) setData(businesses);
    } catch (requestError) {
      if (sequence === requestSequence.current) setError(normalizeApiError(requestError).message);
    } finally {
      if (sequence === requestSequence.current) setLoading(false);
    }
  }, [page]);

  useEffect(() => {
    void load();
    return () => { requestSequence.current += 1; };
  }, [load]);

  return { data, loading, error, reload: load };
}

function BusinessStatus({ active }: { active: boolean }) {
  return (
    <span className={`tm-business-status tm-business-status--${active ? "active" : "inactive"}`}>
      <span aria-hidden="true" />
      {active ? "Activo" : "Inactivo"}
    </span>
  );
}

function BusinessDetailPanel({ businessId, onClose }: { businessId: number; onClose: () => void }) {
  const [business, setBusiness] = useState<Business | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const panelRef = useRef<HTMLElement>(null);
  const closeButtonRef = useRef<HTMLButtonElement>(null);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      setBusiness(await businessApi.getBusinessById(businessId));
    } catch (requestError) {
      setError(normalizeApiError(requestError).message);
    } finally {
      setLoading(false);
    }
  }, [businessId]);

  useEffect(() => { void load(); }, [load]);

  useEffect(() => {
    const previousOverflow = document.body.style.overflow;
    document.body.style.overflow = "hidden";
    closeButtonRef.current?.focus();

    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape") { onClose(); return; }
      if (event.key !== "Tab") return;
      const focusable = Array.from(panelRef.current?.querySelectorAll<HTMLElement>("button:not([disabled]), a[href]") ?? []);
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
  }, [onClose]);

  return (
    <div className="tm-review-backdrop">
      <section ref={panelRef} className="tm-review-panel tm-business-detail-panel" role="dialog" aria-modal="true" aria-labelledby="business-detail-title">
        <header className="tm-review-panel__head">
          <div>
            <span>Ficha del negocio</span>
            <h2 id="business-detail-title">{business?.name ?? "Consultando negocio"}</h2>
            <p>Información registrada en la plataforma</p>
          </div>
          <button ref={closeButtonRef} className="tm-icon-button" type="button" aria-label="Cerrar detalle" onClick={onClose}><X aria-hidden="true" /></button>
        </header>
        <div className="tm-review-panel__body">
          {loading ? <div className="tm-business-detail-loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando información</div> : null}
          {error ? <ErrorState title="No fue posible consultar el negocio" message={error} onRetry={() => void load()} /> : null}
          {business ? (
            <>
              <section className="tm-business-detail-summary">
                <span className="tm-business-detail-summary__mark"><Building2 aria-hidden="true" /></span>
                <div><h3>{business.name}</h3><p>{business.legalName || "Razón social no registrada"}</p></div>
                <BusinessStatus active={business.active} />
              </section>
              <section className="tm-review-block">
                <h3>Información legal y ubicación</h3>
                <dl className="tm-review-facts">
                  <div><dt>Documento</dt><dd>{business.documentTypeCode} {business.documentNumber}</dd></div>
                  <div><dt>Ciudad</dt><dd>{business.cityName}</dd></div>
                  <div className="tm-review-facts__wide"><dt>Dirección</dt><dd>{business.address || "No registrada"}</dd></div>
                </dl>
              </section>
              <section className="tm-review-block">
                <h3>Contacto</h3>
                <div className="tm-review-contact">
                  <p><Mail aria-hidden="true" /><span><small>Correo</small><strong>{business.email}</strong></span></p>
                  <p><Phone aria-hidden="true" /><span><small>Teléfono</small><strong>{business.phone}</strong></span></p>
                  <p><MapPin aria-hidden="true" /><span><small>Ciudad</small><strong>{business.cityName}</strong></span></p>
                </div>
              </section>
              <section className="tm-review-block">
                <h3>Descripción</h3>
                <p className="tm-business-description">{business.description || "Este negocio aún no tiene una descripción registrada."}</p>
              </section>
              <section className="tm-business-rating" aria-label="Calificación del negocio">
                <Star aria-hidden="true" />
                <div>
                  <small>Reputación</small>
                  {business.ratingCount > 0 ? <strong>{ratingFormatter.format(business.ratingAverage)} · {business.ratingCount} {business.ratingCount === 1 ? "reseña" : "reseñas"}</strong> : <strong>Sin reseñas</strong>}
                </div>
              </section>
            </>
          ) : null}
        </div>
      </section>
    </div>
  );
}

export function PlatformBusinesses() {
  const [page, setPage] = useState(0);
  const [selectedBusinessId, setSelectedBusinessId] = useState<number | null>(null);
  const detailTriggerRef = useRef<HTMLButtonElement | null>(null);
  const { data, loading, error, reload } = useBusinesses(page);
  const pageLabel = useMemo(() => !data?.totalPages ? "Página 1 de 1" : `Página ${data.page + 1} de ${data.totalPages}`, [data]);
  const closeDetail = useCallback(() => {
    setSelectedBusinessId(null);
    requestAnimationFrame(() => detailTriggerRef.current?.focus());
  }, []);

  return (
    <PlatformAccess permission="BUSINESS_VIEW">
      <div className="tm-page-stack">
        <header className="tm-workspace-heading">
          <div><h1>Negocios de la plataforma</h1><p>Consulta la información general y el estado de los negocios registrados.</p></div>
          <Link className="tm-button tm-button--secondary" href="/plataforma"><ArrowLeft aria-hidden="true" /> Volver al resumen</Link>
        </header>
        {error && data ? <FormAlert>{error}</FormAlert> : null}
        <section className="tm-request-board tm-business-directory">
          <header className="tm-request-board__head">
            <div><span className="tm-request-board__icon"><Building2 aria-hidden="true" /></span><span><h2>Directorio global</h2><p>{data ? `${data.totalElements} ${data.totalElements === 1 ? "negocio registrado" : "negocios registrados"}` : "Consultando negocios"}</p></span></div>
            <button className="tm-button tm-button--secondary" type="button" disabled={loading} onClick={() => void reload()}><RefreshCw className={loading ? "tm-spinner" : ""} aria-hidden="true" /> Actualizar</button>
          </header>
          {loading && !data ? <div className="tm-request-board__loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando negocios</div> : null}
          {error && !data ? <ErrorState message={error} onRetry={() => void reload()} /> : null}
          {data && !data.items.length ? <EmptyState title="No hay negocios registrados" message="Cuando existan negocios en la plataforma, aparecerán en este directorio." /> : null}
          {data?.items.length ? <div className="tm-business-directory-list">{data.items.map((business) => (
            <article key={business.id}>
              <span className="tm-business-directory-list__mark"><Building2 aria-hidden="true" /></span>
              <div className="tm-business-directory-list__identity"><h3>{business.name}</h3><p>{business.legalName || `${business.documentTypeCode} ${business.documentNumber}`}</p></div>
              <div className="tm-request-list__meta"><MapPin aria-hidden="true" /><span><small>Ubicación</small><strong>{business.cityName}</strong></span></div>
              <div className="tm-request-list__meta"><Mail aria-hidden="true" /><span><small>Contacto</small><strong>{business.email}</strong></span></div>
              <div className="tm-business-directory-list__rating"><Star aria-hidden="true" /><span>{business.ratingCount > 0 ? ratingFormatter.format(business.ratingAverage) : "—"}</span></div>
              <BusinessStatus active={business.active} />
              <button className="tm-button tm-button--secondary" type="button" onClick={(event) => { detailTriggerRef.current = event.currentTarget; setSelectedBusinessId(business.id); }}>Ver detalle <ArrowRight aria-hidden="true" /></button>
            </article>
          ))}</div> : null}
          {data && data.totalPages > 1 ? <footer className="tm-request-pagination"><button className="tm-icon-button" type="button" aria-label="Página anterior" disabled={!data.hasPrevious || loading} onClick={() => setPage((current) => Math.max(0, current - 1))}><ChevronLeft aria-hidden="true" /></button><span>{pageLabel}</span><button className="tm-icon-button" type="button" aria-label="Página siguiente" disabled={!data.hasNext || loading} onClick={() => setPage((current) => current + 1)}><ChevronRight aria-hidden="true" /></button></footer> : null}
        </section>
      </div>
      {selectedBusinessId !== null ? <BusinessDetailPanel businessId={selectedBusinessId} onClose={closeDetail} /> : null}
    </PlatformAccess>
  );
}
