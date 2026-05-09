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

@Component({
    selector: 'role-permission-assignment-page',
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
        './role-permission-assignment.page.html'
})
export class RolePermissionAssignmentPage
    implements OnInit {

    // =========================
    // SERVICES
    // =========================
    private service =  inject(RolePermissionService);
    private toast = inject(AppToastService);

    // =========================
    // STATE
    // =========================
    loading = false;

    saving = false;

    search = '';

    selectedRole: Role | null = null;

    roles: Role[] = [];

    permissions: Permission[] = [];

    groupedPermissions: {
        domain: string;
        permissions: Permission[];
    }[] = [];

    selectedPermissionIds: string[] = [];

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

            roles:
                this.service.getRoles(),

            permissions:
                this.service.getPermissions()

        }).subscribe({

            next: (res) => {

                this.roles =
                    res.roles || [];

                this.permissions =
                    res.permissions || [];

                this.groupedPermissions =
                    this.groupPermissions(
                        this.permissions
                    );
                console.log('ROLES RESPONSE=', res.roles);
                console.log('PERMISSIONS RESPONSE=', res.permissions);
                this.loading = false;
            },

            error: (err) => {

                console.error(err);

                this.loading = false;
            }
        });
    }

    // =========================
    // GROUP PERMISSIONS
    // =========================
    groupPermissions(
        permissions: Permission[]
    ): {
        domain: string;
        permissions: Permission[];
    }[] {

        const groups:
            Record<string, Permission[]> = {};

        permissions.forEach(permission => {

            const domain =
                permission.domain;

            if (!groups[domain]) {

                groups[domain] = [];
            }

            groups[domain].push(permission);
        });

        return Object.keys(groups).map(domain => ({

            domain,

            permissions:
                groups[domain]
        }));
    }

    // =========================
    // ROLE SELECT
    // =========================
    onRoleSelect(
        event: TableRowSelectEvent
    ): void {

        const role = event.data as Role;

        if (!role?.id) {
            return;
        }

        this.selectedRole = role;

        this.loading = true;

        this.service
            .getPermissionsByRole(role.id)
            .subscribe({

                next: (res) => {

                    this.selectedPermissionIds =
                        res.map(
                            x => x.permissionId
                        );

                    this.loading = false;
                },

                error: () => {

                    this.loading = false;
                }
            });
    }

    // =========================
    // TOGGLE PERMISSION
    // =========================
    togglePermission(
        permissionId: string | undefined,
        checked: boolean
    ): void {

        if (!permissionId) {
            return;
        }

        // remove invalid values first
        this.selectedPermissionIds =
            this.selectedPermissionIds.filter(Boolean);

        if (checked) {

            if (!this.selectedPermissionIds.includes(permissionId)) {

                this.selectedPermissionIds = [
                    ...this.selectedPermissionIds,
                    permissionId
                ];
            }

        } else {

            this.selectedPermissionIds =
                this.selectedPermissionIds.filter(
                    id => id !== permissionId
                );
        }

        console.log(
            'SELECTED IDS',
            this.selectedPermissionIds
        );
    }

    // =========================
    // SAVE
    // =========================
    save(): void {
        console.log('SELECTED IDS', this.selectedPermissionIds);
        if (!this.selectedRole?.id) {
            return;
        }

        this.saving = true;

        this.service
            .replaceRolePermissions(
                this.selectedRole.id,
                this.selectedPermissionIds
            )
            .subscribe({

                next: () => {

                    this.toast.success(
                        'Permissions updated successfully'
                    );

                    this.saving = false;
                },

                error: (err) => {

                    console.error(err);

                    this.toast.error(
                        err?.error?.message ||
                        'Failed to update permissions'
                    );

                    this.saving = false;
                }
            });
    }
    reset(): void {

        this.selectedPermissionIds = [];

        this.search = '';
    }

    onSearch(
        value: string
    ): void {

        this.search = value;
    }
}
