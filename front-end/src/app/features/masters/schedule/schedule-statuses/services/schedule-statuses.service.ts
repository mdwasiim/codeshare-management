import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { ScheduleStatus } from '@features/masters/schedule/schedule-statuses/models/schedule-statuses.model';

@Injectable({ providedIn: 'root' })
export class ScheduleStatusService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/schedule-statuses';

    getAll(params?: Record<string, string>) { return this.api.get<ScheduleStatus[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<ScheduleStatus>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: ScheduleStatus) {
        return this.api.post<ScheduleStatus>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Schedule Statuses created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Schedule Statuses'); return throwError(() => err); })
        );
    }
    update(id: string, payload: ScheduleStatus) {
        return this.api.put<ScheduleStatus>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Schedule Statuses updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Schedule Statuses'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Schedule Statuses deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Schedule Statuses'); return throwError(() => err); })
        );
    }
}
