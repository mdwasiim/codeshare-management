import { AuditableModel } from '@shared/models/auditable.model';

export interface PassengerTerminal extends AuditableModel {
    id?: string;
    airportId?: string;
    terminalCode?: string;
    terminalName?: string;
    terminalType?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
