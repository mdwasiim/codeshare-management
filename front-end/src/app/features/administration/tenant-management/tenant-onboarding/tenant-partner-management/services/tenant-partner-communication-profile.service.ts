import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';

import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';

import { TenantPartnerCommunicationProfile } from '@features/administration/tenant-management/models/tenant-partner-profile.model';

@Injectable({ providedIn: 'root' })
export class TenantPartnerCommunicationProfileService {
    private readonly api = inject(AppApiService);
    private readonly toast = inject(AppToastService);

    getAll() {
        return this.api.get<TenantPartnerCommunicationProfile[]>(API_ENDPOINTS.tenantService.tenantPartnerCommunicationProfiles.base);
    }

    getCurrent() {
        return this.api.get<TenantPartnerCommunicationProfile[]>(API_ENDPOINTS.tenantService.tenantPartnerCommunicationProfiles.current);
    }

    getById(id: string | number) {
        return this.api.get<TenantPartnerCommunicationProfile>(API_ENDPOINTS.tenantService.tenantPartnerCommunicationProfiles.byId, {
            pathParams: { id }
        });
    }

    create(payload: TenantPartnerCommunicationProfile) {
        return this.api.post<TenantPartnerCommunicationProfile>(API_ENDPOINTS.tenantService.tenantPartnerCommunicationProfiles.base, payload).pipe(
            tap(() => this.toast.success('Communication profile created successfully')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to create communication profile');
                return throwError(() => error);
            })
        );
    }

    update(id: string | number, payload: TenantPartnerCommunicationProfile) {
        return this.api.put<TenantPartnerCommunicationProfile>(API_ENDPOINTS.tenantService.tenantPartnerCommunicationProfiles.byId, payload, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Communication profile updated successfully')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to update communication profile');
                return throwError(() => error);
            })
        );
    }

    delete(id: string | number) {
        return this.api.delete<void>(API_ENDPOINTS.tenantService.tenantPartnerCommunicationProfiles.byId, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Communication profile deleted successfully')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to delete communication profile');
                return throwError(() => error);
            })
        );
    }
}
