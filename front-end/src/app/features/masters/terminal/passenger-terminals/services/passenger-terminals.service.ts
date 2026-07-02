import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { PassengerTerminal } from '@features/masters/terminal/passenger-terminals/models/passenger-terminals.model';

@Injectable({ providedIn: 'root' })
export class PassengerTerminalService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/airport-terminals';

    getAll() { return this.api.get<PassengerTerminal[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<PassengerTerminal>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: PassengerTerminal) {
        return this.api.post<PassengerTerminal>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Passenger Terminals created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Passenger Terminals'); return throwError(() => err); })
        );
    }
    update(id: string, payload: PassengerTerminal) {
        return this.api.put<PassengerTerminal>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Passenger Terminals updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Passenger Terminals'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Passenger Terminals deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Passenger Terminals'); return throwError(() => err); })
        );
    }
}
