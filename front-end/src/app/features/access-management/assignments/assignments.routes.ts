import { Routes } from '@angular/router';

export const ASSIGNMENTS_ROUTES: Routes = [
    {
        path: 'group-menus',

        loadComponent: () => import('@features/access-management/assignments/group-menus/pages/group-menu.page').then((m) => m.GroupMenuPage)
    },
    {
        path: 'user-groups',

        loadComponent: () => import('@features/access-management/assignments/user-groups/pages/user-group.page').then((m) => m.UserGroupPage)
    },
    {
        path: 'role-permissions',

        loadComponent: () => import('@features/access-management/assignments/role-permissions/pages/role-permission-assignment.page').then((m) => m.RolePermissionAssignmentPage)
    },

    {
        path: 'group-roles',

        loadComponent: () => import('@features/access-management/assignments/group-roles/pages/group-role-assignment.page').then((m) => m.GroupRoleAssignmentPage)
    }
];
