import { AuditableModel } from '@shared/models/auditable.model';

export interface AirlineContact extends AuditableModel {
    id?: string;
    airlineId?: string;
    contactName?: string;
    contactType?: string;
    email?: string;
    phone?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
