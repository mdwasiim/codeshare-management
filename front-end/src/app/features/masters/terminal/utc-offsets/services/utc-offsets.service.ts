import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { UtcOffset } from '@features/masters/terminal/utc-offsets/models/utc-offsets.model';

@Injectable({ providedIn: 'root' })
export class UtcOffsetService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/utc-offsets';

    getAll() { return this.api.get<UtcOffset[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<UtcOffset>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: UtcOffset) {
        return this.api.post<UtcOffset>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('UTC Offsets created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create UTC Offsets'); return throwError(() => err); })
        );
    }
    update(id: string, payload: UtcOffset) {
        return this.api.put<UtcOffset>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('UTC Offsets updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update UTC Offsets'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('UTC Offsets deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete UTC Offsets'); return throwError(() => err); })
        );
    }
}
