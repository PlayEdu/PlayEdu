import client from "./internal/httpClient";

export function resourceCategoryList() {
  return client.get("/backend/v1/resource-category/index", {});
}

export function createResourceCategory() {
  return client.get("/backend/v1/resource-category/create", {});
}

export function storeResourceCategory(
  name: string,
  parentId: number,
  sort: number
) {
  return client.post("/backend/v1/resource-category/create", {
    name,
    parent_id: parentId,
    sort,
  });
}

export function resourceCategory(id: number) {
  return client.get(`/backend/v1/resource-category/${id}`, {});
}

export function updateResourceCategory(
  id: number,
  name: string,
  parentId: number,
  sort: number
) {
  return client.put(`/backend/v1/resource-category/${id}`, {
    name,
    parent_id: parentId,
    sort,
  });
}

export function destroyResourceCategory(id: number) {
  return client.destroy(`/backend/v1/resource-category/${id}`);
}

export function dropSameClass(ids: number[]) {
  return client.put(`/backend/v1/resource-category/update/sort`, {
    ids: ids,
  });
}

export function dropDiffClass(id: number, parent_id: number, ids: number[]) {
  return client.put(`/backend/v1/resource-category/update/parent`, {
    id: id,
    parent_id: parent_id,
    ids: ids,
  });
}

export function checkDestroy(id: number) {
  return client.get(`/backend/v1/resource-category/${id}/destroy`, {});
}
