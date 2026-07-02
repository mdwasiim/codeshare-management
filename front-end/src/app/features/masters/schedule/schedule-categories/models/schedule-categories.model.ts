import { AuditableModel } from '@shared/models/auditable.model';

export interface ScheduleCategory extends AuditableModel {
    id?: string;
    categoryCode?: string;
    categoryName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
