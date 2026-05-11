export interface Role {
    id?: string;

    code: string;
    name: string;
    description?: string;
    tenantId: string;

    createdAt?: string;
    updatedAt?: string;
}
