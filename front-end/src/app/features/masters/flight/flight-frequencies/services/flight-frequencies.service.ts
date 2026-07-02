import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { FlightFrequency } from '@features/masters/flight/flight-frequencies/models/flight-frequencies.model';

@Injectable({ providedIn: 'root' })
export class FlightFrequencyService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/flight-frequencies';

    getAll() { return this.api.get<FlightFrequency[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<FlightFrequency>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: FlightFrequency) {
        return this.api.post<FlightFrequency>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Flight Frequencies created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Flight Frequencies'); return throwError(() => err); })
        );
    }
    update(id: string, payload: FlightFrequency) {
        return this.api.put<FlightFrequency>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Flight Frequencies updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Flight Frequencies'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Flight Frequencies deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Flight Frequencies'); return throwError(() => err); })
        );
    }
}
