import { AuditableModel } from '@shared/models/auditable.model';

export interface CodesharePartner extends AuditableModel {
    id?: string;
    airlineId?: string;
    partnerCode?: string;
    partnerName?: string;
    partnerType?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
