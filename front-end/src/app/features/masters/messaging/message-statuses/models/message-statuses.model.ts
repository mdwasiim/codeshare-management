import { AuditableModel } from '@shared/models/auditable.model';

export interface MessageStatus extends AuditableModel {
    id?: string;
    statusCode?: string;
    statusName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
