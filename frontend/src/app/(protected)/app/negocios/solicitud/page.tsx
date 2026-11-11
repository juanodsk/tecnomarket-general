import type { Metadata } from "next";

import { BusinessRequestWorkspace } from "@/components/business/business-request-workspace";

export const metadata: Metadata = { title: "Solicitudes de negocio" };

export default function BusinessRequestPage() {
  return <BusinessRequestWorkspace />;
}
