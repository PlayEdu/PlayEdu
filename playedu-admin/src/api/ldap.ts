import client from "./internal/httpClient";

// 获取同步记录列表
export function getSyncRecords(params: { page?: number; size?: number }) {
  return client.get("/backend/v1/ldap/sync-records", params);
}

// 获取单条同步记录详情
export function getSyncRecordDetail(id: number) {
  return client.get(`/backend/v1/ldap/sync-records/${id}`, {});
}

// 获取同步记录的详细项目
export function getSyncRecordDetails(id: number, params: { 
  type: 'department' | 'user'; 
  action?: number; 
  page?: number; 
  size?: number 
}) {
  return client.get(`/backend/v1/ldap/sync-records/${id}/details`, params);
}

// 下载同步记录数据
export function downloadSyncRecord(id: number) {
  return client.get(`/backend/v1/ldap/sync-records/${id}/download`, {});
} 