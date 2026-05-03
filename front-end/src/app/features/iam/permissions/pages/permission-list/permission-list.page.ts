import { Component, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import {ConfirmationService, MessageService} from 'primeng/api';

import { forkJoin } from 'rxjs';

import { Permission } from '@features/iam/models/permission.model';
import { BaseListComponent } from '@core/base/base-list.component';
import { PermissionService } from '@features/iam/permissions/services/permission.service';

import { ToolbarActionComponent } from '@shared/toolbar/toolbar-action.component';
import { PermissionFormPage } from '@features/iam/permissions/pages/permission-form/permission-form.page';

@Component({
    selector: 'permission-list',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        TagModule,
        ConfirmDialogModule,
        ToastModule,
        ToolbarActionComponent,
        PermissionFormPage
    ],
    templateUrl: './permission-list.page.html',
    providers: [ConfirmationService, MessageService]
})
export class PermissionListPage extends BaseListComponent<Permission> {

    private service = inject(PermissionService);
    private confirmationService = inject(ConfirmationService);

    dialogVisible = false;
    selectedPermissionId: string | null = null;
    selectedPermissions: Permission[] = [];

    @ViewChild('dt') dt!: Table;
    tenantId = 'QR';
    fetch() {
        return this.service.getAll(this.tenantId);
    }

    // =========================
    // Toolbar Actions
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

        this.confirmationService.confirm({
            message: 'Delete selected permissions?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                const requests = this.selectedPermissions.map(p =>
                    this.service.delete(p.id!)
                );

                forkJoin(requests).subscribe(() => {
                    this.refresh();
                    this.selectedPermissions = [];
                });
            }
        });
    }

    deletePermission(permission: Permission) {
        this.confirmationService.confirm({
            message: `Delete "${permission.name}"?`,
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.service.delete(permission.id!)
                    .subscribe(() => this.refresh());
            }
        });
    }

    onSaved() {
        this.refresh();
    }

    onSearch(value: string) {
        this.dt.filterGlobal(value, 'contains');
    }
}
