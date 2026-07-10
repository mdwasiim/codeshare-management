import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';
import { TenantPartnerDistributionProfile } from '../models/tenant-partner-distribution-profile.model';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TenantPartnerDistributionProfileService {
    private readonly api = inject(AppApiService);
    private readonly toast = inject(AppToastService);

    getAll() {
        return this.api.get<TenantPartnerDistributionProfile[]>(API_ENDPOINTS.accessManagement.tenantPartnerDistributionProfiles.base);
    }

    create(payload: TenantPartnerDistributionProfile) {
        return this.api.post<TenantPartnerDistributionProfile>(API_ENDPOINTS.accessManagement.tenantPartnerDistributionProfiles.base, payload).pipe(
            tap(() => this.toast.success('Distribution profile created successfully'))
        );
    }

    update(id: string, payload: TenantPartnerDistributionProfile) {
        return this.api.put<TenantPartnerDistributionProfile>(API_ENDPOINTS.accessManagement.tenantPartnerDistributionProfiles.byId, payload, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Distribution profile updated successfully'))
        );
    }

    delete(id: string) {
        return this.api.delete<void>(API_ENDPOINTS.accessManagement.tenantPartnerDistributionProfiles.byId, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Distribution profile deleted successfully'))
        );
    }
}
