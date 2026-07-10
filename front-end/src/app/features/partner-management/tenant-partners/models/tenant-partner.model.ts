import { AuditableModel } from '@shared/models/auditable.model';

export interface TenantPartner extends AuditableModel {
    id?: string;
    tenantId?: string;
    homeAirlineId?: string;
    partnerAirlineId?: string;
    homeAirlineCode?: string;
    homeAirlineName?: string;
    partnerAirlineCode?: string;
    partnerAirlineName?: string;
    agreementNumber?: string;
    agreementType?: string;
    agreementStatus?: string;
    displayOrder?: number;
    description?: string;
    remarks?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
