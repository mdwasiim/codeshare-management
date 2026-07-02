import { AuditableModel } from '@shared/models/auditable.model';

export interface Season extends AuditableModel {
    id?: string;
    seasonCode?: string;
    seasonName?: string;
    seasonType?: string;
    startDate?: string;
    endDate?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
