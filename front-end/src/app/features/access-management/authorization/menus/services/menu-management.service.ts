import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/api/config/app-api.service';
import { API_ENDPOINTS } from '@core/api/config/app-api.config';
import { AppToastService } from '@services/toast/app-toast.service';
import { AppMenuModel } from '@features/access-management/models/app-menu.model';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MenuManagementService {
    private api = inject(AppApiService);
    private toast = inject(AppToastService);

    // -----------------------------
    // GET ALL
    // -----------------------------
    getAll() {
        return this.api.get<AppMenuModel[]>(API_ENDPOINTS.accessManagement.menu.manage);
    }

    getAuthorized() {
        return this.api.get<AppMenuModel[]>(API_ENDPOINTS.accessManagement.menu.base);
    }

    // -----------------------------
    // GET BY ID
    // -----------------------------
    getById(id: string) {
        return this.api.get<AppMenuModel>(API_ENDPOINTS.accessManagement.menu.byId, {
            pathParams: { id }
        });
    }

    // -----------------------------
    // CREATE
    // -----------------------------
    create(menuModel: AppMenuModel) {
        return this.api.post<AppMenuModel>(API_ENDPOINTS.accessManagement.menu.base, menuModel).pipe(
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
            .put<AppMenuModel>(API_ENDPOINTS.accessManagement.menu.byId, menuModel, {
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
            .delete<void>(API_ENDPOINTS.accessManagement.menu.byId, {
                pathParams: { id }
            })
            .pipe(
                tap(() => {
                    this.toast.success('Menu deleted successfully');
                })
            );
    }
}
