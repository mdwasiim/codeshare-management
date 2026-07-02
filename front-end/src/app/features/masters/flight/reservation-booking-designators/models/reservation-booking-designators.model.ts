import { AuditableModel } from '@shared/models/auditable.model';

export interface ReservationBookingDesignator extends AuditableModel {
    id?: string;
    rbdCode?: string;
    rbdName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
