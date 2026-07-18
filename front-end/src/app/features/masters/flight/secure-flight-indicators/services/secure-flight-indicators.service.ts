import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { SecureFlightIndicator } from '@features/masters/flight/secure-flight-indicators/models/secure-flight-indicators.model';

@Injectable({ providedIn: 'root' })
export class SecureFlightIndicatorService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/secure-flight-indicators';

    getAll(params?: Record<string, string>) { return this.api.get<SecureFlightIndicator[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<SecureFlightIndicator>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: SecureFlightIndicator) {
        return this.api.post<SecureFlightIndicator>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Secure Flight Indicators created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Secure Flight Indicators'); return throwError(() => err); })
        );
    }
    update(id: string, payload: SecureFlightIndicator) {
        return this.api.put<SecureFlightIndicator>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Secure Flight Indicators updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Secure Flight Indicators'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Secure Flight Indicators deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Secure Flight Indicators'); return throwError(() => err); })
        );
    }
}
