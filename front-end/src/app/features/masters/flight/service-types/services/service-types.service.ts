import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { FlightServiceType } from '@features/masters/flight/service-types/models/service-types.model';

@Injectable({ providedIn: 'root' })
export class FlightServiceTypeService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/flight-commercial-service-types';

    getAll(params?: Record<string, string>) { return this.api.get<FlightServiceType[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<FlightServiceType>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: FlightServiceType) {
        return this.api.post<FlightServiceType>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Service Types created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Service Types'); return throwError(() => err); })
        );
    }
    update(id: string, payload: FlightServiceType) {
        return this.api.put<FlightServiceType>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Service Types updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Service Types'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Service Types deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Service Types'); return throwError(() => err); })
        );
    }
}
