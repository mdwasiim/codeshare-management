import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { LoginResponse } from '@/core/models/auth/login-response.model';
import { RefreshTokenResponse } from '@/core/models/auth/refresh-token-response.model';
import {catchError, Observable, of, tap} from 'rxjs';
import {AuthzService} from "@services/authz.service";
import {TokenService} from "@services/auth/token.service";
import {MenuService} from "@shared/services/menu.service";

@Injectable({ providedIn: 'root' })
export class AuthService {

    constructor(
        private apiService: ApiService,
        private authz: AuthzService,
        private tokenService: TokenService,
        private menuService: MenuService
    ) {}

    login(username: string, password: string): Observable<LoginResponse> {
        return this.apiService.post<LoginResponse>('auth.login', { username, password }).pipe(
            tap((res) => {

                // ✅ 1. Save tokens
                this.tokenService.setSession(
                    res.access_token,
                    res.refresh_token,
                    res.expires_in
                );

                // ✅ 2. Set RBAC
                this.authz.setUserAccess(res.roles, res.permissions);

                // ✅ 3. Load menu AFTER RBAC set
                this.menuService.loadMenus().subscribe();
            })
        );
    }

    refresh(): Observable<RefreshTokenResponse> {
        return this.apiService.post<RefreshTokenResponse>('auth.refresh', {}).pipe(
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
        return this.apiService.post<any>('auth.logout', {}).pipe(

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
        this.authz.clear();
        this.menuService.clear();

        // 🔥 redirect to login
        window.location.href = '/auth/login';
    }


}



