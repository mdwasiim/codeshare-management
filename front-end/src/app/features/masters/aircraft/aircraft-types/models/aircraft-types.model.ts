import { AuditableModel } from '@shared/models/auditable.model';

export interface AircraftType extends AuditableModel {
    id?: string;
    manufacturer?: string;
    modelCode?: string;
    icaoCode?: string;
    iataCode?: string;
    engineType?: string;
    typicalSeatCapacity?: number;
    maxRangeKm?: number;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
