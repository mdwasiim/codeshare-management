import { HttpHeaders } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

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

const normalizedBaseUrl = environment.CSMBaseUrl.replace(/\/$/, '');

export const API_CONFIG = {
    baseUrl: normalizedBaseUrl
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
    const factory = (params?: Record<string, PathParamValue>) => {
        const normalizedPath = resolvePathParams(path, params).replace(/^\//, '');
        return `${API_CONFIG.baseUrl}/${normalizedPath}`;
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


export interface ApiOptions {
    params?: Record<string, string | number | boolean | ReadonlyArray<string | number | boolean>>;
    headers?: HttpHeaders | Record<string, string>;
    pathParams?: Record<string, PathParamValue>;
}

