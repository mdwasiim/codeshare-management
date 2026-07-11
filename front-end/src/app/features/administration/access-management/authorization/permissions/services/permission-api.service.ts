import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';
import { Permission } from '@features/administration/access-management/models/permission.model';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PermissionApiService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);

    // -----------------------------
    // GET ALL
    // -----------------------------
    getAll() {
        return this.api.get<Permission[]>(API_ENDPOINTS.accessManagement.permissions.base);
    }

    // -----------------------------
    // GET BY ID
    // -----------------------------
    getById(id: string) {
        return this.api.get<Permission>(API_ENDPOINTS.accessManagement.permissions.byId, {
            pathParams: { id }
        });
    }

    // -----------------------------
    // CREATE
    // -----------------------------
    create(permission: Permission) {
        return this.api.post<Permission>(API_ENDPOINTS.accessManagement.permissions.base, permission).pipe(
            tap(() => {
                this.toast.success('Permission created successfully');
            })
        );
    }

    // -----------------------------
    // UPDATE
    // -----------------------------
    update(id: string, permission: Permission) {
        return this.api
            .put<Permission>(API_ENDPOINTS.accessManagement.permissions.byId, permission, {
                pathParams: { id }
            })
            .pipe(
                tap(() => {
                    this.toast.success('Permission updated successfully');
                })
            );
    }

    // -----------------------------
    // DELETE
    // -----------------------------
    delete(id: string) {
        return this.api
            .delete<void>(API_ENDPOINTS.accessManagement.permissions.byId, {
                pathParams: { id }
            })
            .pipe(
                tap(() => {
                    this.toast.success('Permission deleted successfully');
                })
            );
    }
}

