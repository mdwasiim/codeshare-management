import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';

import { AppApiService } from '@core/api/config/app-api.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { CommonReferenceOption } from '@features/masters/common/reference-options/models/reference-options.model';

@Injectable({ providedIn: 'root' })
export class CommonReferenceOptionService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);
    private readonly baseUrl = '/master/common/reference-options';

    getAll(params?: Record<string, string>) {
        return this.api.get<CommonReferenceOption[]>(this.baseUrl, { params });
    }

    getCategories() {
        return this.api.get<string[]>(`${this.baseUrl}/categories`);
    }

    getById(id: string | number) {
        return this.api.get<CommonReferenceOption>(`${this.baseUrl}/${encodeURIComponent(id)}`);
    }

    create(payload: CommonReferenceOption) {
        return this.api.post<CommonReferenceOption>(this.baseUrl, payload).pipe(
            tap(() => this.toast.success('Common reference option created successfully')),
            catchError((err) => {
                this.toast.error(err.message || 'Failed to create common reference option');
                return throwError(() => err);
            })
        );
    }

    update(id: string | number, payload: CommonReferenceOption) {
        return this.api.put<CommonReferenceOption>(`${this.baseUrl}/${encodeURIComponent(id)}`, payload).pipe(
            tap(() => this.toast.success('Common reference option updated successfully')),
            catchError((err) => {
                this.toast.error(err.message || 'Failed to update common reference option');
                return throwError(() => err);
            })
        );
    }

    delete(id: string | number) {
        return this.api.delete<void>(`${this.baseUrl}/${encodeURIComponent(id)}`).pipe(
            tap(() => this.toast.success('Common reference option deleted successfully')),
            catchError((err) => {
                this.toast.error(err.message || 'Failed to delete common reference option');
                return throwError(() => err);
            })
        );
    }
}
