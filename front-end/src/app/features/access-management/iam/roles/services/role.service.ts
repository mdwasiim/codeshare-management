import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/config/app-api.service';
import { AppToastService } from '@services/app-toast.service';
import { Role } from '@features/access-management/iam/models/role.model';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RoleService {

    private api = inject(AppApiService);
    private toast = inject(AppToastService);

    getAll() {
        return this.api.get<Role[]>('accessManagement.roles.base');
    }

    getById(id: string) {
        return this.api.get<Role>('accessManagement.roles.byId', {
            pathParams: { id }
        });
    }

    create(role: Role) {
        return this.api.post<Role>('accessManagement.roles.base', role).pipe(
            tap(() => {
                this.toast.success('Role created successfully');
            })
        );
    }

    update(id: string, role: Role) {
        return this.api.put<Role>('accessManagement.roles.byId', role, {
            pathParams: { id }
        }).pipe(
            tap(() => {
                this.toast.success('Role updated successfully');
            })
        );
    }

    delete(id: string) {
        return this.api.delete<void>('accessManagement.roles.byId', {
            pathParams: { id }
        }).pipe(
            tap(() => {
                this.toast.success('Role deleted successfully');
            })
        );
    }
}
