import { AuditableModel } from '@shared/models/auditable.model';

export interface TrafficRestriction extends AuditableModel {
    id?: string;
    restrictionCode?: string;
    restrictionName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
