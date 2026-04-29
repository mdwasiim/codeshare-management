import { Routes } from '@angular/router';
import {AppAuthGuard} from "@core/guards/app-auth.guard";

export const APP_ROUTES: Routes = [

    // Public
    {
        path: 'auth',
        loadChildren: () =>
            import('./app/features/auth/auth.routes')
                .then(m => m.AUTH_ROUTES)
    },

    // Root redirect
    {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
    },

    // Protected Layout
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
                    import('./app/features/dashboard/dashboard.routes')
                        .then(m => m.DASHBOARD_ROUTES)
            },
            {
                path: 'iam',
                loadChildren: () =>
                    import('@features/iam/iam.routes')
                        .then(m => m.IAM_ROUTES)
            },
            {
                path: 'settings',
                loadChildren: () =>
                    import('@features/settings/settings.routes')
                        .then(m => m.SETTINGS_ROUTES)
            },

            // ✅ ADD THIS
            {
                path: 'notfound',
                loadComponent: () =>
                    import('./app/features/error/pages/notfound/notfound')
                        .then(m => m.CSMNotfound)
            }
        ]
    },

    // ✅ Redirect unknown routes INTO layout
    {
        path: '**',
        redirectTo: '/notfound'
    }
];
