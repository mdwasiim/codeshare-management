export interface LoginResponse {
    external_id: string;
    username: string;
    email: string;
    tenant_id: string;
    tenant_code: string;
    roles: string[];
    permissions?: string[];
    groups: string[];
    access_token: string;
    refresh_token: string;
    expires_in: number;
}
