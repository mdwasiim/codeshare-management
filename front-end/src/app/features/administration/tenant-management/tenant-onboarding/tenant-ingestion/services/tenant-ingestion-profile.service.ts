import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';

import { TenantIngestionProfile } from '@features/administration/tenant-management/models/tenant-ingestion-profile.model';

@Injectable({ providedIn: 'root' })
export class TenantIngestionProfileService {
    private readonly api = inject(AppApiService);

    getAll() {
        return this.api.get<TenantIngestionProfile[]>(API_ENDPOINTS.accessManagement.tenantIngestionProfiles.base);
    }

    getByTenantCode(tenantCode: string) {
        return this.api.get<TenantIngestionProfile>(API_ENDPOINTS.accessManagement.tenantIngestionProfiles.byTenantCode, {
            pathParams: { tenantCode }
        });
    }

    enable(id: string, enabled: boolean) {
        return this.api.patch<void>(API_ENDPOINTS.accessManagement.tenantIngestionProfiles.byId, null, {
            pathParams: { id },
            params: { enabled }
        });
    }

    delete(id: string) {
        return this.api.delete<void>(API_ENDPOINTS.accessManagement.tenantIngestionProfiles.byId, {
            pathParams: { id }
        });
    }
}
