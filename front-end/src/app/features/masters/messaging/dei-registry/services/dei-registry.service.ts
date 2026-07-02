import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { DeiRegistry } from '@features/masters/messaging/dei-registry/models/dei-registry.model';

@Injectable({ providedIn: 'root' })
export class DeiRegistryService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/dei';

    getAll() { return this.api.get<DeiRegistry[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<DeiRegistry>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: DeiRegistry) {
        return this.api.post<DeiRegistry>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('DEI Registry created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create DEI Registry'); return throwError(() => err); })
        );
    }
    update(id: string, payload: DeiRegistry) {
        return this.api.put<DeiRegistry>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('DEI Registry updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update DEI Registry'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('DEI Registry deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete DEI Registry'); return throwError(() => err); })
        );
    }
}
