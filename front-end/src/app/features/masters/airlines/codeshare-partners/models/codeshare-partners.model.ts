import { AuditableModel } from '@shared/models/auditable.model';

export interface CodesharePartner extends AuditableModel {
    id?: string;
    homeAirlineId?: string;
    partnerAirlineId?: string;
    homeAirlineCode?: string;
    homeAirlineName?: string;
    partnerAirlineCode?: string;
    partnerAirlineName?: string;
    agreementNumber?: string;
    agreementType?: string;
    agreementStatus?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
