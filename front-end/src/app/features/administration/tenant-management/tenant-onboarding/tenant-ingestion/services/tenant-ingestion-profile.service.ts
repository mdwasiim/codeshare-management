import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';

import { TenantIngestionProfile } from '@features/administration/tenant-management/models/tenant-ingestion-profile.model';

@Injectable({ providedIn: 'root' })
export class TenantIngestionProfileService {
    private readonly api = inject(AppApiService);

    getAll() {
        return this.api.get<TenantIngestionProfile[]>(API_ENDPOINTS.tenantService.tenantIngestionProfiles.base);
    }

    getCurrent() {
        return this.api.get<TenantIngestionProfile>(API_ENDPOINTS.tenantService.tenantIngestionProfiles.current);
    }

    saveCurrent(payload: TenantIngestionProfile) {
        return this.api.put<TenantIngestionProfile>(API_ENDPOINTS.tenantService.tenantIngestionProfiles.current, payload);
    }

    create(payload: TenantIngestionProfile) {
        return this.api.post<TenantIngestionProfile>(API_ENDPOINTS.tenantService.tenantIngestionProfiles.base, payload);
    }

    update(id: string | number, payload: TenantIngestionProfile) {
        return this.api.put<TenantIngestionProfile>(API_ENDPOINTS.tenantService.tenantIngestionProfiles.byId, payload, {
            pathParams: { id }
        });
    }

    getByTenantCode(tenantCode: string) {
        return this.api.get<TenantIngestionProfile>(API_ENDPOINTS.tenantService.tenantIngestionProfiles.byTenantCode, {
            pathParams: { tenantCode }
        });
    }

    enable(id: string | number, enabled: boolean) {
        return this.api.patch<void>(API_ENDPOINTS.tenantService.tenantIngestionProfiles.byId, null, {
            pathParams: { id },
            params: { enabled }
        });
    }

    delete(id: string | number) {
        return this.api.delete<void>(API_ENDPOINTS.tenantService.tenantIngestionProfiles.byId, {
            pathParams: { id }
        });
    }
}
