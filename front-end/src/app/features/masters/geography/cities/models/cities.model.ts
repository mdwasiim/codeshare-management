import { AuditableModel } from '@shared/models/auditable.model';

export interface City extends AuditableModel {
    id?: string;
    iataCode?: string;
    name?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
    countryId?: string;
    stateId?: string;
}
