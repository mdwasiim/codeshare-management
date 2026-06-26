export interface Role {
    id?: string;

    code: string;
    name: string;
    description?: string;
    tenantId: string;

    createdAt?: string;
    createdBy?: string;
    updatedAt?: string;
    updatedBy?: string;
    active?: boolean;
}
