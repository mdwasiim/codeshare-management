import {
    HttpInterceptorFn,
    HttpEvent,
    HttpResponse,
    HttpErrorResponse
} from '@angular/common/http';

import { map, catchError, throwError } from 'rxjs';
import { CSMServiceResponse } from '@core/models/app-service-response.model';

export const AppResponseInterceptor: HttpInterceptorFn = (req, next) => {

    return next(req).pipe(

        map((event: HttpEvent<any>) => {

            if (event instanceof HttpResponse) {

                const body = event.body;

                // ✅ Handle CSM standard response
                if (body && typeof body === 'object' && 'success' in body) {

                    const response = body as CSMServiceResponse<any>;

                    if (!response.success) {
                        // 🔥 Preserve HTTP structure
                        throw new HttpErrorResponse({
                            error: response,
                            status: 200, // business error, not HTTP error
                            statusText: response.error?.message || 'Business Error'
                        });
                    }

                    // ✅ unwrap data
                    return event.clone({
                        body: response.data
                    });
                }
            }

            return event;
        }),

        catchError((error: unknown) => {

            // ✅ HTTP errors (including business errors we created)
            if (error instanceof HttpErrorResponse) {

                console.error('HTTP Error:', error);

                const message =
                    error.error?.error?.message ||
                    error.error?.message ||
                    error.message ||
                    'Server error';

                return throwError(() => new Error(message));
            }

            // ✅ fallback
            return throwError(() =>
                error instanceof Error
                    ? error
                    : new Error('Unknown error')
            );
        })
    );
};
