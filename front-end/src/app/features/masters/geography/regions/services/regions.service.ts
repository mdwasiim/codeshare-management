import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { Region } from '@features/masters/geography/regions/models/regions.model';

@Injectable({ providedIn: 'root' })
export class RegionService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/regions';

    getAll(params?: Record<string, string>) { return this.api.get<Region[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<Region>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: Region) {
        return this.api.post<Region>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Regions created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Regions'); return throwError(() => err); })
        );
    }
    update(id: string, payload: Region) {
        return this.api.put<Region>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Regions updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Regions'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Regions deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Regions'); return throwError(() => err); })
        );
    }
}
