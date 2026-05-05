export interface Permission {
    id?: string;

    displayName: string;
    code?: string; // backend may auto-generate

    description?: string;

    domain: string;
    action: string;

    createdAt?: string;
    updatedAt?: string;
}
