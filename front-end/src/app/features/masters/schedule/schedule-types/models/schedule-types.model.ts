import { AuditableModel } from '@shared/models/auditable.model';

export interface ScheduleType extends AuditableModel {
    id?: string;
    typeCode?: string;
    typeName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
