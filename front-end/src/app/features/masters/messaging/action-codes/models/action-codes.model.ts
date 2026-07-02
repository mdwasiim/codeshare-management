import { AuditableModel } from '@shared/models/auditable.model';

export interface ActionCode extends AuditableModel {
    id?: string;
    actionCode?: string;
    actionName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
