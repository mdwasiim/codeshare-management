import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';
import { Group } from '@features/administration/access-management/models/group.model';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class GroupService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);

    // -----------------------------
    // GET ALL
    // -----------------------------
    getAll(params?: Record<string, string>) {
        return this.api.get<Group[]>(API_ENDPOINTS.identityService.groups.base, { params });
    }

    // -----------------------------
    // GET BY ID
    // -----------------------------
    getById(id: string) {
        return this.api.get<Group>(API_ENDPOINTS.identityService.groups.byId, {
            pathParams: { id }
        });
    }

    // -----------------------------
    // CREATE
    // -----------------------------
    create(group: Group) {
        return this.api.post<Group>(API_ENDPOINTS.identityService.groups.base, group).pipe(
            tap(() => {
                this.toast.success('Group created successfully');
            })
        );
    }

    // -----------------------------
    // UPDATE
    // -----------------------------
    update(id: string, group: Group) {
        return this.api
            .put<Group>(API_ENDPOINTS.identityService.groups.byId, group, {
                pathParams: { id }
            })
            .pipe(
                tap(() => {
                    this.toast.success('Group updated successfully');
                })
            );
    }

    // -----------------------------
    // DELETE
    // -----------------------------
    delete(id: string) {
        return this.api
            .delete<void>(API_ENDPOINTS.identityService.groups.byId, {
                pathParams: { id }
            })
            .pipe(
                tap(() => {
                    this.toast.success('Group deleted successfully');
                })
            );
    }
}

