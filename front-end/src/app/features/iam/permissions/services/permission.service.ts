import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/config/app-api.service';
import { Permission } from '@features/iam/models/permission.model';

@Injectable({ providedIn: 'root' })
export class PermissionService {

    private api = inject(AppApiService);

    // -----------------------------
    // GET ALL
    // -----------------------------
    getAll() {
        return this.api.get<Permission[]>('permissions.base');
    }

    // -----------------------------
    // GET BY ID
    // -----------------------------
    getById(id: string) {
        return this.api.get<Permission>('permissions.byId', {
            pathParams: { id }
        });
    }

    // -----------------------------
    // CREATE
    // -----------------------------
    create(permission: Permission) {
        return this.api.post<Permission>('permissions.base', permission);
    }

    // -----------------------------
    // UPDATE
    // -----------------------------
    update(id: string, permission: Permission) {
        return this.api.put<Permission>('permissions.byId', permission, {
            pathParams: { id }
        });
    }

    // -----------------------------
    // DELETE
    // -----------------------------
    delete(id: string) {
        return this.api.delete<void>('permissions.byId', {
            pathParams: { id }
        });
    }
}
