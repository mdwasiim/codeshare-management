import {Component, inject, OnInit} from '@angular/core';

import {CommonModule} from '@angular/common';

import {FormsModule} from '@angular/forms';

import {forkJoin} from 'rxjs';

import {AccordionModule} from 'primeng/accordion';

import {CheckboxModule} from 'primeng/checkbox';

import {TableModule, TableRowSelectEvent} from 'primeng/table';

import {
    CsmAssignmentLayoutComponent
} from '@shared/components/access-management/csm-assignment-layout/csm-assignment-layout.component';

import {RolePermissionService} from '@features/access-management/role-permissions/services/role-permission.service';

import {Permission} from '@features/iam/models/permission.model';

import {Role} from '@features/iam/models/role.model';
import {ToolbarActionComponent} from "@shared/components/toolbar/toolbar-action.component";
import {AppToastService} from "@services/app-toast.service";
import {Group} from "@features/iam/models/group.model";
import {GroupRoleService} from "@features/access-management/group-roles/services/group-role.service";

@Component({
    selector: 'group-role-assignment-page',
    standalone: true,
    imports: [
        CommonModule,
        FormsModule,

        AccordionModule,
        CheckboxModule,
        TableModule,

        CsmAssignmentLayoutComponent,
        ToolbarActionComponent
    ],
    templateUrl:
        './group-role-assignment.page.html'
})
export class GroupRoleAssignmentPage
    implements OnInit {

    // =========================
    // SERVICES
    // =========================
    private groupRoleService =  inject(GroupRoleService);
    private toast = inject(AppToastService);

    // =========================
    // STATE
    // =========================
    groups: Group[] = [];

    roles: Role[] = [];

    selectedGroup: Group | null = null;

    selectedRoleIds: string[] = [];

    loading = false;

    saving = false;

    // =========================
    // INIT
    // =========================
    ngOnInit(): void {

        this.loadData();
    }

    // =========================
    // LOAD INITIAL DATA
    // =========================
    loadData(): void {

        this.loading = true;

        forkJoin({

            groups: this.groupRoleService.getAllGroups(),

            roles: this.groupRoleService.getAllRoles()

        }).subscribe({

            next: (res) => {

                this.groups = res.groups || [];

                this.roles = res.roles || [];

                this.loading = false;
            },

            error: (err) => {

                console.error(err);

                this.loading = false;
            }
        });
    }
    // =========================
    // GROUP SELECT
    // =========================
    onGroupSelect(
        group: Group
    ): void {

        this.selectedGroup = group;

        this.selectedRoleIds = [];

        if (!group.id) {
            return;
        }

        this.loading = true;

        this.groupRoleService
            .getRolesByGroup(group.id)
            .subscribe({

                next: (roles: Role[]) => {

                    this.selectedRoleIds =
                        roles
                            .map(role => role.id!)
                            .filter(Boolean);

                    this.loading = false;
                },

                error: (err) => {

                    console.error(err);

                    this.loading = false;

                    this.toast.error(
                        'Failed to load assigned roles'
                    );
                }
            });
    }

    // =========================
    // TOGGLE PERMISSION
    // =========================
    toggleRole(
        roleId: string,
        checked: boolean
    ): void {

        if (checked) {

            if (!this.selectedRoleIds.includes(roleId)) {

                this.selectedRoleIds.push(roleId);
            }

        } else {

            this.selectedRoleIds =
                this.selectedRoleIds.filter(
                    id => id !== roleId
                );
        }
    }

    // =========================
    // SAVE
    // =========================
    save(): void {

        if (!this.selectedGroup?.id) {

            this.toast.warn(
                'Please select a group'
            );

            return;
        }

        this.saving = true;

        this.groupRoleService
            .replaceGroupRoles(
                this.selectedGroup.id,
                this.selectedRoleIds
            )
            .subscribe({

                next: () => {

                    this.toast.success(
                        'Roles updated successfully'
                    );

                    this.saving = false;
                },

                error: (err) => {

                    console.error(err);

                    this.toast.error(
                        'Failed to update roles'
                    );

                    this.saving = false;
                }
            });
    }

    reset(): void {
        if (!this.selectedGroup) {
            return;
        }

        this.onGroupSelect(this.selectedGroup);
    }
}
