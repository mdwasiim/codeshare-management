export interface AuthSessionResponse {
    user_id: string;
    username: string;
    email: string;
    tenant_id: string;
    tenant_code: string;
    roles: string[];
    permissions: string[];
    groups: string[];
}
