import type { Metadata } from "next";

import { BusinessSchedulesWorkspace } from "@/components/business/business-schedules";

export const metadata: Metadata = { title: "Horarios" };

export default async function BusinessSchedulesPage({
  params,
}: {
  params: Promise<{ businessId: string }>;
}) {
  const { businessId } = await params;

  return <BusinessSchedulesWorkspace businessId={Number(businessId)} />;
}
