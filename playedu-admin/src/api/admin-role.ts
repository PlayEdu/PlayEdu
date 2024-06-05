import client from "./internal/httpClient";

export function adminRoleList() {
  return client.get("/backend/v1/admin-role/index", {});
}

export function createAdminRole() {
  return client.get("/backend/v1/admin-role/create", {});
}

export function storeAdminRole(name: string, permissionIds: number[]) {
  return client.post("/backend/v1/admin-role/create", {
    name: name,
    permission_ids: permissionIds,
  });
}

export function adminRole(id: number) {
  return client.get(`/backend/v1/admin-role/${id}`, {});
}

export function updateAdminRole(
  id: number,
  name: string,
  permissionIds: number[]
) {
  return client.put(`/backend/v1/admin-role/${id}`, {
    name: name,
    permission_ids: permissionIds,
  });
}

export function destroyAdminRole(id: number) {
  return client.destroy(`/backend/v1/admin-role/${id}`);
}
