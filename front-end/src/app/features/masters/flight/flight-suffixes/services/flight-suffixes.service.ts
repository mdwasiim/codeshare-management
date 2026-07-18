import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { FlightSuffix } from '@features/masters/flight/flight-suffixes/models/flight-suffixes.model';

@Injectable({ providedIn: 'root' })
export class FlightSuffixService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/flight-suffixes';

    getAll(params?: Record<string, string>) { return this.api.get<FlightSuffix[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<FlightSuffix>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: FlightSuffix) {
        return this.api.post<FlightSuffix>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Flight Suffixes created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Flight Suffixes'); return throwError(() => err); })
        );
    }
    update(id: string, payload: FlightSuffix) {
        return this.api.put<FlightSuffix>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Flight Suffixes updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Flight Suffixes'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Flight Suffixes deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Flight Suffixes'); return throwError(() => err); })
        );
    }
}
