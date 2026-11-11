import type { Metadata } from "next";

import { BusinessTeamWorkspace } from "@/components/business/business-team";

export const metadata: Metadata = { title: "Equipo" };

export default async function BusinessTeamPage({
  params,
}: {
  params: Promise<{ businessId: string }>;
}) {
  const { businessId } = await params;

  return <BusinessTeamWorkspace businessId={Number(businessId)} />;
}
