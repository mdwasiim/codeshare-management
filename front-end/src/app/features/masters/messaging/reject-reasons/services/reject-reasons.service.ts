import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { RejectReason } from '@features/masters/messaging/reject-reasons/models/reject-reasons.model';

@Injectable({ providedIn: 'root' })
export class RejectReasonService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/reject-reasons';

    getAll() { return this.api.get<RejectReason[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<RejectReason>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: RejectReason) {
        return this.api.post<RejectReason>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Reject Reasons created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Reject Reasons'); return throwError(() => err); })
        );
    }
    update(id: string, payload: RejectReason) {
        return this.api.put<RejectReason>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Reject Reasons updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Reject Reasons'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Reject Reasons deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Reject Reasons'); return throwError(() => err); })
        );
    }
}
