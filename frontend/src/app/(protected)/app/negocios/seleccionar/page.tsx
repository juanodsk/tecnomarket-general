import type { Metadata } from "next";
import { BusinessSelection } from "@/components/business/business-views";
export const metadata: Metadata = { title: "Seleccionar negocio" };
export default function BusinessSelectionPage() { return <BusinessSelection />; }
