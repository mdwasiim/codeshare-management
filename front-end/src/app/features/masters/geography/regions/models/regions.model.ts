import { AuditableModel } from '@shared/models/auditable.model';

export interface Region extends AuditableModel {
    id?: string;
    regionCode?: string;
    regionName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
