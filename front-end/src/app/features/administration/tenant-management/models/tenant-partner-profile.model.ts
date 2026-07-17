import { AuditableModel } from '@shared/models/auditable.model';

export type RecordStatus = 'ACTIVE' | 'INACTIVE' | 'SUSPENDED' | 'TERMINATED' | 'EXPIRED';
export type PartnerType = 'OPERATING' | 'MARKETING' | 'RECIPROCAL';
export type AgreementCategory = 'BILATERAL' | 'ALLIANCE' | 'INTERLINE';
export type InventorySharingType = 'FREE_SALE' | 'BLOCK_SPACE' | 'SOFT_BLOCK' | 'HARD_BLOCK';
export type CommunicationProtocol = 'API' | 'MQ' | 'SFTP' | 'EMAIL' | 'AS2';
export type TransportType = 'REST' | 'SOAP' | 'JMS' | 'FTP' | 'SFTP' | 'HTTPS';
export type MessageFormat = 'SSIM' | 'ASM' | 'SSM' | 'XML' | 'JSON' | 'EDIFACT';
export type AuthenticationType = 'NONE' | 'BASIC' | 'API_KEY' | 'OAUTH2' | 'JWT' | 'CERTIFICATE' | 'SSH_KEY';
export type DistributionMode = 'REAL_TIME' | 'SCHEDULED' | 'MANUAL';
export type DistributionMessageType = 'SSIM' | 'SSM' | 'ASM';

export interface TenantPartnerProfile extends AuditableModel {
    id?: number;
    partnerId?: number;
    profileCode?: string;
    profileName?: string;
    partnerType?: PartnerType;
    agreementCategory?: AgreementCategory;
    inventorySharingType?: InventorySharingType;
    priority?: number;
    autoAcceptScheduleChanges?: boolean;
    prorationApplicable?: boolean;
    eTicketAllowed?: boolean;
    active?: boolean;
    displayOrder?: number;
    description?: string;
    remarks?: string;
    recordStatus?: RecordStatus;
    effectiveFrom?: string;
    effectiveTo?: string;
}

export interface TenantPartnerCommunicationProfile extends AuditableModel {
    id?: number;
    partnerId?: number;
    profileCode?: string;
    profileName?: string;
    protocol?: CommunicationProtocol;
    transportType?: TransportType;
    messageFormat?: MessageFormat;
    authenticationType?: AuthenticationType;
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
    recordStatus?: RecordStatus;
    effectiveFrom?: string;
    effectiveTo?: string;
}

export interface TenantPartnerDistributionProfile extends AuditableModel {
    id?: number;
    partnerId?: number;
    profileCode?: string;
    profileName?: string;
    distributionChannel?: CommunicationProtocol;
    distributionMode?: DistributionMode;
    messageType?: DistributionMessageType;
    realTimeEnabled?: boolean;
    acknowledgementRequired?: boolean;
    retryEnabled?: boolean;
    retryCount?: number;
    active?: boolean;
    displayOrder?: number;
    description?: string;
    remarks?: string;
    recordStatus?: RecordStatus;
    effectiveFrom?: string;
    effectiveTo?: string;
}
