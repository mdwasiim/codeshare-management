import { AuditableModel } from '@shared/models/auditable.model';

export interface AppMenuModel extends AuditableModel {
    id?: string;

    code: string;
    label: string;
    topbarLabel?: string;
    sidebarLabel?: string;
    icon?: string;

    route?: string;

    routerLink?: string | string[];

    parentId?: string;
    displayOrder?: number;

    groupIds?: string[];
    permission?: string;

    items?: AppMenuModel[];

    visible?: boolean;
    separator?: boolean;

    styleClass?: string;
    badgeClass?: string;

    expanded?: boolean;

    url?: string;
    target?: string;
}
