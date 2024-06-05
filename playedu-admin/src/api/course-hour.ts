import client from "./internal/httpClient";

export function courseHourList(courseId: number) {
  return client.get(`/backend/v1/course/${courseId}/hour/index`, {});
}

export function createCourseHour(courseId: number) {
  return client.get(`/backend/v1/course/${courseId}/hour/create`, {});
}

export function storeCourseHour(
  courseId: number,
  chapterId: number,
  title: string,
  type: string,
  druation: number,
  rid: number
) {
  return client.post(`/backend/v1/course/${courseId}/hour/create`, {
    chapter_id: chapterId,
    title,
    type,
    druation,
    sort: 0,
    rid,
  });
}

export function storeCourseHourMulti(courseId: number, hours: number[]) {
  return client.post(`/backend/v1/course/${courseId}/hour/create-batch`, {
    hours: hours,
  });
}

export function courseHour(courseId: number, id: number) {
  return client.get(`/backend/v1/course/${courseId}/hour/${id}`, {});
}

export function updateCourseHour(
  courseId: number,
  id: number,
  chapterId: number,
  title: string,
  type: string,
  druation: number,
  rid: number
) {
  return client.put(`/backend/v1/course/${courseId}/hour/${id}`, {
    chapter_id: chapterId,
    title,
    type,
    druation,
    sort: 0,
    rid,
  });
}

export function destroyCourseHour(courseId: number, id: number) {
  return client.destroy(`/backend/v1/course/${courseId}/hour/${id}`);
}

export function transCourseHour(courseId: number, ids: number[]) {
  return client.put(`/backend/v1/course/${courseId}/hour/update/sort`, {
    ids: ids,
  });
}
