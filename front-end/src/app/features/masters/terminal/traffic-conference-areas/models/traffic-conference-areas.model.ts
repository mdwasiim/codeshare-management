import { AuditableModel } from '@shared/models/auditable.model';

export interface TrafficConferenceArea extends AuditableModel {
    id?: string;
    areaCode?: string;
    areaName?: string;
    description?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
