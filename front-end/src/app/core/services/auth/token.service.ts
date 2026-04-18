import { Injectable } from '@angular/core';


@Injectable({ providedIn: 'root' })
export class TokenService {

  /**
   * Returns the current access token used for API authentication.
   * This token is typically sent in the Authorization header.
   */
  get accessToken(): string | null {
    return localStorage.getItem('access_token');
  }

  /**
   * Returns the refresh token used to obtain a new access token
   * when the current one expires.
   */
  get refreshToken(): string | null {
    return localStorage.getItem('refresh_token');
  }

  /**
   * Returns the tenant identifier associated with the current user session.
   * This value is required for multi-tenant backend requests.
   */
  get tenant(): string | null {
    return localStorage.getItem('tenant');
  }

  /**
   * Returns the formatted Authorization header value.
   * Example: "Bearer <access_token>"
   * Used by HTTP interceptors to attach authentication automatically.
   */
  get authorizationHeader(): string | null {
    return this.accessToken ? `Bearer ${this.accessToken}` : null;
  }

  /**
   * Stores access and refresh tokens along with expiry time.
   *
   * @param access - JWT access token
   * @param refresh - JWT refresh token
   * @param expiresIn - Expiry time in seconds
   */
  setTokens(access: string, refresh: string, expiresIn: number) {
    localStorage.setItem('access_token', access);
    localStorage.setItem('refresh_token', refresh);
    localStorage.setItem(
      'expires_at',
      (Date.now() + expiresIn * 1000).toString()
    );
  }

  /**
   * Stores tenant identifier for the current session.
   * This is required for backend multi-tenant routing.
   *
   * @param tenant - Tenant code (e.g., "QR")
   */
  setTenant(tenant: string) {
    localStorage.setItem('tenant', tenant);
  }

  /**
   * Clears all authentication-related data from storage.
   * Typically used during logout.
   */
  clear(): void {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    localStorage.removeItem('expires_at');
    localStorage.removeItem('tenant');
  }

  /**
   * Checks whether the user is currently authenticated.
   * A user is considered authenticated if:
   * - Access token exists
   * - Token is not expired
   *
   * @returns true if authenticated, otherwise false
   */
  isAuthenticated(): boolean {
    return !!this.accessToken && !this.isTokenExpired();
  }

  /**
   * Checks whether the access token has expired.
   *
   * @returns true if token is expired or missing, otherwise false
   */
  isTokenExpired(): boolean {
    const expiresAt = localStorage.getItem('expires_at');
    if (!expiresAt) return true;

    return Date.now() >= Number(expiresAt);
  }

  /**
   * Decodes the JWT payload safely.
   * Useful for extracting user information like roles or username.
   *
   * @param token - JWT token
   * @returns decoded payload or null if invalid
   */
  private decodeToken(token: string): any | null {
    try {
      return JSON.parse(atob(token.split('.')[1]));
    } catch {
      return null;
    }
  }
}
