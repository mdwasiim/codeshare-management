import { Routes } from '@angular/router';

export const SETTINGS_ROUTES: Routes = [

    {
        path: 'products',
        loadChildren: () =>
            import('@features/settings/products/products.routes')
                .then(m => m.PRODUCTS_ROUTES)
    }
];
