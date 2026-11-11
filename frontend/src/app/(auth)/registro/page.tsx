import type { Metadata } from "next";
import { AuthShell } from "@/components/auth/auth-shell";
import { RegisterForm } from "@/components/auth/identity-forms";
export const metadata: Metadata = { title: "Crear cuenta" };
export default function RegisterPage() { return <AuthShell title="Crea tu cuenta" description="Registra tus datos personales y ubicación para comenzar."><RegisterForm /></AuthShell>; }
