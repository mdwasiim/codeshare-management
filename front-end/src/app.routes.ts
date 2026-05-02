import { Routes } from '@angular/router';
import { AppAuthGuard } from "@core/guards/app-auth.guard";

export const APP_ROUTES: Routes = [

    // ================= PUBLIC =================
    {
        path: 'auth',
        loadChildren: () =>
            import('@features/routes/auth.routes')
                .then(m => m.AUTH_ROUTES)
    },

    // ================= ROOT =================
    {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
    },

    // ================= PROTECTED =================
    {
        path: '',
        canActivate: [AppAuthGuard],
        loadComponent: () =>
            import('./app/layout/shell/layout.component')
                .then(m => m.LayoutComponent),

        children: [

            // ✅ Dashboard (keep as-is)
            {
                path: 'dashboard',
                loadChildren: () =>
                    import('@features/routes/dashboard.routes')
                        .then(m => m.DASHBOARD_ROUTES)
            },

            // 🔥 REPLACE IAM + SETTINGS WITH THIS
            {
                path: '',
                loadChildren: () =>
                    import('@features/feature.routes')
                        .then(m => m.FEATURE_ROUTES)
            },

            // ✅ Not Found
            {
                path: 'notfound',
                loadComponent: () =>
                    import('./app/features/error/pages/notfound/notfound')
                        .then(m => m.CSMNotfound)
            }
        ]
    },

    // ================= FALLBACK =================
    {
        path: '**',
        redirectTo: '/notfound'
    }
];
