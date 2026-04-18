import { HttpHeaders } from '@angular/common/http';
import { environment } from 'src/environments/environment';

export interface ApiEndpoints {
  menu: string;
  login: string;
  logout: string;
  refresh: string;
  dashboardStats: string;
}

export interface ApiConfig {
  baseUrl: string;
  endpoints: ApiEndpoints;
}

export const API_CONFIG: Readonly<ApiConfig> = {
  baseUrl: environment.CSMBaseUrl,
  endpoints: {
    menu: '/identity/menus',
    login: '/identity/auth/login',
    logout: '/identity/auth/logout',
    refresh: '/identity/auth/refresh',
    dashboardStats: '/identity/dashboard/stats'
  }
};

export type ApiEndpointKey = keyof ApiEndpoints;

export const buildApiUrl = (key: ApiEndpointKey): string => {
  const base = API_CONFIG.baseUrl.replace(/\/$/, '');
  const path = API_CONFIG.endpoints[key].replace(/^\//, '');
  return `${base}/${path}`;
};

export interface ApiOptions {
  params?: Record<string, string | number | boolean>;
  headers?: HttpHeaders | Record<string, string>;
}

