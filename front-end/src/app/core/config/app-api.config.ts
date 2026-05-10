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

        accessManagement: {
            userGroups: {
                byUserId:
                    '/identity/user-groups/group/{userId}'
            },

            rolePermissions: {
                byRoleId:
                    '/identity/role-permissions/{roleId}'
            },

            groupRole: {
                byGroupId: '/identity/group-role/role/{groupId}'
            },

            groupMenu: {
                byGroupId: '/identity/group-menus/{groupId}'
            },

            users: {
                base: '/identity/users',
                byId: '/identity/users/{id}'
            },

            roles: {
                base: '/identity/roles',
                byId: '/identity/roles/{id}',
                byGroupId: '/identity/roles/{id}'
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
            },
            menu: {
                base: '/identity/menus',
                byId: '/identity/menus/{id}'
            }
        }
    }
} as const;

export type ApiEndpointKey =
    | 'auth.login'
    | 'auth.logout'
    | 'auth.refresh'

    | 'dashboard.stats'

    | 'accessManagement.rolePermissions.byRoleId'
    | 'accessManagement.groupRole.byGroupId'

    | 'accessManagement.groupMenu.byGroupId'

    | 'accessManagement.users.base'
    | 'accessManagement.users.byId'

    |`accessManagement.userGroups.byUserId`

    | 'accessManagement.roles.base'
    | 'accessManagement.roles.byId'
    | 'accessManagement.roles.byGroupId'

    | 'accessManagement.groups.base'
    | 'accessManagement.groups.byId'

    | 'accessManagement.permissions.base'
    | 'accessManagement.permissions.byId'

    | 'accessManagement.tenants.base'
    | 'accessManagement.tenants.byId'

    | 'accessManagement.menu.base'
    | 'accessManagement.menu.byId';

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

