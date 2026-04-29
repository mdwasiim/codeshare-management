import { Routes } from '@angular/router';

export const SETTINGS_ROUTES: Routes = [
    {
        path: 'organizations',
        loadChildren: () =>
            import('./organizations/organization.routes')
                .then(m => m.ORGANIZATION_ROUTES)
    },
    {
        path: 'products',
        loadChildren: () =>
            import('./products/products.routes')
                .then(m => m.PRODUCTS_ROUTES)
    }
];
