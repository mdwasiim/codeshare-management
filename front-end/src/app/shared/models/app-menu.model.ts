export interface AppMenuModel {

    // =========================
    // CORE (BACKEND)
    // =========================
    id?: string;

    label: string;
    icon?: string;

    route?: string;

    routerLink?: string | string[]; //front-end use only

    parentId?: string;
    displayOrder?: number;

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

    // =========================
    // EXTERNAL LINKS (OPTIONAL)
    // =========================
    url?: string;
    target?: string;
}
