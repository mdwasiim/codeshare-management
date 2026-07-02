import { AuditableModel } from '@shared/models/auditable.model';

export interface FlightServiceType extends AuditableModel {
    id?: string;
    serviceTypeCode?: string;
    serviceTypeName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
