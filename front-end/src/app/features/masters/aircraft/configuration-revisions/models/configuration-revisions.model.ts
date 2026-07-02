import { AuditableModel } from '@shared/models/auditable.model';

export interface AircraftConfigurationRevision extends AuditableModel {
    id?: string;
    configurationId?: string;
    revisionCode?: string;
    revisionName?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    recordStatus?: string;
}
