import { AuditableModel } from '@shared/models/auditable.model';

export interface RejectReason extends AuditableModel {
    id?: string;
    reasonCode?: string;
    reasonName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
