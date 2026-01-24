import { Injectable } from '@angular/core';


@Injectable({ providedIn: 'root' })
export class TokenService {

  // =========================
  // Access token (READ ONLY)
  // =========================
  get accessToken(): string | null {
    return localStorage.getItem('access_token');
  }

  // =========================
  // Refresh token (READ ONLY)
  // =========================
  get refreshToken(): string | null {
    return localStorage.getItem('refresh_token');
  }

  // =========================
  // Save tokens
  // =========================
  setTokens(access: string, refresh: string, expiresIn: number) {
  localStorage.setItem('access_token', access);
  localStorage.setItem('refresh_token', refresh);
  localStorage.setItem(
    'expires_at',
    (Date.now() + expiresIn * 1000).toString()
  );
}


  // =========================
  // Clear tokens (logout)
  // =========================
  clear(): void {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    localStorage.removeItem('expires_at');
  }

  // =========================
  // Token validity check
  // =========================
  isAuthenticated(): boolean {
    const token = this.accessToken;
    if (!token) {
      return false;
    }

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  }

  // =========================
  // Token expiry helper (optional but useful)
  // =========================
  isTokenExpired(): boolean {
    const expiresAt = localStorage.getItem('expires_at');
    if (!expiresAt) return true;

    return Date.now() > Number(expiresAt);
  }

  
}

