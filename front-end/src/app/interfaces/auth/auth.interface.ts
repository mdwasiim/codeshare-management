export interface User {
  id: number;
  username: string;
  email: string;
  roles: string[]; 
  permissions: string[]; 
}

export interface AuthTokens {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
}

export interface LoginResponse {
  token: string; 
  type: string; 
  user: User; 
  expiresIn: number; 
}