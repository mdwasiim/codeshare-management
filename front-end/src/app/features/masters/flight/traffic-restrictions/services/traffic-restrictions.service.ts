import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { TrafficRestriction } from '@features/masters/flight/traffic-restrictions/models/traffic-restrictions.model';

@Injectable({ providedIn: 'root' })
export class TrafficRestrictionService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/traffic-restriction-codes';

    getAll(params?: Record<string, string>) { return this.api.get<TrafficRestriction[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<TrafficRestriction>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: TrafficRestriction) {
        return this.api.post<TrafficRestriction>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Traffic Restrictions created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Traffic Restrictions'); return throwError(() => err); })
        );
    }
    update(id: string, payload: TrafficRestriction) {
        return this.api.put<TrafficRestriction>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Traffic Restrictions updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Traffic Restrictions'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Traffic Restrictions deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Traffic Restrictions'); return throwError(() => err); })
        );
    }
}
