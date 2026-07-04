import { AuditableModel } from '@shared/models/auditable.model';

export interface Timezone extends AuditableModel {
    id?: string;
    zoneId?: string;
    utcOffset?: string;
    isDstSupported?: boolean;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
}
