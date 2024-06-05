import client from "./internal/httpClient";

export function adminUserList(
  page: number,
  size: number,
  name: string,
  roleId: number
) {
  return client.get("/backend/v1/admin-user/index", {
    page: page,
    size: size,
    name: name,
    role_id: roleId,
  });
}

export function createAdminUser() {
  return client.get("/backend/v1/admin-user/create", {});
}

export function storeAdminUser(
  name: string,
  email: string,
  password: string,
  isBanLogin: number,
  roleIds: number[]
) {
  return client.post("/backend/v1/admin-user/create", {
    name: name,
    email: email,
    password: password,
    is_ban_login: isBanLogin,
    role_ids: roleIds,
  });
}

export function AdminUser(id: number) {
  return client.get(`/backend/v1/admin-user/${id}`, {});
}

export function updateAdminUser(
  id: number,
  name: string,
  email: string,
  password: string,
  isBanLogin: number,
  roleIds: number[]
) {
  return client.put(`/backend/v1/admin-user/${id}`, {
    name: name,
    email: email,
    password: password,
    is_ban_login: isBanLogin,
    role_ids: roleIds,
  });
}

export function destroyAdminUser(id: number) {
  return client.destroy(`/backend/v1/admin-user/${id}`);
}
