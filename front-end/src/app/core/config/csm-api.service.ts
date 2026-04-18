// csm-api.service.ts

import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { ApiEndpointKey, ApiOptions, buildApiUrl } from './csm-api.config';

@Injectable({
  providedIn: 'root'
})
export class CSMApiService {

  private http = inject(HttpClient);

  private buildHttpOptions(options?: ApiOptions): any {
  const httpOptions: any = {};

  if (options?.params) {
    httpOptions.params = this.buildParams(options.params);
  }

  if (options?.headers) {
    httpOptions.headers = this.buildHeaders(options.headers);
  }

  return httpOptions;
}

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

  private buildHeaders(headers?: HttpHeaders | Record<string, string>): HttpHeaders | undefined {
  if (!headers) return undefined;

  return headers instanceof HttpHeaders
    ? headers
    : new HttpHeaders(headers);
  }

get<T>(endpoint: ApiEndpointKey, options?: ApiOptions) {
  return this.http.get<T>(
    buildApiUrl(endpoint),
    this.buildHttpOptions(options)
  );
}

post<T>(endpoint: ApiEndpointKey, body: any, options?: ApiOptions) {
  return this.http.post<T>(
    buildApiUrl(endpoint),
    body,
    this.buildHttpOptions(options)
  );
}

put<T>(endpoint: ApiEndpointKey, body: any, options?: ApiOptions) {
  return this.http.put<T>(
    buildApiUrl(endpoint),
    body,
    this.buildHttpOptions(options)
  );
}

delete<T>(endpoint: ApiEndpointKey, options?: ApiOptions) {
  return this.http.delete<T>(
    buildApiUrl(endpoint),
    this.buildHttpOptions(options)
  );
}

}