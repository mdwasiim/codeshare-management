import { Routes } from '@angular/router';

export const PERMISSIONS_ROUTES = [
    {
        path: '',
        loadComponent: () =>
            import('./pages/permission-list/permission-list.page')
                .then(m => m.PermissionListPage)
    },
    {
        path: 'create',
        loadComponent: () =>
            import('./pages/permission-form/permission-form.page')
                .then(m => m.PermissionFormPage)
    },
    {
        path: ':id',
        loadComponent: () =>
            import('./pages/permission-form/permission-form.page')
                .then(m => m.PermissionFormPage)
    }
];
