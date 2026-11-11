"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "@heroui/react";
import { KeyRound, LoaderCircle, MapPin, MonitorSmartphone, ShieldCheck, Trash2 } from "lucide-react";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useState } from "react";
import { useForm } from "react-hook-form";

import { ErrorState, FormAlert } from "@/components/ui/feedback";
import { PasswordField } from "@/components/ui/form-controls";
import * as authApi from "@/features/auth/api";
import { changePasswordSchema, type ChangePasswordFormValues } from "@/features/auth/schemas";
import { apiErrorMessage } from "@/lib/api/errors";
import { useAuth } from "@/providers/auth-provider";
import type { AuthSession } from "@/types/auth";

function PageHeading({ title, description }: { title: string; description: string }) {
  return (
    <header className="tm-page-heading">
      <div><h1>{title}</h1><p>{description}</p></div>
    </header>
  );
}

export function ProfilePanel() {
  const { user, access } = useAuth();
  if (!user) return null;

  return (
    <div className="tm-page-stack">
      <PageHeading title="Perfil global" description="Datos de identidad asociados a tu cuenta de TecMarket." />
      <section className="tm-detail-sheet">
        <div className="tm-detail-sheet__identity">
          <span>{user.name.slice(0, 1)}{user.surname.slice(0, 1)}</span>
          <div><h2>{user.name} {user.surname}</h2><p>{user.email}</p></div>
        </div>
        <dl className="tm-definition-grid">
          <div><dt>Documento</dt><dd>{user.documentTypeCode} {user.documentNumber}</dd></div>
          <div><dt>Teléfono</dt><dd>{user.phone}</dd></div>
          <div><dt>Ubicación</dt><dd>{user.cityName}, {user.departmentName}</dd></div>
          <div><dt>País</dt><dd>{user.countryName}</dd></div>
        </dl>
        <div className="tm-token-list">
          <p>Roles globales</p>
          <div>{access?.roles.length ? access.roles.map((role) => <span key={role}>{role === "PLATFORM_ADMIN" ? "Administrador de plataforma" : role.replaceAll("_", " ")}</span>) : <span>Usuario</span>}</div>
        </div>
      </section>
    </div>
  );
}

export function SecurityPanel() {
  const router = useRouter();
  const { endLocalSession } = useAuth();
  const [feedback, setFeedback] = useState<string | null>(null);
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<ChangePasswordFormValues>({ resolver: zodResolver(changePasswordSchema) });
  const onSubmit = handleSubmit(async ({ currentPassword, newPassword }) => {
    setFeedback(null);
    try {
      await authApi.changePassword(currentPassword, newPassword);
      endLocalSession();
      router.replace("/login?contrasena=actualizada");
    } catch (error) {
      setFeedback(apiErrorMessage(error));
    }
  });

  return (
    <div className="tm-page-stack">
      <PageHeading title="Seguridad" description="Cambia tu contraseña y cierra automáticamente todas las sesiones activas." />
      <section className="tm-form-sheet">
        <div className="tm-sheet-icon"><KeyRound aria-hidden="true" /></div>
        <form className="tm-form" onSubmit={onSubmit} noValidate>
          {feedback ? <FormAlert>{feedback}</FormAlert> : null}
          <PasswordField label="Contraseña actual" autoComplete="current-password" error={errors.currentPassword?.message} {...register("currentPassword")} />
          <PasswordField label="Nueva contraseña" autoComplete="new-password" hint="Mínimo 12 caracteres." error={errors.newPassword?.message} {...register("newPassword")} />
          <PasswordField label="Confirmar contraseña" autoComplete="new-password" error={errors.confirmPassword?.message} {...register("confirmPassword")} />
          <Button className="tm-button tm-button--primary" type="submit" isDisabled={isSubmitting}>{isSubmitting ? <LoaderCircle className="tm-spinner" aria-hidden="true" /> : <ShieldCheck aria-hidden="true" />}Actualizar contraseña</Button>
        </form>
      </section>
    </div>
  );
}

function formatDate(value: string) {
  return new Intl.DateTimeFormat("es-CO", { dateStyle: "medium", timeStyle: "short" }).format(new Date(value));
}

export function SessionsPanel() {
  const router = useRouter();
  const { endLocalSession } = useAuth();
  const [sessions, setSessions] = useState<AuthSession[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [busyId, setBusyId] = useState<string | null>(null);

  const load = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      setSessions(await authApi.getSessions());
    } catch (requestError) {
      setError(apiErrorMessage(requestError));
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => void load(), [load]);

  async function revoke(sessionId: string) {
    setBusyId(sessionId);
    try {
      await authApi.revokeSession(sessionId);
      setSessions((current) => current.filter((session) => session.sessionId !== sessionId));
    } catch (requestError) {
      setError(apiErrorMessage(requestError));
    } finally {
      setBusyId(null);
    }
  }

  async function revokeAll() {
    setBusyId("all");
    try {
      await authApi.revokeAllSessions();
      endLocalSession();
      router.replace("/login?sesiones=revocadas");
    } catch (requestError) {
      setError(apiErrorMessage(requestError));
      setBusyId(null);
    }
  }

  return (
    <div className="tm-page-stack">
      <PageHeading title="Sesiones" description="Revisa y revoca accesos activos asociados a tu cuenta." />
      {error ? <ErrorState message={error} onRetry={load} /> : null}
      <section className="tm-session-sheet" aria-busy={loading}>
        <div className="tm-session-sheet__head"><div><h2>Dispositivos con acceso</h2><p>{sessions.length} sesiones activas</p></div><Button className="tm-button tm-button--danger" type="button" onPress={revokeAll} isDisabled={loading || busyId !== null}><Trash2 aria-hidden="true" />Revocar todas</Button></div>
        {loading ? <div className="tm-inline-loader" role="status"><LoaderCircle className="tm-spinner" aria-hidden="true" />Cargando sesiones…</div> : (
          <div className="tm-session-list">
            {sessions.map((session) => (
              <article key={session.sessionId}>
                <div className="tm-session-icon"><MonitorSmartphone aria-hidden="true" /></div>
                <div><h3>{session.clientDescription || "Dispositivo sin identificar"}</h3><p><MapPin aria-hidden="true" /> {session.ipAddress || "IP no disponible"}</p><small>Último uso: {formatDate(session.lastUsedAt)} · Expira: {formatDate(session.expiresAt)}</small></div>
                <button className="tm-icon-button tm-icon-button--danger" type="button" onClick={() => revoke(session.sessionId)} disabled={busyId !== null} aria-label={`Revocar sesión ${session.clientDescription || session.sessionId}`}><Trash2 aria-hidden="true" /></button>
              </article>
            ))}
            {!sessions.length ? <p className="tm-empty-line">No hay sesiones activas para mostrar.</p> : null}
          </div>
        )}
      </section>
    </div>
  );
}
