"use client";

import { ArrowRight, Building2, CheckCircle2, MapPin, Plus } from "lucide-react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useState } from "react";

import { EmptyState, ErrorState, FullPageLoader } from "@/components/ui/feedback";
import { ApiClientError, normalizeApiError } from "@/lib/api/errors";
import { hasPermission, hasRole } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";

export function AppEntryResolver() {
  const { access, businesses } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (hasRole(access?.roles ?? [], "PLATFORM_ADMIN")) router.replace("/plataforma");
    else if (businesses.length === 1) router.replace(`/app/negocios/${businesses[0].id}`);
    else if (businesses.length > 1) router.replace("/app/negocios/seleccionar");
  }, [access?.roles, businesses, router]);

  if (!businesses.length && !hasRole(access?.roles ?? [], "PLATFORM_ADMIN")) {
    const canRequestBusiness = hasPermission(access?.permissions ?? [], "BUSINESS_REQUEST_CREATE");
    return (
      <div className="tm-page-stack">
        <EmptyState
          title="Aún no tienes un negocio asociado"
          message="Envía una solicitud para que el equipo de TecMarket revise la información de tu negocio."
          action={canRequestBusiness ? (
            <Link className="tm-button tm-button--primary" href="/app/negocios/solicitud">
              <Plus aria-hidden="true" /> Solicitar un negocio
            </Link>
          ) : undefined}
        />
      </div>
    );
  }

  return <FullPageLoader label="Abriendo tu espacio de trabajo" />;
}

export function BusinessSelection() {
  const { businesses } = useAuth();
  return (
    <div className="tm-page-stack">
      <header className="tm-page-heading"><div><h1>Selecciona un negocio</h1><p>Cada espacio conserva permisos y sedes independientes.</p></div></header>
      <div className="tm-business-list">
        {businesses.map((business) => (
          <Link key={business.id} href={`/app/negocios/${business.id}`}>
            <div><h2>{business.name}</h2><p><MapPin aria-hidden="true" />{business.cityName}</p></div>
            <ArrowRight aria-hidden="true" />
          </Link>
        ))}
      </div>
    </div>
  );
}

export function BusinessWorkspace({ businessId }: { businessId: number }) {
  const { businesses, businessContext, activeBranchId, selectBusiness } = useAuth();
  const router = useRouter();
  const [error, setError] = useState<ApiClientError | null>(null);
  const exists = businesses.some((business) => business.id === businessId);

  const handleContextError = useCallback((requestError: unknown) => {
    const normalized = normalizeApiError(requestError);
    if (normalized.status === 403) router.replace("/acceso-denegado");
    else setError(normalized);
  }, [router]);

  const retryContext = useCallback(() => {
    setError(null);
    void selectBusiness(businessId).catch(handleContextError);
  }, [businessId, handleContextError, selectBusiness]);

  useEffect(() => {
    if (!exists) return;
    let active = true;
    selectBusiness(businessId).catch((requestError) => {
      if (!active) return;
      handleContextError(requestError);
    });
    return () => {
      active = false;
    };
  }, [businessId, exists, handleContextError, selectBusiness]);

  if (!exists) return <div className="tm-page-stack"><ErrorState title="Negocio no disponible" message="Este negocio no está asociado a tu cuenta o ya no está disponible." /></div>;
  if (error) {
    const contextMissing = error.status === 404;
    return (
      <div className="tm-page-stack">
        <ErrorState
          title={contextMissing ? "Información del negocio no disponible" : undefined}
          message={contextMissing
            ? "No pudimos obtener los permisos y las sedes de este negocio. Verifica que el backend esté actualizado o inténtalo nuevamente."
            : error.message}
          onRetry={retryContext}
        />
      </div>
    );
  }
  if (!businessContext || businessContext.businessId !== businessId) return <FullPageLoader label="Cargando la información del negocio" />;

  const activeBranch = businessContext.branches.find((branch) => branch.id === activeBranchId);
  return (
    <div className="tm-page-stack">
      <header className="tm-workspace-heading">
        <div><h1>{businessContext.businessName}</h1><p>{activeBranch ? `${activeBranch.name} · ${activeBranch.cityName}` : "Sin sedes accesibles"}</p></div>
        <span className="tm-status-pill"><CheckCircle2 aria-hidden="true" /> Todo listo</span>
      </header>
      <section className="tm-diagnostic-board">
        <div className="tm-diagnostic-board__main">
          <Building2 aria-hidden="true" />
          <div><h2>Tu espacio de trabajo está listo.</h2><p>Desde aquí encontrarás los accesos directos y las herramientas disponibles para tu rol. Los módulos operativos se incorporarán progresivamente.</p></div>
        </div>
        <dl>
          <div><dt>Rol en el negocio</dt><dd>{businessContext.roleName || "Administrador de plataforma"}</dd></div>
          <div><dt>Sede actual</dt><dd>{activeBranch?.name ?? "Sin sede seleccionada"}</dd></div>
          <div><dt>Accesos disponibles</dt><dd>{businessContext.permissions.length}</dd></div>
          <div><dt>Sedes disponibles</dt><dd>{businessContext.branches.length}</dd></div>
        </dl>
      </section>
    </div>
  );
}
