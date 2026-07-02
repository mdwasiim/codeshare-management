import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AircraftOwner } from '@features/masters/aircraft/owners/models/owners.model';

@Injectable({ providedIn: 'root' })
export class AircraftOwnerService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/aircraft-owners';

    getAll() { return this.api.get<AircraftOwner[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<AircraftOwner>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: AircraftOwner) {
        return this.api.post<AircraftOwner>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Owners created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Owners'); return throwError(() => err); })
        );
    }
    update(id: string, payload: AircraftOwner) {
        return this.api.put<AircraftOwner>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Owners updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Owners'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Owners deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Owners'); return throwError(() => err); })
        );
    }
}
