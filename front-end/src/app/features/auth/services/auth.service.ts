import {Injectable} from '@angular/core';
import {LoginResponse} from '@features/auth/models/login-response.model';
import {RefreshTokenResponse} from '@features/auth/models/refresh-token-response.model';
import {catchError, Observable, of, tap} from 'rxjs';
import {AuthzService} from "@services/authz.service";
import {AppTokenService} from "@services/auth/app-token.service";
import {LayoutMenuService} from "@layout/services/layout-menu.service";
import {map} from "rxjs/operators";
import { switchMap } from 'rxjs';
import {AppApiService} from "@core/config/app-api.service";

@Injectable({ providedIn: 'root' })
export class AuthService {

    constructor(
        private apiService: AppApiService,
        private authz: AuthzService,
        private tokenService: AppTokenService,
        private menuService: LayoutMenuService
    ) {}



    login(username: string, password: string): Observable<LoginResponse> {
        return this.apiService.post<LoginResponse>('auth.login', { username, password }).pipe(

            tap(res => {
                this.tokenService.setSession(
                    res.access_token,
                    res.refresh_token,
                    res.expires_in
                );

                this.authz.setUserAccess(res.roles, res.permissions);
            }),

            switchMap(res =>
                this.menuService.loadMenus().pipe(
                    map(() => res)   // ✅ return original response
                )
            )
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



