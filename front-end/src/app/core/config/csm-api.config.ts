import { environment } from 'src/environments/environment';

export interface ApiEndpoints {
  menu: string;
  login: string;
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
    menu: '/identity/api/menus',
    login: '/identity/api/auth/login',
    refresh: '/identity/api/auth/refresh',
    dashboardStats: '/identity/api/dashboard/stats'
  }
};

export type ApiEndpointKey = keyof ApiEndpoints;

export const buildApiUrl = (key: ApiEndpointKey): string => {
  const base = API_CONFIG.baseUrl.replace(/\/$/, '');
  const path = API_CONFIG.endpoints[key].replace(/^\//, '');
  return `${base}/${path}`;
};