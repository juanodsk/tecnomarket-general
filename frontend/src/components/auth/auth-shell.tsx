import type { ReactNode } from "react";

import { Wordmark } from "@/components/brand/wordmark";
import { ThemeToggle } from "@/components/ui/theme-toggle";

export function AuthShell({
  title,
  description,
  children,
}: {
  title: string;
  description: string;
  children: ReactNode;
}) {
  return (
    <main className="tm-auth-shell">
      <header className="tm-auth-header">
        <Wordmark />
        <ThemeToggle />
      </header>
      <section className="tm-auth-grid">
        <aside className="tm-auth-aside" aria-label="Flujo de acceso seguro">
          <h2>Una sesión clara antes de tocar una orden.</h2>
          <p>
            Ingresa de forma segura y continúa directamente con la información, los negocios y las
            sedes disponibles para tu cuenta.
          </p>
        </aside>
        <div className="tm-auth-panel">
          <div className="tm-auth-panel__heading">
            <h1>{title}</h1>
            <p>{description}</p>
          </div>
          {children}
        </div>
      </section>
    </main>
  );
}
