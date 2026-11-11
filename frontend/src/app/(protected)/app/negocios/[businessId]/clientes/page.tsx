import type { Metadata } from "next";

import { CustomerDevicesWorkspace } from "@/components/business/customer-devices";

export const metadata: Metadata = { title: "Clientes y dispositivos" };

export default async function CustomerDevicesPage({
  params,
}: PageProps<"/app/negocios/[businessId]/clientes">) {
  const { businessId } = await params;

  return <CustomerDevicesWorkspace businessId={Number(businessId)} />;
}
