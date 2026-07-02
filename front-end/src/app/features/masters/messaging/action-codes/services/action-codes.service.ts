import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { ActionCode } from '@features/masters/messaging/action-codes/models/action-codes.model';

@Injectable({ providedIn: 'root' })
export class ActionCodeService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/action-codes';

    getAll() { return this.api.get<ActionCode[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<ActionCode>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: ActionCode) {
        return this.api.post<ActionCode>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Action Codes created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Action Codes'); return throwError(() => err); })
        );
    }
    update(id: string, payload: ActionCode) {
        return this.api.put<ActionCode>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Action Codes updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Action Codes'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Action Codes deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Action Codes'); return throwError(() => err); })
        );
    }
}
