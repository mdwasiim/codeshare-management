import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { Alliance } from '@features/masters/airlines/alliances/models/alliances.model';

@Injectable({ providedIn: 'root' })
export class AllianceService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/alliances';

    getAll(params?: Record<string, string>) { return this.api.get<Alliance[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<Alliance>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: Alliance) {
        return this.api.post<Alliance>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Alliances created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Alliances'); return throwError(() => err); })
        );
    }
    update(id: string, payload: Alliance) {
        return this.api.put<Alliance>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Alliances updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Alliances'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Alliances deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Alliances'); return throwError(() => err); })
        );
    }
}
