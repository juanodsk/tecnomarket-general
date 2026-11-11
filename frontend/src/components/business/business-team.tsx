"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import {
  Check,
  ChevronLeft,
  ChevronRight,
  CircleAlert,
  LoaderCircle,
  Mail,
  MapPin,
  Pencil,
  Percent,
  RefreshCw,
  Search,
  ShieldCheck,
  Trash2,
  UserCheck,
  UserPlus,
  UsersRound,
  X,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { useForm } from "react-hook-form";

import {
  EmptyState,
  ErrorState,
  FormAlert,
  FullPageLoader,
} from "@/components/ui/feedback";
import { SelectField, TextField } from "@/components/ui/form-controls";
import * as businessApi from "@/features/business/api";
import {
  businessUserAssignmentSchema,
  businessUserEmailSchema,
  type BusinessUserAssignmentValues,
} from "@/features/business/business-user-schemas";
import { ApiClientError, normalizeApiError } from "@/lib/api/errors";
import { hasPermission } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";
import type { PageResponse } from "@/types/api";
import type {
  BusinessContextBranch,
  BusinessRoleOption,
  BusinessUser,
  BusinessUserCandidate,
} from "@/types/business";

const PAGE_SIZE = 20;

const emptyAssignment: BusinessUserAssignmentValues = {
  roleId: "",
  branchIds: [],
  commissionPercentage: "",
};

function assignmentValues(
  member: BusinessUser | null,
): BusinessUserAssignmentValues {
  if (!member) return emptyAssignment;
  return {
    roleId: String(member.roleId),
    branchIds: member.branchIds.map(String),
    commissionPercentage:
      member.roleCode === "TECHNICIAN"
        ? String(member.commissionPercentage ?? 0)
        : "",
  };
}

function initials(name: string, surname = "") {
  return (
    `${name.trim().charAt(0)}${surname.trim().charAt(0)}`.toUpperCase() || "U"
  );
}

function fullNameInitials(fullName: string) {
  const parts = fullName.trim().split(/\s+/).filter(Boolean);
  return (
    `${parts[0]?.charAt(0) ?? ""}${parts.length > 1 ? (parts.at(-1)?.charAt(0) ?? "") : ""}`.toUpperCase() ||
    "U"
  );
}

function normalizedPercentage(value: string) {
  return value.trim() ? Number(value.replace(",", ".")) : null;
}

function formatPercentage(value: number | null) {
  return new Intl.NumberFormat("es-CO", {
    minimumFractionDigits: 0,
    maximumFractionDigits: 2,
  }).format(value ?? 0);
}

function TeamMemberRow({
  member,
  branches,
  canUpdate,
  onEdit,
}: {
  member: BusinessUser;
  branches: BusinessContextBranch[];
  canUpdate: boolean;
  onEdit: (trigger: HTMLButtonElement) => void;
}) {
  const assignedBranches = branches.filter((branch) =>
    member.branchIds.includes(branch.id),
  );
  const hiddenBranchCount = Math.max(
    0,
    member.branchIds.length - assignedBranches.length,
  );

  return (
    <article>
      <span className="tm-team-row__avatar" aria-hidden="true">
        {initials(member.userName, member.userSurname)}
      </span>
      <div className="tm-team-row__identity">
        <div>
          <h3>
            {member.userName} {member.userSurname}
          </h3>
          <span>
            <UserCheck aria-hidden="true" /> Activo
          </span>
        </div>
        <p>
          <Mail aria-hidden="true" /> {member.userEmail}
        </p>
      </div>
      <div className="tm-team-row__fact">
        <small>Rol</small>
        <strong>
          <ShieldCheck aria-hidden="true" /> {member.roleName}
        </strong>
        <span>
          {member.roleCode === "TECHNICIAN"
            ? `Comisión ${formatPercentage(member.commissionPercentage)} %`
            : ""}
        </span>
      </div>
      <div className="tm-team-row__fact">
        <small>Sedes asignadas</small>
        <strong>
          <MapPin aria-hidden="true" /> {member.branchIds.length}{" "}
          {member.branchIds.length === 1 ? "sede" : "sedes"}
        </strong>
        <span>
          {assignedBranches.map((branch) => branch.name).join(" · ") ||
            "Sin sedes visibles"}
          {hiddenBranchCount
            ? ` · ${hiddenBranchCount} no disponible${hiddenBranchCount === 1 ? "" : "s"}`
            : ""}
        </span>
      </div>
      {canUpdate ? (
        <button
          className="tm-button tm-button--secondary"
          type="button"
          onClick={(event) => onEdit(event.currentTarget)}
        >
          <Pencil aria-hidden="true" /> Editar
        </button>
      ) : null}
    </article>
  );
}

function TeamMemberEditor({
  businessId,
  member,
  roles,
  branches,
  onClose,
  onSaved,
}: {
  businessId: number;
  member: BusinessUser | null;
  roles: BusinessRoleOption[];
  branches: BusinessContextBranch[];
  onClose: () => void;
  onSaved: (message: string) => Promise<void>;
}) {
  const panelRef = useRef<HTMLElement>(null);
  const closeRef = useRef<HTMLButtonElement>(null);
  const keepMemberRef = useRef<HTMLButtonElement>(null);
  const [searchEmail, setSearchEmail] = useState("");
  const [candidate, setCandidate] = useState<BusinessUserCandidate | null>(
    null,
  );
  const [searching, setSearching] = useState(false);
  const [searchError, setSearchError] = useState<string | null>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const isEditing = Boolean(member);
  const {
    register,
    handleSubmit,
    watch,
    setValue,
    formState: { errors, isSubmitting },
  } = useForm<BusinessUserAssignmentValues>({
    resolver: zodResolver(businessUserAssignmentSchema),
    defaultValues: assignmentValues(member),
  });
  const roleId = watch("roleId");
  const selectedBranchIds = watch("branchIds");
  const selectedRole = roles.find((role) => role.roleId === Number(roleId));
  const isTechnician = selectedRole?.roleCode === "TECHNICIAN";
  const identity = member
    ? {
        fullName: `${member.userName} ${member.userSurname}`.trim(),
        email: member.userEmail,
      }
    : candidate;

  useEffect(() => {
    closeRef.current?.focus();
    const closeOnEscape = (event: KeyboardEvent) => {
      if (event.key === "Escape" && !isSubmitting && !deleting && !searching)
        onClose();
      if (event.key !== "Tab") return;
      const focusable = Array.from(
        panelRef.current?.querySelectorAll<HTMLElement>(
          "button:not([disabled]), input:not([disabled]), select:not([disabled])",
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
    window.addEventListener("keydown", closeOnEscape);
    return () => window.removeEventListener("keydown", closeOnEscape);
  }, [deleting, isSubmitting, onClose, searching]);

  useEffect(() => {
    if (confirmDelete) keepMemberRef.current?.focus();
  }, [confirmDelete]);

  useEffect(() => {
    if (!isTechnician) setValue("commissionPercentage", "");
  }, [isTechnician, setValue]);

  async function searchCandidate(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setSearchError(null);
    setSubmitError(null);
    setCandidate(null);
    const parsed = businessUserEmailSchema.safeParse(searchEmail);
    if (!parsed.success) {
      setSearchError(
        parsed.error.issues[0]?.message ?? "Ingresa un correo válido.",
      );
      return;
    }

    setSearching(true);
    try {
      const candidates = await businessApi.findAvailableBusinessUsers(
        businessId,
        parsed.data,
      );
      if (!candidates.length) {
        setSearchError(
          "No encontramos un usuario activo con ese correo. Debe registrarse primero en TecMarket.",
        );
        return;
      }
      setCandidate(candidates[0]);
    } catch (error) {
      setSearchError(normalizeApiError(error).message);
    } finally {
      setSearching(false);
    }
  }

  async function submit(values: BusinessUserAssignmentValues) {
    const userId = member?.userId ?? candidate?.userId;
    if (!userId || candidate?.assigned) return;
    setSubmitError(null);
    const request = {
      roleId: Number(values.roleId),
      branchIds: values.branchIds.map(Number),
      commissionPercentage: isTechnician
        ? normalizedPercentage(values.commissionPercentage)
        : null,
    };

    try {
      if (member) {
        await businessApi.updateBusinessUser(businessId, member.id, {
          ...request,
          active: true,
        });
        await onSaved("La asignación del integrante quedó actualizada.");
      } else {
        await businessApi.createBusinessUser(businessId, {
          ...request,
          userId,
        });
        await onSaved("El usuario ya forma parte del equipo.");
      }
      onClose();
    } catch (error) {
      setSubmitError(normalizeApiError(error).message);
    }
  }

  async function removeMember() {
    if (!member) return;
    setDeleting(true);
    setSubmitError(null);
    try {
      await businessApi.deleteBusinessUser(businessId, member.id);
      await onSaved("El integrante fue retirado del equipo.");
      onClose();
    } catch (error) {
      setSubmitError(normalizeApiError(error).message);
      setConfirmDelete(false);
    } finally {
      setDeleting(false);
    }
  }

  return (
    <div className="tm-review-backdrop">
      <section
        ref={panelRef}
        className="tm-review-panel tm-team-editor"
        role="dialog"
        aria-modal="true"
        aria-labelledby="team-editor-title"
      >
        <header className="tm-review-panel__head">
          <div>
            <h2 id="team-editor-title">
              {isEditing ? "Editar integrante" : "Agregar al equipo"}
            </h2>
            <p>
              {isEditing
                ? "Actualiza el rol, las sedes y la comisión asignada."
                : "Busca una cuenta existente y define su acceso al negocio."}
            </p>
          </div>
          <button
            ref={closeRef}
            className="tm-icon-button"
            type="button"
            aria-label="Cerrar"
            onClick={onClose}
            disabled={isSubmitting || deleting || searching}
          >
            <X aria-hidden="true" />
          </button>
        </header>

        <div className="tm-review-panel__body tm-team-editor__body">
          {submitError ? <FormAlert>{submitError}</FormAlert> : null}

          {!member && !candidate ? (
            <form className="tm-team-search" onSubmit={searchCandidate}>
              <div>
                <h3>Buscar cuenta de TecMarket</h3>
                <p>
                  La persona debe tener una cuenta activa. La búsqueda se
                  realiza por correo exacto.
                </p>
              </div>
              <div className="tm-team-search__controls">
                <TextField
                  label="Correo electrónico"
                  type="email"
                  autoComplete="email"
                  placeholder="persona@correo.com"
                  value={searchEmail}
                  error={searchError ?? undefined}
                  onChange={(event) => setSearchEmail(event.target.value)}
                />
                <button
                  className="tm-button tm-button--primary"
                  type="submit"
                  disabled={searching}
                >
                  {searching ? (
                    <LoaderCircle className="tm-spinner" aria-hidden="true" />
                  ) : (
                    <Search aria-hidden="true" />
                  )}
                  {searching ? "Buscando…" : "Buscar usuario"}
                </button>
              </div>
            </form>
          ) : null}

          {identity ? (
            <>
              <section
                className="tm-team-candidate"
                aria-label="Usuario seleccionado"
              >
                <span aria-hidden="true">
                  {fullNameInitials(identity.fullName)}
                </span>
                <div>
                  <strong>{identity.fullName}</strong>
                  <small>{identity.email}</small>
                </div>
                {!member ? (
                  <button
                    type="button"
                    onClick={() => {
                      setCandidate(null);
                      setSubmitError(null);
                    }}
                  >
                    Cambiar
                  </button>
                ) : null}
              </section>

              {candidate?.assigned ? (
                <FormAlert tone="warning">
                  Este usuario ya pertenece al negocio. Puedes modificar su
                  asignación desde el listado.
                </FormAlert>
              ) : (
                <form className="tm-team-form" onSubmit={handleSubmit(submit)}>
                  <fieldset>
                    <legend>Rol dentro del negocio</legend>
                    <SelectField
                      label="Rol"
                      error={errors.roleId?.message}
                      {...register("roleId")}
                    >
                      <option value="">Selecciona un rol</option>
                      {roles.map((role) => (
                        <option key={role.roleId} value={role.roleId}>
                          {role.roleName}
                        </option>
                      ))}
                    </SelectField>
                    {isTechnician ? (
                      <TextField
                        label="Comisión por reparación (%)"
                        inputMode="decimal"
                        placeholder="0,00"
                        hint="Opcional. Valor permitido entre 0 y 100."
                        error={errors.commissionPercentage?.message}
                        {...register("commissionPercentage")}
                      />
                    ) : null}
                  </fieldset>

                  <fieldset>
                    <legend>Sedes donde puede trabajar</legend>
                    <p className="tm-team-form__lead">
                      Selecciona al menos una de las sedes disponibles para tu
                      cuenta.
                    </p>
                    <div className="tm-team-branch-grid">
                      {branches.map((branch) => {
                        const selected = selectedBranchIds.includes(
                          String(branch.id),
                        );
                        return (
                          <label
                            key={branch.id}
                            className={selected ? "is-selected" : ""}
                          >
                            <input
                              type="checkbox"
                              value={String(branch.id)}
                              {...register("branchIds")}
                            />
                            <span>
                              {selected ? <Check aria-hidden="true" /> : null}
                            </span>
                            <span>
                              <strong>{branch.name}</strong>
                              <small>
                                {branch.cityName} · {branch.address}
                              </small>
                            </span>
                          </label>
                        );
                      })}
                    </div>
                    {errors.branchIds?.message ? (
                      <p className="tm-field__error" role="alert">
                        {errors.branchIds.message}
                      </p>
                    ) : null}
                    {!branches.length ? (
                      <FormAlert tone="warning">
                        No tienes sedes disponibles para realizar una
                        asignación.
                      </FormAlert>
                    ) : null}
                  </fieldset>

                  {member ? (
                    <section className="tm-team-danger">
                      <div>
                        <h3>Retirar del equipo</h3>
                        <p>
                          La persona perderá el acceso a este negocio y deberá
                          volver a iniciar sesión.
                        </p>
                      </div>
                      {confirmDelete ? (
                        <div className="tm-team-danger__confirm">
                          <p>
                            La cuenta global de TecMarket no será eliminada.
                          </p>
                          <div>
                            <button
                              ref={keepMemberRef}
                              className="tm-button tm-button--secondary"
                              type="button"
                              onClick={() => setConfirmDelete(false)}
                              disabled={deleting}
                            >
                              Conservar integrante
                            </button>
                            <button
                              className="tm-button tm-button--danger"
                              type="button"
                              onClick={removeMember}
                              disabled={deleting}
                            >
                              {deleting ? (
                                <LoaderCircle
                                  className="tm-spinner"
                                  aria-hidden="true"
                                />
                              ) : (
                                <Trash2 aria-hidden="true" />
                              )}{" "}
                              Retirar
                            </button>
                          </div>
                        </div>
                      ) : (
                        <button
                          className="tm-button tm-button--danger"
                          type="button"
                          onClick={() => setConfirmDelete(true)}
                        >
                          Retirar
                        </button>
                      )}
                    </section>
                  ) : null}

                  {!confirmDelete ? (
                    <footer className="tm-team-form__actions">
                      <button
                        className="tm-button tm-button--secondary"
                        type="button"
                        onClick={onClose}
                        disabled={isSubmitting || deleting}
                      >
                        Cancelar
                      </button>
                      <button
                        className="tm-button tm-button--primary"
                        type="submit"
                        disabled={isSubmitting || deleting || !branches.length}
                      >
                        {isSubmitting ? (
                          <LoaderCircle
                            className="tm-spinner"
                            aria-hidden="true"
                          />
                        ) : isEditing ? (
                          <Check aria-hidden="true" />
                        ) : (
                          <UserPlus aria-hidden="true" />
                        )}
                        {isSubmitting
                          ? "Guardando…"
                          : isEditing
                            ? "Guardar cambios"
                            : "Agregar al equipo"}
                      </button>
                    </footer>
                  ) : null}
                </form>
              )}
            </>
          ) : null}
        </div>
      </section>
    </div>
  );
}

export function BusinessTeamWorkspace({ businessId }: { businessId: number }) {
  const {
    businesses,
    businessContext,
    selectBusiness,
    refreshBusinessContext,
  } = useAuth();
  const router = useRouter();
  const [team, setTeam] = useState<PageResponse<BusinessUser> | null>(null);
  const [roles, setRoles] = useState<BusinessRoleOption[]>([]);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<ApiClientError | null>(null);
  const [notice, setNotice] = useState<string | null>(null);
  const [editor, setEditor] = useState<
    { mode: "create" } | { mode: "edit"; member: BusinessUser } | null
  >(null);
  const editorTriggerRef = useRef<HTMLButtonElement | null>(null);
  const exists = businesses.some((business) => business.id === businessId);
  const contextReady = businessContext?.businessId === businessId;
  const permissions = contextReady ? businessContext.permissions : [];
  const canView = hasPermission(permissions, "BUSINESS_USER_VIEW");
  const canCreate = hasPermission(permissions, "BUSINESS_USER_CREATE");
  const canUpdate = hasPermission(permissions, "BUSINESS_USER_UPDATE");
  const technicianCount = useMemo(
    () =>
      team?.items.filter((member) => member.roleCode === "TECHNICIAN").length ??
      0,
    [team],
  );

  const handleContextError = useCallback(
    (requestError: unknown) => {
      const normalized = normalizeApiError(requestError);
      if (normalized.status === 403) router.replace("/acceso-denegado");
      else setError(normalized);
    },
    [router],
  );

  useEffect(() => {
    if (!exists || contextReady) return;
    let active = true;
    selectBusiness(businessId).catch((requestError) => {
      if (active) handleContextError(requestError);
    });
    return () => {
      active = false;
    };
  }, [businessId, contextReady, exists, handleContextError, selectBusiness]);

  const loadTeam = useCallback(
    async (targetPage: number) => {
      setLoading(true);
      setError(null);
      try {
        const [members, roleOptions] = await Promise.all([
          businessApi.getBusinessUsers(businessId, targetPage, PAGE_SIZE),
          businessApi.getBusinessRoleOptions(businessId),
        ]);
        setTeam(members);
        setRoles(roleOptions);
      } catch (requestError) {
        const normalized = normalizeApiError(requestError);
        if (normalized.status === 403) router.replace("/acceso-denegado");
        else setError(normalized);
      } finally {
        setLoading(false);
      }
    },
    [businessId, router],
  );

  useEffect(() => {
    if (!contextReady) return;
    if (!canView) {
      router.replace("/acceso-denegado");
      return;
    }
    void loadTeam(page);
  }, [canView, contextReady, loadTeam, page, router]);

  const handleSaved = useCallback(
    async (message: string) => {
      let updatedTeam = await businessApi.getBusinessUsers(
        businessId,
        page,
        PAGE_SIZE,
      );
      if (page > 0 && !updatedTeam.items.length) {
        const previousPage = page - 1;
        updatedTeam = await businessApi.getBusinessUsers(
          businessId,
          previousPage,
          PAGE_SIZE,
        );
        setPage(previousPage);
      }
      setTeam(updatedTeam);
      await refreshBusinessContext(businessId);
      setNotice(message);
    },
    [businessId, page, refreshBusinessContext],
  );

  const closeEditor = useCallback(() => {
    setEditor(null);
    requestAnimationFrame(() => editorTriggerRef.current?.focus());
  }, []);

  if (!exists)
    return (
      <div className="tm-page-stack">
        <ErrorState
          title="Negocio no disponible"
          message="Este negocio no está asociado a tu cuenta o ya no está disponible."
        />
      </div>
    );
  if (error && !contextReady)
    return (
      <div className="tm-page-stack">
        <ErrorState
          message={error.message}
          onRetry={() =>
            void selectBusiness(businessId).catch(handleContextError)
          }
        />
      </div>
    );
  if (!contextReady || !canView)
    return <FullPageLoader label="Preparando el equipo del negocio" />;

  return (
    <div className="tm-page-stack tm-team-page">
      <header className="tm-workspace-heading tm-team-heading">
        <div>
          <h1>Equipo</h1>
          <p>
            Administra quién puede trabajar en {businessContext.businessName},
            su rol y sus sedes.
          </p>
        </div>
        {canCreate ? (
          <button
            className="tm-button tm-button--primary"
            type="button"
            onClick={(event) => {
              editorTriggerRef.current = event.currentTarget;
              setNotice(null);
              setEditor({ mode: "create" });
            }}
          >
            <UserPlus aria-hidden="true" /> Agregar integrante
          </button>
        ) : null}
      </header>

      {notice ? <FormAlert tone="success">{notice}</FormAlert> : null}
      {error ? (
        <ErrorState
          message={error.message}
          onRetry={() => void loadTeam(page)}
        />
      ) : null}

      {!error ? (
        <section className="tm-team-board">
          <header>
            <div className="tm-team-board__title">
              <span>
                <UsersRound aria-hidden="true" />
              </span>
              <div>
                <h2>Personas con acceso</h2>
                <p>Asignaciones activas dentro del negocio.</p>
              </div>
            </div>
            {!loading && team?.totalElements ? (
              <div className="tm-team-board__summary">
                <small>
                  {team.totalElements}{" "}
                  {team.totalElements === 1 ? "integrante" : "integrantes"}
                </small>
                <strong>
                  {technicianCount}{" "}
                  {technicianCount === 1
                    ? "técnico en esta página"
                    : "técnicos en esta página"}
                </strong>
              </div>
            ) : null}
          </header>

          {loading ? (
            <div className="tm-team-board__loading">
              <LoaderCircle className="tm-spinner" aria-hidden="true" />{" "}
              Cargando equipo
            </div>
          ) : team?.items.length ? (
            <div className="tm-team-list">
              {team.items.map((member) => (
                <TeamMemberRow
                  key={member.id}
                  member={member}
                  branches={businessContext.branches}
                  canUpdate={canUpdate}
                  onEdit={(trigger) => {
                    editorTriggerRef.current = trigger;
                    setNotice(null);
                    setEditor({ mode: "edit", member });
                  }}
                />
              ))}
            </div>
          ) : (
            <EmptyState
              title="Todavía no hay integrantes"
              message="Agrega una cuenta existente de TecMarket y define dónde podrá trabajar."
              action={
                canCreate ? (
                  <button
                    className="tm-button tm-button--primary"
                    type="button"
                    onClick={(event) => {
                      editorTriggerRef.current = event.currentTarget;
                      setEditor({ mode: "create" });
                    }}
                  >
                    <UserPlus aria-hidden="true" /> Agregar integrante
                  </button>
                ) : undefined
              }
            />
          )}

          {!loading && team?.items.length ? (
            <footer className="tm-team-board__footer">
              <div>
                <button
                  className="tm-icon-button"
                  type="button"
                  aria-label="Actualizar equipo"
                  onClick={() => void loadTeam(page)}
                >
                  <RefreshCw aria-hidden="true" />
                </button>
                <p>
                  <strong>Acceso por rol y sede</strong>
                  <span>
                    Los cambios de rol o estado invalidan la sesión de la
                    persona afectada.
                  </span>
                </p>
              </div>
              {team.totalPages > 1 ? (
                <nav aria-label="Paginación del equipo">
                  <button
                    className="tm-icon-button"
                    type="button"
                    aria-label="Página anterior"
                    disabled={!team.hasPrevious}
                    onClick={() =>
                      setPage((current) => Math.max(0, current - 1))
                    }
                  >
                    <ChevronLeft aria-hidden="true" />
                  </button>
                  <span>
                    Página {team.page + 1} de {team.totalPages}
                  </span>
                  <button
                    className="tm-icon-button"
                    type="button"
                    aria-label="Página siguiente"
                    disabled={!team.hasNext}
                    onClick={() => setPage((current) => current + 1)}
                  >
                    <ChevronRight aria-hidden="true" />
                  </button>
                </nav>
              ) : null}
            </footer>
          ) : null}
        </section>
      ) : null}

      {editor ? (
        <TeamMemberEditor
          businessId={businessId}
          member={editor.mode === "edit" ? editor.member : null}
          roles={roles}
          branches={businessContext.branches}
          onClose={closeEditor}
          onSaved={handleSaved}
        />
      ) : null}

      {!canCreate && !canUpdate ? (
        <div className="tm-team-readonly">
          <CircleAlert aria-hidden="true" />
          <p>
            <strong>Acceso de consulta</strong>
            <span>
              Puedes revisar el equipo, pero tu rol no permite modificar
              asignaciones.
            </span>
          </p>
        </div>
      ) : null}
    </div>
  );
}
