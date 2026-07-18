import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AircraftConfiguration } from '@features/masters/aircraft/configurations/models/configurations.model';

@Injectable({ providedIn: 'root' })
export class AircraftConfigurationService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/aircraft-configurations';

    getAll(params?: Record<string, string>) { return this.api.get<AircraftConfiguration[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<AircraftConfiguration>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: AircraftConfiguration) {
        return this.api.post<AircraftConfiguration>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Configurations created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Configurations'); return throwError(() => err); })
        );
    }
    update(id: string, payload: AircraftConfiguration) {
        return this.api.put<AircraftConfiguration>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Configurations updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Configurations'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Configurations deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Configurations'); return throwError(() => err); })
        );
    }
}
