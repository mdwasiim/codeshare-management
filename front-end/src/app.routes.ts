import { Routes } from '@angular/router';
import { AppAuthGuard } from '@core/security/guards/app-auth.guard';

export const APP_ROUTES: Routes = [
    {
        path: 'auth',
        loadChildren: () =>
            import('@features/access-management/auth/auth.routes')
                .then(m => m.AUTH_ROUTES)
    },
    {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
    },
    {
        path: '',
        canActivate: [AppAuthGuard],
        loadComponent: () =>
            import('./app/layout/shell/layout.component')
                .then(m => m.LayoutComponent),
        children: [
            {
                path: 'dashboard',
                loadChildren: () =>
                    import('@features/home/dashboard/dashboard.routes')
                        .then(m => m.DASHBOARD_ROUTES)
            },
            {
                path: '',
                loadChildren: () =>
                    import('@features/feature.routes')
                        .then(m => m.FEATURE_ROUTES)
            },
            {
                path: 'unauthorized',
                loadComponent: () =>
                    import('@core/pages/unauthorized/unauthorized.page')
                        .then(m => m.UnauthorizedPage)
            },
            {
                path: 'access-denied',
                loadComponent: () =>
                    import('@core/pages/access-denied/access-denied.component')
                        .then(m => m.AccessDeniedComponent)
            },
            {
                path: 'error',
                loadComponent: () =>
                    import('@core/pages/error/error.component')
                        .then(m => m.ErrorComponent)
            },
            {
                path: 'notfound',
                loadComponent: () =>
                    import('@core/pages/notfound/notfound.page')
                        .then(m => m.CSMNotfound)
            }
        ]
    },
    {
        path: '**',
        redirectTo: '/notfound'
    }
];
