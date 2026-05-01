import {Component, inject, Input, ViewChild} from '@angular/core';
import { CommonModule } from '@angular/common';
import {Table, TableModule} from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { BaseListComponent } from '@core/base/base-list.component';
import { MenuManagementService } from '../../services/menu-management.service';
import { AppMenuModel } from '@shared/models/app-menu.model';
import { MenuFormPage } from "@features/iam/menus/pages/menu-form/menu-form.page";
import {map, takeUntil} from 'rxjs/operators';
import {Observable, tap} from "rxjs";
import {ConfirmDialog} from "primeng/confirmdialog";
import {Toast, ToastItem} from "primeng/toast";
import {ToolbarActionComponent} from "@shared/toolbar/toolbar-action.component";
import {TreeTableModule} from "primeng/treetable";

@Component({
    selector: 'menu-list',
    standalone: true,
    imports: [CommonModule, TableModule,TreeTableModule, ButtonModule, MenuFormPage, ConfirmDialog, Toast, ToolbarActionComponent],
    templateUrl: './menu-list.page.html'
})
export class MenuListPage extends BaseListComponent<AppMenuModel> {

    private service = inject(MenuManagementService);

    @ViewChild('dt') dt!: Table;
    selectedMenus: any[] = [];
    dialogVisible = false;
    selectedMenuId: string | null = null;

    fetch(): Observable<any[]> {
        return this.service.getAll().pipe(
            map(res => {
                console.log('API DATA:', res);
                const tree = this.buildTree(res);
                console.log('FINAL TREE:', tree);
                return tree;   // ✅ RETURN TREE
            }),
            tap(res => {
                console.log('API DATA:', res);
                console.log('FIRST ITEM:', res[0]);
            })
        );
    }

    openCreate() {
        this.selectedMenuId = null;
        this.dialogVisible = true;
    }

    openEdit(menu: AppMenuModel) {
        if (!menu) {
            console.error('Menu is undefined!', menu);
            return;
        }
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

        const ids = this.selectedMenus.map(n => n.data.id);

        ids.forEach(id => {
            this.service.delete(id!)
                .pipe(takeUntil(this.destroy$))
                .subscribe(() => this.refresh());
        });

        this.selectedMenus = [];
    }

    private buildTree(items: AppMenuModel[]): any[] {
        const map = new Map<string, any>();

        items.forEach(item => {
            if (!item?.id) return;

            map.set(item.id, {
                key: item.id,
                data: { ...item },   // ✅ FIX HERE
                children: []
            });
        });

        const roots: any[] = [];

        items.forEach(item => {
            if (!item?.id) return;

            const node = map.get(item.id);
            if (!node) return;

            if (item.parentId && map.has(item.parentId)) {
                map.get(item.parentId).children.push(node);
            } else {
                roots.push(node);
            }
        });

        console.log('FINAL TREE:', roots);

        return roots;
    }
}
