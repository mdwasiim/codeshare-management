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

      // ✅ Only touch actual HTTP responses
      if (event instanceof HttpResponse && event.body?.success !== undefined) {

        const response = event.body as CSMServiceResponse<any>;

        // ❌ Do NOT return throwError here
        if (!response.success) {
          // ✅ throw synchronously
          throw response.error;
        }

        // ✅ unwrap data
        return event.clone({
          body: response.data
        });
      }

      return event;
    }),

    catchError((error: any) => {

      // Backend business error (success=false)
      if (error && error.message) {
        return throwError(() => error);
      }

      // HTTP-level error (401, 500, etc.)
      if (error instanceof HttpErrorResponse) {
        return throwError(() => error.error?.error || error);
      }

      return throwError(() => error);
    })
  );
};

