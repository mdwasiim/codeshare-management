import {Component, inject, Input, ViewChild} from '@angular/core';
import { CommonModule } from '@angular/common';
import {Table, TableModule} from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { BaseListComponent } from '@core/base/base-list.component';
import { MenuManagementService } from '../../services/menu-management.service';
import { AppMenuModel } from '@shared/models/app-menu.model';
import { MenuFormPage } from "@features/iam/menus/pages/menu-form/menu-form.page";
import { takeUntil } from 'rxjs/operators';
import {Observable} from "rxjs";
import {ConfirmDialog} from "primeng/confirmdialog";
import {Toast, ToastItem} from "primeng/toast";
import {ToolbarActionComponent} from "@shared/toolbar/toolbar-action.component";

@Component({
    selector: 'menu-list',
    standalone: true,
    imports: [CommonModule, TableModule, ButtonModule, MenuFormPage, ConfirmDialog, Toast, ToolbarActionComponent],
    templateUrl: './menu-list.page.html'
})
export class MenuListPage extends BaseListComponent<AppMenuModel> {

    private service = inject(MenuManagementService);

    @ViewChild('dt') dt!: Table;
    selectedMenus: AppMenuModel[] = [];
    dialogVisible = false;
    selectedMenuId: string | null = null;

    fetch(): Observable<AppMenuModel[]> {
        return this.service.getAll();
    }

    openCreate() {
        this.selectedMenuId = null;
        this.dialogVisible = true;
    }

    openEdit(menu: AppMenuModel) {
        this.selectedMenuId = menu.id ?? null;
        this.dialogVisible = true;
    }

    onSaved() {
        this.refresh(); // ✅ cleaner
    }

    deleteMenu(menu: AppMenuModel) {
        if (!confirm(`Delete menu "${menu.label}"?`)) return;

        this.service.delete(menu.id!)
            .pipe(takeUntil(this.destroy$))
            .subscribe(() => this.refresh());
    }

    onSearch(value: string) {
        this.dt.filterGlobal(value, 'contains');
    }

    deleteSelectedMenus() {
        if (!this.selectedMenus?.length) return;

        if (!confirm('Delete selected menus?')) return;

        const ids = this.selectedMenus.map(m => m.id);

        ids.forEach(id => {
            this.service.delete(id!)
                .pipe(takeUntil(this.destroy$))
                .subscribe(() => this.refresh());
        });

        this.selectedMenus = [];
    }
}
