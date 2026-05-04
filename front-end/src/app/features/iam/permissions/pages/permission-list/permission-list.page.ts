import { Component, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';

import { forkJoin } from 'rxjs';

import { Permission } from '@features/iam/models/permission.model';
import { BaseListComponent } from '@core/base/base-list.component';
import { PermissionService } from '@features/iam/permissions/services/permission.service';

import { ToolbarActionComponent } from '@shared/toolbar/toolbar-action.component';
import { PermissionFormPage } from '@features/iam/permissions/pages/permission-form/permission-form.page';

// ✅ use wrapper services
import { AppToastService } from '@core/services/app-toast.service';
import { CsmConfirmService } from '@core/services/csm-confirm.service';
import {Tooltip, TooltipModule} from "primeng/tooltip";

@Component({
    selector: 'permission-list',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        TagModule,
        ToolbarActionComponent,
        PermissionFormPage,
        TooltipModule
    ],
    templateUrl: './permission-list.page.html'
})
export class PermissionListPage extends BaseListComponent<Permission> {

    private service = inject(PermissionService);
    private toast = inject(AppToastService);
    private confirm = inject(CsmConfirmService);

    dialogVisible = false;
    selectedPermissionId: string | null = null;
    selectedPermissions: Permission[] = [];

    @ViewChild('dt') dt!: Table;

    fetch() {
        return this.service.getAll();
    }

    // =========================
    // Actions
    // =========================

    openCreate() {
        this.selectedPermissionId = null;
        this.dialogVisible = true;
    }

    openEdit(permission: Permission) {
        this.selectedPermissionId = permission.id ?? null;
        this.dialogVisible = true;
    }

    deleteSelectedPermissions() {
        if (!this.selectedPermissions.length) return;

        this.confirm.delete('Delete selected permissions?', () => {
            const requests = this.selectedPermissions.map(p =>
                this.service.delete(p.id!)
            );

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
        this.confirm.delete(
            `Delete "${permission.name}"?`,
            () => {
                this.service.delete(permission.id!).subscribe({
                    next: () => {
                        this.toast.success('Permission deleted');
                        this.refresh();
                    },
                    error: () => {
                        this.toast.error('Delete failed');
                    }
                });
            }
        );
    }

    onSaved() {
        this.toast.success('Permission saved successfully');
        this.dialogVisible = false;
        this.refresh();
    }

    onSearch(value: string) {
        this.dt.filterGlobal(value, 'contains');
    }
}
