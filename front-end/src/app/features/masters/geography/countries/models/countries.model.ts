import { AuditableModel } from '@shared/models/auditable.model';

export interface Country extends AuditableModel {
    id?: string;
    iso2Code?: string;
    iso3Code?: string;
    countryName?: string;
    regionId?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
