import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';

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


@Injectable({
  providedIn: 'root'
})
export class CSMApiService {

  private http = inject(HttpClient);

  private baseUrl = environment.CSMBaseUrl;  
  private config = CSM_API_CONFIG;

  private buildParams(params?: Record<string, any>): HttpParams {
    let httpParams = new HttpParams();
    if (params) {
      Object.keys(params).forEach(key => {
        if (params[key] !== undefined && params[key] !== null) {
          httpParams = httpParams.append(key, params[key]);
        }
      });
    }
    return httpParams;
  }

  get<T>(endpointKey: keyof typeof CSM_API_CONFIG.endpoints, params?: any) {
    return this.http.get<T>(
      `${this.baseUrl}/${this.config.endpoints[endpointKey]}`,
      { params: this.buildParams(params) }
    );
  }

  post<T>(endpointKey: keyof typeof CSM_API_CONFIG.endpoints, body: any, params?: any) {
    return this.http.post<T>(
      `${this.baseUrl}/${this.config.endpoints[endpointKey]}`,
      body,
      { params: this.buildParams(params) }
    );
  }

  put<T>(endpointKey: keyof typeof CSM_API_CONFIG.endpoints, body: any) {
    return this.http.put<T>(
      `${this.baseUrl}/${this.config.endpoints[endpointKey]}`,
      body
    );
  }

  delete<T>(endpointKey: keyof typeof CSM_API_CONFIG.endpoints, params?: any) {
    return this.http.delete<T>(
      `${this.baseUrl}/${this.config.endpoints[endpointKey]}`,
      { params: this.buildParams(params) }
    );
  }
}
