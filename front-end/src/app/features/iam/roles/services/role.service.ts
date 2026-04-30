import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/config/app-api.service';
import { AppToastService } from '@core/services/app-toast.service';
import { Role } from '@features/iam/models/role.model';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RoleService {

    private api = inject(AppApiService);
    private toast = inject(AppToastService);

    getAll(tenantId: string) {
        return this.api.get<Role[]>('roles.base', {
            params: { tenantId }
        });
    }

    getById(id: string) {
        return this.api.get<Role>('roles.byId', {
            pathParams: { id }
        });
    }

    create(role: Role) {
        return this.api.post<Role>('roles.base', role).pipe(
            tap(() => {
                this.toast.success('Role created successfully');
            })
        );
    }

    update(id: string, role: Role) {
        return this.api.put<Role>('roles.byId', role, {
            pathParams: { id }
        }).pipe(
            tap(() => {
                this.toast.success('Role updated successfully');
            })
        );
    }

    delete(id: string) {
        return this.api.delete<void>('roles.byId', {
            pathParams: { id }
        }).pipe(
            tap(() => {
                this.toast.success('Role deleted successfully');
            })
        );
    }
}
