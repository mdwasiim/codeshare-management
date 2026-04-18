import {
  HttpInterceptorFn,
  HttpEvent,
  HttpResponse,
  HttpErrorResponse
} from '@angular/common/http';
import { map, catchError, throwError } from 'rxjs';
import { CSMServiceResponse } from '@/core/models/response/csm-service-esponse.model';

export const CSMResponseInterceptor: HttpInterceptorFn = (req, next) => {

  return next(req).pipe(

    map((event: HttpEvent<any>) => {

      if (event instanceof HttpResponse) {

        const body = event.body;

        // ✅ Safely check CSM response structure
        if (body && typeof body === 'object' && 'success' in body) {

          const response = body as CSMServiceResponse<any>;

          if (!response.success) {
            throw new Error(response.error?.message || 'Unknown error');
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

      // ✅ HTTP error
      if (error instanceof HttpErrorResponse) {

        console.error('HTTP Error:', error);

        return throwError(() =>
          new Error(
            error.error?.error?.message ||
            error.error?.message ||
            error.message ||
            'Server error'
          )
        );
      }

      // ✅ Business / custom error
      return throwError(() =>
        error instanceof Error
          ? error
          : new Error('Unknown error')
      );
    })
  );
};