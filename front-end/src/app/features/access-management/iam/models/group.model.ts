import { AuditableModel } from '@shared/models/auditable.model';

export interface Group extends AuditableModel {
    id?: string;
    code: string;
    name: string;
    description?: string;
    tenantId: string;
}
