import client from "./internal/httpClient";

export function courseList(
  page: number,
  size: number,
  sortField: string,
  sortAlgo: string,
  title: string,
  depIds: string,
  categoryIds: string
) {
  return client.get("/backend/v1/course/index", {
    page: page,
    size: size,
    sort_field: sortField,
    sort_algo: sortAlgo,
    title: title,
    dep_ids: depIds,
    category_ids: categoryIds,
  });
}

export function createCourse() {
  return client.get("/backend/v1/course/create", {});
}

// depIds => 部门id数组，请用英文逗号连接
// categoryIds => 所属分类数组，请用英文逗号连接
export function storeCourse(
  title: string,
  thumb: string,
  shortDesc: string,
  isShow: number,
  isRequired: number,
  depIds: number[],
  categoryIds: number[],
  chapters: any[],
  hours: any[],
  attachments: any[]
) {
  return client.post("/backend/v1/course/create", {
    title: title,
    thumb: thumb,
    short_desc: shortDesc,
    is_show: isShow,
    is_required: isRequired,
    dep_ids: depIds,
    category_ids: categoryIds,
    chapters: chapters,
    hours: hours,
    attachments: attachments,
  });
}

export function course(id: number) {
  return client.get(`/backend/v1/course/${id}`, {});
}

export function updateCourse(
  id: number,
  title: string,
  thumb: string,
  shortDesc: string,
  isShow: number,
  isRequired: number,
  depIds: number[],
  categoryIds: number[],
  chapters: number[],
  hours: number[],
  publishedAt: string
) {
  return client.put(`/backend/v1/course/${id}`, {
    title: title,
    thumb: thumb,
    short_desc: shortDesc,
    is_show: isShow,
    is_required: isRequired,
    dep_ids: depIds,
    category_ids: categoryIds,
    chapters: chapters,
    hours: hours,
    sort_at: publishedAt,
  });
}

export function destroyCourse(id: number) {
  return client.destroy(`/backend/v1/course/${id}`);
}

//学员列表
export function courseUser(
  courseId: number,
  page: number,
  size: number,
  sortField: string,
  sortAlgo: string,
  name: string,
  email: string,
  idCard: string
) {
  return client.get(`/backend/v1/course/${courseId}/user/index`, {
    page: page,
    size: size,
    sort_field: sortField,
    sort_algo: sortAlgo,
    name: name,
    email: email,
    id_card: idCard,
  });
}

//删除学员
export function destroyCourseUser(courseId: number, ids: number[]) {
  return client.post(`/backend/v1/course/${courseId}/user/destroy`, {
    ids: ids,
  });
}
