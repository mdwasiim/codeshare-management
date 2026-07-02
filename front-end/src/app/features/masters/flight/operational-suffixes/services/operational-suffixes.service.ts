import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { OperationalSuffix } from '@features/masters/flight/operational-suffixes/models/operational-suffixes.model';

@Injectable({ providedIn: 'root' })
export class OperationalSuffixService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/operational-suffixes';

    getAll() { return this.api.get<OperationalSuffix[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<OperationalSuffix>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: OperationalSuffix) {
        return this.api.post<OperationalSuffix>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Operational Suffixes created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Operational Suffixes'); return throwError(() => err); })
        );
    }
    update(id: string, payload: OperationalSuffix) {
        return this.api.put<OperationalSuffix>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Operational Suffixes updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Operational Suffixes'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Operational Suffixes deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Operational Suffixes'); return throwError(() => err); })
        );
    }
}
