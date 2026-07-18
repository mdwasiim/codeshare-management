import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AircraftFamily } from '@features/masters/aircraft/aircraft-families/models/aircraft-families.model';

@Injectable({ providedIn: 'root' })
export class AircraftFamilyService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/aircraft-families';

    getAll(params?: Record<string, string>) { return this.api.get<AircraftFamily[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<AircraftFamily>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: AircraftFamily) {
        return this.api.post<AircraftFamily>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Aircraft Families created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Aircraft Families'); return throwError(() => err); })
        );
    }
    update(id: string, payload: AircraftFamily) {
        return this.api.put<AircraftFamily>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Aircraft Families updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Aircraft Families'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Aircraft Families deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Aircraft Families'); return throwError(() => err); })
        );
    }
}
