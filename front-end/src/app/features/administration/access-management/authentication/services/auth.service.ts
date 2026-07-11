import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, catchError, of, switchMap, tap } from 'rxjs';
import { map } from 'rxjs/operators';

import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppApiService } from '@core/api/config/app-api.service';
import { PermissionService } from '@core/security/permission.service';
import { LayoutMenuService } from '@layout/services/layout-menu.service';
import { AuthTenantService } from '@services/auth/auth-tenant.service';
import { AuthTokenService } from '@services/auth/auth-token.service';
import { AuthSessionResponse } from '@features/administration/access-management/authentication/models/auth-session-response.model';
import { LoginResponse } from '@features/administration/access-management/authentication/models/login-response.model';
import { RefreshTokenResponse } from '@features/administration/access-management/authentication/models/refresh-token-response.model';
import { AuthSource, TenantAuthContext } from '@features/tenant-administration/models/tenant.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
    constructor(
        private apiService: AppApiService,
        private tenantService: AuthTenantService,
        private permissionService: PermissionService,
        private tokenService: AuthTokenService,
        private menuService: LayoutMenuService,
        private router: Router
    ) {}

    login(username: string, password: string, authSource: AuthSource = AuthSource.INTERNAL): Observable<LoginResponse> {
        return this.apiService.post<LoginResponse>(API_ENDPOINTS.auth.login, { username, password, authSource }).pipe(
            tap((res) => {
                this.tokenService.setSession(res.access_token, res.refresh_token, res.expires_in);
                this.tokenService.setTenant(res.tenant_code);
                this.tenantService.setTenant(res.tenant_id, res.tenant_code);
                this.applyAccess(res.roles || [], [], res.groups || [], res.username);
            }),
            switchMap((res) => this.loadSession().pipe(switchMap(() => this.menuService.loadMenus()), map(() => res)))
        );
    }

    getTenantAuthContext(tenantCode: string): Observable<TenantAuthContext> {
        return this.apiService.get<TenantAuthContext>(API_ENDPOINTS.accessManagement.tenants.authContextByCode, {
            pathParams: { code: tenantCode.trim().toUpperCase() }
        });
    }

    startOidcLogin(tenantCode: string, authSource: AuthSource): void {
        const params = new URLSearchParams({
            tenantCode: tenantCode.trim().toUpperCase(),
            authSource
        });
        window.location.assign(`${API_ENDPOINTS.auth.oidcAuthorize()}?${params.toString()}`);
    }

    exchangeOidcCode(code: string): Observable<LoginResponse> {
        return this.apiService.post<LoginResponse>(API_ENDPOINTS.auth.oidcToken, { code }).pipe(
            tap((res) => {
                this.tokenService.setSession(res.access_token, res.refresh_token, res.expires_in);
            }),
            switchMap((res) => this.loadSession().pipe(switchMap(() => this.menuService.loadMenus()), map(() => res)))
        );
    }

    refresh(): Observable<RefreshTokenResponse> {
        return this.apiService
            .post<RefreshTokenResponse>(API_ENDPOINTS.auth.refresh, {}, {
                headers: {
                    Authorization: `Bearer ${this.tokenService.refreshToken || ''}`
                }
            })
            .pipe(
                tap((res) => {
                    this.tokenService.setSession(res.access_token, res.refresh_token, res.expires_in);
                }),
                switchMap((res) => this.loadSession().pipe(map(() => res)))
            );
    }

    logout(): Observable<any> {
        return this.apiService
            .post<any>(API_ENDPOINTS.auth.logout, {}, {
                headers: {
                    Authorization: `Bearer ${this.tokenService.refreshToken || ''}`
                }
            })
            .pipe(
                tap(() => this.handleLogout()),
                catchError(() => {
                    this.handleLogout();
                    return of(null);
                })
            );
    }

    loadSession(): Observable<AuthSessionResponse> {
        return this.apiService.get<AuthSessionResponse>(API_ENDPOINTS.auth.session).pipe(
            tap((session) => {
                if (session.tenant_code) {
                    this.tokenService.setTenant(session.tenant_code);
                }

                if (session.tenant_id || session.tenant_code) {
                    this.tenantService.setTenant(session.tenant_id || '', session.tenant_code || '');
                }

                this.applyAccess(session.roles || [], session.permissions || [], session.groups || [], session.username);
            })
        );
    }

    private applyAccess(roles: string[] = [], permissions: string[] = [], groups: string[] = [], username?: string | null): void {
        this.tokenService.setUserAccess(roles, permissions, groups, username);
        this.permissionService.setUserAccess(roles, permissions, groups);
    }

    private handleLogout(): void {
        this.tokenService.clear();
        this.permissionService.clear();
        this.menuService.clear();
        this.router.navigate(['/auth/login']);
    }
}

