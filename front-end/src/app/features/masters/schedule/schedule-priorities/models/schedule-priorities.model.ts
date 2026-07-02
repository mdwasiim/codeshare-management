import { AuditableModel } from '@shared/models/auditable.model';

export interface SchedulePriority extends AuditableModel {
    id?: string;
    priorityCode?: string;
    priorityName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
