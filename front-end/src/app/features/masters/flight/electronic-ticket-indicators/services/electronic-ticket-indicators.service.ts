import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { ElectronicTicketIndicator } from '@features/masters/flight/electronic-ticket-indicators/models/electronic-ticket-indicators.model';

@Injectable({ providedIn: 'root' })
export class ElectronicTicketIndicatorService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/electronic-ticket-indicators';

    getAll() { return this.api.get<ElectronicTicketIndicator[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<ElectronicTicketIndicator>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: ElectronicTicketIndicator) {
        return this.api.post<ElectronicTicketIndicator>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Electronic Ticket Indicators created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Electronic Ticket Indicators'); return throwError(() => err); })
        );
    }
    update(id: string, payload: ElectronicTicketIndicator) {
        return this.api.put<ElectronicTicketIndicator>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Electronic Ticket Indicators updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Electronic Ticket Indicators'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Electronic Ticket Indicators deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Electronic Ticket Indicators'); return throwError(() => err); })
        );
    }
}
