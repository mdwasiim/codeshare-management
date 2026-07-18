import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AircraftType } from '@features/masters/aircraft/aircraft-types/models/aircraft-types.model';

@Injectable({ providedIn: 'root' })
export class AircraftTypeService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/aircraft-types';

    getAll(params?: Record<string, string>) { return this.api.get<AircraftType[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<AircraftType>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: AircraftType) {
        return this.api.post<AircraftType>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Aircraft Types created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Aircraft Types'); return throwError(() => err); })
        );
    }
    update(id: string, payload: AircraftType) {
        return this.api.put<AircraftType>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Aircraft Types updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Aircraft Types'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Aircraft Types deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Aircraft Types'); return throwError(() => err); })
        );
    }
}
