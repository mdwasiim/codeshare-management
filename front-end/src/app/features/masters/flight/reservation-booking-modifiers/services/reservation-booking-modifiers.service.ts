import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { ReservationBookingModifier } from '@features/masters/flight/reservation-booking-modifiers/models/reservation-booking-modifiers.model';

@Injectable({ providedIn: 'root' })
export class ReservationBookingModifierService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/reservation-booking-modifiers';

    getAll() { return this.api.get<ReservationBookingModifier[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<ReservationBookingModifier>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: ReservationBookingModifier) {
        return this.api.post<ReservationBookingModifier>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Reservation Booking Modifiers created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Reservation Booking Modifiers'); return throwError(() => err); })
        );
    }
    update(id: string, payload: ReservationBookingModifier) {
        return this.api.put<ReservationBookingModifier>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Reservation Booking Modifiers updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Reservation Booking Modifiers'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Reservation Booking Modifiers deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Reservation Booking Modifiers'); return throwError(() => err); })
        );
    }
}
