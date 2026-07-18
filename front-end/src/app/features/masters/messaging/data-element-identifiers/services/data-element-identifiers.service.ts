import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { DataElementIdentifier } from '@features/masters/messaging/data-element-identifiers/models/data-element-identifiers.model';

@Injectable({ providedIn: 'root' })
export class DataElementIdentifierService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/data-element-identifiers';

    getAll(params?: Record<string, string>) { return this.api.get<DataElementIdentifier[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<DataElementIdentifier>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: DataElementIdentifier) {
        return this.api.post<DataElementIdentifier>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Data Element Identifiers created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Data Element Identifiers'); return throwError(() => err); })
        );
    }
    update(id: string, payload: DataElementIdentifier) {
        return this.api.put<DataElementIdentifier>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Data Element Identifiers updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Data Element Identifiers'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Data Element Identifiers deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Data Element Identifiers'); return throwError(() => err); })
        );
    }
}
