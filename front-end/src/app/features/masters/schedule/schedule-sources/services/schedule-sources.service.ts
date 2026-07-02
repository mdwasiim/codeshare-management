import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { ScheduleSource } from '@features/masters/schedule/schedule-sources/models/schedule-sources.model';

@Injectable({ providedIn: 'root' })
export class ScheduleSourceService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/schedule-sources';

    getAll() { return this.api.get<ScheduleSource[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<ScheduleSource>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: ScheduleSource) {
        return this.api.post<ScheduleSource>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Schedule Sources created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Schedule Sources'); return throwError(() => err); })
        );
    }
    update(id: string, payload: ScheduleSource) {
        return this.api.put<ScheduleSource>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Schedule Sources updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Schedule Sources'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Schedule Sources deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Schedule Sources'); return throwError(() => err); })
        );
    }
}
