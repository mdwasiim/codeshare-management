export interface AuditableModel {
    createdAt?: string;
    createdBy?: string;
    updatedAt?: string;
    updatedBy?: string;
    active?: boolean;
}
