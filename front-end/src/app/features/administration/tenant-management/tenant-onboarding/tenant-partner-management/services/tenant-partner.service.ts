import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';

import { TenantPartner } from '@features/administration/tenant-management/models/tenant-partner.model';

@Injectable({ providedIn: 'root' })
export class TenantPartnerService {
    private readonly api = inject(AppApiService);
    private readonly toast = inject(AppToastService);

    getAll(params?: Record<string, string>) {
        return this.api.get<TenantPartner[]>(API_ENDPOINTS.tenantService.tenantPartners.base, { params });
    }

    getCurrent() {
        return this.api.get<TenantPartner[]>(API_ENDPOINTS.tenantService.tenantPartners.current);
    }

    createCurrent(payload: TenantPartner) {
        return this.api.post<TenantPartner>(API_ENDPOINTS.tenantService.tenantPartners.current, payload).pipe(
            tap(() => this.toast.success('Codeshare partner added')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to add codeshare partner');
                return throwError(() => error);
            })
        );
    }

    create(payload: TenantPartner) {
        return this.api.post<TenantPartner>(API_ENDPOINTS.tenantService.tenantPartners.base, payload).pipe(
            tap(() => this.toast.success('Codeshare partner added')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to add codeshare partner');
                return throwError(() => error);
            })
        );
    }

    update(id: string | number, payload: TenantPartner) {
        return this.api.put<TenantPartner>(API_ENDPOINTS.tenantService.tenantPartners.byId, payload, { pathParams: { id } }).pipe(
            tap(() => this.toast.success('Codeshare partner updated')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to update codeshare partner');
                return throwError(() => error);
            })
        );
    }

    delete(id: string | number) {
        return this.api.delete<void>(API_ENDPOINTS.tenantService.tenantPartners.byId, { pathParams: { id } }).pipe(
            tap(() => this.toast.success('Codeshare partner removed')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to remove codeshare partner');
                return throwError(() => error);
            })
        );
    }

    updateCurrent(id: string | number, payload: TenantPartner) {
        return this.api.put<TenantPartner>(API_ENDPOINTS.tenantService.tenantPartners.currentById, payload, { pathParams: { id } }).pipe(
            tap(() => this.toast.success('Codeshare partner updated')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to update codeshare partner');
                return throwError(() => error);
            })
        );
    }

    deleteCurrent(id: string | number) {
        return this.api.delete<void>(API_ENDPOINTS.tenantService.tenantPartners.currentById, { pathParams: { id } }).pipe(
            tap(() => this.toast.success('Codeshare partner removed')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to remove codeshare partner');
                return throwError(() => error);
            })
        );
    }
}
