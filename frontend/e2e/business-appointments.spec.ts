import { mkdir } from "node:fs/promises";
import path from "node:path";

import { expect, test, type Page, type Route } from "@playwright/test";

import type { Appointment } from "@/types/appointment";

const reviewDir = path.resolve(".impeccable/review");

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

const statuses = [
  { id: 1, businessId: 10, catalogType: "APPOINTMENT_STATUS", code: "SCHEDULED", name: "Agendada", description: null, icon: null, sortOrder: 10, protectedItem: true, active: true, createdAt: "2026-01-01T08:00:00", updatedAt: "2026-01-01T08:00:00" },
  { id: 2, businessId: 10, catalogType: "APPOINTMENT_STATUS", code: "CONFIRMED", name: "Confirmada", description: null, icon: null, sortOrder: 20, protectedItem: true, active: true, createdAt: "2026-01-01T08:00:00", updatedAt: "2026-01-01T08:00:00" },
  { id: 3, businessId: 10, catalogType: "APPOINTMENT_STATUS", code: "COMPLETED", name: "Completada", description: null, icon: null, sortOrder: 30, protectedItem: true, active: true, createdAt: "2026-01-01T08:00:00", updatedAt: "2026-01-01T08:00:00" },
];

const devices = [
  {
    id: 301,
    businessId: 10,
    customerId: 21,
    customerDocumentTypeCode: "CC",
    customerDocumentNumber: "55157801",
    customerName: "María",
    customerSurname: "Pérez",
    customerEmail: "maria@example.com",
    deviceTypeId: 11,
    deviceTypeCode: "PHONE",
    deviceTypeName: "Celular",
    brandId: 12,
    brandName: "Apple",
    model: "iPhone 13 Pro",
    serialNumber: "SN-ABC-12345",
    imei: "351756051523999",
    registeredByBusinessUserId: 71,
    registeredByBusinessUserName: "Laura Méndez",
    color: "Azul",
    registeredAt: "2026-09-20T10:00:00",
    active: true,
  },
];

const services = [
  {
    id: 401,
    businessId: 10,
    serviceCategoryId: 31,
    serviceCategoryCode: "REPAIR",
    serviceCategoryName: "Reparación",
    deviceTypeId: 11,
    deviceTypeCode: "PHONE",
    deviceTypeName: "Celular",
    name: "Diagnóstico de encendido",
    description: "Revisión inicial del circuito de alimentación.",
    basePrice: 60000,
    estimatedTimeMinutes: 60,
    imageUrl: null,
    active: true,
  },
];

function appointment(overrides: Partial<Appointment> = {}): Appointment {
  return {
    id: 501,
    businessId: 10,
    branchId: 101,
    branchName: "Sede principal",
    customerId: 21,
    customerName: "María",
    customerSurname: "Pérez",
    customerEmail: "maria@example.com",
    customerDeviceId: 301,
    customerDeviceLabel: "Apple iPhone 13 Pro",
    businessServiceId: 401,
    businessServiceName: "Diagnóstico de encendido",
    statusId: 1,
    statusCode: "SCHEDULED",
    statusName: "Agendada",
    scheduledAt: "2026-09-24T09:30:00",
    problemDescription: "El equipo no enciende después de una caída.",
    active: true,
    ...overrides,
  };
}

async function fulfill(route: Route, data: unknown, status = 200, message = "ok") {
  await route.fulfill({
    status,
    contentType: "application/json",
    body: JSON.stringify({ success: status < 400, message, data }),
  });
}

function pageData<T>(items: T[]) {
  return {
    items,
    page: 0,
    size: 20,
    totalElements: items.length,
    totalPages: items.length ? 1 : 0,
    hasNext: false,
    hasPrevious: false,
  };
}

async function mockAppointments(page: Page) {
  let records = [
    appointment(),
    appointment({ id: 502, statusId: 2, statusCode: "CONFIRMED", statusName: "Confirmada", scheduledAt: "2026-09-24T14:00:00", problemDescription: "La pantalla presenta líneas después de recibir humedad." }),
    appointment({ id: 503, customerName: "Carlos", customerSurname: "Rojas", customerEmail: "carlos@example.com", scheduledAt: "2026-09-25T10:15:00", problemDescription: "El equipo se reinicia al conectar el cargador." }),
  ];

  await page.route("https://api-dev.juanodsk.dev/**", async (route) => {
    const url = new URL(route.request().url());
    const apiPath = url.pathname;
    const method = route.request().method();

    if (apiPath === "/api/v1/auth/refresh") return fulfill(route, { accessToken: "owner-token", user });
    if (apiPath === "/api/v1/auth/me") return fulfill(route, user);
    if (apiPath === "/api/v1/auth/me/permissions") return fulfill(route, { userId: 7, roles: ["USER"], permissions: [] });
    if (apiPath === "/api/v1/businesses/my") return fulfill(route, [business]);
    if (apiPath === "/api/v1/businesses/10/me/context") {
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
        permissions: [
          "APPOINTMENT_VIEW",
          "APPOINTMENT_CREATE",
          "APPOINTMENT_UPDATE",
          "CUSTOMER_DEVICE_VIEW",
          "BUSINESS_SERVICE_VIEW",
          "BUSINESS_CATALOG_VIEW",
        ],
        branches,
      });
    }
    if (apiPath === "/api/v1/businesses/10/customer-devices") return fulfill(route, pageData(devices));
    if (apiPath === "/api/v1/businesses/10/services") return fulfill(route, pageData(services));
    if (apiPath === "/api/v1/businesses/10/catalogs/APPOINTMENT_STATUS") return fulfill(route, statuses);
    if (apiPath === "/api/v1/businesses/10/appointments" && method === "GET") return fulfill(route, pageData(records));
    if (apiPath === "/api/v1/businesses/10/appointments" && method === "POST") {
      const body = route.request().postDataJSON() as Record<string, unknown>;
      const created = appointment({ id: 504, scheduledAt: String(body.scheduledAt), problemDescription: String(body.problemDescription) });
      records = [created, ...records];
      return fulfill(route, created, 201, "Cita registrada.");
    }

    const statusMatch = apiPath.match(/^\/api\/v1\/businesses\/10\/appointments\/(\d+)\/status$/);
    if (statusMatch && method === "PATCH") {
      const id = Number(statusMatch[1]);
      const body = route.request().postDataJSON() as { statusId: number };
      const status = statuses.find((item) => item.id === body.statusId)!;
      records = records.map((item) => item.id === id ? { ...item, statusId: status.id, statusCode: status.code, statusName: status.name } : item);
      return fulfill(route, records.find((item) => item.id === id));
    }
    return fulfill(route, null, 404, "No encontrado");
  });
}

test("muestra la agenda y actualiza el estado", async ({ page }) => {
  await mkdir(reviewDir, { recursive: true });
  await page.setViewportSize({ width: 1440, height: 1000 });
  await mockAppointments(page);
  await page.goto("/app/negocios/10/citas");

  await expect(page.getByRole("heading", { name: "Citas", exact: true })).toBeVisible();
  await expect(page.getByRole("heading", { name: "María Pérez" }).first()).toBeVisible();
  await page.getByLabel("Estado de la cita de María Pérez").first().selectOption("2");
  await expect(page.getByText("El estado de la cita quedó actualizado.")).toBeVisible();
  await page.screenshot({ path: path.join(reviewDir, "appointments-desktop.png"), fullPage: true });
});

test("abre el formulario de una cita en móvil y tema oscuro", async ({ page }) => {
  await mkdir(reviewDir, { recursive: true });
  await page.setViewportSize({ width: 360, height: 800 });
  await mockAppointments(page);
  await page.goto("/app/negocios/10/citas");
  await page.getByRole("button", { name: "Usar tema oscuro" }).click();
  await page.screenshot({ path: path.join(reviewDir, "appointments-mobile-dark.png"), fullPage: true });
  await page.getByRole("button", { name: "Agendar cita" }).click();

  await expect(page.getByRole("heading", { name: "Agendar cita" })).toBeVisible();
  await expect(page.getByLabel("Sede")).toHaveValue("101");
  await expect(page.getByLabel("Estado", { exact: true })).toHaveValue("1");
  await page.screenshot({ path: path.join(reviewDir, "appointments-editor-mobile-dark.png") });

  await page.getByLabel("Fecha y hora").fill("2026-09-26T11:30");
  await page.getByLabel("Cliente", { exact: true }).selectOption("21");
  await page.getByLabel("Dispositivo", { exact: true }).selectOption("301");
  await page.getByLabel("Servicio", { exact: true }).selectOption("401");
  await page.getByLabel("Motivo de la cita").fill("El equipo pierde carga durante el uso.");
  await page.locator(".tm-appointment-form").getByRole("button", { name: "Agendar cita" }).click();
  await expect(page.getByText("La cita quedó registrada en la agenda.")).toBeVisible();
});
