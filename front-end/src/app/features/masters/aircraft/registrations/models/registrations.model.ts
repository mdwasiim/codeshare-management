import { AuditableModel } from '@shared/models/auditable.model';

export interface AircraftRegistration extends AuditableModel {
    id?: string;
    registrationNumber?: string;
    aircraftTypeId?: string;
    ownerId?: string;
    active?: boolean;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
}
