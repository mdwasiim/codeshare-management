import { AuditableModel } from '@shared/models/auditable.model';

export interface AircraftCabinLayout extends AuditableModel {
    id?: string;
    layoutCode?: string;
    layoutName?: string;
    cabinClass?: string;
    seatCount?: number;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
