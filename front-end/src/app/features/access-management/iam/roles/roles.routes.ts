import { Routes } from '@angular/router';

export const ROLES_ROUTES: Routes = [
    {
        path: '',
        loadComponent: () =>
            import('./pages/role-list/role-list.page')
                .then(m => m.RoleListPage)   // ✅ FIXED
    },
    {
        path: 'create',
        loadComponent: () =>
            import('./pages/role-form/role-form.page')
                .then(m => m.RoleFormPage)
    },
    {
        path: ':id',
        loadComponent: () =>
            import('./pages/role-form/role-form.page')
                .then(m => m.RoleFormPage)
    }
];
