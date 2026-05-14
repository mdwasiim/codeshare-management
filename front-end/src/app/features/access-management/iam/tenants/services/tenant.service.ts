import { inject, Injectable } from '@angular/core';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';
import { tap } from 'rxjs';
import { Tenant } from '@features/access-management/iam/models/tenant.model';

@Injectable({ providedIn: 'root' })
export class TenantService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);

    getAll() {
        return this.api.get<Tenant[]>(API_ENDPOINTS.accessManagement.tenants.base);
    }

    getById(id: string) {
        return this.api.get<Tenant>(API_ENDPOINTS.accessManagement.tenants.byId, {
            pathParams: { id }
        });
    }

    create(tenant: Tenant) {
        return this.api.post<Tenant>(API_ENDPOINTS.accessManagement.tenants.base, tenant).pipe(
            tap(() => {
                this.toast.success('Tenant created successfully');
            })
        );
    }

    update(id: string, tenant: Tenant) {
        return this.api
            .put<Tenant>(API_ENDPOINTS.accessManagement.tenants.byId, tenant, {
                pathParams: { id }
            })
            .pipe(
                tap(() => {
                    this.toast.success('Tenant updated successfully');
                })
            );
    }

    delete(id: string) {
        return this.api
            .delete<void>(API_ENDPOINTS.accessManagement.tenants.byId, {
                pathParams: { id }
            })
            .pipe(
                tap(() => {
                    this.toast.success('Tenant deleted successfully');
                })
            );
    }
}
