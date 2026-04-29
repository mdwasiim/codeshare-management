import { Routes } from '@angular/router';

export const ORGANIZATION_ROUTES: Routes = [
    {
        path: '',
        pathMatch: 'full',   // ✅ IMPORTANT
        loadComponent: () =>
            import('./pages/organization-list/organization-list.page')
                .then(m => m.OrganizationListPage)
    },
    {
        path: 'create',
        loadComponent: () =>
            import('./pages/organization-form/organization-form.page')
                .then(m => m.OrganizationFormPage)
    },
    {
        path: ':id',
        loadComponent: () =>
            import('./pages/organization-form/organization-form.page')
                .then(m => m.OrganizationFormPage)
    }
];
