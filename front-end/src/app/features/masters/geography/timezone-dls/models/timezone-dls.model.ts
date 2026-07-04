import { AuditableModel } from '@shared/models/auditable.model';

export interface TimezoneDls extends AuditableModel {
    id?: string;
    timezoneId?: string;
    timezoneIdentifier?: string;
    dstStart?: string;
    dstEnd?: string;
    dstOffsetMinutes?: number;
    effectiveFrom?: string;
    effectiveTo?: string;
    recordStatus?: string;
}
