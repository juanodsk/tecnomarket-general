# TecMarket frontend

Frontend de TecMarket construido con Next.js 16 App Router, TypeScript, HeroUI v3 y Tailwind CSS.

## Requisitos

- Node.js 20 o superior.
- Backend TecMarket accesible desde el navegador.
- El origen del frontend autorizado en la configuración CORS del backend.

## Configuración

1. Copia `.env.example` como `.env.local`.
2. Ajusta `NEXT_PUBLIC_API_URL` si usarás otro entorno.
3. Instala dependencias con `npm install`.
4. Inicia el entorno local con `npm run dev`.

El valor de desarrollo incluido es `https://api-dev.juanodsk.dev`. La cookie `tm_refresh` es creada por el backend y el access token se mantiene únicamente en memoria.

## Comandos

```bash
npm run dev
npm run build
npm run lint
npm test
npm run test:e2e
```

## Alcance de la fase 1

- Registro, verificación de correo, login y recuperación de contraseña.
- Restauración de sesión, perfil, cambio de contraseña, cierre y revocación de sesiones.
- Selección segura de negocio y sede.
- Shell protegido para negocio y administración global.
- Normalización central de errores y refresh concurrente de promesa única.

Los módulos operativos todavía no están expuestos en el menú; se incorporarán por recorridos verticales en fases posteriores.
