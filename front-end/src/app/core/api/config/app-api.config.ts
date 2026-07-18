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
    identityService: {
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
        userGroups: {
            byUserId: makeEndpoint('/identity/user-groups/group/{userId}'),
            byGroupId: makeEndpoint('/identity/user-groups/user/{groupId}')
        },
        rolePermissions: {
            byRoleId: makeEndpoint('/identity/role-permissions/{roleId}')
        },
        groupRoles: {
            byGroupId: makeEndpoint('/identity/group-role/role/{groupId}')
        },
        groupMenus: {
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
        menus: {
            base: makeEndpoint('/identity/menus'),
            manage: makeEndpoint('/identity/menus/manage/all'),
            byId: makeEndpoint('/identity/menus/{id}')
        }
    },
    tenantService: {
        tenants: {
            base: makeEndpoint('/tenant/tenants'),
            byId: makeEndpoint('/tenant/tenants/{id}'),
            identityProviders: makeEndpoint('/tenant/tenant-identity-providers'),
            oidcConfigs: makeEndpoint('/tenant/tenant-oidc-configs'),
            authContextByCode: makeEndpoint('/tenant/tenants/code/{code}/auth-context'),
            loginOptions: makeEndpoint('/tenant/tenants/login-options')
        },
        tenantPartners: {
            base: makeEndpoint('/tenant/tenant-partners'),
            byId: makeEndpoint('/tenant/tenant-partners/{id}')
        },
        tenantPartnerProfiles: {
            base: makeEndpoint('/tenant/tenant-partner-profiles'),
            byId: makeEndpoint('/tenant/tenant-partner-profiles/{id}')
        },
        tenantPartnerCommunicationProfiles: {
            base: makeEndpoint('/tenant/tenant-partner-communication-profiles'),
            byId: makeEndpoint('/tenant/tenant-partner-communication-profiles/{id}')
        },
        tenantPartnerDistributionProfiles: {
            base: makeEndpoint('/tenant/tenant-partner-distribution-profiles'),
            byId: makeEndpoint('/tenant/tenant-partner-distribution-profiles/{id}')
        },
        tenantIngestionProfiles: {
            base: makeEndpoint('/tenant/tenant-ingestion-profiles'),
            byId: makeEndpoint('/tenant/tenant-ingestion-profiles/{id}'),
            byTenantCode: makeEndpoint('/tenant/tenant-ingestion-profiles/tenant/{tenantCode}')
        }
    },
    scheduleIngestionService: {
        ssim: {
            files: makeEndpoint('/schedule/ssim/files'),
            loadedScheduleById: makeEndpoint('/schedule/ssim/loaded-schedules/{fileId}'),
            messageByFileId: makeEndpoint('/schedule/ssim/files/{fileId}/message'),
            validationReport: makeEndpoint('/schedule/ssim/files/{fileId}/validation-report'),
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
        },
        outboundMessages: {
            byId: makeEndpoint('/schedule/internal/outbound-messages/{outboundMessageId}')
        }
    }
} as const;

export interface ApiOptions {
    params?: Record<string, string | number | boolean | ReadonlyArray<string | number | boolean>>;
    headers?: HttpHeaders | Record<string, string>;
    pathParams?: Record<string, PathParamValue>;
}
