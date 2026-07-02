import { AuditableModel } from '@shared/models/auditable.model';

export interface AirlineCarrier extends AuditableModel {
    id?: string;
    iataCode?: string;
    icaoCode?: string;
    iataNumericCode?: string;
    legalName?: string;
    commercialName?: string;
    displayName?: string;
    callsign?: string;
    website?: string;
    email?: string;
    phone?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
