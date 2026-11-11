import type { Metadata } from "next";
import { Inter } from "next/font/google";
import type { ReactNode } from "react";

import "maplibre-gl/dist/maplibre-gl.css";

import { AppProviders } from "@/providers/app-providers";
import "@/styles/globals.css";

const inter = Inter({ subsets: ["latin"], variable: "--font-inter", display: "swap" });

export const metadata: Metadata = {
  title: { default: "TecMarket", template: "%s · TecMarket" },
  description: "Plataforma de gestión para negocios de servicio técnico.",
};

export default function RootLayout({ children }: Readonly<{ children: ReactNode }>) {
  return (
    <html lang="es" data-scroll-behavior="smooth" suppressHydrationWarning>
      <body className={inter.variable}><AppProviders>{children}</AppProviders></body>
    </html>
  );
}
