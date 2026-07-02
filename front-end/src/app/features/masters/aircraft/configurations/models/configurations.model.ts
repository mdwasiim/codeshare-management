import { AuditableModel } from '@shared/models/auditable.model';

export interface AircraftConfiguration extends AuditableModel {
    id?: string;
    configurationCode?: string;
    configurationName?: string;
    aircraftTypeId?: string;
    totalSeats?: number;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
