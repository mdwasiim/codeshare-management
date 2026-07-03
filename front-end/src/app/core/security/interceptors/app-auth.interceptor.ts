import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, catchError, filter, finalize, switchMap, take, throwError } from 'rxjs';

import { AuthTokenService } from '@services/auth/auth-token.service';
import { AuthService } from '@features/access-management/authentication/services/auth.service';
import { AuthTenantService } from '@services/auth/auth-tenant.service';

let isRefreshing = false;
const refreshTokenSubject = new BehaviorSubject<string | null>(null);

export const AppAuthInterceptor: HttpInterceptorFn = (req, next) => {
    const tokenService = inject(AuthTokenService);
    const authService = inject(AuthService);
    const authTenantService = inject(AuthTenantService);
    const router = inject(Router);

    const tenantCode = authTenantService.getTenantCode();

    const isAuthEndpoint = req.url.includes('/auth/login') || req.url.includes('/auth/refresh') || req.url.includes('/auth/logout');

    // =========================
    // Build headers ONCE
    // =========================
    const headers: Record<string, string> = {
        'X-Tenant-Id': tenantCode ?? ''
    };

    if (!isAuthEndpoint) {
        const existingAuth = req.headers.get('Authorization');
        const isLogout = req.url.includes('/auth/logout');
        if (isLogout && tokenService.refreshToken) {
            // ✅ FORCE refresh token for logout
            headers['Authorization'] = `Bearer ${tokenService.refreshToken}`;
        } else if (!existingAuth && tokenService.accessToken) {
            // ✅ normal flow
            headers['Authorization'] = `Bearer ${tokenService.accessToken}`;
        }
    }

    const authReq = req.clone({ setHeaders: headers });

    // 🚫 Do NOT refresh on auth endpoints
    if (isAuthEndpoint) {
        return next(authReq);
    }

    return next(authReq).pipe(
        catchError((error: HttpErrorResponse) => {
            // 🔐 ONLY handle 401
            if (error.status !== 401) {
                return throwError(() => error);
            }

            // 🚫 No refresh token → logout scenario
            if (!tokenService.refreshToken) {
                tokenService.clear();
                router.navigate(['/auth/login'], {
                    queryParams: { returnUrl: router.url }
                });
                return throwError(() => error);
            }

            // =========================
            // Refresh already in progress
            // =========================
            if (isRefreshing) {
                return refreshTokenSubject.pipe(
                    filter((token) => token !== null),
                    take(1),
                    switchMap((token) =>
                        next(
                            req.clone({
                                setHeaders: {
                                    Authorization: `Bearer ${token}`,
                                    'X-Tenant-Id': tenantCode ?? ''
                                }
                            })
                        )
                    )
                );
            }

            // =========================
            // Start refresh flow
            // =========================
            isRefreshing = true;
            refreshTokenSubject.next(null);

            return authService.refresh().pipe(
                switchMap((response) => {
                    isRefreshing = false;

                    // ✅ update tokens
                    tokenService.setSession(response.access_token, response.refresh_token, response.expires_in);

                    refreshTokenSubject.next(response.access_token);

                    // ✅ retry original request
                    return next(
                        req.clone({
                            setHeaders: {
                                Authorization: `Bearer ${response.access_token}`,
                                'X-Tenant-Id': tenantCode ?? ''
                            }
                        })
                    );
                }),

                catchError((err) => {
                    isRefreshing = false;

                    tokenService.clear();
                    refreshTokenSubject.next(null);

                    // 🔥 FORCE logout
                    router.navigate(['/auth/login'], {
                        queryParams: { returnUrl: router.url }
                    });

                    return throwError(() => err);
                }),

                finalize(() => {
                    isRefreshing = false;
                })
            );
        })
    );
};
