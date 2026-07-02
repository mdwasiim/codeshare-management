import { AuditableModel } from '@shared/models/auditable.model';

export interface SecureFlightIndicator extends AuditableModel {
    id?: string;
    indicatorCode?: string;
    indicatorName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
