import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { forkJoin, of, switchMap } from 'rxjs';

import { AppMenuModel } from '@features/administration/access-management/models/app-menu.model';
import { Group } from '@features/administration/access-management/models/group.model';
import { Permission } from '@features/administration/access-management/models/permission.model';
import { Role } from '@features/administration/access-management/models/role.model';
import { User } from '@features/administration/access-management/models/user.model';
import { GroupMenuService } from '@features/administration/access-management/assignments/group-menus/services/group-menu.service';
import { GroupRoleService } from '@features/administration/access-management/assignments/group-roles/services/group-role.service';
import { RolePermissionService } from '@features/administration/access-management/assignments/role-permissions/services/role-permission.service';
import { UserGroupService } from '@features/administration/access-management/assignments/user-groups/services/user-group.service';
import { MenuManagementService } from '@features/administration/access-management/authorization/menus/services/menu-management.service';
import { PermissionApiService } from '@features/administration/access-management/authorization/permissions/services/permission-api.service';
import { RoleService } from '@features/administration/access-management/identity/roles/services/role.service';
import { UserService } from '@features/administration/access-management/identity/users/services/user.service';
import { GroupService } from '../../services/group.service';
import { AppToastService } from '@services/toast/app-toast.service';

type GroupOperationsTab = 'general' | 'users' | 'roles' | 'menus' | 'permissions' | 'matrix' | 'audit';

interface MenuNode extends AppMenuModel {
    children: MenuNode[];
}

interface PermissionBucket {
    domain: string;
    permissions: Permission[];
}

@Component({
    selector: 'app-group-operations',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterLink],
    templateUrl: './group-operations.page.html',
    styleUrl: './group-operations.page.scss'
})
export class GroupOperationsPage {
    private readonly groupService = inject(GroupService);
    private readonly userService = inject(UserService);
    private readonly roleService = inject(RoleService);
    private readonly menuService = inject(MenuManagementService);
    private readonly permissionService = inject(PermissionApiService);
    private readonly userGroupService = inject(UserGroupService);
    private readonly groupRoleService = inject(GroupRoleService);
    private readonly groupMenuService = inject(GroupMenuService);
    private readonly rolePermissionService = inject(RolePermissionService);
    private readonly toast = inject(AppToastService);

    readonly tabs: { key: GroupOperationsTab; label: string }[] = [
        { key: 'general', label: 'General' },
        { key: 'users', label: 'Users' },
        { key: 'roles', label: 'Roles' },
        { key: 'menus', label: 'Menus' },
        { key: 'permissions', label: 'Permissions' },
        { key: 'matrix', label: 'Permission Matrix' },
        { key: 'audit', label: 'Audit' }
    ];
    readonly matrixActions = ['read', 'create', 'update', 'delete', 'approve', 'export'];

    loading = true;
    saving = false;
    activeTab: GroupOperationsTab = 'general';
    groupSearch = '';
    userSearch = '';
    roleSearch = '';
    menuSearch = '';
    permissionSearch = '';
    selectedRoleForPermissions = '';

    groups: Group[] = [];
    users: User[] = [];
    roles: Role[] = [];
    menus: AppMenuModel[] = [];
    permissions: Permission[] = [];
    selectedGroup: Group | null = null;
    groupUserCounts = new Map<string, number>();

    selectedUserIds = new Set<string>();
    selectedRoleIds = new Set<string>();
    selectedMenuIds = new Set<string>();
    selectedPermissionIds = new Set<string>();
    effectivePermissionIds = new Set<string>();

    private readonly refreshTick = signal(0);

    readonly filteredGroups = computed(() => {
        this.refreshTick();
        const term = this.groupSearch.trim().toLowerCase();
        if (!term) return this.groups;
        return this.groups.filter((group) => [group.code, group.name, group.description].some((value) => (value || '').toLowerCase().includes(term)));
    });

    readonly filteredUsers = computed(() => {
        this.refreshTick();
        const term = this.userSearch.trim().toLowerCase();
        if (!term) return this.users;
        return this.users.filter((user) => [user.username, user.email, user.firstName, user.lastName].some((value) => (value || '').toLowerCase().includes(term)));
    });

    readonly filteredRoles = computed(() => {
        this.refreshTick();
        const term = this.roleSearch.trim().toLowerCase();
        if (!term) return this.roles;
        return this.roles.filter((role) => [role.code, role.name, role.description].some((value) => (value || '').toLowerCase().includes(term)));
    });

    readonly permissionBuckets = computed<PermissionBucket[]>(() => {
        this.refreshTick();
        const term = this.permissionSearch.trim().toLowerCase();
        const filtered = this.permissions.filter((permission) => {
            if (!term) return true;
            return [permission.code, permission.name, permission.domain, permission.action, permission.description].some((value) => (value || '').toLowerCase().includes(term));
        });
        const buckets = new Map<string, Permission[]>();
        filtered.forEach((permission) => {
            const domain = permission.domain || 'general';
            buckets.set(domain, [...(buckets.get(domain) || []), permission]);
        });
        return Array.from(buckets.entries())
            .map(([domain, permissions]) => ({ domain, permissions: permissions.sort((a, b) => (a.action || '').localeCompare(b.action || '')) }))
            .sort((a, b) => a.domain.localeCompare(b.domain));
    });

    readonly menuTree = computed(() => {
        this.refreshTick();
        return this.buildMenuTree(this.menus, this.menuSearch);
    });

    ngOnInit(): void {
        this.loadWorkspace();
    }

    loadWorkspace(): void {
        this.loading = true;
        forkJoin({
            groups: this.groupService.getAll(),
            users: this.userService.getAll(),
            roles: this.roleService.getAll(),
            menus: this.menuService.getAll(),
            permissions: this.permissionService.getAll()
        }).subscribe({
            next: ({ groups, users, roles, menus, permissions }) => {
                this.groups = groups || [];
                this.users = users || [];
                this.roles = roles || [];
                this.menus = menus || [];
                this.permissions = permissions || [];
                this.selectedGroup = this.groups[0] || null;
                this.loading = false;
                this.refreshComputed();
                this.loadGroupUserCounts();
                this.loadSelectedGroupAssignments();
            },
            error: () => {
                this.loading = false;
                this.toast.error('Failed to load group operations data');
            }
        });
    }

    selectGroup(group: Group): void {
        this.selectedGroup = { ...group };
        this.activeTab = 'general';
        this.selectedRoleForPermissions = '';
        this.loadSelectedGroupAssignments();
    }

    loadGroupUserCounts(): void {
        const groupsWithIds = this.groups.filter((group) => !!group.id);
        if (!groupsWithIds.length) return;

        forkJoin(groupsWithIds.map((group) => this.userGroupService.getUsersByGroup(group.id!))).subscribe({
            next: (groupUsers) => {
                this.groupUserCounts = new Map(
                    groupsWithIds.map((group, index) => [group.id!, groupUsers[index]?.length || 0])
                );
                this.refreshComputed();
            }
        });
    }

    loadSelectedGroupAssignments(): void {
        const groupId = this.selectedGroup?.id;
        if (!groupId) return;

        forkJoin({
            users: this.userGroupService.getUsersByGroup(groupId),
            roles: this.groupRoleService.getRolesByGroup(groupId),
            menus: this.groupMenuService.getMenusByGroup(groupId)
        })
            .pipe(
                switchMap(({ users, roles, menus }) => {
                    this.selectedUserIds = new Set((users || []).map((user) => user.id).filter(Boolean) as string[]);
                    this.selectedRoleIds = new Set((roles || []).map((role) => role.id).filter(Boolean) as string[]);
                    this.selectedMenuIds = new Set((menus || []).map((menu) => menu.id).filter(Boolean) as string[]);
                    this.selectedRoleForPermissions = this.selectedRoleForPermissions || Array.from(this.selectedRoleIds)[0] || '';

                    const roleIds = Array.from(this.selectedRoleIds);
                    if (!roleIds.length) return of([]);
                    return forkJoin(roleIds.map((roleId) => this.rolePermissionService.getPermissionsByRole(roleId)));
                })
            )
            .subscribe({
                next: (rolePermissions) => {
                    const ids = rolePermissions.flat().map((item) => item.permissionId).filter(Boolean);
                    this.effectivePermissionIds = new Set(ids);
                    this.loadSelectedRolePermissions();
                    this.refreshComputed();
                },
                error: () => this.toast.error('Failed to load group assignments')
            });
    }

    loadSelectedRolePermissions(): void {
        if (!this.selectedRoleForPermissions) {
            this.selectedPermissionIds = new Set();
            this.refreshComputed();
            return;
        }

        this.rolePermissionService.getPermissionsByRole(this.selectedRoleForPermissions).subscribe({
            next: (items) => {
                this.selectedPermissionIds = new Set((items || []).map((item) => item.permissionId).filter(Boolean));
                this.refreshComputed();
            },
            error: () => this.toast.error('Failed to load role permissions')
        });
    }

    saveGeneral(): void {
        if (!this.selectedGroup?.id) return;
        this.saving = true;
        this.groupService.update(this.selectedGroup.id, this.selectedGroup).subscribe({
            next: (updated) => {
                this.groups = this.groups.map((group) => (group.id === updated.id ? updated : group));
                this.selectedGroup = updated;
                this.saving = false;
                this.toast.success('Group saved');
                this.refreshComputed();
            },
            error: () => {
                this.saving = false;
                this.toast.error('Failed to save group');
            }
        });
    }

    saveUsers(): void {
        const groupId = this.selectedGroup?.id;
        if (!groupId) return;
        this.saving = true;
        this.userGroupService.replaceGroupUsers(groupId, Array.from(this.selectedUserIds)).subscribe({
            next: () => {
                this.saving = false;
                this.toast.success('Group users saved');
                this.groupUserCounts.set(groupId, this.selectedUserIds.size);
                this.refreshComputed();
            },
            error: () => {
                this.saving = false;
                this.toast.error('Failed to save group users');
            }
        });
    }

    saveRoles(): void {
        if (!this.selectedGroup?.id) return;
        this.saving = true;
        this.groupRoleService.replaceGroupRoles(this.selectedGroup.id, Array.from(this.selectedRoleIds)).subscribe({
            next: () => {
                this.saving = false;
                this.toast.success('Group roles saved');
                this.selectedRoleForPermissions = Array.from(this.selectedRoleIds)[0] || '';
                this.loadSelectedGroupAssignments();
            },
            error: () => {
                this.saving = false;
                this.toast.error('Failed to save group roles');
            }
        });
    }

    saveMenus(): void {
        if (!this.selectedGroup?.id) return;
        this.saving = true;
        this.groupMenuService.replaceGroupMenus(this.selectedGroup.id, Array.from(this.selectedMenuIds)).subscribe({
            next: () => {
                this.saving = false;
                this.toast.success('Group menus saved');
            },
            error: () => {
                this.saving = false;
                this.toast.error('Failed to save group menus');
            }
        });
    }

    saveRolePermissions(): void {
        if (!this.selectedRoleForPermissions) return;
        this.saving = true;
        this.rolePermissionService.replaceRolePermissions(this.selectedRoleForPermissions, Array.from(this.selectedPermissionIds)).subscribe({
            next: () => {
                this.saving = false;
                this.toast.success('Role permissions saved');
                this.loadSelectedGroupAssignments();
            },
            error: () => {
                this.saving = false;
                this.toast.error('Failed to save role permissions');
            }
        });
    }

    setActiveTab(tab: GroupOperationsTab): void {
        this.activeTab = tab;
    }

    toggleUser(userId?: string): void {
        this.toggleSetValue(this.selectedUserIds, userId);
    }

    toggleRole(roleId?: string): void {
        this.toggleSetValue(this.selectedRoleIds, roleId);
    }

    toggleMenu(menuId?: string): void {
        this.toggleSetValue(this.selectedMenuIds, menuId);
    }

    togglePermission(permissionId?: string): void {
        this.toggleSetValue(this.selectedPermissionIds, permissionId);
    }

    toggleMatrixPermission(domain: string, action: string): void {
        const permission = this.permissions.find((item) => item.domain === domain && item.action === action);
        this.togglePermission(permission?.id);
    }

    selectAllPermissions(bucket: PermissionBucket): void {
        bucket.permissions.forEach((permission) => {
            if (permission.id) this.selectedPermissionIds.add(permission.id);
        });
        this.refreshComputed();
    }

    clearPermissions(): void {
        this.selectedPermissionIds = new Set();
        this.refreshComputed();
    }

    isUserSelected(userId?: string): boolean {
        return !!userId && this.selectedUserIds.has(userId);
    }

    isRoleSelected(roleId?: string): boolean {
        return !!roleId && this.selectedRoleIds.has(roleId);
    }

    isMenuSelected(menuId?: string): boolean {
        return !!menuId && this.selectedMenuIds.has(menuId);
    }

    isPermissionSelected(permissionId?: string): boolean {
        return !!permissionId && this.selectedPermissionIds.has(permissionId);
    }

    hasMatrixPermission(domain: string, action: string): boolean {
        const permission = this.permissions.find((item) => item.domain === domain && item.action === action);
        return !!permission?.id && this.selectedPermissionIds.has(permission.id);
    }

    hasEffectivePermission(permissionId?: string): boolean {
        return !!permissionId && this.effectivePermissionIds.has(permissionId);
    }

    roleName(roleId: string): string {
        const role = this.roles.find((item) => item.id === roleId);
        return role?.name || role?.code || 'Role';
    }

    userDisplayName(user: User): string {
        return [user.firstName, user.lastName].filter(Boolean).join(' ') || user.username;
    }

    domains(): string[] {
        return Array.from(new Set(this.permissions.map((permission) => permission.domain).filter(Boolean))).sort();
    }

    assignedRoles(): Role[] {
        return this.roles.filter((role) => role.id && this.selectedRoleIds.has(role.id));
    }

    private toggleSetValue(set: Set<string>, id?: string): void {
        if (!id) return;
        if (set.has(id)) {
            set.delete(id);
        } else {
            set.add(id);
        }
        this.refreshComputed();
    }

    private buildMenuTree(menus: AppMenuModel[], search: string): MenuNode[] {
        const term = search.trim().toLowerCase();
        const byParent = new Map<string, AppMenuModel[]>();
        menus.forEach((menu) => {
            const parent = menu.parentCode || '';
            byParent.set(parent, [...(byParent.get(parent) || []), menu]);
        });

        const build = (parentCode = ''): MenuNode[] =>
            (byParent.get(parentCode) || [])
                .sort((a, b) => (a.displayOrder || 0) - (b.displayOrder || 0) || (a.label || '').localeCompare(b.label || ''))
                .map((menu) => ({ ...menu, children: build(menu.code) }))
                .filter((node) => {
                    if (!term) return true;
                    const ownMatch = [node.code, node.label, node.route].some((value) => (value || '').toLowerCase().includes(term));
                    return ownMatch || node.children.length > 0;
                });

        return build();
    }

    refreshComputed(): void {
        this.refreshTick.update((value) => value + 1);
    }
}
