import client from "./internal/httpClient";

export function dashboardList() {
  return client.get("/backend/v1/dashboard/index", {});
}
