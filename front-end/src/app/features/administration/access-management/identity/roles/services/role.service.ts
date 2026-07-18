import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';
import { Role } from '@features/administration/access-management/models/role.model';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RoleService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);

    getAll(params?: Record<string, string>) {
        return this.api.get<Role[]>(API_ENDPOINTS.identityService.roles.base, { params });
    }

    getById(id: string) {
        return this.api.get<Role>(API_ENDPOINTS.identityService.roles.byId, {
            pathParams: { id }
        });
    }

    create(role: Role) {
        return this.api.post<Role>(API_ENDPOINTS.identityService.roles.base, role).pipe(
            tap(() => {
                this.toast.success('Role created successfully');
            })
        );
    }

    update(id: string, role: Role) {
        return this.api
            .put<Role>(API_ENDPOINTS.identityService.roles.byId, role, {
                pathParams: { id }
            })
            .pipe(
                tap(() => {
                    this.toast.success('Role updated successfully');
                })
            );
    }

    delete(id: string) {
        return this.api
            .delete<void>(API_ENDPOINTS.identityService.roles.byId, {
                pathParams: { id }
            })
            .pipe(
                tap(() => {
                    this.toast.success('Role deleted successfully');
                })
            );
    }
}

