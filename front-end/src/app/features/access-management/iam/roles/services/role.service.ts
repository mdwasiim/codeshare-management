import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/config/app-api.service';
import { API_ENDPOINTS } from '@core/config/app-api.config';
import { AppToastService } from '@services/app-toast.service';
import { Role } from '@features/access-management/iam/models/role.model';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RoleService {

    private api = inject(AppApiService);
    private toast = inject(AppToastService);

    getAll() {
        return this.api.get<Role[]>(API_ENDPOINTS.accessManagement.roles.base);
    }

    getById(id: string) {
        return this.api.get<Role>(API_ENDPOINTS.accessManagement.roles.byId, {
            pathParams: { id }
        });
    }

    create(role: Role) {
        return this.api.post<Role>(API_ENDPOINTS.accessManagement.roles.base, role).pipe(
            tap(() => {
                this.toast.success('Role created successfully');
            })
        );
    }

    update(id: string, role: Role) {
        return this.api.put<Role>(API_ENDPOINTS.accessManagement.roles.byId, role, {
            pathParams: { id }
        }).pipe(
            tap(() => {
                this.toast.success('Role updated successfully');
            })
        );
    }

    delete(id: string) {
        return this.api.delete<void>(API_ENDPOINTS.accessManagement.roles.byId, {
            pathParams: { id }
        }).pipe(
            tap(() => {
                this.toast.success('Role deleted successfully');
            })
        );
    }
}
