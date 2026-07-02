import { AuditableModel } from '@shared/models/auditable.model';

export interface Airport extends AuditableModel {
    id?: string;
    iataCode?: string;
    icaoCode?: string;
    airportName?: string;
    cityId?: string;
    countryId?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
