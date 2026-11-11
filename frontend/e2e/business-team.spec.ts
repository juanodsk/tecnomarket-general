import { expect, test, type Page, type Route } from "@playwright/test";

import type { BusinessUser } from "@/types/business";

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

const branches = [
  { id: 101, branchNumber: 1, name: "Sede principal", cityId: 3, cityName: "Neiva", address: "Calle 10 # 2-30", primaryBranch: true },
  { id: 102, branchNumber: 2, name: "Sede norte", cityId: 3, cityName: "Neiva", address: "Carrera 5 # 18-20", primaryBranch: false },
];

const roles = [
  { roleId: 2, roleCode: "OWNER", roleName: "Propietario" },
  { roleId: 3, roleCode: "ADMIN", roleName: "Administrador" },
  { roleId: 4, roleCode: "TECHNICIAN", roleName: "Técnico" },
];

function member(id: number, userId: number, name: string, surname: string, roleId: number, roleCode: string, roleName: string): BusinessUser {
  return {
    id,
    businessId: 10,
    userId,
    userName: name,
    userSurname: surname,
    userEmail: `${name.toLowerCase()}@example.com`,
    roleId,
    roleCode,
    roleName,
    branchIds: [101],
    commissionPercentage: roleCode === "TECHNICIAN" ? 20 : 0,
    active: true,
  };
}

async function fulfill(route: Route, data: unknown, status = 200, message = "ok") {
  await route.fulfill({
    status,
    contentType: "application/json",
    body: JSON.stringify({ success: status < 400, message, data }),
  });
}

function pageData(items: BusinessUser[]) {
  return {
    items,
    page: 0,
    size: 20,
    totalElements: items.length,
    totalPages: 1,
    hasNext: false,
    hasPrevious: false,
  };
}

async function mockTeam(page: Page, options?: { conflictOnCreate?: boolean }) {
  let members = [member(71, 7, "Laura", "Méndez", 2, "OWNER", "Propietario")];

  await page.route("https://api-dev.juanodsk.dev/**", async (route) => {
    const url = new URL(route.request().url());
    const path = url.pathname;
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
        permissions: ["BUSINESS_VIEW", "BUSINESS_BRANCH_VIEW", "BUSINESS_USER_VIEW", "BUSINESS_USER_CREATE", "BUSINESS_USER_UPDATE"],
        branches,
      });
    }
    if (path === "/api/v1/businesses/10/users/role-options") return fulfill(route, roles);
    if (path === "/api/v1/businesses/10/users/available-users") {
      return fulfill(route, [{ userId: 9, fullName: "Daniel Rojas", email: url.searchParams.get("email"), assigned: false }]);
    }
    if (path === "/api/v1/businesses/10/users" && method === "GET") return fulfill(route, pageData(members));
    if (path === "/api/v1/businesses/10/users" && method === "POST") {
      if (options?.conflictOnCreate) return fulfill(route, null, 409, "El usuario ya pertenece al negocio.");
      const body = route.request().postDataJSON() as { userId: number; roleId: number; branchIds: number[]; commissionPercentage: number | null };
      const created = {
        ...member(73, body.userId, "Daniel", "Rojas", body.roleId, "TECHNICIAN", "Técnico"),
        branchIds: body.branchIds,
        commissionPercentage: body.commissionPercentage,
      };
      members = [...members, created];
      return fulfill(route, created, 201, "Integrante agregado.");
    }
    const memberMatch = path.match(/^\/api\/v1\/businesses\/10\/users\/(\d+)$/);
    if (memberMatch && method === "PUT") {
      const id = Number(memberMatch[1]);
      const body = route.request().postDataJSON() as { roleId: number; branchIds: number[]; commissionPercentage: number | null };
      const role = roles.find((item) => item.roleId === body.roleId)!;
      members = members.map((item) => item.id === id ? {
        ...item,
        roleId: role.roleId,
        roleCode: role.roleCode,
        roleName: role.roleName,
        branchIds: body.branchIds,
        commissionPercentage: body.commissionPercentage ?? 0,
      } : item);
      return fulfill(route, members.find((item) => item.id === id), 200, "Asignación actualizada.");
    }
    if (memberMatch && method === "DELETE") {
      const id = Number(memberMatch[1]);
      members = members.filter((item) => item.id !== id);
      return fulfill(route, null, 200, "Integrante retirado.");
    }
    return fulfill(route, null, 404, "No encontrado");
  });
}

async function findCandidate(page: Page) {
  await page.getByLabel("Correo electrónico").fill("daniel@example.com");
  await page.getByRole("button", { name: "Buscar usuario" }).click();
  await expect(page.getByText("Daniel Rojas", { exact: true })).toBeVisible();
}

async function fillAssignment(page: Page) {
  await page.getByLabel("Rol").selectOption("4");
  await page.getByLabel("Comisión por reparación (%)").fill("20,50");
  await page.getByLabel("Sede principal").check();
  await page.getByLabel("Sede norte").check();
}

test("lista el equipo y agrega un integrante existente", async ({ page }) => {
  await mockTeam(page);
  await page.goto("/app/negocios/10/equipo");

  await expect(page.getByRole("heading", { name: "Equipo", exact: true })).toBeVisible();
  await expect(page.getByRole("heading", { name: "Laura Méndez" })).toBeVisible();
  await page.getByRole("button", { name: "Agregar integrante" }).click();
  await findCandidate(page);
  await fillAssignment(page);
  await page.locator(".tm-team-form").getByRole("button", { name: "Agregar al equipo" }).click();

  await expect(page.getByText("El usuario ya forma parte del equipo.")).toBeVisible();
  await expect(page.getByRole("heading", { name: "Daniel Rojas" })).toBeVisible();
  await page.screenshot({ path: ".impeccable/review/business-team-desktop.png", fullPage: true });
  await page.getByRole("button", { name: "Usar tema oscuro" }).click();
  await page.screenshot({ path: ".impeccable/review/business-team-dark.png", fullPage: true });
});

test("un conflicto conserva la asignación introducida", async ({ page }) => {
  await mockTeam(page, { conflictOnCreate: true });
  await page.goto("/app/negocios/10/equipo");
  await page.getByRole("button", { name: "Agregar integrante" }).click();
  await findCandidate(page);
  await fillAssignment(page);
  await page.locator(".tm-team-form").getByRole("button", { name: "Agregar al equipo" }).click();

  await expect(page.getByText("El usuario ya pertenece al negocio.")).toBeVisible();
  await expect(page.getByLabel("Rol")).toHaveValue("4");
  await expect(page.getByLabel("Sede principal")).toBeChecked();
  await expect(page.getByLabel("Sede norte")).toBeChecked();
  await expect(page.getByLabel("Comisión por reparación (%)")).toHaveValue("20,50");
});

test("actualiza y retira un integrante con confirmación en móvil", async ({ page }) => {
  await page.setViewportSize({ width: 360, height: 800 });
  await mockTeam(page);
  await page.goto("/app/negocios/10/equipo");

  await page.locator(".tm-team-list article").filter({ hasText: "Laura Méndez" }).getByRole("button", { name: "Editar" }).click();
  await page.getByLabel("Rol").selectOption("3");
  await page.getByLabel("Sede norte").check();
  await page.getByRole("button", { name: "Guardar cambios" }).click();
  await expect(page.getByText("La asignación del integrante quedó actualizada.")).toBeVisible();

  await page.locator(".tm-team-list article").filter({ hasText: "Laura Méndez" }).getByRole("button", { name: "Editar" }).click();
  await page.getByRole("button", { name: "Retirar", exact: true }).click();
  await expect(page.getByText("La cuenta global de TecMarket no será eliminada.")).toBeVisible();
  await expect(page.getByRole("button", { name: "Conservar integrante" })).toBeFocused();
  await page.screenshot({ path: ".impeccable/review/business-team-mobile-confirm.png", fullPage: true });
  await page.locator(".tm-team-danger__confirm").getByRole("button", { name: "Retirar" }).click();

  await expect(page.getByText("El integrante fue retirado del equipo.")).toBeVisible();
  await expect(page.getByRole("heading", { name: "Laura Méndez" })).toHaveCount(0);
});
