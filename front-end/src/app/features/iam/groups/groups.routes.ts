import { Routes } from '@angular/router';

export const GROUPS_ROUTES: Routes = [
    {
        path: '',
        loadComponent: () =>
            import('./pages/group-list/group-list.page')
                .then(m => m.GroupListPage)
    },
    {
        path: 'create',
        loadComponent: () =>
            import('./pages/group-form/group-form.page')
                .then(m => m.GroupFormPage)
    },
    {
        path: ':id',
        loadComponent: () =>
            import('./pages/group-form/group-form.page')
                .then(m => m.GroupFormPage)
    }
];
