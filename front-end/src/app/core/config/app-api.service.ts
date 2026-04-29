// app-api.service.ts

import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { ApiEndpointKey, ApiOptions, buildApiUrl } from './app-api.config';

@Injectable({
    providedIn: 'root'
})
export class AppApiService {

    private http = inject(HttpClient);

    // -----------------------------
    // Build HTTP Options
    // -----------------------------
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

    // -----------------------------
    // Build Query Params
    // -----------------------------
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

    // -----------------------------
    // Build Headers
    // -----------------------------
    private buildHeaders(headers?: HttpHeaders | Record<string, string>): HttpHeaders | undefined {
        if (!headers) return undefined;

        return headers instanceof HttpHeaders
            ? headers
            : new HttpHeaders(headers);
    }

    // -----------------------------
    // Replace Path Params
    // -----------------------------
    private replacePathParams(
        url: string,
        pathParams?: Record<string, string | number | boolean>
    ): string {
        if (!pathParams) return url;

        Object.entries(pathParams).forEach(([key, value]) => {
            url = url.replace(`{${key}}`, String(value));
        });

        return url;
    }

    // -----------------------------
    // Build Final URL
    // -----------------------------
    private buildUrl(endpoint: ApiEndpointKey, options?: ApiOptions): string {
        let url = buildApiUrl(endpoint);

        url = this.replacePathParams(url, options?.pathParams);

        // Optional safety check
        if (url.includes('{')) {
            console.warn('⚠️ Missing pathParams for URL:', url);
        }

        return url;
    }

    // -----------------------------
    // HTTP METHODS
    // -----------------------------

    get<T>(endpoint: ApiEndpointKey, options?: ApiOptions) {
        return this.http.get<T>(
            this.buildUrl(endpoint, options),
            this.buildHttpOptions(options)
        );
    }

    post<T>(endpoint: ApiEndpointKey, body: any, options?: ApiOptions) {
        return this.http.post<T>(
            this.buildUrl(endpoint, options),   // ✅ FIXED
            body,
            this.buildHttpOptions(options)
        );
    }

    put<T>(endpoint: ApiEndpointKey, body: any, options?: ApiOptions) {
        return this.http.put<T>(
            this.buildUrl(endpoint, options),   // ✅ FIXED
            body,
            this.buildHttpOptions(options)
        );
    }

    delete<T>(endpoint: ApiEndpointKey, options?: ApiOptions) {
        return this.http.delete<T>(
            this.buildUrl(endpoint, options),   // ✅ FIXED
            this.buildHttpOptions(options)
        );
    }
}
