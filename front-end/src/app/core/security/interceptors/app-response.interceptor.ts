import { HttpErrorResponse, HttpInterceptorFn, HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';

import { catchError, map, throwError } from 'rxjs';
import { ApiServiceResponse } from '@core/api/models/api-service-response.model';
import { AppToastService } from '@services/toast/app-toast.service';
import { SKIP_GLOBAL_ERROR_TOAST } from '@core/security/interceptors/http-context.tokens';

export const AppResponseInterceptor: HttpInterceptorFn = (req, next) => {
    const toast = inject(AppToastService);

    return next(req).pipe(
        map((event) => {
            if (event instanceof HttpResponse) {
                const body = event.body;

                if (body && typeof body === 'object' && 'success' in body) {
                    const response = body as ApiServiceResponse<any>;

                    if (!response.success) {
                        throw new HttpErrorResponse({
                            error: response,
                            status: 200,
                            statusText: resolveErrorMessage(response)
                        });
                    }

                    return event.clone({ body: response.data });
                }

                return event;
            }

            return event;
        }),

        catchError((error: unknown) => {
            if (error instanceof HttpErrorResponse) {
                const message = resolveErrorMessage(error.error) || error.message || 'Server error';
                const shouldToast = !req.context.get(SKIP_GLOBAL_ERROR_TOAST) && !req.url.includes('/auth/refresh');

                if (shouldToast) {
                    toast.error(message);
                }

                return throwError(() => new Error(message));
            }

            const normalizedError = error instanceof Error ? error : new Error('Unknown error');

            if (!req.context.get(SKIP_GLOBAL_ERROR_TOAST)) {
                toast.error(normalizedError.message);
            }

            return throwError(() => normalizedError);
        })
    );
};

function resolveErrorMessage(error: unknown): string {
    if (!error) return '';

    if (typeof error === 'string') return error;

    if (typeof error === 'object') {
        const value = error as Record<string, any>;

        return value['error']?.['message']
            || value['message']
            || value['error_description']
            || value['detail']
            || value['title']
            || '';
    }

    return '';
}
