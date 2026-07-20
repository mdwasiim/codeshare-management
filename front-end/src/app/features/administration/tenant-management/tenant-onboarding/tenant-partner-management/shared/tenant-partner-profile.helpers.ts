import { TenantPartner } from '@features/administration/tenant-management/models/tenant-partner.model';

export interface TenantPartnerOption {
    label: string;
    value: number;
}

export interface SelectOption<T extends string = string> {
    label: string;
    value: T;
}

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

    return left || right || (partner.id != null ? String(partner.id) : 'Unknown partner');
}

export function toTenantPartnerOptions(partners: TenantPartner[]): TenantPartnerOption[] {
    return partners
        .filter((partner): partner is TenantPartner & { id: number } => partner.id != null)
        .map((partner) => ({
            value: partner.id,
            label: buildTenantPartnerLabel(partner)
        }))
        .sort((left, right) => left.label.localeCompare(right.label));
}
