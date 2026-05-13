import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AnyApiEndpointFactory, ApiOptions, API_CONFIG } from './app-api.config';

@Injectable({
    providedIn: 'root'
})
export class AppApiService {
    private http = inject(HttpClient);

    private buildHttpOptions(options?: ApiOptions): {
        headers?: HttpHeaders;
        params?: HttpParams;
    } {
        const httpOptions: {
            headers?: HttpHeaders;
            params?: HttpParams;
        } = {};

        if (options?.params) {
            httpOptions.params = this.buildParams(options.params);
        }

        if (options?.headers) {
            httpOptions.headers = this.buildHeaders(options.headers);
        }

        return httpOptions;
    }

    private buildParams(params?: ApiOptions['params']): HttpParams {
        let httpParams = new HttpParams();

        if (params) {
            Object.entries(params).forEach(([key, value]) => {
                if (value === undefined || value === null) return;

                if (Array.isArray(value)) {
                    value.forEach(item => {
                        if (item !== undefined && item !== null) {
                            httpParams = httpParams.append(key, String(item));
                        }
                    });
                    return;
                }

                httpParams = httpParams.append(key, String(value));
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

    private buildUrl(endpoint: string | AnyApiEndpointFactory, options?: ApiOptions): string {
        if (typeof endpoint === 'function') {
            return endpoint(options?.pathParams);
        }

        if (endpoint.startsWith('http://') || endpoint.startsWith('https://')) {
            return endpoint;
        }

        const base = API_CONFIG.baseUrl;
        const path = endpoint.replace(/^\//, '');
        return `${base}/${path}`;
    }

    get<T>(endpoint: string | AnyApiEndpointFactory, options?: ApiOptions): Observable<T> {
        return this.http.get<T>(
            this.buildUrl(endpoint, options),
            this.buildHttpOptions(options)
        );
    }

    post<T>(endpoint: string | AnyApiEndpointFactory, body: any, options?: ApiOptions): Observable<T> {
        return this.http.post<T>(
            this.buildUrl(endpoint, options),
            body,
            this.buildHttpOptions(options)
        );
    }

    put<T>(endpoint: string | AnyApiEndpointFactory, body: any, options?: ApiOptions): Observable<T> {
        return this.http.put<T>(
            this.buildUrl(endpoint, options),
            body,
            this.buildHttpOptions(options)
        );
    }

    delete<T>(endpoint: string | AnyApiEndpointFactory, options?: ApiOptions): Observable<T> {
        return this.http.delete<T>(
            this.buildUrl(endpoint, options),
            this.buildHttpOptions(options)
        );
    }
}
