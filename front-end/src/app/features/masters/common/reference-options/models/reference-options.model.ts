import { AuditableModel } from '@shared/models/auditable.model';

export interface CommonReferenceOption extends AuditableModel {
    id?: string;
    categoryCode?: string;
    value?: string;
    label?: string;
    description?: string;
    displayOrder?: number;
    recordStatus?: string;
    active?: boolean;
}
