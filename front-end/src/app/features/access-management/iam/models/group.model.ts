export interface Group {
    id?: string; // optional for create
    code: string;
    name: string;
    description?: string;
    tenantId: string;

    // inherited from AppAuditableDTO (optional)
    createdAt?: string;
    updatedAt?: string;
    createdBy?: string;
    updatedBy?: string;
    active?: boolean;
}
