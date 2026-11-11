import type { Metadata } from "next";
import { BusinessWorkspace } from "@/components/business/business-views";
export const metadata: Metadata = { title: "Espacio de negocio" };
export default async function BusinessPage({
  params,
}: {
  params: Promise<{ businessId: string }>;
}) {
  const { businessId } = await params;
  return <BusinessWorkspace businessId={Number(businessId)} />;
}
