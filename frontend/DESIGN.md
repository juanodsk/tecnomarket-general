---
name: TecMarket
description: Interfaz operativa precisa inspirada en una mesa de diagnóstico técnico.
colors:
  diagnostic-blue: "#2458d8"
  diagnostic-blue-hover: "#1948bd"
  diagnostic-blue-soft: "#eaf0ff"
  bench-background: "#f4f6f8"
  work-surface: "#ffffff"
  raised-surface: "#fbfcfd"
  technical-ink: "#111827"
  secondary-ink: "#526278"
  divider: "#dbe1e8"
  strong-divider: "#b8c2cf"
  verified: "#087f6b"
  verified-soft: "#e2f5f0"
  warning: "#a85b00"
  warning-soft: "#fff3d6"
  danger: "#c83243"
  danger-soft: "#fff0f2"
  night-background: "#0d1117"
  night-surface: "#141a22"
  night-raised-surface: "#19212b"
  night-ink: "#eef2f7"
  night-secondary-ink: "#9aa8b8"
typography:
  display:
    fontFamily: "Inter, sans-serif"
    fontSize: "clamp(2.7rem, 5.8vw, 5.4rem)"
    fontWeight: 760
    lineHeight: 0.98
    letterSpacing: "-0.04em"
  headline:
    fontFamily: "Inter, sans-serif"
    fontSize: "clamp(1.75rem, 3vw, 2.7rem)"
    fontWeight: 700
    lineHeight: 1.1
    letterSpacing: "-0.04em"
  body:
    fontFamily: "Inter, sans-serif"
    fontSize: "1rem"
    fontWeight: 400
    lineHeight: 1.65
  label:
    fontFamily: "Inter, sans-serif"
    fontSize: "0.75rem"
    fontWeight: 730
    lineHeight: 1.4
rounded:
  control-sm: "5px"
  control: "7px"
  surface: "8px"
  panel: "10px"
  pill: "999px"
spacing:
  xs: "4px"
  sm: "8px"
  md: "16px"
  lg: "24px"
  xl: "32px"
  section: "56px"
components:
  button-primary:
    backgroundColor: "{colors.diagnostic-blue}"
    textColor: "{colors.work-surface}"
    rounded: "{rounded.control}"
    padding: "12px 16px"
    height: "44px"
  button-primary-hover:
    backgroundColor: "{colors.diagnostic-blue-hover}"
    textColor: "{colors.work-surface}"
  button-secondary:
    backgroundColor: "{colors.work-surface}"
    textColor: "{colors.technical-ink}"
    rounded: "{rounded.control}"
    padding: "12px 16px"
    height: "44px"
  text-field:
    backgroundColor: "{colors.raised-surface}"
    textColor: "{colors.technical-ink}"
    rounded: "6px"
    padding: "0 12px"
    height: "46px"
  status-verified:
    backgroundColor: "{colors.verified-soft}"
    textColor: "{colors.verified}"
    rounded: "{rounded.pill}"
    padding: "8px 11px"
---

# Design System: TecMarket

## Overview

**Creative North Star: "La mesa de diagnóstico"**

TecMarket se siente como una estación de trabajo técnica: precisa, sobria y preparada para inspeccionar información sin ruido. La interfaz prioriza trazabilidad, contexto y seguridad mediante superficies claras, divisores estructurales, estados explícitos y un azul de diagnóstico usado con moderación.

La densidad es operativa, no decorativa. Las composiciones amplias respiran, mientras formularios, selectores y paneles mantienen un ritmo compacto. El tema oscuro conserva la misma jerarquía sin invertir el carácter del producto.

**Key Characteristics:**

- Geometría rectangular con esquinas discretamente suavizadas.
- Una sola familia tipográfica, Inter, con jerarquía por escala y peso.
- Azul para acción y foco; verde exclusivamente para estados verificados.
- Contexto de negocio, sede y progreso siempre visible cuando afecta la operación.
- Movimiento corto, funcional y compatible con reducción de movimiento.

## Colors

La paleta combina neutrales fríos de banco de trabajo con un azul técnico y estados semánticos inequívocos.

### Primary

- **Azul de diagnóstico:** acción primaria, foco, selección y progreso actual.
- **Azul de diagnóstico suave:** fondo de navegación activa y énfasis de bajo contraste.

### Secondary

- **Verde verificado:** confirmaciones y contexto empresarial validado.
- **Ámbar preventivo:** estados que requieren atención sin implicar error.
- **Rojo de bloqueo:** errores, acciones destructivas y sesión invalidada.

### Neutral

- **Fondo de banco:** lienzo general del tema claro.
- **Superficie de trabajo:** paneles, formularios y navegación.
- **Tinta técnica:** texto principal y marca tipográfica.
- **Tinta secundaria:** texto de apoyo que mantiene contraste AA.
- **Divisor y divisor fuerte:** estructura, agrupación y límites interactivos.

**The Semantic Signal Rule.** El azul indica acción o foco, el verde confirma y el rojo bloquea; no intercambiar estos significados por decoración.

## Typography

**Display Font:** Inter (con fallback sans-serif)  
**Body Font:** Inter (con fallback sans-serif)

**Character:** Una grotesca contemporánea y legible mantiene el producto técnico sin volverlo frío. La jerarquía depende de peso, escala y espacio; no de etiquetas ornamentales.

### Hierarchy

- **Display** (760, escala fluida, 0.98): mensajes públicos principales.
- **Headline** (700, escala fluida, 1.1): títulos de páginas, paneles y estados.
- **Title** (650–720, 0.82–1.15rem): secciones y elementos de listas.
- **Body** (400, 1rem, 1.65): explicaciones; mantener líneas legibles y compactas.
- **Label** (730, 0.75rem, 1.4): controles y metadatos funcionales.

**The Clear Heading Rule.** Los títulos empiezan directamente con el contenido; no anteponer eyebrows, kickers ni numeración decorativa.

## Layout

El contenedor principal alcanza 1180px y deja márgenes fluidos. La aplicación protegida usa una barra lateral de 248px y un lienzo flexible; por debajo de 900px la navegación pasa a un drawer controlado. A 620px, formularios y métricas colapsan a una o dos columnas, pero los selectores de negocio y sede permanecen disponibles.

El espaciado sigue un ritmo base de 8px. Las secciones amplias usan 24–56px; controles y listas usan 4–16px. El `businessId` visible y la sede seleccionada son contexto funcional, no metadatos prescindibles.

## Elevation & Depth

El sistema es plano por defecto. Bordes fríos y cambios tonales separan niveles; la sombra ambiental se reserva para paneles destacados como la vista ilustrativa y el formulario de autenticación.

### Shadow Vocabulary

- **Elevación de inspección** (`0 20px 60px rgb(20 31 48 / 8%)`): paneles protagonistas sobre el fondo claro.
- **Elevación nocturna** (`0 24px 70px rgb(0 0 0 / 28%)`): el mismo rol dentro del tema oscuro.

**The Flat Workbench Rule.** Las superficies operativas permanecen planas; una sombra debe comunicar jerarquía real, nunca adornar una tarjeta común.

## Shapes

Los bordes son finos y las esquinas se mantienen entre 5px y 10px. Los controles usan 6–7px, los paneles 8–10px y las píldoras se reservan para estados. No usar tarjetas excesivamente redondeadas ni formas flotantes sin anclaje estructural.

## Components

### Buttons

- **Shape:** control compacto y firme (7px), altura mínima de 44px.
- **Primary:** azul de diagnóstico con texto claro; en oscuro usa tinta nocturna para contraste.
- **Hover / Focus:** cambio al azul profundo y anillo visible de 3px; la activación baja 1px.
- **Secondary:** superficie blanca, borde fuerte y texto técnico; el hover adopta azul.

### Chips

- **Style:** píldora solo para estados compactos; fondo semántico suave y borde mezclado.
- **State:** el texto siempre nombra el estado, nunca depende únicamente del color.

### Cards / Containers

- **Corner Style:** rectángulo técnico, normalmente 0–10px.
- **Background:** superficie de trabajo o superficie elevada.
- **Shadow Strategy:** plana salvo panel protagonista.
- **Border:** divisor de 1px como estructura principal.
- **Internal Padding:** entre 16px y 32px según densidad.

### Inputs / Fields

- **Style:** fondo elevado, borde fuerte, radio de 6px y altura de 46px.
- **Focus:** borde azul y halo translúcido de 3px.
- **Error / Disabled:** rojo semántico para error; reducción de opacidad y cursor bloqueado para disabled.

### Navigation

La navegación lateral usa texto compacto, iconos lineales y estado activo sobre azul suave. En móvil el drawer queda `inert` cuando está cerrado, atrapa foco al abrirse, responde a Escape y devuelve el foco al disparador.

### Bootstrap Rail

Cinco pasos —Credencial, Perfil, Negocio, Contexto y Listo— hacen visible la restauración segura. Los pasos completos son verdes, el actual azul y el fallido rojo; la animación se reduce cuando el sistema lo solicita.

## Do's and Don'ts

### Do:

- **Do** mantener negocio y sede visibles en cualquier ancho cuando exista contexto empresarial.
- **Do** usar estados con texto, icono y color semántico.
- **Do** conservar foco visible, contraste AA y áreas interactivas cercanas a 44px.
- **Do** construir menús empresariales únicamente desde permisos del contexto activo.

### Don't:

- **Don't** usar números si no representan una secuencia, un identificador o un dato real.
- **Don't** introducir slogans comerciales, fotografías o logotipos no aprobados.
- **Don't** ocultar la sede en móvil ni reutilizar una sede ausente del contexto recibido.
- **Don't** almacenar el access token ni tokens de correo en almacenamiento persistente.
