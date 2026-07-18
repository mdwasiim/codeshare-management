import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { ReservationBookingDesignator } from '@features/masters/flight/reservation-booking-designators/models/reservation-booking-designators.model';

@Injectable({ providedIn: 'root' })
export class ReservationBookingDesignatorService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/reservation-booking-designators';

    getAll(params?: Record<string, string>) { return this.api.get<ReservationBookingDesignator[]>(this.baseUrl, { params }); }
    getById(id: string) { return this.api.get<ReservationBookingDesignator>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: ReservationBookingDesignator) {
        return this.api.post<ReservationBookingDesignator>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Reservation Booking Designators created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Reservation Booking Designators'); return throwError(() => err); })
        );
    }
    update(id: string, payload: ReservationBookingDesignator) {
        return this.api.put<ReservationBookingDesignator>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Reservation Booking Designators updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Reservation Booking Designators'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Reservation Booking Designators deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Reservation Booking Designators'); return throwError(() => err); })
        );
    }
}
