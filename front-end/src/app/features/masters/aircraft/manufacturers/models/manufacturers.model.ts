import { AuditableModel } from '@shared/models/auditable.model';

export interface AircraftManufacturer extends AuditableModel {
    id?: string;
    manufacturerCode?: string;
    manufacturerName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
