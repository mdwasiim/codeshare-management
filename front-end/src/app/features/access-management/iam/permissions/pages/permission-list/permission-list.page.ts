import { Component, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';

import { forkJoin } from 'rxjs';

import { Permission } from '@features/access-management/iam/models/permission.model';
import { BaseListComponent } from '@shared/components/base/base-list.component';
import { PermissionApiService } from '@features/access-management/iam/permissions/services/permission-api.service';

import { ToolbarActionComponent } from '@shared/components/toolbar/toolbar-action.component';

// ✅ use wrapper services
import { AppToastService } from '@services/toast/app-toast.service';
import { AppConfirmService } from '@services/app-confirm.service';
import { TooltipModule } from 'primeng/tooltip';
import { AppDialogComponent } from '@shared/components/app-dialog/app-dialog.component';
import { PermissionFormPage } from '@features/access-management/iam/permissions/pages/permission-form/permission-form.page';
import { HasPermissionDirective } from '@shared/directives/permission/has-permission.directive';

@Component({
    selector: 'permission-list',
    standalone: true,
    imports: [CommonModule, TableModule, ButtonModule, TagModule, ToolbarActionComponent, TooltipModule, AppDialogComponent, PermissionFormPage, HasPermissionDirective],
    templateUrl: './permission-list.page.html'
})
export class PermissionListPage extends BaseListComponent<Permission> {
    protected override resourceName = 'permission';

    private service = inject(PermissionApiService);
    private toast = inject(AppToastService);
    private confirm = inject(AppConfirmService);

    dialogVisible = false;
    selectedId: string | null = null;
    selectedPermissions: Permission[] = [];

    @ViewChild('dt') dt!: Table;

    fetch() {
        return this.service.getAll();
    }

    // =========================
    // Actions
    // =========================

    openCreate() {
        this.selectedId = null;
        this.dialogVisible = true;
    }

    openEdit(permission: Permission) {
        this.selectedId = permission.id ?? null;
        this.dialogVisible = true;
    }

    deleteSelectedPermissions() {
        if (!this.selectedPermissions.length) return;

        this.confirm.delete('Delete selected permissions?', () => {
            const requests = this.selectedPermissions.map((p) => this.service.delete(p.id!));

            forkJoin(requests).subscribe({
                next: () => {
                    this.toast.success('Permissions deleted successfully');
                    this.refresh();
                    this.selectedPermissions = [];
                },
                error: () => {
                    this.toast.error('Failed to delete permissions');
                }
            });
        });
    }

    deletePermission(permission: Permission) {
        this.confirm.delete(`Delete "${permission.name}"?`, () => {
            this.service.delete(permission.id!).subscribe({
                next: () => {
                    this.toast.success('Permission deleted');
                    this.refresh();
                },
                error: () => {
                    this.toast.error('Delete failed');
                }
            });
        });
    }

    onSaved() {
        this.dialogVisible = false;
        this.refresh();
    }

    onSearch(value: string) {
        this.dt.filterGlobal(value, 'contains');
    }

    exportCSV() {
        this.dt.exportCSV();
    }
}
