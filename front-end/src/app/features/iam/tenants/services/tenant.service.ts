import {inject, Injectable} from '@angular/core';
import {AppApiService} from '@core/config/app-api.service';
import {AppToastService} from '@core/services/app-toast.service';
import {tap} from 'rxjs';
import {Tenant} from "@features/iam/models/tenant.model";

@Injectable({ providedIn: 'root' })
export class TenantService  {

    private api = inject(AppApiService);
    private toast = inject(AppToastService);

    getAll() {
        return this.api.get<Tenant[]>('tenants.base');
    }

    getById(id: string) {
        return this.api.get<Tenant>('tenants.byId', {
            pathParams: { id }
        });
    }

    create(tenant: Tenant) {
        return this.api.post<Tenant>('tenants.base', tenant).pipe(
            tap(() => {
                this.toast.success('Tenant created successfully');
            })
        );
    }

    update(id: string, tenant: Tenant) {
        return this.api.put<Tenant>('tenants.byId', tenant, {
            pathParams: { id }
        }).pipe(
            tap(() => {
                this.toast.success('Tenant updated successfully');
            })
        );
    }

    delete(id: string) {
        return this.api.delete<void>('tenants.byId', {
            pathParams: { id }
        }).pipe(
            tap(() => {
                this.toast.success('Tenant deleted successfully');
            })
        );
    }
}
