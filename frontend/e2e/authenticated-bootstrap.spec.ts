import { expect, test, type Page, type Route } from "@playwright/test";

const user = {
  id: 1,
  documentTypeCode: "CC",
  documentNumber: "1001",
  name: "Ana",
  surname: "Pérez",
  email: "ana@example.com",
  phone: "3000000000",
  countryId: 1,
  countryName: "Colombia",
  departmentId: 2,
  departmentName: "Huila",
  cityId: 3,
  cityName: "Neiva",
};

function business(id: number, name: string) {
  return {
    id,
    documentTypeCode: "NIT",
    documentNumber: `900${id}`,
    name,
    legalName: `${name} S.A.S.`,
    email: `contacto${id}@example.com`,
    phone: "6080000000",
    address: "Calle 1",
    description: null,
    cityId: 3,
    cityName: "Neiva",
    logo: null,
    coverImage: null,
    ratingAverage: 0,
    ratingCount: 0,
    active: true,
  };
}

function context(id: number, name: string) {
  return {
    userId: 1,
    businessId: id,
    businessName: name,
    businessLogo: null,
    platformAdmin: false,
    businessUserId: 100 + id,
    roleId: 2,
    roleCode: "OWNER",
    roleName: "Propietario",
    permissions: ["BUSINESS_VIEW"],
    branches: [
      { id: id * 10, branchNumber: 1, name: "Sede principal", cityId: 3, cityName: "Neiva", address: "Calle 1", primaryBranch: true },
      { id: id * 10 + 1, branchNumber: 2, name: "Sede norte", cityId: 3, cityName: "Neiva", address: "Carrera 2", primaryBranch: false },
    ],
  };
}

async function fulfill(route: Route, data: unknown, status = 200) {
  await route.fulfill({ status, contentType: "application/json", body: JSON.stringify({ success: status < 400, message: status < 400 ? "ok" : "Sin permiso", data }) });
}

async function mockAuthenticated(page: Page, businesses: ReturnType<typeof business>[], options?: { forbiddenBusinessId?: number; delayedBusinessId?: number; failFirstBusinessId?: number }) {
  let failedInitialContextRequests = 0;
  await page.route("https://api-dev.juanodsk.dev/**", async (route) => {
    const path = new URL(route.request().url()).pathname;
    if (path === "/api/v1/auth/refresh") return fulfill(route, { accessToken: "access-token", user });
    if (path === "/api/v1/auth/me") return fulfill(route, user);
    if (path === "/api/v1/auth/me/permissions") return fulfill(route, { userId: 1, roles: ["USER"], permissions: [] });
    if (path === "/api/v1/businesses/my") return fulfill(route, businesses);
    const match = path.match(/^\/api\/v1\/businesses\/(\d+)\/me\/context$/);
    if (match) {
      const id = Number(match[1]);
      if (id === options?.forbiddenBusinessId) return fulfill(route, null, 403);
      if (id === options?.failFirstBusinessId && failedInitialContextRequests < 2) {
        failedInitialContextRequests += 1;
        return fulfill(route, null, 503);
      }
      if (id === options?.delayedBusinessId) await new Promise((resolve) => setTimeout(resolve, 350));
      const selected = businesses.find((item) => item.id === id);
      return fulfill(route, context(id, selected?.name ?? "Desconocido"));
    }
    return fulfill(route, null, 404);
  });
}

test("un solo negocio abre su información y muestra las sedes en el desplegable", async ({ page }) => {
  await mockAuthenticated(page, [business(10, "Taller Central")]);
  await page.goto("/app");
  await expect(page).toHaveURL(/\/app\/negocios\/10$/);
  await expect(page.getByRole("heading", { name: "Taller Central", exact: true })).toBeVisible();
  await expect(page.getByText("Sede principal · Neiva")).toBeVisible();
  await page.locator(".tm-workspace-trigger").click();
  await expect(page.locator(".tm-branch-option").filter({ hasText: "Sede principal" })).toBeVisible();
  await expect(page.locator(".tm-branch-option").filter({ hasText: "Sede norte" })).toBeVisible();
  await page.screenshot({ path: ".impeccable/review/app-desktop.png", fullPage: true });
});

test("varios negocios dirigen al selector", async ({ page }) => {
  await mockAuthenticated(page, [business(10, "Taller Norte"), business(20, "Taller Sur")]);
  await page.goto("/app");
  await expect(page).toHaveURL(/\/app\/negocios\/seleccionar$/);
  await expect(page.getByRole("heading", { name: "Selecciona un negocio" })).toBeVisible();
  await expect(page.getByRole("link", { name: /Taller Norte/ })).toBeVisible();
  await expect(page.getByRole("link", { name: /Taller Sur/ })).toBeVisible();
});

test("el cambio de negocio no deja visible la información anterior", async ({ page }) => {
  await page.setViewportSize({ width: 360, height: 800 });
  await mockAuthenticated(page, [business(10, "Negocio A"), business(20, "Negocio B")], { delayedBusinessId: 20 });
  await page.goto("/app/negocios/10");
  await expect(page.getByRole("heading", { name: "Negocio A", exact: true })).toBeVisible();
  await page.locator(".tm-workspace-trigger").click();
  await page.getByRole("button", { name: /Negocio B/ }).click();
  await expect(page.getByRole("heading", { name: "Negocio A", exact: true })).not.toBeVisible();
  await expect(page.getByText("Cargando la información del negocio")).toBeVisible();
  await expect(page.getByRole("heading", { name: "Negocio B", exact: true })).toBeVisible();
  await page.locator(".tm-workspace-trigger").click();
  await page.screenshot({ path: ".impeccable/review/workspace-mobile-dropdown.png", fullPage: true });
  await page.getByRole("button", { name: /Sede norte/ }).click();
  await expect(page.locator(".tm-workspace-trigger")).toContainText("Sede norte");

  const menuButton = page.getByRole("button", { name: "Abrir menú" });
  await menuButton.click();
  await expect(page.getByRole("navigation", { name: "Navegación principal" })).toBeVisible();
  await page.keyboard.press("Escape");
  await expect(menuButton).toBeFocused();
  await expect(page.locator("#tm-sidebar-navigation")).toHaveAttribute("aria-hidden", "true");
  await page.screenshot({ path: ".impeccable/review/app-mobile.png", fullPage: true });
});

test("un negocio con respuesta 403 conserva el error de autorización", async ({ page }) => {
  await mockAuthenticated(page, [business(10, "Taller Restringido")], { forbiddenBusinessId: 10 });
  await page.goto("/app/negocios/10");
  await expect(page).toHaveURL(/\/acceso-denegado$/);
  await expect(page.getByRole("heading", { name: "Acceso denegado" })).toBeVisible();
});

test("un reintento de carga controla el error y puede recuperarse", async ({ page }) => {
  const pageErrors: Error[] = [];
  page.on("pageerror", (error) => pageErrors.push(error));
  await mockAuthenticated(page, [business(10, "Taller Recuperable")], { failFirstBusinessId: 10 });

  await page.goto("/app/negocios/10");
  await expect(page.getByRole("heading", { name: "No pudimos cargar esta información" })).toBeVisible();
  await page.getByRole("button", { name: "Reintentar" }).click();
  await expect(page.getByRole("heading", { name: "Taller Recuperable", exact: true })).toBeVisible();
  expect(pageErrors).toEqual([]);
});
