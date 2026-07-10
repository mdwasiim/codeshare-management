import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';
import { TenantPartnerProfile } from '../models/tenant-partner-profile.model';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TenantPartnerProfileService {
    private readonly api = inject(AppApiService);
    private readonly toast = inject(AppToastService);

    getAll() {
        return this.api.get<TenantPartnerProfile[]>(API_ENDPOINTS.accessManagement.tenantPartnerProfiles.base);
    }

    create(payload: TenantPartnerProfile) {
        return this.api.post<TenantPartnerProfile>(API_ENDPOINTS.accessManagement.tenantPartnerProfiles.base, payload).pipe(
            tap(() => this.toast.success('Partner profile created successfully'))
        );
    }

    update(id: string, payload: TenantPartnerProfile) {
        return this.api.put<TenantPartnerProfile>(API_ENDPOINTS.accessManagement.tenantPartnerProfiles.byId, payload, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Partner profile updated successfully'))
        );
    }

    delete(id: string) {
        return this.api.delete<void>(API_ENDPOINTS.accessManagement.tenantPartnerProfiles.byId, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Partner profile deleted successfully'))
        );
    }
}
