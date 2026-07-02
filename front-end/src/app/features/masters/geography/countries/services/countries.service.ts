import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { Country } from '@features/masters/geography/countries/models/countries.model';

@Injectable({ providedIn: 'root' })
export class CountryService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/countries';

    getAll() { return this.api.get<Country[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<Country>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: Country) {
        return this.api.post<Country>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Countries created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Countries'); return throwError(() => err); })
        );
    }
    update(id: string, payload: Country) {
        return this.api.put<Country>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Countries updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Countries'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Countries deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Countries'); return throwError(() => err); })
        );
    }
}
