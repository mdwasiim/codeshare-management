import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';

import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';

import { Tenant } from '@features/administration/tenant-management/models/tenant.model';

@Injectable({ providedIn: 'root' })
export class TenantService {
    private readonly api = inject(AppApiService);
    private readonly toast = inject(AppToastService);

    getAll() {
        return this.api.get<Tenant[]>(API_ENDPOINTS.accessManagement.tenants.base);
    }

    getById(id: string | number) {
        return this.api.get<Tenant>(API_ENDPOINTS.accessManagement.tenants.byId, {
            pathParams: { id }
        });
    }

    create(payload: Tenant) {
        return this.api.post<Tenant>(API_ENDPOINTS.accessManagement.tenants.base, payload).pipe(
            tap(() => this.toast.success('Tenant created successfully')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to create tenant');
                return throwError(() => error);
            })
        );
    }

    update(id: string | number, payload: Tenant) {
        return this.api.put<Tenant>(API_ENDPOINTS.accessManagement.tenants.byId, payload, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Tenant updated successfully')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to update tenant');
                return throwError(() => error);
            })
        );
    }

    delete(id: string | number) {
        return this.api.delete<void>(API_ENDPOINTS.accessManagement.tenants.byId, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Tenant deleted successfully')),
            catchError((error) => {
                this.toast.error(error?.message || 'Failed to delete tenant');
                return throwError(() => error);
            })
        );
    }
}
