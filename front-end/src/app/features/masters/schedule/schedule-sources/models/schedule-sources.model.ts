import { AuditableModel } from '@shared/models/auditable.model';

export interface ScheduleSource extends AuditableModel {
    id?: string;
    sourceCode?: string;
    sourceName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
