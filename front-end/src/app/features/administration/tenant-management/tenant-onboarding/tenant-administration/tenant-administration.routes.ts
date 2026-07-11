import { Routes } from '@angular/router';

export const TENANT_ADMINISTRATION_ROUTES: Routes = [
    {
        path: 'tenants',
        loadChildren: () => import('./tenants/tenants.routes').then((m) => m.TENANTS_ROUTES)
    }
];
