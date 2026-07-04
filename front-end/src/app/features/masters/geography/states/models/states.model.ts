import { AuditableModel } from '@shared/models/auditable.model';

export interface State extends AuditableModel {
    id?: string;
    code?: string;
    name?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
    countryId?: string;
}
