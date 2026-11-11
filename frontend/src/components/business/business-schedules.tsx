"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import {
  CalendarClock,
  CircleAlert,
  Clock3,
  LoaderCircle,
  MapPin,
  Pencil,
  Plus,
  RefreshCw,
  Trash2,
  X,
} from "lucide-react";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { useForm } from "react-hook-form";

import { EmptyState, ErrorState, FormAlert, FullPageLoader } from "@/components/ui/feedback";
import { TextField } from "@/components/ui/form-controls";
import * as schedulesApi from "@/features/business-schedules/api";
import {
  businessScheduleSchema,
  schedulesOverlap,
  toApiTime,
  type BusinessScheduleFormValues,
} from "@/features/business-schedules/schemas";
import { ApiClientError, normalizeApiError } from "@/lib/api/errors";
import { hasPermission } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";
import type { BusinessContextBranch } from "@/types/business";
import type { BusinessSchedule } from "@/types/business-schedule";

const DAYS = [
  { value: 1, label: "Lunes", shortLabel: "Lun" },
  { value: 2, label: "Martes", shortLabel: "Mar" },
  { value: 3, label: "Miércoles", shortLabel: "Mié" },
  { value: 4, label: "Jueves", shortLabel: "Jue" },
  { value: 5, label: "Viernes", shortLabel: "Vie" },
  { value: 6, label: "Sábado", shortLabel: "Sáb" },
  { value: 7, label: "Domingo", shortLabel: "Dom" },
] as const;

type Notice = { tone: "success" | "warning"; message: string };

function formatTime(value: string) {
  return value.slice(0, 5);
}

function valuesFor(schedule: BusinessSchedule | null): BusinessScheduleFormValues {
  return {
    openTime: schedule ? formatTime(schedule.openTime) : "08:00",
    closeTime: schedule ? formatTime(schedule.closeTime) : "17:00",
  };
}

function ScheduleEditor({
  businessId,
  branch,
  day,
  schedule,
  branchSchedules,
  onClose,
  onSaved,
}: {
  businessId: number;
  branch: BusinessContextBranch;
  day: (typeof DAYS)[number];
  schedule: BusinessSchedule | null;
  branchSchedules: BusinessSchedule[];
  onClose: () => void;
  onSaved: (notice: Notice) => Promise<void>;
}) {
  const panelRef = useRef<HTMLElement>(null);
  const closeRef = useRef<HTMLButtonElement>(null);
  const keepRef = useRef<HTMLButtonElement>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const isEditing = Boolean(schedule);
  const {
    register,
    handleSubmit,
    setError,
    formState: { errors, isSubmitting },
  } = useForm<BusinessScheduleFormValues>({
    resolver: zodResolver(businessScheduleSchema),
    defaultValues: valuesFor(schedule),
  });

  useEffect(() => {
    closeRef.current?.focus();
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape" && !isSubmitting && !deleting) onClose();
      if (event.key !== "Tab") return;
      const focusable = Array.from(
        panelRef.current?.querySelectorAll<HTMLElement>(
          "button:not([disabled]), input:not([disabled])",
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
  }, [deleting, isSubmitting, onClose]);

  useEffect(() => {
    if (confirmDelete) keepRef.current?.focus();
  }, [confirmDelete]);

  async function submit(values: BusinessScheduleFormValues) {
    setSubmitError(null);
    const overlapping = branchSchedules.some((current) =>
      current.dayOfWeek === day.value
      && current.id !== schedule?.id
      && schedulesOverlap(values.openTime, values.closeTime, current.openTime, current.closeTime),
    );
    if (overlapping) {
      setError("closeTime", {
        message: "Esta jornada se cruza con otro horario del mismo día.",
      });
      return;
    }

    try {
      if (schedule) {
        await schedulesApi.updateBusinessSchedule(businessId, schedule.id, {
          dayOfWeek: day.value,
          openTime: toApiTime(values.openTime),
          closeTime: toApiTime(values.closeTime),
          slotOrder: schedule.slotOrder,
          active: schedule.active,
        });
      } else {
        await schedulesApi.createBusinessSchedule(businessId, {
          branchId: branch.id,
          dayOfWeek: day.value,
          openTime: toApiTime(values.openTime),
          closeTime: toApiTime(values.closeTime),
          slotOrder: null,
        });
      }
      await onSaved({
        tone: "success",
        message: schedule
          ? `El horario del ${day.label.toLowerCase()} quedó actualizado.`
          : `Se agregó una jornada para el ${day.label.toLowerCase()}.`,
      });
      onClose();
    } catch (error) {
      setSubmitError(normalizeApiError(error).message);
    }
  }

  async function removeSchedule() {
    if (!schedule) return;
    setDeleting(true);
    setSubmitError(null);
    try {
      await schedulesApi.deleteBusinessSchedule(businessId, schedule.id);
      await onSaved({ tone: "success", message: `La jornada del ${day.label.toLowerCase()} fue eliminada.` });
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
        className="tm-review-panel tm-schedule-editor"
        role="dialog"
        aria-modal="true"
        aria-labelledby="schedule-editor-title"
      >
        <header className="tm-review-panel__head">
          <div>
            <h2 id="schedule-editor-title">{isEditing ? "Editar jornada" : "Añadir jornada"}</h2>
            <p>{day.label} en {branch.name}</p>
          </div>
          <button ref={closeRef} className="tm-icon-button" type="button" aria-label="Cerrar" onClick={onClose} disabled={isSubmitting || deleting}>
            <X aria-hidden="true" />
          </button>
        </header>

        <form className="tm-review-panel__body tm-schedule-form" onSubmit={handleSubmit(submit)}>
          {submitError ? <FormAlert>{submitError}</FormAlert> : null}

          <div className="tm-schedule-form__summary">
            <span><MapPin aria-hidden="true" /></span>
            <div><small>Sede seleccionada</small><strong>{branch.name}</strong><p>{branch.address}, {branch.cityName}</p></div>
          </div>

          <fieldset>
            <legend>Horario del {day.label.toLowerCase()}</legend>
            <div className="tm-schedule-form__times">
              <TextField type="time" label="Hora de apertura" error={errors.openTime?.message} {...register("openTime")} />
              <TextField type="time" label="Hora de cierre" error={errors.closeTime?.message} {...register("closeTime")} />
            </div>
            <p className="tm-schedule-form__hint">Puedes añadir otra jornada al mismo día, por ejemplo después del almuerzo.</p>
          </fieldset>

          {confirmDelete ? (
            <div className="tm-schedule-delete" role="alert">
              <div><Trash2 aria-hidden="true" /><p><strong>¿Eliminar esta jornada?</strong><span>Dejará de mostrarse en el horario de la sede.</span></p></div>
              <div>
                <button ref={keepRef} className="tm-button tm-button--secondary" type="button" onClick={() => setConfirmDelete(false)} disabled={deleting}>Conservar</button>
                <button className="tm-button tm-button--danger" type="button" onClick={() => void removeSchedule()} disabled={deleting}>{deleting ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <Trash2 aria-hidden="true" />} Eliminar</button>
              </div>
            </div>
          ) : null}

          <footer className="tm-review-panel__actions">
            {schedule && !confirmDelete ? <button className="tm-button tm-button--danger-ghost" type="button" onClick={() => setConfirmDelete(true)} disabled={isSubmitting}><Trash2 aria-hidden="true" /> Eliminar</button> : <span />}
            <div>
              <button className="tm-button tm-button--secondary" type="button" onClick={onClose} disabled={isSubmitting || deleting}>Cancelar</button>
              <button className="tm-button tm-button--primary" type="submit" disabled={isSubmitting || deleting}>{isSubmitting ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : null}{isEditing ? "Guardar cambios" : "Añadir horario"}</button>
            </div>
          </footer>
        </form>
      </section>
    </div>
  );
}

export function BusinessSchedulesWorkspace({ businessId }: { businessId: number }) {
  const { businesses, businessContext, activeBranchId, selectBusiness } = useAuth();
  const router = useRouter();
  const [schedules, setSchedules] = useState<BusinessSchedule[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<ApiClientError | null>(null);
  const [notice, setNotice] = useState<Notice | null>(null);
  const [editor, setEditor] = useState<{ day: (typeof DAYS)[number]; schedule: BusinessSchedule | null } | null>(null);
  const editorTriggerRef = useRef<HTMLButtonElement | null>(null);
  const exists = businesses.some((business) => business.id === businessId);
  const contextReady = businessContext?.businessId === businessId;
  const permissions = contextReady ? businessContext.permissions : [];
  const canView = hasPermission(permissions, "BUSINESS_SCHEDULE_VIEW");
  const canCreate = hasPermission(permissions, "BUSINESS_SCHEDULE_CREATE");
  const canUpdate = hasPermission(permissions, "BUSINESS_SCHEDULE_UPDATE");
  const activeBranch = businessContext?.branches.find((branch) => branch.id === activeBranchId) ?? null;
  const branchSchedules = useMemo(
    () => schedules
      .filter((schedule) => schedule.active && schedule.branchId === activeBranchId)
      .sort((a, b) => a.dayOfWeek - b.dayOfWeek || (a.slotOrder ?? 0) - (b.slotOrder ?? 0) || a.openTime.localeCompare(b.openTime)),
    [activeBranchId, schedules],
  );
  const openDays = useMemo(
    () => new Set(branchSchedules.map((schedule) => schedule.dayOfWeek)).size,
    [branchSchedules],
  );

  const handleContextError = useCallback((requestError: unknown) => {
    const normalized = normalizeApiError(requestError);
    if (normalized.status === 403) router.replace("/acceso-denegado");
    else setError(normalized);
  }, [router]);

  useEffect(() => {
    if (!exists || contextReady) return;
    let active = true;
    selectBusiness(businessId).catch((requestError) => {
      if (active) handleContextError(requestError);
    });
    return () => { active = false; };
  }, [businessId, contextReady, exists, handleContextError, selectBusiness]);

  const loadSchedules = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      setSchedules(await schedulesApi.getAllBusinessSchedules(businessId));
    } catch (requestError) {
      const normalized = normalizeApiError(requestError);
      if (normalized.status === 403) router.replace("/acceso-denegado");
      else setError(normalized);
    } finally {
      setLoading(false);
    }
  }, [businessId, router]);

  useEffect(() => {
    if (!contextReady) return;
    if (!canView) {
      router.replace("/acceso-denegado");
      return;
    }
    void loadSchedules();
  }, [canView, contextReady, loadSchedules, router]);

  const handleSaved = useCallback(async (nextNotice: Notice) => {
    setSchedules(await schedulesApi.getAllBusinessSchedules(businessId));
    setNotice(nextNotice);
  }, [businessId]);

  const closeEditor = useCallback(() => {
    setEditor(null);
    requestAnimationFrame(() => editorTriggerRef.current?.focus());
  }, []);

  const openEditor = (trigger: HTMLButtonElement, day: (typeof DAYS)[number], schedule: BusinessSchedule | null) => {
    editorTriggerRef.current = trigger;
    setNotice(null);
    setEditor({ day, schedule });
  };

  if (!exists) return <div className="tm-page-stack"><ErrorState title="Negocio no disponible" message="Este negocio no está asociado a tu cuenta o ya no está disponible." /></div>;
  if (error && !contextReady) return <div className="tm-page-stack"><ErrorState message={error.message} onRetry={() => void selectBusiness(businessId).catch(handleContextError)} /></div>;
  if (!contextReady || !canView) return <FullPageLoader label="Preparando los horarios del negocio" />;

  return (
    <div className="tm-page-stack tm-schedules-page">
      <header className="tm-workspace-heading tm-schedules-heading">
        <div><h1>Horarios</h1><p>Organiza las jornadas de atención de cada sede de {businessContext.businessName}.</p></div>
      </header>

      {notice ? <FormAlert tone={notice.tone}>{notice.message}</FormAlert> : null}
      {error ? <ErrorState message={error.message} onRetry={() => void loadSchedules()} /> : null}

      {!error && !activeBranch ? (
        <EmptyState title="No hay una sede disponible" message="Necesitas una sede accesible para consultar o configurar sus horarios." />
      ) : null}

      {!error && activeBranch ? (
        <section className="tm-schedule-board">
          <header className="tm-schedule-board__head">
            <div className="tm-schedule-board__identity">
              <span><CalendarClock aria-hidden="true" /></span>
              <div><small>Agenda semanal</small><h2>{activeBranch.name}</h2><p><MapPin aria-hidden="true" /> {activeBranch.address}, {activeBranch.cityName}</p></div>
            </div>
            {!loading ? <div className="tm-schedule-board__summary"><small>{branchSchedules.length} {branchSchedules.length === 1 ? "jornada" : "jornadas"}</small><strong>{openDays} {openDays === 1 ? "día" : "días"} con atención</strong></div> : null}
          </header>

          {loading ? (
            <div className="tm-schedule-board__loading"><LoaderCircle className="tm-spinner" aria-hidden="true" /> Cargando horarios</div>
          ) : (
            <div className="tm-schedule-week">
              {DAYS.map((day) => {
                const daySchedules = branchSchedules.filter((schedule) => schedule.dayOfWeek === day.value);
                return (
                  <article key={day.value} className={`tm-schedule-day ${daySchedules.length ? "has-schedules" : ""}`}>
                    <div className="tm-schedule-day__name"><span>{day.shortLabel}</span><strong>{day.label}</strong></div>
                    <div className="tm-schedule-day__slots">
                      {daySchedules.length ? daySchedules.map((schedule) => (
                        <div key={schedule.id} className="tm-schedule-slot">
                          <Clock3 aria-hidden="true" />
                          <strong>{formatTime(schedule.openTime)} — {formatTime(schedule.closeTime)}</strong>
                          {canUpdate ? <button className="tm-icon-button" type="button" aria-label={`Editar jornada del ${day.label.toLowerCase()} de ${formatTime(schedule.openTime)} a ${formatTime(schedule.closeTime)}`} onClick={(event) => openEditor(event.currentTarget, day, schedule)}><Pencil aria-hidden="true" /></button> : null}
                        </div>
                      )) : <span className="tm-schedule-day__closed">Sin atención</span>}
                    </div>
                    {canCreate ? <button className="tm-schedule-day__add" type="button" onClick={(event) => openEditor(event.currentTarget, day, null)}><Plus aria-hidden="true" /> Añadir horario</button> : null}
                  </article>
                );
              })}
            </div>
          )}

          {!loading ? (
            <footer className="tm-schedule-board__footer">
              <button className="tm-icon-button" type="button" aria-label="Actualizar horarios" onClick={() => void loadSchedules()}><RefreshCw aria-hidden="true" /></button>
              <p><strong>¿Quieres trabajar otra sede?</strong><span>Cámbiala desde el selector superior y verás su propia agenda.</span></p>
            </footer>
          ) : null}
        </section>
      ) : null}

      {editor && activeBranch ? <ScheduleEditor businessId={businessId} branch={activeBranch} day={editor.day} schedule={editor.schedule} branchSchedules={branchSchedules} onClose={closeEditor} onSaved={handleSaved} /> : null}

      {!canCreate && !canUpdate ? <div className="tm-service-readonly"><CircleAlert aria-hidden="true" /><p><strong>Acceso de consulta</strong><span>Puedes revisar los horarios, pero tu rol no permite modificarlos.</span></p></div> : null}
    </div>
  );
}
