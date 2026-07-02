import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { ScheduleChannel } from '@features/masters/schedule/schedule-channels/models/schedule-channels.model';

@Injectable({ providedIn: 'root' })
export class ScheduleChannelService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/schedule-channels';

    getAll() { return this.api.get<ScheduleChannel[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<ScheduleChannel>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: ScheduleChannel) {
        return this.api.post<ScheduleChannel>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Schedule Channels created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Schedule Channels'); return throwError(() => err); })
        );
    }
    update(id: string, payload: ScheduleChannel) {
        return this.api.put<ScheduleChannel>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Schedule Channels updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Schedule Channels'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Schedule Channels deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Schedule Channels'); return throwError(() => err); })
        );
    }
}
