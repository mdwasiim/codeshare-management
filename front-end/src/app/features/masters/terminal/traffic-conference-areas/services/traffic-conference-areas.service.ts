import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { TrafficConferenceArea } from '@features/masters/terminal/traffic-conference-areas/models/traffic-conference-areas.model';

@Injectable({ providedIn: 'root' })
export class TrafficConferenceAreaService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/traffic-conference-areas';

    getAll(params?: Record<string, string>) { return this.api.get<TrafficConferenceArea[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<TrafficConferenceArea>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: TrafficConferenceArea) {
        return this.api.post<TrafficConferenceArea>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Traffic Conference Areas created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Traffic Conference Areas'); return throwError(() => err); })
        );
    }
    update(id: string, payload: TrafficConferenceArea) {
        return this.api.put<TrafficConferenceArea>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Traffic Conference Areas updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Traffic Conference Areas'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Traffic Conference Areas deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Traffic Conference Areas'); return throwError(() => err); })
        );
    }
}
