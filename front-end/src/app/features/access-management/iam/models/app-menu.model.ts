export interface AppMenuModel {
    // =========================
    // CORE (BACKEND)
    // =========================
    id?: string;

    code: string;
    label: string;
    icon?: string;

    route?: string;

    routerLink?: string | string[]; //front-end use only

    parentId?: string;
    displayOrder?: number;

    groupIds?: string[];
    permission?: string;

    // =========================
    // TREE (FRONTEND GENERATED)
    // =========================
    items?: AppMenuModel[];

    // =========================
    // UI (OPTIONAL)
    // =========================
    visible?: boolean;
    separator?: boolean;

    styleClass?: string;
    badgeClass?: string;

    expanded?: boolean;

    // =========================
    // EXTERNAL LINKS (OPTIONAL)
    // =========================
    url?: string;
    target?: string;
}
