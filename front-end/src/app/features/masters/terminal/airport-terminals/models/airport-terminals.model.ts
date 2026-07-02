import { AuditableModel } from '@shared/models/auditable.model';

export interface AirportTerminal extends AuditableModel {
    id?: string;
    airportId?: string;
    terminalCode?: string;
    terminalName?: string;
    recordStatus?: string;
    effectiveFrom?: string;
    effectiveTo?: string;
    active?: boolean;
}
