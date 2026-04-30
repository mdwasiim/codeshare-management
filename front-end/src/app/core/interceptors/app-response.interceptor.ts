import {
    HttpInterceptorFn,
    HttpResponse,
    HttpErrorResponse
} from '@angular/common/http';

import { map, catchError, throwError } from 'rxjs';
import { CSMServiceResponse } from '@core/models/app-service-response.model';

export const AppResponseInterceptor: HttpInterceptorFn = (req, next) => {

    return next(req).pipe(

        map((event) => {

            if (event instanceof HttpResponse) {

                const body = event.body;

                if (body && typeof body === 'object' && 'success' in body) {

                    const response = body as CSMServiceResponse<any>;

                    if (!response.success) {
                        throw new HttpErrorResponse({
                            error: response,
                            status: 200,
                            statusText: response.error?.message || 'Business Error'
                        });
                    }

                    // ✅ FIX: clone response, keep HttpResponse structure
                    return event.clone({ body: response.data });
                }

                return event;
            }

            return event;
        }),

        catchError((error: unknown) => {

            if (error instanceof HttpErrorResponse) {

                const message =
                    error.error?.error?.message ||
                    error.error?.message ||
                    error.message ||
                    'Server error';

                return throwError(() => new Error(message));
            }

            return throwError(() =>
                error instanceof Error
                    ? error
                    : new Error('Unknown error')
            );
        })
    );
};
