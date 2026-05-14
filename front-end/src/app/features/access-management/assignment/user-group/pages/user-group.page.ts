import { UserGroupService } from '@features/access-management/assignment/user-group/services/user-group.service';
import { User } from '@features/access-management/iam/models/user.model';
import { Group } from '@features/access-management/iam/models/group.model';
import { forkJoin } from 'rxjs';
import { Component, inject, OnInit } from '@angular/core';
import { AppToastService } from '@services/toast/app-toast.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AccordionModule } from 'primeng/accordion';
import { CheckboxModule } from 'primeng/checkbox';
import { TableModule } from 'primeng/table';
import { CsmAssignmentLayoutComponent } from '@shared/components/access-management/csm-assignment-layout/csm-assignment-layout.component';
import { ToolbarActionComponent } from '@shared/components/toolbar/toolbar-action.component';

@Component({
    selector: 'app-user-group',
    standalone: true,
    imports: [CommonModule, FormsModule, AccordionModule, CheckboxModule, TableModule, CsmAssignmentLayoutComponent, ToolbarActionComponent],
    templateUrl: './user-group.page.html'
})
export class UserGroupPage implements OnInit {
    private service = inject(UserGroupService);

    private toast = inject(AppToastService);

    // =====================================================
    // STATE
    // =====================================================
    users: User[] = [];

    groups: Group[] = [];

    selectedUser?: User;

    selectedGroupIds: string[] = [];

    loading = false;

    saving = false;

    // =====================================================
    // INIT
    // =====================================================
    ngOnInit(): void {
        this.loadData();
    }

    // =====================================================
    // LOAD INITIAL DATA
    // =====================================================
    loadData(): void {
        this.loading = true;

        forkJoin({
            users: this.service.getAllUsers(),

            groups: this.service.getAllGroups()
        }).subscribe({
            next: (res) => {
                this.users = res.users || [];

                this.groups = res.groups || [];

                this.loading = false;
            },

            error: (err) => {
                console.error(err);

                this.loading = false;

                this.toast.error('Failed to load data');
            }
        });
    }

    // =====================================================
    // USER SELECT
    // =====================================================
    onUserSelect(user: User): void {
        this.selectedUser = user;

        this.selectedGroupIds = [];

        if (!user.id) {
            return;
        }

        this.loading = true;

        this.service.getGroupsByUser(user.id).subscribe({
            next: (groups) => {
                this.selectedGroupIds = groups.map((group) => group.id!).filter(Boolean);

                this.loading = false;
            },

            error: (err) => {
                console.error(err);

                this.loading = false;

                this.toast.error('Failed to load groups');
            }
        });
    }

    // =====================================================
    // TOGGLE GROUP
    // =====================================================
    toggleGroup(groupId: string, checked: boolean): void {
        if (checked) {
            if (!this.selectedGroupIds.includes(groupId)) {
                this.selectedGroupIds.push(groupId);
            }
        } else {
            this.selectedGroupIds = this.selectedGroupIds.filter((id) => id !== groupId);
        }
    }

    // =====================================================
    // SAVE
    // =====================================================
    save(): void {
        if (!this.selectedUser?.id) {
            this.toast.warn('Please select a user');

            return;
        }

        this.saving = true;

        this.service.replaceUserGroups(this.selectedUser.id, this.selectedGroupIds).subscribe({
            next: () => {
                this.toast.success('User groups updated successfully');

                this.saving = false;
            },

            error: (err) => {
                console.error(err);

                this.toast.error('Failed to update groups');

                this.saving = false;
            }
        });
    }

    // =====================================================
    // RESET
    // =====================================================
    reset(): void {
        if (!this.selectedUser) {
            return;
        }

        this.onUserSelect(this.selectedUser);
    }
}
