"use client";
import { useEffect } from "react";
import { ErrorState } from "@/components/ui/feedback";
export default function GlobalError({ error, reset }: { error: Error & { digest?: string }; reset: () => void }) { useEffect(() => { console.error(error); }, [error]); return <main className="tm-system-page"><ErrorState title="TecMarket encontró un problema" message="No fue posible mostrar esta pantalla. Puedes intentar cargarla nuevamente." onRetry={reset} /></main>; }
