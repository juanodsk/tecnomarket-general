"use client";

import {
  ArrowLeft,
  ArrowRight,
  Ban,
  Check,
  CheckCircle2,
  ChevronLeft,
  ChevronRight,
  KeyRound,
  LoaderCircle,
  Mail,
  MapPin,
  RefreshCw,
  ShieldCheck,
  ShieldOff,
  UserRound,
  UsersRound,
  X,
} from "lucide-react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";

import { PlatformAccess } from "@/components/platform/platform-workspace";
import { EmptyState, ErrorState, FormAlert } from "@/components/ui/feedback";
import * as platformUsersApi from "@/features/platform-users/api";
import { normalizeApiError } from "@/lib/api/errors";
import { hasPermission } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";
import type { PageResponse } from "@/types/api";
import type { PlatformUser, PlatformUserRole } from "@/types/platform-user";

const PAGE_SIZE = 20;
const dateFormatter = new Intl.DateTimeFormat("es-CO", { dateStyle: "medium" });

function formatDate(value: string) {
  const date = new Date(value);
  return Number.isNaN(date.getTime()) ? "Fecha no disponible" : dateFormatter.format(date);
}

function initials(user: PlatformUser) {
  return `${user.name.slice(0, 1)}${user.surname.slice(0, 1)}`.toUpperCase();
}

function usePlatformUsers(page: number) {
  const [data, setData] = useState<PageResponse<PlatformUser> | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const requestSequence = useRef(0);

  const load = useCallback(async () => {
    const sequence = ++requestSequence.current;
    setLoading(true);
    setError(null);
    try {
      const users = await platformUsersApi.getPlatformUsers(page, PAGE_SIZE);
      if (sequence === requestSequence.current) setData(users);
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

function AccountBadge({ verified }: { verified: boolean }) {
  return (
    <span className={`tm-user-verification tm-user-verification--${verified ? "verified" : "pending"}`}>
      {verified ? <CheckCircle2 aria-hidden="true" /> : <Mail aria-hidden="true" />}
      {verified ? "Correo verificado" : "Correo pendiente"}
    </span>
  );
}

type AdminAction = "sessions" | "deactivate" | null;

function UserDetailPanel({
  userId,
  canManage,
  currentUserId,
  onClose,
  onRemoved,
}: {
  userId: number;
  canManage: boolean;
  currentUserId: number | null;
  onClose: () => void;
  onRemoved: (message: string) => Promise<void>;
}) {
  const router = useRouter();
  const { endLocalSession } = useAuth();
  const [user, setUser] = useState<PlatformUser | null>(null);
  const [roles, setRoles] = useState<PlatformUserRole[]>([]);
  const [selectedRoleIds, setSelectedRoleIds] = useState<number[]>([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [feedback, setFeedback] = useState<string | null>(null);
  const [action, setAction] = useState<AdminAction>(null);
  const panelRef = useRef<HTMLElement>(null);
  const closeButtonRef = useRef<HTMLButtonElement>(null);
  const isCurrentUser = userId === currentUserId;

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const [nextUser, nextRoles] = await Promise.all([
        platformUsersApi.getPlatformUser(userId),
        platformUsersApi.getPlatformUserRoles(userId),
      ]);
      setUser(nextUser);
      setRoles(nextRoles);
      setSelectedRoleIds(nextRoles.filter((role) => role.assigned).map((role) => role.roleId));
    } catch (requestError) {
      setError(normalizeApiError(requestError).message);
    } finally {
      setLoading(false);
    }
  }, [userId]);

  useEffect(() => { void load(); }, [load]);

  useEffect(() => {
    const previousOverflow = document.body.style.overflow;
    document.body.style.overflow = "hidden";
    closeButtonRef.current?.focus();

    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape" && !saving) { onClose(); return; }
      if (event.key !== "Tab") return;
      const focusable = Array.from(panelRef.current?.querySelectorAll<HTMLElement>("button:not([disabled]), input:not([disabled]), a[href]") ?? []);
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

  const assignedRoleIds = useMemo(
    () => roles.filter((role) => role.assigned).map((role) => role.roleId).sort((a, b) => a - b),
    [roles],
  );
  const sortedSelectedRoleIds = useMemo(
    () => [...selectedRoleIds].sort((a, b) => a - b),
    [selectedRoleIds],
  );
  const rolesChanged = assignedRoleIds.join(",") !== sortedSelectedRoleIds.join(",");

  function toggleRole(roleId: number) {
    setSelectedRoleIds((current) => current.includes(roleId)
      ? current.filter((id) => id !== roleId)
      : [...current, roleId]);
    setFeedback(null);
    setError(null);
  }

  async function saveRoles() {
    setSaving(true);
    setError(null);
    setFeedback(null);
    try {
      const updatedRoles = await platformUsersApi.updatePlatformUserRoles(userId, selectedRoleIds);
      if (isCurrentUser) {
        endLocalSession();
        router.replace("/login?acceso=actualizado");
        return;
      }
      setRoles(updatedRoles);
      setSelectedRoleIds(updatedRoles.filter((role) => role.assigned).map((role) => role.roleId));
      setFeedback("Los roles globales fueron actualizados y las sesiones anteriores quedaron invalidadas.");
    } catch (requestError) {
      setError(normalizeApiError(requestError).message);
    } finally {
      setSaving(false);
    }
  }

  async function revokeSessions() {
    setSaving(true);
    setError(null);
    setFeedback(null);
    try {
      await platformUsersApi.revokePlatformUserSessions(userId);
      if (isCurrentUser) {
        endLocalSession();
        router.replace("/login?sesiones=revocadas");
        return;
      }
      setAction(null);
      setFeedback("Todas las sesiones del usuario fueron revocadas.");
    } catch (requestError) {
      setError(normalizeApiError(requestError).message);
    } finally {
      setSaving(false);
    }
  }

  async function deactivateUser() {
    setSaving(true);
    setError(null);
    setFeedback(null);
    try {
      await platformUsersApi.updatePlatformUserStatus(userId, false);
      if (isCurrentUser) {
        endLocalSession();
        router.replace("/login?cuenta=desactivada");
        return;
      }
      await onRemoved(`Se desactivó la cuenta de ${user?.name ?? "este usuario"}.`);
    } catch (requestError) {
      setError(normalizeApiError(requestError).message);
      setSaving(false);
    }
  }

  return (
    <div className="tm-review-backdrop">
      <section ref={panelRef} className="tm-review-panel tm-platform-user-panel" role="dialog" aria-modal="true" aria-labelledby="platform-user-title">
        <header className="tm-review-panel__head">
          <div><span>Cuenta global</span><h2 id="platform-user-title">{user ? `${user.name} ${user.surname}` : "Consultando usuario"}</h2><p>Identidad, acceso y seguridad de la cuenta</p></div>
          <button ref={closeButtonRef} className="tm-icon-button" type="button" aria-label="Cerrar detalle" disabled={saving} onClick={onClose}><X aria-hidden="true" /></button>
        </header>
        <div className="tm-review-panel__body">
          {loading ? <div className="tm-business-detail-loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando información</div> : null}
          {error && !user ? <ErrorState title="No fue posible consultar el usuario" message={error} onRetry={() => void load()} /> : null}
          {user ? (
            <>
              <section className="tm-platform-user-summary">
                <span className="tm-platform-user-avatar">{initials(user)}</span>
                <div><h3>{user.name} {user.surname}</h3><p>Miembro desde {formatDate(user.createdAt)}</p></div>
                <AccountBadge verified={user.emailVerified} />
              </section>
              {feedback ? <FormAlert tone="success">{feedback}</FormAlert> : null}
              {error ? <FormAlert>{error}</FormAlert> : null}
              <section className="tm-review-block">
                <h3>Identificación y ubicación</h3>
                <dl className="tm-review-facts">
                  <div><dt>Documento</dt><dd>{user.documentTypeName} {user.documentNumber}</dd></div>
                  <div><dt>País</dt><dd>{user.countryName}</dd></div>
                  <div><dt>Departamento</dt><dd>{user.departmentName}</dd></div>
                  <div><dt>Ciudad</dt><dd>{user.cityName}</dd></div>
                  <div className="tm-review-facts__wide"><dt>Dirección</dt><dd>{user.address || "No registrada"}</dd></div>
                </dl>
              </section>
              <section className="tm-review-block">
                <h3>Contacto</h3>
                <div className="tm-review-contact">
                  <p><Mail aria-hidden="true" /><span><small>Correo</small><strong>{user.email}</strong></span></p>
                  <p><UserRound aria-hidden="true" /><span><small>Teléfono</small><strong>{user.phone}</strong></span></p>
                  <p><MapPin aria-hidden="true" /><span><small>Ubicación</small><strong>{user.cityName}, {user.departmentName}</strong></span></p>
                </div>
              </section>
              <section className="tm-review-block tm-user-roles-block">
                <div className="tm-user-section-heading"><div><h3>Roles globales</h3><p>{canManage ? "Selecciona los accesos que debe conservar esta cuenta." : "Accesos globales asignados a esta cuenta."}</p></div>{!canManage ? <span>Solo lectura</span> : null}</div>
                {roles.length ? (
                  <fieldset className="tm-role-options" disabled={!canManage || saving}>
                    <legend className="sr-only">Roles globales disponibles</legend>
                    {roles.map((role) => {
                      const checked = selectedRoleIds.includes(role.roleId);
                      if (!canManage && !checked) return null;
                      return (
                        <label key={role.roleId} className={checked ? "is-selected" : ""}>
                          <input type="checkbox" checked={checked} onChange={() => toggleRole(role.roleId)} />
                          <span className="tm-role-options__check">{checked ? <Check aria-hidden="true" /> : null}</span>
                          <span><strong>{role.name}</strong><small>{role.description || "Rol global de la plataforma"}</small></span>
                          <em>Global</em>
                        </label>
                      );
                    })}
                  </fieldset>
                ) : <p className="tm-review-unavailable">No hay roles globales disponibles.</p>}
                {canManage ? <div className="tm-user-role-actions"><small>Cambiar roles invalida las sesiones actuales del usuario.</small><button className="tm-button tm-button--primary" type="button" disabled={!rolesChanged || saving} onClick={() => void saveRoles()}>{saving && !action ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <ShieldCheck aria-hidden="true" />} Guardar roles</button></div> : null}
              </section>
              {canManage ? (
                <section className="tm-review-block tm-user-security-block">
                  <div className="tm-user-section-heading"><div><h3>Seguridad de la cuenta</h3><p>Estas acciones invalidan inmediatamente las credenciales existentes.</p></div></div>
                  {!action ? <div className="tm-user-security-actions"><button className="tm-button tm-button--secondary" type="button" onClick={() => { setError(null); setFeedback(null); setAction("sessions"); }}><KeyRound aria-hidden="true" /> Revocar sesiones</button><button className="tm-button tm-button--danger" type="button" onClick={() => { setError(null); setFeedback(null); setAction("deactivate"); }}><Ban aria-hidden="true" /> Desactivar cuenta</button></div> : null}
                  {action === "sessions" ? <div className="tm-review-decision"><div><ShieldOff aria-hidden="true" /><span><h3>¿Revocar todas las sesiones?</h3><p>{isCurrentUser ? "Esta es tu cuenta. Tendrás que iniciar sesión nuevamente." : "El usuario tendrá que autenticarse nuevamente en todos sus dispositivos."}</p></span></div><div className="tm-review-decision__actions"><button className="tm-button tm-button--secondary" type="button" disabled={saving} onClick={() => setAction(null)}>Cancelar</button><button className="tm-button tm-button--danger" type="button" disabled={saving} onClick={() => void revokeSessions()}>{saving ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <KeyRound aria-hidden="true" />} Confirmar revocación</button></div></div> : null}
                  {action === "deactivate" ? <div className="tm-review-decision tm-review-decision--reject"><div><span><h3>¿Desactivar esta cuenta?</h3><p>{isCurrentUser ? "Tu cuenta quedará inactiva y perderás el acceso a la plataforma." : "El usuario perderá el acceso y todas sus sesiones quedarán invalidadas."}</p></span></div><div className="tm-review-decision__actions"><button className="tm-button tm-button--secondary" type="button" disabled={saving} onClick={() => setAction(null)}>Cancelar</button><button className="tm-button tm-button--danger" type="button" disabled={saving} onClick={() => void deactivateUser()}>{saving ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Ban aria-hidden="true" />} Desactivar cuenta</button></div></div> : null}
                </section>
              ) : null}
            </>
          ) : null}
        </div>
      </section>
    </div>
  );
}

export function PlatformUsers() {
  const { access, user } = useAuth();
  const [page, setPage] = useState(0);
  const [selectedUserId, setSelectedUserId] = useState<number | null>(null);
  const [feedback, setFeedback] = useState<string | null>(null);
  const detailTriggerRef = useRef<HTMLButtonElement | null>(null);
  const { data, loading, error, reload } = usePlatformUsers(page);
  const canManage = hasPermission(access?.permissions ?? [], "PARAMETER_MANAGE");
  const pageLabel = useMemo(() => !data?.totalPages ? "Página 1 de 1" : `Página ${data.page + 1} de ${data.totalPages}`, [data]);
  const closeDetail = useCallback(() => {
    setSelectedUserId(null);
    requestAnimationFrame(() => detailTriggerRef.current?.focus());
  }, []);

  async function handleRemoved(message: string) {
    setSelectedUserId(null);
    setFeedback(message);
    if (data?.items.length === 1 && page > 0) setPage((current) => current - 1);
    else await reload();
  }

  return (
    <PlatformAccess permission="PARAMETER_VIEW">
      <div className="tm-page-stack">
        <header className="tm-workspace-heading">
          <div><h1>Usuarios globales</h1><p>Consulta las cuentas registradas y administra sus accesos a la plataforma.</p></div>
          <Link className="tm-button tm-button--secondary" href="/plataforma"><ArrowLeft aria-hidden="true" /> Volver al resumen</Link>
        </header>
        {feedback ? <FormAlert tone="success">{feedback}</FormAlert> : null}
        {error && data ? <FormAlert>{error}</FormAlert> : null}
        <section className="tm-request-board tm-platform-user-directory">
          <header className="tm-request-board__head">
            <div><span className="tm-request-board__icon"><UsersRound aria-hidden="true" /></span><span><h2>Directorio de usuarios</h2><p>{data ? `${data.totalElements} ${data.totalElements === 1 ? "cuenta activa" : "cuentas activas"}` : "Consultando usuarios"}</p></span></div>
            <button className="tm-button tm-button--secondary" type="button" disabled={loading} onClick={() => void reload()}><RefreshCw className={loading ? "tm-spinner" : ""} aria-hidden="true" /> Actualizar</button>
          </header>
          {loading && !data ? <div className="tm-request-board__loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando usuarios</div> : null}
          {error && !data ? <ErrorState message={error} onRetry={() => void reload()} /> : null}
          {data && !data.items.length ? <EmptyState title="No hay usuarios activos" message="Las cuentas activas registradas en la plataforma aparecerán aquí." /> : null}
          {data?.items.length ? <div className="tm-platform-user-list">{data.items.map((platformUser) => (
            <article key={platformUser.id}>
              <span className="tm-platform-user-list__avatar">{initials(platformUser)}</span>
              <div className="tm-platform-user-list__identity"><h3>{platformUser.name} {platformUser.surname}</h3><p>{platformUser.email}</p></div>
              <div className="tm-request-list__meta"><MapPin aria-hidden="true" /><span><small>Ubicación</small><strong>{platformUser.cityName}</strong></span></div>
              <div className="tm-request-list__meta"><UserRound aria-hidden="true" /><span><small>Documento</small><strong>{platformUser.documentTypeCode} {platformUser.documentNumber}</strong></span></div>
              <AccountBadge verified={platformUser.emailVerified} />
              <time dateTime={platformUser.createdAt}>Desde {formatDate(platformUser.createdAt)}</time>
              <button className="tm-button tm-button--secondary" type="button" onClick={(event) => { detailTriggerRef.current = event.currentTarget; setFeedback(null); setSelectedUserId(platformUser.id); }}>Ver usuario <ArrowRight aria-hidden="true" /></button>
            </article>
          ))}</div> : null}
          {data && data.totalPages > 1 ? <footer className="tm-request-pagination"><button className="tm-icon-button" type="button" aria-label="Página anterior" disabled={!data.hasPrevious || loading} onClick={() => setPage((current) => Math.max(0, current - 1))}><ChevronLeft aria-hidden="true" /></button><span>{pageLabel}</span><button className="tm-icon-button" type="button" aria-label="Página siguiente" disabled={!data.hasNext || loading} onClick={() => setPage((current) => current + 1)}><ChevronRight aria-hidden="true" /></button></footer> : null}
        </section>
      </div>
      {selectedUserId !== null ? <UserDetailPanel userId={selectedUserId} canManage={canManage} currentUserId={user?.id ?? null} onClose={closeDetail} onRemoved={handleRemoved} /> : null}
    </PlatformAccess>
  );
}
