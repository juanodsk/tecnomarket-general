"use client";

import {
  BadgeDollarSign,
  Building2,
  CalendarClock,
  CalendarDays,
  Check,
  ChevronDown,
  ClipboardCheck,
  ClipboardList,
  KeyRound,
  LayoutDashboard,
  LogOut,
  MapPin,
  Menu,
  ShieldCheck,
  ShieldEllipsis,
  SlidersHorizontal,
  Smartphone,
  UserRound,
  UsersRound,
  Wrench,
  X,
} from "lucide-react";
import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useEffect, useRef, useState, type ReactNode } from "react";

import { Wordmark } from "@/components/brand/wordmark";
import { ThemeToggle } from "@/components/ui/theme-toggle";
import { hasPermission, hasRole } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";

const accountLinks = [
  { href: "/app/perfil", label: "Perfil", icon: UserRound },
  { href: "/app/seguridad", label: "Seguridad", icon: KeyRound },
  { href: "/app/sesiones", label: "Sesiones", icon: ShieldCheck },
];

function WorkspaceSwitcher() {
  const { businesses, businessContext, activeBranchId, selectBranch, clearBusinessContext } = useAuth();
  const router = useRouter();
  const [open, setOpen] = useState(false);
  const rootRef = useRef<HTMLDivElement>(null);
  const triggerRef = useRef<HTMLButtonElement>(null);
  const activeBranch = businessContext?.branches.find((branch) => branch.id === activeBranchId);
  const singleBusiness = businesses.length === 1 ? businesses[0] : null;
  const displayedBusinessName = businessContext?.businessName ?? singleBusiness?.name ?? "Selecciona un negocio";
  const displayedBranchName = activeBranch?.name ?? (businessContext ? "Sin sede seleccionada" : `${businesses.length} negocios disponibles`);

  useEffect(() => {
    if (!open) return;

    const closeOnPointerDown = (event: PointerEvent) => {
      if (!rootRef.current?.contains(event.target as Node)) setOpen(false);
    };
    const closeOnEscape = (event: KeyboardEvent) => {
      if (event.key !== "Escape") return;
      setOpen(false);
      triggerRef.current?.focus();
    };

    document.addEventListener("pointerdown", closeOnPointerDown);
    window.addEventListener("keydown", closeOnEscape);
    return () => {
      document.removeEventListener("pointerdown", closeOnPointerDown);
      window.removeEventListener("keydown", closeOnEscape);
    };
  }, [open]);

  if (!businesses.length) {
    return (
      <div className="tm-workspace-static">
        <span><UserRound aria-hidden="true" /></span>
        <div><small>Área personal</small><strong>Sin negocio activo</strong></div>
      </div>
    );
  }

  const chooseBusiness = (businessId: number) => {
    if (businessContext?.businessId === businessId) return;
    clearBusinessContext();
    setOpen(false);
    router.push(`/app/negocios/${businessId}`);
  };

  const chooseBranch = (branchId: number) => {
    selectBranch(branchId);
    setOpen(false);
    triggerRef.current?.focus();
  };

  return (
    <div className="tm-workspace-switcher" ref={rootRef}>
      <button
        ref={triggerRef}
        className="tm-workspace-trigger"
        type="button"
        aria-expanded={open}
        aria-controls="tm-workspace-dropdown"
        onClick={() => setOpen((value) => !value)}
      >
        <span className="tm-workspace-trigger__icon"><Building2 aria-hidden="true" /></span>
        <span className="tm-workspace-trigger__copy">
          <small>Negocio</small>
          <strong>{displayedBusinessName}</strong>
          <span>{displayedBranchName}</span>
        </span>
        <ChevronDown className={open ? "is-open" : ""} aria-hidden="true" />
      </button>

      {open ? (
        <section id="tm-workspace-dropdown" className="tm-workspace-dropdown" aria-label="Seleccionar negocio y sede">
          {businessContext ? (
            <>
              <header className="tm-workspace-dropdown__head">
                <div><h2>{businessContext.businessName}</h2><p>Elige la sede donde vas a trabajar.</p></div>
                <span>{businessContext.branches.length} {businessContext.branches.length === 1 ? "sede" : "sedes"}</span>
              </header>
              {businessContext.branches.length ? (
                <div className="tm-branch-options" aria-label="Sedes disponibles">
                  {businessContext.branches.map((branch) => {
                    const selected = branch.id === activeBranchId;
                    return (
                      <button
                        key={branch.id}
                        className={`tm-branch-option ${selected ? "is-selected" : ""}`}
                        type="button"
                        aria-pressed={selected}
                        onClick={() => chooseBranch(branch.id)}
                      >
                        <span className="tm-branch-option__icon"><MapPin aria-hidden="true" /></span>
                        <span className="tm-branch-option__copy">
                          <strong>{branch.name}</strong>
                          <span>{branch.address}</span>
                          <small>{branch.cityName}{branch.primaryBranch ? " · Sede principal" : ` · Sede ${branch.branchNumber}`}</small>
                        </span>
                        {selected ? <Check className="tm-branch-option__check" aria-hidden="true" /> : null}
                      </button>
                    );
                  })}
                </div>
              ) : <p className="tm-workspace-dropdown__empty">Este negocio todavía no tiene sedes disponibles.</p>}
            </>
          ) : (
            <header className="tm-workspace-dropdown__head">
              <div><h2>Selecciona un negocio</h2><p>Después podrás elegir una de sus sedes.</p></div>
            </header>
          )}

          {businesses.some((business) => business.id !== businessContext?.businessId) ? (
            <div className="tm-business-options">
              <p>{businessContext ? "Cambiar de negocio" : "Negocios disponibles"}</p>
              <div>
                {businesses
                  .filter((business) => business.id !== businessContext?.businessId)
                  .map((business) => (
                    <button key={business.id} type="button" onClick={() => chooseBusiness(business.id)}>
                      <span><strong>{business.name}</strong><small>{business.cityName}</small></span>
                      <ChevronDown aria-hidden="true" />
                    </button>
                  ))}
              </div>
            </div>
          ) : null}
        </section>
      ) : null}
    </div>
  );
}

export function AppShell({ children }: { children: ReactNode }) {
  const { user, access, businessContext, signOut } = useAuth();
  const pathname = usePathname();
  const router = useRouter();
  const [mobileOpen, setMobileOpen] = useState(false);
  const [mobileMode, setMobileMode] = useState(false);
  const menuButtonRef = useRef<HTMLButtonElement>(null);
  const sidebarRef = useRef<HTMLElement>(null);
  const wasOpen = useRef(false);
  const isPlatformAdmin = hasRole(access?.roles ?? [], "PLATFORM_ADMIN");
  const isPlatformArea = pathname === "/plataforma" || pathname.startsWith("/plataforma/");
  const isAccountArea = accountLinks.some(({ href }) => pathname === href);
  const usesPlatformNavigation = isPlatformAdmin && (isPlatformArea || isAccountArea);
  const canViewOwnBusinessRequests = hasPermission(access?.permissions ?? [], "BUSINESS_REQUEST_VIEW_OWN");
  const canViewBranches = hasPermission(businessContext?.permissions ?? [], "BUSINESS_BRANCH_VIEW");
  const canViewTeam = hasPermission(businessContext?.permissions ?? [], "BUSINESS_USER_VIEW");
  const canViewServices = hasPermission(businessContext?.permissions ?? [], "BUSINESS_SERVICE_VIEW");
  const canViewSchedules = hasPermission(businessContext?.permissions ?? [], "BUSINESS_SCHEDULE_VIEW");
  const canViewQualityTests = hasPermission(businessContext?.permissions ?? [], "BUSINESS_QUALITY_TEST_VIEW");
  const canViewCustomerDevices = hasPermission(businessContext?.permissions ?? [], "CUSTOMER_DEVICE_VIEW");
  const canViewAppointments = hasPermission(businessContext?.permissions ?? [], "APPOINTMENT_VIEW");
  const canViewRepairOrders = hasPermission(businessContext?.permissions ?? [], "REPAIR_ORDER_VIEW");

  useEffect(() => {
    const media = window.matchMedia("(max-width: 900px)");
    const update = () => setMobileMode(media.matches);
    update();
    media.addEventListener("change", update);
    return () => media.removeEventListener("change", update);
  }, []);

  useEffect(() => {
    if (!mobileMode) {
      wasOpen.current = false;
      return;
    }
    if (mobileOpen) {
      wasOpen.current = true;
      document.body.style.overflow = "hidden";
      requestAnimationFrame(() => sidebarRef.current?.querySelector<HTMLElement>("a[href]")?.focus());
    } else {
      document.body.style.overflow = "";
      if (wasOpen.current) menuButtonRef.current?.focus();
      wasOpen.current = false;
    }
    return () => {
      document.body.style.overflow = "";
    };
  }, [mobileMode, mobileOpen]);

  useEffect(() => {
    if (!mobileOpen) return;
    const closeOnEscape = (event: KeyboardEvent) => {
      if (event.key === "Escape") {
        setMobileOpen(false);
        return;
      }
      if (event.key !== "Tab") return;
      const focusable = Array.from(
        sidebarRef.current?.querySelectorAll<HTMLElement>("a[href], button:not([disabled]), select:not([disabled])") ?? [],
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
  }, [mobileOpen]);

  async function handleLogout() {
    try {
      await signOut();
    } catch {
      // La sesión local siempre se limpia aunque el backend ya no tenga la cookie.
    } finally {
      router.replace("/login");
    }
  }

  return (
    <div className="tm-app-shell">
      <button
        ref={menuButtonRef}
        className="tm-mobile-menu"
        type="button"
        onClick={() => setMobileOpen((value) => !value)}
        aria-label={mobileOpen ? "Cerrar menú" : "Abrir menú"}
        aria-expanded={mobileOpen}
        aria-controls="tm-sidebar-navigation"
      >
        {mobileOpen ? <X aria-hidden="true" /> : <Menu aria-hidden="true" />}
      </button>
      <aside
        ref={sidebarRef}
        id="tm-sidebar-navigation"
        className={`tm-sidebar ${mobileOpen ? "is-open" : ""}`}
        aria-hidden={mobileMode && !mobileOpen ? true : undefined}
        inert={mobileMode && !mobileOpen ? true : undefined}
      >
        <div className="tm-sidebar__brand"><Wordmark href={usesPlatformNavigation ? "/plataforma" : "/app"} /></div>
        <nav aria-label="Navegación principal">
          {usesPlatformNavigation ? (
            <>
              <p className="tm-nav-label">General</p>
              <Link className={pathname === "/plataforma" ? "is-active" : ""} href="/plataforma" onClick={() => setMobileOpen(false)}>
                <LayoutDashboard aria-hidden="true" /> Resumen
              </Link>
              <p className="tm-nav-label">Gestión</p>
              <Link className={pathname.startsWith("/plataforma/solicitudes") ? "is-active" : ""} href="/plataforma/solicitudes" onClick={() => setMobileOpen(false)}>
                <ClipboardList aria-hidden="true" /> Solicitudes
              </Link>
              <Link className={pathname.startsWith("/plataforma/negocios") ? "is-active" : ""} href="/plataforma/negocios" onClick={() => setMobileOpen(false)}>
                <Building2 aria-hidden="true" /> Negocios
              </Link>
              <Link className={pathname.startsWith("/plataforma/suscripciones") ? "is-active" : ""} href="/plataforma/suscripciones" onClick={() => setMobileOpen(false)}>
                <BadgeDollarSign aria-hidden="true" /> Suscripciones
              </Link>
              <Link className={pathname.startsWith("/plataforma/usuarios") ? "is-active" : ""} href="/plataforma/usuarios" onClick={() => setMobileOpen(false)}>
                <UsersRound aria-hidden="true" /> Usuarios
              </Link>
              <p className="tm-nav-label">Configuración</p>
              <Link className={pathname.startsWith("/plataforma/accesos") ? "is-active" : ""} href="/plataforma/accesos" onClick={() => setMobileOpen(false)}>
                <ShieldEllipsis aria-hidden="true" /> Roles y permisos
              </Link>
              <Link className={pathname.startsWith("/plataforma/parametros") ? "is-active" : ""} href="/plataforma/parametros" onClick={() => setMobileOpen(false)}>
                <SlidersHorizontal aria-hidden="true" /> Parámetros globales
              </Link>
            </>
          ) : (
            <>
              <p className="tm-nav-label">General</p>
              <Link className={pathname === "/app" ? "is-active" : ""} href="/app" onClick={() => setMobileOpen(false)}>
                <LayoutDashboard aria-hidden="true" /> Resumen
              </Link>
              {canViewOwnBusinessRequests ? (
                <Link className={pathname.startsWith("/app/negocios/solicitud") ? "is-active" : ""} href="/app/negocios/solicitud" onClick={() => setMobileOpen(false)}>
                  <ClipboardList aria-hidden="true" /> Solicitudes de negocio
                </Link>
              ) : null}
              {businessContext ? (
                <Link className={pathname === `/app/negocios/${businessContext.businessId}` ? "is-active" : ""} href={`/app/negocios/${businessContext.businessId}`} onClick={() => setMobileOpen(false)}>
                  <Building2 aria-hidden="true" /> {businessContext.businessName}
                </Link>
              ) : null}
              {businessContext && canViewBranches ? (
                <Link className={pathname.startsWith(`/app/negocios/${businessContext.businessId}/sedes`) ? "is-active" : ""} href={`/app/negocios/${businessContext.businessId}/sedes`} onClick={() => setMobileOpen(false)}>
                  <MapPin aria-hidden="true" /> Sedes
                </Link>
              ) : null}
              {businessContext && canViewTeam ? (
                <Link className={pathname.startsWith(`/app/negocios/${businessContext.businessId}/equipo`) ? "is-active" : ""} href={`/app/negocios/${businessContext.businessId}/equipo`} onClick={() => setMobileOpen(false)}>
                  <UsersRound aria-hidden="true" /> Equipo
                </Link>
              ) : null}
              {businessContext && canViewServices ? (
                <Link className={pathname.startsWith(`/app/negocios/${businessContext.businessId}/servicios`) ? "is-active" : ""} href={`/app/negocios/${businessContext.businessId}/servicios`} onClick={() => setMobileOpen(false)}>
                  <Wrench aria-hidden="true" /> Servicios
                </Link>
              ) : null}
              {businessContext && canViewSchedules ? (
                <Link className={pathname.startsWith(`/app/negocios/${businessContext.businessId}/horarios`) ? "is-active" : ""} href={`/app/negocios/${businessContext.businessId}/horarios`} onClick={() => setMobileOpen(false)}>
                  <CalendarClock aria-hidden="true" /> Horarios
                </Link>
              ) : null}
              {businessContext && canViewQualityTests ? (
                <Link className={pathname.startsWith(`/app/negocios/${businessContext.businessId}/pruebas`) ? "is-active" : ""} href={`/app/negocios/${businessContext.businessId}/pruebas`} onClick={() => setMobileOpen(false)}>
                  <ClipboardCheck aria-hidden="true" /> Pruebas de recepción
                </Link>
              ) : null}
              {businessContext && canViewCustomerDevices ? (
                <Link className={pathname.startsWith(`/app/negocios/${businessContext.businessId}/clientes`) ? "is-active" : ""} href={`/app/negocios/${businessContext.businessId}/clientes`} onClick={() => setMobileOpen(false)}>
                  <Smartphone aria-hidden="true" /> Clientes y dispositivos
                </Link>
              ) : null}
              {businessContext && canViewAppointments ? (
                <Link className={pathname.startsWith(`/app/negocios/${businessContext.businessId}/citas`) ? "is-active" : ""} href={`/app/negocios/${businessContext.businessId}/citas`} onClick={() => setMobileOpen(false)}>
                  <CalendarDays aria-hidden="true" /> Citas
                </Link>
              ) : null}
              {businessContext && canViewRepairOrders ? (
                <Link className={pathname.startsWith(`/app/negocios/${businessContext.businessId}/ordenes`) ? "is-active" : ""} href={`/app/negocios/${businessContext.businessId}/ordenes`} onClick={() => setMobileOpen(false)}>
                  <ClipboardList aria-hidden="true" /> Órdenes de reparación
                </Link>
              ) : null}
              {isPlatformAdmin ? (
                <Link className="" href="/plataforma" onClick={() => setMobileOpen(false)}>
                  <ShieldCheck aria-hidden="true" /> Plataforma
                </Link>
              ) : null}
            </>
          )}
          <p className="tm-nav-label">Cuenta</p>
          {accountLinks.map(({ href, label, icon: Icon }) => (
            <Link key={href} className={pathname === href ? "is-active" : ""} href={href} onClick={() => setMobileOpen(false)}>
              <Icon aria-hidden="true" /> {label}
            </Link>
          ))}
        </nav>
        <div className="tm-sidebar__foot">
          <div className="tm-user-summary">
            <span>{user?.name?.slice(0, 1)}{user?.surname?.slice(0, 1)}</span>
            <div><strong>{user?.name} {user?.surname}</strong><small>{user?.email}</small></div>
          </div>
          <button type="button" onClick={handleLogout}><LogOut aria-hidden="true" /> Cerrar sesión</button>
        </div>
      </aside>
      {mobileOpen ? <button className="tm-sidebar-backdrop" type="button" aria-label="Cerrar menú" onClick={() => setMobileOpen(false)} /> : null}
      <div className="tm-app-main">
        <header className="tm-topbar">
          <div className="tm-topbar__workspace">
            {usesPlatformNavigation ? (
              <div className="tm-workspace-static">
                <span><ShieldCheck aria-hidden="true" /></span>
                <div><small>Área administrativa</small><strong>Administración de plataforma</strong></div>
              </div>
            ) : <WorkspaceSwitcher />}
          </div>
          <ThemeToggle />
        </header>
        <main className="tm-app-content">{children}</main>
      </div>
    </div>
  );
}
