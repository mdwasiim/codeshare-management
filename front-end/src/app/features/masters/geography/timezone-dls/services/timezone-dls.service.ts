import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { TimezoneDls } from '@features/masters/geography/timezone-dls/models/timezone-dls.model';

@Injectable({ providedIn: 'root' })
export class TimezoneDlsService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/dst-rules';

    getAll() { return this.api.get<TimezoneDls[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<TimezoneDls>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: TimezoneDls) {
        return this.api.post<TimezoneDls>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Time Zone Offset Period created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Time Zone Offset Period'); return throwError(() => err); })
        );
    }
    update(id: string, payload: TimezoneDls) {
        return this.api.put<TimezoneDls>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Time Zone Offset Period updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Time Zone Offset Period'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Time Zone Offset Period deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Time Zone Offset Period'); return throwError(() => err); })
        );
    }
}
