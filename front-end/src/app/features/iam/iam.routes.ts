import { Routes } from '@angular/router';

export const IAM_ROUTES: Routes = [
    {
        path: '',
        children: [
            {
                path: 'users',
                loadChildren: () =>
                    import('./users/users.routes')
                        .then(m => m.USERS_ROUTES)
            },
            {
                path: 'roles',
                loadChildren: () =>
                    import('./roles/roles.routes')
                        .then(m => m.ROLES_ROUTES)
            },
            {
                path: 'permissions',
                loadChildren: () =>
                    import('./permissions/permissions.routes')
                        .then(m => m.PERMISSIONS_ROUTES)
            },
            {
                path: 'groups',
                loadChildren: () =>
                    import('./groups/groups.routes')
                        .then(m => m.GROUPS_ROUTES)
            },

            {
                path: '',
                redirectTo: 'users',
                pathMatch: 'full'
            }
        ]
    }
];
