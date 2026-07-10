import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';
import { TenantPartnerCommunicationProfile } from '../models/tenant-partner-communication-profile.model';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TenantPartnerCommunicationProfileService {
    private readonly api = inject(AppApiService);
    private readonly toast = inject(AppToastService);

    getAll() {
        return this.api.get<TenantPartnerCommunicationProfile[]>(API_ENDPOINTS.accessManagement.tenantPartnerCommunicationProfiles.base);
    }

    create(payload: TenantPartnerCommunicationProfile) {
        return this.api.post<TenantPartnerCommunicationProfile>(API_ENDPOINTS.accessManagement.tenantPartnerCommunicationProfiles.base, payload).pipe(
            tap(() => this.toast.success('Communication profile created successfully'))
        );
    }

    update(id: string, payload: TenantPartnerCommunicationProfile) {
        return this.api.put<TenantPartnerCommunicationProfile>(API_ENDPOINTS.accessManagement.tenantPartnerCommunicationProfiles.byId, payload, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Communication profile updated successfully'))
        );
    }

    delete(id: string) {
        return this.api.delete<void>(API_ENDPOINTS.accessManagement.tenantPartnerCommunicationProfiles.byId, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Communication profile deleted successfully'))
        );
    }
}
