import client from "./internal/httpClient";

export function appConfig() {
  return client.get("/backend/v1/app-config", {});
}

export function saveAppConfig(data: any) {
  return client.put(`/backend/v1/app-config`, { data: data });
}
