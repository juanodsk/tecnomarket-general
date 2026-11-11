import { expect, test, type Page, type Route } from "@playwright/test";

const user = {
  id: 7,
  documentTypeCode: "CC",
  documentNumber: "1075000007",
  name: "Laura",
  surname: "Méndez",
  email: "laura@example.com",
  phone: "+573001112233",
  countryId: 1,
  countryName: "Colombia",
  departmentId: 2,
  departmentName: "Huila",
  cityId: 3,
  cityName: "Neiva",
};

const business = {
  id: 10,
  documentTypeCode: "NIT",
  documentNumber: "901234567-1",
  name: "Tecno Centro",
  legalName: "Tecno Centro S.A.S.",
  email: "contacto@tecnocentro.com",
  phone: "+573001119999",
  address: "Calle 10 # 2-30",
  description: null,
  cityId: 3,
  cityName: "Neiva",
  logo: null,
  coverImage: null,
  ratingAverage: 0,
  ratingCount: 0,
  active: true,
};

function branch(id: number, branchNumber: number, name: string, primaryBranch: boolean) {
  return {
    id,
    businessId: 10,
    businessName: "Tecno Centro",
    branchNumber,
    name,
    countryId: 1,
    countryName: "Colombia",
    cityId: 3,
    cityName: "Neiva",
    address: branchNumber === 1 ? "Calle 10 # 2-30" : "Carrera 5 # 18-20",
    neighborhood: branchNumber === 1 ? "Centro" : "Quirinal",
    phone: branchNumber === 1 ? "+573001119999" : null,
    latitude: null,
    longitude: null,
    serviceRadiusKm: branchNumber === 1 ? 8 : null,
    primaryBranch,
    active: true,
    createdAt: "2026-09-20T10:00:00",
    updatedAt: "2026-09-20T10:00:00",
  };
}

async function fulfill(route: Route, data: unknown, status = 200, message = "ok") {
  await route.fulfill({
    status,
    contentType: "application/json",
    body: JSON.stringify({ success: status < 400, message, data }),
  });
}

async function mockBranches(page: Page, options?: { conflictOnCreate?: boolean }) {
  let branches = [branch(101, 1, "Sede principal", true), branch(102, 2, "Sede norte", false)];

  await page.route("https://api-dev.juanodsk.dev/**", async (route) => {
    const path = new URL(route.request().url()).pathname;
    const method = route.request().method();

    if (path === "/api/v1/auth/refresh") return fulfill(route, { accessToken: "owner-token", user });
    if (path === "/api/v1/auth/me") return fulfill(route, user);
    if (path === "/api/v1/auth/me/permissions") return fulfill(route, { userId: 7, roles: ["USER"], permissions: [] });
    if (path === "/api/v1/businesses/my") return fulfill(route, [business]);
    if (path === "/api/v1/businesses/10/me/context") {
      return fulfill(route, {
        userId: 7,
        businessId: 10,
        businessName: "Tecno Centro",
        businessLogo: null,
        platformAdmin: false,
        businessUserId: 71,
        roleId: 2,
        roleCode: "OWNER",
        roleName: "Propietario",
        permissions: ["BUSINESS_VIEW", "BUSINESS_BRANCH_VIEW", "BUSINESS_BRANCH_CREATE", "BUSINESS_BRANCH_UPDATE"],
        branches: branches.map(({ id, branchNumber, name, cityId, cityName, address, primaryBranch }) => ({ id, branchNumber, name, cityId, cityName, address, primaryBranch })),
      });
    }
    if (path === "/api/v1/businesses/10/branches" && method === "GET") return fulfill(route, branches);
    if (path === "/api/v1/businesses/10/branches" && method === "POST") {
      if (options?.conflictOnCreate) return fulfill(route, null, 409, "Ya existe una sede con ese nombre.");
      const body = route.request().postDataJSON() as Record<string, unknown>;
      const created = {
        ...branch(103, 3, String(body.name), Boolean(body.primaryBranch)),
        ...body,
        countryId: 1,
        countryName: "Colombia",
        cityName: "Neiva",
      };
      if (created.primaryBranch) branches = branches.map((item) => ({ ...item, primaryBranch: false }));
      branches = [created, ...branches];
      return fulfill(route, created, 201, "Sede creada.");
    }
    const branchMatch = path.match(/^\/api\/v1\/businesses\/10\/branches\/(\d+)$/);
    if (branchMatch && method === "PUT") {
      const id = Number(branchMatch[1]);
      const body = route.request().postDataJSON() as Record<string, unknown>;
      if (body.primaryBranch) branches = branches.map((item) => ({ ...item, primaryBranch: false }));
      const current = branches.find((item) => item.id === id)!;
      const updated = { ...current, ...body, cityId: Number(body.cityId), countryId: 1, countryName: "Colombia", cityName: "Neiva" };
      branches = branches.map((item) => item.id === id ? updated : item);
      return fulfill(route, updated, 200, "Sede actualizada.");
    }
    if (branchMatch && method === "DELETE") {
      const id = Number(branchMatch[1]);
      branches = branches.filter((item) => item.id !== id);
      return fulfill(route, null, 200, "Sede desactivada.");
    }
    if (path === "/api/v1/public/locations/countries") {
      return fulfill(route, [{ id: 1, code: "CO", name: "Colombia", active: true }]);
    }
    if (path === "/api/v1/public/locations/countries/1/departments") {
      return fulfill(route, [{ id: 2, countryId: 1, countryCode: "CO", countryName: "Colombia", code: "HUI", name: "Huila", active: true }]);
    }
    if (path === "/api/v1/public/locations/departments/2/cities") {
      return fulfill(route, [{ id: 3, countryId: 1, countryCode: "CO", countryName: "Colombia", departmentId: 2, departmentCode: "HUI", departmentName: "Huila", name: "Neiva", active: true }]);
    }
    return fulfill(route, null, 404, "No encontrado");
  });
}

async function fillNewBranch(page: Page, name = "Sede sur") {
  await page.getByLabel("Nombre de la sede").fill(name);
  await page.getByLabel("Dirección").fill("Calle 21 # 8-40");
  await page.getByLabel("Barrio").fill("Altico");
  await page.getByLabel("Teléfono").fill("+573001110000");
  await page.getByLabel("País").selectOption("1");
  await page.getByLabel("Departamento").selectOption("2");
  await page.getByLabel("Ciudad").selectOption("3");
}

async function expectLocationMapAvailable(page: Page) {
  await expect(
    page.locator('.tm-location-map[data-ready="true"]').or(page.getByText(/Geoapify no está configurado/)),
  ).toBeVisible({ timeout: 35_000 });
}

test("lista las sedes autorizadas y crea una nueva sin desincronizar el selector", async ({ page }) => {
  await mockBranches(page);
  await page.goto("/app/negocios/10/sedes");

  await expect(page.getByRole("heading", { name: "Sedes", exact: true })).toBeVisible();
  await expect(page.getByRole("heading", { name: "Sede principal" })).toBeVisible();
  await expect(page.getByText("Principal", { exact: true })).toBeVisible();
  await page.getByRole("button", { name: "Nueva sede" }).click();
  await fillNewBranch(page);
  await page.locator(".tm-location-picker").scrollIntoViewIfNeeded();
  await expectLocationMapAvailable(page);
  await page.screenshot({ path: ".impeccable/review/business-location-picker-desktop.png" });
  await page.getByRole("button", { name: "Crear sede" }).click();

  await expect(page.getByText("La nueva sede ya está disponible en el negocio.")).toBeVisible();
  await expect(page.getByRole("heading", { name: "Sede sur" })).toBeVisible();
  await page.locator(".tm-workspace-trigger").click();
  await expect(page.locator(".tm-branch-option").filter({ hasText: "Sede sur" })).toBeVisible();
  await page.keyboard.press("Escape");
  await page.screenshot({ path: ".impeccable/review/business-branches-desktop.png", fullPage: true });
});

test("un conflicto conserva todos los datos de la nueva sede", async ({ page }) => {
  await mockBranches(page, { conflictOnCreate: true });
  await page.goto("/app/negocios/10/sedes");
  await page.getByRole("button", { name: "Nueva sede" }).click();
  await fillNewBranch(page, "Sede norte");
  await expectLocationMapAvailable(page);
  await page.getByRole("button", { name: "Crear sede" }).click();

  await expect(page.getByText("Ya existe una sede con ese nombre.")).toBeVisible();
  await expect(page.getByLabel("Nombre de la sede")).toHaveValue("Sede norte");
  await expect(page.getByLabel("Dirección")).toHaveValue("Calle 21 # 8-40");
  await page.getByRole("button", { name: "Cerrar", exact: true }).click();
  await expect(page.getByRole("button", { name: "Nueva sede" })).toBeFocused();
});

test("edita la sede principal y desactiva una sede con confirmación en móvil", async ({ page }) => {
  await page.setViewportSize({ width: 360, height: 800 });
  await mockBranches(page);
  await page.goto("/app/negocios/10/sedes");

  await page.locator(".tm-workspace-trigger").click();
  await page.locator(".tm-branch-option").filter({ hasText: "Sede norte" }).click();
  await page.locator(".tm-branch-list article").filter({ hasText: "Sede norte" }).getByRole("button", { name: "Editar" }).click();
  await page.locator(".tm-location-picker").scrollIntoViewIfNeeded();
  await expectLocationMapAvailable(page);
  await page.screenshot({ path: ".impeccable/review/business-location-picker-mobile.png" });
  await page.getByLabel("Usar como sede principal").check();
  await page.getByRole("button", { name: "Guardar cambios" }).click();
  await expect(page.getByText("Los datos de la sede quedaron actualizados.")).toBeVisible();

  await page.locator(".tm-branch-list article").filter({ hasText: "Sede norte" }).getByRole("button", { name: "Editar" }).click();
  await page.getByRole("button", { name: "Desactivar", exact: true }).click();
  await expect(page.getByText("Esta acción no es posible si es la única sede activa.")).toBeVisible();
  await expect(page.getByRole("button", { name: "Conservar sede" })).toBeFocused();
  await page.screenshot({ path: ".impeccable/review/business-branches-mobile-confirm.png", fullPage: true });
  await page.locator(".tm-branch-danger__confirm").getByRole("button", { name: "Desactivar" }).click();

  await expect(page.getByText("La sede fue desactivada.")).toBeVisible();
  await expect(page.getByRole("heading", { name: "Sede norte" })).toHaveCount(0);
  await expect(page.locator(".tm-workspace-trigger")).toContainText("Sede principal");
});
