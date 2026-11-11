import type { ReactNode } from "react";
import { AlertTriangle, CheckCircle2, LoaderCircle, RotateCcw } from "lucide-react";
import { Button } from "@heroui/react";

export function FullPageLoader({ label = "Preparando tu espacio de trabajo" }: { label?: string }) {
  return (
    <main className="tm-full-loader" aria-busy="true" aria-live="polite">
      <span className="tm-loader-mark" aria-hidden="true">
        <LoaderCircle className="tm-spinner" />
      </span>
      <p>{label}</p>
    </main>
  );
}

export function FormAlert({
  tone = "error",
  children,
}: {
  tone?: "error" | "success" | "warning";
  children: ReactNode;
}) {
  return (
    <div className={`tm-alert tm-alert--${tone}`} role={tone === "error" ? "alert" : "status"}>
      {tone === "success" ? <CheckCircle2 aria-hidden="true" /> : <AlertTriangle aria-hidden="true" />}
      <div>{children}</div>
    </div>
  );
}

export function ErrorState({
  title = "No pudimos cargar esta información",
  message,
  onRetry,
}: {
  title?: string;
  message: string;
  onRetry?: () => void;
}) {
  return (
    <section className="tm-state tm-state--error" role="alert">
      <AlertTriangle aria-hidden="true" />
      <div>
        <h2>{title}</h2>
        <p>{message}</p>
      </div>
      {onRetry ? (
        <Button className="tm-button tm-button--secondary" type="button" onPress={onRetry}>
          <RotateCcw aria-hidden="true" /> Reintentar
        </Button>
      ) : null}
    </section>
  );
}

export function EmptyState({
  title,
  message,
  action,
}: {
  title: string;
  message: string;
  action?: ReactNode;
}) {
  return (
    <section className="tm-state">
      <span className="tm-state__index" aria-hidden="true">—</span>
      <div>
        <h2>{title}</h2>
        <p>{message}</p>
      </div>
      {action}
    </section>
  );
}
