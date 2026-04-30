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

        dashboard: {
            stats: '/identity/dashboard/stats'
        },

        menu: {
            base: '/identity/menus',
            byId: '/identity/menus/{id}'
        },

        users: {
            base: '/identity/users',
            byId: '/identity/users/{id}'
        },

        roles: {
            base: '/identity/roles',
            byId: '/identity/roles/{id}'
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
            base: '/identity/tenants',
            byId: '/identity/tenants/{id}'
        }
    }
} as const;

export type ApiEndpointKey =
    | 'auth.login'
    | 'auth.logout'
    | 'auth.refresh'

    | 'dashboard.stats'

    | 'menu.base'
    | 'menu.byId'

    | 'users.base'
    | 'users.byId'

    | 'roles.base'
    | 'roles.byId'

    | 'groups.base'
    | 'groups.byId'

    | 'permissions.base'
    | 'permissions.byId'

    | 'tenants.base'
    | 'tenants.byId';

export const buildApiUrl = (key: ApiEndpointKey): string => {
    const base = API_CONFIG.baseUrl.replace(/\/$/, '');

    const path = key.split('.').reduce((acc: any, part) => {
        if (!acc || !(part in acc)) {
            throw new Error(`Invalid API endpoint key: ${key}`);
        }
        return acc[part];
    }, API_CONFIG.endpoints);

    if (typeof path !== 'string') {
        throw new Error(`Invalid endpoint path for key: ${key}`);
    }

    return `${base}/${path.replace(/^\//, '')}`;
};


export interface ApiOptions {
    params?: Record<string, string | number | boolean | ReadonlyArray<string | number | boolean>>;
    headers?: HttpHeaders | Record<string, string>;
    pathParams?: Record<string, string | number>;
}

