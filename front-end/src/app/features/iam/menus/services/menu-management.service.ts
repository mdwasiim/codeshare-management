import { Injectable, inject } from '@angular/core';
import { AppApiService } from '@core/config/app-api.service';
import { AppToastService } from '@core/services/app-toast.service';
import { AppMenuModel } from '@shared/models/app-menu.model';
import { tap } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MenuManagementService {

    private api = inject(AppApiService);
    private toast = inject(AppToastService);

    // -----------------------------
    // GET ALL
    // -----------------------------
    getAll() {
        return this.api.get<AppMenuModel[]>('menu.base');
    }

    // -----------------------------
    // GET BY ID
    // -----------------------------
    getById(id: string) {
        return this.api.get<AppMenuModel>('menu.byId', {
            pathParams: { id }
        });
    }

    // -----------------------------
    // CREATE
    // -----------------------------
    create(menuModel: AppMenuModel) {

        return this.api.post<AppMenuModel>('menu.base', menuModel).pipe(
            tap(() => {
                this.toast.success('Menu created successfully');
            })
        );
    }

    // -----------------------------
    // UPDATE
    // -----------------------------
    update(id: string, menuModel: AppMenuModel) {
       debugger;
        return this.api.put<AppMenuModel>('menu.byId', menuModel, {
            pathParams: { id }
        }).pipe(
            tap(() => {
                this.toast.success('Menu updated successfully');
            })
        );
    }

    // -----------------------------
    // DELETE
    // -----------------------------
    delete(id: string) {
        return this.api.delete<void>('menu.base', {
            pathParams: { id }
        }).pipe(
            tap(() => {
                this.toast.success('Menu deleted successfully');
            })
        );
    }
}
