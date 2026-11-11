import type { Metadata } from "next";
import { AuthShell } from "@/components/auth/auth-shell";
import { ForgotPasswordForm } from "@/components/auth/identity-forms";
export const metadata: Metadata = { title: "Recuperar contraseña" };
export default function ForgotPage() { return <AuthShell title="Recupera tu acceso" description="Te enviaremos instrucciones si el correo está registrado."><ForgotPasswordForm /></AuthShell>; }
