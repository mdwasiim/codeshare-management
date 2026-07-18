import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AirlineBusinessRole } from '@features/masters/airlines/airline-business-roles/models/airline-business-roles.model';

@Injectable({ providedIn: 'root' })
export class AirlineBusinessRoleService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/airline-business-roles';

    getAll(params?: Record<string, string>) { return this.api.get<AirlineBusinessRole[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<AirlineBusinessRole>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: AirlineBusinessRole) {
        return this.api.post<AirlineBusinessRole>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Airline Business Roles created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Airline Business Roles'); return throwError(() => err); })
        );
    }
    update(id: string, payload: AirlineBusinessRole) {
        return this.api.put<AirlineBusinessRole>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Airline Business Roles updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Airline Business Roles'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Airline Business Roles deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Airline Business Roles'); return throwError(() => err); })
        );
    }
}
