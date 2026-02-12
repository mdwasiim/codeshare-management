import {
  HttpInterceptorFn,
  HttpErrorResponse
} from '@angular/common/http';
import { inject } from '@angular/core';
import {
  BehaviorSubject,
  catchError,
  filter,
  finalize,
  switchMap,
  take,
  throwError,
} from 'rxjs';

import { TokenService } from '@services/auth/token.service';
import { AuthService } from '@services/auth/auth.service';
import { TenantService } from '@core/services/auth/tenant.service';

let isRefreshing = false;
const refreshTokenSubject = new BehaviorSubject<string | null>(null);

export const AuthInterceptor: HttpInterceptorFn = (req, next) => {

  const tokenService = inject(TokenService);
  const authService = inject(AuthService);
  const tenantService = inject(TenantService);

  const tenantCode = tenantService.getTenantCode();

  if (!tenantCode) {
    console.warn('Tenant code missing on frontend');
  }
  
  const isAuthEndpoint = req.url.includes('/auth/login') || req.url.includes('/auth/refresh');

  // =========================
  // Build headers ONCE
  // =========================
  const headers: Record<string, string> = {
    'tenant-code': tenantCode ?? ''
  };

  if (!isAuthEndpoint && tokenService.accessToken) {
    headers['Authorization'] = `Bearer ${tokenService.accessToken}`;
  }

  const authReq = req.clone({ setHeaders: headers });

  // 🚫 Do NOT refresh on auth endpoints
  if (isAuthEndpoint) {
    return next(authReq);
  }

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {

      if (error.status === 401) {

        // 🚫 NO refresh token = initial login / unauthenticated
        if (!tokenService.refreshToken) {
          return throwError(() => error);
        }

        // otherwise → refresh flow
      }


      // =========================
      // Refresh already running
      // =========================
      if (isRefreshing) {
        return refreshTokenSubject.pipe(
          filter(token => token !== null),
          take(1),
          switchMap(token =>
            next(
              req.clone({
                setHeaders: {
                  Authorization: `Bearer ${token}`,
                  'tenant-code': tenantCode ?? ''
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
        switchMap(response => {
          isRefreshing = false;

          tokenService.setTokens(
            response.access_token,
            response.refresh_token,
            response.expires_in
          );

          refreshTokenSubject.next(response.access_token);

          return next(
            req.clone({
              setHeaders: {
                Authorization: `Bearer ${response.access_token}`,
                'tenant-code': tenantCode ?? ''
              }
            })
          );
        }),
        catchError(err => {
          isRefreshing = false;
          tokenService.clear();
          window.location.href = '/auth/login';
          return throwError(() => err);
        })
      );
    }),

  );
};
