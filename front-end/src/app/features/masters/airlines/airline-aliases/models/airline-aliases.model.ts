import { AuditableModel } from '@shared/models/auditable.model';

export interface AirlineAlias extends AuditableModel {
    id?: string;
    aliasCode?: string;
    aliasName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
    airlineId?: string;
}
