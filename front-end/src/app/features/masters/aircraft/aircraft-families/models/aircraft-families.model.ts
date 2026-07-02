import { AuditableModel } from '@shared/models/auditable.model';

export interface AircraftFamily extends AuditableModel {
    id?: string;
    familyCode?: string;
    familyName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
