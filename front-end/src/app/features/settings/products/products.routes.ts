import { Routes } from '@angular/router';

export const PRODUCTS_ROUTES: Routes = [
    {
        path: '',
        loadComponent: () =>
            import('@features/settings/products/pages/product-list/product-list.page')
                .then(m => m.ProductListPage)
    },
    {
        path: 'create',   // ✅ THIS IS REQUIRED
        loadComponent: () =>
            import('./pages/product-form/product-form.page')
                .then(m => m.ProductFormPage)
    },
    {
        path: ':id',
        loadComponent: () =>
            import('./pages/product-form/product-form.page')
                .then(m => m.ProductFormPage)
    }
];
