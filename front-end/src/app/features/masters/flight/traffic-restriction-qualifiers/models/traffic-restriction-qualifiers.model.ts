import { AuditableModel } from '@shared/models/auditable.model';

export interface TrafficRestrictionQualifier extends AuditableModel {
    id?: string;
    qualifierCode?: string;
    qualifierName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
