# Identidad y sesiones

## Modelo de invalidación

- `app_user.security_version` es la versión global de seguridad del usuario.
- Cada JWT incluye `security_version` y `sid`.
- `sid` identifica la familia de refresh tokens que originó el access token.
- En cada request autenticado se valida que el usuario siga activo, que la versión
  coincida y que la familia `sid` continúe activa y sin expirar.
- Cada refresh token conserva `issued_security_version`; una rotación con una versión
  anterior se rechaza incluso ante concurrencia con una revocación.

La versión aumenta y todos los refresh tokens se revocan cuando cambia o se recupera
la contraseña, cambia el estado de la cuenta, cambian roles/permisos efectivos o se
ejecuta una revocación total. La revocación individual, logout y detección de replay
revocan la familia y, por medio de `sid`, invalidan sus access tokens inmediatamente.

## Endpoints

| Método | Ruta | Efecto |
|--------|------|--------|
| POST | `/api/v1/auth/password/change` | Cambia la contraseña y cierra todas las sesiones. |
| DELETE | `/api/v1/auth/sessions` | Revoca todas las sesiones del usuario autenticado. |
| DELETE | `/api/v1/auth/sessions/{sessionId}` | Revoca una familia y sus access tokens. |
| PATCH | `/api/v1/parameters/users/{id}/status` | Activa/desactiva la cuenta e invalida sus sesiones. |
| DELETE | `/api/v1/parameters/users/{id}/sessions` | Revocación total administrativa. |

## Rate limit distribuido

Los endpoints sensibles mantienen contadores Redis con ventana de 60 segundos para:

- IP global y operación + IP.
- Correo normalizado y hasheado, global y por operación.
- Combinación IP + cuenta, global y por operación.
- Volumen global de cada operación.

Los correos nunca forman parte legible de las claves Redis. Un JSON de autenticación
mayor a 16 KiB se rechaza antes de almacenarlo en memoria. En producción Redis es
obligatorio y cualquier indisponibilidad falla de forma cerrada.

## MFA aplazado

MFA TOTP y sus códigos de recuperación quedan fuera de este cambio por decisión de
alcance. Los códigos de recuperación no se implementan aislados porque solo tienen
sentido como mecanismo alternativo a un segundo factor habilitado. TOTP no requiere
SMS ni un servicio móvil pagado; puede añadirse posteriormente con aplicaciones
Authenticator compatibles con el estándar.
