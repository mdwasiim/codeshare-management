import { Component, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';

import { forkJoin } from 'rxjs';

import { Role } from '@features/iam/models/role.model';
import { BaseListComponent } from '@shared/components/base/base-list.component';
import { RoleService } from '../../services/role.service';

import { ToolbarActionComponent } from '@shared/toolbar/toolbar-action.component';
import { RoleFormPage } from '@features/iam/roles/pages/role-form/role-form.page';

// ✅ wrapper services
import { AppToastService } from '@core/services/app-toast.service';
import { CsmConfirmService } from '@core/services/csm-confirm.service';
import {Tooltip, TooltipModule} from "primeng/tooltip";
import {PRIMENG_IMPORTS} from "@shared/primeng/primeng.imports";
import {CsmDialogComponent} from "@shared/components/csm-dialog/csm-dialog.component";

@Component({
    selector: 'role-list',
    standalone: true,
    imports: [
        CommonModule,
        ToolbarActionComponent,
        RoleFormPage,
        CsmDialogComponent,
        RoleFormPage,
        PRIMENG_IMPORTS
    ],
    templateUrl: './role-list.page.html'
})
export class RoleListPage extends BaseListComponent<Role> {

    private service = inject(RoleService);
    private toast = inject(AppToastService);
    private confirm = inject(CsmConfirmService);

    dialogVisible = false;
    selectedId: string | null = null;
    selectedRoles: Role[] = [];

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

    openEdit(role: Role) {
        this.selectedId = role.id ?? null;
        this.dialogVisible = true;
    }

    deleteSelectedRoles() {
        if (!this.selectedRoles.length) return;

        this.confirm.delete('Delete selected roles?', () => {
            const requests = this.selectedRoles.map(r =>
                this.service.delete(r.id!)
            );

            forkJoin(requests).subscribe({
                next: () => {
                    this.toast.success('Roles deleted successfully');
                    this.refresh();
                    this.selectedRoles = [];
                },
                error: () => {
                    this.toast.error('Failed to delete roles');
                }
            });
        });
    }

    deleteRole(role: Role) {
        this.confirm.delete(
            `Delete role "${role.displayName}"?`,
            () => {
                this.service.delete(role.id!).subscribe({
                    next: () => {
                        this.toast.success('Role deleted successfully');
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
