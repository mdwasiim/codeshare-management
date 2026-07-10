import { HttpHeaders } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

type PathParamValue = string | number | boolean;

type ExtractPathParams<T extends string> = T extends `${string}{${infer Param}}${infer Rest}` ? Param | ExtractPathParams<Rest> : never;

type ParamsForPath<T extends string> = [ExtractPathParams<T>] extends [never] ? void : Record<ExtractPathParams<T>, PathParamValue>;

export type ApiEndpointFactory<P = void> = [P] extends [void] ? () => string : (params: P) => string;

export type AnyApiEndpointFactory = (...args: any[]) => string;

const normalizedBaseUrl = environment.apiBaseUrl.replace(/\/$/, '');

export const API_CONFIG = {
    baseUrl: normalizedBaseUrl
} as const;

const resolvePathParams = (path: string, params?: Record<string, PathParamValue>): string => {
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
        refresh: makeEndpoint('/identity/auth/refresh'),
        session: makeEndpoint('/identity/auth/session'),
        oidcAuthorize: makeEndpoint('/identity/auth/oidc/authorize'),
        oidcToken: makeEndpoint('/identity/auth/oidc/token')
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
            base: makeEndpoint('/tenant/tenants'),
            byId: makeEndpoint('/tenant/tenants/{id}'),
            authContextByCode: makeEndpoint('/tenant/tenants/code/{code}/auth-context')
        },
        menu: {
            base: makeEndpoint('/identity/menus'),
            manage: makeEndpoint('/identity/menus/manage/all'),
            byId: makeEndpoint('/identity/menus/{id}')
        },
        tenantPartners: {
            base: makeEndpoint('/tenant/tenant-partners'),
            byId: makeEndpoint('/tenant/tenant-partners/{id}')
        }
    },
    scheduleIngestion: {
        upload: makeEndpoint('/schedule/upload'),
        messages: {
            validate: makeEndpoint('/schedule/messages/{type}/validate'),
            parse: makeEndpoint('/schedule/messages/{type}/parse'),
            ingest: makeEndpoint('/schedule/messages/{type}/ingest')
        },
        ssim: {
            files: makeEndpoint('/schedule/ssim/files'),
            loadedScheduleById: makeEndpoint('/schedule/ssim/loaded-schedules/{fileId}'),
            messageByFileId: makeEndpoint('/schedule/ssim/files/{fileId}/message'),
            fileFlights: makeEndpoint('/schedule/ssim/files/{fileId}/flights'),
            loadedSchedules: makeEndpoint('/schedule/ssim/loaded-schedules')
        },
        asmSsm: {
            files: makeEndpoint('/schedule/asm-ssm/{type}/files'),
            messages: makeEndpoint('/schedule/asm-ssm/{type}/messages'),
            loadedScheduleById: makeEndpoint('/schedule/asm-ssm/loaded-schedules/{fileId}'),
            scheduleByFileId: makeEndpoint('/schedule/asm-ssm/{type}/files/{fileId}/schedule'),
            fileFlights: makeEndpoint('/schedule/asm-ssm/{type}/files/{fileId}/flights'),
            loadedSchedules: makeEndpoint('/schedule/asm-ssm/loaded-schedules')
        }
    }
} as const;

export interface ApiOptions {
    params?: Record<string, string | number | boolean | ReadonlyArray<string | number | boolean>>;
    headers?: HttpHeaders | Record<string, string>;
    pathParams?: Record<string, PathParamValue>;
}
