import { AuditableModel } from '@shared/models/auditable.model';

export interface AircraftOwner extends AuditableModel {
    id?: string;
    ownerCode?: string;
    ownerName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
