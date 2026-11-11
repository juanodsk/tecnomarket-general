"use client";

import type { ThemeProviderProps } from "next-themes";
import { ThemeProvider } from "next-themes";
import type { ReactNode } from "react";

import { AuthProvider } from "@/providers/auth-provider";

export function AppProviders({
  children,
  themeProps,
}: {
  children: ReactNode;
  themeProps?: ThemeProviderProps;
}) {
  return (
    <ThemeProvider attribute="class" defaultTheme="system" enableSystem disableTransitionOnChange {...themeProps}>
      <AuthProvider>{children}</AuthProvider>
    </ThemeProvider>
  );
}
