import client from "./internal/httpClient";

export function courseCategoryList() {
  return client.get("/backend/v1/course-category/index", {});
}

export function createCourseCategory() {
  return client.get("/backend/v1/course-category/create", {});
}

export function storeCourseCategory(
  name: string,
  parentId: number,
  sort: number
) {
  return client.post("/backend/v1/course-category/create", {
    name: name,
    parent_id: parentId,
    sort: sort,
  });
}

export function courseCategory(id: number) {
  return client.get(`/backend/v1/course-category/${id}`, {});
}

export function updateCourseCategory(
  id: number,
  name: string,
  parentId: number,
  sort: number
) {
  return client.post(`/backend/v1/course-category/${id}`, {
    name: name,
    parent_id: parentId,
    sort: sort,
  });
}

export function destroyCourseCategory(id: number) {
  return client.destroy(`/backend/v1/course-category/${id}`);
}
