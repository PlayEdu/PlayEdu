import client from "./internal/httpClient";

export function storeCourseAttachmentMulti(
  courseId: number,
  attachments: number[]
) {
  return client.post(`/backend/v1/course/${courseId}/attachment/create-batch`, {
    attachments: attachments,
  });
}

export function destroyAttachment(courseId: number, id: number) {
  return client.destroy(`/backend/v1/course/${courseId}/attachment/${id}`);
}

export function transCourseAttachment(courseId: number, ids: number[]) {
  return client.put(`/backend/v1/course/${courseId}/attachment/update/sort`, {
    ids: ids,
  });
}
