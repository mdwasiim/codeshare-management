import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';
import { TenantIngestionProfile } from '../models/tenant-ingestion-profile.model';
import { catchError, of, tap, throwError } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TenantIngestionProfileService {
    private readonly api = inject(AppApiService);
    private readonly toast = inject(AppToastService);

    getAll() {
        return this.api.get<TenantIngestionProfile[]>(API_ENDPOINTS.accessManagement.tenantIngestionProfiles.base);
    }

    getByTenantCode(tenantCode: string) {
        return this.api.get<TenantIngestionProfile>(API_ENDPOINTS.accessManagement.tenantIngestionProfiles.byTenantCode, {
            pathParams: { tenantCode }
        }).pipe(
            catchError((error) => {
                if (error.status === 404) {
                    return of(null);
                }
                return throwError(() => error);
            })
        );
    }

    create(payload: TenantIngestionProfile) {
        return this.api.post<TenantIngestionProfile>(API_ENDPOINTS.accessManagement.tenantIngestionProfiles.base, payload).pipe(
            tap(() => this.toast.success('Ingestion profile created successfully'))
        );
    }

    update(id: string, payload: TenantIngestionProfile) {
        return this.api.put<TenantIngestionProfile>(API_ENDPOINTS.accessManagement.tenantIngestionProfiles.byId, payload, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Ingestion profile updated successfully'))
        );
    }

    delete(id: string) {
        return this.api.delete<void>(API_ENDPOINTS.accessManagement.tenantIngestionProfiles.byId, {
            pathParams: { id }
        }).pipe(
            tap(() => this.toast.success('Ingestion profile deleted successfully'))
        );
    }
}
