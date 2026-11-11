import { ArrowRight, CheckCircle2, ClipboardCheck, Gauge, Moon, ScanLine } from "lucide-react";
import Link from "next/link";

import { Wordmark } from "@/components/brand/wordmark";
import { ThemeToggle } from "@/components/ui/theme-toggle";

export default function HomePage() {
  return (
    <main className="tm-public">
      <header className="tm-public-nav"><Wordmark /><nav aria-label="Acceso"><ThemeToggle /><Link className="tm-link-button" href="/login">Ingresar</Link></nav></header>
      <section className="tm-hero">
        <div className="tm-hero__copy">
          <h1>Cada reparación necesita una historia verificable.</h1>
          <p className="tm-hero__lead">TecMarket reúne la información del negocio, sus sedes y el acceso de cada usuario antes de iniciar la operación.</p>
          <div className="tm-hero__actions"><Link className="tm-button tm-button--primary" href="/registro">Crear cuenta <ArrowRight aria-hidden="true" /></Link><Link className="tm-button tm-button--secondary" href="/login">Ingresar al panel</Link></div>
          <ul className="tm-hero__facts" aria-label="Base funcional disponible"><li><CheckCircle2 aria-hidden="true" /> Identidad protegida</li><li><CheckCircle2 aria-hidden="true" /> Operación multinegocio</li><li><CheckCircle2 aria-hidden="true" /> Accesos por rol</li></ul>
        </div>
        <div className="tm-console" aria-label="Vista ilustrativa de un flujo de diagnóstico">
          <div className="tm-console__head"><div><span /><span /><span /></div><small>VISTA ILUSTRATIVA · DIAGNÓSTICO</small><Gauge aria-hidden="true" /></div>
          <div className="tm-console__body">
            <aside><span className="is-active"><ScanLine aria-hidden="true" /> Recepción</span><span><ClipboardCheck aria-hidden="true" /> Pruebas</span><span><Moon aria-hidden="true" /> Evidencias</span></aside>
            <div className="tm-console__workspace">
              <div className="tm-console__title"><span>OT-000184</span><strong>Equipo recibido</strong><small>Acceso verificado</small></div>
              <div className="tm-device-map"><div className="tm-device-map__screen" /><i /><i /><i /><i /><span>INSPECCIÓN VISUAL</span></div>
              <div className="tm-console__checklist"><p><span>01</span> Estado exterior <b>Registrado</b></p><p><span>02</span> Accesorios recibidos <b>Verificado</b></p><p><span>03</span> Evidencia inicial <b>Pendiente</b></p></div>
            </div>
          </div>
        </div>
      </section>
      <section className="tm-foundation"><header><h2>Acceso antes que operación.</h2></header><div><article><span>01</span><h3>Sesión</h3><p>Access token en memoria y renovación mediante cookie segura.</p></article><article><span>02</span><h3>Negocio</h3><p>Selección explícita sin mezclar información entre empresas.</p></article><article><span>03</span><h3>Sede</h3><p>Solo se muestran las sedes autorizadas por el backend.</p></article></div></section>
    </main>
  );
}
