"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import {
  ArrowLeft,
  ArrowRight,
  Ban,
  Check,
  ChevronDown,
  KeyRound,
  Layers3,
  LoaderCircle,
  LockKeyhole,
  Plus,
  RefreshCw,
  Save,
  ShieldCheck,
  ShieldEllipsis,
  X,
} from "lucide-react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";

import { PlatformAccess } from "@/components/platform/platform-workspace";
import { EmptyState, ErrorState, FormAlert } from "@/components/ui/feedback";
import * as accessApi from "@/features/platform-access-control/api";
import { normalizeApiError } from "@/lib/api/errors";
import { hasPermission } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";
import type {
  AccessScope,
  PlatformPermission,
  PlatformRole,
  RolePermissionOption,
} from "@/types/access-control";

type AccessView = "roles" | "permissions";
type RoleSelection = number | "new" | null;

const roleSchema = z.object({
  code: z.string().trim().min(1, "Escribe el código del rol.").max(50, "Máximo 50 caracteres."),
  name: z.string().trim().min(1, "Escribe el nombre del rol.").max(100, "Máximo 100 caracteres."),
  description: z.string().trim().min(1, "Escribe una descripción funcional.").max(255, "Máximo 255 caracteres."),
  scope: z.enum(["GLOBAL", "BUSINESS"]),
});

type RoleFormValues = z.infer<typeof roleSchema>;

function scopeLabel(scope: AccessScope) {
  return scope === "GLOBAL" ? "Global" : "Negocio";
}

function ScopeBadge({ scope }: { scope: AccessScope }) {
  return <span className={`tm-access-scope tm-access-scope--${scope.toLowerCase()}`}>{scopeLabel(scope)}</span>;
}

function StatusBadge({ active }: { active: boolean }) {
  return <span className={`tm-business-status tm-business-status--${active ? "active" : "inactive"}`}><span aria-hidden="true" />{active ? "Activo" : "Inactivo"}</span>;
}

function useAccessCatalogs() {
  const [roles, setRoles] = useState<PlatformRole[]>([]);
  const [permissions, setPermissions] = useState<PlatformPermission[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const requestSequence = useRef(0);

  const load = useCallback(async () => {
    const sequence = ++requestSequence.current;
    setLoading(true);
    setError(null);
    try {
      const [nextRoles, nextPermissions] = await Promise.all([accessApi.getRoles(), accessApi.getPermissions()]);
      if (sequence === requestSequence.current) {
        setRoles(nextRoles);
        setPermissions(nextPermissions);
      }
    } catch (requestError) {
      if (sequence === requestSequence.current) setError(normalizeApiError(requestError).message);
    } finally {
      if (sequence === requestSequence.current) setLoading(false);
    }
  }, []);

  useEffect(() => {
    void load();
    return () => { requestSequence.current += 1; };
  }, [load]);

  return { roles, permissions, loading, error, reload: load };
}

function groupPermissions<T extends { moduleId: number; moduleName: string }>(permissions: T[]) {
  const groups = new Map<number, { moduleName: string; permissions: T[] }>();
  permissions.forEach((permission) => {
    const group = groups.get(permission.moduleId) ?? { moduleName: permission.moduleName, permissions: [] };
    group.permissions.push(permission);
    groups.set(permission.moduleId, group);
  });
  return [...groups.entries()].map(([moduleId, group]) => ({ moduleId, ...group }));
}

function RoleEditorPanel({
  selection,
  canManage,
  currentRoleCodes,
  onClose,
  onChanged,
}: {
  selection: Exclude<RoleSelection, null>;
  canManage: boolean;
  currentRoleCodes: string[];
  onClose: () => void;
  onChanged: (message: string, close?: boolean) => Promise<void>;
}) {
  const router = useRouter();
  const { endLocalSession } = useAuth();
  const [role, setRole] = useState<PlatformRole | null>(null);
  const [rolePermissions, setRolePermissions] = useState<RolePermissionOption[]>([]);
  const [selectedPermissionIds, setSelectedPermissionIds] = useState<number[]>([]);
  const [loading, setLoading] = useState(selection !== "new");
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [feedback, setFeedback] = useState<string | null>(null);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const panelRef = useRef<HTMLElement>(null);
  const closeButtonRef = useRef<HTMLButtonElement>(null);
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<RoleFormValues>({
    resolver: zodResolver(roleSchema),
    defaultValues: { code: "", name: "", description: "", scope: "BUSINESS" },
  });

  const loadRole = useCallback(async (roleId: number) => {
    setLoading(true);
    setError(null);
    try {
      const nextRole = await accessApi.getRole(roleId);
      const nextPermissions = nextRole.active ? await accessApi.getRolePermissions(roleId) : [];
      setRole(nextRole);
      setRolePermissions(nextPermissions);
      setSelectedPermissionIds(nextPermissions.filter((permission) => permission.assigned).map((permission) => permission.permissionId));
      reset({ code: nextRole.code, name: nextRole.name, description: nextRole.description, scope: nextRole.scope });
    } catch (requestError) {
      setError(normalizeApiError(requestError).message);
    } finally {
      setLoading(false);
    }
  }, [reset]);

  useEffect(() => {
    if (selection !== "new") void loadRole(selection);
  }, [loadRole, selection]);

  useEffect(() => {
    const previousOverflow = document.body.style.overflow;
    document.body.style.overflow = "hidden";
    closeButtonRef.current?.focus();
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape" && !saving) { onClose(); return; }
      if (event.key !== "Tab") return;
      const focusable = Array.from(panelRef.current?.querySelectorAll<HTMLElement>("button:not([disabled]), input:not([disabled]), textarea:not([disabled]), select:not([disabled])") ?? []);
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

  const assignedPermissionIds = useMemo(
    () => rolePermissions.filter((permission) => permission.assigned).map((permission) => permission.permissionId).sort((a, b) => a - b),
    [rolePermissions],
  );
  const sortedSelectedPermissionIds = useMemo(
    () => [...selectedPermissionIds].sort((a, b) => a - b),
    [selectedPermissionIds],
  );
  const permissionsChanged = assignedPermissionIds.join(",") !== sortedSelectedPermissionIds.join(",");
  const permissionGroups = useMemo(() => groupPermissions(rolePermissions), [rolePermissions]);
  const affectsCurrentSession = role ? currentRoleCodes.includes(role.code) : false;

  const saveRole = handleSubmit(async (values) => {
    setSaving(true);
    setError(null);
    setFeedback(null);
    try {
      if (!role) {
        const created = await accessApi.createRole({ ...values, code: values.code.trim().toUpperCase() });
        setRole(created);
        reset({ code: created.code, name: created.name, description: created.description, scope: created.scope });
        const permissions = await accessApi.getRolePermissions(created.id);
        setRolePermissions(permissions);
        setSelectedPermissionIds([]);
        setFeedback("El rol fue creado. Ahora puedes definir sus permisos.");
        await onChanged(`Se creó el rol ${created.name}.`);
      } else {
        const updated = await accessApi.updateRole(role.id, {
          name: values.name,
          description: values.description,
          scope: role.scope,
          active: role.active,
        });
        setRole(updated);
        reset({ code: updated.code, name: updated.name, description: updated.description, scope: updated.scope });
        setFeedback("La información del rol fue actualizada.");
        await onChanged(`Se actualizó el rol ${updated.name}.`);
      }
    } catch (requestError) {
      setError(normalizeApiError(requestError).message);
    } finally {
      setSaving(false);
    }
  });

  function togglePermission(permissionId: number) {
    setSelectedPermissionIds((current) => current.includes(permissionId)
      ? current.filter((id) => id !== permissionId)
      : [...current, permissionId]);
    setError(null);
    setFeedback(null);
  }

  async function savePermissions() {
    if (!role) return;
    setSaving(true);
    setError(null);
    setFeedback(null);
    try {
      const updated = await accessApi.updateRolePermissions(role.id, selectedPermissionIds);
      if (affectsCurrentSession) {
        endLocalSession();
        router.replace("/login?acceso=actualizado");
        return;
      }
      setRolePermissions(updated);
      setSelectedPermissionIds(updated.filter((permission) => permission.assigned).map((permission) => permission.permissionId));
      setFeedback("Los permisos del rol fueron actualizados y las sesiones afectadas quedaron invalidadas.");
    } catch (requestError) {
      setError(normalizeApiError(requestError).message);
    } finally {
      setSaving(false);
    }
  }

  async function reactivateRole() {
    if (!role) return;
    setSaving(true);
    setError(null);
    try {
      const updated = await accessApi.updateRole(role.id, {
        name: role.name,
        description: role.description,
        scope: role.scope,
        active: true,
      });
      setRole(updated);
      const permissions = await accessApi.getRolePermissions(updated.id);
      setRolePermissions(permissions);
      setSelectedPermissionIds(permissions.filter((permission) => permission.assigned).map((permission) => permission.permissionId));
      setFeedback("El rol volvió a estar activo.");
      await onChanged(`Se reactivó el rol ${updated.name}.`);
    } catch (requestError) {
      setError(normalizeApiError(requestError).message);
    } finally {
      setSaving(false);
    }
  }

  async function deleteRole() {
    if (!role) return;
    setSaving(true);
    setError(null);
    try {
      await accessApi.deleteRole(role.id);
      if (affectsCurrentSession) {
        endLocalSession();
        router.replace("/login?acceso=actualizado");
        return;
      }
      await onChanged(`Se desactivó el rol ${role.name}.`, true);
    } catch (requestError) {
      setError(normalizeApiError(requestError).message);
      setSaving(false);
    }
  }

  return (
    <div className="tm-review-backdrop">
      <section ref={panelRef} className="tm-review-panel tm-access-role-panel" role="dialog" aria-modal="true" aria-labelledby="role-editor-title">
        <header className="tm-review-panel__head">
          <div><span>{role ? "Configuración del rol" : "Nuevo rol"}</span><h2 id="role-editor-title">{role?.name ?? "Crear rol"}</h2><p>{role ? "Información y permisos efectivos" : "Define el alcance antes de asignar permisos"}</p></div>
          <button ref={closeButtonRef} className="tm-icon-button" type="button" aria-label="Cerrar configuración" disabled={saving} onClick={onClose}><X aria-hidden="true" /></button>
        </header>
        <div className="tm-review-panel__body">
          {loading ? <div className="tm-business-detail-loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando rol</div> : null}
          {error && !role && selection !== "new" ? <ErrorState title="No fue posible consultar el rol" message={error} onRetry={() => void loadRole(selection)} /> : null}
          {!loading && (role || selection === "new") ? (
            <>
              {feedback ? <FormAlert tone="success">{feedback}</FormAlert> : null}
              {error ? <FormAlert>{error}</FormAlert> : null}
              {role ? <section className="tm-access-role-summary"><span><ShieldCheck aria-hidden="true" /></span><div><h3>{role.name}</h3><p>{role.description}</p></div><StatusBadge active={role.active} /></section> : null}
              <form className="tm-access-role-form" onSubmit={saveRole} noValidate>
                <div className="tm-access-form-grid">
                  <label className="tm-access-field"><span>Código interno</span><input {...register("code")} readOnly={Boolean(role) || !canManage} maxLength={50} autoComplete="off" onInput={(event) => { if (!role) event.currentTarget.value = event.currentTarget.value.toUpperCase(); }} />{errors.code ? <small role="alert">{errors.code.message}</small> : <small>{role ? "El código no cambia después de crear el rol." : "El backend lo almacenará en mayúsculas."}</small>}</label>
                  <label className="tm-access-field"><span>Alcance</span>{role ? <><input type="hidden" {...register("scope")} /><div className="tm-access-readonly"><ScopeBadge scope={role.scope} /></div></> : <div className="tm-access-select"><select {...register("scope")} disabled={!canManage}><option value="BUSINESS">Negocio</option><option value="GLOBAL">Global</option></select><ChevronDown aria-hidden="true" /></div>}</label>
                  <label className="tm-access-field tm-access-field--wide"><span>Nombre visible</span><input {...register("name")} readOnly={!canManage} maxLength={100} autoComplete="off" />{errors.name ? <small role="alert">{errors.name.message}</small> : null}</label>
                  <label className="tm-access-field tm-access-field--wide"><span>Descripción funcional</span><textarea {...register("description")} readOnly={!canManage} maxLength={255} rows={3} />{errors.description ? <small role="alert">{errors.description.message}</small> : null}</label>
                </div>
                {canManage ? <div className="tm-access-form-actions"><button className="tm-button tm-button--primary" type="submit" disabled={saving}>{saving ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Save aria-hidden="true" />} {role ? "Guardar información" : "Crear rol"}</button></div> : null}
              </form>
              {role?.active ? (
                <section className="tm-access-permission-editor">
                  <header><div><h3>Permisos del rol</h3><p>{canManage ? "Selecciona únicamente los accesos que necesita este rol." : "Permisos actualmente asignados."}</p></div><span>{selectedPermissionIds.length} asignados</span></header>
                  {permissionGroups.length ? <div className="tm-access-permission-groups">{permissionGroups.map((group) => (
                    <section key={group.moduleId}>
                      <header><div><Layers3 aria-hidden="true" /><h4>{group.moduleName}</h4></div><span>{group.permissions.filter((permission) => selectedPermissionIds.includes(permission.permissionId)).length}/{group.permissions.length}</span></header>
                      <div>{group.permissions.map((permission) => {
                        const checked = selectedPermissionIds.includes(permission.permissionId);
                        if (!canManage && !checked) return null;
                        return <label key={permission.permissionId} className={checked ? "is-selected" : ""}><input type="checkbox" checked={checked} disabled={!canManage || saving} onChange={() => togglePermission(permission.permissionId)} /><span className="tm-role-options__check">{checked ? <Check aria-hidden="true" /> : null}</span><span><strong>{permission.name}</strong><small>{permission.description}</small></span><ScopeBadge scope={permission.scope} /></label>;
                      })}</div>
                    </section>
                  ))}</div> : <EmptyState title="Sin permisos disponibles" message="No hay permisos compatibles con el alcance de este rol." />}
                  {canManage ? <footer><p>Cambiar permisos invalida las sesiones de los usuarios que tienen este rol.</p><button className="tm-button tm-button--primary" type="button" disabled={!permissionsChanged || saving} onClick={() => void savePermissions()}>{saving ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <KeyRound aria-hidden="true" />} Guardar permisos</button></footer> : null}
                </section>
              ) : role ? <section className="tm-access-inactive"><LockKeyhole aria-hidden="true" /><div><h3>Rol inactivo</h3><p>Reactívalo para consultar o modificar sus permisos.</p></div>{canManage ? <button className="tm-button tm-button--secondary" type="button" disabled={saving} onClick={() => void reactivateRole()}>Reactivar rol</button> : null}</section> : null}
              {role && canManage ? <section className="tm-access-danger-zone"><div><h3>Desactivar rol</h3><p>El rol dejará de estar disponible y se invalidarán las sesiones asociadas.</p></div>{!confirmDelete ? <button className="tm-button tm-button--danger" type="button" onClick={() => setConfirmDelete(true)}><Ban aria-hidden="true" /> Desactivar rol</button> : <div className="tm-access-confirm"><p>¿Confirmas la desactivación de <strong>{role.name}</strong>?</p><div><button className="tm-button tm-button--secondary" type="button" disabled={saving} onClick={() => setConfirmDelete(false)}>Cancelar</button><button className="tm-button tm-button--danger" type="button" disabled={saving} onClick={() => void deleteRole()}>{saving ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Ban aria-hidden="true" />} Confirmar</button></div></div>}</section> : null}
            </>
          ) : null}
        </div>
      </section>
    </div>
  );
}

function RolesView({ roles, loading, canManage, onOpen }: { roles: PlatformRole[]; loading: boolean; canManage: boolean; onOpen: (selection: Exclude<RoleSelection, null>, trigger: HTMLButtonElement) => void }) {
  if (loading) return <div className="tm-request-board__loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando roles</div>;
  if (!roles.length) return <EmptyState title="No hay roles registrados" message="Los roles del sistema aparecerán en esta sección." />;
  return <div className="tm-access-role-list">{roles.map((role) => <article key={role.id}><span className="tm-access-role-list__icon"><ShieldEllipsis aria-hidden="true" /></span><div><h3>{role.name}</h3><p>{role.description}</p></div><ScopeBadge scope={role.scope} /><StatusBadge active={role.active} /><button className="tm-button tm-button--secondary" type="button" onClick={(event) => onOpen(role.id, event.currentTarget)}>{canManage ? "Configurar" : "Consultar"} <ArrowRight aria-hidden="true" /></button></article>)}</div>;
}

function PermissionsView({ permissions, loading }: { permissions: PlatformPermission[]; loading: boolean }) {
  const groups = useMemo(() => groupPermissions(permissions), [permissions]);
  if (loading) return <div className="tm-request-board__loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando permisos</div>;
  if (!groups.length) return <EmptyState title="No hay permisos registrados" message="El catálogo de permisos aparecerá en esta sección." />;
  return <div className="tm-permission-catalog">{groups.map((group) => <section key={group.moduleId}><header><span><Layers3 aria-hidden="true" /></span><div><h3>{group.moduleName}</h3><p>{group.permissions.length} {group.permissions.length === 1 ? "permiso" : "permisos"}</p></div></header><div>{group.permissions.map((permission) => <article key={permission.id}><span><LockKeyhole aria-hidden="true" /></span><div><h4>{permission.name}</h4><p>{permission.description}</p></div><StatusBadge active={permission.active} /></article>)}</div></section>)}</div>;
}

export function PlatformAccessControl() {
  const { access } = useAuth();
  const [view, setView] = useState<AccessView>("roles");
  const [selection, setSelection] = useState<RoleSelection>(null);
  const [feedback, setFeedback] = useState<string | null>(null);
  const detailTriggerRef = useRef<HTMLButtonElement | null>(null);
  const { roles, permissions, loading, error, reload } = useAccessCatalogs();
  const canManage = hasPermission(access?.permissions ?? [], "PARAMETER_MANAGE");

  const closePanel = useCallback(() => {
    setSelection(null);
    requestAnimationFrame(() => detailTriggerRef.current?.focus());
  }, []);

  function openPanel(nextSelection: Exclude<RoleSelection, null>, trigger: HTMLButtonElement) {
    detailTriggerRef.current = trigger;
    setFeedback(null);
    setSelection(nextSelection);
  }

  async function handleChanged(message: string, close = false) {
    setFeedback(message);
    if (close) setSelection(null);
    await reload();
  }

  return (
    <PlatformAccess permission="PARAMETER_VIEW">
      <div className="tm-page-stack">
        <header className="tm-workspace-heading"><div><h1>Roles y permisos</h1><p>Define responsabilidades y revisa los accesos disponibles en toda la plataforma.</p></div><Link className="tm-button tm-button--secondary" href="/plataforma"><ArrowLeft aria-hidden="true" /> Volver al resumen</Link></header>
        {feedback ? <FormAlert tone="success">{feedback}</FormAlert> : null}
        {error && (roles.length || permissions.length) ? <FormAlert>{error}</FormAlert> : null}
        <section className="tm-access-workspace">
          <header className="tm-access-workspace__head"><div className="tm-access-tabs" role="tablist" aria-label="Administración de accesos"><button type="button" role="tab" aria-selected={view === "roles"} className={view === "roles" ? "is-active" : ""} onClick={() => setView("roles")}><ShieldEllipsis aria-hidden="true" /> Roles <span>{roles.length}</span></button><button type="button" role="tab" aria-selected={view === "permissions"} className={view === "permissions" ? "is-active" : ""} onClick={() => setView("permissions")}><LockKeyhole aria-hidden="true" /> Permisos <span>{permissions.length}</span></button></div><div className="tm-access-head-actions"><button className="tm-button tm-button--secondary" type="button" disabled={loading} onClick={() => void reload()}><RefreshCw className={loading ? "tm-spinner" : ""} aria-hidden="true" /> Actualizar</button>{view === "roles" && canManage ? <button className="tm-button tm-button--primary" type="button" onClick={(event) => openPanel("new", event.currentTarget)}><Plus aria-hidden="true" /> Nuevo rol</button> : null}</div></header>
          {error && !roles.length && !permissions.length ? <ErrorState message={error} onRetry={() => void reload()} /> : null}
          {!error || roles.length || permissions.length ? <div role="tabpanel">{view === "roles" ? <RolesView roles={roles} loading={loading} canManage={canManage} onOpen={openPanel} /> : <PermissionsView permissions={permissions} loading={loading} />}</div> : null}
          {view === "permissions" ? <footer className="tm-access-catalog-note"><LockKeyhole aria-hidden="true" /><p><strong>Catálogo de consulta</strong><span>La asignación se realiza desde cada rol para conservar la relación entre alcance y permisos.</span></p></footer> : null}
        </section>
      </div>
      {selection !== null ? <RoleEditorPanel selection={selection} canManage={canManage} currentRoleCodes={access?.roles ?? []} onClose={closePanel} onChanged={handleChanged} /> : null}
    </PlatformAccess>
  );
}
