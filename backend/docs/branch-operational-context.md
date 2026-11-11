# Contexto operativo por sede

## Responsabilidades

- `Business`: tenant, configuracion, catalogos, clientes y suscripcion.
- `BusinessBranch`: sede fisica y contexto de la operacion diaria.
- El dueño y el administrador del negocio pueden consolidar todas las sedes.
- Los demas empleados solo acceden a las sedes registradas en `business_user_branch`.

## Datos operativos por sede

Los siguientes recursos conservan `business_id` como frontera del tenant y agregan `branch_id`:

- ordenes de reparacion;
- citas;
- inventario;
- movimientos de stock;
- ventas;
- horarios.

Mantener ambos identificadores permite validar que la sede pertenece al negocio, filtrar con eficiencia y generar reportes consolidados.

## Numero de orden

Formato:

```text
OS-{BUSINESS_ID_3}-{BRANCH_NUMBER_2}-{SEQUENCE_6}
```

Ejemplo:

```text
OS-001-01-000101
```

- `001`: identificador del negocio.
- `01`: consecutivo estable de la sede dentro del negocio (`branch_number`).
- `000101`: consecutivo de la orden dentro de la sede.

La secuencia se administra por la pareja `(business_id, branch_id)`, por lo que cada sede tiene su propio consecutivo.

## Seguimiento para clientes

Endpoint recomendado e implementado:

```http
GET /api/v1/me/repair-orders/{orderNumber}
Authorization: Bearer {customerToken}
```

El numero de orden es amigable para buscar, pero no funciona como credencial. El backend exige autenticacion y solo devuelve la orden si pertenece al usuario autenticado.

La respuesta de seguimiento excluye informacion interna como diagnostico, costos, notas privadas y datos de empleados.

Si posteriormente se necesita consulta sin iniciar sesion, debe usarse un segundo codigo aleatorio de seguimiento y rate limiting; nunca debe exponerse una orden usando solamente su numero consecutivo.
