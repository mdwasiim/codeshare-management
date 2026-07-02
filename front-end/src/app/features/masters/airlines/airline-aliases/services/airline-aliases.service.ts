import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AirlineAlias } from '@features/masters/airlines/airline-aliases/models/airline-aliases.model';

@Injectable({ providedIn: 'root' })
export class AirlineAliasService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/airline-aliases';

    getAll() { return this.api.get<AirlineAlias[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<AirlineAlias>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: AirlineAlias) {
        return this.api.post<AirlineAlias>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Airline Aliases created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Airline Aliases'); return throwError(() => err); })
        );
    }
    update(id: string, payload: AirlineAlias) {
        return this.api.put<AirlineAlias>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Airline Aliases updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Airline Aliases'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Airline Aliases deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Airline Aliases'); return throwError(() => err); })
        );
    }
}
