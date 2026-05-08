import { Component, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { InputTextModule } from 'primeng/inputtext';

import { forkJoin } from 'rxjs';

import { UserService } from '@features/iam/users/services/user.service';
import { User } from '@features/iam/models/user.model';
import { BaseListComponent } from '@shared/components/base/base-list.component';

import { ToolbarActionComponent } from '@shared/toolbar/toolbar-action.component';
import { UserFormPage } from '@features/iam/users/pages/user-form/user-form.page';

// ✅ use your wrapper services
import { CsmConfirmService } from '@core/services/csm-confirm.service';
import { AppToastService } from '@core/services/app-toast.service';
import {TooltipModule} from "primeng/tooltip";
import {CsmDialogComponent} from "@shared/components/csm-dialog/csm-dialog.component";
import {HasPermissionDirective} from "@shared/directives/permission/has-permission.directive";

@Component({
    selector: 'user-list',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        TagModule,
        TooltipModule,
        InputTextModule,
        ToolbarActionComponent,
        UserFormPage,
        CsmDialogComponent,
        HasPermissionDirective
    ],
    templateUrl: './user-list.page.html'
})
export class UserListPage extends BaseListComponent<User> {

    protected override resourceName = 'USER';
    // =========================
    // Dialog State
    // =========================
    dialogVisible = false;
    selectedUserId: string | null = null;

    // =========================
    // Services
    // =========================
    private service = inject(UserService);
    private toast = inject(AppToastService);
    private confirm = inject(CsmConfirmService); // or use ConfirmationService if you prefer

    // =========================
    // Table
    // =========================
    selectedUsers: User[] = [];

    @ViewChild('dt') dt!: Table;

    override ngOnInit(): void {
        console.log('UserListPage INIT');
        super.ngOnInit();
    }

    // =========================
    // Data Fetch
    // =========================
     override fetch() {
         console.log('FETCH CALLED');
         return this.service.getAll();
    }

    // =========================
    // Toolbar Actions
    // =========================

    openCreate() {
        this.selectedUserId = null;
        this.dialogVisible = true;
    }

    deleteSelectedUsers() {
        if (!this.selectedUsers.length) return;

        this.confirm.delete('Delete selected users?', () => {
            const requests = this.selectedUsers.map(u =>
                this.service.delete(u.id!)
            );

            forkJoin(requests).subscribe({
                next: () => {
                    this.toast.success('Users deleted successfully');
                    this.refresh();
                    this.selectedUsers = [];
                },
                error: () => {
                    this.toast.error('Failed to delete users');
                }
            });
        });
    }

    exportCSV() {
        this.dt.exportCSV();
    }

    // =========================
    // Row Actions
    // =========================

    openEdit(user: User) {
        this.selectedUserId = user.id ?? null;
        this.dialogVisible = true;
    }

    deleteUser(user: User) {
        this.confirm.delete(
            `Delete user "${user.username}"?`,
            () => {
                this.service.delete(user.id!).subscribe({
                    next: () => {
                        this.toast.success('User deleted successfully');
                        this.refresh();
                    },
                    error: () => {
                        this.toast.error('Failed to delete user');
                    }
                });
            }
        );
    }

    // =========================
    // Dialog Callback
    // =========================

    onSaved() {
        this.dialogVisible = false;
        this.refresh();
    }

    // =========================
    // Search
    // =========================

    onSearch(value: string) {
        this.dt.filterGlobal(value, 'contains');
    }
}
