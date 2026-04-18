import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { API_CONFIG, ApiEndpointKey, buildApiUrl } from '@/core/config/csm-api.config';

@Injectable({ providedIn: 'root' })
export class CSMApiService {

  private http = inject(HttpClient);

  private buildParams(params?: Record<string, any>): HttpParams {
    let httpParams = new HttpParams();
    if (params) {
      Object.entries(params).forEach(([key, value]) => {
        if (value !== undefined && value !== null) {
          httpParams = httpParams.append(key, value);
        }
      });
    }
    return httpParams;
  }

  get<T>(endpoint: ApiEndpointKey, params?: any) {
    return this.http.get<T>(buildApiUrl(endpoint),
      { params: this.buildParams(params) }
    );
  }

  post<T>(endpoint: ApiEndpointKey, body: any, params?: any) {
    return this.http.post<T>(buildApiUrl(endpoint),
      body,
      { params: this.buildParams(params) }
    );
  }

  put<T>(endpoint: ApiEndpointKey, body: any) {
    return this.http.put<T>(buildApiUrl(endpoint),
      body
    );
  }

  delete<T>(endpoint: ApiEndpointKey, params?: any) {
    return this.http.delete<T>(buildApiUrl(endpoint),
      { params: this.buildParams(params) }
    );
  }
}
