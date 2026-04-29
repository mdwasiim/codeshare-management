import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { ApiEndpointKey, buildApiUrl, ApiOptions } from '@core/config/app-api.config';

@Injectable({ providedIn: 'root' })
export class AppApiService {

    private http = inject(HttpClient);

    private buildParams(params?: Record<string, any>): HttpParams {
        let httpParams = new HttpParams();

        if (params) {
            Object.entries(params).forEach(([key, value]) => {
                if (value !== undefined && value !== null) {
                    httpParams = httpParams.append(key, String(value)); // ✅ fix
                }
            });
        }

        return httpParams;
    }

    private buildHeaders(headers?: HttpHeaders | Record<string, string>): HttpHeaders {
        if (!headers) return new HttpHeaders();

        return headers instanceof HttpHeaders
            ? headers
            : new HttpHeaders(headers);
    }

    // =========================
    // GET
    // =========================
    get<T>(endpoint: ApiEndpointKey, options?: ApiOptions) {
        return this.http.get<T>(buildApiUrl(endpoint, options?.pathParams), {
            params: this.buildParams(options?.params),
            headers: this.buildHeaders(options?.headers)
        });
    }

    // =========================
    // POST
    // =========================
    post<T, B = unknown>(
        endpoint: ApiEndpointKey,
        body: B,
        options?: ApiOptions
    ) {
        return this.http.post<T>(buildApiUrl(endpoint, options?.pathParams), body, {
            params: this.buildParams(options?.params),
            headers: this.buildHeaders(options?.headers)
        });
    }

    // =========================
    // PUT
    // =========================
    put<T, B = unknown>(
        endpoint: ApiEndpointKey,
        body: B,
        options?: ApiOptions
    ) {
        return this.http.put<T>(buildApiUrl(endpoint, options?.pathParams), body, {
            params: this.buildParams(options?.params),
            headers: this.buildHeaders(options?.headers)
        });
    }

    // =========================
    // DELETE
    // =========================
    delete<T>(endpoint: ApiEndpointKey, options?: ApiOptions) {
        return this.http.delete<T>(buildApiUrl(endpoint, options?.pathParams), {
            params: this.buildParams(options?.params),
            headers: this.buildHeaders(options?.headers)
        });
    }
}
