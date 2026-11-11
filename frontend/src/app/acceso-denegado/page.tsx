import { ArrowLeft, ShieldX } from "lucide-react";
import Link from "next/link";
import { Wordmark } from "@/components/brand/wordmark";
export default function AccessDeniedPage() {
  return (
    <main className="tm-system-page">
      <Wordmark />
      <section>
        <ShieldX aria-hidden="true" />
        <h1>Acceso denegado</h1>
        <p>No tienes permiso para acceder a esta sección.</p>
        <Link className="tm-button tm-button--primary" href="/app">
          <ArrowLeft aria-hidden="true" /> Volver al inicio
        </Link>
      </section>
    </main>
  );
}
