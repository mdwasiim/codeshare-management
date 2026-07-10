import { Injectable, inject } from '@angular/core';
import { catchError, tap, throwError } from 'rxjs';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';
import { TenantPartner } from '@features/partner-management/tenant-partners/models/tenant-partner.model';

@Injectable({ providedIn: 'root' })
export class TenantPartnerService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);

    getAll() {
        return this.api.get<TenantPartner[]>(API_ENDPOINTS.accessManagement.tenantPartners.base);
    }

    getById(id: string) {
        return this.api.get<TenantPartner>(API_ENDPOINTS.accessManagement.tenantPartners.byId, {
            pathParams: { id }
        });
    }

    create(payload: TenantPartner) {
        return this.api.post<TenantPartner>(API_ENDPOINTS.accessManagement.tenantPartners.base, payload).pipe(
            tap(() => this.toast.success('Tenant partner created successfully')),
            catchError((err) => {
                this.toast.error(err.message || 'Failed to create tenant partner');
                return throwError(() => err);
            })
        );
    }

    update(id: string, payload: TenantPartner) {
        return this.api.put<TenantPartner>(API_ENDPOINTS.accessManagement.tenantPartners.byId, payload, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Tenant partner updated successfully')),
            catchError((err) => {
                this.toast.error(err.message || 'Failed to update tenant partner');
                return throwError(() => err);
            })
        );
    }

    delete(id: string) {
        return this.api.delete<void>(API_ENDPOINTS.accessManagement.tenantPartners.byId, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Tenant partner deleted successfully')),
            catchError((err) => {
                this.toast.error(err.message || 'Failed to delete tenant partner');
                return throwError(() => err);
            })
        );
    }
}
