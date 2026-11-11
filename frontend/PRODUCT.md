# Product

<!-- impeccable:product-schema 1 -->

## Platform

web

## Users

- Personas que crean y administran su cuenta en TecMarket.
- Propietarios, administradores, recepcionistas, tecnicos y vendedores que operan uno o varios negocios de servicio tecnico.
- Administradores globales que gestionan la plataforma.
- Clientes que consultan el estado de sus reparaciones; su portal completo se implementara en una fase posterior.

## Product Purpose

TecMarket combina un marketplace publico con un sistema interno para negocios de servicio tecnico. El frontend debe permitir autenticar usuarios y, de forma progresiva, operar solicitudes de negocio, sedes, servicios, clientes, dispositivos, ordenes de reparacion, evidencias, cotizaciones, inventario, ventas y reseñas consumiendo el backend existente.

## Positioning

La aplicacion conecta la confianza publica del cliente con la trazabilidad operativa del taller: la misma plataforma acompana la eleccion del negocio y el ciclo verificable de una reparacion.

## Operating Context

- El producto se usa en escritorios y moviles dentro de talleres con trabajo frecuente, interrupciones y multiples actores.
- Un usuario puede pertenecer a varios negocios y acceder solamente a las sedes y permisos devueltos por el backend para el negocio activo.
- El `businessId` de la URL define el tenant visible; el backend conserva la autoridad final sobre permisos y sedes.

## Capabilities and Constraints

- Next.js App Router, TypeScript, HeroUI v3 y Tailwind CSS.
- Backend Spring Boot existente bajo `/api/v1`; no se inventan endpoints, DTOs, permisos ni estados.
- Access token unicamente en memoria y refresh token en cookie `tm_refresh` HttpOnly administrada por el backend.
- Respuestas envueltas en `ApiResponse<T>` y errores HTTP tratados de forma centralizada.
- La primera fase cubre identidad completa, restauracion de sesion, contexto multinegocio minimo, proteccion de rutas y el shell inicial.
- Interfaz y URLs visibles en espanol.
- Temas claro y oscuro desde la primera fase.

## Brand Commitments

- Nombre confirmado: TecMarket.
- Voz clara, profesional y operativa, sin promesas comerciales inventadas.
- Direccion aprobada para la primera fase: una interfaz sobria y precisa inspirada en una mesa de diagnostico tecnico.
- El archivo de Figma enlazado no contiene todavia frames utilizables; cuando existan, se alineara la implementacion sin atribuirle decisiones inexistentes.

## Evidence on Hand

- Contrato de endpoints y DTOs en el backend y su catalogo QA.
- Starter local de Next.js con HeroUI, Tailwind CSS e Inter.
- No hay logotipo, fotografias, metricas, testimonios ni material comercial confirmado; no deben fabricarse.

## Product Principles

- El backend es la autoridad de seguridad y negocio.
- Ningun cambio de negocio o sede puede filtrar contexto anterior.
- Cada recorrido vertical debe quedar usable, verificable y visualmente terminado antes de abrir el siguiente.
- Los errores deben explicar la accion siguiente sin borrar el trabajo del usuario.
- La interfaz debe priorizar lectura rapida, foco visible y operacion consistente en escritorio y movil.

## Accessibility & Inclusion

La primera fase debe soportar navegacion por teclado, foco visible, etiquetas accesibles, mensajes anunciables, contraste suficiente y diseno responsivo en ambos temas.
