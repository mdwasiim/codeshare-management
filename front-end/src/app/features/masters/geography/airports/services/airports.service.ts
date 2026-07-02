import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { Airport } from '@features/masters/geography/airports/models/airports.model';

@Injectable({ providedIn: 'root' })
export class AirportService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/airports';

    getAll() { return this.api.get<Airport[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<Airport>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: Airport) {
        return this.api.post<Airport>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Airports created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Airports'); return throwError(() => err); })
        );
    }
    update(id: string, payload: Airport) {
        return this.api.put<Airport>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Airports updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Airports'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Airports deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Airports'); return throwError(() => err); })
        );
    }
}
