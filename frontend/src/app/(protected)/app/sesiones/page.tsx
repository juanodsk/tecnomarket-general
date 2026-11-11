import type { Metadata } from "next";
import { SessionsPanel } from "@/components/account/account-panels";
export const metadata: Metadata = { title: "Sesiones" };
export default function SessionsPage() { return <SessionsPanel />; }
