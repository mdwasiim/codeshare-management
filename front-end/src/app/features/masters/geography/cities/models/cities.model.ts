import { AuditableModel } from '@shared/models/auditable.model';

export interface City extends AuditableModel {
    id?: string;
    cityCode?: string;
    cityName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
    countryId?: string;
    stateId?: string;
}
