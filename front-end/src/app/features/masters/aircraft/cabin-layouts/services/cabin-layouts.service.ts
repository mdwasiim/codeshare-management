import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AircraftCabinLayout } from '@features/masters/aircraft/cabin-layouts/models/cabin-layouts.model';

@Injectable({ providedIn: 'root' })
export class AircraftCabinLayoutService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/aircraft-cabin-layouts';

    getAll(params?: Record<string, string>) { return this.api.get<AircraftCabinLayout[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<AircraftCabinLayout>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: AircraftCabinLayout) {
        return this.api.post<AircraftCabinLayout>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Cabin Layouts created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Cabin Layouts'); return throwError(() => err); })
        );
    }
    update(id: string, payload: AircraftCabinLayout) {
        return this.api.put<AircraftCabinLayout>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Cabin Layouts updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Cabin Layouts'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Cabin Layouts deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Cabin Layouts'); return throwError(() => err); })
        );
    }
}
