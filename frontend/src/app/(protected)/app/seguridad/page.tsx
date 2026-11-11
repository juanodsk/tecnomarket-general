import type { Metadata } from "next";
import { SecurityPanel } from "@/components/account/account-panels";
export const metadata: Metadata = { title: "Seguridad" };
export default function SecurityPage() { return <SecurityPanel />; }
