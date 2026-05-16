import { Component, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TreeTable, TreeTableModule } from 'primeng/treetable';
import { ButtonModule } from 'primeng/button';

import { BaseListComponent } from '@shared/components/base/base-list.component';
import { MenuManagementService } from '../../services/menu-management.service';
import { AppMenuModel } from '@features/access-management/iam/models/app-menu.model';
import { ToolbarActionComponent } from '@shared/components/toolbar/toolbar-action.component';

import { map } from 'rxjs/operators';
import { forkJoin, Observable } from 'rxjs';

// ✅ wrapper services
import { AppToastService } from '@services/toast/app-toast.service';
import { CsmConfirmService } from '@services/csm-confirm.service';
import { TooltipModule } from 'primeng/tooltip';
import { MenuFormPage } from '@features/access-management/iam/menus/pages/menu-form/menu-form.page';
import { CsmDialogComponent } from '@shared/components/csm-dialog/csm-dialog.component';
import { HasPermissionDirective } from '@shared/directives/permission/has-permission.directive';
import { LayoutMenuService } from '@layout/services/layout-menu.service';

@Component({
    selector: 'menu-list',
    standalone: true,
    imports: [CommonModule, TreeTableModule, ButtonModule, ToolbarActionComponent, TooltipModule, MenuFormPage, CsmDialogComponent, HasPermissionDirective],
    templateUrl: './menu-list.page.html'
})
export class MenuListPage extends BaseListComponent<AppMenuModel> {
    protected override resourceName = 'menu';
    private service = inject(MenuManagementService);
    private toast = inject(AppToastService);
    private confirm = inject(CsmConfirmService);
    private layoutMenuService = inject(LayoutMenuService);

    @ViewChild('dt') dt!: TreeTable;

    selectedMenus: any[] = [];
    dialogVisible = false;
    selectedId: string | null = null;

    // =========================
    // Fetch Tree Data
    // =========================
    fetch(): Observable<any[]> {
        return this.service.getAll().pipe(map((res) => this.buildTree(res)));
    }

    // =========================
    // Toolbar Actions
    // =========================

    openCreate() {
        this.selectedId = null;
        this.dialogVisible = true;
    }

    openEdit(menu: AppMenuModel) {
        if (!menu) return;

        this.selectedId = menu.id ?? null;
        this.dialogVisible = true;
    }

    deleteMenu(menu: AppMenuModel) {
        this.confirm.delete(`Delete menu "${menu.label}"?`, () => {
            this.service.delete(menu.id!).subscribe({
                next: () => {
                    this.toast.success('Menu deleted successfully');
                    this.refresh();
                },
                error: () => {
                    this.toast.error('Failed to delete menu');
                }
            });
        });
    }

    deleteSelectedMenus() {
        if (!this.selectedMenus?.length) return;

        this.confirm.delete('Delete selected menus?', () => {
            const ids = this.selectedMenus.map((n) => n.data.id);
            const requests = ids.map((id) => this.service.delete(id));

            forkJoin(requests).subscribe({
                next: () => {
                    this.toast.success('Menus deleted successfully');
                    this.refresh();
                    this.selectedMenus = [];
                },
                error: () => {
                    this.toast.error('Failed to delete menus');
                }
            });
        });
    }

    onSaved() {
        this.toast.success('Menu saved successfully');
        this.dialogVisible = false;
        this.refresh();
        this.layoutMenuService.loadMenus().subscribe();
    }

    onSearch(value: string) {
        this.dt.filterGlobal(value, 'contains');
    }

    // =========================
    // Tree Builder
    // =========================
    private buildTree(items: AppMenuModel[]): any[] {
        const map = new Map<string, any>();

        items.forEach((item) => {
            if (!item?.id) return;

            map.set(item.id, {
                key: item.id,
                data: { ...item },
                children: []
            });
        });

        const roots: any[] = [];

        items.forEach((item) => {
            if (!item?.id) return;

            const node = map.get(item.id);

            if (item.parentId && map.has(item.parentId)) {
                map.get(item.parentId).children.push(node);
            } else {
                roots.push(node);
            }
        });

        return roots;
    }

    exportCSV() {
        //this.dt.exportCSV();
    }
}
