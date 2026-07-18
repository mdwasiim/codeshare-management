import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { Season } from '@features/masters/schedule/seasons/models/seasons.model';

@Injectable({ providedIn: 'root' })
export class SeasonService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/seasons';

    getAll(params?: Record<string, string>) { return this.api.get<Season[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<Season>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: Season) {
        return this.api.post<Season>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Seasons created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Seasons'); return throwError(() => err); })
        );
    }
    update(id: string, payload: Season) {
        return this.api.put<Season>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Seasons updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Seasons'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Seasons deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Seasons'); return throwError(() => err); })
        );
    }
}
