import { AuditableModel } from '@shared/models/auditable.model';

export interface ActionIdentifier extends AuditableModel {
    id?: string;
    actionIdentifier?: string;
    actionName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
