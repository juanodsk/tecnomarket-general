import type { Metadata } from "next";

import { BusinessBranchesWorkspace } from "@/components/business/business-branches";

export const metadata: Metadata = { title: "Sedes" };

export default async function BusinessBranchesPage({
  params,
}: {
  params: Promise<{ businessId: string }>;
}) {
  const { businessId } = await params;

  return <BusinessBranchesWorkspace businessId={Number(businessId)} />;
}
