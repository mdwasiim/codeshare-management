import { Routes } from '@angular/router';
import { CSMLayout } from '@layout/csm.layout';
import { CSMDashboard } from '@pages/dashboard/csm.dashboard';
import { Documentation } from '@pages/documentation/documentation';
import { CSMLanding } from '@pages/landing/landing';
import { CSMNotfound } from '@pages/notfound/notfound';

export const csmRoutes: Routes = [
    {
        path: '',
        component: CSMLayout,
        children: [
            { path: '', redirectTo: 'dashboard', pathMatch: 'full' },

            { path: 'dashboard', component: CSMDashboard },

            {
                path: 'settings',
                loadChildren: () =>
                    import('@pages/settings/organizations/organizations.routes')
                        .then(m => m.ORGANIZATION_ROUTES)
            },

            { path: 'uikit', loadChildren: () => import('@pages/uikit/uikit.routes') },
            { path: 'documentation', component: Documentation },
            { path: 'pages', loadChildren: () => import('@pages/pages.routes') }
        ]
    },

    { path: 'landing', component: CSMLanding },
    { path: 'auth', loadChildren: () => import('@pages/auth/auth.routes') },
    { path: 'notfound', component: CSMNotfound },
    { path: '**', redirectTo: 'notfound' }
];

