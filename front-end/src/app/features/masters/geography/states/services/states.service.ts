import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { State } from '@features/masters/geography/states/models/states.model';

@Injectable({ providedIn: 'root' })
export class StateService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/states';

    getAll(params?: Record<string, string>) { return this.api.get<State[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<State>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: State) {
        return this.api.post<State>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('States created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create States'); return throwError(() => err); })
        );
    }
    update(id: string, payload: State) {
        return this.api.put<State>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('States updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update States'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('States deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete States'); return throwError(() => err); })
        );
    }
}
