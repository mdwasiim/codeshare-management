export interface CSMApiConfig {
  BASE_URL: string;
  endpoints: {
    menu: string;
    login: string;
    dashboardStats: string;
  };
}

export const CSM_API_CONFIG: CSMApiConfig = {
  BASE_URL: '',
  endpoints: {
    menu: 'menu',
    login: 'auth/login',
    dashboardStats: 'dashboard/stats'
  }
};
