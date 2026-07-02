import { AuditableModel } from '@shared/models/auditable.model';

export interface ScheduleChannel extends AuditableModel {
    id?: string;
    channelCode?: string;
    channelName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
