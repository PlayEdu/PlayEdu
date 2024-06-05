import client from "./internal/httpClient";

export function login(email: string, password: string) {
  return client.post("/api/v1/auth/login/password", {
    email: email,
    password: password,
  });
}

export function logout() {
  return client.post("/api/v1/auth/logout", {});
}

export function loginLdap(email: string, password: string) {
  return client.post("/api/v1/auth/login/ldap", {
    username: email,
    password: password,
  });
}
