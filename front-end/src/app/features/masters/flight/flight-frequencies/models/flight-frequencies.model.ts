import { AuditableModel } from '@shared/models/auditable.model';

export interface FlightFrequency extends AuditableModel {
    id?: string;
    frequencyCode?: string;
    frequencyName?: string;
    daysOfOperation?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
