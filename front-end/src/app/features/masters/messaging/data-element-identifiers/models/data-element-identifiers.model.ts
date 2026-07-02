import { AuditableModel } from '@shared/models/auditable.model';

export interface DataElementIdentifier extends AuditableModel {
    id?: string;
    deiCode?: string;
    deiName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
