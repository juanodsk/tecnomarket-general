import type { Metadata } from "next";

import { BusinessQualityTestsWorkspace } from "@/components/business/business-quality-tests";

export const metadata: Metadata = { title: "Pruebas de recepción" };

export default async function BusinessQualityTestsPage({
  params,
}: {
  params: Promise<{ businessId: string }>;
}) {
  const { businessId } = await params;

  return <BusinessQualityTestsWorkspace businessId={Number(businessId)} />;
}
