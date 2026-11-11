import { BusinessRepairOrdersWorkspace } from "@/components/business/business-repair-orders";

export default async function BusinessRepairOrdersPage({
  params,
}: PageProps<"/app/negocios/[businessId]/ordenes">) {
  const { businessId } = await params;

  return <BusinessRepairOrdersWorkspace businessId={Number(businessId)} />;
}
