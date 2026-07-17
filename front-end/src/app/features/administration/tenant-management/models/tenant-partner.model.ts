import { AuditableModel } from '@shared/models/auditable.model';

export interface TenantPartner extends AuditableModel {
    id?: number;
    tenantId?: number;
    homeAirlineId?: number;
    partnerAirlineId?: number;
    homeAirlineCode?: string;
    homeAirlineName?: string;
    partnerAirlineCode?: string;
    partnerAirlineName?: string;
    agreementNumber?: string;
    agreementType?: string;
    agreementStatus?: string;
    active?: boolean;
    displayOrder?: number;
    description?: string;
    remarks?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
}
