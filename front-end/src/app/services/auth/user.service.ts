import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { tap, map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { User } from '../../interfaces/auth/auth.interface';

@Injectable({ providedIn: 'root' })
export class UserService {
  private accessTokenKey = 'access_token';
  private refreshTokenKey = 'refresh_token';

  constructor(private http: HttpClient) {}

//  login(username: string, password: string): Observable<{ accessToken: string; refreshToken: string; role: string }> {
//     const mockUsers = [
//       { username: 'admin', password: 'admin123', role: 'ADMIN' },
//       { username: 'user', password: 'user123', role: 'USER' }
//     ];

//     const user = mockUsers.find(u => u.username === username && u.password === password);

//     if (!user) {
//       return throwError(() => new Error('Invalid username or password'));
//     }

//     const fakeTokens = {
//       accessToken: btoa(JSON.stringify({ username: user.username, role: user.role })),
//       refreshToken: btoa(user.username + '-refresh-token'),
//       role: user.role
//     };

//     return of(fakeTokens).pipe(
//       tap(res => {
//         localStorage.setItem(this.accessTokenKey, res.accessToken);
//         localStorage.setItem(this.refreshTokenKey, res.refreshToken);
//         localStorage.setItem('user_role', res.role);
//       })
//     );
//   }


  login(username: string, password: string): Observable<void> {
    return this.http.post<{ accessToken: string; refreshToken: string }>(
      `${environment.apiUrl}/auth/login`, { username, password }
    ).pipe(
      tap(res => {
        localStorage.setItem(this.accessTokenKey, res.accessToken);
        localStorage.setItem(this.refreshTokenKey, res.refreshToken);
      }),
      map(() => {})
    );
  }

  logout() {
    localStorage.removeItem(this.accessTokenKey);
    localStorage.removeItem(this.refreshTokenKey);
  }

  getAccessToken(): string | null { return localStorage.getItem(this.accessTokenKey); }
  getRefreshToken(): string | null { return localStorage.getItem(this.refreshTokenKey); }

  getUser(): User | null {
    const token = this.getAccessToken();
    return token ? JSON.parse(atob(token)) : null;
  }

  isLoggedIn(): boolean { return !!this.getAccessToken(); }

  hasRole(role: string): boolean {
    const user = this.getUser();
    return user ? user.roles.includes(role) : false;
  }

  refreshAccessToken(): Observable<string> {
    const refreshToken = this.getRefreshToken();
    if (!refreshToken) return throwError(() => new Error('No refresh token'));
    return this.http.post<{ accessToken: string }>(
      `${environment.apiUrl}/refresh-token`, { refreshToken }
    ).pipe(
      tap(res => localStorage.setItem(this.accessTokenKey, res.accessToken)),
      map(res => res.accessToken)
    );
  }
}
