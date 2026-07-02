import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { ScheduleCategory } from '@features/masters/schedule/schedule-categories/models/schedule-categories.model';

@Injectable({ providedIn: 'root' })
export class ScheduleCategoryService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/schedule-categories';

    getAll() { return this.api.get<ScheduleCategory[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<ScheduleCategory>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: ScheduleCategory) {
        return this.api.post<ScheduleCategory>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Schedule Categories created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Schedule Categories'); return throwError(() => err); })
        );
    }
    update(id: string, payload: ScheduleCategory) {
        return this.api.put<ScheduleCategory>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Schedule Categories updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Schedule Categories'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Schedule Categories deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Schedule Categories'); return throwError(() => err); })
        );
    }
}
