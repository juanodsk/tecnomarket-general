"use client";

import type { ReactNode } from "react";

import { hasPermission, hasRole } from "@/lib/permissions/can";
import { useAuth } from "@/providers/auth-provider";

export function PermissionGate({
  permission,
  role,
  fallback = null,
  children,
}: {
  permission?: string;
  role?: string;
  fallback?: ReactNode;
  children: ReactNode;
}) {
  const { access, businessContext } = useAuth();
  const permissions = businessContext?.permissions ?? access?.permissions ?? [];
  const roles = access?.roles ?? [];
  const allowed = (!permission || hasPermission(permissions, permission)) && (!role || hasRole(roles, role));

  return allowed ? children : fallback;
}
