import { ArrowLeft, FileQuestion } from "lucide-react";
import Link from "next/link";
import { Wordmark } from "@/components/brand/wordmark";
export default function NotFound() { return <main className="tm-system-page"><Wordmark /><section><FileQuestion aria-hidden="true" /><h1>Ruta no encontrada</h1><p>La página o el módulo solicitado no existe (error 404).</p><Link className="tm-button tm-button--primary" href="/"><ArrowLeft aria-hidden="true" /> Volver al inicio</Link></section></main>; }
