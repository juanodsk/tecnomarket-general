import type { Metadata } from "next";

import { PlatformRequests } from "@/components/platform/platform-workspace";

export const metadata: Metadata = { title: "Solicitudes de negocio" };

export default function PlatformRequestsPage() {
  return <PlatformRequests />;
}
