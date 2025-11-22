import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, from } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { UserService } from './user.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private auth: UserService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.auth.getAccessToken();
    const clonedReq = token ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }) : req;

    return next.handle(clonedReq).pipe(
      catchError((err: HttpErrorResponse) => {
        if (err.status === 401) {
          return from(this.auth.refreshAccessToken().toPromise()).pipe(
            switchMap(newToken => {
              const retryReq = req.clone({ setHeaders: { Authorization: `Bearer ${newToken}` } });
              return next.handle(retryReq);
            }),
            catchError(refreshErr => { this.auth.logout(); return throwError(() => refreshErr); })
          );
        }
        return throwError(() => err);
      })
    );
  }
}
