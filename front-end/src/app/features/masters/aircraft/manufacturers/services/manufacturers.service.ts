import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { AircraftManufacturer } from '@features/masters/aircraft/manufacturers/models/manufacturers.model';

@Injectable({ providedIn: 'root' })
export class AircraftManufacturerService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/aircraft-manufacturers';

    getAll() { return this.api.get<AircraftManufacturer[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<AircraftManufacturer>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: AircraftManufacturer) {
        return this.api.post<AircraftManufacturer>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Manufacturers created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Manufacturers'); return throwError(() => err); })
        );
    }
    update(id: string, payload: AircraftManufacturer) {
        return this.api.put<AircraftManufacturer>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Manufacturers updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Manufacturers'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Manufacturers deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Manufacturers'); return throwError(() => err); })
        );
    }
}
