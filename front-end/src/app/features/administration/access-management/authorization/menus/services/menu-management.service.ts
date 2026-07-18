import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';
import { AppMenuModel } from '@features/administration/access-management/models/app-menu.model';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MenuManagementService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);

    // -----------------------------
    // GET ALL
    // -----------------------------
    getAll(params?: Record<string, string>) {
        return this.api.get<AppMenuModel[]>(API_ENDPOINTS.identityService.menus.manage, { params });
    }

    getAuthorized(params?: Record<string, string>) {
        return this.api.get<AppMenuModel[]>(API_ENDPOINTS.identityService.menus.base, { params });
    }

    // -----------------------------
    // GET BY ID
    // -----------------------------
    getById(id: string) {
        return this.api.get<AppMenuModel>(API_ENDPOINTS.identityService.menus.byId, {
            pathParams: { id }
        });
    }

    // -----------------------------
    // CREATE
    // -----------------------------
    create(menuModel: AppMenuModel) {
        return this.api.post<AppMenuModel>(API_ENDPOINTS.identityService.menus.base, menuModel).pipe(
            tap(() => {
                this.toast.success('Menu created successfully');
            })
        );
    }

    // -----------------------------
    // UPDATE
    // -----------------------------
    update(id: string, menuModel: AppMenuModel) {
        return this.api
            .put<AppMenuModel>(API_ENDPOINTS.identityService.menus.byId, menuModel, {
                pathParams: { id }
            })
            .pipe(
                tap(() => {
                    this.toast.success('Menu updated successfully');
                })
            );
    }

    // -----------------------------
    // DELETE
    // -----------------------------
    delete(id: string) {
        return this.api
            .delete<void>(API_ENDPOINTS.identityService.menus.byId, {
                pathParams: { id }
            })
            .pipe(
                tap(() => {
                    this.toast.success('Menu deleted successfully');
                })
            );
    }
}

