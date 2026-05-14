import {Injectable} from '@angular/core';
import {LoginResponse} from '@features/access-management/auth/models/login-response.model';
import {RefreshTokenResponse} from '@features/access-management/auth/models/refresh-token-response.model';
import {catchError, Observable, of, tap} from 'rxjs';
import {AuthTokenService} from "@services/auth/auth-token.service";
import {LayoutMenuService} from "@layout/services/layout-menu.service";
import {map} from "rxjs/operators";
import { switchMap } from 'rxjs';
import {AppApiService} from "@core/api/config/app-api.service";
import {API_ENDPOINTS} from "@core/api/config/app-api.config";
import {PermissionService} from "@core/security/permission.service";
import {AuthTenantService} from "@services/auth/auth-tenant.service";

@Injectable({ providedIn: 'root' })
export class AuthService {

    constructor(
        private apiService: AppApiService,
        private tenantService: AuthTenantService,
        private permissionService: PermissionService,
        private tokenService: AuthTokenService,
        private menuService: LayoutMenuService
    ) {}



    login(username: string, password: string): Observable<LoginResponse> {
        return this.apiService.post<LoginResponse>(API_ENDPOINTS.auth.login, { username, password }).pipe(

            tap(res => {

                // =========================
                // STORE SESSION
                // =========================
                this.tokenService.setSession(
                    res.access_token,
                    res.refresh_token,
                    res.expires_in
                );

                // =========================
                // STORE TENANT
                // =========================
                this.tokenService.setTenant(
                    res.tenant_code
                );

                // =========================
                // RESTORE RUNTIME TENANT
                // =========================
                this.tenantService.setTenant(
                    res.tenant_id,
                    res.tenant_code
                );

                // =========================
                // RBAC
                // =========================
                this.permissionService.setPermissions(
                    res.permissions || []
                );

                this.permissionService.setRoles(
                    res.roles || []
                );

                this.permissionService.setGroups(
                    res.groups || []
                );
            }),

            switchMap(res =>
                this.menuService.loadMenus().pipe(
                    map(() => res)   // ✅ return original response
                )
            )
        );
    }

    refresh(): Observable<RefreshTokenResponse> {
        return this.apiService.post<RefreshTokenResponse>(API_ENDPOINTS.auth.refresh, {}).pipe(
            tap(res => {
                this.tokenService.setSession(
                    res.access_token,
                    this.tokenService.refreshToken || '',
                    res.expires_in
                );
            })
        );
    }

    logout(): Observable<any> {
        return this.apiService.post<any>(API_ENDPOINTS.auth.logout, {}).pipe(

            // ✅ Always clear session
            tap(() => this.handleLogout()),

            // ✅ Even if API fails → still logout
            catchError(() => {
                this.handleLogout();
                return of(null);
            })
        );
    }

    private handleLogout() {
        this.tokenService.clear();
        this.permissionService.clear();
        this.menuService.clear();

        // 🔥 redirect to login
        window.location.href = '/auth/login';
    }


}



