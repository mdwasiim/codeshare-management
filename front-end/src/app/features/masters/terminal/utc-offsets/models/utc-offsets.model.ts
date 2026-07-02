import { AuditableModel } from '@shared/models/auditable.model';

export interface UtcOffset extends AuditableModel {
    id?: string;
    offsetCode?: string;
    offsetValue?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
