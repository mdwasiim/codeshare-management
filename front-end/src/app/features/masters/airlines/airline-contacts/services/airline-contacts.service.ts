import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AirlineContact } from '@features/masters/airlines/airline-contacts/models/airline-contacts.model';

@Injectable({ providedIn: 'root' })
export class AirlineContactService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/airline-contacts';

    getAll() { return this.api.get<AirlineContact[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<AirlineContact>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: AirlineContact) {
        return this.api.post<AirlineContact>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Airline Contacts created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Airline Contacts'); return throwError(() => err); })
        );
    }
    update(id: string, payload: AirlineContact) {
        return this.api.put<AirlineContact>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Airline Contacts updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Airline Contacts'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Airline Contacts deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Airline Contacts'); return throwError(() => err); })
        );
    }
}
