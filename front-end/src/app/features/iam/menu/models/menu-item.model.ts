export interface AppMenuItemModel {
    id?: string;                       // ✅ for tracking (VERY IMPORTANT)

    label: string;
    icon?: string;

    routerLink?: string | string[];
    url?: string;
    target?: string;

    items?: AppMenuItemModel[];

    visible?: boolean;
    separator?: boolean;

    styleClass?: string;
    badgeClass?: string;
}
