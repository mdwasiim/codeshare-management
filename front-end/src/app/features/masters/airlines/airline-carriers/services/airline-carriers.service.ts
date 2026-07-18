import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AirlineCarrier } from '@features/masters/airlines/airline-carriers/models/airline-carriers.model';

@Injectable({ providedIn: 'root' })
export class AirlineCarrierService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/airline-carriers';

    getAll(params?: Record<string, string>) { return this.api.get<AirlineCarrier[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<AirlineCarrier>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: AirlineCarrier) {
        return this.api.post<AirlineCarrier>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Airlines created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Airlines'); return throwError(() => err); })
        );
    }
    update(id: string, payload: AirlineCarrier) {
        return this.api.put<AirlineCarrier>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Airlines updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Airlines'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Airlines deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Airlines'); return throwError(() => err); })
        );
    }
}
