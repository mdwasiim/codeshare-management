import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { StandardMessageIdentifier } from '@features/masters/messaging/standard-message-identifiers/models/standard-message-identifiers.model';

@Injectable({ providedIn: 'root' })
export class StandardMessageIdentifierService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/standard-message-identifiers';

    getAll(params?: Record<string, string>) { return this.api.get<StandardMessageIdentifier[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<StandardMessageIdentifier>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: StandardMessageIdentifier) {
        return this.api.post<StandardMessageIdentifier>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Standard Message Identifiers created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Standard Message Identifiers'); return throwError(() => err); })
        );
    }
    update(id: string, payload: StandardMessageIdentifier) {
        return this.api.put<StandardMessageIdentifier>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Standard Message Identifiers updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Standard Message Identifiers'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Standard Message Identifiers deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Standard Message Identifiers'); return throwError(() => err); })
        );
    }
}
