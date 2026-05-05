export interface Role {
    id?: string;

    code: string;
    displayName: string;
    description?: string;
    tenantId: string;

    createdAt?: string;
    updatedAt?: string;
}
