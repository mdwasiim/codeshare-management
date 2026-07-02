import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { MessageStatus } from '@features/masters/messaging/message-statuses/models/message-statuses.model';

@Injectable({ providedIn: 'root' })
export class MessageStatusService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/message-statuses';

    getAll() { return this.api.get<MessageStatus[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<MessageStatus>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: MessageStatus) {
        return this.api.post<MessageStatus>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Message Statuses created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Message Statuses'); return throwError(() => err); })
        );
    }
    update(id: string, payload: MessageStatus) {
        return this.api.put<MessageStatus>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Message Statuses updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Message Statuses'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Message Statuses deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Message Statuses'); return throwError(() => err); })
        );
    }
}
