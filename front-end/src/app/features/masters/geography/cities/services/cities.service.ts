import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { City } from '@features/masters/geography/cities/models/cities.model';

@Injectable({ providedIn: 'root' })
export class CityService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/cities';

    getAll() { return this.api.get<City[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<City>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: City) {
        return this.api.post<City>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Cities created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Cities'); return throwError(() => err); })
        );
    }
    update(id: string, payload: City) {
        return this.api.put<City>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Cities updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Cities'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Cities deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Cities'); return throwError(() => err); })
        );
    }
}
