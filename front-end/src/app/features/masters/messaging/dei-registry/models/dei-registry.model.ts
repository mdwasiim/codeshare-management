import { AuditableModel } from '@shared/models/auditable.model';

export interface DeiRegistry extends AuditableModel {
    id?: string;
    deiNumber?: string;
    deiName?: string;
    deiCategory?: string;
    description?: string;
    recordStatus?: string;
}
