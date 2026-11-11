import http from 'k6/http';
import { check, fail, sleep } from 'k6';

const baseUrl = (__ENV.QA_BASE_URL || '').replace(/\/$/, '');
const email = __ENV.QA_EMAIL;
const password = __ENV.QA_PASSWORD;
const businessId = __ENV.QA_BUSINESS_ID;
const orderId = __ENV.QA_ORDER_ID;
const searchPath = __ENV.QA_SEARCH_PATH || '/api/v1/public/locations/countries';

if (!baseUrl || !email || !password || !businessId) {
  throw new Error('QA_BASE_URL, QA_EMAIL, QA_PASSWORD and QA_BUSINESS_ID are required');
}

export const options = {
  scenarios: {
    login: {
      executor: 'constant-arrival-rate',
      exec: 'loginScenario',
      rate: Number(__ENV.QA_LOGIN_RATE || 1),
      timeUnit: '1s',
      duration: __ENV.QA_DURATION || '2m',
      preAllocatedVUs: 5,
      maxVUs: 20,
    },
    authenticatedReads: {
      executor: 'constant-vus',
      exec: 'authenticatedReadsScenario',
      vus: Number(__ENV.QA_READ_VUS || 10),
      duration: __ENV.QA_DURATION || '2m',
    },
  },
  thresholds: {
    http_req_failed: ['rate<0.01'],
    http_req_duration: ['p(95)<1500', 'p(99)<3000'],
    checks: ['rate>0.99'],
  },
};

function login() {
  const response = http.post(
    `${baseUrl}/api/v1/auth/login`,
    JSON.stringify({ email, password }),
    { headers: { 'Content-Type': 'application/json' }, tags: { operation: 'login' } },
  );
  const valid = check(response, { 'login returns 200': (result) => result.status === 200 });
  if (!valid) {
    fail(`Login failed with status ${response.status}`);
  }
  return response.json('data.accessToken');
}

export function setup() {
  const token = login();
  if (!token) {
    fail('Login response did not include data.accessToken');
  }
  return { token };
}

export function loginScenario() {
  login();
  sleep(1);
}

export function authenticatedReadsScenario(data) {
  const auth = { headers: { Authorization: `Bearer ${data.token}` } };
  const search = http.get(`${baseUrl}${searchPath}`, { tags: { operation: 'search' } });
  check(search, { 'search returns 200': (response) => response.status === 200 });

  const orders = http.get(
    `${baseUrl}/api/v1/businesses/${businessId}/repair-orders?page=0&size=20`,
    { ...auth, tags: { operation: 'orders' } },
  );
  check(orders, { 'orders return 200': (response) => response.status === 200 });

  if (orderId) {
    const files = http.get(
      `${baseUrl}/api/v1/businesses/${businessId}/repair-orders/${orderId}/attachments?page=0&size=20`,
      { ...auth, tags: { operation: 'files' } },
    );
    check(files, { 'attachments return 200': (response) => response.status === 200 });
  }
  sleep(1);
}
