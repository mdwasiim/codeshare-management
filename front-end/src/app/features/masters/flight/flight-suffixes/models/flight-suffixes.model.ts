import { AuditableModel } from '@shared/models/auditable.model';

export interface FlightSuffix extends AuditableModel {
    id?: string;
    suffixCode?: string;
    suffixName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
