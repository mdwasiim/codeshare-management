import { Component, inject, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { InputTextModule } from 'primeng/inputtext';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import {ConfirmationService, MessageService} from 'primeng/api';

import { forkJoin } from 'rxjs';

import { UserService } from '@features/iam/users/services/user.service';
import { User } from '@features/iam/models/user.model';
import { BaseListComponent } from '@core/base/base-list.component';

import { ToolbarActionComponent } from '@shared/toolbar/toolbar-action.component';
import { UserFormPage } from '@features/iam/users/pages/user-form/user-form.page';

@Component({
    selector: 'user-list',
    standalone: true,
    imports: [
        CommonModule,
        TableModule,
        ButtonModule,
        TagModule,
        InputTextModule,
        ConfirmDialogModule,
        ToastModule,
        ToolbarActionComponent,
        UserFormPage
    ],
    templateUrl: './user-list.page.html',
    providers: [ConfirmationService, MessageService]
})
export class UserListPage extends BaseListComponent<User> {

    // =========================
    // Dialog State
    // =========================
    dialogVisible = false;
    selectedUserId: string | null = null;

    // =========================
    // Services
    // =========================
    private service = inject(UserService);
    private confirmationService = inject(ConfirmationService);

    // =========================
    // Table
    // =========================
    selectedUsers: User[] = [];

    @ViewChild('dt') dt!: Table;

    // =========================
    // Data Fetch
    // =========================
    fetch() {
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

        this.confirmationService.confirm({
            message: 'Delete selected users?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                const requests = this.selectedUsers.map(u =>
                    this.service.delete(u.id!)
                );

                forkJoin(requests).subscribe(() => {
                    this.refresh();
                    this.selectedUsers = [];
                });
            }
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
        this.confirmationService.confirm({
            message: `Delete user "${user.username}"?`,
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.service.delete(user.id!)
                    .subscribe(() => this.refresh());
            }
        });
    }

    // =========================
    // Dialog Callback
    // =========================

    onSaved() {
        this.refresh();
    }

    // =========================
    // Search
    // =========================

    onSearch(value: string) {
        this.dt.filterGlobal(value, 'contains');
    }
}
