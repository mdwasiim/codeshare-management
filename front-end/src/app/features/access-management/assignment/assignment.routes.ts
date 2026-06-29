import { Routes } from '@angular/router';

export const ASSIGNMENT_ROUTES: Routes = [
    {
        path: 'group-menus',

        loadComponent: () => import('@features/access-management/assignment/group-menus/pages/group-menu.page').then((m) => m.GroupMenuPage)
    },
    {
        path: 'user-groups',

        loadComponent: () => import('@features/access-management/assignment/user-group/pages/user-group.page').then((m) => m.UserGroupPage)
    },
    {
        path: 'role-permissions',

        loadComponent: () => import('@features/access-management/assignment/role-permissions/pages/role-permission-assignment.page').then((m) => m.RolePermissionAssignmentPage)
    },

    {
        path: 'group-roles',

        loadComponent: () => import('@features/access-management/assignment/group-roles/pages/group-role-assignment.page').then((m) => m.GroupRoleAssignmentPage)
    }
];
