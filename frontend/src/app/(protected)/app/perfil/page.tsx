import type { Metadata } from "next";
import { ProfilePanel } from "@/components/account/account-panels";
export const metadata: Metadata = { title: "Perfil" };
export default function ProfilePage() { return <ProfilePanel />; }
