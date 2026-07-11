import { TenantPartner } from '@features/administration/tenant-management/models/tenant-partner.model';
import {
    AgreementCategory,
    AuthenticationType,
    CommunicationProtocol,
    DistributionMessageType,
    DistributionMode,
    InventorySharingType,
    MessageFormat,
    PartnerType,
    RecordStatus,
    TransportType
} from '@features/administration/tenant-management/models/tenant-partner-profile.model';

export interface TenantPartnerOption {
    label: string;
    value: string;
}

export interface SelectOption<T extends string = string> {
    label: string;
    value: T;
}

export const RECORD_STATUS_OPTIONS: SelectOption<RecordStatus>[] = [
    { label: 'Active', value: 'ACTIVE' },
    { label: 'Inactive', value: 'INACTIVE' },
    { label: 'Suspended', value: 'SUSPENDED' },
    { label: 'Terminated', value: 'TERMINATED' },
    { label: 'Expired', value: 'EXPIRED' }
];

export const PARTNER_TYPE_OPTIONS = toSelectOptions<PartnerType>(['OPERATING', 'MARKETING', 'RECIPROCAL']);
export const AGREEMENT_CATEGORY_OPTIONS = toSelectOptions<AgreementCategory>(['BILATERAL', 'ALLIANCE', 'INTERLINE']);
export const INVENTORY_SHARING_OPTIONS = toSelectOptions<InventorySharingType>(['FREE_SALE', 'BLOCK_SPACE', 'SOFT_BLOCK', 'HARD_BLOCK']);
export const COMMUNICATION_PROTOCOL_OPTIONS = toSelectOptions<CommunicationProtocol>(['API', 'MQ', 'SFTP', 'EMAIL', 'AS2']);
export const TRANSPORT_TYPE_OPTIONS = toSelectOptions<TransportType>(['REST', 'SOAP', 'JMS', 'FTP', 'SFTP', 'HTTPS']);
export const MESSAGE_FORMAT_OPTIONS = toSelectOptions<MessageFormat>(['SSIM', 'ASM', 'SSM', 'XML', 'JSON', 'EDIFACT']);
export const AUTHENTICATION_TYPE_OPTIONS = toSelectOptions<AuthenticationType>(['NONE', 'BASIC', 'API_KEY', 'OAUTH2', 'JWT', 'CERTIFICATE', 'SSH_KEY']);
export const DISTRIBUTION_MODE_OPTIONS = toSelectOptions<DistributionMode>(['REAL_TIME', 'SCHEDULED', 'MANUAL']);
export const DISTRIBUTION_MESSAGE_TYPE_OPTIONS = toSelectOptions<DistributionMessageType>(['SSIM', 'SSM', 'ASM']);

export function formatEnumLabel(value?: string | null): string {
    if (!value) {
        return '-';
    }

    return value
        .toLowerCase()
        .split('_')
        .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
        .join(' ');
}

export function toSelectOptions<T extends string>(values: readonly T[]): SelectOption<T>[] {
    return values.map((value) => ({
        value,
        label: formatEnumLabel(value)
    }));
}

export function toDateInputValue(value?: string | null): string {
    return value ? value.slice(0, 10) : '';
}

export function normalizeOptionalText(value?: string | null): string | undefined {
    const trimmed = value?.trim();
    return trimmed ? trimmed : undefined;
}

export function buildTenantPartnerLabel(partner: Partial<TenantPartner>): string {
    const left = [partner.homeAirlineCode, partner.homeAirlineName].filter(Boolean).join(' - ');
    const right = [partner.partnerAirlineCode, partner.partnerAirlineName].filter(Boolean).join(' - ');

    if (left && right) {
        return `${left} -> ${right}`;
    }

    return left || right || partner.id || 'Unknown partner';
}

export function toTenantPartnerOptions(partners: TenantPartner[]): TenantPartnerOption[] {
    return partners
        .filter((partner): partner is TenantPartner & { id: string } => !!partner.id)
        .map((partner) => ({
            value: partner.id,
            label: buildTenantPartnerLabel(partner)
        }))
        .sort((left, right) => left.label.localeCompare(right.label));
}
