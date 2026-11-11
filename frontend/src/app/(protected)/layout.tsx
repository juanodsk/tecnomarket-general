import type { ReactNode } from "react";
import { AppShell } from "@/components/app/app-shell";
import { AuthGuard } from "@/components/app/auth-guard";
export default function ProtectedLayout({ children }: { children: ReactNode }) { return <AuthGuard><AppShell>{children}</AppShell></AuthGuard>; }
