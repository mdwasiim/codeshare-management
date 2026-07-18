import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { SchedulePriority } from '@features/masters/schedule/schedule-priorities/models/schedule-priorities.model';

@Injectable({ providedIn: 'root' })
export class SchedulePriorityService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/schedule-priorities';

    getAll(params?: Record<string, string>) { return this.api.get<SchedulePriority[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<SchedulePriority>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: SchedulePriority) {
        return this.api.post<SchedulePriority>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Schedule Priorities created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Schedule Priorities'); return throwError(() => err); })
        );
    }
    update(id: string, payload: SchedulePriority) {
        return this.api.put<SchedulePriority>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Schedule Priorities updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Schedule Priorities'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Schedule Priorities deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Schedule Priorities'); return throwError(() => err); })
        );
    }
}
