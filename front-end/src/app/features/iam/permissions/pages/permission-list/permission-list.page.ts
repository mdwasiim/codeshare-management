import {Component, inject, ViewChild} from '@angular/core';
import {CommonModule} from '@angular/common';

import {Table, TableModule} from 'primeng/table';
import {ButtonModule} from 'primeng/button';
import {TagModule} from 'primeng/tag';

import {forkJoin} from 'rxjs';

import {Permission} from '@features/iam/models/permission.model';
import {BaseListComponent} from '@shared/components/base/base-list.component';
import {PermissionApiService} from '@features/iam/permissions/services/permission-api.service';

import {ToolbarActionComponent} from '@shared/toolbar/toolbar-action.component';

// ✅ use wrapper services
import {AppToastService} from '@core/services/app-toast.service';
import {CsmConfirmService} from '@core/services/csm-confirm.service';
import {TooltipModule} from "primeng/tooltip";
import {CsmDialogComponent} from "@shared/components/csm-dialog/csm-dialog.component";
import {PermissionFormPage} from "@features/iam/permissions/pages/permission-form/permission-form.page";
import {HasPermissionDirective} from "@shared/directives/permission/has-permission.directive";

@Component({
    selector: 'permission-list',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        TagModule,
        ToolbarActionComponent,
        TooltipModule,
        CsmDialogComponent,
        PermissionFormPage,
        HasPermissionDirective
    ],
    templateUrl: './permission-list.page.html'
})
export class PermissionListPage extends BaseListComponent<Permission> {
    protected override resourceName = 'permission';
    private service = inject(PermissionApiService);
    private toast = inject(AppToastService);
    private confirm = inject(CsmConfirmService);

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
