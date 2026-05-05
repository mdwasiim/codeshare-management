import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/config/app-api.service';
import { AppToastService } from '@core/services/app-toast.service';
import { Permission } from '@features/iam/models/permission.model';
import { tap } from 'rxjs';
import {Group} from "@features/iam/models/group.model";

@Injectable({ providedIn: 'root' })
export class PermissionApiService {

    private api = inject(AppApiService);
    private toast = inject(AppToastService);

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
        return this.api.post<Permission>('permissions.base', permission).pipe(
            tap(() => {
                this.toast.success('Permission created successfully');
            })
        );
    }

    // -----------------------------
    // UPDATE
    // -----------------------------
    update(id: string, permission: Permission) {
        return this.api.put<Permission>('permissions.byId', permission, {
            pathParams: { id }
        }).pipe(
            tap(() => {
                this.toast.success('Permission updated successfully');
            })
        );
    }

    // -----------------------------
    // DELETE
    // -----------------------------
    delete(id: string) {
        return this.api.delete<void>('permissions.byId', {
            pathParams: { id }
        }).pipe(
            tap(() => {
                this.toast.success('Permission deleted successfully');
            })
        );
    }
}
