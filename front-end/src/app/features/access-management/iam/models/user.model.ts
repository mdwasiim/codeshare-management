import { AuditableModel } from '@shared/models/auditable.model';

export interface User extends AuditableModel {
    id?: string;

    username: string;
    email: string;
    password?: string;

    firstName?: string;
    lastName?: string;

    enabled: boolean;

    accountNonLocked: boolean;
    accountNonExpired: boolean;
    credentialsNonExpired: boolean;

    lastLogin?: string;
    lastLoginProvider?: string;

    externalId?: string;

    authSource?: 'LOCAL' | 'LDAP' | 'OAUTH';

    recordStatus?: 'ACTIVE' | 'INACTIVE';

    tenant?: {
        id: string;
        name: string;
        code: string;
    };
    roles?: {
        id: string;
        name: string;
    }[];
}
