import client from "./internal/httpClient";

export function courseChapterList(courseId: number) {
  return client.get(`/backend/v1/course/${courseId}/chapter/index`, {});
}

export function createCourseChapter(courseId: number) {
  return client.get(`/backend/v1/course/${courseId}/chapter/create`, {});
}

export function storeCourseChapter(
  courseId: number,
  name: string,
  sort: number
) {
  return client.post(`/backend/v1/course/${courseId}/chapter/create`, {
    name: name,
    sort: sort,
  });
}

export function courseChapter(courseId: number, id: number) {
  return client.get(`/backend/v1/course/${courseId}/course-chapter/${id}`, {});
}

export function updateCourseChapter(
  courseId: number,
  id: number,
  name: string,
  sort: number
) {
  return client.put(`/backend/v1/course/${courseId}/chapter/${id}`, {
    name: name,
    sort: sort,
  });
}

export function destroyCourseChapter(courseId: number, id: number) {
  return client.destroy(`/backend/v1/course/${courseId}/chapter/${id}`);
}

export function transCourseChapter(courseId: number, ids: number[]) {
  return client.put(`/backend/v1/course/${courseId}/chapter/update/sort`, {
    ids: ids,
  });
}
