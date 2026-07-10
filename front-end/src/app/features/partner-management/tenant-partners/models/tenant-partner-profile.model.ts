import { AuditableModel } from '@shared/models/auditable.model';

export interface TenantPartnerProfile extends AuditableModel {
    id?: string;
    partnerId?: string;
    profileCode?: string;
    profileName?: string;
    partnerType?: string;
    agreementCategory?: string;
    inventorySharingType?: string;
    priority?: number;
    autoAcceptScheduleChanges?: boolean;
    prorationApplicable?: boolean;
    eTicketAllowed?: boolean;
    active?: boolean;
    displayOrder?: number;
    description?: string;
    remarks?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
}
