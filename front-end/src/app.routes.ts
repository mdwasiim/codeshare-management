import { Routes } from '@angular/router';
import { AUTHENTICATION_ROUTES } from '@core/auth/authentication.routes';
import { AppAuthGuard } from '@core/security/guards/app-auth.guard';

export const APP_ROUTES: Routes = [
    ...AUTHENTICATION_ROUTES,
    {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
    },
    {
        path: '',
        canActivate: [AppAuthGuard],
        canActivateChild: [AppAuthGuard],
        loadComponent: () => import('./app/layout/shell/layout.component').then((m) => m.LayoutComponent),
        children: [
            {
                path: 'dashboard',
                loadChildren: () => import('@features/home/dashboard/dashboard.routes').then((m) => m.DASHBOARD_ROUTES)
            },
            {
                path: '',
                loadChildren: () => import('@features/feature.routes').then((m) => m.FEATURE_ROUTES)
            },
            {
                path: 'unauthorized',
                loadComponent: () => import('@core/pages/unauthorized/unauthorized.page').then((m) => m.UnauthorizedPage)
            },
            {
                path: 'access-denied',
                loadComponent: () => import('@core/pages/access-denied/access-denied.page').then((m) => m.AccessDeniedPage)
            },
            {
                path: 'error',
                loadComponent: () => import('@core/pages/error/error.page').then((m) => m.ErrorPage)
            },
            {
                path: 'notfound',
                loadComponent: () => import('@core/pages/notfound/notfound.page').then((m) => m.AppNotFoundPage)
            }
        ]
    },
    {
        path: '**',
        redirectTo: '/notfound'
    }
];
