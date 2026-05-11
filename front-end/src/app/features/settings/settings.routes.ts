import { Routes } from '@angular/router';

export const SETTINGS_ROUTES: Routes = [
    {
        path: 'orgs',
        loadChildren: () =>
            import('@features/settings/organizations/organization.routes')
                .then(m => m.ORGANIZATION_ROUTES)
    },
    {
        path: 'products',
        loadChildren: () =>
            import('@features/settings/products/products.routes')
                .then(m => m.PRODUCTS_ROUTES)
    }
];
