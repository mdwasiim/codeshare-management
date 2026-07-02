import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { MealService } from '@features/masters/flight/meal-services/models/meal-services.model';

@Injectable({ providedIn: 'root' })
export class MealServiceService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/meal-services';

    getAll() { return this.api.get<MealService[]>(this.baseUrl); }
    getById(id: string) { return this.api.get<MealService>(this.baseUrl + '/' + encodeURIComponent(id)); }
    create(payload: MealService) {
        return this.api.post<MealService>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Meal Services created successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to create Meal Services'); return throwError(() => err); })
        );
    }
    update(id: string, payload: MealService) {
        return this.api.put<MealService>(this.baseUrl + '/' + encodeURIComponent(id), payload).pipe(
            tap(() => this.toast.success('Meal Services updated successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to update Meal Services'); return throwError(() => err); })
        );
    }
    delete(id: string) {
        return this.api.delete<void>(this.baseUrl + '/' + encodeURIComponent(id)).pipe(
            tap(() => this.toast.success('Meal Services deleted successfully')),
            catchError((err) => { this.toast.error(err.message || 'Failed to delete Meal Services'); return throwError(() => err); })
        );
    }
}
