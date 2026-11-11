import type { Metadata } from "next";

import { AuthShell } from "@/components/auth/auth-shell";
import { LoginForm } from "@/components/auth/identity-forms";
import { FormAlert } from "@/components/ui/feedback";

export const metadata: Metadata = { title: "Ingresar" };

export default async function LoginPage({ searchParams }: { searchParams: Promise<Record<string, string | string[] | undefined>> }) {
  const params = await searchParams;
  const next = typeof params.next === "string" ? params.next : "/app";
  const notice = params.registro === "exitoso" ? "Cuenta creada. Revisa tu correo para verificarla antes de ingresar." : params.contrasena === "actualizada" ? "Contraseña actualizada. Inicia sesión nuevamente." : params.sesiones === "revocadas" ? "Todas las sesiones fueron revocadas." : params.acceso === "actualizado" ? "Inicia sesión nuevamente para cargar los accesos de tu negocio aprobado." : null;
  return <AuthShell title="Continúa tu operación" description="Ingresa con tu cuenta global de TecMarket.">{notice ? <FormAlert tone="success">{notice}</FormAlert> : null}<LoginForm next={next} /></AuthShell>;
}
