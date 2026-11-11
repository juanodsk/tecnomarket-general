import { expect, test, type Page, type Route } from "@playwright/test";

const admin = {
  id: 90,
  documentTypeCode: "CC",
  documentNumber: "9001",
  name: "Admin",
  surname: "Plataforma",
  email: "admin@example.com",
  phone: "3000000000",
  countryId: 1,
  countryName: "Colombia",
  departmentId: 2,
  departmentName: "Huila",
  cityId: 3,
  cityName: "Neiva",
};

function businessRequest(id: number, name: string) {
  return {
    id,
    documentTypeCode: "NIT",
    documentNumber: `900100${id}`,
    businessName: name,
    legalName: `${name} S.A.S.`,
    email: `contacto${id}@example.com`,
    phone: "+573001112233",
    cityId: 3,
    cityName: "Neiva",
    address: "Calle 12 # 3-45",
    legalFile: "https://example.com/documento.pdf",
    status: "PENDING",
    requestedByUserId: id + 100,
    requestedByUserName: `Propietario ${id}`,
    reviewedByUserId: null,
    reviewedByUserName: null,
    reviewedAt: null,
    rejectionReason: null,
    createdAt: "2026-09-13T14:30:00",
  };
}

function business(id: number, name: string, active = true) {
  return {
    id,
    documentTypeCode: "NIT",
    documentNumber: `901200${id}`,
    name,
    legalName: `${name} S.A.S.`,
    email: `negocio${id}@example.com`,
    phone: "+573008887766",
    address: "Carrera 5 # 10-20",
    description: id === 1 ? "Servicio técnico especializado en dispositivos móviles." : null,
    cityId: 3,
    cityName: "Neiva",
    logo: null,
    coverImage: null,
    ratingAverage: id === 1 ? 4.7 : 0,
    ratingCount: id === 1 ? 24 : 0,
    active,
  };
}

function platformUser(id: number, name: string, surname: string, emailVerified = true) {
  return {
    id,
    documentTypeCode: "CC",
    documentTypeName: "Cédula de ciudadanía",
    documentNumber: `107500${id}`,
    name,
    surname,
    email: `${name.toLowerCase()}${id}@example.com`,
    phone: "+573005554433",
    address: "Calle 8 # 14-30",
    countryId: 1,
    countryName: "Colombia",
    departmentId: 2,
    departmentName: "Huila",
    cityId: 3,
    cityName: "Neiva",
    active: true,
    emailVerified,
    createdAt: "2026-08-10T09:30:00",
    updatedAt: "2026-09-13T14:30:00",
  };
}

async function fulfill(route: Route, data: unknown, status = 200, message = "ok") {
  await route.fulfill({ status, contentType: "application/json", body: JSON.stringify({ success: status < 400, message, data }) });
}

async function mockPlatform(page: Page, initialRequests = [businessRequest(1, "Tecno Centro"), businessRequest(2, "Móvil Lab")]) {
  let pending = [...initialRequests];
  const businesses = [business(1, "Tecno Centro"), business(2, "Móvil Lab", false)];
  let users = [platformUser(91, "María", "Gómez"), platformUser(92, "Carlos", "Rojas", false)];
  let userRoles = [
    { roleId: 1, code: "PLATFORM_ADMIN", name: "Administrador de plataforma", description: "Administra la operación global.", scope: "GLOBAL", assigned: false },
    { roleId: 2, code: "CUSTOMER", name: "Cliente", description: "Acceso general como cliente.", scope: "GLOBAL", assigned: true },
  ];
  let roles = [
    { id: 1, code: "PLATFORM_ADMIN", name: "Administrador de plataforma", description: "Administra la operación global.", scope: "GLOBAL", active: true },
    { id: 2, code: "TECHNICIAN", name: "Técnico", description: "Diagnostica y repara equipos del negocio.", scope: "BUSINESS", active: true },
    { id: 3, code: "LEGACY_ROLE", name: "Rol anterior", description: "Rol conservado para revisión.", scope: "BUSINESS", active: false },
  ];
  const permissions = [
    { id: 10, moduleId: 1, moduleCode: "PARAMETER", moduleName: "Configuración global", code: "PARAMETER_VIEW", name: "Consultar configuración", description: "Permite consultar catálogos globales.", active: true },
    { id: 11, moduleId: 2, moduleCode: "INVENTORY", moduleName: "Inventario", code: "INVENTORY_VIEW", name: "Consultar inventario", description: "Permite consultar productos y repuestos.", active: true },
    { id: 12, moduleId: 2, moduleCode: "INVENTORY", moduleName: "Inventario", code: "INVENTORY_UPDATE", name: "Actualizar inventario", description: "Permite modificar productos y existencias.", active: false },
  ];
  let countries = [
    { id: 1, code: "CO", name: "Colombia", active: true },
    { id: 4, code: "EC", name: "Ecuador", active: false },
  ];
  let departments = [
    { id: 2, countryId: 1, countryCode: "CO", countryName: "Colombia", code: "HUI", name: "Huila", active: true },
    { id: 5, countryId: 1, countryCode: "CO", countryName: "Colombia", code: "CUN", name: "Cundinamarca", active: true },
  ];
  let cities = [
    { id: 3, countryId: 1, countryCode: "CO", countryName: "Colombia", departmentId: 2, departmentCode: "HUI", departmentName: "Huila", name: "Neiva", active: true },
    { id: 6, countryId: 1, countryCode: "CO", countryName: "Colombia", departmentId: 5, departmentCode: "CUN", departmentName: "Cundinamarca", name: "Soacha", active: true },
  ];
  let documentTypes = [
    { id: 1, code: "CC", name: "Cédula de ciudadanía", description: "Documento de identidad colombiano", appliesToPerson: true, appliesToCompany: false, active: true },
    { id: 2, code: "NIT", name: "Número de identificación tributaria", description: "Identificación fiscal de empresas", appliesToPerson: false, appliesToCompany: true, active: true },
  ];
  let modules = [
    { id: 1, code: "PARAMETER", name: "Configuración global", active: true },
    { id: 2, code: "INVENTORY", name: "Inventario", active: true },
  ];
  const subscriptions = [
    { id: 41, businessId: 1, businessName: "Tecno Centro", planId: 7, planCode: "PRO", planName: "Pro", status: "ACTIVE", billingCycle: "MONTHLY", currentPeriodStart: "2026-09-01T00:00:00", currentPeriodEnd: "2026-10-01T00:00:00", trialEndsAt: null, cancelAtPeriodEnd: false, paymentProvider: "WOMPI", providerCustomerId: "cus_tecno", providerSubscriptionId: "sub_tecno", active: true, createdAt: "2026-08-01T09:00:00", updatedAt: "2026-09-01T09:00:00" },
    { id: 42, businessId: 2, businessName: "Móvil Lab", planId: 6, planCode: "STARTER", planName: "Inicial", status: "PAST_DUE", billingCycle: "YEARLY", currentPeriodStart: "2026-01-15T00:00:00", currentPeriodEnd: "2027-01-15T00:00:00", trialEndsAt: null, cancelAtPeriodEnd: true, paymentProvider: "MANUAL", providerCustomerId: null, providerSubscriptionId: null, active: true, createdAt: "2026-01-15T08:00:00", updatedAt: "2026-09-12T11:00:00" },
  ];
  const subscriptionUsage = new Map([
    [41, { id: 51, businessSubscriptionId: 41, usersCount: 4, ordersCount: 120, branchesCount: 2, storageUsed: 157286400, updatedAt: "2026-09-14T10:30:00" }],
    [42, { id: 52, businessSubscriptionId: 42, usersCount: 2, ordersCount: 18, branchesCount: 1, storageUsed: 0, updatedAt: "2026-09-13T08:00:00" }],
  ]);
  const rolePermissionOptions = new Map<number, Array<Record<string, unknown>>>([
    [1, [{ permissionId: 10, code: "PARAMETER_VIEW", name: "Consultar configuración", description: "Permite consultar catálogos globales.", scope: "GLOBAL", moduleId: 1, moduleCode: "PARAMETER", moduleName: "Configuración global", assigned: true }]],
    [2, [
      { permissionId: 11, code: "INVENTORY_VIEW", name: "Consultar inventario", description: "Permite consultar productos y repuestos.", scope: "BUSINESS", moduleId: 2, moduleCode: "INVENTORY", moduleName: "Inventario", assigned: true },
      { permissionId: 12, code: "INVENTORY_UPDATE", name: "Actualizar inventario", description: "Permite modificar productos y existencias.", scope: "BUSINESS", moduleId: 2, moduleCode: "INVENTORY", moduleName: "Inventario", assigned: false },
    ]],
  ]);
  let rejectAttempts = 0;

  await page.route("https://api-dev.juanodsk.dev/**", async (route) => {
    const url = new URL(route.request().url());
    const path = url.pathname;
    if (path === "/api/v1/auth/refresh") return fulfill(route, { accessToken: "platform-token", user: admin });
    if (path === "/api/v1/auth/me") return fulfill(route, admin);
    if (path === "/api/v1/auth/me/permissions") return fulfill(route, { userId: admin.id, roles: ["PLATFORM_ADMIN"], permissions: ["BUSINESS_REQUEST_REVIEW", "BUSINESS_VIEW", "PARAMETER_VIEW", "PARAMETER_MANAGE", "SUBSCRIPTION_VIEW", "SUBSCRIPTION_MANAGE"] });
    if (path === "/api/v1/businesses/my") return fulfill(route, []);
    if (path === "/api/v1/parameters/users") {
      return fulfill(route, { items: users, page: 0, size: Number(url.searchParams.get("size") ?? 20), totalElements: users.length, totalPages: users.length ? 1 : 0, hasNext: false, hasPrevious: false });
    }
    const userRolesMatch = path.match(/^\/api\/v1\/parameters\/users\/(\d+)\/roles$/);
    if (userRolesMatch) {
      if (route.request().method() === "PUT") {
        const body = route.request().postDataJSON() as { roleIds: number[] };
        userRoles = userRoles.map((role) => ({ ...role, assigned: body.roleIds.includes(role.roleId) }));
      }
      return fulfill(route, userRoles);
    }
    const userStatusMatch = path.match(/^\/api\/v1\/parameters\/users\/(\d+)\/status$/);
    if (userStatusMatch) {
      const id = Number(userStatusMatch[1]);
      const selected = users.find((item) => item.id === id);
      const body = route.request().postDataJSON() as { active: boolean };
      users = body.active ? users : users.filter((item) => item.id !== id);
      return fulfill(route, selected ? { ...selected, active: body.active } : null);
    }
    if (/^\/api\/v1\/parameters\/users\/\d+\/sessions$/.test(path)) return fulfill(route, null);
    const userMatch = path.match(/^\/api\/v1\/parameters\/users\/(\d+)$/);
    if (userMatch) {
      const selected = users.find((item) => item.id === Number(userMatch[1]));
      return selected ? fulfill(route, selected) : fulfill(route, null, 404, "El usuario no existe.");
    }
    if (path === "/api/v1/parameters/countries") {
      if (route.request().method() === "POST") {
        const body = route.request().postDataJSON() as { code: string; name: string };
        const created = { id: 30, ...body, active: true }; countries = [...countries, created];
        return fulfill(route, created, 201, "País creado.");
      }
      return fulfill(route, countries);
    }
    const countryMatch = path.match(/^\/api\/v1\/parameters\/countries\/(\d+)$/);
    if (countryMatch) {
      const id = Number(countryMatch[1]); const selected = countries.find((item) => item.id === id);
      if (!selected) return fulfill(route, null, 404, "El país no existe.");
      if (route.request().method() === "DELETE") { countries = countries.filter((item) => item.id !== id); return fulfill(route, null); }
      if (route.request().method() === "PUT") { const body = route.request().postDataJSON() as { name: string; active?: boolean }; const updated = { ...selected, ...body }; countries = countries.map((item) => item.id === id ? updated : item); return fulfill(route, updated); }
      return fulfill(route, selected);
    }
    if (path === "/api/v1/parameters/departments") {
      if (route.request().method() === "POST") {
        const body = route.request().postDataJSON() as { countryId: number; code: string; name: string };
        const country = countries.find((item) => item.id === body.countryId)!;
        const created = { id: 31, ...body, countryCode: country.code, countryName: country.name, active: true }; departments = [...departments, created];
        return fulfill(route, created, 201, "Departamento creado.");
      }
      return fulfill(route, departments);
    }
    const departmentMatch = path.match(/^\/api\/v1\/parameters\/departments\/(\d+)$/);
    if (departmentMatch) {
      const id = Number(departmentMatch[1]); const selected = departments.find((item) => item.id === id);
      if (!selected) return fulfill(route, null, 404, "El departamento no existe.");
      if (route.request().method() === "DELETE") { departments = departments.filter((item) => item.id !== id); return fulfill(route, null); }
      if (route.request().method() === "PUT") { const body = route.request().postDataJSON() as { countryId: number; code: string; name: string; active?: boolean }; const country = countries.find((item) => item.id === body.countryId)!; const updated = { ...selected, ...body, countryCode: country.code, countryName: country.name }; departments = departments.map((item) => item.id === id ? updated : item); return fulfill(route, updated); }
      return fulfill(route, selected);
    }
    if (path === "/api/v1/parameters/cities") {
      if (route.request().method() === "POST") {
        const body = route.request().postDataJSON() as { departmentId: number; name: string };
        const department = departments.find((item) => item.id === body.departmentId)!;
        const created = { id: 32, ...body, countryId: department.countryId, countryCode: department.countryCode, countryName: department.countryName, departmentCode: department.code, departmentName: department.name, active: true }; cities = [...cities, created];
        return fulfill(route, created, 201, "Ciudad creada.");
      }
      return fulfill(route, cities);
    }
    const cityMatch = path.match(/^\/api\/v1\/parameters\/cities\/(\d+)$/);
    if (cityMatch) {
      const id = Number(cityMatch[1]); const selected = cities.find((item) => item.id === id);
      if (!selected) return fulfill(route, null, 404, "La ciudad no existe.");
      if (route.request().method() === "DELETE") { cities = cities.filter((item) => item.id !== id); return fulfill(route, null); }
      if (route.request().method() === "PUT") { const body = route.request().postDataJSON() as { departmentId: number; name: string; active?: boolean }; const department = departments.find((item) => item.id === body.departmentId)!; const updated = { ...selected, ...body, countryId: department.countryId, countryCode: department.countryCode, countryName: department.countryName, departmentCode: department.code, departmentName: department.name }; cities = cities.map((item) => item.id === id ? updated : item); return fulfill(route, updated); }
      return fulfill(route, selected);
    }
    if (path === "/api/v1/parameters/document-types") {
      if (route.request().method() === "POST") { const body = route.request().postDataJSON() as Omit<(typeof documentTypes)[number], "id" | "active">; const created = { id: 33, ...body, active: true }; documentTypes = [...documentTypes, created]; return fulfill(route, created, 201, "Tipo de documento creado."); }
      return fulfill(route, documentTypes);
    }
    const documentMatch = path.match(/^\/api\/v1\/parameters\/document-types\/(\d+)$/);
    if (documentMatch) {
      const id = Number(documentMatch[1]); const selected = documentTypes.find((item) => item.id === id);
      if (!selected) return fulfill(route, null, 404, "El tipo de documento no existe.");
      if (route.request().method() === "DELETE") { documentTypes = documentTypes.filter((item) => item.id !== id); return fulfill(route, null); }
      if (route.request().method() === "PUT") { const body = route.request().postDataJSON() as Partial<(typeof documentTypes)[number]>; const updated = { ...selected, ...body }; documentTypes = documentTypes.map((item) => item.id === id ? updated : item); return fulfill(route, updated); }
      return fulfill(route, selected);
    }
    if (path === "/api/v1/parameters/modules") {
      if (route.request().method() === "POST") { const body = route.request().postDataJSON() as { code: string; name: string }; const created = { id: 34, ...body, active: true }; modules = [...modules, created]; return fulfill(route, created, 201, "Módulo creado."); }
      return fulfill(route, modules);
    }
    const moduleMatch = path.match(/^\/api\/v1\/parameters\/modules\/(\d+)$/);
    if (moduleMatch) {
      const id = Number(moduleMatch[1]); const selected = modules.find((item) => item.id === id);
      if (!selected) return fulfill(route, null, 404, "El módulo no existe.");
      if (route.request().method() === "DELETE") { modules = modules.filter((item) => item.id !== id); return fulfill(route, null); }
      if (route.request().method() === "PUT") { const body = route.request().postDataJSON() as { name: string; active?: boolean }; const updated = { ...selected, ...body }; modules = modules.map((item) => item.id === id ? updated : item); return fulfill(route, updated); }
      return fulfill(route, selected);
    }
    if (path === "/api/v1/parameters/permissions") return fulfill(route, permissions);
    if (path === "/api/v1/business-subscriptions") return fulfill(route, subscriptions);
    const subscriptionUsageMatch = path.match(/^\/api\/v1\/business-subscriptions\/(\d+)\/usage$/);
    if (subscriptionUsageMatch) {
      const id = Number(subscriptionUsageMatch[1]);
      const usage = subscriptionUsage.get(id);
      return usage ? fulfill(route, usage) : fulfill(route, null, 404, "No existe una medición de uso.");
    }
    const subscriptionRecalculationMatch = path.match(/^\/api\/v1\/business-subscriptions\/(\d+)\/usage\/recalculate$/);
    if (subscriptionRecalculationMatch) {
      const id = Number(subscriptionRecalculationMatch[1]);
      const usage = subscriptionUsage.get(id);
      if (!usage) return fulfill(route, null, 404, "La suscripción no existe.");
      const recalculated = { ...usage, usersCount: usage.usersCount + 1, ordersCount: usage.ordersCount + 3, updatedAt: "2026-09-15T00:20:00" };
      subscriptionUsage.set(id, recalculated);
      return fulfill(route, recalculated, 200, "Uso recalculado.");
    }
    const subscriptionMatch = path.match(/^\/api\/v1\/business-subscriptions\/(\d+)$/);
    if (subscriptionMatch) {
      const selected = subscriptions.find((item) => item.id === Number(subscriptionMatch[1]));
      return selected ? fulfill(route, selected) : fulfill(route, null, 404, "La suscripción no existe.");
    }
    if (path === "/api/v1/parameters/roles") {
      if (route.request().method() === "POST") {
        const body = route.request().postDataJSON() as { code: string; name: string; description: string; scope: "GLOBAL" | "BUSINESS" };
        const created = { id: 20, ...body, active: true };
        roles = [...roles, created];
        rolePermissionOptions.set(created.id, body.scope === "GLOBAL"
          ? [{ permissionId: 10, code: "PARAMETER_VIEW", name: "Consultar configuración", description: "Permite consultar catálogos globales.", scope: "GLOBAL", moduleId: 1, moduleCode: "PARAMETER", moduleName: "Configuración global", assigned: false }]
          : [{ permissionId: 11, code: "INVENTORY_VIEW", name: "Consultar inventario", description: "Permite consultar productos y repuestos.", scope: "BUSINESS", moduleId: 2, moduleCode: "INVENTORY", moduleName: "Inventario", assigned: false }]);
        return fulfill(route, created, 201, "Rol creado.");
      }
      return fulfill(route, roles);
    }
    const rolePermissionsMatch = path.match(/^\/api\/v1\/parameters\/roles\/(\d+)\/permissions$/);
    if (rolePermissionsMatch) {
      const roleId = Number(rolePermissionsMatch[1]);
      const options = rolePermissionOptions.get(roleId) ?? [];
      if (route.request().method() === "PUT") {
        const body = route.request().postDataJSON() as { permissionIds: number[] };
        const updated = options.map((permission) => ({ ...permission, assigned: body.permissionIds.includes(permission.permissionId as number) }));
        rolePermissionOptions.set(roleId, updated);
        return fulfill(route, updated);
      }
      return fulfill(route, options);
    }
    const roleMatch = path.match(/^\/api\/v1\/parameters\/roles\/(\d+)$/);
    if (roleMatch) {
      const roleId = Number(roleMatch[1]);
      const selected = roles.find((role) => role.id === roleId);
      if (!selected) return fulfill(route, null, 404, "El rol no existe.");
      if (route.request().method() === "DELETE") {
        roles = roles.filter((role) => role.id !== roleId);
        return fulfill(route, null);
      }
      if (route.request().method() === "PUT") {
        const body = route.request().postDataJSON() as { name: string; description: string; scope: "GLOBAL" | "BUSINESS"; active?: boolean };
        const updated = { ...selected, ...body };
        roles = roles.map((role) => role.id === roleId ? updated : role);
        return fulfill(route, updated);
      }
      return fulfill(route, selected);
    }
    if (path === "/api/v1/businesses") {
      return fulfill(route, { items: businesses, page: 0, size: Number(url.searchParams.get("size") ?? 20), totalElements: businesses.length, totalPages: 1, hasNext: false, hasPrevious: false });
    }
    const businessMatch = path.match(/^\/api\/v1\/businesses\/(\d+)$/);
    if (businessMatch) {
      const selected = businesses.find((item) => item.id === Number(businessMatch[1]));
      return selected ? fulfill(route, selected) : fulfill(route, null, 404, "El negocio no existe.");
    }
    if (path === "/api/v1/business/requests/pending") {
      return fulfill(route, { items: pending, page: 0, size: Number(url.searchParams.get("size") ?? 20), totalElements: pending.length, totalPages: pending.length ? 1 : 0, hasNext: false, hasPrevious: false });
    }
    const approveMatch = path.match(/^\/api\/v1\/business\/requests\/(\d+)\/approve$/);
    if (approveMatch) {
      const id = Number(approveMatch[1]);
      const resolved = pending.find((request) => request.id === id);
      pending = pending.filter((request) => request.id !== id);
      return fulfill(route, resolved ? { ...resolved, status: "APPROVED" } : null);
    }
    const rejectMatch = path.match(/^\/api\/v1\/business\/requests\/(\d+)\/reject$/);
    if (rejectMatch) {
      rejectAttempts += 1;
      if (rejectAttempts === 1) return fulfill(route, null, 409, "La solicitud cambió mientras la revisabas.");
      const id = Number(rejectMatch[1]);
      const resolved = pending.find((request) => request.id === id);
      pending = pending.filter((request) => request.id !== id);
      return fulfill(route, resolved ? { ...resolved, status: "REJECTED" } : null);
    }
    return fulfill(route, null, 404, "No encontrado");
  });
}

test("el administrador obtiene un shell global y un resumen basado en solicitudes reales", async ({ page }) => {
  await mockPlatform(page);
  await page.goto("/plataforma");

  await expect(page.getByRole("heading", { name: "Administración de plataforma", exact: true })).toBeVisible();
  await expect(page.getByText("2", { exact: true })).toBeVisible();
  await expect(page.getByRole("link", { name: "Solicitudes" })).toBeVisible();
  await expect(page.locator(".tm-workspace-static")).toContainText("Administración de plataforma");
  await expect(page.locator(".tm-workspace-trigger")).toHaveCount(0);
  await page.screenshot({ path: ".impeccable/review/platform-dashboard-desktop.png", fullPage: true });

  await page.getByRole("link", { name: "Perfil" }).click();
  await expect(page).toHaveURL(/\/app\/perfil$/);
  await expect(page.getByRole("link", { name: "Solicitudes" })).toBeVisible();
  await expect(page.locator(".tm-workspace-static")).toContainText("Administración de plataforma");
  await expect(page.locator(".tm-workspace-trigger")).toHaveCount(0);
  await page.screenshot({ path: ".impeccable/review/platform-profile-sidebar.png", fullPage: true });
});

test("aprobar una solicitud pide confirmación y actualiza la bandeja", async ({ page }) => {
  await mockPlatform(page);
  await page.goto("/plataforma/solicitudes");

  await page.getByRole("button", { name: /Revisar/ }).first().click();
  await expect(page.getByRole("dialog", { name: "Tecno Centro" })).toBeVisible();
  await page.getByRole("button", { name: "Aprobar", exact: true }).click();
  await expect(page.getByRole("heading", { name: "Confirmar aprobación" })).toBeVisible();
  await page.getByRole("button", { name: "Aprobar negocio" }).click();

  await expect(page.getByText("Se aprobó la solicitud de Tecno Centro.")).toBeVisible();
  await expect(page.getByRole("heading", { name: "Tecno Centro" })).toHaveCount(0);
});

test("el rechazo conserva el motivo después de un conflicto y funciona en móvil", async ({ page }) => {
  await page.setViewportSize({ width: 360, height: 800 });
  await mockPlatform(page, [businessRequest(1, "Tecno Centro")]);
  await page.goto("/plataforma/solicitudes");

  await page.getByRole("button", { name: /Revisar/ }).click();
  await page.getByRole("button", { name: "Rechazar", exact: true }).click();
  const reason = page.getByRole("textbox", { name: "Motivo" });
  await reason.fill("El documento legal no permite verificar la razón social.");
  await page.getByRole("button", { name: "Rechazar solicitud" }).click();
  await expect(page.getByText("La solicitud cambió mientras la revisabas.")).toBeVisible();
  await expect(reason).toHaveValue("El documento legal no permite verificar la razón social.");
  await page.screenshot({ path: ".impeccable/review/platform-review-mobile.png", fullPage: true });
  await page.getByRole("button", { name: "Rechazar solicitud" }).click();
  await expect(page.getByText("Se rechazó la solicitud de Tecno Centro.")).toBeVisible();
  await expect(page.getByText("La bandeja está al día")).toBeVisible();
});

test("el directorio global muestra estados en español y consulta el detalle real", async ({ page }) => {
  await mockPlatform(page);
  await page.goto("/plataforma/negocios");

  await expect(page.getByRole("heading", { name: "Negocios de la plataforma" })).toBeVisible();
  await expect(page.getByText("2 negocios registrados")).toBeVisible();
  await expect(page.getByText("Activo", { exact: true })).toBeVisible();
  await expect(page.getByText("Inactivo", { exact: true })).toBeVisible();
  await expect(page.getByText("ACTIVE", { exact: true })).toHaveCount(0);
  await page.screenshot({ path: ".impeccable/review/platform-businesses-desktop.png", fullPage: true });

  await page.getByRole("button", { name: "Ver detalle" }).first().click();
  const dialog = page.getByRole("dialog", { name: "Tecno Centro" });
  await expect(dialog).toBeVisible();
  await expect(dialog.getByText("Servicio técnico especializado en dispositivos móviles.")).toBeVisible();
  await expect(dialog.getByText("4,7 · 24 reseñas")).toBeVisible();
});

test("el detalle de negocio conserva lectura clara en móvil", async ({ page }) => {
  await page.setViewportSize({ width: 360, height: 800 });
  await mockPlatform(page);
  await page.goto("/plataforma/negocios");
  await page.getByRole("button", { name: "Ver detalle" }).first().click();
  await expect(page.getByRole("dialog", { name: "Tecno Centro" })).toBeVisible();
  await page.screenshot({ path: ".impeccable/review/platform-business-detail-mobile.png", fullPage: true });
});

test("el directorio global de usuarios presenta identidad y acceso en español", async ({ page }) => {
  await mockPlatform(page);
  await page.goto("/plataforma/usuarios");

  await expect(page.getByRole("heading", { name: "Usuarios globales" })).toBeVisible();
  await expect(page.getByText("2 cuentas activas")).toBeVisible();
  await expect(page.getByText("Correo verificado", { exact: true })).toBeVisible();
  await expect(page.getByText("Correo pendiente", { exact: true })).toBeVisible();
  await expect(page.getByText("GLOBAL", { exact: true })).toHaveCount(0);
  await page.screenshot({ path: ".impeccable/review/platform-users-desktop.png", fullPage: true });

  await page.getByRole("button", { name: "Ver usuario" }).first().click();
  const dialog = page.getByRole("dialog", { name: "María Gómez" });
  await expect(dialog).toBeVisible();
  await expect(dialog.getByText("Cliente", { exact: true })).toBeVisible();
  await expect(dialog.getByText("Administrador de plataforma", { exact: true })).toBeVisible();
});

test("el administrador actualiza roles, revoca sesiones y desactiva otra cuenta", async ({ page }) => {
  await page.setViewportSize({ width: 360, height: 800 });
  await mockPlatform(page);
  await page.goto("/plataforma/usuarios");
  await page.getByRole("button", { name: "Ver usuario" }).first().click();

  const dialog = page.getByRole("dialog", { name: "María Gómez" });
  await dialog.getByRole("checkbox", { name: /Administrador de plataforma/ }).check();
  await dialog.getByRole("button", { name: "Guardar roles" }).click();
  await expect(dialog.getByText("Los roles globales fueron actualizados y las sesiones anteriores quedaron invalidadas.")).toBeVisible();

  await dialog.getByRole("button", { name: "Revocar sesiones" }).click();
  await expect(dialog.getByRole("heading", { name: "¿Revocar todas las sesiones?" })).toBeVisible();
  await dialog.getByRole("button", { name: "Confirmar revocación" }).click();
  await expect(dialog.getByText("Todas las sesiones del usuario fueron revocadas.")).toBeVisible();
  await page.screenshot({ path: ".impeccable/review/platform-user-mobile.png", fullPage: true });

  await dialog.getByRole("button", { name: "Desactivar cuenta" }).click();
  await expect(dialog.getByRole("heading", { name: "¿Desactivar esta cuenta?" })).toBeVisible();
  await dialog.getByRole("button", { name: "Desactivar cuenta" }).click();
  await expect(page.getByText("Se desactivó la cuenta de María.")).toBeVisible();
  await expect(page.getByText("1 cuenta activa")).toBeVisible();
});

test("roles y permisos presenta alcances traducidos y catálogo agrupado", async ({ page }) => {
  await mockPlatform(page);
  await page.goto("/plataforma/accesos");

  await expect(page.getByRole("heading", { name: "Roles y permisos" })).toBeVisible();
  await expect(page.getByText("Administrador de plataforma", { exact: true })).toBeVisible();
  await expect(page.getByText("Global", { exact: true })).toBeVisible();
  await expect(page.getByText("Negocio", { exact: true }).first()).toBeVisible();
  await expect(page.getByText("GLOBAL", { exact: true })).toHaveCount(0);
  await page.screenshot({ path: ".impeccable/review/platform-access-roles-desktop.png", fullPage: true });

  await page.getByRole("tab", { name: /Permisos/ }).click();
  await expect(page.getByRole("heading", { name: "Configuración global" })).toBeVisible();
  await expect(page.getByRole("heading", { name: "Inventario", exact: true })).toBeVisible();
  await expect(page.getByText("Catálogo de consulta")).toBeVisible();
});

test("crea un rol, asigna permisos y lo desactiva en móvil", async ({ page }) => {
  await page.setViewportSize({ width: 360, height: 800 });
  await mockPlatform(page);
  await page.goto("/plataforma/accesos");
  await page.getByRole("button", { name: "Nuevo rol" }).click();

  const dialog = page.getByRole("dialog", { name: "Crear rol" });
  await dialog.getByLabel("Código interno").fill("inventory_auditor");
  await dialog.getByLabel("Nombre visible").fill("Auditor de inventario");
  await dialog.getByLabel("Descripción funcional").fill("Consulta el inventario sin modificar existencias.");
  await dialog.getByLabel("Alcance").selectOption("BUSINESS");
  await dialog.getByRole("button", { name: "Crear rol" }).click();

  const createdDialog = page.getByRole("dialog", { name: "Auditor de inventario" });
  await expect(createdDialog.getByText("El rol fue creado. Ahora puedes definir sus permisos.")).toBeVisible();
  await createdDialog.getByRole("checkbox", { name: /Consultar inventario/ }).check();
  await createdDialog.getByRole("button", { name: "Guardar permisos" }).click();
  await expect(createdDialog.getByText("Los permisos del rol fueron actualizados y las sesiones afectadas quedaron invalidadas.")).toBeVisible();
  await page.screenshot({ path: ".impeccable/review/platform-access-role-mobile.png", fullPage: true });

  await createdDialog.getByRole("button", { name: "Desactivar rol" }).click();
  await createdDialog.getByRole("button", { name: "Confirmar" }).click();
  await expect(page.getByText("Se desactivó el rol Auditor de inventario.")).toBeVisible();
  await expect(page.getByText("Auditor de inventario", { exact: true })).toHaveCount(0);
});

test("parámetros globales reúne ubicación, documentos y configuración del sistema", async ({ page }) => {
  await mockPlatform(page);
  await page.goto("/plataforma/parametros");

  await expect(page.getByRole("heading", { name: "Parámetros globales" })).toBeVisible();
  await expect(page.getByRole("link", { name: "Parámetros globales" })).toHaveClass(/is-active/);
  await expect(page.getByText("Colombia", { exact: true })).toBeVisible();
  await expect(page.getByText("Ecuador", { exact: true })).toBeVisible();

  await page.getByRole("tab", { name: /Departamentos/ }).click();
  await expect(page.getByText("Huila", { exact: true })).toBeVisible();
  await page.getByRole("tab", { name: /Ciudades/ }).click();
  await expect(page.getByText("Neiva", { exact: true })).toBeVisible();
  await page.getByRole("tab", { name: /Documentos/ }).click();
  await expect(page.getByText("Cédula de ciudadanía", { exact: true })).toBeVisible();
  await expect(page.getByText("Personas", { exact: true })).toBeVisible();
  await page.getByRole("tab", { name: /Sistema/ }).click();
  await expect(page.getByRole("heading", { name: "Configuración global", exact: true })).toBeVisible();
  await expect(page.getByText("Consultar configuración", { exact: true })).toBeVisible();
  await expect(page.getByText("Permisos protegidos")).toBeVisible();
  await page.screenshot({ path: ".impeccable/review/platform-parameters-desktop.png", fullPage: true });
});

test("crea una ciudad usando país y departamento dependientes en móvil", async ({ page }) => {
  await page.setViewportSize({ width: 360, height: 800 });
  await mockPlatform(page);
  await page.goto("/plataforma/parametros");
  await page.getByRole("tab", { name: /Ciudades/ }).click();
  await page.getByRole("button", { name: "Nueva ciudad" }).click();

  const dialog = page.getByRole("dialog", { name: "Crear ciudad" });
  await dialog.getByLabel("País").selectOption("1");
  await dialog.getByLabel("Departamento").selectOption("2");
  await dialog.getByLabel("Nombre visible").fill("Pitalito");
  await dialog.getByRole("button", { name: "Crear", exact: true }).click();

  await expect(page.getByText("Se creó la ciudad Pitalito.")).toBeVisible();
  await expect(page.getByText("Pitalito", { exact: true })).toBeVisible();
  await page.screenshot({ path: ".impeccable/review/platform-parameters-city-mobile.png", fullPage: true });
});

test("crea un módulo sin ofrecer mutaciones inseguras de permisos", async ({ page }) => {
  await mockPlatform(page);
  await page.goto("/plataforma/parametros");
  await page.getByRole("tab", { name: /Sistema/ }).click();
  await page.getByRole("button", { name: "Nuevo módulo" }).click();

  const dialog = page.getByRole("dialog", { name: "Crear módulo" });
  await dialog.getByLabel("Código interno").fill("REPORTS");
  await dialog.getByLabel("Nombre visible").fill("Reportes");
  await dialog.getByRole("button", { name: "Crear", exact: true }).click();

  await expect(page.getByText("Se creó el módulo Reportes.")).toBeVisible();
  await expect(page.getByRole("heading", { name: "Reportes", exact: true })).toBeVisible();
  await expect(page.getByRole("button", { name: /Nuevo permiso/ })).toHaveCount(0);
});

test("suscripciones presenta el plan y estado global de cada negocio", async ({ page }) => {
  await mockPlatform(page);
  await page.goto("/plataforma/suscripciones");

  await expect(page.getByRole("heading", { name: "Suscripciones" })).toBeVisible();
  await expect(page.getByRole("link", { name: "Suscripciones" })).toHaveClass(/is-active/);
  await expect(page.getByText("1 activas")).toBeVisible();
  await expect(page.getByText("1 requieren atención")).toBeVisible();
  await expect(page.getByText("Tecno Centro", { exact: true })).toBeVisible();
  await expect(page.getByText("Pago pendiente", { exact: true })).toBeVisible();
  await expect(page.getByText("PAST_DUE", { exact: true })).toHaveCount(0);
  await page.screenshot({ path: ".impeccable/review/platform-subscriptions-desktop.png", fullPage: true });
});

test("consulta y recalcula el uso de una suscripción en móvil", async ({ page }) => {
  await page.setViewportSize({ width: 360, height: 800 });
  await mockPlatform(page);
  await page.goto("/plataforma/suscripciones");
  await page.getByRole("button", { name: "Ver uso" }).first().click();

  const dialog = page.getByRole("dialog", { name: "Tecno Centro" });
  await expect(dialog.getByText("150 MB", { exact: true })).toBeVisible();
  await expect(dialog.getByText("120", { exact: true })).toBeVisible();
  await dialog.getByRole("button", { name: "Recalcular uso" }).click();
  await expect(dialog.getByRole("heading", { name: "¿Recalcular el uso ahora?" })).toBeVisible();
  await dialog.getByRole("button", { name: "Confirmar recálculo" }).click();
  await expect(dialog.getByText("El uso fue recalculado con la información actual del negocio.")).toBeVisible();
  await expect(dialog.getByText("123", { exact: true })).toBeVisible();
  await page.screenshot({ path: ".impeccable/review/platform-subscription-usage-mobile.png", fullPage: true });
});

test("explica cuando suscripciones está deshabilitado en el backend", async ({ page }) => {
  await mockPlatform(page);
  await page.route("https://api-dev.juanodsk.dev/api/v1/business-subscriptions", async (route) => fulfill(route, null, 404, "No encontrado"));
  await page.goto("/plataforma/suscripciones");

  await expect(page.getByRole("heading", { name: "Suscripciones no disponibles" })).toBeVisible();
  await expect(page.getByText("FEATURE_SUBSCRIPTIONS_ENABLED")).toBeVisible();
});
