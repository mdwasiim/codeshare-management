import { AuditableModel } from '@shared/models/auditable.model';

export interface State extends AuditableModel {
    id?: string;
    stateCode?: string;
    stateName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
    countryId?: string;
}
