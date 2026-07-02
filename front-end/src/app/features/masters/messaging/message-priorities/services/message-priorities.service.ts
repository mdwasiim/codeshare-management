import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { MessagePriority } from '@features/masters/messaging/message-priorities/models/message-priorities.model';

@Injectable({ providedIn: 'root' })
export class MessagePriorityService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/message-priorities';

    getAll() { return this.api.get<MessagePriority[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<MessagePriority>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: MessagePriority) {
        return this.api.post<MessagePriority>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Message Priorities created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Message Priorities'); return throwError(() => err); })
        );
    }
    update(id: string, payload: MessagePriority) {
        return this.api.put<MessagePriority>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Message Priorities updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Message Priorities'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Message Priorities deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Message Priorities'); return throwError(() => err); })
        );
    }
}
