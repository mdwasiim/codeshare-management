export interface Permission {
    id?: string;

    name: string;
    code?: string; // backend may auto-generate

    description?: string;

    domain: string;
    action: string;

    createdAt?: string;
    createdBy?: string;
    updatedAt?: string;
    updatedBy?: string;
    active?: boolean;
}
