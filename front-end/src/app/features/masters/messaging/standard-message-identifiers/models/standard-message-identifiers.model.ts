import { AuditableModel } from '@shared/models/auditable.model';

export interface StandardMessageIdentifier extends AuditableModel {
    id?: string;
    messageIdentifier?: string;
    messageName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
