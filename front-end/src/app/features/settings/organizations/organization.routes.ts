import { Routes } from '@angular/router';

export const ORGANIZATION_ROUTES: Routes = [
    {
        path: '',
        pathMatch: 'full',   // ✅ IMPORTANT
        loadComponent: () =>
            import('./pages/organizations/organization-list/organization-list.page.ts')
                .then(m => m.OrganizationListPage)
    },
    {
        path: 'create',
        loadComponent: () =>
            import('./pages/organizations/organization-form/organization-form.page.ts')
                .then(m => m.OrganizationFormPage)
    },
    {
        path: ':id',
        loadComponent: () =>
            import('./pages/organizations/organization-form/organization-form.page.ts')
                .then(m => m.OrganizationFormPage)
    }
];
