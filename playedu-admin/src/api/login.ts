import client from "./internal/httpClient";

export function login(email: string, password: string) {
  return client.post("/backend/v1/auth/login", {
    email: email,
    password: password,
  });
}

export function logout() {
  return client.post("/backend/v1/auth/logout", {});
}

export function getUser() {
  return client.get("/backend/v1/auth/detail", {});
}

export function passwordChange(oldPassword: string, newPassword: string) {
  return client.put("/backend/v1/auth/password", {
    old_password: oldPassword,
    new_password: newPassword,
  });
}
