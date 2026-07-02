import { AuditableModel } from '@shared/models/auditable.model';

export interface Timezone extends AuditableModel {
    id?: string;
    timezoneCode?: string;
    timezoneName?: string;
    utcOffset?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
