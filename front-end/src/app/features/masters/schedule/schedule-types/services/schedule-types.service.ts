import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { ScheduleType } from '@features/masters/schedule/schedule-types/models/schedule-types.model';

@Injectable({ providedIn: 'root' })
export class ScheduleTypeService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/schedule-types';

    getAll() { return this.api.get<ScheduleType[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<ScheduleType>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: ScheduleType) {
        return this.api.post<ScheduleType>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Schedule Types created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Schedule Types'); return throwError(() => err); })
        );
    }
    update(id: string, payload: ScheduleType) {
        return this.api.put<ScheduleType>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Schedule Types updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Schedule Types'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Schedule Types deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Schedule Types'); return throwError(() => err); })
        );
    }
}
