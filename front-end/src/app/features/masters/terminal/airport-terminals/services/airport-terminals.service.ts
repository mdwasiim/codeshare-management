import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AirportTerminal } from '@features/masters/terminal/airport-terminals/models/airport-terminals.model';

@Injectable({ providedIn: 'root' })
export class AirportTerminalService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/airport-terminals';

    getAll(params?: Record<string, string>) { return this.api.get<AirportTerminal[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<AirportTerminal>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: AirportTerminal) {
        return this.api.post<AirportTerminal>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Airport Terminals created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Airport Terminals'); return throwError(() => err); })
        );
    }
    update(id: string, payload: AirportTerminal) {
        return this.api.put<AirportTerminal>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Airport Terminals updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Airport Terminals'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Airport Terminals deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Airport Terminals'); return throwError(() => err); })
        );
    }
}
