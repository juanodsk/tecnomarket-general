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

function request(status: "PENDING" | "APPROVED" | "REJECTED", id = 81) {
  return {
    id,
    documentTypeCode: "NIT",
    documentNumber: "901234567-1",
    businessName: "Tecno Centro",
    legalName: "Tecno Centro S.A.S.",
    email: "contacto@tecnocentro.com",
    phone: "+573001119999",
    cityId: 3,
    cityName: "Neiva",
    address: "Calle 10 # 2-30",
    legalFile: "https://archivos.example.com/camara-comercio.pdf",
    status,
    requestedByUserId: 7,
    requestedByUserName: "Laura Méndez",
    reviewedByUserId: status === "PENDING" ? null : 90,
    reviewedByUserName: status === "PENDING" ? null : "Admin Plataforma",
    reviewedAt: status === "PENDING" ? null : "2026-09-16T10:00:00",
    rejectionReason: status === "REJECTED" ? "El documento adjunto no permite verificar la razón social." : null,
    createdAt: "2026-09-16T09:00:00",
  };
}

async function fulfill(route: Route, data: unknown, status = 200, message = "ok") {
  await route.fulfill({
    status,
    contentType: "application/json",
    body: JSON.stringify({ success: status < 400, message, data }),
  });
}

async function mockOwner(
  page: Page,
  initialRequests: ReturnType<typeof request>[],
  options?: { conflictOnCreate?: boolean },
) {
  let requests = [...initialRequests];
  await page.route("https://api-dev.juanodsk.dev/**", async (route) => {
    const path = new URL(route.request().url()).pathname;
    const method = route.request().method();
    if (path === "/api/v1/auth/refresh") return fulfill(route, { accessToken: "owner-token", user });
    if (path === "/api/v1/auth/me") return fulfill(route, user);
    if (path === "/api/v1/auth/me/permissions") {
      return fulfill(route, {
        userId: 7,
        roles: ["CUSTOMER"],
        permissions: ["BUSINESS_REQUEST_CREATE", "BUSINESS_REQUEST_VIEW_OWN"],
      });
    }
    if (path === "/api/v1/auth/logout") return fulfill(route, null);
    if (path === "/api/v1/businesses/my") return fulfill(route, []);
    if (path === "/api/v1/business/requests/my") {
      return fulfill(route, {
        items: requests,
        page: 0,
        size: 100,
        totalElements: requests.length,
        totalPages: requests.length ? 1 : 0,
        hasNext: false,
        hasPrevious: false,
      });
    }
    if (path === "/api/v1/business/requests" && method === "POST") {
      if (options?.conflictOnCreate) return fulfill(route, null, 409, "Ya existe una solicitud para este documento.");
      const body = route.request().postDataJSON() as Record<string, unknown>;
      const created = { ...request("PENDING", 99), ...body };
      requests = [created, ...requests];
      return fulfill(route, created, 201, "Solicitud creada.");
    }
    if (path === "/api/v1/parameters/document-types") {
      return fulfill(route, [{ id: 1, code: "NIT", name: "Número de identificación tributaria", description: "Empresa", appliesToPerson: false, appliesToCompany: true, active: true }]);
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

async function fillRequestForm(page: Page) {
  await page.getByLabel("Tipo de documento").selectOption("NIT");
  await page.getByLabel("Número de documento").fill("901234567-1");
  await page.getByLabel("Nombre del negocio").fill("Tecno Centro");
  await page.getByLabel("Razón social").fill("Tecno Centro S.A.S.");
  await page.getByLabel("Correo del negocio").fill("contacto@tecnocentro.com");
  await page.getByLabel("Teléfono").fill("+573001119999");
  await page.getByLabel("País").selectOption("1");
  await page.getByLabel("Departamento").selectOption("2");
  await page.getByLabel("Ciudad").selectOption("3");
  await page.getByLabel("Dirección").fill("Calle 10 # 2-30");
  await page.getByLabel("Enlace del documento legal").fill("https://archivos.example.com/camara-comercio.pdf");
}

test("un usuario sin negocio puede solicitarlo y seguir el estado pendiente", async ({ page }) => {
  await mockOwner(page, []);
  await page.goto("/app");

  await expect(page.getByRole("heading", { name: "Aún no tienes un negocio asociado" })).toBeVisible();
  await page.getByRole("link", { name: "Solicitar un negocio" }).click();
  await expect(page).toHaveURL(/\/app\/negocios\/solicitud$/);
  await fillRequestForm(page);
  await page.getByRole("button", { name: "Enviar solicitud" }).click();

  await expect(page.getByText("La solicitud fue enviada.")).toBeVisible();
  await expect(page.getByRole("heading", { name: "Tecno Centro está en revisión" })).toBeVisible();
  await expect(page.getByRole("button", { name: "Enviar solicitud" })).toHaveCount(0);
  await expect(page.getByRole("article").getByText("Pendiente", { exact: true })).toBeVisible();
  await page.screenshot({ path: ".impeccable/review/owner-business-request-pending-desktop.png", fullPage: true });
});

test("una solicitud rechazada explica el motivo y permite corregirla en móvil", async ({ page }) => {
  await page.setViewportSize({ width: 360, height: 800 });
  await mockOwner(page, [request("REJECTED")]);
  await page.goto("/app/negocios/solicitud");

  await expect(page.getByText("Rechazada", { exact: true })).toBeVisible();
  await expect(page.getByText("El documento adjunto no permite verificar la razón social.")).toBeVisible();
  await expect(page.getByRole("button", { name: "Enviar solicitud" })).toBeVisible();
  await page.screenshot({ path: ".impeccable/review/owner-business-request-rejected-mobile.png", fullPage: true });
});

test("un conflicto conserva los datos introducidos", async ({ page }) => {
  await mockOwner(page, [], { conflictOnCreate: true });
  await page.goto("/app/negocios/solicitud");
  await fillRequestForm(page);
  await page.getByRole("button", { name: "Enviar solicitud" }).click();

  await expect(page.getByText("Ya existe una solicitud para este documento.")).toBeVisible();
  await expect(page.getByLabel("Nombre del negocio")).toHaveValue("Tecno Centro");
  await expect(page.getByLabel("Enlace del documento legal")).toHaveValue("https://archivos.example.com/camara-comercio.pdf");
});

test("una solicitud aprobada ofrece renovar el acceso", async ({ page }) => {
  await mockOwner(page, [request("APPROVED")]);
  await page.goto("/app/negocios/solicitud");
  await page.getByRole("button", { name: "Volver a ingresar" }).click();

  await expect(page).toHaveURL(/\/login\?acceso=actualizado&next=\/app$/);
  await expect(page.getByText("Inicia sesión nuevamente para cargar los accesos de tu negocio aprobado.")).toBeVisible();
});
