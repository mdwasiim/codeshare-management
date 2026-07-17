import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';

import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';

import { TenantPartnerDistributionProfile } from '@features/administration/tenant-management/models/tenant-partner-profile.model';

@Injectable({ providedIn: 'root' })
export class TenantPartnerDistributionProfileService {
    private readonly api = inject(AppApiService);
    private readonly toast = inject(AppToastService);

    getAll() {
        return this.api.get<TenantPartnerDistributionProfile[]>(API_ENDPOINTS.accessManagement.tenantPartnerDistributionProfiles.base);
    }

    getById(id: string | number) {
        return this.api.get<TenantPartnerDistributionProfile>(API_ENDPOINTS.accessManagement.tenantPartnerDistributionProfiles.byId, {
            pathParams: { id }
        });
    }

    create(payload: TenantPartnerDistributionProfile) {
        return this.api.post<TenantPartnerDistributionProfile>(API_ENDPOINTS.accessManagement.tenantPartnerDistributionProfiles.base, payload).pipe(
            tap(() => this.toast.success('Distribution profile created successfully')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to create distribution profile');
                return throwError(() => error);
            })
        );
    }

    update(id: string | number, payload: TenantPartnerDistributionProfile) {
        return this.api.put<TenantPartnerDistributionProfile>(API_ENDPOINTS.accessManagement.tenantPartnerDistributionProfiles.byId, payload, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Distribution profile updated successfully')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to update distribution profile');
                return throwError(() => error);
            })
        );
    }

    delete(id: string | number) {
        return this.api.delete<void>(API_ENDPOINTS.accessManagement.tenantPartnerDistributionProfiles.byId, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Distribution profile deleted successfully')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to delete distribution profile');
                return throwError(() => error);
            })
        );
    }
}
