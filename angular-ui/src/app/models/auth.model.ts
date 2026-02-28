export interface User {
  userId: number;
  username: string;
  name: string;
  email: string;
  role: string;
  token: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  username: string;
  password: string;
}

export interface AuthResponse {
  userId: number;
  username: string;
  name: string;
  email: string;
  role: string;
  token: string;
}
