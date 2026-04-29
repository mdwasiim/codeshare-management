import { HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';

export const API_CONFIG = {
    baseUrl: environment.CSMBaseUrl,

    endpoints: {
        auth: {
            login: '/identity/auth/login',
            logout: '/identity/auth/logout',
            refresh: '/identity/auth/refresh'
        },
        menu: {
            get: '/identity/menus'
        },
        dashboard: {
            stats: '/identity/dashboard/stats'
        },
        users: {
            base: '/identity/users',
            byId: '/identity/users/{id}'
        },
        roles: {
            base: '/identity/roles'
        },
        groups: {
            base: '/identity/groups',
            byId: '/identity/groups/{id}'
        },
        permissions: {
            base: '/identity/permissions',
            byId: '/identity/permissions/{id}'
        },
        tenants: {
            base: '/identity/tenants'
        }
    }
} as const;

export type ApiEndpointKey =
    | 'auth.login'
    | 'auth.logout'
    | 'auth.refresh'

    | 'menu.get'
    |'menu.byId'
    |'menu.create'
    |'menu.update'
    |'menu.delete'

    | 'dashboard.stats'

    | 'users.base'
    | 'users.byId'

    | 'roles.base'
    | 'roles.byId'

    | 'groups.base'
    | 'groups.byId'

    | 'permissions.base'
    | 'permissions.byId';

export const buildApiUrl = (  key: ApiEndpointKey,
    pathParams?: Record<string, string | number> ): string => {
    const base = API_CONFIG.baseUrl.replace(/\/$/, '');

    let path = key.split('.').reduce((acc: any, part) => {
        if (!acc || !(part in acc)) {
            throw new Error(`Invalid API endpoint key: ${key}`);
        }
        return acc[part];
    }, API_CONFIG.endpoints);

    if (typeof path !== 'string') {
        throw new Error(`Invalid endpoint path for key: ${key}`);
    }

    // 🔥 APPLY PATH PARAMS
    if (pathParams) {
        Object.entries(pathParams).forEach(([k, v]) => {
            path = path.replace(`{${k}}`, String(v));
        });
    }

    return `${base}/${path.replace(/^\//, '')}`;
};

export interface ApiOptions {
    params?: Record<string, string | number | boolean>;
    headers?: HttpHeaders | Record<string, string>;
    pathParams?: Record<string, string | number>;   // ✅ ADD THIS
}

