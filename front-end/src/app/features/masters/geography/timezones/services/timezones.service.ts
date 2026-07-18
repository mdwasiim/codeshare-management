import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { Timezone } from '@features/masters/geography/timezones/models/timezones.model';

@Injectable({ providedIn: 'root' })
export class TimezoneService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/timezones';

    getAll(params?: Record<string, string>) { return this.api.get<Timezone[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<Timezone>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: Timezone) {
        return this.api.post<Timezone>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Time Zones created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Time Zones'); return throwError(() => err); })
        );
    }
    update(id: string, payload: Timezone) {
        return this.api.put<Timezone>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Time Zones updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Time Zones'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Time Zones deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Time Zones'); return throwError(() => err); })
        );
    }
}
