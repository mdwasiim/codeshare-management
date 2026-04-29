import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/config/app-api.service';
import { Role } from '@features/iam/models/role.model';

@Injectable({ providedIn: 'root' })
export class RoleService {

    private api = inject(AppApiService);

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
        return this.api.post<Role>('roles.base', role);
    }

    update(id: string, role: Role) {
        return this.api.put<Role>('roles.byId', role, {
            pathParams: { id }
        });
    }

    delete(id: string) {
        return this.api.delete<void>('roles.byId', {
            pathParams: { id }
        });
    }
}
