import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { ActionIdentifier } from '@features/masters/messaging/action-identifiers/models/action-identifiers.model';

@Injectable({ providedIn: 'root' })
export class ActionIdentifierService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/action-identifiers';

    getAll() { return this.api.get<ActionIdentifier[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<ActionIdentifier>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: ActionIdentifier) {
        return this.api.post<ActionIdentifier>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Action Identifiers created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Action Identifiers'); return throwError(() => err); })
        );
    }
    update(id: string, payload: ActionIdentifier) {
        return this.api.put<ActionIdentifier>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Action Identifiers updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Action Identifiers'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Action Identifiers deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Action Identifiers'); return throwError(() => err); })
        );
    }
}
