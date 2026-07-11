import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';

import { TenantPartner } from '@features/administration/tenant-management/models/tenant-partner.model';

@Injectable({ providedIn: 'root' })
export class TenantPartnerService {
    private readonly api = inject(AppApiService);

    getAll() {
        return this.api.get<TenantPartner[]>(API_ENDPOINTS.accessManagement.tenantPartners.base);
    }
}
