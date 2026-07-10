import { AuditableModel } from '@shared/models/auditable.model';

export interface TenantPartnerCommunicationProfile extends AuditableModel {
    id?: string;
    partnerId?: string;
    profileCode?: string;
    profileName?: string;
    protocol?: string;
    transportType?: string;
    messageFormat?: string;
    authenticationType?: string;
    endpointUrl?: string;
    username?: string;
    credentialAlias?: string;
    connectionTimeout?: number;
    readTimeout?: number;
    retryCount?: number;
    compressionEnabled?: boolean;
    encryptionEnabled?: boolean;
    active?: boolean;
    displayOrder?: number;
    description?: string;
    remarks?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
}
