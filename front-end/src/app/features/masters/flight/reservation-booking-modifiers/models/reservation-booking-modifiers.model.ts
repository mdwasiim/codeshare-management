import { AuditableModel } from '@shared/models/auditable.model';

export interface ReservationBookingModifier extends AuditableModel {
    id?: string;
    modifierCode?: string;
    modifierName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
