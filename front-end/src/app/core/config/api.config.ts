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

export const API_CONFIG = {
  endpoints: {
    menu: 'api/menus',
    login: 'api/auth/login',
    refresh: 'api/auth/refresh',
    dashboardStats: 'dashboard/stats'
  }
} as const;

export type ApiEndpointKey = keyof typeof API_CONFIG.endpoints;
