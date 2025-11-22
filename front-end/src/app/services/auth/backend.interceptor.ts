import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpResponse, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { mergeMap, delay } from 'rxjs/operators';

@Injectable()
export class BackendInterceptor implements HttpInterceptor {
  private users = [
    { username: 'admin', password: 'admin', roles: ['ADMIN'] },
    { username: 'user', password: 'user', roles: ['USER'] }
  ];
  private refreshTokens: { [username: string]: string } = {};

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return of(null).pipe(mergeMap(() => {
      // LOGIN
      if (req.url.endsWith('/login') && req.method === 'POST') {
        const { username, password } = req.body;
        const user = this.users.find(u => u.username === username && u.password === password);
        if (!user) return throwError(() => ({ status: 401, error: 'Invalid credentials' }));

        const now = Date.now();
        const accessToken = btoa(JSON.stringify({ username, roles: user.roles, exp: now + 300_000 }));
        const refreshToken = btoa(JSON.stringify({ username, roles: user.roles, exp: now + 7*24*60*60*1000 }));
        this.refreshTokens[username] = refreshToken;

        return of(new HttpResponse({ status: 200, body: { accessToken, refreshToken } }));
      }

      // REFRESH TOKEN
      if (req.url.endsWith('/refresh-token') && req.method === 'POST') {
        const { refreshToken } = req.body;
        if (!refreshToken) return throwError(() => ({ status: 401, error: 'No refresh token' }));
        let payload;
        try { payload = JSON.parse(atob(refreshToken)); } catch { return throwError(() => ({ status: 401, error: 'Invalid token' })); }
        if (payload.exp < Date.now()) return throwError(() => ({ status: 401, error: 'Refresh token expired' }));
        if (this.refreshTokens[payload.username] !== refreshToken) return throwError(() => ({ status: 401, error: 'Invalid refresh token' }));

        const newAccessToken = btoa(JSON.stringify({ username: payload.username, roles: payload.roles, exp: Date.now() + 300_000 }));
        return of(new HttpResponse({ status: 200, body: { accessToken: newAccessToken } }));
      }
      return next.handle(req);
    }), delay(500));
  }
}

export const backendProvider = { provide: HTTP_INTERCEPTORS, useClass: BackendInterceptor, multi: true };
