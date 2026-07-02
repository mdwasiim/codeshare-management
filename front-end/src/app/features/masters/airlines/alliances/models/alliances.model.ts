import { AuditableModel } from '@shared/models/auditable.model';

export interface Alliance extends AuditableModel {
    id?: string;
    allianceCode?: string;
    allianceName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
