import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AircraftRegistration } from '@features/masters/aircraft/registrations/models/registrations.model';

@Injectable({ providedIn: 'root' })
export class AircraftRegistrationService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/aircraft-registrations';

    getAll(params?: Record<string, string>) { return this.api.get<AircraftRegistration[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<AircraftRegistration>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: AircraftRegistration) {
        return this.api.post<AircraftRegistration>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Registrations created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Registrations'); return throwError(() => err); })
        );
    }
    update(id: string, payload: AircraftRegistration) {
        return this.api.put<AircraftRegistration>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Registrations updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Registrations'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Registrations deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Registrations'); return throwError(() => err); })
        );
    }
}
