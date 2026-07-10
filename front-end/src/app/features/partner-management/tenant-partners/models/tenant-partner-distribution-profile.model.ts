import { AuditableModel } from '@shared/models/auditable.model';

export interface TenantPartnerDistributionProfile extends AuditableModel {
    id?: string;
    partnerId?: string;
    profileCode?: string;
    profileName?: string;
    distributionChannel?: string;
    distributionMode?: string;
    messageType?: string;
    realTimeEnabled?: boolean;
    acknowledgementRequired?: boolean;
    retryEnabled?: boolean;
    retryCount?: number;
    active?: boolean;
    displayOrder?: number;
    description?: string;
    remarks?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
}
