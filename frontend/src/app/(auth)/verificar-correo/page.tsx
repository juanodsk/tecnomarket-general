import type { Metadata } from "next";
import { AuthShell } from "@/components/auth/auth-shell";
import { VerifyEmailPanel } from "@/components/auth/identity-forms";
export const metadata: Metadata = { title: "Verificar correo" };
export default async function VerifyPage({ searchParams }: { searchParams: Promise<{ token?: string }> }) { const { token = "" } = await searchParams; return <AuthShell title="Validando tu correo" description="Confirmaremos que el enlace corresponde a tu cuenta."><VerifyEmailPanel token={token} /></AuthShell>; }
