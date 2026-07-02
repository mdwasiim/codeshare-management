import { AuditableModel } from '@shared/models/auditable.model';

export interface AllianceMember extends AuditableModel {
    id?: string;
    allianceId?: string;
    airlineId?: string;
    memberType?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
