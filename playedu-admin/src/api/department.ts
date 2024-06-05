import client from "./internal/httpClient";

export function departmentList(params: any) {
  return client.get("/backend/v1/department/index", params);
}

export function createDepartment() {
  return client.get("/backend/v1/department/create", {});
}

export function storeDepartment(name: string, parentId: number, sort: number) {
  return client.post("/backend/v1/department/create", {
    name,
    parent_id: parentId,
    sort,
  });
}

export function department(id: number) {
  return client.get(`/backend/v1/department/${id}`, {});
}

export function updateDepartment(
  id: number,
  name: string,
  parentId: number,
  sort: number
) {
  return client.put(`/backend/v1/department/${id}`, {
    name,
    parent_id: parentId,
    sort,
  });
}

export function destroyDepartment(id: number) {
  return client.destroy(`/backend/v1/department/${id}`);
}

export function dropSameClass(ids: number[]) {
  return client.put(`/backend/v1/department/update/sort`, {
    ids: ids,
  });
}

export function dropDiffClass(id: number, parent_id: number, ids: number[]) {
  return client.put(`/backend/v1/department/update/parent`, {
    id: id,
    parent_id: parent_id,
    ids: ids,
  });
}

export function checkDestroy(id: number) {
  return client.get(`/backend/v1/department/${id}/destroy`, {});
}

export function ldapSync() {
  return client.post(`/backend/v1/department/ldap-sync`, {});
}
