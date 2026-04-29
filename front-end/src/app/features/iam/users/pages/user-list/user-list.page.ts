import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { Table, TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { InputTextModule } from 'primeng/inputtext';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

import { UserService } from '@features/iam/users/services/user.service';
import { User } from '@features/iam/models/user.model';
import { BaseListComponent } from '@core/base/base-list.component';

import { ToolbarActionComponent } from '@shared/toolbar/toolbar-action.component';

import { forkJoin } from 'rxjs';

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
        ToolbarActionComponent
    ],
    templateUrl: './user-list.page.html',
    providers: [ConfirmationService]
})
export class UserListPage extends BaseListComponent<User> implements OnInit {

    private service = inject(UserService);
    private router = inject(Router);
    private confirmationService = inject(ConfirmationService);

    // ✅ REQUIRED
    selectedUsers: User[] = [];

    // ✅ REQUIRED for filter + export
    @ViewChild('dt') dt!: Table;

    ngOnInit(): void {
        this.loadData();
    }

    fetch() {
        return this.service.getAll();
    }

    // =========================
    // Toolbar Actions
    // =========================

    createUser() {
        this.router.navigate(['/iam/users/create']);
    }

    deleteSelectedUsers() {
        this.confirmationService.confirm({
            message: 'Delete selected users?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                const requests = this.selectedUsers.map(u =>
                    this.service.delete(u.id!)
                );

                forkJoin(requests).subscribe(() => {
                    this.loadData();
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

    editUser(user: User) {
        this.router.navigate(['/iam/users', user.id]);
    }

    deleteUser(user: User) {
        this.confirmationService.confirm({
            message: `Delete user "${user.username}"?`,
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.service.delete(user.id!).subscribe(() => this.loadData());
            }
        });
    }

    // =========================
    // Helpers
    // =========================

    onGlobalFilter(table: Table, value: string) {
        table.filterGlobal(value, 'contains');
    }
}
