import type { Metadata } from "next";

import { BusinessServicesWorkspace } from "@/components/business/business-services";

export const metadata: Metadata = { title: "Servicios" };

export default async function BusinessServicesPage({
  params,
}: {
  params: Promise<{ businessId: string }>;
}) {
  const { businessId } = await params;

  return <BusinessServicesWorkspace businessId={Number(businessId)} />;
}
