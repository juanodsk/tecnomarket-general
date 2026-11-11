import { expect, test } from "@playwright/test";

test("muestra la presentación pública y permite abrir el login", async ({ page }) => {
  await page.goto("/");
  await expect(page.getByRole("heading", { name: "Cada reparación necesita una historia verificable." })).toBeVisible();
  await page.getByRole("link", { name: "Ingresar", exact: true }).first().click();
  await expect(page).toHaveURL(/\/login$/);
  await expect(page.getByRole("heading", { name: "Continúa tu operación" })).toBeVisible();
});

test("el login anuncia errores de validación sin borrar el formulario", async ({ page }) => {
  await page.goto("/login");
  await page.getByRole("textbox", { name: "Correo electrónico" }).fill("correo-invalido");
  await page.getByRole("button", { name: "Ingresar" }).click();
  await expect(page.getByText("Ingresa un correo válido.")).toBeVisible();
  await expect(page.getByRole("textbox", { name: "Correo electrónico" })).toHaveValue("correo-invalido");
});

test("las rutas públicas no intentan renovar una sesión ausente", async ({ page }) => {
  const refreshRequests: string[] = [];
  page.on("request", (request) => {
    if (new URL(request.url()).pathname === "/api/v1/auth/refresh") refreshRequests.push(request.url());
  });

  await page.goto("/login");
  await expect(page.getByRole("heading", { name: "Continúa tu operación" })).toBeVisible();
  await page.waitForTimeout(250);
  expect(refreshRequests).toEqual([]);
});
