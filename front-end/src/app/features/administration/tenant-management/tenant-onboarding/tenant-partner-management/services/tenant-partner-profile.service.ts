import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';

import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';

import { TenantPartnerProfile } from '@features/administration/tenant-management/models/tenant-partner-profile.model';

@Injectable({ providedIn: 'root' })
export class TenantPartnerProfileService {
    private readonly api = inject(AppApiService);
    private readonly toast = inject(AppToastService);

    getAll() {
        return this.api.get<TenantPartnerProfile[]>(API_ENDPOINTS.tenantService.tenantPartnerProfiles.base);
    }

    getCurrent() {
        return this.api.get<TenantPartnerProfile[]>(API_ENDPOINTS.tenantService.tenantPartnerProfiles.current);
    }

    getById(id: string | number) {
        return this.api.get<TenantPartnerProfile>(API_ENDPOINTS.tenantService.tenantPartnerProfiles.byId, {
            pathParams: { id }
        });
    }

    create(payload: TenantPartnerProfile) {
        return this.api.post<TenantPartnerProfile>(API_ENDPOINTS.tenantService.tenantPartnerProfiles.base, payload).pipe(
            tap(() => this.toast.success('Partner profile created successfully')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to create partner profile');
                return throwError(() => error);
            })
        );
    }

    update(id: string | number, payload: TenantPartnerProfile) {
        return this.api.put<TenantPartnerProfile>(API_ENDPOINTS.tenantService.tenantPartnerProfiles.byId, payload, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Partner profile updated successfully')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to update partner profile');
                return throwError(() => error);
            })
        );
    }

    delete(id: string | number) {
        return this.api.delete<void>(API_ENDPOINTS.tenantService.tenantPartnerProfiles.byId, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Partner profile deleted successfully')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to delete partner profile');
                return throwError(() => error);
            })
        );
    }
}
