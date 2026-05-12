import { HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';

type PathParamValue = string | number | boolean;

type ExtractPathParams<T extends string> =
    T extends `${string}{${infer Param}}${infer Rest}`
        ? Param | ExtractPathParams<Rest>
        : never;

type ParamsForPath<T extends string> =
    [ExtractPathParams<T>] extends [never]
        ? void
        : Record<ExtractPathParams<T>, PathParamValue>;

export type ApiEndpointFactory<P = void> =
    [P] extends [void]
        ? () => string
        : (params: P) => string;

export type AnyApiEndpointFactory = (...args: any[]) => string;

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

const resolvePathParams = (
    path: string,
    params?: Record<string, PathParamValue>
): string => {
    const resolved = path.replace(/\{([^}]+)\}/g, (_, token: string) => {
        const value = params?.[token];

        if (value === undefined || value === null) {
            throw new Error(`Missing path param "${token}" for path "${path}"`);
        }

        return encodeURIComponent(String(value));
    });

    if (resolved.includes('{')) {
        throw new Error(`Unresolved path params in "${resolved}"`);
    }

    return resolved;
};

const makeEndpoint = <TPath extends string>(path: TPath): ApiEndpointFactory<ParamsForPath<TPath>> => {
    const base = API_CONFIG.baseUrl.replace(/\/$/, '');

    const factory = (params?: Record<string, PathParamValue>) => {
        const normalizedPath = resolvePathParams(path, params).replace(/^\//, '');
        return `${base}/${normalizedPath}`;
    };

    return factory as ApiEndpointFactory<ParamsForPath<TPath>>;
};

export const API_ENDPOINTS = {
    auth: {
        login: makeEndpoint('/identity/auth/login'),
        logout: makeEndpoint('/identity/auth/logout'),
        refresh: makeEndpoint('/identity/auth/refresh')
    },
    dashboard: {
        stats: makeEndpoint('/identity/dashboard/stats')
    },
    accessManagement: {
        userGroups: {
            byUserId: makeEndpoint('/identity/user-groups/group/{userId}')
        },
        rolePermissions: {
            byRoleId: makeEndpoint('/identity/role-permissions/{roleId}')
        },
        groupRole: {
            byGroupId: makeEndpoint('/identity/group-role/role/{groupId}')
        },
        groupMenu: {
            byGroupId: makeEndpoint('/identity/group-menus/{groupId}')
        },
        users: {
            base: makeEndpoint('/identity/users'),
            byId: makeEndpoint('/identity/users/{id}')
        },
        roles: {
            base: makeEndpoint('/identity/roles'),
            byId: makeEndpoint('/identity/roles/{id}'),
            byGroupId: makeEndpoint('/identity/roles/{id}')
        },
        groups: {
            base: makeEndpoint('/identity/groups'),
            byId: makeEndpoint('/identity/groups/{id}')
        },
        permissions: {
            base: makeEndpoint('/identity/permissions'),
            byId: makeEndpoint('/identity/permissions/{id}')
        },
        tenants: {
            base: makeEndpoint('/identity/tenants'),
            byId: makeEndpoint('/identity/tenants/{id}')
        },
        menu: {
            base: makeEndpoint('/identity/menus'),
            byId: makeEndpoint('/identity/menus/{id}')
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

    const path = key.split('.').reduce<unknown>((acc, part) => {
        if (!acc || typeof acc !== 'object') {
            throw new Error(`Invalid API endpoint key: ${key}`);
        }

        const obj = acc as Record<string, unknown>;

        if (!(part in obj)) {
            throw new Error(`Invalid API endpoint key: ${key}`);
        }

        return obj[part];
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

