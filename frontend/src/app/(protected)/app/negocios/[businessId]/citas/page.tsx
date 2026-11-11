import { BusinessAppointmentsWorkspace } from "@/components/business/business-appointments";

export default async function BusinessAppointmentsPage({
  params,
}: PageProps<"/app/negocios/[businessId]/citas">) {
  const { businessId } = await params;

  return <BusinessAppointmentsWorkspace businessId={Number(businessId)} />;
}
