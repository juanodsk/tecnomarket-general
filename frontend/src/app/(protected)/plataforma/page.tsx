import type { Metadata } from "next";
import { PlatformDashboard } from "@/components/platform/platform-workspace";

export const metadata: Metadata = { title: "Resumen de plataforma" };

export default function PlatformPage() {
  return <PlatformDashboard />;
}
