import { Component, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import {ConfirmationService, MessageService} from 'primeng/api';

import { forkJoin } from 'rxjs';

import { Role } from '@features/iam/models/role.model';
import { BaseListComponent } from '@core/base/base-list.component';
import { RoleService } from '../../services/role.service';

import { ToolbarActionComponent } from '@shared/toolbar/toolbar-action.component';
import { RoleFormPage } from '@features/iam/roles/pages/role-form/role-form.page';

@Component({
    selector: 'role-list',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        TagModule,
        ConfirmDialogModule,
        ToastModule,
        ToolbarActionComponent,
        RoleFormPage
    ],
    templateUrl: './role-list.page.html',
    providers: [ConfirmationService, MessageService]
})
export class RoleListPage extends BaseListComponent<Role> {

    private service = inject(RoleService);
    private confirmationService = inject(ConfirmationService);

    tenantId = 'QR';

    dialogVisible = false;
    selectedRoleId: string | null = null;
    selectedRoles: Role[] = [];

    @ViewChild('dt') dt!: Table;

    fetch() {
        return this.service.getAll(this.tenantId);
    }

    // =========================
    // Toolbar Actions
    // =========================

    openCreate() {
        this.selectedRoleId = null;
        this.dialogVisible = true;
    }

    openEdit(role: Role) {
        this.selectedRoleId = role.id ?? null;
        this.dialogVisible = true;
    }

    deleteSelectedRoles() {
        if (!this.selectedRoles.length) return;

        this.confirmationService.confirm({
            message: 'Delete selected roles?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                const requests = this.selectedRoles.map(r =>
                    this.service.delete(r.id!)
                );

                forkJoin(requests).subscribe(() => {
                    this.refresh();
                    this.selectedRoles = [];
                });
            }
        });
    }

    deleteRole(role: Role) {
        this.confirmationService.confirm({
            message: `Delete role "${role.name}"?`,
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.service.delete(role.id!)
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
