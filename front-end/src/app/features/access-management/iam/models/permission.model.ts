import { AuditableModel } from '@shared/models/auditable.model';

export interface Permission extends AuditableModel {
    id?: string;

    name: string;
    code?: string;

    description?: string;

    domain: string;
    action: string;
}
