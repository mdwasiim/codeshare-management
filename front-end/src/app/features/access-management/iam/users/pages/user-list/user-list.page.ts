import { Component, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { InputTextModule } from 'primeng/inputtext';

import { forkJoin } from 'rxjs';

import { UserService } from '@features/access-management/iam/users/services/user.service';
import { User } from '@features/access-management/iam/models/user.model';
import { BaseListComponent } from '@shared/components/base/base-list.component';

import { ToolbarActionComponent } from '@shared/components/toolbar/toolbar-action.component';
import { UserFormPage } from '@features/access-management/iam/users/pages/user-form/user-form.page';

// ✅ use your wrapper services
import { AppConfirmService } from '@services/app-confirm.service';
import { AppToastService } from '@services/toast/app-toast.service';
import { TooltipModule } from 'primeng/tooltip';
import { AppDialogComponent } from '@shared/components/app-dialog/app-dialog.component';
import { HasPermissionDirective } from '@shared/directives/permission/has-permission.directive';

@Component({
    selector: 'user-list',
    standalone: true,
    imports: [CommonModule, TableModule, ButtonModule, TagModule, TooltipModule, InputTextModule, ToolbarActionComponent, UserFormPage, AppDialogComponent, HasPermissionDirective],
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
    private confirm = inject(AppConfirmService); // or use ConfirmationService if you prefer

    // =========================
    // Table
    // =========================
    selectedUsers: User[] = [];

    @ViewChild('dt') dt!: Table;

    // =========================
    // Data Fetch
    // =========================
    override fetch() {
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
            const requests = this.selectedUsers.map((u) => this.service.delete(u.id!));

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
        this.confirm.delete(`Delete user "${user.username}"?`, () => {
            this.service.delete(user.id!).subscribe({
                next: () => {
                    this.toast.success('User deleted successfully');
                    this.refresh();
                },
                error: () => {
                    this.toast.error('Failed to delete user');
                }
            });
        });
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
