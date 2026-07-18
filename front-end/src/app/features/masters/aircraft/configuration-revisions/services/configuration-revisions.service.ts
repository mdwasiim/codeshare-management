import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AircraftConfigurationRevision } from '@features/masters/aircraft/configuration-revisions/models/configuration-revisions.model';

@Injectable({ providedIn: 'root' })
export class AircraftConfigurationRevisionService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/aircraft-configuration-revisions';

    getAll(params?: Record<string, string>) { return this.api.get<AircraftConfigurationRevision[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<AircraftConfigurationRevision>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: AircraftConfigurationRevision) {
        return this.api.post<AircraftConfigurationRevision>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Configuration Revisions created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Configuration Revisions'); return throwError(() => err); })
        );
    }
    update(id: string, payload: AircraftConfigurationRevision) {
        return this.api.put<AircraftConfigurationRevision>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Configuration Revisions updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Configuration Revisions'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Configuration Revisions deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Configuration Revisions'); return throwError(() => err); })
        );
    }
}
