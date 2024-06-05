import client from "./internal/httpClient";

export function adminLogList(
  page: number,
  size: number,
  admin_name: string,
  title: string,
  opt: string,
  start_time: string,
  end_time: string
) {
  return client.get("/backend/v1/admin/log/index", {
    page: page,
    size: size,
    admin_name: admin_name,
    title: title,
    opt: opt,
    start_time: start_time,
    end_time: end_time,
  });
}

export function adminLogDetail(id: number) {
  return client.get(`/backend/v1/admin/log/detail/${id}`, {});
}
