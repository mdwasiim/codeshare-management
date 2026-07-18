import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { TrafficRestrictionQualifier } from '@features/masters/flight/traffic-restriction-qualifiers/models/traffic-restriction-qualifiers.model';

@Injectable({ providedIn: 'root' })
export class TrafficRestrictionQualifierService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/traffic-restriction-qualifiers';

    getAll(params?: Record<string, string>) { return this.api.get<TrafficRestrictionQualifier[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<TrafficRestrictionQualifier>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: TrafficRestrictionQualifier) {
        return this.api.post<TrafficRestrictionQualifier>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Traffic Restriction Qualifiers created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Traffic Restriction Qualifiers'); return throwError(() => err); })
        );
    }
    update(id: string, payload: TrafficRestrictionQualifier) {
        return this.api.put<TrafficRestrictionQualifier>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Traffic Restriction Qualifiers updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Traffic Restriction Qualifiers'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Traffic Restriction Qualifiers deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Traffic Restriction Qualifiers'); return throwError(() => err); })
        );
    }
}
