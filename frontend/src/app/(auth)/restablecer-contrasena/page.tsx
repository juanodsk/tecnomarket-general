import type { Metadata } from "next";
import { AuthShell } from "@/components/auth/auth-shell";
import { ResetPasswordForm } from "@/components/auth/identity-forms";
export const metadata: Metadata = { title: "Restablecer contraseña" };
export default async function ResetPage({ searchParams }: { searchParams: Promise<{ token?: string }> }) { const { token = "" } = await searchParams; return <AuthShell title="Define una nueva contraseña" description="El enlace es de un solo uso y no se guardará en el navegador."><ResetPasswordForm token={token} /></AuthShell>; }
