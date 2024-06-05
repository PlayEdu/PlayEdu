import client from "./internal/httpClient";

export function config() {
  return client.get("/api/v1/system/config", {});
}

export function imageCaptcha() {
  return client.get("/api/v1/system/image-captcha", {});
}
