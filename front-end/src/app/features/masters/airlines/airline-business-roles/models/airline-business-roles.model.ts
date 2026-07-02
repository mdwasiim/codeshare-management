import { AuditableModel } from '@shared/models/auditable.model';

export interface AirlineBusinessRole extends AuditableModel {
    id?: string;
    roleCode?: string;
    roleName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
