export interface LoginResponse {
  external_id: string;
  username: string;
  email: string;
  tenant_code: string;
  roles: string[];
  permissions: string[];
  access_token: string;
  refresh_token: string;
  expires_in: number;
}
