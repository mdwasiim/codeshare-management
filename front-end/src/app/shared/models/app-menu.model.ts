export interface AppMenuModel {
    id?: string;                       // ✅ BACKEND (unique id, important for tracking)

    label: string;                     // ✅ BACKEND (menu display text)
    icon?: string;                     // ✅ BACKEND (optional icon)

    path?: string;                     // ❌ FRONTEND (generated for menu hierarchy / active state)

    routerLink?: string | string[];    // ✅ BACKEND (string preferred, frontend can convert to array)

    routerLinkActiveOptions?: {        // ❌ FRONTEND (Angular-specific, do not send from API)
        paths?: 'exact' | 'subset';
        queryParams?: 'exact' | 'subset' | 'ignored';
        matrixParams?: 'exact' | 'subset' | 'ignored';
        fragment?: 'exact' | 'ignored';
    };

    queryParams?: any;                 // ❌ FRONTEND (routing config)
    fragment?: string;                 // ❌ FRONTEND
    queryParamsHandling?: 'merge' | 'preserve' | ''; // ❌ FRONTEND
    preserveFragment?: boolean;        // ❌ FRONTEND
    skipLocationChange?: boolean;      // ❌ FRONTEND
    replaceUrl?: boolean;              // ❌ FRONTEND
    state?: any;                       // ❌ FRONTEND

    url?: string;                      // ✅ BACKEND (external links)
    target?: string;                   // ✅ BACKEND (e.g. _blank)

    visible?: boolean;                 // ✅ BACKEND (permission / feature control)
    separator?: boolean;               // ✅ BACKEND (UI divider)

    styleClass?: string;               // 🟡 BACKEND (optional UI styling)
    badgeClass?: string;               // 🟡 BACKEND (optional badge styling)

    parentId?: string;                 // ✅ BACKEND (if flat structure)
    displayOrder?: number;             // ✅ BACKEND (sorting)

    items?: AppMenuModel[];            // ✅ BACKEND (nested structure)

    active?: boolean;                  // ❌ FRONTEND (runtime UI state, DO NOT store)

    command?: (event: {               // ❌ FRONTEND (functions cannot come from backend)
        originalEvent: Event;
        item: AppMenuModel;
    }) => void;
}
